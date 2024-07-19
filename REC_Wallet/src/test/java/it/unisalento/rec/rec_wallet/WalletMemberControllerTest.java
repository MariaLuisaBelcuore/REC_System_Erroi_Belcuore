package it.unisalento.rec.rec_wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_wallet.dto.WalletMemberDTO;
import it.unisalento.rec.rec_wallet.dto.WalletMemberListDTO;
import it.unisalento.rec.rec_wallet.security.CheckJwt;
import it.unisalento.rec.rec_wallet.service.WalletMemberDetailsService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private WalletMemberDetailsService walletMemberDetailsService;

    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
        walletMemberDTO.setId("1");
        walletMemberDTO.setNome("name");
        walletMemberDTO.setCognome("description");
        walletMemberDTO.setMemberEmail(email);
        walletMemberDTO.setResidualCredit(100);

        when(walletMemberDetailsService.createWalletMember(any(WalletMemberDTO.class))).thenReturn(walletMemberDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/wallet/member/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(walletMemberDTO)))
                .andExpect(status().isOk());

        verify(walletMemberDetailsService, times(1)).createWalletMember(any(WalletMemberDTO.class));
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
        walletMemberDTO.setId("1");
        walletMemberDTO.setNome("name");
        walletMemberDTO.setCognome("description");
        walletMemberDTO.setMemberEmail(email);
        walletMemberDTO.setResidualCredit(100);

        when(walletMemberDetailsService.searchWalletMemberByMemberEmail(anyString()))
                .thenReturn(walletMemberDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/member/search")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(walletMemberDetailsService, times(1)).searchWalletMemberByMemberEmail(anyString());
    }

    @Test
    public void testReadAll() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletMemberListDTO walletMemberListDTO = new WalletMemberListDTO();
        ArrayList<WalletMemberDTO> wallets = new ArrayList<>();
        walletMemberListDTO.setList(wallets);

        WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
        walletMemberDTO.setId("1");
        walletMemberDTO.setNome("name");
        walletMemberDTO.setCognome("description");
        walletMemberDTO.setMemberEmail(email);
        walletMemberDTO.setResidualCredit(100);

        wallets.add(walletMemberDTO);

        when(walletMemberDetailsService.searchAllWalletMember())
                .thenReturn(walletMemberListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/member/searchAll")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(walletMemberDetailsService, times(1)).searchAllWalletMember();
    }

    @Test
    public void testDelete() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(walletMemberDetailsService.deleteWalletMember(anyString())).thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/wallet/member/delete")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(walletMemberDetailsService, times(1)).deleteWalletMember(anyString());
    }

    @Test
    public void testUpdate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
        walletMemberDTO.setId("1");
        walletMemberDTO.setNome("name");
        walletMemberDTO.setCognome("description");
        walletMemberDTO.setMemberEmail(email);
        walletMemberDTO.setResidualCredit(100);

        when(walletMemberDetailsService.updateWalletMember(any(WalletMemberDTO.class))).thenReturn(walletMemberDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/wallet/member/update", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(walletMemberDTO)))
                .andExpect(status().isOk());

        verify(walletMemberDetailsService, times(1)).updateWalletMember(any(WalletMemberDTO.class));
    }
}
