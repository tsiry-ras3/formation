package hei.school.subscribe.service;

import hei.school.subscribe.exception.NotFoundException;
import hei.school.subscribe.repository.CourseRepository;
import hei.school.subscribe.repository.SubscriptionRepository;
import hei.school.subscribe.repository.UserRepository;
import hei.school.subscribe.repository.model.JCourse;
import hei.school.subscribe.repository.model.JSubscription;
import hei.school.subscribe.repository.model.JUser;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;

  public JSubscription subscribe(UUID userId, UUID courseId) {
    JUser user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    JCourse course =
        courseRepository
            .findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

    JSubscription subscription =
        JSubscription.builder().user(user).course(course).subscribedAt(Instant.now()).build();

    JSubscription saved = subscriptionRepository.save(subscription);
    // mail
    return saved;
  }
}
