package it.unisalento.rec.rec_wallet.repositories;

import it.unisalento.rec.rec_wallet.domain.WalletMember;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
public interface WalletMemberRepository extends MongoRepository<WalletMember, String> {
    Optional<WalletMember> findByMemberEmail(String memberEmail);
    void deleteById(String id);
    void deleteByMemberEmail(String memberEmail);
    boolean existsByMemberEmail(String memberEmail);

}
