// @formatter:off
package com.everhomes.rest.activity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>activityId:活动ID</li>
 *<li>categoryId: 类型</li>
 *<li>contentCategoryId: 主题分类</li>
 *<li>startTime:开始时间,格式:YYYY-MM-DD hh:mm:ss<li>
 *<li>stopTime:结束时间,格式:YYYY-MM-DD hh:mm:ss</li>
 *<li>location:位置</li>
 *<li>checkInFlag:报名标签</li>
 *<li>confirmFlag:确认</li>
 *<li>enrollUserCount:报名人数</li>
 *<li>enrollFamilyCount:报名家庭数</li>
 *<li>checkinUserCount:签到人数</li>
 *<li>checkinFamilyCount:签到家庭数</li>
 *<li>confirmUserCount:确认人数</li>
 *<li>confirmFamilyCount：确认家庭数</li>
 *<li>familyId:家庭ID</li>
 *<li>groupId:圈ID</li>
 *<li>forumId:论坛</li>
 *<li>postId:帖子ID</li>
 *<li>posterUrl:海报链接</li>
 *<li>userActivityStatus:活动登记状态,1 未报名,2 已报名,3 已签到，4 已确认</li>
 *<li>processStatus：处理状态，0 未知,1 未开始，2 进行中，3 已结束</li>
 *<li>uuid:活动唯一的标识</li>
 *<li>mediaUrl:活动url</li>
 *<li>favoriteFlag: 是否收藏标记，参见{@link com.everhomes.rest.forum.PostFavoriteFlag}</li>
 * <li>achievement: 活动成果</li>
 * <li>achievementType: 活动成果文本类型 richtext：富文本, link：第三方链接 </li>
 * <li>achievementRichtextUrl: 活动成果富文本页面url</li>
 * <li>activityAttachmentFlag: 是否有活动附件 true: 有 false: 无</li>
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
    private String location;
    private Integer checkinFlag;
    private Integer confirmFlag;
    private Integer enrollUserCount;
    private Integer enrollFamilyCount;
    private Integer checkinUserCount;
    private Integer checkinFamilyCount;
    private Integer confirmUserCount;
    private Integer confirmFamilyCount;
    private Long groupId;
    private Long familyId;
    private String tag;
    private Double longitude;
    private Double latitude;
    private String subject;
    private String posterUrl;
   
    private Integer userActivityStatus;
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

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
