// @formatter:off
package com.everhomes.rest.border;

import javax.validation.constraints.NotNull;

public class UpdateBorderCommand {
    @NotNull
    private int id;
    
    private String privateAddress;
    
    private Integer privatePort;
    
    private String publicAddress;
    
    private Integer publicPort;
    
    private Integer status;
    
    private String configTag;
    
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivateAddress() {
        return privateAddress;
    }

    public void setPrivateAddress(String privateAddress) {
        this.privateAddress = privateAddress;
    }

    public Integer getPrivatePort() {
        return privatePort;
    }

    public void setPrivatePort(Integer privatePort) {
        this.privatePort = privatePort;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public Integer getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(Integer publicPort) {
        this.publicPort = publicPort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConfigTag() {
        return configTag;
    }

    public void setConfigTag(String configTag) {
        this.configTag = configTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
