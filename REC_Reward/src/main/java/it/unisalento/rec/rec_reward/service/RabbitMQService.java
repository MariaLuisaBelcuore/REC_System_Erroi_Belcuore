package it.unisalento.rec.rec_reward.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_reward.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_reward.dto.SendRewardDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public String sendRewardToPay(SendRewardDTO sendRewardDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(sendRewardDTO);
        String response = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_REWARD, RabbitMQConfig.BUY_REWARD_KEY, jsonMessage);
        return response;
    }
}
