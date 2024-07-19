package it.unisalento.rec.rec_performance.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_performance.domain.Performance;
import it.unisalento.rec.rec_performance.dto.InfoToPerformanceDTO;
import it.unisalento.rec.rec_performance.dto.PerformanceDTO;
import it.unisalento.rec.rec_performance.dto.PerformanceListDTO;
import it.unisalento.rec.rec_performance.exceptions.PerformanceNotFoundException;
import it.unisalento.rec.rec_performance.repositories.PerformanceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PerformanceDetailsService {
    @Autowired
    PerformanceRepository performanceRepository;
    @Autowired
    RabbitMQService rabbitMQService;

    public PerformanceDTO calculate() throws JsonProcessingException {
        Float totalEarnings = rabbitMQService.sendRequestEarrings();
        InfoToPerformanceDTO infoToPerformanceDTO = rabbitMQService.sendRequestPerformanceForAdmin();
        Performance performance = new Performance();
        BeanUtils.copyProperties(infoToPerformanceDTO, performance);
        performance.setCalculationDate(LocalDateTime.now());
        performance.setTotalEarnings(totalEarnings);
        performance.setTotalEnergyConsumed(infoToPerformanceDTO.getTotalEnergy()-infoToPerformanceDTO.getTotalEnergyAvailable());
        performance.setEnergyResoldPercentage(performance.getTotalEnergyConsumed()*100/infoToPerformanceDTO.getTotalEnergy());
        PerformanceDTO performanceDTO = new PerformanceDTO();
        BeanUtils.copyProperties(performance, performanceDTO);
        performance = performanceRepository.save(performance);
        performanceDTO.setId(performance.getId());
        return performanceDTO;
    }

    public PerformanceListDTO report(LocalDateTime calculationDateStart, LocalDateTime calculationDateEnd) throws PerformanceNotFoundException {
        PerformanceListDTO performanceListDTO = new PerformanceListDTO();
        ArrayList<PerformanceDTO> performaces = new ArrayList<>();
        performanceListDTO.setList(performaces);
        List<Performance> list = performanceRepository.findByCalculationDateBetween(calculationDateStart,calculationDateEnd);
        if (list.isEmpty()) {
            throw new PerformanceNotFoundException("No performance calculated in the specified period");
        }
        for(Performance performance : list){
            PerformanceDTO performanceDTO = new PerformanceDTO();
            BeanUtils.copyProperties(performance, performanceDTO);
            performaces.add(performanceDTO);
        }
        System.out.println(performanceListDTO);
        return performanceListDTO;
    }

    public PerformanceListDTO searchAll() throws PerformanceNotFoundException {
        PerformanceListDTO performanceListDTO = new PerformanceListDTO();
        ArrayList<PerformanceDTO> performances = new ArrayList<>();
        performanceListDTO.setList(performances);
        List<Performance> list = performanceRepository.findAll();
        if (list.isEmpty()) {
            throw new PerformanceNotFoundException("No performance calculated");
        }
        for(Performance performance : list){
            PerformanceDTO performanceDTO = new PerformanceDTO();
            BeanUtils.copyProperties(performance, performanceDTO);
            performances.add(performanceDTO);
        }
        return performanceListDTO;
    }
}
