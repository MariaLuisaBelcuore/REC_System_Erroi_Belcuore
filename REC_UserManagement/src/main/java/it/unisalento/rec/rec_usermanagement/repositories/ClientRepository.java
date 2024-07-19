package it.unisalento.rec.rec_usermanagement.repositories;

import it.unisalento.rec.rec_usermanagement.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {

    Client findClientByEmail(String email);
    void deleteById(String id);
    void deleteByEmail(String email);
    Client findClientById(String id);
    boolean existsByEmail(String email);

}
