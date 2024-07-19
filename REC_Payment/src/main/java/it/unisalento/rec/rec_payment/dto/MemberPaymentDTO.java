package it.unisalento.rec.rec_payment.dto;

import java.time.LocalDateTime;

public class MemberPaymentDTO {
    private String id;
    private String causal;
    private LocalDateTime emissionDate;
    private int amount;
    private String memberEmail;
    private String paymentorcredit;

    public String getPaymentorcredit() {
        return paymentorcredit;
    }

    public void setPaymentorcredit(String paymentorcredit) {
        this.paymentorcredit = paymentorcredit;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCausal() {
        return causal;
    }

    public void setCausal(String causal) {
        this.causal = causal;
    }

    public LocalDateTime getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(LocalDateTime emissionDate) {
        this.emissionDate = emissionDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
