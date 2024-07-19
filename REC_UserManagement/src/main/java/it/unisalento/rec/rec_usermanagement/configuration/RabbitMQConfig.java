package it.unisalento.rec.rec_usermanagement.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_REGISTRAZIONE_ADMIN = "registrazione-admin";
    public static final String QUEUE_REGISTRAZIONE_CLIENT = "registrazione-client";
    public static final String QUEUE_REGISTRAZIONE_MEMBER = "registrazione-member";
    public static final String QUEUE_ADMINCODE_REGISTRATION = "admincode-registration";
    public static final String TOPIC_EXCHANGE_USERMANAGEMENT = "topicExchangeUserManagement";
    public static final String REGISTRAZIONE_CLIENT_KEY = "registrationClientKey";
    public static final String REGISTRAZIONE_MEMBER_KEY = "registrationMemberKey";
    public static final String REGISTRAZIONE_ADMIN_KEY = "registrationAdminKey";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_DELETE = "fanoutExchangeUserManagementDelete";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_MODIFY = "fanoutExchangeUserManagementModify";
    public static final String QUEUE_DELETE_USER = "delete-user";
    public static final String DELETE_USER_KEY = "deleteUserKey";
    public static final String QUEUE_MODIFY_USER = "modify-user";
    public static final String MODIFY_USER_KEY = "modifyUserKey";
    public static final String ADMINCODE_REGISTRATION_KEY = "adminCodeRegistrationKey";


    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue registrazioneAdminQueue(){
        return new Queue(QUEUE_REGISTRAZIONE_ADMIN);
    }

    @Bean
    public Queue registrazioneClientQueue(){
        return new Queue(QUEUE_REGISTRAZIONE_CLIENT);
    }

    @Bean
    public Queue registrazioneMemberQueue(){
        return new Queue(QUEUE_REGISTRAZIONE_MEMBER);
    }

    @Bean
    public Queue verifyAdminRegistrationQueue(){
        return new Queue(QUEUE_ADMINCODE_REGISTRATION);
    }

    @Bean
    public Queue deleteUserQueue(){
        return new Queue(QUEUE_DELETE_USER);
    }

    @Bean
    public Queue modifyUserQueue(){
        return new Queue(QUEUE_MODIFY_USER);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_USERMANAGEMENT);
    }

    @Bean
    FanoutExchange fanoutExchangeDelete() {
        return new FanoutExchange(FANOUT_EXCHANGE_USERMANAGEMENT_DELETE);
    }

    @Bean
    FanoutExchange fanoutExchangeModify() {
        return new FanoutExchange(FANOUT_EXCHANGE_USERMANAGEMENT_MODIFY);
    }

    @Bean
    public Binding registrationAdminBinding(Queue registrazioneAdminQueue, TopicExchange exchange) {return BindingBuilder.bind(registrazioneAdminQueue).to(exchange).with(REGISTRAZIONE_ADMIN_KEY);}

    @Bean
    public Binding registrationClientBinding(Queue registrazioneClientQueue, TopicExchange exchange) {return BindingBuilder.bind(registrazioneClientQueue).to(exchange).with(REGISTRAZIONE_CLIENT_KEY);}

    @Bean
    public Binding registrationMemberBinding(Queue registrazioneMemberQueue, TopicExchange exchange) {return BindingBuilder.bind(registrazioneMemberQueue).to(exchange).with(REGISTRAZIONE_MEMBER_KEY);}

    @Bean
    public Binding deleteUserBinding(Queue deleteUserQueue, FanoutExchange fanoutExchangeDelete) {return BindingBuilder.bind(deleteUserQueue).to(fanoutExchangeDelete);}

    @Bean
    public Binding modifyUserBinding(Queue modifyUserQueue, FanoutExchange fanoutExchangeModify) {return BindingBuilder.bind(modifyUserQueue).to(fanoutExchangeModify);}

    @Bean
    public Binding verifyAdminRegistrationBinding(Queue verifyAdminRegistrationQueue, TopicExchange exchange) {return BindingBuilder.bind(verifyAdminRegistrationQueue).to(exchange).with(ADMINCODE_REGISTRATION_KEY);}
}
