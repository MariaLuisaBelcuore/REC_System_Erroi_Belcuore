package it.unisalento.rec.rec_usermanagement.repositories;

import it.unisalento.rec.rec_usermanagement.domain.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findAdminByEmail(String email);
    void deleteById(String id);
    void deleteByEmail(String email);
    Admin findAdminById(String id);
    boolean existsByEmail(String email);

}
