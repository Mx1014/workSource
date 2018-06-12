package com.everhomes.rest.pmsy;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>customerId: 用户id</li>
 * <li>resourceId: 资源id</li>
 * <li>projectId: 项目id(根据第三方获取的id)</li>
 * <li>billDate: 账单日期</li>
 * </ul>
 */
public class GetPmsyBills {
	
	@NotNull
	private String customerId;
	@NotNull
	private String projectId;
	@NotNull
	private String resourceId;
	private String billDateStr;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getBillDateStr() {
		return billDateStr;
	}
	public void setBillDateStr(String billDateStr) {
		this.billDateStr = billDateStr;
	}
}
