package com.everhomes.rest.quality;



import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 任务所属的主体id</li>
 *  <li>ownerType: 任务所属的主体，com.everhomes.rest.quality.OwnerType</li>
 *  <li>name: 任务名称</li>
 *  <li>categoryId: category表中的id</li>
 *  <li>group: 业务组信息 com.everhomes.rest.quality.StandardGroupDTO</li>
 * </ul>
 */
public class CreateQualityInspectionTaskCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private String name;
	
	@NotNull
	private Long categoryId;
	
	@NotNull
	@ItemType(StandardGroupDTO.class)
	private StandardGroupDTO group;

	
	
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public StandardGroupDTO getGroup() {
		return group;
	}


	public void setGroup(StandardGroupDTO group) {
		this.group = group;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
