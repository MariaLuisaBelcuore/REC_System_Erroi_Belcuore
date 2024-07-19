package it.unisalento.rec.rec_usermanagement.dto;

import java.io.Serializable;

public class RequestVerifyCodeDTO {
    private String email;
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
