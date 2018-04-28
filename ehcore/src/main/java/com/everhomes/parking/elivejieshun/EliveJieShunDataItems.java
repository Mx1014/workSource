// @formatter:off
package com.everhomes.parking.elivejieshun;

import com.everhomes.util.StringHelper;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/12 11:13
 */
public class EliveJieShunDataItems<ATTR,FAIL,SUB> {
    private ATTR attributes;
    private FAIL failItems;
    private String objectId;
    private String operateType;
    private SUB subItems;

    public ATTR getAttributes() {
        return attributes;
    }

    public void setAttributes(ATTR attributes) {
        this.attributes = attributes;
    }

    public FAIL getFailItems() {
        return failItems;
    }

    public void setFailItems(FAIL failItems) {
        this.failItems = failItems;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public SUB getSubItems() {
        return subItems;
    }

    public void setSubItems(SUB subItems) {
        this.subItems = subItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
