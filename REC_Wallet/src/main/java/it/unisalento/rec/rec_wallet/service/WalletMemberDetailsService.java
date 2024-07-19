package it.unisalento.rec.rec_wallet.service;

import it.unisalento.rec.rec_wallet.domain.WalletClient;
import it.unisalento.rec.rec_wallet.domain.WalletMember;
import it.unisalento.rec.rec_wallet.dto.WalletClientDTO;
import it.unisalento.rec.rec_wallet.dto.WalletClientListDTO;
import it.unisalento.rec.rec_wallet.dto.WalletMemberDTO;
import it.unisalento.rec.rec_wallet.dto.WalletMemberListDTO;
import it.unisalento.rec.rec_wallet.exceptions.WalletNotFoundException;
import it.unisalento.rec.rec_wallet.repositories.WalletMemberRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletMemberDetailsService {
    @Autowired
    WalletMemberRepository walletMemberRepository;

    public WalletMemberDTO createWalletMember(WalletMemberDTO walletMemberDTO) {
        WalletMember walletMember = new WalletMember();
        BeanUtils.copyProperties(walletMemberDTO, walletMember, "id");
        walletMember = walletMemberRepository.save(walletMember);
        walletMemberDTO.setId(walletMember.getId());
        return walletMemberDTO;
    }

    //stiamo supponendo che per il membro esista un solo wallet
    public WalletMemberDTO searchWalletMemberByMemberEmail(String memberEmail) throws WalletNotFoundException {
        WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
        Optional<WalletMember> optionalWalletMember = walletMemberRepository.findByMemberEmail(memberEmail);
        if (optionalWalletMember.isEmpty()) {
            throw new WalletNotFoundException("Non-existent wallet");
        }
        WalletMember walletMember = optionalWalletMember.get();
        BeanUtils.copyProperties(walletMember, walletMemberDTO);
        return  walletMemberDTO;
    }

    public WalletMemberListDTO searchAllWalletMember() throws WalletNotFoundException {
        WalletMemberListDTO walletMemberListDTO = new WalletMemberListDTO();
        ArrayList<WalletMemberDTO> wallets = new ArrayList<>();
        walletMemberListDTO.setList(wallets);

        List<WalletMember> list = walletMemberRepository.findAll();

        if (list.isEmpty()) {
            throw new WalletNotFoundException("No wallet found");
        }

        for(WalletMember walletMember : list){
            WalletMemberDTO walletMemberDTO = new WalletMemberDTO();
            BeanUtils.copyProperties(walletMember, walletMemberDTO);
            wallets.add(walletMemberDTO);
        }
        return walletMemberListDTO;
    }

    public String deleteWalletMember(String memberEmail) throws WalletNotFoundException{
        if (!walletMemberRepository.existsByMemberEmail(memberEmail)) {
            throw new WalletNotFoundException("Non-existent wallet");
        }
        walletMemberRepository.deleteByMemberEmail(memberEmail);
        return memberEmail;
    }

    public WalletMemberDTO updateWalletMember(WalletMemberDTO walletMemberDTO) throws WalletNotFoundException {
        Optional<WalletMember> optionalWalletMember = walletMemberRepository.findByMemberEmail(walletMemberDTO.getMemberEmail());
        if(optionalWalletMember.isEmpty()) {
            throw new WalletNotFoundException("Non-existent wallet");
        }
        WalletMember walletMember = optionalWalletMember.get();
        BeanUtils.copyProperties(walletMemberDTO, walletMember, "memberEmail", "id");
        walletMember = walletMemberRepository.save(walletMember);
        walletMemberDTO.setId(walletMember.getId());
        return walletMemberDTO;
    }
}
