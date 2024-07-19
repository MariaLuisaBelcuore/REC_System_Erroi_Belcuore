package it.unisalento.rec.rec_login.repositories;

import it.unisalento.rec.rec_login.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {

    Client findClientByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

}
