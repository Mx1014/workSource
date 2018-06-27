// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>qrcode: (必填)二维码字符串</li>
 * </ul>
 */
public class TransferQrcodeCommand {
    private String qrcode;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
