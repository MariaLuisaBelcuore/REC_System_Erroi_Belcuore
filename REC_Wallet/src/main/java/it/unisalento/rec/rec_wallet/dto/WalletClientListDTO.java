package it.unisalento.rec.rec_wallet.dto;

import java.util.ArrayList;

public class WalletClientListDTO {
    private ArrayList<WalletClientDTO> list;
    public ArrayList<WalletClientDTO> getList() {
        return list;
    }
    public void setList(ArrayList<WalletClientDTO> list) {
        this.list = list;
    }
}
