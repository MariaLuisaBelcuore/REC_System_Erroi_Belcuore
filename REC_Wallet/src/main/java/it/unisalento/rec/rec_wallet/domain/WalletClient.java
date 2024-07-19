package it.unisalento.rec.rec_wallet.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("walletClient")
public class WalletClient {
    @Id
    private String id;
    private String nome;
    private String cognome;
    private String clientEmail;
    private String cardNumber;
    private String cardDeadline;
    private String cvc;
    private float residualCredit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardDeadline() {
        return cardDeadline;
    }

    public void setCardDeadline(String cardDeadline) {
        this.cardDeadline = cardDeadline;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public float getResidualCredit() {
        return residualCredit;
    }

    public void setResidualCredit(float residualCredit) {
        this.residualCredit = residualCredit;
    }
}
