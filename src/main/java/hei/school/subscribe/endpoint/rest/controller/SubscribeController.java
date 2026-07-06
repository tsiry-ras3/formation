package hei.school.subscribe.endpoint.rest.controller;

import hei.school.subscribe.endpoint.event.EventProducer;
import hei.school.subscribe.endpoint.event.model.SendEmailRequested;
import hei.school.subscribe.endpoint.rest.controller.dto.SubscriptionCreationRequest;
import hei.school.subscribe.endpoint.rest.controller.validator.SubscriptionCreationRequestValidator;
import hei.school.subscribe.repository.model.JSubscription;
import hei.school.subscribe.service.SubscriptionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class SubscribeController {

  private SubscriptionService subscriptionService;
  private SubscriptionCreationRequestValidator validator;
  private EventProducer<SendEmailRequested> eventProducer;

  @PostMapping("/subscriptions")
  @ResponseStatus(HttpStatus.CREATED)
  public SubscriptionCreationRequest postSubscribe(
      @RequestBody SubscriptionCreationRequest request) {
    validator.validate(request);
    JSubscription sub = subscriptionService.subscribe(request.userId(), request.courseId());
    var event = SendEmailRequested.builder().to(sub.getUser().getEmail()).build();
    eventProducer.accept(List.of(event));
    return request;
  }
}
