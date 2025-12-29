package com.renault.garage.messaging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "messaging")
public class MessagingProperties {
    private String exchange;
    private String vehicleQueueName;
    private String vehicleRoutingKey;
}
