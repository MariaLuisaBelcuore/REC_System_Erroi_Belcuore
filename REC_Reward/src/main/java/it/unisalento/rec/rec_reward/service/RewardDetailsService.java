package it.unisalento.rec.rec_reward.service;

import it.unisalento.rec.rec_reward.domain.Reward;
import it.unisalento.rec.rec_reward.dto.RewardDTO;
import it.unisalento.rec.rec_reward.dto.RewardListDTO;
import it.unisalento.rec.rec_reward.exceptions.RewardNotFoundException;
import it.unisalento.rec.rec_reward.repositories.RewardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RewardDetailsService {
    @Autowired
    RewardRepository rewardRepository;

    public RewardDTO createReward(RewardDTO rewardDTO){
        Reward reward = new Reward();
        rewardDTO.setCreationDate(LocalDateTime.now());
        BeanUtils.copyProperties(rewardDTO, reward);
        reward = rewardRepository.save(reward);
        rewardDTO.setId(reward.getId());
        return rewardDTO;
    }

    public RewardDTO search(String id) throws RewardNotFoundException {
        if (!rewardRepository.existsById(id)) {
            throw new RewardNotFoundException("The reward does not exist");
        }
        Reward reward = rewardRepository.findById(id).orElseThrow();
        RewardDTO rewardDTO = new RewardDTO();
        BeanUtils.copyProperties(reward, rewardDTO);
        return rewardDTO;
    }

    public RewardListDTO searchAll() throws RewardNotFoundException {
        RewardListDTO rewardListDTO = new RewardListDTO();
        ArrayList<RewardDTO> rewards = new ArrayList<>();
        rewardListDTO.setList(rewards);
        List<Reward> list = rewardRepository.findAll();

        if (list.isEmpty()) {
            throw new RewardNotFoundException("No reward found");
        }

        for(Reward reward : list){
            RewardDTO rewardDTO = new RewardDTO();
            BeanUtils.copyProperties(reward, rewardDTO);
            rewards.add(rewardDTO);
        }
        return rewardListDTO;
    }

    public RewardListDTO searchByCriteria(String searchTerm) throws RewardNotFoundException {
        List<Reward> matchingRewards = rewardRepository.findByNameContainingIgnoreCase(searchTerm);

        if (matchingRewards.isEmpty()) {
            throw new RewardNotFoundException("No matching reward");
        }

        RewardListDTO rewardListDTO = new RewardListDTO();
        ArrayList<RewardDTO> rewards = new ArrayList<>();
        rewardListDTO.setList(rewards);

        for (Reward reward : matchingRewards) {
            RewardDTO rewardDTO = new RewardDTO();
            BeanUtils.copyProperties(reward, rewardDTO);
            rewards.add(rewardDTO);
        }
        return rewardListDTO;
    }

    public String deleteReward(String id) throws RewardNotFoundException {
        if (!rewardRepository.existsById(id)) {
            throw new RewardNotFoundException("The reward does not exist");
        }
        rewardRepository.deleteById(id);
        return id;
    }

    public RewardDTO updateReward(String id, RewardDTO rewardDTO) throws RewardNotFoundException{
        Optional<Reward> optionalReward = rewardRepository.findById(id);
        if (optionalReward.isEmpty()) {
            throw new RewardNotFoundException("The reward does not exist");
        }
        Reward reward = optionalReward.get();
        BeanUtils.copyProperties(rewardDTO, reward, "id");
        reward = rewardRepository.save(reward);
        rewardDTO.setId(reward.getId());
        return rewardDTO;
    }
}
