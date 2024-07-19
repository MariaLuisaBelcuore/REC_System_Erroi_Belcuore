package it.unisalento.rec.rec_usermanagement.ServiceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_usermanagement.domain.Admin;
import it.unisalento.rec.rec_usermanagement.domain.Client;
import it.unisalento.rec.rec_usermanagement.domain.Member;
import it.unisalento.rec.rec_usermanagement.dto.AdminRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.ClientRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.MemberRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.RequestVerifyCodeDTO;
import it.unisalento.rec.rec_usermanagement.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_usermanagement.repositories.AdminRepository;
import it.unisalento.rec.rec_usermanagement.repositories.ClientRepository;
import it.unisalento.rec.rec_usermanagement.repositories.MemberRepository;
import it.unisalento.rec.rec_usermanagement.service.RabbitMQService;
import it.unisalento.rec.rec_usermanagement.service.RegistrationDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationDetailsServiceTest {
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private RabbitMQService rabbitMQService;
    @Autowired
    private RegistrationDetailsService registrationDetailsService;

    @Test
    public void testCreateClient() throws JsonProcessingException, OperationNotPermittedException {
        ClientRegistrationDTO dto = new ClientRegistrationDTO();
        dto.setEmail("client@example.com");
        dto.setPassword("password");

        when(clientRepository.existsByEmail(anyString())).thenReturn(false);
        when(adminRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);

        Client client = new Client();
        client.setId("1");
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(rabbitMQService).sendClientCreated(any(ClientRegistrationDTO.class));

        ClientRegistrationDTO result = registrationDetailsService.createClient(dto);

        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getPassword()).isNull();

        verify(clientRepository, times(1)).existsByEmail(anyString());
        verify(adminRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(rabbitMQService, times(1)).sendClientCreated(any(ClientRegistrationDTO.class));
    }


    @Test
    public void testCreateMember() throws JsonProcessingException, OperationNotPermittedException {
        MemberRegistrationDTO dto = new MemberRegistrationDTO();
        dto.setEmail("member@example.com");
        dto.setPassword("password");

        when(clientRepository.existsByEmail(anyString())).thenReturn(false);
        when(adminRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);

        Member member = new Member();
        member.setId("1");
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        doNothing().when(rabbitMQService).sendMemberCreated(any(MemberRegistrationDTO.class));

        MemberRegistrationDTO result = registrationDetailsService.createMember(dto);

        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getPassword()).isNull();

        verify(clientRepository, times(1)).existsByEmail(anyString());
        verify(adminRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(rabbitMQService, times(1)).sendMemberCreated(any(MemberRegistrationDTO.class));
    }

    @Test
    public void testCreateAdmin() throws JsonProcessingException, OperationNotPermittedException {
        AdminRegistrationDTO dto = new AdminRegistrationDTO();
        dto.setEmail("admin@example.com");
        dto.setPassword("password");

        when(rabbitMQService.sendVerifyAdminCode(any(RequestVerifyCodeDTO.class))).thenReturn(true);
        when(clientRepository.existsByEmail(anyString())).thenReturn(false);
        when(adminRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);

        Admin admin = new Admin();
        admin.setId("1");
        String code = "code";
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        doNothing().when(rabbitMQService).sendAdminCreated(any(AdminRegistrationDTO.class));

        AdminRegistrationDTO result = registrationDetailsService.createAdmin(dto, code);

        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());
        assertThat(result.getPassword()).isNull();

        verify(rabbitMQService, times(1)).sendVerifyAdminCode(any(RequestVerifyCodeDTO.class));
        verify(clientRepository, times(1)).existsByEmail(anyString());
        verify(adminRepository, times(1)).existsByEmail(anyString());
        verify(memberRepository, times(1)).existsByEmail(anyString());
        verify(adminRepository, times(1)).save(any(Admin.class));
        verify(rabbitMQService, times(1)).sendAdminCreated(any(AdminRegistrationDTO.class));
    }
}
