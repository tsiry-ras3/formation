package hei.school.subscribe.endpoint.rest.controller.validator;

import hei.school.subscribe.endpoint.rest.controller.dto.SubscriptionCreationRequest;
import hei.school.subscribe.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionCreationRequestValidator {

  public void validate(SubscriptionCreationRequest request) {
    if (request.userId() == null) {
      throw new BadRequestException("userId must not be null");
    }
    if (request.courseId() == null) {
      throw new BadRequestException("courseId must not be null");
    }
  }
}