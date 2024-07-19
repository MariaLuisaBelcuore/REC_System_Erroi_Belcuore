package it.unisalento.rec.rec_reward.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_BUY_REWARD = "buy-reward";
    public static final String TOPIC_EXCHANGE_REWARD = "topicExchangeReward";
    public static final String BUY_REWARD_KEY = "buyRewardKey";

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public Queue buyRewardQueue(){
        return new Queue(QUEUE_BUY_REWARD);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_REWARD);
    }

    @Bean
    public Binding buyRewardBinding(Queue buyRewardQueue, TopicExchange exchange) {return BindingBuilder.bind(buyRewardQueue).to(exchange).with(BUY_REWARD_KEY);}
}
