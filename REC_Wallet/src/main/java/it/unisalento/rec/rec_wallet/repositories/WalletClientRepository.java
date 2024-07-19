package it.unisalento.rec.rec_wallet.repositories;

import it.unisalento.rec.rec_wallet.domain.WalletClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface WalletClientRepository extends MongoRepository<WalletClient, String> {
    Optional<WalletClient> findById(String id);
    Optional<WalletClient> findByClientEmail(String clientEmail);
    //si usa quando il client vuole cancellare la sua carta
    void deleteById(String id);
    //si usa per eliminare la carta di un certo client
    void deleteByClientEmail(String clientEmail);
    boolean existsByClientEmail(String clientEmail);
}
