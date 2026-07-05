package hei.school.subscribe.repository;

import hei.school.subscribe.repository.model.JUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<JUser, UUID> {
}