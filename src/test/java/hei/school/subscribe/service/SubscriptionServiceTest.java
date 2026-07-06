package hei.school.subscribe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import hei.school.subscribe.endpoint.event.EventProducer;
import hei.school.subscribe.endpoint.event.model.SendEmailRequested;
import hei.school.subscribe.file.bucket.BucketComponent;
import hei.school.subscribe.repository.CourseRepository;
import hei.school.subscribe.repository.SubscriptionRepository;
import hei.school.subscribe.repository.UserRepository;
import hei.school.subscribe.repository.model.JCourse;
import hei.school.subscribe.repository.model.JSubscription;
import hei.school.subscribe.repository.model.JUser;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock private SubscriptionRepository subscriptionRepository;

  @Mock private UserRepository userRepository;

  @Mock private CourseRepository courseRepository;

  @Mock private EventProducer<SendEmailRequested> eventProducer;

  @Mock private BucketComponent bucketComponent;

  @InjectMocks private SubscriptionService subscriptionService;

  private UUID userId;
  private UUID courseId;
  private JUser user;
  private JCourse course;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    courseId = UUID.randomUUID();

    user =
        JUser.builder()
            .id(userId)
            .firstName("zety")
            .lastName("Rakoto")
            .email("zety.rakoto@gmail.com")
            .build();

    course =
        JCourse.builder()
            .id(courseId)
            .title("asf")
            .startDate(Instant.now())
            .endDate(Instant.now().plusSeconds(3600))
            .build();
  }

  @Test
  void should_subscribe_user_to_course() {
    var mockUrl = mock(URL.class);
    when(mockUrl.toString()).thenReturn("http://example.com/receipt");
    when(bucketComponent.upload(any(), anyString())).thenReturn(null);
    when(bucketComponent.presign(anyString(), any(Duration.class))).thenReturn(mockUrl);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(subscriptionRepository.save(any(JSubscription.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    JSubscription result = subscriptionService.subscribe(userId, courseId);

    assertThat(result.getUser()).isEqualTo(user);
    assertThat(result.getCourse()).isEqualTo(course);
    assertThat(result.getSubscribedAt()).isNotNull();

    ArgumentCaptor<JSubscription> captor = ArgumentCaptor.forClass(JSubscription.class);
    verify(subscriptionRepository).save(captor.capture());
    assertThat(captor.getValue().getUser()).isEqualTo(user);
    assertThat(captor.getValue().getCourse()).isEqualTo(course);
  }

  @Test
  void should_throw_exception_when_user_not_found() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> subscriptionService.subscribe(userId, courseId))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining(userId.toString());

    verifyNoInteractions(courseRepository, subscriptionRepository);
  }

  @Test
  void should_throw_exception_when_course_not_found() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> subscriptionService.subscribe(userId, courseId))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining(courseId.toString());

    verifyNoInteractions(subscriptionRepository);
  }
}
