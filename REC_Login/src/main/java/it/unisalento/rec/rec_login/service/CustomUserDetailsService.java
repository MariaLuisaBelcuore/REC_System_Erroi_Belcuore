package it.unisalento.rec.rec_login.service;

import it.unisalento.rec.rec_login.domain.Admin;
import it.unisalento.rec.rec_login.domain.Client;
import it.unisalento.rec.rec_login.domain.Member;
import it.unisalento.rec.rec_login.repositories.AdminRepository;
import it.unisalento.rec.rec_login.repositories.ClientRepository;
import it.unisalento.rec.rec_login.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Client client = clientRepository.findClientByEmail(email);
        if(client == null) {
           final Member member = memberRepository.findMemberByEmail(email);
            if (member == null) {
                final Admin admin = adminRepository.findAdminByEmail(email);
                if(admin==null){
                    throw new UsernameNotFoundException(email);
                }
                UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(admin.getEmail()).password(admin.getPassword()).roles("ADMIN").build();
                return userDetails;
            }
            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(member.getEmail()).password(member.getPassword()).roles("MEMBER").build();
            return userDetails;
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(client.getEmail()).password(client.getPassword()).roles("CLIENT").build();
        return userDetails;
    }
}
