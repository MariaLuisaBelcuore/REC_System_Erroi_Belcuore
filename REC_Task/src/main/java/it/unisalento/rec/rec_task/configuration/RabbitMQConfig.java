package it.unisalento.rec.rec_task.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_SUBMISSION_TASK = "submission-task";
    public static final String TOPIC_EXCHANGE_TASK = "topicExchangeTask";
    public static final String SUBMISSION_TASK_KEY = "submissionTaskKey";
    public static final String QUEUE_BILL_TASK = "bill-task";
    public static final String BILL_TASK_KEY = "billTaskKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue submissionTaskQueue(){
        return new Queue(QUEUE_SUBMISSION_TASK);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_TASK);
    }

    @Bean
    public Binding submissionTaskBinding(Queue submissionTaskQueue, TopicExchange exchange) {return BindingBuilder.bind(submissionTaskQueue).to(exchange).with(SUBMISSION_TASK_KEY);}

    @Bean
    public Queue billTaskQueue(){
        return new Queue(QUEUE_BILL_TASK);
    }

    @Bean
    public Binding billResourceBinding(Queue billTaskQueue, TopicExchange exchange) {return BindingBuilder.bind(billTaskQueue).to(exchange).with(BILL_TASK_KEY);}
}
