package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>cmd: 消息命令字</li>
 * <li>secretVersion: 消息命令版本好， 0x0 或 0x1 </li>
 * <li>encrypted: 消息的 base64 加密内容，加密内容里面包括了 cmd,secretVersion </li>
 * </ul>
 * @author janson
 *
 */
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
