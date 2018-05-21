// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>enterpriseId: (必填)公司id</li>
 * <li>enterpriseName: (必填)公司名称</li>
 * </ul>
 */
public class BaseVisitorEnterpriseDTO {
    private Long enterpriseId;
    private String enterpriseName;

    public BaseVisitorEnterpriseDTO(Long enterpriseId, String enterpriseName) {
        this.enterpriseId = enterpriseId;
        this.enterpriseName = enterpriseName;
    }

    public BaseVisitorEnterpriseDTO() {
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
