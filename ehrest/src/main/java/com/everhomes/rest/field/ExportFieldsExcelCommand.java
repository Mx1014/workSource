//@formatter:off
package com.everhomes.rest.field;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/13.
 */
/**
 *<ul>
 * <li>moduleName：module name</li>
 * <li>namespaceId：域空间</li>
 * <li>customerId：客户id</li>
 * <li>customerType：客户类型</li>
 * <li>includedGroupIds：导出需要的group的id，用逗号相连接</li>
 * <li>communityId：园区id</li>
 *</ul>
 */
public class ExportFieldsExcelCommand {

    private String moduleName;

    private Integer namespaceId;
    private Long customerId;

    private Byte customerType;
    private String includedGroupIds;

    private Long communityId;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public String getIncludedGroupIds() {
        return includedGroupIds;
    }

    public void setIncludedGroupIds(String includedGroupIds) {
        this.includedGroupIds = includedGroupIds;
    }
}
