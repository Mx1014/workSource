// @formatter:off
package com.everhomes.rest.activity;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 */
/**
 * 
 * @author elians
 *<ul>
 *<li>namespaceId:命名空间</li>
 *<li>subject:主题</li>
 *<li>description:描述（活动实际内容）</li>
 *<li>contentType:内容类型，参考{@link com.everhomes.rest.forum.PostContentType}</li>
 *<li>content:拼接的内容</li>
 *<li>location:位置</li>
 *<li>contactPerson:联系人</li>
 *<li>startTime:开始时间，时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>endTime:结束时间,时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>signupEndTime:活动报名截止时间,时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *<li>checkInFlag:签到标签，0-不需要签到、1-需要签到</li>
 *<li>confirmFlag:确认标签，0-不需要确认、1-需要确认</li>
 *<li>maxAttendeeCount:最大邀请人数</li>
 *<li>creatorFamilyId:创建者家庭ID</li>
 *<li>groupId:组ID</li>
 *<li>longitude:经度</li>
 *<li>latitude:纬度</li>
 *<li>changeVersion:版本</li>
 *<li>posterUri:海报</li>
 *<li>guest:嘉宾</li>
 *<li>mediaUrl:活动url</li>
 *<li>officialFlag: 是否为官方帖；参考{@link com.everhomes.rest.organization.OfficialFlag}</li>
 * <li>maxQuantity: 限制人数</li>
 * <li>categoryId: 活动类型id（入口id)</li>
 * <li>contentCategoryId: 主题分类id</li>
 * <li>forumId: 论坛ID</li>
 * <li>creatorTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>targetTag: 创建者标签，参考{@link com.everhomes.rest.forum.PostEntityTag}</li>
 * <li>visibleRegionType: 区域范围类型，{@link com.everhomes.rest.visibility.VisibleRegionType}</li>
 * <li>visibleRegionId: 区域范围类型对应的ID</li>
 * <li>chargeFlag: 是否收费：0-不收费， 1-收费  参考{@link com.everhomes.rest.activity.ActivityChargeFlag }</li>
 * <li>chargePrice: 收费价格</li>
 * <li>status: 活动状态，0-已删除，1-待确认，2-正常。用于暂存或者立刻发布，不传默认2立刻发布，参考{@link com.everhomes.rest.forum.PostStatus}</li>
 * <li>wechatSignup: 是否支持微信报名，0-不支持，1-支持 参考  参考{@link com.everhomes.rest.activity.WechatSignupFlag }</li>
 *</ul>
 */
public class ActivityPostCommand{
    private Integer namespaceId;
    private String subject;
    private String description;
    private String contentType;
    private String content;
    private String location;
    private String contactPerson;
    private String contactNumber;
    private String startTime;
    private String endTime;
    private Byte allDayFlag;
    private String signupEndTime;
    private Byte checkinFlag;
    private Byte confirmFlag;
    private Integer maxAttendeeCount;
    private Long creatorFamilyId;
    private Long groupId;
    private Integer changeVersion;
    private Double longitude;
    private Double latitude;
    private String tag;
    private transient Long id;
    private String posterUri;
    private String guest;
    
    private String mediaUrl;
    
    private Byte officialFlag;
    
    //added by Janson
    private Byte isVideoSupport;
    private String videoUrl;
    private Byte videoState;
    
    private Integer maxQuantity;
    
    //added by xiongying
    private Long categoryId;
    
    //added by tt
    private Long contentCategoryId;

    private Long forumId;
    
    private String creatorTag;
    
    private String targetTag;
    
    private Byte visibleRegionType;
    
    private Long visibleRegionId;
    
    private Byte chargeFlag;

    private BigDecimal chargePrice;
    
    private Byte status;

    private Byte wechatSignup;

	public String getSignupEndTime() {
		return signupEndTime;
	}

	public void setSignupEndTime(String signupEndTime) {
		this.signupEndTime = signupEndTime;
	}

	public Long getContentCategoryId() {
		return contentCategoryId;
	}

	public void setContentCategoryId(Long contentCategoryId) {
		this.contentCategoryId = contentCategoryId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public Byte getOfficialFlag() {
		return officialFlag;
	}

	public void setOfficialFlag(Byte officialFlag) {
		this.officialFlag = officialFlag;
	}
	
    public ActivityPostCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Byte getAllDayFlag() {
		return allDayFlag;
	}

	public void setAllDayFlag(Byte allDayFlag) {
		this.allDayFlag = allDayFlag;
	}

	public Byte getCheckinFlag() {
        return checkinFlag;
    }

    public void setCheckinFlag(Byte checkinFlag) {
        this.checkinFlag = checkinFlag;
    }

    public Byte getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(Byte confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Integer getMaxAttendeeCount() {
        return maxAttendeeCount;
    }

    public void setMaxAttendeeCount(Integer maxAttendeeCount) {
        this.maxAttendeeCount = maxAttendeeCount;
    }

    public Long getCreatorFamilyId() {
        return creatorFamilyId;
    }

    public void setCreatorFamilyId(Long creatorFamilyId) {
        this.creatorFamilyId = creatorFamilyId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getChangeVersion() {
        return changeVersion;
    }

    public void setChangeVersion(Integer changeVersion) {
        this.changeVersion = changeVersion;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public Byte getIsVideoSupport() {
        return isVideoSupport;
    }

    public void setIsVideoSupport(Byte isVideoSupport) {
        this.isVideoSupport = isVideoSupport;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Byte getVideoState() {
        return videoState;
    }

    public void setVideoState(Byte videoState) {
        this.videoState = videoState;
    }

    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getForumId() {
		return forumId;
	}

	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	public String getCreatorTag() {
		return creatorTag;
	}

	public void setCreatorTag(String creatorTag) {
		this.creatorTag = creatorTag;
	}

	public String getTargetTag() {
		return targetTag;
	}

	public void setTargetTag(String targetTag) {
		this.targetTag = targetTag;
	}

	public Byte getVisibleRegionType() {
		return visibleRegionType;
	}

	public void setVisibleRegionType(Byte visibleRegionType) {
		this.visibleRegionType = visibleRegionType;
	}

	public Long getVisibleRegionId() {
		return visibleRegionId;
	}

	public void setVisibleRegionId(Long visibleRegionId) {
		this.visibleRegionId = visibleRegionId;
	}

	public Byte getChargeFlag() {
		return chargeFlag;
	}

	public void setChargeFlag(Byte chargeFlag) {
		this.chargeFlag = chargeFlag;
	}

	public BigDecimal getChargePrice() {
		return chargePrice;
	}

	public void setChargePrice(BigDecimal chargePrice) {
		this.chargePrice = chargePrice;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

    public Byte getWechatSignup() {
        return wechatSignup;
    }

    public void setWechatSignup(Byte wechatSignup) {
        this.wechatSignup = wechatSignup;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
