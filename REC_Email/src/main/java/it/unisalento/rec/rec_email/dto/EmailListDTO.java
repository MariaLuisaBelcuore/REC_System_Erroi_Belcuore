package it.unisalento.rec.rec_email.dto;

import java.util.ArrayList;

public class EmailListDTO {
    private ArrayList<EmailDTO> list;
    public ArrayList<EmailDTO> getList() {
        return list;
    }
    public void setList(ArrayList<EmailDTO> list) {
        this.list = list;
    }
}
