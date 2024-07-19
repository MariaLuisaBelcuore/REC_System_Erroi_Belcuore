package it.unisalento.rec.rec_usermanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_usermanagement.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_usermanagement.dto.*;
import it.unisalento.rec.rec_usermanagement.exceptions.OperationNotPermittedException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendClientCreated(ClientRegistrationDTO clientRegistrationDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(clientRegistrationDTO);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_USERMANAGEMENT, RabbitMQConfig.REGISTRAZIONE_CLIENT_KEY, jsonMessage);
        System.out.println("Message sent");
    }
    public void sendMemberCreated(MemberRegistrationDTO memberRegistrationDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(memberRegistrationDTO);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_USERMANAGEMENT, RabbitMQConfig.REGISTRAZIONE_MEMBER_KEY, jsonMessage);
        System.out.println("Message sent");
    }

    public void sendAdminCreated(AdminRegistrationDTO adminRegistrationDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(adminRegistrationDTO);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_USERMANAGEMENT, RabbitMQConfig.REGISTRAZIONE_ADMIN_KEY, jsonMessage);
        System.out.println("Message sent");
    }

    public void sendDeleteUser(String email) throws JsonProcessingException {
        String jsonMessage;
        jsonMessage = email;
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_USERMANAGEMENT_DELETE, "", jsonMessage);
        System.out.println("Message sent");
    }

    public void sendModifyUser(UserDTO userDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(userDTO);
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_USERMANAGEMENT_MODIFY, "", jsonMessage);
        System.out.println("Message sent");
    }

    public Boolean sendVerifyAdminCode(RequestVerifyCodeDTO requestVerifyCodeDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(requestVerifyCodeDTO);
        Boolean response = (Boolean) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_USERMANAGEMENT, RabbitMQConfig.ADMINCODE_REGISTRATION_KEY, jsonMessage);

        assert response != null;
        if(response){
           return true;
       } else {
           throw new OperationNotPermittedException("Code or email not valid");
       }
    }
}
