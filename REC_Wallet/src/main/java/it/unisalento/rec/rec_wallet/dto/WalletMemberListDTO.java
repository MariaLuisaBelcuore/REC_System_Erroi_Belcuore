package it.unisalento.rec.rec_wallet.dto;

import java.util.ArrayList;

public class WalletMemberListDTO {
    private ArrayList<WalletMemberDTO> list;
    public ArrayList<WalletMemberDTO> getList() {
        return list;
    }
    public void setList(ArrayList<WalletMemberDTO> list) {
        this.list = list;
    }
}
