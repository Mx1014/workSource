// @formatter:off
package com.everhomes.rest.qrcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>qrid: 二维码ID</li>
 * </ul>
 */
public class GetQRCodeInfoCommand {
    private String qrid;
    
    public GetQRCodeInfoCommand() {
    }

    public String getQrid() {
        return qrid;
    }

    public void setQrid(String qrid) {
        this.qrid = qrid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
