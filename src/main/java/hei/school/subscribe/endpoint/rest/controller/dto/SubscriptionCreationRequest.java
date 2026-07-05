package hei.school.subscribe.endpoint.rest.controller.dto;

import java.util.UUID;

public record SubscriptionCreationRequest(
        UUID userId,
        UUID courseId) {
}