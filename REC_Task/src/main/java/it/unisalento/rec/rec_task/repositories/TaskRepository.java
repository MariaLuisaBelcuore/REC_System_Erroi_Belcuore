package it.unisalento.rec.rec_task.repositories;

import it.unisalento.rec.rec_task.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByClientEmail(String clientEmail);
    void deleteById(String id);
    Optional<Task> findById(String id);
}
