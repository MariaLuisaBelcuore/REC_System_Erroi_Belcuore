package it.unisalento.rec.rec_usermanagement.repositories;

import it.unisalento.rec.rec_usermanagement.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

}
