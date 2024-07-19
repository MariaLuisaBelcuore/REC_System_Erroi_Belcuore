package it.unisalento.rec.rec_usermanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_usermanagement.domain.Admin;
import it.unisalento.rec.rec_usermanagement.domain.Client;
import it.unisalento.rec.rec_usermanagement.domain.Member;
import it.unisalento.rec.rec_usermanagement.dto.UserDTO;
import it.unisalento.rec.rec_usermanagement.dto.UserListDTO;
import it.unisalento.rec.rec_usermanagement.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_usermanagement.exceptions.UserNotFoundException;
import it.unisalento.rec.rec_usermanagement.repositories.AdminRepository;
import it.unisalento.rec.rec_usermanagement.repositories.ClientRepository;
import it.unisalento.rec.rec_usermanagement.repositories.MemberRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagementDetailsService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private RabbitMQService rabbitMQService;

    public String deleteUser(String email) throws UserNotFoundException, JsonProcessingException {
        final Client client = clientRepository.findClientByEmail(email);
        if (client == null) {
            final Member member = memberRepository.findMemberByEmail(email);
            if (member == null) {
                throw new UserNotFoundException("User with email " + email + " not found");
            }
            memberRepository.deleteByEmail(email);
            rabbitMQService.sendDeleteUser(email);
            return email;
        }
        clientRepository.deleteByEmail(email);
        rabbitMQService.sendDeleteUser(email);
        return email;
    }

    public String deleteForAdmin(String email) throws UserNotFoundException, OperationNotPermittedException, JsonProcessingException {
        final Client client = clientRepository.findClientByEmail(email); //in questi non faccio la send a rabbit perchè l'admin puo bannare utenti senza eliminare anche wallet ecc
        if(client == null) {
            final Member member = memberRepository.findMemberByEmail(email);
            if (member == null) {
                final Admin admin = adminRepository.findAdminByEmail(email);
                if (admin == null) {
                    throw new UserNotFoundException("User with email " + email + " not found");
                }else if(admin.getEmail().equals(email)){
                    adminRepository.deleteByEmail(email);
                    rabbitMQService.sendDeleteUser(email);
                    return email;
                }
                throw new OperationNotPermittedException("the email " + email + " is not equal to the admin");
            }
                memberRepository.deleteByEmail(email);
                rabbitMQService.sendDeleteUser(email);
                return email;
            }
            clientRepository.deleteByEmail(email);
            rabbitMQService.sendDeleteUser(email);
            return email;
    }

    public UserDTO updateUser(String id, UserDTO userDTO) throws UserNotFoundException, JsonProcessingException {
        Client client = clientRepository.findClientById(id);
        if (client == null) {
            Member member = memberRepository.findMemberById(id);
            if (member == null) {
                throw new UserNotFoundException("User with id " + id + " not found");
            }
            BeanUtils.copyProperties(userDTO, member, "id");
            member = memberRepository.save(member);
            rabbitMQService.sendModifyUser(userDTO);
            userDTO.setId(member.getId());
            return userDTO;
        }
        BeanUtils.copyProperties(userDTO, client, "id");
        client = clientRepository.save(client);
        rabbitMQService.sendModifyUser(userDTO);
        userDTO.setId(client.getId());
        return userDTO;
    }

    public UserDTO updateForAdmin(String id, UserDTO userDTO, String email) throws UserNotFoundException, OperationNotPermittedException, JsonProcessingException {
        Client client = clientRepository.findClientById(id);
        if(client == null) {
            Member member = memberRepository.findMemberById(id);
            if (member == null) {
                Admin admin = adminRepository.findAdminById(id);
                if (admin == null) {
                    throw new UserNotFoundException("User with email " + email + " not found");
                }else if(admin.getEmail().equals(email)){
                    BeanUtils.copyProperties(userDTO, admin, "id");
                    admin = adminRepository.save(admin);
                    rabbitMQService.sendModifyUser(userDTO);
                    userDTO.setId(admin.getId());
                    return userDTO;
                }
                throw new OperationNotPermittedException("the email " + email + " is not equal to the admin");
            }
            BeanUtils.copyProperties(userDTO, member, "id");
            member = memberRepository.save(member);
            rabbitMQService.sendModifyUser(userDTO);
            userDTO.setId(member.getId());
            return userDTO;
        }
        BeanUtils.copyProperties(userDTO, client, "id");
        client = clientRepository.save(client);
        rabbitMQService.sendModifyUser(userDTO);
        userDTO.setId(client.getId());
        return userDTO;
    }

    public UserListDTO searchAllClient() throws UserNotFoundException {
        UserListDTO userListDTO = new UserListDTO();
        ArrayList<UserDTO> clients = new ArrayList<>();
        userListDTO.setList(clients);
        List<Client> list = clientRepository.findAll();
        if(list.isEmpty()){
            throw new UserNotFoundException("The Client list is empty");
        }
        for (Client client : list) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(client, userDTO);
            clients.add(userDTO);
        }

        return userListDTO;
    }

    public UserListDTO searchAllMember() throws UserNotFoundException {
        UserListDTO userListDTO = new UserListDTO();
        ArrayList<UserDTO> members = new ArrayList<>();
        userListDTO.setList(members);
        List<Member> list = memberRepository.findAll();
        if(list.isEmpty()){
            throw new UserNotFoundException("The Member list is empty");
        }
        for (Member member : list) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(member, userDTO);
            members.add(userDTO);
        }
        return userListDTO;
    }

    public UserListDTO searchAllAdmin() throws UserNotFoundException {
        UserListDTO userListDTO = new UserListDTO();
        ArrayList<UserDTO> admins = new ArrayList<>();
        userListDTO.setList(admins);
        List<Admin> list = adminRepository.findAll();
        if(list.isEmpty()){
            throw new UserNotFoundException("The Admin list is empty");
        }
        for (Admin admin : list) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(admin, userDTO);
            admins.add(userDTO);
        }
        return userListDTO;
    }

    public UserDTO searchByEmail(String email) throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();
        Optional<Client> optionalClient = Optional.ofNullable(clientRepository.findClientByEmail(email));
        if (optionalClient.isEmpty()) {
            Optional<Member> optionalMember = Optional.ofNullable(memberRepository.findMemberByEmail(email));
            if (optionalMember.isEmpty()) {
                Optional<Admin> optionalAdmin = Optional.ofNullable(adminRepository.findAdminByEmail(email));
                if (optionalAdmin.isEmpty()) {
                    throw new UserNotFoundException("Non-existing user");
                }
                Admin admin = optionalAdmin.get();
                BeanUtils.copyProperties(admin, userDTO);
                return userDTO;
            }
            Member member = optionalMember.get();
            BeanUtils.copyProperties(member, userDTO);
            return userDTO;
        }
        Client client = optionalClient.get();
        BeanUtils.copyProperties(client, userDTO);
        return userDTO;
    }
}


