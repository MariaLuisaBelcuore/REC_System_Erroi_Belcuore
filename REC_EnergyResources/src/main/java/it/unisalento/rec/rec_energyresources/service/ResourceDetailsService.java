package it.unisalento.rec.rec_energyresources.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_energyresources.domain.Resource;
import it.unisalento.rec.rec_energyresources.dto.*;
import it.unisalento.rec.rec_energyresources.exceptions.ResourceNotFoundException;
import it.unisalento.rec.rec_energyresources.repositories.ResourceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceDetailsService {
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    RabbitMQService rabbitMQService;

    public ResourceDTO createResource(ResourceDTO resourceDTO) throws JsonProcessingException {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDTO, resource, "id","availability");
        resource.setAvailability(true);
        resourceDTO.setAvailability(true);
        resource.setAvailableTimeOriginary(resourceDTO.getAvailableTime());

        resource = resourceRepository.save(resource);

        ResourceCreditMemberDTO resourceCreditMemberDTO = new ResourceCreditMemberDTO();
        BeanUtils.copyProperties(resourceDTO, resourceCreditMemberDTO);
        resourceCreditMemberDTO.setAdditionalCredit(2);
        rabbitMQService.sendMemberCredit(resourceCreditMemberDTO);

        resourceDTO.setId(resource.getId());
        return resourceDTO;
    }

    public ResourceListDTO searchResourceByMemberEmail(String memberEmail) throws ResourceNotFoundException {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        ArrayList<ResourceDTO> resources = new ArrayList<>();
        resourceListDTO.setList(resources);

        List<Resource> list = resourceRepository.findByMemberEmail(memberEmail);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No resource created");
        }

        for(Resource resource : list){
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(resource,resourceDTO);
            resources.add(resourceDTO);
        }
        return resourceListDTO;
    }

    public ResourceListDTO searchResourceForTask(ResourceRequestDTO resourceRequestDTO) throws ResourceNotFoundException {
        ResourceListDTO resourceListDTO = new ResourceListDTO();
        ArrayList<ResourceDTO> resources = new ArrayList<>();
        resourceListDTO.setList(resources);

        int availableTime = resourceRequestDTO.getAvailableTime();
        String os = resourceRequestDTO.getOs();
        int memory = resourceRequestDTO.getMemory();
        String processorModel = resourceRequestDTO.getProcessorModel();
        float processorVelocity = resourceRequestDTO.getProcessorVelocity();
        boolean availability = true;

        List<Resource> list = resourceRepository.findByAvailableTimeGreaterThanEqualAndProcessorModelAndMemoryGreaterThanEqualAndOsAndProcessorVelocityGreaterThanEqualAndAvailability(availableTime,processorModel,memory,os,processorVelocity,availability);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No appropriate resources available");
        }

        for(Resource resource : list) {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(resource,resourceDTO);
            resources.add(resourceDTO);
        }
        return resourceListDTO;
    }

    public ResourceDTO searchById(String id) throws ResourceNotFoundException {
        ResourceDTO resourceDTO = new ResourceDTO();
        Optional<Resource> optionalResource = resourceRepository.findById(id);

        if (optionalResource.isEmpty()) {
            throw new ResourceNotFoundException("Non-existing resource");
        }

        Resource resource = optionalResource.get();
        BeanUtils.copyProperties(resource, resourceDTO);
        return resourceDTO;
    }

    public String deleteResource(String id) throws ResourceNotFoundException {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("The resource does not exist");
        }
        resourceRepository.deleteById(id);
        return id;
    }

    public String deleteAllResource(String memberEmail) throws ResourceNotFoundException{
        resourceRepository.deleteAllBymemberEmail(memberEmail);
        return memberEmail;
    }

    public ResourceDTO updateResource(String id, ResourceDTO resourceDTO) throws ResourceNotFoundException {
        Optional<Resource> optionalResource = resourceRepository.findById(id);
        if (optionalResource.isEmpty()) {
            throw new ResourceNotFoundException("The resource does not exist");
        }

        Resource resource = optionalResource.get();
        BeanUtils.copyProperties(resourceDTO,resource, "id","availability");

        if(resourceDTO.getAvailableTime()<=0){
            resource.setAvailability(false);
            resourceDTO.setAvailability(false);
        }

        resource = resourceRepository.save(resource);
        resourceDTO.setId(resource.getId());
        return resourceDTO;
    }

    public String updateAvailableTime(String id, int executionTime) throws ResourceNotFoundException {
        Optional<Resource> optionalResource = resourceRepository.findById(id);
        if (optionalResource.isEmpty()) {
            throw new ResourceNotFoundException("The resource does not exist");
        }

        Resource resource = optionalResource.get();
        resource.setAvailableTime(resource.getAvailableTime() - executionTime);

        ResourceDTO resourceDTO = new ResourceDTO();
        BeanUtils.copyProperties(resource, resourceDTO, "id");

        if(resourceDTO.getAvailableTime()<=0){
            resource.setAvailability(false);
            resourceDTO.setAvailability(false);
        }

        String result1 = downloadTask(id);
        String result2 = executeTask(id);

        resource = resourceRepository.save(resource);
        resourceDTO.setId(resource.getId());

        return result1 + "\n" + result2;
    }

    public String downloadTask(String id) {
        try {
            URL url = new URL("https://raw.githubusercontent.com/MariaLuisaBelcuore/REC_Support/main/script.sh");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder scriptContent = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                scriptContent.append(inputLine).append("\n");
            }
            in.close();

            File scriptFile = new File("/tmp/scriptFor" + id + ".sh");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(scriptFile))) {
                writer.write(scriptContent.toString());
            }

            boolean permissionGranted = scriptFile.setExecutable(true, false);
            if (!permissionGranted) {
                return "Errore durante la concessione dei permessi di esecuzione allo script.";
            }

            return "Script scaricato con successo e pronto per essere eseguito. Loading...";
        } catch (Exception e) {
            return "Errore durante il download dello script: " + e.getMessage();
        }
    }

    public String executeTask(String id){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "/tmp/scriptFor" + id + ".sh");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "Script eseguito con successo:\n" + output;
            } else {
                return "Errore nell'esecuzione dello script:\n" + output;
            }
        } catch (Exception e) {
            return "Errore durante l'esecuzione dello script: " + e.getMessage();
        }
    }
}
