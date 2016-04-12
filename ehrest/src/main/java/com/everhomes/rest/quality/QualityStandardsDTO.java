package com.everhomes.rest.quality;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 标准id</li>
 *  <li>name: 标准名称</li>
 *  <li>ownerId: 标准所属的主体id</li>
 *  <li>ownerType: 标准所属的主体，如QA</li>
 *  <li>standardNumber: 标准编号</li>
 *  <li>description: 具体内容</li>
 *  <li>categoryId: category表的id</li>
 *  <li>categoryName: 所属类型名称</li>
 *  <li>repeat: 执行周期 com.everhomes.rest.quality.RepeatSettingDTO</li>
 *  <li>executiveGroup: 执行业务组信息 com.everhomes.rest.quality.StandardGroupDTO</li>
 *  <li>reviewGroup: 审阅业务组信息 com.everhomes.rest.quality.StandardGroupDTO</li>
 *  <li>status: 标准状态 com.everhomes.rest.quality.QualityStandardStatus</li>
 *  <li>creatorUid: 创建该标准的用户id</li>
 *  <li>createTime: 创建该标准的时间</li>
 *  <li>operatorUid: 最后对该标准进行修改的用户id</li>
 *  <li>updateTime: 更新该标准的时间</li>
 *  <li>deleterUid: 删除该标准的用户id</li>
 *  <li>deleteTime: 删除该标准的时间</li>
 * </ul>
 */

public class QualityStandardsDTO {

	private Long id;

	private String name;
	
	private String ownerType;
	
	private Long ownerId;
	
	private String standardNumber;
	
	private String description;
	
	private Long categoryId;
	
	private String categoryName;
	
	@ItemType(RepeatSettingsDTO.class)
	private RepeatSettingsDTO repeat;
	
	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> executiveGroup;

	@ItemType(StandardGroupDTO.class)
	private List<StandardGroupDTO> reviewGroup;
	
	private Byte status;
	
	private Long creatorUid;
	
	private Timestamp createTime;
	
	private Long operatorUid;
	
	private Timestamp updateTime;
	
	private Long deleterUid;
	
	private Timestamp deleteTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStandardNumber() {
		return standardNumber;
	}

	public void setStandardNumber(String standardNumber) {
		this.standardNumber = standardNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public RepeatSettingsDTO getRepeat() {
		return repeat;
	}

	public void setRepeat(RepeatSettingsDTO repeat) {
		this.repeat = repeat;
	}
	
	public List<StandardGroupDTO> getExecutiveGroup() {
		return executiveGroup;
	}

	public void setExecutiveGroup(List<StandardGroupDTO> executiveGroup) {
		this.executiveGroup = executiveGroup;
	}

	public List<StandardGroupDTO> getReviewGroup() {
		return reviewGroup;
	}

	public void setReviewGroup(List<StandardGroupDTO> reviewGroup) {
		this.reviewGroup = reviewGroup;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getDeleterUid() {
		return deleterUid;
	}

	public void setDeleterUid(Long deleterUid) {
		this.deleterUid = deleterUid;
	}

	public Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
