package it.unisalento.rec.rec_usermanagement.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_usermanagement.dto.UserDTO;
import it.unisalento.rec.rec_usermanagement.dto.UserListDTO;
import it.unisalento.rec.rec_usermanagement.security.CheckJwt;
import it.unisalento.rec.rec_usermanagement.service.ManagementDetailsService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private ManagementDetailsService managementDetailsService;

    @Test
    public void testDelete() throws Exception {
        //il token corrisponde ad un CLIENT ed ha una validità di un anno, questo è un esempio e quindi
        //poteva essere anche un token corrispondente ad un MEMBER

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";


        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(managementDetailsService.deleteUser(anyString())).thenReturn(email);



        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/management/delete/{email}", email)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).deleteUser(anyString());
    }

    @Test
    public void testDeleteForAdmin() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(managementDetailsService.deleteForAdmin(anyString())).thenReturn(email);

        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/management/deleteForAdmin/{email}", email)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).deleteForAdmin(anyString());

    }

    @Test
    public void testUpdate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setNome("name");
        userDTO.setCognome("surname");
        userDTO.setEmail(email);
        userDTO.setPassword("password");
        userDTO.setRuolo("MEMBER");

        when(managementDetailsService.updateUser(anyString(), any(UserDTO.class))).thenReturn(userDTO);

        // Execute the PATCH request
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/management/update/{id}", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).updateUser(anyString(), any(UserDTO.class));
    }

    @Test
    public void testUpdateForAdmin() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setNome("name");
        userDTO.setCognome("surname");
        userDTO.setEmail(email);
        userDTO.setPassword("password");
        userDTO.setRuolo("MEMBER");

        when(managementDetailsService.updateForAdmin(anyString(), any(UserDTO.class), anyString())).thenReturn(userDTO);

        // Execute the PATCH request
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/management/updateForAdmin/{id}", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .param("email", email))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).updateForAdmin(anyString(), any(UserDTO.class), anyString());
    }

    @Test
    public void testReadClient() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        UserListDTO userListDTO = new UserListDTO();
        ArrayList<UserDTO> clients = new ArrayList<>();
        userListDTO.setList(clients);

        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setNome("name");
        userDTO.setCognome("surname");
        userDTO.setEmail(email);
        userDTO.setPassword("password");
        userDTO.setRuolo("CLIENT");

        clients.add(userDTO);


        when(managementDetailsService.searchAllClient()).thenReturn(userListDTO);

        // Execute the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/management/searchAll/client")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).searchAllClient();
    }

    @Test
    public void testReadMember() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        UserListDTO userListDTO = new UserListDTO();
        ArrayList<UserDTO> members = new ArrayList<>();
        userListDTO.setList(members);

        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setNome("name");
        userDTO.setCognome("surname");
        userDTO.setEmail(email);
        userDTO.setPassword("password");
        userDTO.setRuolo("MEMBER");

        members.add(userDTO);


        when(managementDetailsService.searchAllMember()).thenReturn(userListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/management/searchAll/member")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).searchAllMember();
    }

    @Test
    public void testReadAdmin() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        UserListDTO userListDTO = new UserListDTO();
        ArrayList<UserDTO> admins = new ArrayList<>();
        userListDTO.setList(admins);

        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setNome("name");
        userDTO.setCognome("surname");
        userDTO.setEmail(email);
        userDTO.setPassword("password");
        userDTO.setRuolo("ADMIN");

        admins.add(userDTO);


        when(managementDetailsService.searchAllAdmin()).thenReturn(userListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/management/searchAll/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).searchAllAdmin();
    }

    @Test
    public void testRead() throws Exception {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());


        UserDTO userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setNome("name");
        userDTO.setCognome("surname");
        userDTO.setEmail(email);
        userDTO.setPassword("password");
        userDTO.setRuolo("ADMIN");


        when(managementDetailsService.searchByEmail(anyString())).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/management/search/{email}", email)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(managementDetailsService, times(1)).searchByEmail(anyString());
    }
}
