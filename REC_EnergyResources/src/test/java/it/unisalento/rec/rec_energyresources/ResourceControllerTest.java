package it.unisalento.rec.rec_energyresources;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_energyresources.dto.ResourceDTO;
import it.unisalento.rec.rec_energyresources.dto.ResourceListDTO;
import it.unisalento.rec.rec_energyresources.dto.ResourceRequestDTO;
import it.unisalento.rec.rec_energyresources.security.CheckJwt;
import it.unisalento.rec.rec_energyresources.service.ResourceDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckJwt checkJwt;

    @MockBean
    private ResourceDetailsService resourceDetailsService;

    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setDescription("Description1");
        resourceDTO.setOwner("Owner1");
        resourceDTO.setMemberEmail(email);
        resourceDTO.setTypeEnergy("Solar");
        resourceDTO.setAvailableTime(10);
        resourceDTO.setkWh(100.5f);
        resourceDTO.setOs("Windows");
        resourceDTO.setMemory(16);
        resourceDTO.setProcessorModel("Intel i7");
        resourceDTO.setProcessorVelocity(3.5f);
        resourceDTO.setAvailability(true);

        when(resourceDetailsService.createResource(any(ResourceDTO.class))).thenReturn(resourceDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/resource/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resourceDTO)))
                .andExpect(status().isOk());

        verify(resourceDetailsService, times(1)).createResource(any(ResourceDTO.class));
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());


        ResourceListDTO resourceListDTO = new ResourceListDTO();
        ArrayList<ResourceDTO> resources = new ArrayList<>();
        resourceListDTO.setList(resources);

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setDescription("Description1");
        resourceDTO.setOwner("Owner1");
        resourceDTO.setMemberEmail(email);
        resourceDTO.setTypeEnergy("Solar");
        resourceDTO.setAvailableTime(10);
        resourceDTO.setkWh(100.5f);
        resourceDTO.setOs("Windows");
        resourceDTO.setMemory(16);
        resourceDTO.setProcessorModel("Intel i7");
        resourceDTO.setProcessorVelocity(3.5f);
        resourceDTO.setAvailability(true);

        resources.add(resourceDTO);

        when(resourceDetailsService.searchResourceByMemberEmail(anyString())).thenReturn(resourceListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/resource/searchAll")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(resourceDetailsService, times(1)).searchResourceByMemberEmail(anyString());
    }

    @Test
    public void testReadOne() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";


        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setDescription("Description1");
        resourceDTO.setOwner("Owner1");
        resourceDTO.setMemberEmail(email);
        resourceDTO.setTypeEnergy("Solar");
        resourceDTO.setAvailableTime(10);
        resourceDTO.setkWh(100.5f);
        resourceDTO.setOs("Windows");
        resourceDTO.setMemory(16);
        resourceDTO.setProcessorModel("Intel i7");
        resourceDTO.setProcessorVelocity(3.5f);
        resourceDTO.setAvailability(true);

        when(resourceDetailsService.searchById(anyString())).thenReturn(resourceDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/resource/search/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(resourceDetailsService, times(1)).searchById(anyString());
    }

    @Test
    public void testReadForTask() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6Im1hcmNvcmF0YW5vMTlAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzIyLCJleHAiOjE3NTEwNDgzMjJ9.L3BWvaKL2IphiXyW0gDdD5gt-tT6ioDrgFi98nfXetQ";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO();
        resourceRequestDTO.setAvailableTime(10);
        resourceRequestDTO.setOs("Windows");
        resourceRequestDTO.setMemory(16);
        resourceRequestDTO.setProcessorModel("Intel i7");
        resourceRequestDTO.setProcessorVelocity(3.5f);


        ResourceListDTO resourceListDTO = new ResourceListDTO();
        ArrayList<ResourceDTO> resources = new ArrayList<>();
        resourceListDTO.setList(resources);

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setDescription("Description1");
        resourceDTO.setOwner("Owner1");
        resourceDTO.setMemberEmail(email);
        resourceDTO.setTypeEnergy("Solar");
        resourceDTO.setAvailableTime(10);
        resourceDTO.setkWh(100.5f);
        resourceDTO.setOs("Windows");
        resourceDTO.setMemory(16);
        resourceDTO.setProcessorModel("Intel i7");
        resourceDTO.setProcessorVelocity(3.5f);
        resourceDTO.setAvailability(true);

        resources.add(resourceDTO);

        when(resourceDetailsService.searchResourceForTask(any(ResourceRequestDTO.class))).thenReturn(resourceListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/resource/searchAllforTask")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resourceRequestDTO)))
                .andExpect(status().isOk());

        verify(resourceDetailsService, times(1)).searchResourceForTask(any(ResourceRequestDTO.class));
    }

    @Test
    public void testDelete() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(resourceDetailsService.deleteResource(anyString())).thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/resource/delete/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(resourceDetailsService, times(1)).deleteResource(anyString());
    }

    @Test
    public void testUpdate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setDescription("Updated Description");
        resourceDTO.setOwner("Updated Owner");
        resourceDTO.setMemberEmail(email);
        resourceDTO.setTypeEnergy("Wind");
        resourceDTO.setAvailableTime(20);
        resourceDTO.setkWh(200.5f);
        resourceDTO.setOs("Linux");
        resourceDTO.setMemory(32);
        resourceDTO.setProcessorModel("AMD Ryzen 7");
        resourceDTO.setProcessorVelocity(4.0f);
        resourceDTO.setAvailability(false);

        when(resourceDetailsService.updateResource(anyString(), any(ResourceDTO.class))).thenReturn(resourceDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/resource/update/{id}", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resourceDTO)))
                .andExpect(status().isOk());

        verify(resourceDetailsService, times(1)).updateResource(anyString(), any(ResourceDTO.class));
    }
}


