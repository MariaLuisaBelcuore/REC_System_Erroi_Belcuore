package it.unisalento.rec.rec_energyresources.dto;

import java.io.Serializable;

public class ResourceCreditMemberDTO implements Serializable {
    private String memberEmail;
    private int additionalCredit;

    public String getMemberEmail() {
        return memberEmail;
    }
    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
    public int getAdditionalCredit() {
        return additionalCredit;
    }
    public void setAdditionalCredit(int additionalCredit) {
        this.additionalCredit = additionalCredit;
    }
}
