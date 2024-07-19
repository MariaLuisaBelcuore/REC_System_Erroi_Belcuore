package it.unisalento.rec.rec_reward;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_reward.dto.RewardDTO;
import it.unisalento.rec.rec_reward.dto.RewardListDTO;
import it.unisalento.rec.rec_reward.security.CheckJwt;
import it.unisalento.rec.rec_reward.service.RewardDetailsService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private RewardDetailsService rewardDetailsService;


    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setId("1");
        rewardDTO.setName("name");
        rewardDTO.setDescription("description");
        rewardDTO.setCost(1);

        when(rewardDetailsService.createReward(any(RewardDTO.class))).thenReturn(rewardDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reward/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rewardDTO)))
                .andExpect(status().isOk());

        verify(rewardDetailsService, times(1)).createReward(any(RewardDTO.class));
    }

    @Test
    public void testSearch() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());


        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setId("1");
        rewardDTO.setName("name");
        rewardDTO.setDescription("description");
        rewardDTO.setCost(1);

        when(rewardDetailsService.search(anyString())).thenReturn(rewardDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reward/search/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(rewardDetailsService, times(1)).search(anyString());
    }

    @Test
    public void testSearchAll() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        RewardListDTO rewardListDTO = new RewardListDTO();
        ArrayList<RewardDTO> rewards = new ArrayList<>();
        rewardListDTO.setList(rewards);

        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setId("1");
        rewardDTO.setName("name");
        rewardDTO.setDescription("description");
        rewardDTO.setCost(1);

        rewards.add(rewardDTO);

        when(rewardDetailsService.searchAll()).thenReturn(rewardListDTO);
        when(rewardDetailsService.searchByCriteria(anyString())).thenReturn(rewardListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reward/searchAll")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(rewardDetailsService, times(1)).searchAll();
        verify(rewardDetailsService, times(0)).searchByCriteria(anyString());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reward/searchAll")
                        .header("Authorization", "Bearer " + token)
                        .param("searchTerm", "term"))
                .andExpect(status().isOk());

        verify(rewardDetailsService, times(1)).searchByCriteria("term");
    }

    @Test
    public void testDelete() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(rewardDetailsService.deleteReward(anyString())).thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/reward/delete/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(rewardDetailsService, times(1)).deleteReward(anyString());
    }

    @Test
    public void testUpdate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        RewardDTO rewardDTO = new RewardDTO();
        rewardDTO.setId("1");
        rewardDTO.setName("name");
        rewardDTO.setDescription("description");
        rewardDTO.setCost(1);

        when(rewardDetailsService.updateReward(anyString(), any(RewardDTO.class))).thenReturn(rewardDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/reward/update/{id}", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rewardDTO)))
                .andExpect(status().isOk());

        verify(rewardDetailsService, times(1)).updateReward(anyString(), any(RewardDTO.class));
    }
}

