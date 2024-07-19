package it.unisalento.rec.rec_login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.rec.rec_login.configuration.RabbitMQConfig;
import it.unisalento.rec.rec_login.domain.Admin;
import it.unisalento.rec.rec_login.domain.Client;
import it.unisalento.rec.rec_login.domain.Member;
import it.unisalento.rec.rec_login.dto.AdminDTO;
import it.unisalento.rec.rec_login.dto.ClientDTO;
import it.unisalento.rec.rec_login.dto.MemberDTO;
import it.unisalento.rec.rec_login.dto.UserLoginDTO;
import it.unisalento.rec.rec_login.exceptions.UserNotFoundException;
import it.unisalento.rec.rec_login.repositories.AdminRepository;
import it.unisalento.rec.rec_login.repositories.ClientRepository;
import it.unisalento.rec.rec_login.repositories.MemberRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static it.unisalento.rec.rec_login.configuration.SecurityConfig.passwordEncoder;


@Component
public class RabbitMQService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRepository adminRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REGISTRAZIONE_CLIENT)
    public void receiveClient(String jsonMessage){
        try{
            ClientDTO clientDTO = objectMapper.readValue(jsonMessage, ClientDTO.class);
            Client client = new Client();
            client.setEmail(clientDTO.getEmail());
            client.setPassword(passwordEncoder().encode(clientDTO.getPassword()));
            client.setRuolo(clientDTO.getRuolo());

            client = clientRepository.save(client);
            clientDTO.setId(client.getId());
            clientDTO.setPassword(null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REGISTRAZIONE_MEMBER)
    public void receiveMember(String jsonMessage){
        try{
            MemberDTO memberDTO = objectMapper.readValue(jsonMessage, MemberDTO.class);
            Member member = new Member();
            member.setEmail(memberDTO.getEmail());
            member.setPassword(passwordEncoder().encode(memberDTO.getPassword()));
            member.setRuolo(memberDTO.getRuolo());

            member = memberRepository.save(member);
            memberDTO.setId(member.getId());
            memberDTO.setPassword(null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REGISTRAZIONE_ADMIN)
    public void receiveAdmin(String jsonMessage){
        try{
            AdminDTO adminDTO = objectMapper.readValue(jsonMessage, AdminDTO.class);
            Admin admin= new Admin();
            admin.setEmail(adminDTO.getEmail());
            admin.setPassword(passwordEncoder().encode(adminDTO.getPassword()));
            admin.setRuolo(adminDTO.getRuolo());

            admin = adminRepository.save(admin);
            adminDTO.setId(admin.getId());
            adminDTO.setPassword(null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DELETE_USER_LOGIN)
    public void deleteUser(String email) throws UserNotFoundException {
        if (adminRepository.existsByEmail(email)) {
            adminRepository.deleteByEmail(email);
        } else if (memberRepository.existsByEmail(email)) {
            memberRepository.deleteByEmail(email);
        } else if (clientRepository.existsByEmail(email)) {
            clientRepository.deleteByEmail(email);
        } else {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_MODIFY_USER_LOGIN)
    public void modifyUser(String jsonMessage) throws UserNotFoundException, JsonProcessingException {

        UserLoginDTO userLoginDTO = objectMapper.readValue(jsonMessage, UserLoginDTO.class);

        if (adminRepository.existsByEmail(userLoginDTO.getEmail())) {
            adminRepository.deleteByEmail(userLoginDTO.getEmail());
            Admin admin = new Admin();
            admin.setEmail(userLoginDTO.getEmail());
            admin.setPassword(passwordEncoder().encode(userLoginDTO.getPassword()));
            admin.setRuolo("MEMBER");
            admin = adminRepository.save(admin);
        } else if (memberRepository.existsByEmail(userLoginDTO.getEmail())) {
            memberRepository.deleteByEmail(userLoginDTO.getEmail());
            Member member = new Member();
            member.setEmail(userLoginDTO.getEmail());
            member.setPassword(passwordEncoder().encode(userLoginDTO.getPassword()));
            member.setRuolo("MEMBER");
            member = memberRepository.save(member);
        } else if (clientRepository.existsByEmail(userLoginDTO.getEmail())) {
            clientRepository.deleteByEmail(userLoginDTO.getEmail());
            Client client = new Client();
            client.setEmail(userLoginDTO.getEmail());
            client.setPassword(passwordEncoder().encode(userLoginDTO.getPassword()));
            client.setRuolo("CLIENT");
            client = clientRepository.save(client);
        } else {
            throw new UserNotFoundException("User with email " + userLoginDTO.getEmail() + " not found");
        }
    }
}
