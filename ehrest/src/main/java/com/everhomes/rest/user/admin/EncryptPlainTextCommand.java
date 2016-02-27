package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>plainText: 密码明文</li>
 * </ul>
 */
public class EncryptPlainTextCommand {
	private String plainText;

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
