package it.unisalento.rec.rec_email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_email.dto.EmailBillDTO;
import it.unisalento.rec.rec_email.dto.ReceiveCodeToVerifyDTO;
import it.unisalento.rec.rec_email.exceptions.OperationNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import it.unisalento.rec.rec_email.configuration.RabbitMQConfig;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EmailDetailsService emailDetailsService;
    @Autowired
    TemporaryCodeDetailsService temporaryCodeDetailsService;
    private static final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BILL_TASK)
    public void receiveTaskBill(String jsonMessage) throws JsonProcessingException {
        log.info("Ricevuto messaggio: {}", jsonMessage);
        try {
            EmailBillDTO emailBillDTO = objectMapper.readValue(jsonMessage, EmailBillDTO.class);
            log.info("Messaggio deserializzato correttamente: {}", emailBillDTO);
            emailDetailsService.sendEmailBill(emailBillDTO);
            log.info("Email inviata correttamente a {}", emailBillDTO.getClientEmail());
        } catch (JsonProcessingException e) {
            log.error("Errore nella deserializzazione del messaggio: ", e);
            throw e;
        } catch (Exception e) {
            log.error("Errore generico: ", e);
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ADMINCODE_REGISTRATION)
    public boolean receiveAdminCode(String jsonMessage) throws JsonProcessingException, OperationNotPermittedException {
        log.info("Ricevuto messaggio: {}", jsonMessage);
        ReceiveCodeToVerifyDTO receiveCodeToVerifyDTO = objectMapper.readValue(jsonMessage, ReceiveCodeToVerifyDTO.class);
        log.info("Messaggio deserializzato correttamente: {}", receiveCodeToVerifyDTO);
        return temporaryCodeDetailsService.validateCode(receiveCodeToVerifyDTO.getCode(), receiveCodeToVerifyDTO.getEmail());
    }
}

