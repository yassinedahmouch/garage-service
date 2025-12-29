package com.renault.garage.messaging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.renault.garage.dto.vehicle.VehicleEventDTO;

@Component
@Slf4j
public class VehicleConsumer {

    @RabbitListener(queues = "${messaging.vehicle-queue-name}")
    public void consumeVehicleAdded(VehicleEventDTO event) {
        log.info("Received vehicle added event: {}", event);
    }
}
