package it.unisalento.rec.rec_payment.dto;

import java.util.ArrayList;

public class ClientPaymentListDTO {
    private ArrayList<ClientPaymentDTO> list;
    public ArrayList<ClientPaymentDTO> getList() {
        return list;
    }
    public void setList(ArrayList<ClientPaymentDTO> list) {
        this.list = list;
    }
}
