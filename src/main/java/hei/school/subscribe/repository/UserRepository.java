package hei.school.subscribe.repository;

import hei.school.subscribe.repository.model.JUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JUser, UUID> {}
