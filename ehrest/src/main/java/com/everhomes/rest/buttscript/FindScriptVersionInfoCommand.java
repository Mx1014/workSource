package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID(必填)</li>
 * <li>infoType: 脚本分类</li>
 * <li>pageSize: 显示数量</li>
 * <li>pageAnchor: 页面锚点</li>
 * </ul>
 */
public class FindScriptVersionInfoCommand {

    private  Integer namespaceId ;
    private  String   infoType ;
    private  Integer   pageSize ;
    private  Integer   pageOffset;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
