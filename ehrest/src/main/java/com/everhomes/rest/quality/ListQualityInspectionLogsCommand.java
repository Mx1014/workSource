package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <ul>
 *  <li>ownerId: 记录所属的主体id</li>
 *  <li>ownerType: 记录所属的主体，com.everhomes.rest.quality.OwnerType</li>
 *  <li>targetType: 目标类型，标准的话为standard</li>
 *  <li>targetId: 目标id</li>
 *  <li>scopeCode: 类型 {@link com.everhomes.rest.quality.SpecificationScopeCode}</li>
 *  <li>scopeId: 项目id</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * </ul>
 */
public class ListQualityInspectionLogsCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	@NotNull
	private String targetType;
	
	private Long targetId;

	@ItemType(Long.class)
	private List<Long> scopeIds;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	private  Byte scopeCode;

	private  Long scopeId;

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
	
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
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

	public Byte getScopeCode() {
		return scopeCode;
	}

	public void setScopeCode(Byte scopeCode) {
		this.scopeCode = scopeCode;
	}

	public Long getScopeId() {
		return scopeId;
	}

	public void setScopeId(Long scopeId) {
		this.scopeId = scopeId;
	}

	public List<Long> getScopeIds() {
		return scopeIds;
	}

	public void setScopeIds(List<Long> scopeIds) {
		this.scopeIds = scopeIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
