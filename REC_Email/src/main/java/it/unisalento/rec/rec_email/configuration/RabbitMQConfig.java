package it.unisalento.rec.rec_email.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_BILL_TASK = "bill-task";
    public static final String QUEUE_ADMINCODE_REGISTRATION = "admincode-registration";
    public static final String TOPIC_EXCHANGE_EMAIL = "topicExchangeEmail";
    public static final String BILL_TASK_KEY = "billTaskKey";
    public static final String ADMINCODE_REGISTRATION_KEY = "adminCodeRegistrationKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue billResourceQueue(){
        return new Queue(QUEUE_BILL_TASK);
    }

    @Bean
    public Queue verifyAdminRegistrationQueue(){
        return new Queue(QUEUE_ADMINCODE_REGISTRATION);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_EMAIL);
    }

    @Bean
    public Binding billResourceBinding(Queue billResourceQueue, TopicExchange exchange) {return BindingBuilder.bind(billResourceQueue).to(exchange).with(BILL_TASK_KEY);}

    @Bean
    public Binding verifyAdminRegistrationBinding(Queue verifyAdminRegistrationQueue, TopicExchange exchange) {return BindingBuilder.bind(verifyAdminRegistrationQueue).to(exchange).with(ADMINCODE_REGISTRATION_KEY);}
}
