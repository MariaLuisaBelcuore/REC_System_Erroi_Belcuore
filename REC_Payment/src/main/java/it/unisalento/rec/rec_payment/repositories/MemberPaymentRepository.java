package it.unisalento.rec.rec_payment.repositories;

import it.unisalento.rec.rec_payment.domain.MemberPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberPaymentRepository extends MongoRepository<MemberPayment, String> {
    List<MemberPayment> findByMemberEmail(String clientEmail);
    Optional<MemberPayment> findById(String id);
    void deleteById(String id);
}
