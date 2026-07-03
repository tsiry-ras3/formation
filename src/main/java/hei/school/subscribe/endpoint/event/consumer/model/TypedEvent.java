package hei.school.subscribe.endpoint.event.consumer.model;

import hei.school.subscribe.PojaGenerated;
import hei.school.subscribe.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
