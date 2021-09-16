package hr.aspira.careapp.backend.model.repositories;

import hr.aspira.careapp.backend.model.entities.TherapyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapyPlanRepository extends JpaRepository<TherapyPlan, Integer> {
}
