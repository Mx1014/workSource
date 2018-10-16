package com.everhomes.rest.visitorsys;

public class IdentifierCardResponse {
    private String code ;
    private String msg ;
    private IdentifierCardDTO cardInfo ;
    private String timestamp ;
    private String nonce ;
    private String sign ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public IdentifierCardDTO getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(IdentifierCardDTO cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
