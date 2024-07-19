package it.unisalento.rec.rec_email;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_email.domain.Email;
import it.unisalento.rec.rec_email.domain.StatusEmail;
import it.unisalento.rec.rec_email.domain.TemporaryCode;
import it.unisalento.rec.rec_email.dto.EmailDTO;
import it.unisalento.rec.rec_email.dto.EmailListDTO;
import it.unisalento.rec.rec_email.service.EmailDetailsService;
import it.unisalento.rec.rec_email.security.CheckJwt;
import it.unisalento.rec.rec_email.service.TemporaryCodeDetailsService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private EmailDetailsService emailDetailsService;
    @MockBean
    private TemporaryCodeDetailsService temporaryCodeDetailsService;

    @Test
    public void testSend() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId("1");
        emailDTO.setEmailFrom("recsystem2024@example.com");
        emailDTO.setEmailTo("client@example.com");
        emailDTO.setText("text");
        emailDTO.setSubject("example");
        emailDTO.setOwnerRef("owner");

        Email email = new Email();
        email.setId("1");
        email.setEmailFrom("recsystem2024@example.com");
        email.setEmailTo("client@example.com");
        email.setText("text");
        email.setSubject("example");
        email.setOwnerRef("owner");
        email.setStatusEmail(StatusEmail.SENT);

        when(emailDetailsService.createEmail(any(EmailDTO.class))).thenReturn(email);
        doNothing().when(emailDetailsService).sendEmail(any(Email.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/email/send")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(emailDTO)))
                .andExpect(status().isCreated());

        verify(emailDetailsService, times(1)).createEmail(any(EmailDTO.class));
        verify(emailDetailsService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    public void testSendInvitation() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId("1");
        emailDTO.setEmailFrom("recsystem2024@example.com");
        emailDTO.setEmailTo("client@example.com");
        emailDTO.setText("text");
        emailDTO.setSubject("example");
        emailDTO.setOwnerRef("owner");

        Email email = new Email();
        email.setId("1");
        email.setEmailFrom("recsystem2024@example.com");
        email.setEmailTo("client@example.com");
        email.setText("text");
        email.setSubject("example");
        email.setOwnerRef("owner");
        email.setStatusEmail(StatusEmail.SENT);

        TemporaryCode tempCode = new TemporaryCode();
        tempCode.setCode("code");
        tempCode.setEmail("admin@example.com");
        tempCode.setUsed(false);

        when(emailDetailsService.createEmail(any(EmailDTO.class))).thenReturn(email);
        when(temporaryCodeDetailsService.generateCode(anyString())).thenReturn(tempCode);
        doNothing().when(emailDetailsService).sendEmail(any(Email.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/email/sendInvitation")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(emailDTO)))
                .andExpect(status().isCreated());

        verify(emailDetailsService, times(1)).createEmail(any(EmailDTO.class));
        verify(temporaryCodeDetailsService, times(1)).generateCode(anyString());
        verify(emailDetailsService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    public void testSearchAll() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        EmailListDTO emailListDTO = new EmailListDTO();
        ArrayList<EmailDTO> emails = new ArrayList<>();
        emailListDTO.setList(emails);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId("1");
        emailDTO.setEmailFrom("recsystem2024@example.com");
        emailDTO.setEmailTo("client@example.com");
        emailDTO.setText("text");
        emailDTO.setSubject("example");
        emailDTO.setOwnerRef("owner");

        emails.add(emailDTO);

        when(emailDetailsService.searchAll())
                .thenReturn(emailListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/email/searchAll")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(emailDetailsService, times(1)).searchAll();
    }

    @Test
    public void testSearchAllEmailTo() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        EmailListDTO emailListDTO = new EmailListDTO();
        ArrayList<EmailDTO> emails = new ArrayList<>();
        emailListDTO.setList(emails);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId("1");
        emailDTO.setEmailFrom("recsystem2024@example.com");
        emailDTO.setEmailTo("client@example.com");
        emailDTO.setText("text");
        emailDTO.setSubject("example");
        emailDTO.setOwnerRef("owner");

        emails.add(emailDTO);

        when(emailDetailsService.searchAllEmailTo(anyString()))
                .thenReturn(emailListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/email/searchAllEmailTo")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("emailTo", email))
                .andExpect(status().isOk());

        verify(emailDetailsService, times(1)).searchAllEmailTo(anyString());
    }

    @Test
    public void testSearch() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId("1");
        emailDTO.setEmailFrom("recsystem2024@example.com");
        emailDTO.setEmailTo("client@example.com");
        emailDTO.setText("text");
        emailDTO.setSubject("example");
        emailDTO.setOwnerRef("owner");

        when(emailDetailsService.search(anyString()))
                .thenReturn(emailDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/email/search/{id}", "1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(emailDetailsService, times(1)).search(anyString());
    }

    @Test
    public void testDelete() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(emailDetailsService.deleteEmail(anyString()))
                .thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/email/delete/{id}", "1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        verify(emailDetailsService, times(1)).deleteEmail(anyString());
    }

    @Test
    public void testDeleteAll() throws Exception {
        String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoiaXNhOTliY3NAZ21haWwuY29tIiwiaWF0IjoxNzE5NTEyMzA0LCJleHAiOjE3NTEwNDgzMDR9.DeWJX6X4xCWen_8TL_vBUwtHa3fN58yWxRCd4Y_dSVE";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(emailDetailsService.deleteAllEmailTo(anyString()))
                .thenReturn(email);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/email/deleteAllEmailTo")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("emailTo", email))
                .andExpect(status().isOk());

        verify(emailDetailsService, times(1)).deleteAllEmailTo(anyString());
    }
}
