package com.everhomes.rest.techpark.expansion;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.enterprise.EnterpriseAttachmentDTO;
import com.everhomes.util.StringHelper;

public class EnterpriseDetailDTO {
	private Long id;
	private Long enterpriseId;
	private String enterpriseName;
	private String contactPhone;
	private String address;
	private String description;
    private Double longitude;
    private Double latitude;
    private String geohash;
    private String avatarUri;
    private String avatarUrl;
    private Timestamp createTime;
    @ItemType(EnterpriseAttachmentDTO.class)
    private List<EnterpriseAttachmentDTO> attachments;

	private String fullInitial;
	private String fullPinyin;
	private String initial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
	public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    
    public String getAvatarUri() {
		return avatarUri;
	}

	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public List<EnterpriseAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EnterpriseAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

	public String getFullInitial() {
		return fullInitial;
	}

	public void setFullInitial(String fullInitial) {
		this.fullInitial = fullInitial;
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public void setFullPinyin(String fullPinyin) {
		this.fullPinyin = fullPinyin;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
