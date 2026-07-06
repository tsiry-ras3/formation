package hei.school.subscribe.service;

import hei.school.subscribe.endpoint.event.EventProducer;
import hei.school.subscribe.endpoint.event.model.SendEmailRequested;
import hei.school.subscribe.exception.NotFoundException;
import hei.school.subscribe.file.bucket.BucketComponent;
import hei.school.subscribe.repository.CourseRepository;
import hei.school.subscribe.repository.SubscriptionRepository;
import hei.school.subscribe.repository.UserRepository;
import hei.school.subscribe.repository.model.JCourse;
import hei.school.subscribe.repository.model.JSubscription;
import hei.school.subscribe.repository.model.JUser;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private EventProducer<SendEmailRequested> eventProducer;
  private final BucketComponent bucketComponent;


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
    var event = SendEmailRequested.builder().to(saved.getUser().getEmail()).build();
    eventProducer.accept(List.of(event));
    return saved;
  }
}
