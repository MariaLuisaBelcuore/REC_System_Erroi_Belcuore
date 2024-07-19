package it.unisalento.rec.rec_reward.dto;

import java.util.ArrayList;

public class RewardPurchasedListDTO {
    private ArrayList<RewardPurchasedDTO> list;
    public ArrayList<RewardPurchasedDTO> getList() {
        return list;
    }
    public void setList(ArrayList<RewardPurchasedDTO> list) {
        this.list = list;
    }
}
