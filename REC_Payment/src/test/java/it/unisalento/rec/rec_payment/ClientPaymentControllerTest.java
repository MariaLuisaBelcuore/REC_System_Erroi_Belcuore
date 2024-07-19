package it.unisalento.rec.rec_payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_payment.dto.ClientPaymentDTO;
import it.unisalento.rec.rec_payment.dto.ClientPaymentListDTO;
import it.unisalento.rec.rec_payment.security.CheckJwt;
import it.unisalento.rec.rec_payment.service.ClientPaymentDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientPaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private ClientPaymentDetailsService clientPaymentDetailsService;

    @Test
    public void testCreate() throws Exception {
        String clientToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6Im1hcmNvcmF0YW5vMTlAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzIyLCJleHAiOjE3NTEwNDgzMjJ9.L3BWvaKL2IphiXyW0gDdD5gt-tT6ioDrgFi98nfXetQ";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
        clientPaymentDTO.setId("1");
        clientPaymentDTO.setCausal("Test Causal");
        clientPaymentDTO.setAmount(100.0f);
        clientPaymentDTO.setClientEmail(email);

        when(clientPaymentDetailsService.createClientPayment(any(ClientPaymentDTO.class)))
                .thenReturn(clientPaymentDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/payment/client/create")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clientPaymentDTO)))
                .andExpect(status().isOk());

        verify(clientPaymentDetailsService, times(1)).createClientPayment(any(ClientPaymentDTO.class));
    }

    @Test
    public void testReadAll() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ClientPaymentListDTO clientPaymentListDTO = new ClientPaymentListDTO();
        ArrayList<ClientPaymentDTO> payments = new ArrayList<>();
        clientPaymentListDTO.setList(payments);

        ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
        clientPaymentDTO.setId("1");
        clientPaymentDTO.setClientEmail("client@example.com");
        clientPaymentDTO.setCausal("casual");
        clientPaymentDTO.setAmount(1);
        clientPaymentDTO.setEmissionDate(LocalDateTime.now());

        payments.add(clientPaymentDTO);

        when(clientPaymentDetailsService.searchAll())
                .thenReturn(clientPaymentListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/payment/client/searchAll")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(clientPaymentDetailsService, times(1)).searchAll();
    }

    @Test
    public void testReadAllOfClient() throws Exception {
        String clientToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6Im1hcmNvcmF0YW5vMTlAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzIyLCJleHAiOjE3NTEwNDgzMjJ9.L3BWvaKL2IphiXyW0gDdD5gt-tT6ioDrgFi98nfXetQ";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        ClientPaymentListDTO clientPaymentListDTO = new ClientPaymentListDTO();
        ArrayList<ClientPaymentDTO> payments = new ArrayList<>();
        clientPaymentListDTO.setList(payments);

        ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
        clientPaymentDTO.setId("1");
        clientPaymentDTO.setClientEmail(email);
        clientPaymentDTO.setCausal("casual");
        clientPaymentDTO.setAmount(1);
        clientPaymentDTO.setEmissionDate(LocalDateTime.now());

        payments.add(clientPaymentDTO);

        when(clientPaymentDetailsService.searchClientPaymentByClientEmail(anyString()))
                .thenReturn(clientPaymentListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/payment/client/searchAllByClient")
                        .header("Authorization", "Bearer " + clientToken)
                        .param("clientEmail", email))
                .andExpect(status().isOk());

        verify(clientPaymentDetailsService, times(1)).searchClientPaymentByClientEmail(anyString());
    }

    @Test
    public void testRead() throws Exception {
        String clientToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6Im1hcmNvcmF0YW5vMTlAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzIyLCJleHAiOjE3NTEwNDgzMjJ9.L3BWvaKL2IphiXyW0gDdD5gt-tT6ioDrgFi98nfXetQ";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());


        ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
        clientPaymentDTO.setId("1");
        clientPaymentDTO.setCausal("Test Causal");
        clientPaymentDTO.setEmissionDate(LocalDateTime.now());
        clientPaymentDTO.setAmount(100.0f);
        clientPaymentDTO.setClientEmail("client@example.com");

        when(clientPaymentDetailsService.searchClientPaymentById(anyString()))
                .thenReturn(clientPaymentDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/payment/client/search/{id}", "1")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk());

        verify(clientPaymentDetailsService, times(1)).searchClientPaymentById(anyString());
    }

    @Test
    public void testDelete() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(clientPaymentDetailsService.deleteClientPayment(anyString()))
                .thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/payment/client/delete/{id}", "1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(clientPaymentDetailsService, times(1)).deleteClientPayment(anyString());
    }
}

