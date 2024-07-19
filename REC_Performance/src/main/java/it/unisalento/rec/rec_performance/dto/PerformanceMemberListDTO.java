package it.unisalento.rec.rec_performance.dto;

import java.util.ArrayList;

public class PerformanceMemberListDTO {
    private ArrayList<PerformanceMemberDTO> list;
    public ArrayList<PerformanceMemberDTO> getList() {
        return list;
    }
    public void setList(ArrayList<PerformanceMemberDTO> list) {
        this.list = list;
    }
}
