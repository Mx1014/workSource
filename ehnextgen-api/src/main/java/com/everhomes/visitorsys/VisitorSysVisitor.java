// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitors;
import com.everhomes.util.StringHelper;

import java.util.List;

public class VisitorSysVisitor extends EhVisitorSysVisitors {
	
	private static final long serialVersionUID = -1090547867158368768L;
	private Long enterpriseId;
	private List<PostApprovalFormItem> communityFormValues;
	private List<PostApprovalFormItem> enterpriseFormValues;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public List<PostApprovalFormItem> getCommunityFormValues() {
		return communityFormValues;
	}

	public void setCommunityFormValues(List<PostApprovalFormItem> communityFormValues) {
		this.communityFormValues = communityFormValues;
	}

	public List<PostApprovalFormItem> getEnterpriseFormValues() {
		return enterpriseFormValues;
	}

	public void setEnterpriseFormValues(List<PostApprovalFormItem> enterpriseFormValues) {
		this.enterpriseFormValues = enterpriseFormValues;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}