package it.unisalento.rec.rec_task.dto;

import java.util.ArrayList;

public class TaskListDTO {
    private ArrayList<TaskDTO> list;
    public ArrayList<TaskDTO> getList() {
        return list;
    }
    public void setList(ArrayList<TaskDTO> list) {
        this.list = list;
    }
}
