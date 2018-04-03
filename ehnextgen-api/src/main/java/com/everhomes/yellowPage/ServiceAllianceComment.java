// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhServiceAllianceComments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceComment extends EhServiceAllianceComments {
	
	private static final long serialVersionUID = 3224647444903999185L;
	
	List<ServiceAllianceCommentAttachment> atts;
	
	public List<ServiceAllianceCommentAttachment> getAtts() {
		return atts;
	}

	public void setAtts(List<ServiceAllianceCommentAttachment> atts) {
		this.atts = atts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}