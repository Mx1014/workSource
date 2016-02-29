package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>plainText: 密码明文</li>
 * <li>plainTextHash: 密码明文的SHA265 Hash值</li>
 * <li>salt:盐值</li>
 * <li>encryptText: 密码密文</li>
 * </ul>
 */
public class EncriptInfoDTO {
    private String plainText;
    private String plainTextHash;
    private String salt;
    private String encryptText;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getPlainTextHash() {
        return plainTextHash;
    }

    public void setPlainTextHash(String plainTextHash) {
        this.plainTextHash = plainTextHash;
    }

    public String getEncryptText() {
        return encryptText;
    }

    public void setEncryptText(String encryptText) {
        this.encryptText = encryptText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
