// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>buildingNames: 小区楼栋号列表</li>
 * <li>addressIds: 地址id列表</li>
 * <li>message: 消息内容</li>
 * </ul>
 */
public class PropCommunityBuildAddessCommand {
    private Long communityId;
    
//    @ItemType(String.class)
//    private List<String> buildingNames;
    
    @ItemType(Long.class)
    private Long[] addressIds;
    
    private String message;
   
    public PropCommunityBuildAddessCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	public List<String> getBuildingNames() {
		//return buildingNames;
		return null;
	}


	public void setBuildingNames(List<String> buildingNames) {
		//this.buildingNames = buildingNames;
	}


	public Long[] getAddressIds() {
		return addressIds;
	}


	public void setAddressIds(Long[] addressIds) {
		this.addressIds = addressIds;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
