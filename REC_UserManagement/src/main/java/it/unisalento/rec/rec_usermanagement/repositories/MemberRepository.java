package it.unisalento.rec.rec_usermanagement.repositories;

import it.unisalento.rec.rec_usermanagement.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
    Member findMemberByEmail(String email);
    void deleteById(String id);
    void deleteByEmail(String email);
    Member findMemberById(String id);
    boolean existsByEmail(String email);

}
