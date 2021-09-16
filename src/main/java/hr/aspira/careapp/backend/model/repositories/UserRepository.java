package hr.aspira.careapp.backend.model.repositories;

import hr.aspira.careapp.backend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
