package it.unisalento.rec.rec_wallet.dto;

public class ReceiveMemberToPayDTO {
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
