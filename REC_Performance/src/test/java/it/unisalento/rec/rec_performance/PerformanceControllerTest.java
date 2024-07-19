package it.unisalento.rec.rec_performance;

import it.unisalento.rec.rec_performance.dto.PerformanceDTO;
import it.unisalento.rec.rec_performance.dto.PerformanceListDTO;
import it.unisalento.rec.rec_performance.security.CheckJwt;
import it.unisalento.rec.rec_performance.service.PerformanceDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PerformanceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private PerformanceDetailsService performanceDetailsService;

    @Test
    public void testCalculate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        PerformanceDTO performanceDTO = new PerformanceDTO();
        performanceDTO.setId("1");
        performanceDTO.setEnergyResoldPercentage(1);
        performanceDTO.setTotalEarnings(1);
        performanceDTO.setTotalEnergyAvailable(1);
        performanceDTO.setTotalEnergyConsumed(1);

        when(performanceDetailsService.calculate()).thenReturn(performanceDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/performance/calculate")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(performanceDetailsService, times(1)).calculate();
    }

    @Test
    public void testReport() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        PerformanceListDTO performanceListDTO = new PerformanceListDTO();
        ArrayList<PerformanceDTO> performances = new ArrayList<>();
        performanceListDTO.setList(performances);

        PerformanceDTO performanceDTO = new PerformanceDTO();
        performanceDTO.setId("1");
        performanceDTO.setEnergyResoldPercentage(1);
        performanceDTO.setTotalEarnings(1);
        performanceDTO.setTotalEnergyAvailable(1);
        performanceDTO.setTotalEnergyConsumed(1);

        performances.add(performanceDTO);

        LocalDateTime calculationDateStart = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime calculationDateEnd = LocalDateTime.of(2023, 1, 31, 23, 59);


        when(performanceDetailsService.report(calculationDateStart, calculationDateEnd)).thenReturn(performanceListDTO);

        // Execute the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/performance/reportOfPeriod")
                        .header("Authorization", "Bearer " + token)
                        .param("calculationDateStart", calculationDateStart.toString())
                        .param("calculationDateEnd", calculationDateEnd.toString()))
                .andExpect(status().isOk());

        verify(performanceDetailsService, times(1)).report(calculationDateStart, calculationDateEnd);
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWJqZWN0IjoibWFyaWFsdWlzYS5iZWxjdW9yZUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM1NDUsImV4cCI6MTc1MDg0OTU0NX0.jvTLTHqX--9y3y9QgMAK8ogcJ1_eZ4CCKP4WlKz8H98";
        String email = "admin@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        PerformanceListDTO performanceListDTO = new PerformanceListDTO();
        ArrayList<PerformanceDTO> performaces = new ArrayList<>();
        performanceListDTO.setList(performaces);

        PerformanceDTO performanceDTO = new PerformanceDTO();
        performanceDTO.setId("1");
        performanceDTO.setEnergyResoldPercentage(1);
        performanceDTO.setTotalEarnings(1);
        performanceDTO.setTotalEnergyAvailable(1);
        performanceDTO.setTotalEnergyConsumed(1);

        performaces.add(performanceDTO);

        when(performanceDetailsService.searchAll()).thenReturn(performanceListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/performance/searchAll")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());


        verify(performanceDetailsService, times(1)).searchAll();
    }
}
