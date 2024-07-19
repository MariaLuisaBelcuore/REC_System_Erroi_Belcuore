package it.unisalento.rec.rec_login.repositories;

import it.unisalento.rec.rec_login.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
    Member findMemberByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);

}
