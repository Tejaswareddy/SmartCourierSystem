package com.capg.smartcourier.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "tracking.queue"; // Creates a queue object naming teacking_queue
    public static final String EXCHANGE = "tracking.exchange";
    public static final String ROUTING_KEY = "tracking.routingKey";

    @Bean // Spring will register this queue into rabbitMQ manually
    public Queue queue() {
        return new Queue(QUEUE);
    }
    /*
     * for converting our message into JSON
     * when ever we are calling the rabbitTemplate.convertandSend,
     *  it will check if there is any jsonconverter and then it will convert
     *  If there is no message converter, then it will use default converter and 
     *  convert them into bytes, and the consumer will fail read it properly
     */
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() { 
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange exchange() { //DirectExchange is used for routing the message in the queue based on tracking.routingkey
        return new DirectExchange(EXCHANGE);
    }
    
    /*
     * In binding we will connect everything
     * bind(queue) - connect queue
     * to(exchange) - with this exchange as a center
     * with(Routing_key) - with the routing rules
     */

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}