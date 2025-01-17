package it.unisalento.rec.rec_login.repositories;

import it.unisalento.rec.rec_login.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

}
