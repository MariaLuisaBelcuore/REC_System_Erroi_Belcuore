package it.unisalento.rec.rec_wallet.dto;

public class WalletMemberDTO {
    private String id;
    private String nome;
    private String cognome;
    private String memberEmail;
    private int residualCredit;

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getCognome() {return cognome;}

    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getMemberEmail() {return memberEmail;}

    public void setMemberEmail(String memberEmail) {this.memberEmail = memberEmail;}

    public int getResidualCredit() {return residualCredit;}

    public void setResidualCredit(int residualCredit) {this.residualCredit = residualCredit;}
}
