package it.unisalento.rec.rec_payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_payment.dto.MemberPaymentDTO;
import it.unisalento.rec.rec_payment.dto.MemberPaymentListDTO;
import it.unisalento.rec.rec_payment.security.CheckJwt;
import it.unisalento.rec.rec_payment.service.MemberPaymentDetailsService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberPaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private MemberPaymentDetailsService memberPaymentDetailsService;

    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "membert@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        MemberPaymentDTO memberPaymentDTO = new MemberPaymentDTO();
        memberPaymentDTO.setId("1");
        memberPaymentDTO.setCausal("Test Causal");
        memberPaymentDTO.setAmount(100);
        memberPaymentDTO.setMemberEmail(email);
        memberPaymentDTO.setPaymentorcredit("payment");

        when(memberPaymentDetailsService.createMemberPayment(any(MemberPaymentDTO.class))).thenReturn(memberPaymentDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/payment/member/createPayment")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberPaymentDTO)))
                .andExpect(status().isOk());

        verify(memberPaymentDetailsService, times(1)).createMemberPayment(any(MemberPaymentDTO.class));
    }

    @Test
    public void testReadAll() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        MemberPaymentListDTO memberPaymentListDTO = new MemberPaymentListDTO();
        ArrayList<MemberPaymentDTO> payments = new ArrayList<>();
        memberPaymentListDTO.setList(payments);

        MemberPaymentDTO memberPaymentDTO = new MemberPaymentDTO();
        memberPaymentDTO.setId("1");
        memberPaymentDTO.setCausal("Test Causal");
        memberPaymentDTO.setAmount(100);
        memberPaymentDTO.setMemberEmail(email);
        memberPaymentDTO.setPaymentorcredit("payment");

        payments.add(memberPaymentDTO);

        when(memberPaymentDetailsService.searchMemberPaymentByMemberEmail(anyString()))
                .thenReturn(memberPaymentListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/payment/member/searchAll")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(memberPaymentDetailsService, times(1)).searchMemberPaymentByMemberEmail(anyString());
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6ImlwcGF6aW8uYmVsY3VvcmVAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEwODY1LCJleHAiOjE3NTEwNDY4NjV9.c_GvsGhNEet7UmuTm52tNnXqwxKsi-_o9qIy9NCMatM";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        MemberPaymentDTO memberPaymentDTO = new MemberPaymentDTO();
        memberPaymentDTO.setId("1");
        memberPaymentDTO.setCausal("Test Causal");
        memberPaymentDTO.setAmount(100);
        memberPaymentDTO.setMemberEmail(email);
        memberPaymentDTO.setPaymentorcredit("payment");

        when(memberPaymentDetailsService.searchMemberPaymentById(anyString()))
                .thenReturn(memberPaymentDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/payment/member/search/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(memberPaymentDetailsService, times(1)).searchMemberPaymentById(anyString());
    }

    @Test
    public void testDelete() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(memberPaymentDetailsService.deleteMemberPayment(anyString()))
                .thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/payment/member/delete/{id}", "1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(memberPaymentDetailsService, times(1)).deleteMemberPayment(anyString());
    }
}

