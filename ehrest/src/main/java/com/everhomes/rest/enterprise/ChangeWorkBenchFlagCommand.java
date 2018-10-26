package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 组织ID</li>
 *     <li>workBenchFlag: 工作台的状态，1-表示的是开启，0-表示的是关闭</li>
 *     <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class ChangeWorkBenchFlagCommand {
    //组织ID
    private Long organizationId;
    //工作台状态
    private Byte workBenchFlag;
    //域空间ID
    private Integer namespaceId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getWorkBenchFlag() {
        return workBenchFlag;
    }

    public void setWorkBenchFlag(Byte workBenchFlag) {
        this.workBenchFlag = workBenchFlag;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
