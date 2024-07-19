package it.unisalento.rec.rec_performance.repositories;

import it.unisalento.rec.rec_performance.domain.PerformanceMember;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PerformanceMemberRepository extends MongoRepository<PerformanceMember, String> {
    Optional<PerformanceMember> findByCalculationDate(LocalDateTime calculationDate);
    List<PerformanceMember> findByCalculationDateAfter(LocalDateTime calculationDate);
    List<PerformanceMember> findAllByMemberEmail(String memberEmail);
    List<PerformanceMember> findByMemberEmailAndCalculationDateBetween(String memberEmail, LocalDateTime calculationDateStart, LocalDateTime calculationDateEnd);
}
