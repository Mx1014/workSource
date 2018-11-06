package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 设备所属组织等的id</li>
 *  <li>ownerType: 设备所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>targetId: 设备所属管理处id</li>
 *  <li>targetType: 设备所属管理处类型</li>
 *  <li>categoryId: 设备类型id</li>
 *  <li>status: 设备状态 参考{@link com.everhomes.rest.equipment.EquipmentStatus}</li>
 *  <li>reviewStatus: 设备-标准关联状态，参考{@link com.everhomes.rest.equipment.EquipmentReviewStatus}</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>inspectionCategoryId: 巡检对象类型id</li>
 *  <li>namespaceId: 域空间id</li>
 *  <li>communityId: 项目id</li>
 *  <li>moduleName: 模块名</li>
 *  <li>groupPath: 所属字段组在系统中的path</li>
 *  <li>equipmentIds: 设备id列表用于导出excel  get 方式 传入类型为String</li>
 * </ul>
 */
public class SearchEquipmentsCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;

	@ItemType(Long.class)
	private List<Long> targetIds;

	private String targetIdString;

	private String targetType;
	
	private Byte reviewStatus;
	
	private Byte reviewResult;
	
	private Byte status;
	
	private Long categoryId;
	
	private String keyword;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	
	private Long inspectionCategoryId;

	private Integer namespaceId;

	private Long communityId;

	private String moduleName;

	private String groupPath;

//	@ItemType(Long.class)
	private String equipmentIds;

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

	public Byte getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Byte reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getGroupPath() {
		return groupPath;
	}

	public void setGroupPath(String groupPath) {
		this.groupPath = groupPath;
	}

	public String getEquipmentIds() {
		return equipmentIds;
	}

	public void setEquipmentIds(String equipmentIds) {
		this.equipmentIds = equipmentIds;
	}

	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	public String getTargetIdString() {
		return targetIdString;
	}

	public void setTargetIdString(String targetIdString) {
		this.targetIdString = targetIdString;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
