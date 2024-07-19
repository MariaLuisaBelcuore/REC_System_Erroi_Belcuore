package it.unisalento.rec.rec_payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_payment.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_payment.domain.ClientPayment;
import it.unisalento.rec.rec_payment.domain.MemberPayment;
import it.unisalento.rec.rec_payment.dto.*;
import it.unisalento.rec.rec_payment.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_payment.repositories.ClientPaymentRepository;
import it.unisalento.rec.rec_payment.repositories.MemberPaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RabbitMQService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClientPaymentRepository clientPaymentRepository;
    @Autowired
    MemberPaymentRepository memberPaymentRepository;
    @Autowired
    ClientPaymentDetailsService clientPaymentDetailsService;
    @Autowired
    MemberPaymentDetailsService memberPaymentDetailsService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    public String sendClientPayment(SendClientPaymentDTO sendClientPaymentDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(sendClientPaymentDTO);
        return (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_PAYMENT, RabbitMQConfig.PAYMENT_TASK_KEY, jsonMessage);
    }

    public String sendMemberPayment(ReceiveMemberPaymentDTO receiveMemberPaymentDTO) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(receiveMemberPaymentDTO);
        return (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE_PAYMENT, RabbitMQConfig.PAYMENT_REWARD_KEY, jsonMessage);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SUBMISSION_TASK)
    public String ReceiveClientPayment(String jsonMessage) throws JsonProcessingException, OperationNotPermittedException {

        try{
            ReceiveClientPaymentDTO receiveClientPaymentDTO = objectMapper.readValue(jsonMessage, ReceiveClientPaymentDTO.class);
            SendClientPaymentDTO sendClientPaymentDTO = new SendClientPaymentDTO();
            sendClientPaymentDTO.setAmount(receiveClientPaymentDTO.getExecutionTime() * 2);
            sendClientPaymentDTO.setClientEmail(receiveClientPaymentDTO.getClientEmail());

            String response = sendClientPayment(sendClientPaymentDTO);

            if(!response.equals("OK")) {
                throw new OperationNotPermittedException("Insufficient credit to submit task");
            }

            ClientPaymentDTO clientPaymentDTO = new ClientPaymentDTO();
            clientPaymentDTO.setClientEmail(sendClientPaymentDTO.getClientEmail());
            clientPaymentDTO.setAmount(sendClientPaymentDTO.getAmount());
            clientPaymentDetailsService.createClientPayment(clientPaymentDTO);
            return "OK";
        } catch (JsonProcessingException e) {
            log.error("Errore nella deserializzazione del messaggio: ", e);
            throw e;
        } catch (OperationNotPermittedException e){
            log.error("PAYMENT NOT AVAILABLE: ", e);
            return "PAYMENT_NOT_AVAILABLE";
        } catch (Exception e){
            log.error("Errore generico: ", e);
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BUY_REWARD)
    public String ReceiveMemberPayment(String jsonMessage) throws JsonProcessingException {

        try{
            ReceiveMemberPaymentDTO receiveMemberPaymentDTO = objectMapper.readValue(jsonMessage, ReceiveMemberPaymentDTO.class);
            String response = sendMemberPayment(receiveMemberPaymentDTO);

            if(!response.equals("OK")) {
                throw new OperationNotPermittedException("Insufficient credits");
            }

            MemberPaymentDTO memberPaymentDTO = new MemberPaymentDTO();
            memberPaymentDTO.setMemberEmail(receiveMemberPaymentDTO.getEmailMember());
            memberPaymentDTO.setAmount(receiveMemberPaymentDTO.getCost());
            memberPaymentDetailsService.createMemberPayment(memberPaymentDTO);
            return "OK";
        } catch (JsonProcessingException e) {
            log.error("Errore nella deserializzazione del messaggio: ", e);
            throw e;
        } catch (OperationNotPermittedException e){
            log.error("PAYMENT NOT AVAILABLE: ", e);
            return "PAYMENT_NOT_AVAILABLE";
        } catch (Exception e){
            log.error("Errore generico: ", e);
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PERFORMANCE_ADMIN)
    public float ResponseToPerformanceRequest(String message) {
        List<ClientPayment> list = clientPaymentRepository.findAll();

        float totalEarnings = 0;

        for(ClientPayment clientPayment : list) totalEarnings += clientPayment.getAmount();
        return totalEarnings;
    }


    @RabbitListener(queues = RabbitMQConfig.QUEUE_CREDIT_MEMBER_PAYMENT)
    public void ReceiveMemberCredit(String jsonMessage) throws JsonProcessingException {
            ReceiveMemberCreditDTO receiveMemberCreditDTO = objectMapper.readValue(jsonMessage, ReceiveMemberCreditDTO.class);
            MemberPayment memberPayment = new MemberPayment();
            memberPayment.setCausal("credit to member");
            memberPayment.setEmissionDate(LocalDateTime.now());
            memberPayment.setAmount(receiveMemberCreditDTO.getAdditionalCredit());
            memberPayment.setMemberEmail(receiveMemberCreditDTO.getMemberEmail());
            memberPayment.setPaymentorcredit("Credit");

            memberPaymentRepository.save(memberPayment);

    }



}
