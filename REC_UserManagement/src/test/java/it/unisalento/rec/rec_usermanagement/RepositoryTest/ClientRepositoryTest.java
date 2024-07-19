package it.unisalento.rec.rec_usermanagement.RepositoryTest;

import it.unisalento.rec.rec_usermanagement.domain.Client;
import it.unisalento.rec.rec_usermanagement.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ClientRepositoryTest {
    @MockBean
    private ClientRepository clientRepository;

    @Test
    public void testFindClientByEmail() {
        Client client = new Client();
        client.setEmail("client@example.com");
        client.setId("1");

        when(clientRepository.findClientByEmail(anyString())).thenReturn(client);

        Client foundClient = clientRepository.findClientByEmail(anyString());

        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getEmail()).isEqualTo("client@example.com");
        verify(clientRepository, times(1)).findClientByEmail(anyString());
    }


    @Test
    public void testDeleteAdminById() {
        doNothing().when(clientRepository).deleteById(anyString());
        clientRepository.deleteById(anyString());
        verify(clientRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void testDeleteAdminByEmail() {
        doNothing().when(clientRepository).deleteByEmail(anyString());
        clientRepository.deleteByEmail(anyString());
        verify(clientRepository, times(1)).deleteByEmail(anyString());
    }

    @Test
    public void testFindAdminById() {
        Client client = new Client();
        client.setEmail("client@example.com");
        client.setId("1");

        when(clientRepository.findClientById(anyString())).thenReturn(client);

        Client foundClient = clientRepository.findClientById(anyString());

        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getId()).isEqualTo("1");
        verify(clientRepository, times(1)).findClientById(anyString());
    }


    @Test
    public void testExistsByEmail() {
        when(clientRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = clientRepository.existsByEmail(anyString());

        assertThat(exists).isTrue();
        verify(clientRepository, times(1)).existsByEmail(anyString());
    }
}
