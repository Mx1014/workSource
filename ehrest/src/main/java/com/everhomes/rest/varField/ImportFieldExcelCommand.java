//@formatter:off
package com.everhomes.rest.varField;

/**
 * Created by Wentian Wang on 2017/9/19.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>customerId:客户id</li>
 * <li>customerType:客户类型</li>
 * <li>moduleName：module的name</li>
 * <li>namespaceId：域空间</li>
 * <li>communityId：园区id</li>
 * <li>orgId：公司id</li>
 * <li>privilegeCode：1: 客户导入   2： 管理导入  暂时不写枚举了。。</li>
 *</ul>
 */
public class ImportFieldExcelCommand {
    
    private Long customerId;
    private String customerType;
    private String moduleName;
    private Integer namespaceId;
    private Long communityId;
    private Long orgId;
    private Byte privilegeCode;
    private Long ownerId;
    private String ownerType;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Byte getPrivilegeCode() {
        return privilegeCode;
    }

    public void setPrivilegeCode(Byte privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
