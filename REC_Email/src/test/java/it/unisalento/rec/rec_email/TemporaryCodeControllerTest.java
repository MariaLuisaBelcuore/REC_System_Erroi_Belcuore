package it.unisalento.rec.rec_email;

import it.unisalento.rec.rec_email.service.TemporaryCodeDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TemporaryCodeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TemporaryCodeDetailsService temporaryCodeDetailsService;

    @Test
    public void testVerification() throws Exception {
        String code = "code";
        String email = "admin@example.com";

        when(temporaryCodeDetailsService.validateCode(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(get("/api/verificationCode/{code}", code)
                        .param("email", email))
                .andExpect(status().isOk());
    }
}
