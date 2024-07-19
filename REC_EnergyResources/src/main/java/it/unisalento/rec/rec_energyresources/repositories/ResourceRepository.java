package it.unisalento.rec.rec_energyresources.repositories;

import it.unisalento.rec.rec_energyresources.domain.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends MongoRepository<Resource, String> {
    List<Resource> findByMemberEmail(String memberEmail);
    List<Resource> findByAvailableTimeGreaterThanEqualAndProcessorModelAndMemoryGreaterThanEqualAndOsAndProcessorVelocityGreaterThanEqualAndAvailability(int availableTime, String processorModel, int memory, String os, float processorVelocity, Boolean availability);
    void deleteById(String id);
    Optional<Resource> findById(String id);
    void deleteAllBymemberEmail(String memberEmail);
}
