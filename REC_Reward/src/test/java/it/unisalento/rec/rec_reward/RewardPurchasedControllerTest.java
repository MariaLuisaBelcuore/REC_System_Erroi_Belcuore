package it.unisalento.rec.rec_reward;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_reward.dto.RewardDTO;
import it.unisalento.rec.rec_reward.dto.RewardListDTO;
import it.unisalento.rec.rec_reward.dto.RewardPurchasedDTO;
import it.unisalento.rec.rec_reward.security.CheckJwt;
import it.unisalento.rec.rec_reward.service.RewardPurchasedDetailsService;
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
public class RewardPurchasedControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private RewardPurchasedDetailsService rewardPurchasedDetailsService;

    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        RewardPurchasedDTO rewardPurchasedDTO = new RewardPurchasedDTO();
        rewardPurchasedDTO.setId("1");
        rewardPurchasedDTO.setEmailMember(email);
        rewardPurchasedDTO.setUsed(false);
        rewardPurchasedDTO.setCost(1);

        when(rewardPurchasedDetailsService.createRewardPurchased(any(RewardPurchasedDTO.class))).thenReturn(rewardPurchasedDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reward/purchased/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rewardPurchasedDTO)))
                .andExpect(status().isOk());

        verify(rewardPurchasedDetailsService, times(1)).createRewardPurchased(any(RewardPurchasedDTO.class));
    }


    @Test
    public void testSearchPurchasedByMemberEmail() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

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

        when(rewardPurchasedDetailsService.searchPurchasedByMemberEmail(anyString())).thenReturn(rewardListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reward/purchased/searchPurchased")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(rewardPurchasedDetailsService, times(1)).searchPurchasedByMemberEmail(anyString());
    }

    @Test
    public void testUseRewardPurchased() throws Exception {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        RewardPurchasedDTO rewardPurchasedDTO = new RewardPurchasedDTO();
        rewardPurchasedDTO.setId("1");
        rewardPurchasedDTO.setEmailMember(email);
        rewardPurchasedDTO.setUsed(true);
        rewardPurchasedDTO.setCost(1);

        when(rewardPurchasedDetailsService.useRewardPurchased(anyString())).thenReturn(rewardPurchasedDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/reward/purchased/use/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(rewardPurchasedDetailsService, times(1)).useRewardPurchased(anyString());
    }
}
