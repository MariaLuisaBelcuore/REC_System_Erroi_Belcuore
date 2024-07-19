package it.unisalento.rec.rec_performance.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_performance.domain.PerformanceMember;
import it.unisalento.rec.rec_performance.dto.InfoToPerformanceDTO;
import it.unisalento.rec.rec_performance.dto.PerformanceMemberDTO;
import it.unisalento.rec.rec_performance.dto.PerformanceMemberListDTO;
import it.unisalento.rec.rec_performance.exceptions.PerformanceNotFoundException;
import it.unisalento.rec.rec_performance.repositories.PerformanceMemberRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceMemberDetailsService {
    @Autowired
    PerformanceMemberRepository performanceMemberRepository;
    @Autowired
    RabbitMQService rabbitMQService;

    public PerformanceMemberDTO calculate(String memberEmail) throws JsonProcessingException {
        InfoToPerformanceDTO infoToPerformanceDTO = rabbitMQService.sendRequestPerformance(memberEmail);
        PerformanceMember performanceMember = new PerformanceMember();
        BeanUtils.copyProperties(infoToPerformanceDTO, performanceMember);
        performanceMember.setCalculationDate(LocalDateTime.now());
        performanceMember.setMemberEmail(memberEmail);
        performanceMember.setEnergyResold(infoToPerformanceDTO.getTotalEnergy()- infoToPerformanceDTO.getTotalEnergyAvailable());
        performanceMember = performanceMemberRepository.save(performanceMember);
        PerformanceMemberDTO performanceMemberDTO = new PerformanceMemberDTO();
        BeanUtils.copyProperties(performanceMember, performanceMemberDTO);
        performanceMemberDTO.setId(performanceMember.getId());
        return performanceMemberDTO;
    }

    public PerformanceMemberListDTO searchAllByMember(String memberEmail) throws PerformanceNotFoundException {
        PerformanceMemberListDTO performanceMemberListDTO = new PerformanceMemberListDTO();
        ArrayList<PerformanceMemberDTO> performances = new ArrayList<>();
        performanceMemberListDTO.setList(performances);
        List<PerformanceMember> list = performanceMemberRepository.findAllByMemberEmail(memberEmail);
        if (list.isEmpty()) {
            throw new PerformanceNotFoundException("No performance calculated ");
        }
        for(PerformanceMember performanceMember : list){
            PerformanceMemberDTO performanceMemberDTO = new PerformanceMemberDTO();
            BeanUtils.copyProperties(performanceMember, performanceMemberDTO);
            performances.add(performanceMemberDTO);
        }
        return performanceMemberListDTO;
    }

    public PerformanceMemberListDTO report(LocalDateTime calculationDateStart, LocalDateTime calculationDateEnd, String memberEmail) throws PerformanceNotFoundException {
        PerformanceMemberListDTO performanceMemberListDTO = new PerformanceMemberListDTO();
        ArrayList<PerformanceMemberDTO> performances = new ArrayList<>();
        performanceMemberListDTO.setList(performances);
        List<PerformanceMember> list = performanceMemberRepository.findByMemberEmailAndCalculationDateBetween(memberEmail, calculationDateStart,calculationDateEnd);
        if (list.isEmpty()) {
            throw new PerformanceNotFoundException("No performance calculated in the specified period");
        }
        for(PerformanceMember performanceMember : list){
            PerformanceMemberDTO performanceMemberDTO = new PerformanceMemberDTO();
            BeanUtils.copyProperties(performanceMember, performanceMemberDTO);
            performances.add(performanceMemberDTO);
        }
        return performanceMemberListDTO;
    }
}
