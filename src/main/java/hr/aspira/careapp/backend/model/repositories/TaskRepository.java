package hr.aspira.careapp.backend.model.repositories;

import hr.aspira.careapp.backend.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
