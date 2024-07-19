package it.unisalento.rec.rec_task.dto;

import java.io.Serializable;

public class SendTaskDTO implements Serializable {
    private int executionTime;
    private String clientEmail;
    public int getExecutionTime() {
        return executionTime;
    }
    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }
    public String getClientEmail() {
        return clientEmail;
    }
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
