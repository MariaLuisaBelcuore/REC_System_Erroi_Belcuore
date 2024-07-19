package it.unisalento.rec.rec_payment.repositories;

import it.unisalento.rec.rec_payment.domain.ClientPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ClientPaymentRepository extends MongoRepository<ClientPayment, String> {
    List<ClientPayment> findByClientEmail(String clientEmail);
    Optional<ClientPayment> findById(String id);
    void deleteById(String id);
}
