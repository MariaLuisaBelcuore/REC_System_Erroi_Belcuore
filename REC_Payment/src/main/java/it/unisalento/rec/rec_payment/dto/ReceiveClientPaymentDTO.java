package it.unisalento.rec.rec_payment.dto;

public class ReceiveClientPaymentDTO {
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
