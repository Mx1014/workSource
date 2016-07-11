// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationTasks;
import com.everhomes.util.StringHelper;

public class OrganizationTask extends EhOrganizationTasks {
	
	private String option;
	
	private String entrancePrivilege;
	
	private String targetName;
	
	private String targetToken;
	
	
	
	public String getEntrancePrivilege() {
		return entrancePrivilege;
	}

	public void setEntrancePrivilege(String entrancePrivilege) {
		this.entrancePrivilege = entrancePrivilege;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetToken() {
		return targetToken;
	}

	public void setTargetToken(String targetToken) {
		this.targetToken = targetToken;
	}



	private static final long serialVersionUID = -1852518988310908484L;

	public OrganizationTask() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
