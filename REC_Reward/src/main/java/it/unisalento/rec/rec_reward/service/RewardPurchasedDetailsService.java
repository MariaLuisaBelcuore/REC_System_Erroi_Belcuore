package it.unisalento.rec.rec_reward.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unisalento.rec.rec_reward.domain.RewardPurchased;
import it.unisalento.rec.rec_reward.dto.RewardDTO;
import it.unisalento.rec.rec_reward.dto.RewardListDTO;
import it.unisalento.rec.rec_reward.dto.RewardPurchasedDTO;
import it.unisalento.rec.rec_reward.dto.SendRewardDTO;
import it.unisalento.rec.rec_reward.exceptions.OperationNotPermittedException;
import it.unisalento.rec.rec_reward.exceptions.RewardNotFoundException;
import it.unisalento.rec.rec_reward.repositories.RewardPurchasedRepository;
import it.unisalento.rec.rec_reward.repositories.RewardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RewardPurchasedDetailsService {
    @Autowired
    RewardRepository rewardRepository;
    @Autowired
    RewardPurchasedRepository rewardPurchasedRepository;
    @Autowired
    RewardDetailsService rewardDetailsService;
    @Autowired
    RabbitMQService rabbitMQService;

    public RewardPurchasedDTO createRewardPurchased(RewardPurchasedDTO rewardPurchasedDTO) throws RewardNotFoundException, JsonProcessingException, OperationNotPermittedException {
        if (!rewardRepository.existsById(rewardPurchasedDTO.getRewardId())) {
            throw new RewardNotFoundException("The reward does not exist");
        }
        System.out.println(rewardPurchasedDTO.getCost());
        RewardPurchased rewardPurchased = new RewardPurchased();
        BeanUtils.copyProperties(rewardPurchasedDTO, rewardPurchased);
        rewardPurchased.setUsed(false);
        rewardPurchased.setPurchaseDate(LocalDateTime.now());
        SendRewardDTO sendRewardDTO = new SendRewardDTO();
        BeanUtils.copyProperties(rewardPurchasedDTO, sendRewardDTO);
        String response = rabbitMQService.sendRewardToPay(sendRewardDTO);
        if(!response.equals("OK")) {
            throw new OperationNotPermittedException("Insufficient credits");
        }
        rewardPurchased = rewardPurchasedRepository.save(rewardPurchased);
        rewardPurchasedDTO.setId(rewardPurchased.getId());
        rewardPurchasedDTO.setPurchaseDate(rewardPurchased.getPurchaseDate());
        return rewardPurchasedDTO;
    }

    public RewardListDTO searchPurchasedByMemberEmail(String memberEmail) throws RewardNotFoundException {
        RewardListDTO rewardListDTO = new RewardListDTO();
        ArrayList<RewardDTO> rewards = new ArrayList<>();
        rewardListDTO.setList(rewards);
        List<RewardPurchased> list =  rewardPurchasedRepository.findByEmailMember(memberEmail);
        if (list.isEmpty()){
            throw new RewardNotFoundException("No rewards purchased");
        }
        for(RewardPurchased rewardPurchased : list){
            if(!rewardPurchased.isUsed()) {
                RewardDTO rewardDTO = rewardDetailsService.search(rewardPurchased.getRewardId());
                rewards.add(rewardDTO);
            }
        }
        return rewardListDTO;
    }

    public RewardPurchasedDTO useRewardPurchased(String id) throws RewardNotFoundException {
        Optional<RewardPurchased> optionalRewardPurchased = rewardPurchasedRepository.findById(id);
        if (optionalRewardPurchased.isEmpty()) {
            throw new RewardNotFoundException("Reward not found");
        }
        RewardPurchased rewardPurchased = optionalRewardPurchased.get();
        if(rewardPurchased.isUsed()) {
            throw new OperationNotPermittedException("reward used");
        }
        rewardPurchased.setUsed(true);
        rewardPurchased = rewardPurchasedRepository.save(rewardPurchased);
        RewardPurchasedDTO rewardPurchasedDTO = new RewardPurchasedDTO();
        BeanUtils.copyProperties(rewardPurchased, rewardPurchasedDTO);
        return rewardPurchasedDTO;
    }
}
