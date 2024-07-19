package it.unisalento.rec.rec_performance.repositories;

import it.unisalento.rec.rec_performance.domain.Performance;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends MongoRepository<Performance, String> {
    Optional<Performance> findByCalculationDate(LocalDateTime calculationDate);
    List<Performance> findByCalculationDateAfter(LocalDateTime calculationDate);
    List<Performance> findByCalculationDateBetween(LocalDateTime calculationDateStart, LocalDateTime calculationDateEnd);
}
