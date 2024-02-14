package com.omegalambdang.rentanitem.feature.account.rentor.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RentorAccountCreatedEventsPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    public void publishCreateRentorEvent(RentorAccountCreatedEvent createRentorAccountEvent) {
        applicationEventPublisher.publishEvent(createRentorAccountEvent);
    }
}
