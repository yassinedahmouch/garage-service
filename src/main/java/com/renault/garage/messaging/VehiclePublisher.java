package com.renault.garage.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.renault.garage.dto.vehicle.VehicleEventDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiclePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public void publishVehicleAdded(VehicleEventDTO vehicleEvent) {
        log.info("Publishing vehicle added event: {}", vehicleEvent);
        rabbitTemplate.convertAndSend(
                messagingProperties.getExchange(),
                messagingProperties.getVehicleRoutingKey(),
                vehicleEvent
        );
    }
}
