package it.unisalento.rec.rec_task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_task.dto.SendTaskDTO;
import it.unisalento.rec.rec_task.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_task.dto.TaskBillDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public String sendTask(SendTaskDTO sendTaskDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(sendTaskDTO);
        String response = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_TASK, RabbitMQConfig.SUBMISSION_TASK_KEY, jsonMessage);
        return response;
    }

    public void sendBillTask(TaskBillDTO taskBillDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(taskBillDTO);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_TASK, RabbitMQConfig.BILL_TASK_KEY, jsonMessage);
    }
}
