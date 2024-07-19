package it.unisalento.rec.rec_wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_wallet.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_wallet.domain.WalletClient;
import it.unisalento.rec.rec_wallet.domain.WalletMember;
import it.unisalento.rec.rec_wallet.dto.ReceiveClientToPayDTO;
import it.unisalento.rec.rec_wallet.dto.ReceiveMemberToPayDTO;
import it.unisalento.rec.rec_wallet.dto.WalletClientDTO;
import it.unisalento.rec.rec_wallet.dto.WalletMemberDTO;
import it.unisalento.rec.rec_wallet.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_wallet.exceptions.WalletNotFoundException;
import it.unisalento.rec.rec_wallet.repositories.WalletClientRepository;
import it.unisalento.rec.rec_wallet.repositories.WalletMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class RabbitMQService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WalletClientRepository walletClientRepository;
    @Autowired
    WalletMemberRepository walletMemberRepository;
    @Autowired
    WalletClientDetailsService walletClientDetailsService;
    @Autowired
    WalletMemberDetailsService walletMemberDetailsService;
    private static final Logger log = LoggerFactory.getLogger(RabbitMQService.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT_TASK)
    public String ReceiveClientToPay(String jsonMessage) throws JsonProcessingException {

        try {
            ReceiveClientToPayDTO receiveClientToPayDTO = objectMapper.readValue(jsonMessage, ReceiveClientToPayDTO.class);

            Optional<WalletClient> optionalWalletClient = walletClientRepository.findByClientEmail(receiveClientToPayDTO.getClientEmail());
            if (optionalWalletClient.isEmpty()) {
                throw new WalletNotFoundException("Non-existent wallet");
            }
            WalletClient walletClient = optionalWalletClient.get();

            if (walletClient.getResidualCredit() < receiveClientToPayDTO.getAmount()) {
                throw new OperationNotPermittedException("Insufficient credit");
            }
            WalletClientDTO walletClientDTO = new WalletClientDTO();
            BeanUtils.copyProperties(walletClient, walletClientDTO, "id", "residualCredit");
            walletClientDTO.setResidualCredit(walletClient.getResidualCredit() - receiveClientToPayDTO.getAmount());
            walletClientDetailsService.updateWalletClient(walletClientDTO);
            return "OK";
        } catch (JsonProcessingException e) {
            log.error("Errore nella deserializzazione del messaggio: ", e);
            throw e;
        } catch (OperationNotPermittedException e) {
            log.error("The wallet is empty: ", e);
            return "WALLET_EMPTY";
        } catch (WalletNotFoundException e){
            log.error("Wallet not found: ", e);
            return "WALLET_NOT_FOUND";
        } catch (Exception e){
            log.error("Errore generico: ", e);
            throw new RuntimeException(e);
        }

    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT_REWARD)
    public String ReceiveMemberToPay(String jsonMessage) throws JsonProcessingException {

        try {
            ReceiveMemberToPayDTO receiveMemberToPayDTO = objectMapper.readValue(jsonMessage, ReceiveMemberToPayDTO.class);

            Optional<WalletMember> optionalWalletMember = walletMemberRepository.findByMemberEmail(receiveMemberToPayDTO.getEmailMember());
            if (optionalWalletMember.isEmpty()) {
                throw new WalletNotFoundException("Non-existent wallet");
            }
            WalletMember walletMember = optionalWalletMember.get();

            if (walletMember.getResidualCredit() < receiveMemberToPayDTO.getCost()) {
                throw new OperationNotPermittedException("Insufficient credits");
            }
            WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
            BeanUtils.copyProperties(walletMember, walletMemberDTO, "id", "residualCredit");
            walletMemberDTO.setResidualCredit(walletMember.getResidualCredit() - receiveMemberToPayDTO.getCost());
            walletMemberDetailsService.updateWalletMember(walletMemberDTO);
            return "OK";
        } catch (JsonProcessingException e) {
            log.error("Errore nella deserializzazione del messaggio: ", e);
            throw e;
        } catch (OperationNotPermittedException e) {
            log.error("The wallet is empty: ", e);
            return "WALLET_EMPTY";
        } catch (WalletNotFoundException e){
            log.error("Wallet not found: ", e);
            return "WALLET_NOT_FOUND";
        } catch (Exception e){
            log.error("Errore generico: ", e);
            throw new RuntimeException(e);
        }

    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DELETE_USER_WALLET)
    public void DeleteUser(String email) throws WalletNotFoundException {
        try {
            walletClientDetailsService.deleteWalletClient(email);
        } catch (WalletNotFoundException e) {
            walletMemberDetailsService.deleteWalletMember(email);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_MODIFY_USER_WALLET)
    public void ModifyUser(String jsonMessage) throws WalletNotFoundException, JsonProcessingException {
        WalletMemberDTO walletMemberDTO = objectMapper.readValue(jsonMessage, WalletMemberDTO.class);
        JsonNode jsonNode = objectMapper.readTree(jsonMessage);
        String additionalField = jsonNode.get("email").asText();
        walletMemberDTO.setMemberEmail(additionalField);
        try {
            Optional<WalletMember> optionalWalletMember = walletMemberRepository.findByMemberEmail(additionalField);
            if (optionalWalletMember.isEmpty()) {
                throw new WalletNotFoundException("Non-existent wallet");
            }
            WalletMember walletMember = optionalWalletMember.get();
            BeanUtils.copyProperties(walletMember, walletMemberDTO, "id","nome","cognome");
            walletMemberDetailsService.updateWalletMember(walletMemberDTO);
        } catch (WalletNotFoundException e) {
            WalletClientDTO walletClientDTO = objectMapper.readValue(jsonMessage, WalletClientDTO.class);
            walletClientDTO.setClientEmail(additionalField);
            Optional<WalletClient> optionalWalletClient = walletClientRepository.findByClientEmail(additionalField);
            if (optionalWalletClient.isEmpty()) {
                throw new WalletNotFoundException("Non-existent wallet");
            }
            WalletClient walletClient = optionalWalletClient.get();
            BeanUtils.copyProperties(walletClient, walletClientDTO, "id","nome","cognome");
            walletClientDetailsService.updateWalletClient(walletClientDTO);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CREDIT_MEMBER_WALLET)
    public void AddCreditToMember(String jsonMessage) throws WalletNotFoundException, JsonProcessingException {
        WalletMemberDTO walletMemberDTO = objectMapper.readValue(jsonMessage, WalletMemberDTO.class);
        JsonNode jsonNode = objectMapper.readTree(jsonMessage);
        int additionalField = Integer.parseInt(jsonNode.get("additionalCredit").asText());

            Optional<WalletMember> optionalWalletMember = walletMemberRepository.findByMemberEmail(walletMemberDTO.getMemberEmail());
            if (optionalWalletMember.isEmpty()) {
                throw new WalletNotFoundException("Non-existent wallet");
            }
            WalletMember walletMember = optionalWalletMember.get();
            BeanUtils.copyProperties(walletMember, walletMemberDTO, "id", "residualCredit");
            walletMemberDTO.setResidualCredit(walletMember.getResidualCredit() + additionalField);
            walletMemberDetailsService.updateWalletMember(walletMemberDTO);
    }
}

