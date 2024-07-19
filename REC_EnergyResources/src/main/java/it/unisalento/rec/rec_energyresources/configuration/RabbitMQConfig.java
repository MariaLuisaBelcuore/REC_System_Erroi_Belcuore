package it.unisalento.rec.rec_energyresources.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE_RESOURCE = "topicExchangeResource";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_DELETE = "fanoutExchangeUserManagementDelete";
    public static final String QUEUE_DELETE_USER_RESOURCE = "delete-user-resource";
    public static final String DELETE_USER_RESOURCE_KEY = "deleteUserResourceKey";
    public static final String FANOUT_EXCHANGE_CREDIT_MEMBER = "fanoutExchangeCreditMember";
    public static final String QUEUE_CREDIT_MEMBER = "credit-member";
    public static final String CREDIT_MEMBER_KEY = "creditmemberKey";
    public static final String QUEUE_PERFORMANCE_MEMBER = "performance-member";
    public static final String PERFORMANCE_MEMBER_KEY = "performanceMemberKey";
    public static final String QUEUE_PERFORMANCE_ADMIN_RESOURCES = "performance-admin-resources";
    public static final String PERFORMANCE_ADMIN_RESOURCES_KEY = "performanceAdminResourcesKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue performanceAdminResourcesQueue(){
        return new Queue(QUEUE_PERFORMANCE_ADMIN_RESOURCES);
    }

    @Bean
    public Queue creditMemberQueue(){
        return new Queue(QUEUE_CREDIT_MEMBER);
    }

    @Bean
    public Queue deleteUserResourceQueue(){
        return new Queue(QUEUE_DELETE_USER_RESOURCE);
    }

    @Bean
    public Queue performanceMemberQueue(){
        return new Queue(QUEUE_PERFORMANCE_MEMBER);
    }

    @Bean
    FanoutExchange fanoutExchangeDelete() {
        return new FanoutExchange(FANOUT_EXCHANGE_USERMANAGEMENT_DELETE);
    }

    @Bean
    FanoutExchange fanoutExchangeCreditMember() {
        return new FanoutExchange(FANOUT_EXCHANGE_CREDIT_MEMBER);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_RESOURCE);
    }

    @Bean
    public Binding deleteUserResourceBinding(Queue deleteUserResourceQueue, FanoutExchange fanoutExchangeDelete) {return BindingBuilder.bind(deleteUserResourceQueue).to(fanoutExchangeDelete);}

    @Bean
    public Binding creditMemberBinding(Queue creditMemberQueue, FanoutExchange fanoutExchangeCreditMember) {return BindingBuilder.bind(creditMemberQueue).to(fanoutExchangeCreditMember);}

    @Bean
    public Binding performanceMemberBinding(Queue performanceMemberQueue, TopicExchange exchange) {return BindingBuilder.bind(performanceMemberQueue).to(exchange).with(PERFORMANCE_MEMBER_KEY);}

    @Bean
    public Binding performanceAdminResourcesBinding(Queue performanceAdminResourcesQueue, TopicExchange exchange) {return BindingBuilder.bind(performanceAdminResourcesQueue).to(exchange).with(PERFORMANCE_ADMIN_RESOURCES_KEY);}
}
