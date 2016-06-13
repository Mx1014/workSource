package com.everhomes.rest.device;

public class CreateCertCommand {
    private String name;
    private Integer certType;
    private String certKey;
    private String certPass;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCertKey() {
        return certKey;
    }
    public void setCertKey(String certKey) {
        this.certKey = certKey;
    }
    public Integer getCertType() {
        return certType;
    }
    public void setCertType(Integer certType) {
        this.certType = certType;
    }
    public String getCertPass() {
        return certPass;
    }
    public void setCertPass(String certPass) {
        this.certPass = certPass;
    }   

}
