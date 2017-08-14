package com.everhomes.rest.barcode;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: byte    (必填)条形码类型  电商为1，coreServer其他模块 参考 {@link com.everhomes.rest.barcode.BarcodeType}</li>
 * <li>url: String   (必填) 条形码跳转的完整链接</li>
 * <li>title: String 名称</li>
 * <li>description: String 描述</li>
 * </ul>
 */
public class BarcodeDTO {

    private byte type;
    private String url;
    private String title;
    private String description;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }
}
