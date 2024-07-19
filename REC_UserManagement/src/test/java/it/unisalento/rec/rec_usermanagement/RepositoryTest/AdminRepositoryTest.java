package it.unisalento.rec.rec_usermanagement.RepositoryTest;

import it.unisalento.rec.rec_usermanagement.domain.Admin;
import it.unisalento.rec.rec_usermanagement.repositories.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminRepositoryTest {
    @MockBean
    private AdminRepository adminRepository;

    @Test
    public void testFindAdminByEmail() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setId("1");

        when(adminRepository.findAdminByEmail(anyString())).thenReturn(admin);

        Admin foundAdmin = adminRepository.findAdminByEmail(anyString());

        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getEmail()).isEqualTo("admin@example.com");
        verify(adminRepository, times(1)).findAdminByEmail(anyString());
    }


    @Test
    public void testDeleteAdminById() {
        doNothing().when(adminRepository).deleteById(anyString());
        adminRepository.deleteById(anyString());
        verify(adminRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void testDeleteAdminByEmail() {
        doNothing().when(adminRepository).deleteByEmail(anyString());
        adminRepository.deleteByEmail(anyString());
        verify(adminRepository, times(1)).deleteByEmail(anyString());
    }

    @Test
    public void testFindAdminById() {
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");
        admin.setId("1");

        when(adminRepository.findAdminById(anyString())).thenReturn(admin);

        Admin foundAdmin = adminRepository.findAdminById(anyString());

        assertThat(foundAdmin).isNotNull();
        assertThat(foundAdmin.getId()).isEqualTo("1");
        verify(adminRepository, times(1)).findAdminById(anyString());
    }


    @Test
    public void testExistsByEmail() {
        when(adminRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = adminRepository.existsByEmail(anyString());

        assertThat(exists).isTrue();
        verify(adminRepository, times(1)).existsByEmail(anyString());
    }
}
