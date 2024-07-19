package it.unisalento.rec.rec_wallet.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PAYMENT_TASK = "payment-task";
    public static final String QUEUE_PAYMENT_REWARD = "payment-reward";
    public static final String TOPIC_EXCHANGE_WALLET = "topicExchangeWallet";
    public static final String PAYMENT_TASK_KEY = "paymentTaskKey";
    public static final String PAYMENT_REWARD_KEY = "paymentRewardKey";
    public static final String QUEUE_DELETE_USER_WALLET = "delete-user-wallet";
    public static final String DELETE_USER_WALLET_KEY = "deleteUserWalletKey";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_DELETE = "fanoutExchangeUserManagementDelete";
    public static final String FANOUT_EXCHANGE_USERMANAGEMENT_MODIFY = "fanoutExchangeUserManagementModify";
    public static final String QUEUE_MODIFY_USER_WALLET = "modify-user-wallet";
    public static final String MODIFY_USER_WALLET_KEY = "modifyUserWalletKey";
    public static final String QUEUE_CREDIT_MEMBER_WALLET = "credit-member-wallet";
    public static final String FANOUT_EXCHANGE_CREDIT_MEMBER = "fanoutExchangeCreditMember";
    public static final String CREDIT_MEMBER_KEY = "creditmemberKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue paymentTaskQueue(){
        return new Queue(QUEUE_PAYMENT_TASK);
    }

    @Bean
    public Queue paymentRewardQueue(){
        return new Queue(QUEUE_PAYMENT_REWARD);
    }

    @Bean
    public Queue deleteUserWalletQueue(){
        return new Queue(QUEUE_DELETE_USER_WALLET);
    }

    @Bean
    public Queue modifyUserWalletQueue(){
        return new Queue(QUEUE_MODIFY_USER_WALLET);
    }

    @Bean
    public Queue creditMemberWalletQueue(){
        return new Queue(QUEUE_CREDIT_MEMBER_WALLET);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_WALLET);
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
    FanoutExchange fanoutExchangeCreditMember() {
        return new FanoutExchange(FANOUT_EXCHANGE_CREDIT_MEMBER);
    }

    @Bean
    public Binding paymentTaskBinding(Queue paymentTaskQueue, TopicExchange exchange) {return BindingBuilder.bind(paymentTaskQueue).to(exchange).with(PAYMENT_TASK_KEY);}

    @Bean
    public Binding paymentRewardBinding(Queue paymentRewardQueue, TopicExchange exchange) {return BindingBuilder.bind(paymentRewardQueue).to(exchange).with(PAYMENT_REWARD_KEY);}

    @Bean
    public Binding deleteUserWalletBinding(Queue deleteUserWalletQueue, FanoutExchange fanoutExchangeDelete) {return BindingBuilder.bind(deleteUserWalletQueue).to(fanoutExchangeDelete);}

    @Bean
    public Binding modifyUserWalletBinding(Queue modifyUserWalletQueue, FanoutExchange fanoutExchangeModify) {return BindingBuilder.bind(modifyUserWalletQueue).to(fanoutExchangeModify);}

    @Bean
    public Binding creditMemberWalletBinding(Queue creditMemberWalletQueue, FanoutExchange fanoutExchangeCreditMember) {return BindingBuilder.bind(creditMemberWalletQueue).to(fanoutExchangeCreditMember);}
}
