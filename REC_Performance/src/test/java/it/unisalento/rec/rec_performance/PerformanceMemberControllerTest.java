package it.unisalento.rec.rec_performance;

import it.unisalento.rec.rec_performance.dto.PerformanceMemberDTO;
import it.unisalento.rec.rec_performance.dto.PerformanceMemberListDTO;
import it.unisalento.rec.rec_performance.security.CheckJwt;
import it.unisalento.rec.rec_performance.service.PerformanceMemberDetailsService;
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
public class PerformanceMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CheckJwt checkJwt;
    @MockBean
    private PerformanceMemberDetailsService performanceMemberDetailsService;

    @Test
    public void testCalculate() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        PerformanceMemberDTO performanceMemberDTO = new PerformanceMemberDTO();
        performanceMemberDTO.setId("1");
        performanceMemberDTO.setTotalEnergyAvailable(1);
        performanceMemberDTO.setTotalEnergy(1);
        performanceMemberDTO.setEnergyResold(1);

        when(performanceMemberDetailsService.calculate(anyString())).thenReturn(performanceMemberDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/performance/member/calculate")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());


        verify(performanceMemberDetailsService, times(1)).calculate(anyString());
    }

    @Test
    public void testReport() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        PerformanceMemberListDTO performanceMemberListDTO = new PerformanceMemberListDTO();
        ArrayList<PerformanceMemberDTO> performances = new ArrayList<>();
        performanceMemberListDTO.setList(performances);

        PerformanceMemberDTO performanceMemberDTO = new PerformanceMemberDTO();
        performanceMemberDTO.setId("1");
        performanceMemberDTO.setTotalEnergyAvailable(1);
        performanceMemberDTO.setTotalEnergy(1);
        performanceMemberDTO.setEnergyResold(1);

        performances.add(performanceMemberDTO);

        LocalDateTime calculationDateStart = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime calculationDateEnd = LocalDateTime.of(2023, 1, 31, 23, 59);

        when(performanceMemberDetailsService.report(calculationDateStart, calculationDateEnd, email)).thenReturn(performanceMemberListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/performance/member/reportOfPeriod")
                        .header("Authorization", "Bearer " + token)
                        .param("calculationDateStart", calculationDateStart.toString())
                        .param("calculationDateEnd", calculationDateEnd.toString())
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(performanceMemberDetailsService, times(1)).report(calculationDateStart, calculationDateEnd, email);
    }

    @Test
    public void testRead() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwic3ViamVjdCI6Imtla2tvLnNjaGlyaW56aUBnbWFpbC5jb20iLCJpYXQiOjE3MTkzMTM0OTksImV4cCI6MTc1MDg0OTQ5OX0._y-_M-LXtzwunrqbHgKPU7VmtJJmhUN7gwHiwt5XVfc";
        String email = "member@example.com";

        when(checkJwt.check(any(String.class), any(ArrayList.class)))
                .thenReturn(ResponseEntity.ok().build());

        PerformanceMemberListDTO performanceMemberListDTO = new PerformanceMemberListDTO();
        ArrayList<PerformanceMemberDTO> performances = new ArrayList<>();
        performanceMemberListDTO.setList(performances);

        PerformanceMemberDTO performanceMemberDTO = new PerformanceMemberDTO();
        performanceMemberDTO.setId("1");
        performanceMemberDTO.setTotalEnergyAvailable(1);
        performanceMemberDTO.setTotalEnergy(1);
        performanceMemberDTO.setEnergyResold(1);

        performances.add(performanceMemberDTO);

        when(performanceMemberDetailsService.searchAllByMember(anyString())).thenReturn(performanceMemberListDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/performance/member/searchAll")
                        .header("Authorization", "Bearer " + token)
                        .param("memberEmail", email))
                .andExpect(status().isOk());

        verify(performanceMemberDetailsService, times(1)).searchAllByMember(anyString());
    }
}
