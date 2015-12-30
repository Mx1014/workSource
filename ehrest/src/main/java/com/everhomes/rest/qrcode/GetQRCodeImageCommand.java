// @formatter:off
package com.everhomes.rest.qrcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>qrid: 二维码ID</li>
 * <li>width: 二维码宽</li>
 * <li>height: 二维码高</li>
 * </ul>
 */
public class GetQRCodeImageCommand {
    private String qrid;
    private Integer width;
    private Integer height;
    
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
