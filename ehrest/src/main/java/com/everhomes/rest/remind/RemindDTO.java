package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 日程ID</li>
 * <li>contactName: 日程所有人的姓名</li>
 * <li>ownerUserId: 日程所有人的userId</li>
 * <li>planDescription: 日程描述</li>
 * <li>planDate: 日期时间戳</li>
 * <li>planTime: 日程的时间 时分秒的毫秒数0到86400000，可选项(默认9点)</li>
 * <li>repeatType: 重复类型，参考{@link com.everhomes.rest.remind.RemindRepeatType}</li>
 * <li>remindDisplayName: 提醒时间,如提前一天（09:00）</li>
 * <li>remindTypeId: 提醒类型ID</li>
 * <li>remindTime: 提醒时间的时间戳</li>
 * <li>remindCategoryId: 分类ID</li>
 * <li>displayCategoryName: 列表显示的分类或者同事的日程字样</li>
 * <li>displayColour: 列表显示的日程颜色</li>
 * <li>shareCount: 共享人的数量</li>
 * <li>trackStatus: 是否已经添加到我的日程，参考{@link com.everhomes.rest.remind.SubscribeStatus}</li>
 * <li>trackRemindId: 我关注的日程的原始日程ID</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.remind.RemindStatus}</li>
 * <li>shareToMembers: 默认共享人的档案ID列表，可选项，参考{@link com.everhomes.rest.remind.ShareMemberDTO}</li>
 * <li>shareShortDisplay: 分享人显示概要，如YH等3人</li>
 * <li>defaultOrder: 排序序号</li>
 * </ul>
 */
public class RemindDTO {
    private Long id;
    private String contactName;
    private Long ownerUserId;
    private String planDescription;
    private Long planDate;
    private Long planTime;
    private Byte repeatType;
    private Integer remindTypeId;
    private String remindDisplayName;
    private Long remindTime;
    private Long remindCategoryId;
    private String displayCategoryName;
    private String displayColour;
    private Byte status;
    private List<ShareMemberDTO> shareToMembers;
    private Integer shareCount;
    private String shareShortDisplay;
    private Byte trackStatus;
    private Long trackRemindId;
    private Integer defaultOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public Long getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Long planDate) {
        this.planDate = planDate;
    }

    public Byte getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Byte repeatType) {
        this.repeatType = repeatType;
    }

    public String getRemindDisplayName() {
        return remindDisplayName;
    }

    public void setRemindDisplayName(String remindDisplayName) {
        this.remindDisplayName = remindDisplayName;
    }

    public Long getRemindCategoryId() {
        return remindCategoryId;
    }

    public void setRemindCategoryId(Long remindCategoryId) {
        this.remindCategoryId = remindCategoryId;
    }

    public List<ShareMemberDTO> getShareToMembers() {
        return shareToMembers;
    }

    public void setShareToMembers(List<ShareMemberDTO> shareToMembers) {
        this.shareToMembers = shareToMembers;
    }

    public String getShareShortDisplay() {
        return shareShortDisplay;
    }

    public void setShareShortDisplay(String shareShortDisplay) {
        this.shareShortDisplay = shareShortDisplay;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getRemindTypeId() {
        return remindTypeId;
    }

    public void setRemindTypeId(Integer remindTypeId) {
        this.remindTypeId = remindTypeId;
    }

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDisplayCategoryName() {
        return displayCategoryName;
    }

    public void setDisplayCategoryName(String displayCategoryName) {
        this.displayCategoryName = displayCategoryName;
    }

    public String getDisplayColour() {
        return displayColour;
    }

    public void setDisplayColour(String displayColour) {
        this.displayColour = displayColour;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Byte getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(Byte trackStatus) {
        this.trackStatus = trackStatus;
    }

    public Long getTrackRemindId() {
        return trackRemindId;
    }

    public void setTrackRemindId(Long trackRemindId) {
        this.trackRemindId = trackRemindId;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Long planTime) {
		this.planTime = planTime;
	}
}
