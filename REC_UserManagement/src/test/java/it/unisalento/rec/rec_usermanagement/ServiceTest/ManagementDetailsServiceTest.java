package it.unisalento.rec.rec_usermanagement.ServiceTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_usermanagement.domain.Admin;
import it.unisalento.rec.rec_usermanagement.domain.Client;
import it.unisalento.rec.rec_usermanagement.domain.Member;
import it.unisalento.rec.rec_usermanagement.dto.UserDTO;
import it.unisalento.rec.rec_usermanagement.dto.UserListDTO;
import it.unisalento.rec.rec_usermanagement.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_usermanagement.exceptions.UserNotFoundException;
import it.unisalento.rec.rec_usermanagement.repositories.AdminRepository;
import it.unisalento.rec.rec_usermanagement.repositories.ClientRepository;
import it.unisalento.rec.rec_usermanagement.repositories.MemberRepository;
import it.unisalento.rec.rec_usermanagement.service.ManagementDetailsService;
import it.unisalento.rec.rec_usermanagement.service.RabbitMQService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class ManagementDetailsServiceTest {
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private RabbitMQService rabbitMQService;
    @Autowired
    private ManagementDetailsService managementDetailsService;

    @Test
    public void testDeleteUser() throws UserNotFoundException, JsonProcessingException {
        String email = "client@example.com";
        Client client = new Client();
        client.setEmail(email);

        when(clientRepository.findClientByEmail(anyString())).thenReturn(client);
        doNothing().when(clientRepository).deleteByEmail(anyString());
        doNothing().when(rabbitMQService).sendDeleteUser(anyString());

        String result = managementDetailsService.deleteUser(email);

        assertThat(result).isEqualTo(email);
        verify(clientRepository, times(1)).findClientByEmail(anyString());
        verify(clientRepository, times(1)).deleteByEmail(anyString());
        verify(rabbitMQService, times(1)).sendDeleteUser(anyString());
    }

    @Test
    public void testDeleteForAdmin() throws UserNotFoundException, OperationNotPermittedException, JsonProcessingException {
        String email = "admin@example.com";
        Admin admin = new Admin();
        admin.setEmail(email);

        when(adminRepository.findAdminByEmail(anyString())).thenReturn(admin);
        doNothing().when(adminRepository).deleteByEmail(anyString());
        doNothing().when(rabbitMQService).sendDeleteUser(anyString());


        String result = managementDetailsService.deleteForAdmin(email);

        assertThat(result).isEqualTo(email);
        verify(adminRepository, times(1)).findAdminByEmail(anyString());
        verify(adminRepository, times(1)).deleteByEmail(anyString());
        verify(rabbitMQService, times(1)).sendDeleteUser(anyString());
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException, JsonProcessingException {
        String id = "1";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        Client client = new Client();
        client.setId(id);
        when(clientRepository.findClientById(anyString())).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(rabbitMQService).sendModifyUser(any(UserDTO.class));


        UserDTO result = managementDetailsService.updateUser(id, userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(clientRepository, times(1)).findClientById(anyString());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(rabbitMQService, times(1)).sendModifyUser(any(UserDTO.class));
    }

    @Test
    public void testUpdateForAdmin() throws UserNotFoundException, OperationNotPermittedException, JsonProcessingException {
        String id = "1";
        String email = "admin@example.com";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        Client client = new Client();
        client.setId(id);
        when(clientRepository.findClientById(anyString())).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(rabbitMQService).sendModifyUser(any(UserDTO.class));


        UserDTO result = managementDetailsService.updateForAdmin(id, userDTO, email);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(clientRepository, times(1)).findClientById(anyString());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(rabbitMQService, times(1)).sendModifyUser(any(UserDTO.class));
    }

    @Test
    public void testSearchAllClient() throws UserNotFoundException {
        Client client = new Client();
        List<Client> clients = new ArrayList<>();
        clients.add(client);
        when(clientRepository.findAll()).thenReturn(clients);

        UserListDTO result = managementDetailsService.searchAllClient();

        assertThat(result).isNotNull();
        assertThat(result.getList()).hasSize(1);
        verify(clientRepository, times(1)).findAll();

    }

    @Test
    public void testSearchAllMember() throws UserNotFoundException {
        Member member = new Member();
        List<Member> members = new ArrayList<>();
        members.add(member);
        when(memberRepository.findAll()).thenReturn(members);

        UserListDTO result = managementDetailsService.searchAllMember();

        assertThat(result).isNotNull();
        assertThat(result.getList()).hasSize(1);
        verify(memberRepository, times(1)).findAll();

    }

    @Test
    public void testSearchAllAdmin() throws UserNotFoundException {
        Admin admin = new Admin();
        List<Admin> admins = new ArrayList<>();
        admins.add(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        UserListDTO result = managementDetailsService.searchAllAdmin();

        assertThat(result).isNotNull();
        assertThat(result.getList()).hasSize(1);
        verify(adminRepository, times(1)).findAll();

    }

    @Test
    public void testSearchByEmail() throws UserNotFoundException {
        String email = "email@example.com";
        Client client = new Client();
        client.setEmail(email);
        when(clientRepository.findClientByEmail(anyString())).thenReturn(client);

        UserDTO result = managementDetailsService.searchByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        verify(clientRepository, times(1)).findClientByEmail(anyString());
    }
}
