package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: community / organaization 见{@link com.everhomes.rest.yellowPage.ServiceAllianceBelongType}</li>
 * <li>ownerId: 项目id/公司id</li>
 * <li>type: 服务联盟类型</li>
 * <li>tagGroup: 筛选父节点{@link com.everhomes.rest.yellowPage.AllianceTagGroupDTO}</li>
 * </ul>
 */
public class UpdateAllianceTagCommand {
	
	private Integer namespaceId;
	
	private String ownerType;
	
	private Long ownerId;
	
	private Long type;
	
	private AllianceTagGroupDTO tagGroup;
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public AllianceTagGroupDTO getTagGroup() {
		return tagGroup;
	}

	public void setTagGroup(AllianceTagGroupDTO tagGroup) {
		this.tagGroup = tagGroup;
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

}
