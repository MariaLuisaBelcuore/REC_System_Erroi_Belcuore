package it.unisalento.rec.rec_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import it.unisalento.rec.rec_login.dto.UserLoginDTO;
import it.unisalento.rec.rec_login.domain.Client;
import it.unisalento.rec.rec_login.repositories.AdminRepository;
import it.unisalento.rec.rec_login.repositories.ClientRepository;
import it.unisalento.rec.rec_login.repositories.MemberRepository;
import it.unisalento.rec.rec_login.security.JwtUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtilities jwtUtilities;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ClientRepository clientRepository;



    @Test
    public void testCreateAuthenticationToken() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("client@example.com");
        userLoginDTO.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken("client@example.com", "password");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        Client client = new Client();
        client.setEmail("client@example.com");
        when(clientRepository.findClientByEmail(anyString())).thenReturn(client);

        when(jwtUtilities.generateToken(anyString(), anyString())).thenReturn("mockJwt");


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("mockJwt"));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(clientRepository, times(1)).findClientByEmail(anyString());
        verify(jwtUtilities, times(1)).generateToken(anyString(), anyString());
        
    }




}