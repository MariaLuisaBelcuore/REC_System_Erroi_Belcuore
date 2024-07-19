package it.unisalento.rec.rec_performance.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_performance.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_performance.dto.InfoToPerformanceDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public InfoToPerformanceDTO sendRequestPerformance(String memberEmail) throws JsonProcessingException {
        String response = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_PERFORMANCE, RabbitMQConfig.PERFORMANCE_MEMBER_KEY, memberEmail);
        return objectMapper.readValue(response, InfoToPerformanceDTO.class);
    }

    public Float sendRequestEarrings() {
        return (Float) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_PERFORMANCE, RabbitMQConfig.PERFORMANCE_ADMIN_KEY, "");
    }

    public InfoToPerformanceDTO sendRequestPerformanceForAdmin() throws JsonProcessingException {
        String response = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_PERFORMANCE, RabbitMQConfig.PERFORMANCE_ADMIN_RESOURCES_KEY, "");
        return objectMapper.readValue(response, InfoToPerformanceDTO.class);
    }
}
