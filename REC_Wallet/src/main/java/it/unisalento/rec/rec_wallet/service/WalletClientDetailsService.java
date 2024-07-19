package it.unisalento.rec.rec_wallet.service;

import it.unisalento.rec.rec_wallet.domain.WalletClient;
import it.unisalento.rec.rec_wallet.dto.WalletClientDTO;
import it.unisalento.rec.rec_wallet.dto.WalletClientListDTO;
import it.unisalento.rec.rec_wallet.exceptions.WalletNotFoundException;
import it.unisalento.rec.rec_wallet.repositories.WalletClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WalletClientDetailsService {
    @Autowired
    WalletClientRepository walletClientRepository;

    public WalletClientDTO createWalletClient(WalletClientDTO walletClientDTO){
        WalletClient walletClient = new WalletClient();
        BeanUtils.copyProperties(walletClientDTO, walletClient, "id");
        walletClient = walletClientRepository.save(walletClient);
        walletClientDTO.setId(walletClient.getId());
        return walletClientDTO;
    }


    public String deleteWalletClient(String clientEmail) throws WalletNotFoundException{
        if (!walletClientRepository.existsByClientEmail(clientEmail)) {
            throw new WalletNotFoundException("No wallet found");
        }
        walletClientRepository.deleteByClientEmail(clientEmail);
        return clientEmail;
    }

    public WalletClientDTO updateWalletClient(WalletClientDTO walletClientDTO) throws WalletNotFoundException {
        Optional<WalletClient> optionalWalletClient = walletClientRepository.findByClientEmail(walletClientDTO.getClientEmail());
        if (optionalWalletClient.isEmpty()) {
            throw new WalletNotFoundException("Non-existent wallet");
        }
        WalletClient walletClient = optionalWalletClient.get();
        BeanUtils.copyProperties(walletClientDTO, walletClient, "clientEmail", "id");
        walletClient = walletClientRepository.save(walletClient);
        walletClientDTO.setId(walletClient.getId());
        return walletClientDTO;
    }

    //stiamo supponendo che per il client esista un solo wallet
    public WalletClientDTO searchWalletClientByClientEmail(String clientEmail) throws WalletNotFoundException {
        WalletClientDTO walletClientDTO = new WalletClientDTO();
        Optional<WalletClient> optionalWalletClient = walletClientRepository.findByClientEmail(clientEmail);
        if (optionalWalletClient.isEmpty()) {
            throw new WalletNotFoundException("Non-existent wallet");
        }
        WalletClient walletClient = optionalWalletClient.get();
        BeanUtils.copyProperties(walletClient, walletClientDTO);
        return  walletClientDTO;
    }

    public WalletClientListDTO searchAllWalletClient() throws WalletNotFoundException {
        WalletClientListDTO walletClientListDTO = new WalletClientListDTO();
        ArrayList<WalletClientDTO> wallets = new ArrayList<>();
        walletClientListDTO.setList(wallets);

        List<WalletClient> list = walletClientRepository.findAll();

        if (list.isEmpty()) {
            throw new WalletNotFoundException("No wallet found");
        }

        for(WalletClient walletClient : list){
            WalletClientDTO walletClientDTO = new WalletClientDTO();
            BeanUtils.copyProperties(walletClient, walletClientDTO);
            wallets.add(walletClientDTO);
        }
        return walletClientListDTO;
    }
}
