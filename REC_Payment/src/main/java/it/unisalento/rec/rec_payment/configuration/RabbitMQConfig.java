package it.unisalento.rec.rec_payment.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_SUBMISSION_TASK = "submission-task";
    public static final String QUEUE_PAYMENT_TASK = "payment-task";
    public static final String QUEUE_BUY_REWARD = "buy-reward";
    public static final String QUEUE_PAYMENT_REWARD = "payment-reward";
    public static final String QUEUE_PERFORMANCE_ADMIN = "performance-admin";
    public static final String TOPIC_EXCHANGE_PAYMENT = "topicExchangePayment";
    public static final String FANOUT_EXCHANGE_CREDIT_MEMBER = "fanoutExchangeCreditMember";
    public static final String QUEUE_CREDIT_MEMBER_PAYMENT = "credit-member-payment";
    public static final String SUBMISSION_TASK_KEY = "submissionTaskKey";
    public static final String PAYMENT_TASK_KEY = "paymentTaskKey";
    public static final String BUY_REWARD_KEY = "buyRewardKey";
    public static final String PAYMENT_REWARD_KEY = "paymentRewardKey";
    public static final String PERFORMANCE_ADMIN_KEY = "performanceAdminKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue submissionTaskQueue(){
        return new Queue(QUEUE_SUBMISSION_TASK);
    }

    @Bean
    public Queue paymentRewardQueue(){
        return new Queue(QUEUE_PAYMENT_REWARD);
    }

    @Bean
    public Queue paymentTaskQueue(){
        return new Queue(QUEUE_PAYMENT_TASK);
    }

    @Bean
    public Queue buyRewardQueue(){
        return new Queue(QUEUE_BUY_REWARD);
    }

    @Bean
    public Queue performanceAdminQueue(){
        return new Queue(QUEUE_PERFORMANCE_ADMIN);
    }

    @Bean
    public Queue creditMemberPaymentQueue(){
        return new Queue(QUEUE_CREDIT_MEMBER_PAYMENT);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_PAYMENT);
    }

    @Bean
    FanoutExchange fanoutExchangeCreditMember() {
        return new FanoutExchange(FANOUT_EXCHANGE_CREDIT_MEMBER);
    }

    @Bean
    public Binding submissionTaskBinding(Queue submissionTaskQueue, TopicExchange exchange) {return BindingBuilder.bind(submissionTaskQueue).to(exchange).with(SUBMISSION_TASK_KEY);}

    @Bean
    public Binding paymentTaskBinding(Queue paymentTaskQueue, TopicExchange exchange) {return BindingBuilder.bind(paymentTaskQueue).to(exchange).with(PAYMENT_TASK_KEY);}

    @Bean
    public Binding buyRewardBinding(Queue buyRewardQueue, TopicExchange exchange) {return BindingBuilder.bind(buyRewardQueue).to(exchange).with(BUY_REWARD_KEY);}

    @Bean
    public Binding paymentRewardBinding(Queue paymentRewardQueue, TopicExchange exchange) {return BindingBuilder.bind(paymentRewardQueue).to(exchange).with(PAYMENT_REWARD_KEY);}

    @Bean
    public Binding performanceAdminBinding(Queue performanceAdminQueue, TopicExchange exchange) {return BindingBuilder.bind(performanceAdminQueue).to(exchange).with(PERFORMANCE_ADMIN_KEY);}

    @Bean
    public Binding creditMemberPaymentBinding(Queue creditMemberPaymentQueue, FanoutExchange fanoutExchangeCreditMember) {return BindingBuilder.bind(creditMemberPaymentQueue).to(fanoutExchangeCreditMember);}

}
