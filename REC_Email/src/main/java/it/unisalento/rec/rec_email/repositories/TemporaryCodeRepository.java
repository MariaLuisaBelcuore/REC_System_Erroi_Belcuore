package it.unisalento.rec.rec_email.repositories;

import it.unisalento.rec.rec_email.domain.TemporaryCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TemporaryCodeRepository extends MongoRepository<TemporaryCode, String> {
    Optional<TemporaryCode> findByCode(String code);
}
