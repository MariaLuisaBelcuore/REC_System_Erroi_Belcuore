package it.unisalento.rec.rec_payment.dto;

import java.io.Serializable;

public class SendClientPaymentDTO implements Serializable {
    private float amount;
    private String clientEmail;
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public String getClientEmail() {
        return clientEmail;
    }
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
