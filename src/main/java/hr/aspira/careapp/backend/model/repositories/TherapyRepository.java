package hr.aspira.careapp.backend.model.repositories;

import hr.aspira.careapp.backend.model.entities.Therapy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapyRepository extends JpaRepository<Therapy, Integer> {
}
