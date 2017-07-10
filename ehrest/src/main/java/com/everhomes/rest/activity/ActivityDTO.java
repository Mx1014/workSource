// @formatter:off
package com.everhomes.rest.activity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 *<ul>
 *<li>activityId:活动ID</li>
 *<li>categoryId: 类型</li>
 *<li>contentCategoryId: 主题分类</li>
 *<li>startTime:开始时间,格式:YYYY-MM-DD hh:mm:ss<li>
 *<li>stopTime:结束时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>signupEndTime:活动报名截止时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>location:位置</li>
 *<li>checkInFlag:报名标签</li>
 *<li>confirmFlag:确认</li>
 *<li>enrollUserCount:已报名人数</li>
 *<li>enrollFamilyCount:报名家庭数</li>
 *<li>confirmUserCount:已确认人数</li>
 *<li>unConfirmUserCount:待确认人数</li>
 *<li>payUserCount:已支付人数</li>
 *<li>unPayUserCount:待支付人数</li>
 *<li>checkinUserCount:已签到人数</li>
 *<li>unCheckinUserCount:待签到人数</li>
 *<li>checkinFamilyCount:签到家庭数</li>
 *<li>confirmFamilyCount：确认家庭数</li>
 *<li>familyId:家庭ID</li>
 *<li>groupId:圈ID</li>
 *<li>forumId:论坛</li>
 *<li>postId:帖子ID</li>
 *<li>posterUrl:海报链接</li>
 *<li>posterUri:海报链接Uri</li>
 *<li>userActivityStatus:活动登记状态,1 未报名,2 已报名,3 已签到，4 已确认</li>
 *<li>userPayFlag:支付状态  0: no pay, 1:have pay, 2:refund 参考 {@link com.everhomes.rest.activity.ActivityRosterPayFlag }</li>
 *<li>userOrderCountdown:订单倒计时时间长度</li>
 *<li>userRosterId:订单倒计时时间长度</li>
 *<li>processStatus：处理状态，0 未知,1 未开始，2 进行中，3 已结束</li>
 *<li>uuid:活动唯一的标识</li>
 *<li>mediaUrl:活动url</li>
 *<li>favoriteFlag: 是否收藏标记，参见{@link com.everhomes.rest.forum.PostFavoriteFlag}</li>
 * <li>achievement: 活动成果</li>
 * <li>achievementType: 活动成果文本类型 richtext：富文本, link：第三方链接 </li>
 * <li>achievementRichtextUrl: 活动成果富文本页面url</li>
 * <li>activityAttachmentFlag: 是否有活动附件 true: 有 false: 无</li>
 * <li>chargeFlag: 是否收费：0-不收费， 1-收费  参考{@link com.everhomes.rest.activity.ActivityChargeFlag }</li>
 * <li>chargePrice: 收费价格</li>
 * <li>createTime: 创建时间</li>
 * <li>systemTime: 系统时间</li>
 * <li>wechatSignup: 是否支持微信报名，0-不支持，1-支持 参考  参考{@link com.everhomes.rest.activity.WechatSignupFlag }</li>
 *</ul>
 */
public class ActivityDTO {
    private Long activityId;
    private Integer namespaceId;
    private Long postId;
    private Long forumId;
    private Long categoryId;
    private Long contentCategoryId;
    private String startTime;
    private String stopTime;
    private Byte allDayFlag;
    private String signupEndTime;
    private String location;
    private Integer checkinFlag;
    private Integer confirmFlag;
    private Integer enrollUserCount;
    private Integer enrollFamilyCount;
    private Integer confirmUserCount;
    private Integer unConfirmUserCount;
    private Integer payUserCount;
    private Integer unPayUserCount;
    private Integer checkinUserCount;
    private Integer unCheckinUserCount;
    private Integer checkinFamilyCount;
    private Integer confirmFamilyCount;
    private Long groupId;
    private Long familyId;
    private String tag;
    private Double longitude;
    private Double latitude;
    private String subject;
    private String posterUrl;
    private String posterUri;
   
    private Integer userActivityStatus;
    private Byte userPayFlag;
    private Long userOrderCountdown;
    private Long userRosterId;
    private Integer processStatus;
    private String uuid;
    private String guest;
    
    private String mediaUrl;
    
    private Byte favoriteFlag;
    
    // Add by jannson
    private Byte isVideoSupport;
    private Long videoId;
    private String manufacturerType;
    private String videoUrl;    //yzb://[vid]
    private Byte videoState;

    private String description;
    private String contentType;
    private String content;
    private Integer maxQuantity;
    private String version;

    private String achievementType;

    private String achievement;

    private boolean activityAttachmentFlag;

    private String achievementRichtextUrl;
    
    private Byte chargeFlag;

    private BigDecimal chargePrice;
    
    private Long createTime;
    
    private Long systemTime;

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

	public String getAchievementRichtextUrl() {
        return achievementRichtextUrl;
    }

    public void setAchievementRichtextUrl(String achievementRichtextUrl) {
        this.achievementRichtextUrl = achievementRichtextUrl;
    }

    public String getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public boolean isActivityAttachmentFlag() {
        return activityAttachmentFlag;
    }

    public void setActivityAttachmentFlag(boolean activityAttachmentFlag) {
        this.activityAttachmentFlag = activityAttachmentFlag;
    }

    public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ActivityDTO() {
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }
    
    public Byte getAllDayFlag() {
		return allDayFlag;
	}

	public void setAllDayFlag(Byte allDayFlag) {
		this.allDayFlag = allDayFlag;
	}

	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Integer getCheckinFlag() {
        return checkinFlag;
    }

    public void setCheckinFlag(Integer checkinFlag) {
        this.checkinFlag = checkinFlag;
    }

    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Integer getEnrollUserCount() {
        return enrollUserCount;
    }

    public void setEnrollUserCount(Integer enrollUserCount) {
        this.enrollUserCount = enrollUserCount;
    }

    public Integer getEnrollFamilyCount() {
        return enrollFamilyCount;
    }

    public void setEnrollFamilyCount(Integer enrollFamilyCount) {
        this.enrollFamilyCount = enrollFamilyCount;
    }

    public Integer getCheckinUserCount() {
        return checkinUserCount;
    }

    public void setCheckinUserCount(Integer checkinUserCount) {
        this.checkinUserCount = checkinUserCount;
    }

    public Integer getUnCheckinUserCount() {
		return unCheckinUserCount;
	}

	public void setUnCheckinUserCount(Integer unCheckinUserCount) {
		this.unCheckinUserCount = unCheckinUserCount;
	}

	public Integer getPayUserCount() {
		return payUserCount;
	}

	public void setPayUserCount(Integer payUserCount) {
		this.payUserCount = payUserCount;
	}

	public Integer getUnPayUserCount() {
		return unPayUserCount;
	}

	public void setUnPayUserCount(Integer unPayUserCount) {
		this.unPayUserCount = unPayUserCount;
	}

	public Integer getCheckinFamilyCount() {
        return checkinFamilyCount;
    }

    public void setCheckinFamilyCount(Integer checkinFamilyCount) {
        this.checkinFamilyCount = checkinFamilyCount;
    }

    public Integer getConfirmUserCount() {
        return confirmUserCount;
    }

    public void setConfirmUserCount(Integer confirmUserCount) {
        this.confirmUserCount = confirmUserCount;
    }

    public Integer getUnConfirmUserCount() {
		return unConfirmUserCount;
	}

	public void setUnConfirmUserCount(Integer unConfirmUserCount) {
		this.unConfirmUserCount = unConfirmUserCount;
	}

	public Integer getConfirmFamilyCount() {
        return confirmFamilyCount;
    }

    public void setConfirmFamilyCount(Integer confirmFamilyCount) {
        this.confirmFamilyCount = confirmFamilyCount;
    }

    public Integer getUserActivityStatus() {
        return userActivityStatus;
    }

    public void setUserActivityStatus(Integer userActivityStatus) {
        this.userActivityStatus = userActivityStatus;
    }

	public Byte getUserPayFlag() {
		return userPayFlag;
	}

	public void setUserPayFlag(Byte userPayFlag) {
		this.userPayFlag = userPayFlag;
	}

	public Long getUserOrderCountdown() {
		return userOrderCountdown;
	}

	public void setUserOrderCountdown(Long userOrderCountdown) {
		this.userOrderCountdown = userOrderCountdown;
	}

	public Long getUserRosterId() {
		return userRosterId;
	}

	public void setUserRosterId(Long userRosterId) {
		this.userRosterId = userRosterId;
	}

	public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }
    
    
    
    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
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



    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    
    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

	public Byte getFavoriteFlag() {
		return favoriteFlag;
	}

	public void setFavoriteFlag(Byte favoriteFlag) {
		this.favoriteFlag = favoriteFlag;
	}
	
	public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getManufacturerType() {
        return manufacturerType;
    }

    public void setManufacturerType(String manufacturerType) {
        this.manufacturerType = manufacturerType;
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

    public Byte getIsVideoSupport() {
        return isVideoSupport;
    }

    public void setIsVideoSupport(Byte isVideoSupport) {
        this.isVideoSupport = isVideoSupport;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(Long systemTime) {
		this.systemTime = systemTime;
	}

    public Byte getWechatSignup() {
        return wechatSignup;
    }

    public void setWechatSignup(Byte wechatSignup) {
        this.wechatSignup = wechatSignup;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
