package it.unisalento.rec.rec_reward.repositories;

import it.unisalento.rec.rec_reward.domain.RewardPurchased;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RewardPurchasedRepository extends MongoRepository<RewardPurchased, String> {
    List<RewardPurchased> findByEmailMember(String memberEmail);
}
