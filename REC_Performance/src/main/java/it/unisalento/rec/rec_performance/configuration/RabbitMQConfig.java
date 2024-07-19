package it.unisalento.rec.rec_performance.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_PERFORMANCE_MEMBER = "performance-member";
    public static final String QUEUE_PERFORMANCE_ADMIN = "performance-admin";
    public static final String QUEUE_PERFORMANCE_ADMIN_RESOURCES = "performance-admin-resources";
    public static final String TOPIC_EXCHANGE_PERFORMANCE = "topicExchangePerformance";
    public static final String PERFORMANCE_MEMBER_KEY = "performanceMemberKey";
    public static final String PERFORMANCE_ADMIN_KEY = "performanceAdminKey";
    public static final String PERFORMANCE_ADMIN_RESOURCES_KEY = "performanceAdminResourcesKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue performanceAdminResourcesQueue(){
        return new Queue(QUEUE_PERFORMANCE_ADMIN_RESOURCES);
    }

    @Bean
    public Queue performanceAdminQueue(){
        return new Queue(QUEUE_PERFORMANCE_ADMIN);
    }

    @Bean
    public Queue performanceMemberQueue(){
        return new Queue(QUEUE_PERFORMANCE_MEMBER);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_PERFORMANCE);
    }

    @Bean
    public Binding performanceMemberBinding(Queue performanceMemberQueue, TopicExchange exchange) {return BindingBuilder.bind(performanceMemberQueue).to(exchange).with(PERFORMANCE_MEMBER_KEY);}

    @Bean
    public Binding performanceAdminBinding(Queue performanceAdminQueue, TopicExchange exchange) {return BindingBuilder.bind(performanceAdminQueue).to(exchange).with(PERFORMANCE_ADMIN_KEY);}

    @Bean
    public Binding performanceAdminResourcesBinding(Queue performanceAdminResourcesQueue, TopicExchange exchange) {return BindingBuilder.bind(performanceAdminResourcesQueue).to(exchange).with(PERFORMANCE_ADMIN_RESOURCES_KEY);}
}
