package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 查询园区的企业
 * <li>enterpriseId: 小区ID</li>
 * </ul>
 * @author wuhan
 *
 */
public class SetCurrentEnterpriseCommand {
    @NotNull
	private
    Long enterpriseId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
 
}
