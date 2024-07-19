package it.unisalento.rec.rec_payment.dto;

public class ReceiveMemberPaymentDTO {
    private int cost;
    private String emailMember;

    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public String getEmailMember() {
        return emailMember;
    }
    public void setEmailMember(String emailMember) {
        this.emailMember = emailMember;
    }
}
