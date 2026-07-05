package hei.school.subscribe.repository;

import hei.school.subscribe.repository.model.JSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<JSubscription, UUID> {
}