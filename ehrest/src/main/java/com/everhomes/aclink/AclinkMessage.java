package com.everhomes.aclink;

public class AclinkMessage {
    Byte cmd;
    Byte secretVersion; //0x0 or 0x1
    String encrypted;
    
    public Byte getCmd() {
        return cmd;
    }
    public void setCmd(Byte cmd) {
        this.cmd = cmd;
    }
    public Byte getSecretVersion() {
        return secretVersion;
    }
    public void setSecretVersion(Byte secretVersion) {
        this.secretVersion = secretVersion;
    }
    public String getEncrypted() {
        return encrypted;
    }
    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }
}
