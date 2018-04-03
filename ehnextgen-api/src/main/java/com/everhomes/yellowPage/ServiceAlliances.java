package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.util.StringHelper;

public class ServiceAlliances extends EhServiceAlliances {

	private static final long serialVersionUID = -1568787873947269540L;

	private String posterUrl;
	
	private List<ServiceAllianceAttachment> attachments =  new ArrayList<ServiceAllianceAttachment>();
	private List<ServiceAllianceAttachment> fileAttachments =  new ArrayList<ServiceAllianceAttachment>();

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

	public List<ServiceAllianceAttachment> getFileAttachments() {
		return fileAttachments;
	}

	public void setFileAttachments(List<ServiceAllianceAttachment> fileAttachments) {
		this.fileAttachments = fileAttachments;
	}

	public String getEmail() {
        return NewServiceAllianceCustomField.EMAIL.getStringValue(this);
    }
    
    public void setEmail(String email) {
    	NewServiceAllianceCustomField.EMAIL.setStringValue(this, email);
    }
    
    public String getTemplateType() {
        return NewServiceAllianceCustomField.TEMPLATETYPE.getStringValue(this);
    }
    
    public void setTemplateType(String templateType) {
    	NewServiceAllianceCustomField.TEMPLATETYPE.setStringValue(this, templateType);
    }

	public Long getJumpType() {
		return NewServiceAllianceCustomField.JUMPTYPE.getIntegralValue(this);
	}

	public void setJumpType(Long jumpType) {
		NewServiceAllianceCustomField.JUMPTYPE.setIntegralValue(this, jumpType);
	}

	public Long getJumpId() {
		return NewServiceAllianceCustomField.JUMPID.getIntegralValue(this);
	}

	public void setJumpId(Long jumpId) {
		NewServiceAllianceCustomField.JUMPID.setIntegralValue(this, jumpId);
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
