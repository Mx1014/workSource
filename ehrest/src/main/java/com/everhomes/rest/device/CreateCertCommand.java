package com.everhomes.rest.device;

import com.everhomes.util.StringHelper;

public class CreateCertCommand {
    private String name;
    private Integer certType;
    private String certPass;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
