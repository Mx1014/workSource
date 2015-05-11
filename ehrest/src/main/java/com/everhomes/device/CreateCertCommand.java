package com.everhomes.device;

public class CreateCertCommand {
    private String name;
    private Integer certType;
    private String certKey;
    
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

}
