package it.unisalento.rec.rec_usermanagement.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_usermanagement.dto.ClientRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.MemberRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.restcontroller.RegistrationController;
import it.unisalento.rec.rec_usermanagement.service.RegistrationDetailsService;
import it.unisalento.rec.rec_usermanagement.dto.AdminRegistrationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RegistrationDetailsService registrationDetailsService;
    @InjectMocks
    private RegistrationController registrationController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    public void testPostAdmin() throws Exception {
        AdminRegistrationDTO adminRegistrationDTO = new AdminRegistrationDTO();
        String code = "validCode";
        adminRegistrationDTO.setId("3");
        adminRegistrationDTO.setNome("Giovanna");
        adminRegistrationDTO.setCognome("Bianchi");
        adminRegistrationDTO.setEmail("giovanna.bianchi@example.com");
        adminRegistrationDTO.setPassword("adminPass123");
        adminRegistrationDTO.setRuolo("ADMIN");

        when(registrationDetailsService.createAdmin(any(AdminRegistrationDTO.class), anyString())).thenReturn(adminRegistrationDTO);

        mockMvc.perform(post("/api/registration/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", code)
                        .content(new ObjectMapper().writeValueAsString(adminRegistrationDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostClient() throws Exception {
        ClientRegistrationDTO clientRegistrationDTO = new ClientRegistrationDTO();
        clientRegistrationDTO.setId("1");
        clientRegistrationDTO.setNome("Mario");
        clientRegistrationDTO.setCognome("Rossi");
        clientRegistrationDTO.setEmail("mario.rossi@example.com");
        clientRegistrationDTO.setPassword("password123");
        clientRegistrationDTO.setRuolo("CLIENT");

        when(registrationDetailsService.createClient(any(ClientRegistrationDTO.class))).thenReturn(clientRegistrationDTO);

        mockMvc.perform(post("/api/registration/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clientRegistrationDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostMember() throws Exception {
        MemberRegistrationDTO memberRegistrationDTO = new MemberRegistrationDTO();
        memberRegistrationDTO.setId("2");
        memberRegistrationDTO.setNome("Luigi");
        memberRegistrationDTO.setCognome("Verdi");
        memberRegistrationDTO.setEmail("luigi.verdi@example.com");
        memberRegistrationDTO.setPassword("securePassword");
        memberRegistrationDTO.setRuolo("MEMBER");

        when(registrationDetailsService.createMember(any(MemberRegistrationDTO.class))).thenReturn(memberRegistrationDTO);

        mockMvc.perform(post("/api/registration/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberRegistrationDTO)))
                .andExpect(status().isOk());
    }
}
