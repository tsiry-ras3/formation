package hei.school.subscribe.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.school.subscribe.endpoint.rest.controller.SubscribeController;
import hei.school.subscribe.endpoint.rest.controller.dto.SubscriptionCreationRequest;
import hei.school.subscribe.endpoint.rest.controller.validator.SubscriptionCreationRequestValidator;
import hei.school.subscribe.exception.BadRequestException;
import hei.school.subscribe.exception.NotFoundException;
import hei.school.subscribe.repository.model.JCourse;
import hei.school.subscribe.repository.model.JSubscription;
import hei.school.subscribe.repository.model.JUser;
import hei.school.subscribe.service.SubscriptionService;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubscribeController.class)
class SubscribeControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private SubscriptionService subscriptionService;
  @MockBean private SubscriptionCreationRequestValidator validator;
  private UUID userId;
  private UUID courseId;
  private JSubscription subscription;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    courseId = UUID.randomUUID();
    JUser user =
        JUser.builder()
            .id(userId)
            .firstName("zety")
            .lastName("Rakoto")
            .email("zety.rakoto@example.com")
            .build();
    JCourse course =
        JCourse.builder()
            .id(courseId)
            .title("Spring Boot Avancé")
            .startDate(Instant.now())
            .endDate(Instant.now().plusSeconds(3600))
            .build();
    subscription =
        JSubscription.builder()
            .id(UUID.randomUUID())
            .user(user)
            .course(course)
            .subscribedAt(Instant.now())
            .build();
  }

  @Test
  void should_return_201_when_subscription_succeeds() throws Exception {
    when(subscriptionService.subscribe(any(UUID.class), any(UUID.class))).thenReturn(subscription);
    SubscriptionCreationRequest request = new SubscriptionCreationRequest(userId, courseId);
    mockMvc
        .perform(
            post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(userId.toString()))
        .andExpect(jsonPath("$.courseId").value(courseId.toString()));
  }

  @Test
  void should_return_404_when_user_not_found() throws Exception {
    when(subscriptionService.subscribe(any(UUID.class), any(UUID.class)))
        .thenThrow(new NotFoundException("User not found: " + userId));
    SubscriptionCreationRequest request = new SubscriptionCreationRequest(userId, courseId);
    mockMvc
        .perform(
            post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void should_return_400_when_body_is_invalid() throws Exception {
    doThrow(new BadRequestException("userId must not be null")).when(validator).validate(any());
    mockMvc
        .perform(post("/subscriptions").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }
}