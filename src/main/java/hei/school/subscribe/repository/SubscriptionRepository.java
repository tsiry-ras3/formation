package hei.school.subscribe.repository;

import hei.school.subscribe.repository.model.JSubscription;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<JSubscription, UUID> {}
