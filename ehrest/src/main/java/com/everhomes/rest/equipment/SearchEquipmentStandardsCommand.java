package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 标准所属组织等的id</li>
 *  <li>ownerType: 标准所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 标准所属项目id</li>
 *  <li>targetType: 标准所属项目类型</li>
 *  <li>status: 标准状态 参考{@link com.everhomes.rest.equipment.EquipmentStandardStatus}</li>
 *  <li>repeatType:标准周期  0:no repeat 1: by day 2:by week 3: by month 4:year</li>
 *  <li>categoryId:设备具体类型 </li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>targetIdFlag: 要不要取没有targetId的数据 参考{@link com.everhomes.rest.equipment.TargetIdFlag}</li>
 *  <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class SearchEquipmentStandardsCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	@ItemType(Long.class)
	private List<Long> targetIds;

	private String targetType;

	@Deprecated
	private Byte standardType;

	private  Byte repeatType;

	private Byte status;

	//巡检V3.0.2增加
	private  Long categoryId;
	
	private String keyword;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	private Long inspectionCategoryId;

	private Byte targetIdFlag;

	private Integer namespaceId;

	public Byte getTargetIdFlag() {
		return targetIdFlag;
	}

	public void setTargetIdFlag(Byte targetIdFlag) {
		this.targetIdFlag = targetIdFlag;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getInspectionCategoryId() {
		return inspectionCategoryId;
	}

	public void setInspectionCategoryId(Long inspectionCategoryId) {
		this.inspectionCategoryId = inspectionCategoryId;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Byte getStandardType() {
		return standardType;
	}

	public void setStandardType(Byte standardType) {
		this.standardType = standardType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Byte getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(Byte repeatType) {
		this.repeatType = repeatType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
