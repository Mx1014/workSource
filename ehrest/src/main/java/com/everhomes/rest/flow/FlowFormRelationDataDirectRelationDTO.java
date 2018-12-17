package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>formOriginId: 表单 originId</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>updateFlag: 是否需要更新的标志</li>
 *     <li>updateTime: 更新时间</li>
 *     <li>name: 表单名称</li>
 * </ul>
 */
public class FlowFormRelationDataDirectRelationDTO {

    private Long formOriginId;
    private Long formVersion;

    private Byte updateFlag;
    private Long updateTime;
    private String name;

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public Byte getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Byte updateFlag) {
        this.updateFlag = updateFlag;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
