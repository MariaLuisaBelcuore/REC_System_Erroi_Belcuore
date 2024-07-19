package it.unisalento.rec.rec_usermanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_usermanagement.domain.Admin;
import it.unisalento.rec.rec_usermanagement.domain.Client;
import it.unisalento.rec.rec_usermanagement.domain.Member;
import it.unisalento.rec.rec_usermanagement.dto.AdminRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.ClientRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.MemberRegistrationDTO;
import it.unisalento.rec.rec_usermanagement.dto.RequestVerifyCodeDTO;
import it.unisalento.rec.rec_usermanagement.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_usermanagement.repositories.AdminRepository;
import it.unisalento.rec.rec_usermanagement.repositories.ClientRepository;
import it.unisalento.rec.rec_usermanagement.repositories.MemberRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationDetailsService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private RabbitMQService rabbitMQService;

    public ClientRegistrationDTO createClient(ClientRegistrationDTO clientRegistrationDTO) throws JsonProcessingException, OperationNotPermittedException {
        if(clientRepository.existsByEmail(clientRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The client with email " + clientRegistrationDTO.getEmail() + " already exists");
        }else if(adminRepository.existsByEmail(clientRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The admin with email " + clientRegistrationDTO.getEmail() + " already exists");
        }else if(memberRepository.existsByEmail(clientRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The member with email " + clientRegistrationDTO.getEmail() + " already exists");
        }
        Client client = new Client();
        BeanUtils.copyProperties(clientRegistrationDTO, client, "id");
        client = clientRepository.save(client);
        clientRegistrationDTO.setId(client.getId());
        rabbitMQService.sendClientCreated(clientRegistrationDTO);
        clientRegistrationDTO.setPassword(null);
        return clientRegistrationDTO;
    }

    public MemberRegistrationDTO createMember(MemberRegistrationDTO memberRegistrationDTO) throws JsonProcessingException, OperationNotPermittedException {
        if(clientRepository.existsByEmail(memberRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The client with email " + memberRegistrationDTO.getEmail() + " already exists");
        }else if(adminRepository.existsByEmail(memberRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The admin with email " + memberRegistrationDTO.getEmail() + " already exists");
        }else if(memberRepository.existsByEmail(memberRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The member with email " + memberRegistrationDTO.getEmail() + " already exists");
        }
        Member member = new Member();
        BeanUtils.copyProperties(memberRegistrationDTO, member, "id");
        member = memberRepository.save(member);
        memberRegistrationDTO.setId(member.getId());
        rabbitMQService.sendMemberCreated(memberRegistrationDTO);
        memberRegistrationDTO.setPassword(null);
        return memberRegistrationDTO;
    }

    public AdminRegistrationDTO createAdmin(AdminRegistrationDTO adminRegistrationDTO, String code) throws JsonProcessingException, OperationNotPermittedException {
        RequestVerifyCodeDTO requestVerifyCodeDTO = new RequestVerifyCodeDTO();
        requestVerifyCodeDTO.setCode(code);
        requestVerifyCodeDTO.setEmail(adminRegistrationDTO.getEmail());
        Boolean response = rabbitMQService.sendVerifyAdminCode(requestVerifyCodeDTO);

        if(!response) {
            throw new OperationNotPermittedException("Code or email not valid");
        }

        if(clientRepository.existsByEmail(adminRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The client with email " + adminRegistrationDTO.getEmail() + " already exists");
        }else if(adminRepository.existsByEmail(adminRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The admin with email " + adminRegistrationDTO.getEmail() + " already exists");
        }else if(memberRepository.existsByEmail(adminRegistrationDTO.getEmail())) {
            throw new OperationNotPermittedException("The member with email " + adminRegistrationDTO.getEmail() + " already exists");
        }
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminRegistrationDTO, admin, "id");
        admin = adminRepository.save(admin);
        adminRegistrationDTO.setId(admin.getId());
        rabbitMQService.sendAdminCreated(adminRegistrationDTO);
        adminRegistrationDTO.setPassword(null);
        return adminRegistrationDTO;
    }
}
