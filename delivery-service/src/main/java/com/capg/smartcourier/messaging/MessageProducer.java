package com.capg.smartcourier.messaging;

/*
 *A producer is a any service that sends data to the rabbitMQ
 */

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.smartcourier.config.RabbitMQConfig;
import com.capg.smartcourier.event.TrackingEvent;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTrackingEvent(TrackingEvent event) {
        rabbitTemplate.convertAndSend(  //here we are taking the java object as a event, but we are not converting this Java object, the conversion of the java object into JSON is done in rabbitMQ Config file
                RabbitMQConfig.EXCHANGE, //exchange is a routing center
                RabbitMQConfig.ROUTING_KEY, // decides where message goes
                event
        );
    }
}