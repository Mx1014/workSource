package com.everhomes.rest.equipment;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 模板id</li>
 *  <li>ownerId: 模板所属组织等的id</li>
 *  <li>ownerType: 模板所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>name: 模板名称</li>
 *  <li>createTime: 添加时间</li>
 *  <li>items: 巡检项信息 参考{@link com.everhomes.rest.equipment.InspectionItemDTO}</li>
 * </ul>
 */
public class InspectionTemplateDTO {

	private Long id;
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private String name;
	
	private Timestamp createTime;
	
	@ItemType(InspectionItemDTO.class)
	private List<InspectionItemDTO> items;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<InspectionItemDTO> getItems() {
		return items;
	}

	public void setItems(List<InspectionItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
