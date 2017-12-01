// @formatter:off
package com.everhomes.rest.qrcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>qrid: 二维码ID</li>
 *     <li>source: 扫一扫的来源 {@link com.everhomes.rest.qrcode.QRCodeSource}</li>
 * </ul>
 */
public class GetQRCodeInfoCommand {

    private String qrid;
    private String source;

    public GetQRCodeInfoCommand() {
    }

    public String getQrid() {
        return qrid;
    }

    public void setQrid(String qrid) {
        this.qrid = qrid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
