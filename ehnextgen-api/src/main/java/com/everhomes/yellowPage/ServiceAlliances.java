package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.yellowPage.AllianceTagGroupDTO;
import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.util.StringHelper;

public class ServiceAlliances extends EhServiceAlliances {

	private static final long serialVersionUID = -1568787873947269540L;

	private String posterUrl;
	private String startDate;
	private String endDate;
	
	private List<ServiceAllianceAttachment> coverAttachments =  new ArrayList<ServiceAllianceAttachment>(10);
	private List<ServiceAllianceAttachment> attachments =  new ArrayList<ServiceAllianceAttachment>();
	private List<ServiceAllianceAttachment> fileAttachments =  new ArrayList<ServiceAllianceAttachment>();
	
	
	private List<AllianceTagGroupDTO> tagGroups;

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
	
	public Byte getEnableProvider() {
		Long ret = NewServiceAllianceCustomField.ENABLEPROVIDER.getIntegralValue(this);
		if (null == ret) {
			return (byte)0;
		}
		
		return ret.byteValue();
	}

	public void setEnableProvider(Byte enableProvider) {
		if (null == enableProvider) {
			enableProvider = (byte)0;
		}
		
		NewServiceAllianceCustomField.ENABLEPROVIDER .setIntegralValue(this, enableProvider.longValue());
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<ServiceAllianceAttachment> getCoverAttachments() {
		return coverAttachments;
	}

	public void setCoverAttachments(List<ServiceAllianceAttachment> coverAttachments) {
		this.coverAttachments = coverAttachments;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<AllianceTagGroupDTO> getTagGroups() {
		return tagGroups;
	}

	public void setTagGroups(List<AllianceTagGroupDTO> tagGroups) {
		this.tagGroups = tagGroups;
	}
}
