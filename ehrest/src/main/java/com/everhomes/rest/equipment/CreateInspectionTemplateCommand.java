package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <ul>
 *  <li>ownerId: 模板所属组织等的id</li>
 *  <li>ownerType: 模板所属组织类型，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>name: 模板名称</li>
 *  <li>items: 巡检项信息 参考{@link com.everhomes.rest.equipment.InspectionItemDTO}</li>
 *  <li>communities: 巡检项信息 </li>
 * </ul>
 */
public class CreateInspectionTemplateCommand {
	
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private String name;

	@ItemType(Long.class)
	private  List<Long> communities;
	
	@ItemType(InspectionItemDTO.class)
	private List<InspectionItemDTO> items;
	
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

	public List<InspectionItemDTO> getItems() {
		return items;
	}

	public void setItems(List<InspectionItemDTO> items) {
		this.items = items;
	}

	public List<Long> getCommunities() {
		return communities;
	}

	public void setCommunities(List<Long> communities) {
		this.communities = communities;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
