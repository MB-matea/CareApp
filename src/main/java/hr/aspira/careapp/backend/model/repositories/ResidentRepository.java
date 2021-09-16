package hr.aspira.careapp.backend.model.repositories;

import hr.aspira.careapp.backend.model.entities.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident, Integer> {
}
