package it.unisalento.rec.rec_login.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_REGISTRAZIONE_ADMIN = "registrazione-admin";
    public static final String QUEUE_REGISTRAZIONE_CLIENT = "registrazione-client";
    public static final String QUEUE_REGISTRAZIONE_MEMBER = "registrazione-member";
    public static final String TOPIC_EXCHANGE_LOGIN= "topicExchangeLogin";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_DELETE = "fanoutExchangeUserManagementDelete";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_MODIFY = "fanoutExchangeUserManagementModify";
    public static final String REGISTRAZIONE_CLIENT_KEY = "registrationClientKey";
    public static final String REGISTRAZIONE_MEMBER_KEY = "registrationMemberKey";
    public static final String REGISTRAZIONE_ADMIN_KEY = "registrationAdminKey";
    public static final String QUEUE_DELETE_USER_LOGIN = "delete-user-login";
    public static final String DELETE_USER_LOGIN_KEY = "deleteUserLoginKey";
    public static final String QUEUE_MODIFY_USER_LOGIN = "modify-user-login";
    public static final String MODIFY_USER_LOGIN_KEY = "modifyUserLoginKey";


    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue registrazioneAdminQueue(){return new Queue(QUEUE_REGISTRAZIONE_ADMIN);}

    @Bean
    public Queue registrazioneClientQueue(){
        return new Queue(QUEUE_REGISTRAZIONE_CLIENT);
    }

    @Bean
    public Queue registrazioneMemberQueue(){
        return new Queue(QUEUE_REGISTRAZIONE_MEMBER);
    }

    @Bean
    public Queue deleteUserLoginQueue(){
        return new Queue(QUEUE_DELETE_USER_LOGIN);
    }

    @Bean
    public Queue modifyUserLoginQueue(){
        return new Queue(QUEUE_MODIFY_USER_LOGIN);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_LOGIN);
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
    public Binding deleteUserLoginBinding(Queue deleteUserLoginQueue, FanoutExchange fanoutExchangeDelete) {return BindingBuilder.bind(deleteUserLoginQueue).to(fanoutExchangeDelete);}

    @Bean
    public Binding modifyUserLoginBinding(Queue modifyUserLoginQueue, FanoutExchange fanoutExchangeModify) {return BindingBuilder.bind(modifyUserLoginQueue).to(fanoutExchangeModify);}
}
