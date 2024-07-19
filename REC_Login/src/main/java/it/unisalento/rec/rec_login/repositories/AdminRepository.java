package it.unisalento.rec.rec_login.repositories;

import it.unisalento.rec.rec_login.domain.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findAdminByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}
