package it.unisalento.rec.rec_wallet.dto;

public class ReceiveClientToPayDTO {
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
