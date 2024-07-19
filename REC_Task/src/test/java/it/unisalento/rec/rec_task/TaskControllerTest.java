package it.unisalento.rec.rec_task;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_task.dto.TaskDTO;
import it.unisalento.rec.rec_task.dto.TaskListDTO;
import it.unisalento.rec.rec_task.security.CheckJwt;
import it.unisalento.rec.rec_task.service.TaskDetailsService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private TaskDetailsService taskDetailsService;

    @Test
    public void testCreate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId("1");
        taskDTO.setName("name");
        taskDTO.setDescription("description");
        taskDTO.setExecutionTime(1);
        taskDTO.setLinkGit("link");
        taskDTO.setOs("os");
        taskDTO.setMemory(1);
        taskDTO.setProcessorModel("model");
        taskDTO.setProcessorVelocity(1);
        taskDTO.setClientEmail(email);

        when(taskDetailsService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/task/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDTO)))
                .andExpect(status().isOk());

        verify(taskDetailsService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        TaskListDTO taskListDTO = new TaskListDTO();
        ArrayList<TaskDTO> tasks = new ArrayList<>();
        taskListDTO.setList(tasks);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId("1");
        taskDTO.setName("name");
        taskDTO.setDescription("description");
        taskDTO.setExecutionTime(1);
        taskDTO.setLinkGit("link");
        taskDTO.setOs("os");
        taskDTO.setMemory(1);
        taskDTO.setProcessorModel("model");
        taskDTO.setProcessorVelocity(1);
        taskDTO.setClientEmail(email);

        tasks.add(taskDTO);

        when(taskDetailsService.searchTaskByClientEmail(anyString())).thenReturn(taskListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/task/searchAll")
                        .header("Authorization", "Bearer " + token)
                        .param("clientEmail", email))
                .andExpect(status().isOk());

        verify(taskDetailsService, times(1)).searchTaskByClientEmail(anyString());
    }


    @Test
    public void testReadOne() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId("1");
        taskDTO.setName("name");
        taskDTO.setDescription("description");
        taskDTO.setExecutionTime(1);
        taskDTO.setLinkGit("link");
        taskDTO.setOs("os");
        taskDTO.setMemory(1);
        taskDTO.setProcessorModel("model");
        taskDTO.setProcessorVelocity(1);
        taskDTO.setClientEmail(email);


        when(taskDetailsService.searchById(anyString())).thenReturn(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/task/search/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(taskDetailsService, times(1)).searchById(anyString());
    }


    @Test
    public void testDelete() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        when(taskDetailsService.deleteTask(anyString())).thenReturn("1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/task/delete/{id}", "1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(taskDetailsService, times(1)).deleteTask(anyString());
    }


    @Test
    public void testUpdate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQ0xJRU5UIiwic3ViamVjdCI6ImdpYWNvbW9lcnJvaTk4QGdtYWlsLmNvbSIsImlhdCI6MTcxOTMxMzQ2MSwiZXhwIjoxNzUwODQ5NDYxfQ.ywBjnui0wVL2yNP1akgZcaWFn0h_AWoRr5bSRqWpzcw";
        String email = "client@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId("1");
        taskDTO.setName("name");
        taskDTO.setDescription("description");
        taskDTO.setExecutionTime(1);
        taskDTO.setLinkGit("link");
        taskDTO.setOs("os");
        taskDTO.setMemory(1);
        taskDTO.setProcessorModel("model");
        taskDTO.setProcessorVelocity(1);
        taskDTO.setClientEmail(email);

        when(taskDetailsService.updateTask(anyString(), any(TaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/task/update/{id}", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDTO)))
                .andExpect(status().isOk());

        verify(taskDetailsService, times(1)).updateTask(anyString(), any(TaskDTO.class));
    }
}