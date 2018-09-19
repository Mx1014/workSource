package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 日程ID，没有值时进行添加操作，有值时进行更新操作</li>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>planDescription: 日程描述，必填</li>
 * <li>planDate: 日程的日期时间戳，可选项</li>
 * <li>planTime: 日程的时间 时分秒的毫秒数0到86400000，可选项(默认9点)</li>
 * <li>repeatType: 重复类型，参考{@link com.everhomes.rest.remind.RemindRepeatType}，可选项</li>
 * <li>remainTypeId: 提醒时间类型ID，可选项</li>
 * <li>remindCategoryId: 分类ID，必填</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.remind.RemindStatus}</li>
 * <li>shareToMembers: 默认共享人的档案ID列表，可选项，参考{@link com.everhomes.rest.remind.ShareMemberDTO}</li>
 * </ul>
 */
public class CreateOrUpdateRemindCommand {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String planDescription;
    private Long planDate;
    private Long planTime;
    private Byte repeatType;
    private Integer remainTypeId;
    private Long remindCategoryId;
    private Byte status;
    private List<ShareMemberDTO> shareToMembers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Integer getRemainTypeId() {
        return remainTypeId;
    }

    public void setRemainTypeId(Integer remainTypeId) {
        this.remainTypeId = remainTypeId;
    }

    public Long getRemindCategoryId() {
        return remindCategoryId;
    }

    public void setRemindCategoryId(Long remindCategoryId) {
        this.remindCategoryId = remindCategoryId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<ShareMemberDTO> getShareToMembers() {
        return shareToMembers;
    }

    public void setShareToMembers(List<ShareMemberDTO> shareToMembers) {
        this.shareToMembers = shareToMembers;
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
