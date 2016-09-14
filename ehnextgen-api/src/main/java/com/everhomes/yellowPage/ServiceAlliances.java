package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.util.StringHelper;

public class ServiceAlliances extends EhServiceAlliances {

	private static final long serialVersionUID = -1568787873947269540L;

	private String posterUrl;
	
	private List<ServiceAllianceAttachment> attachments =  new ArrayList<ServiceAllianceAttachment>();
	
	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public List<ServiceAllianceAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ServiceAllianceAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public String getEmail() {
        return NewServiceAllianceCustomField.EMAIL.getStringValue(this);
    }
    
    public void setEmail(String email) {
    	NewServiceAllianceCustomField.EMAIL.setStringValue(this, email);
    }
    
    public Long getTemplateId() {
        return NewServiceAllianceCustomField.TEMPLATEID.getIntegralValue(this);
    }
    
    public void setTemplateId(Long templateId) {
    	NewServiceAllianceCustomField.TEMPLATEID.setIntegralValue(this, templateId);
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
