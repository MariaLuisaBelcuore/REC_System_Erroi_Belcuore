package it.unisalento.rec.rec_wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_wallet.dto.WalletClientDTO;
import it.unisalento.rec.rec_wallet.dto.WalletClientListDTO;
import it.unisalento.rec.rec_wallet.security.CheckJwt;
import it.unisalento.rec.rec_wallet.service.WalletClientDetailsService;
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
public class WalletClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private WalletClientDetailsService walletClientDetailsService;

    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletClientDTO walletClientDTO = new WalletClientDTO();
        walletClientDTO.setId("1");
        walletClientDTO.setNome("name");
        walletClientDTO.setCognome("description");
        walletClientDTO.setClientEmail(email);
        walletClientDTO.setResidualCredit(100);
        walletClientDTO.setCardDeadline("11/28");
        walletClientDTO.setCardNumber("1111");
        walletClientDTO.setCvc("321");

        when(walletClientDetailsService.createWalletClient(any(WalletClientDTO.class))).thenReturn(walletClientDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/wallet/client/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(walletClientDTO)))
                .andExpect(status().isOk());

        verify(walletClientDetailsService, times(1)).createWalletClient(any(WalletClientDTO.class));
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletClientDTO walletClientDTO = new WalletClientDTO();
        walletClientDTO.setId("1");
        walletClientDTO.setNome("name");
        walletClientDTO.setCognome("description");
        walletClientDTO.setClientEmail(email);
        walletClientDTO.setResidualCredit(100);
        walletClientDTO.setCardDeadline("11/28");
        walletClientDTO.setCardNumber("1111");
        walletClientDTO.setCvc("321");

        when(walletClientDetailsService.searchWalletClientByClientEmail(anyString()))
                .thenReturn(walletClientDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/client/search")
                        .header("Authorization", "Bearer " + token)
                        .param("clientEmail", email))
                .andExpect(status().isOk());

        verify(walletClientDetailsService, times(1)).searchWalletClientByClientEmail(anyString());
    }

    @Test
    public void testReadAll() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletClientListDTO walletClientListDTO = new WalletClientListDTO();
        ArrayList<WalletClientDTO> wallets = new ArrayList<>();
        walletClientListDTO.setList(wallets);

        WalletClientDTO walletClientDTO = new WalletClientDTO();
        walletClientDTO.setId("1");
        walletClientDTO.setNome("name");
        walletClientDTO.setCognome("description");
        walletClientDTO.setClientEmail(email);
        walletClientDTO.setResidualCredit(100);
        walletClientDTO.setCardDeadline("11/28");
        walletClientDTO.setCardNumber("1111");
        walletClientDTO.setCvc("321");

        wallets.add(walletClientDTO);

        when(walletClientDetailsService.searchAllWalletClient())
                .thenReturn(walletClientListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/client/searchAll")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(walletClientDetailsService, times(1)).searchAllWalletClient();
    }

    @Test
    public void testDelete() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(walletClientDetailsService.deleteWalletClient(anyString())).thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/wallet/client/delete")
                        .header("Authorization", "Bearer " + token)
                        .param("clientEmail", email))
                .andExpect(status().isOk());

        verify(walletClientDetailsService, times(1)).deleteWalletClient(anyString());
    }

    @Test
    public void testUpdate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        WalletClientDTO walletClientDTO = new WalletClientDTO();
        walletClientDTO.setId("1");
        walletClientDTO.setNome("name");
        walletClientDTO.setCognome("description");
        walletClientDTO.setClientEmail(email);
        walletClientDTO.setResidualCredit(100);
        walletClientDTO.setCardDeadline("11/28");
        walletClientDTO.setCardNumber("1111");
        walletClientDTO.setCvc("321");

        when(walletClientDetailsService.updateWalletClient(any(WalletClientDTO.class))).thenReturn(walletClientDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/wallet/client/update", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(walletClientDTO)))
                .andExpect(status().isOk());

        verify(walletClientDetailsService, times(1)).updateWalletClient(any(WalletClientDTO.class));
    }
}
