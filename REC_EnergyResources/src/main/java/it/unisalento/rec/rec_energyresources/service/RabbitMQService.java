package it.unisalento.rec.rec_energyresources.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_energyresources.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_energyresources.domain.Resource;
import it.unisalento.rec.rec_energyresources.dto.InfoResourceDTO;
import it.unisalento.rec.rec_energyresources.dto.ResourceCreditMemberDTO;
import it.unisalento.rec.rec_energyresources.dto.ResourceDTO;
import it.unisalento.rec.rec_energyresources.exceptions.ResourceNotFoundException;
import it.unisalento.rec.rec_energyresources.repositories.ResourceRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class RabbitMQService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DELETE_USER_RESOURCE)
    public void DeleteUser(String email) throws ResourceNotFoundException {
        List<Resource> list = resourceRepository.findByMemberEmail(email);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No resource created");
        }

        for(Resource resource : list){
            resource.setAvailability(false);
            resourceRepository.save(resource);
        }
    }

    public void sendMemberCredit(ResourceCreditMemberDTO resourceCreditMemberDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(resourceCreditMemberDTO);
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_CREDIT_MEMBER, "", jsonMessage);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PERFORMANCE_MEMBER)
    public String infoResourceForPerformance(String email) throws ResourceNotFoundException, JsonProcessingException {
        List<Resource> resources = resourceRepository.findByMemberEmail(email);

        float totalEnergyAvailable = 0;
        float totalEnergy = 0;

        for (Resource resource : resources) {
            totalEnergyAvailable += (resource.getAvailableTime() * resource.getkWh());
            totalEnergy += (resource.getAvailableTimeOriginary() * resource.getkWh());
        }

        InfoResourceDTO infoResourceDTO = new InfoResourceDTO();
        infoResourceDTO.setTotalEnergy(totalEnergy);
        infoResourceDTO.setTotalEnergyAvailable(totalEnergyAvailable);
        return objectMapper.writeValueAsString(infoResourceDTO);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PERFORMANCE_ADMIN_RESOURCES)
    public String infoResourceForPerformanceForAdmin(String message) throws ResourceNotFoundException, JsonProcessingException {
        List<Resource> resources = resourceRepository.findAll();

        float totalEnergyAvailable = 0;
        float totalEnergy = 0;

        for (Resource resource : resources) {
            totalEnergyAvailable += (resource.getAvailableTime() * resource.getkWh());
            totalEnergy += (resource.getAvailableTimeOriginary() * resource.getkWh());
        }
        InfoResourceDTO infoResourceDTO = new InfoResourceDTO();
        infoResourceDTO.setTotalEnergy(totalEnergy);
        infoResourceDTO.setTotalEnergyAvailable(totalEnergyAvailable);
        return objectMapper.writeValueAsString(infoResourceDTO);
    }
}
