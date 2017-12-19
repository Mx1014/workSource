// @formatter:off
package com.everhomes.rest.qrcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>qrid: 二维码ID</li>
 *     <li>width: 二维码宽</li>
 *     <li>height: 二维码高</li>
 *     <li>source: 扫一扫的来源 {@link com.everhomes.rest.qrcode.QRCodeSource}</li>
 * </ul>
 */
public class GetQRCodeImageCommand {

    private String qrid;
    private Integer width;
    private Integer height;
    private String source;

    public GetQRCodeImageCommand() {
    }

    public String getQrid() {
        return qrid;
    }

    public void setQrid(String qrid) {
        this.qrid = qrid;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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
