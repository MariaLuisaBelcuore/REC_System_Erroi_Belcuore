package it.unisalento.rec.rec_email.repositories;

import it.unisalento.rec.rec_email.domain.Email;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailRepository extends MongoRepository<Email, String> {
    List<Email> findByEmailTo(String emailTo);
    void deleteByEmailTo(String emailTo);
    boolean existsByEmailTo(String emailTo);
}
