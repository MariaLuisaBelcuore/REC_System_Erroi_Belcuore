package it.unisalento.rec.rec_payment.dto;

import java.util.ArrayList;

public class MemberPaymentListDTO {
    private ArrayList<MemberPaymentDTO> list;
    public ArrayList<MemberPaymentDTO> getList() {
        return list;
    }
    public void setList(ArrayList<MemberPaymentDTO> list) {
        this.list = list;
    }
}
