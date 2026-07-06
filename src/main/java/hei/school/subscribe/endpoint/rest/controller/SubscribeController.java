package hei.school.subscribe.endpoint.rest.controller;

import hei.school.subscribe.endpoint.rest.controller.dto.SubscriptionCreationRequest;
import hei.school.subscribe.endpoint.rest.controller.validator.SubscriptionCreationRequestValidator;
import hei.school.subscribe.service.SubscriptionService;
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

  @PostMapping("/subscriptions")
  @ResponseStatus(HttpStatus.CREATED)
  public SubscriptionCreationRequest postSubscribe(
      @RequestBody SubscriptionCreationRequest request) {
    validator.validate(request);
    subscriptionService.subscribe(request.userId(), request.courseId());

    return request;
  }
}
