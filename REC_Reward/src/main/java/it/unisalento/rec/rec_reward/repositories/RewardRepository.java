package it.unisalento.rec.rec_reward.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import it.unisalento.rec.rec_reward.domain.Reward;
import java.util.List;
import java.util.Optional;

public interface RewardRepository extends MongoRepository<Reward, String> {
    Optional<Reward> findById(String id);
    List<Reward> findByNameContainingIgnoreCase(String searchTerm);
}
