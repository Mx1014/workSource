// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem;
import com.everhomes.rest.visitorsys.VisitorsysBaseConfig;
import com.everhomes.rest.visitorsys.VisitorsysPassCardConfig;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysConfigurations;
import com.everhomes.util.StringHelper;

import java.util.List;

public class VisitorSysConfiguration extends EhVisitorSysConfigurations {
	
	private static final long serialVersionUID = 3447559246137092210L;

	private VisitorsysBaseConfig baseConfig;
	private List<VisitorsysApprovalFormItem> formConfig;
	private VisitorsysPassCardConfig passCardConfig;

	public VisitorsysBaseConfig getBaseConfig() {
		return baseConfig;
	}

	public void setBaseConfig(VisitorsysBaseConfig baseConfig) {
		this.baseConfig = baseConfig;
	}

	public List<VisitorsysApprovalFormItem> getFormConfig() {
		return formConfig;
	}

	public void setFormConfig(List<VisitorsysApprovalFormItem> formConfig) {
		this.formConfig = formConfig;
	}

	public VisitorsysPassCardConfig getPassCardConfig() {
		return passCardConfig;
	}

	public void setPassCardConfig(VisitorsysPassCardConfig passCardConfig) {
		this.passCardConfig = passCardConfig;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}