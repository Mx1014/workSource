package com.everhomes.rest.contentserver;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * add content storage proxy
 * 
 * @author elians
 *
 */
public class AddContentServerCommand {

    // private ip address
    private String privateAddress;

    // private port,internal communicate
    private int privatePort;

    // public address
    @NotNull
    private String publicAddress;

    // public port
    @NotNull
    private int publicPort;

    private String name;

    private String description;

    public void setPrivateAddress(String privateAddress) {
        this.privateAddress = privateAddress;
    }

    public void setPrivatePort(int privatePort) {
        this.privatePort = privatePort;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
    }

    public String getPrivateAddress() {
        return privateAddress;
    }

    public int getPrivatePort() {
        return privatePort;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
