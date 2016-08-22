// @formatter:off
package com.everhomes.rest.organization.pm;

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
    
    @ItemType(String.class)
    private List<String> buildingNames;
    
    @ItemType(Long.class)
    private List<Long> buildingIds;
    
    @ItemType(Long.class)
    private List<Long> addressIds;
    
    @ItemType(String.class)
    private List<String> mobilePhones;
    
    private String message;
    
    private String messageBodyType;
    
    private String imgUri;
   
    public PropCommunityBuildAddessCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	public List<String> getBuildingNames() {
		return buildingNames;
		
	}


	public void setBuildingNames(List<String> buildingNames) {
		this.buildingNames = buildingNames;
	}


	public List<Long> getAddressIds() {
		return addressIds;
	}


	public void setAddressIds(List<Long> addressIds) {
		this.addressIds = addressIds;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	



	public List<String> getMobilePhones() {
		return mobilePhones;
	}


	public void setMobilePhones(List<String> mobilePhones) {
		this.mobilePhones = mobilePhones;
	}


	public List<Long> getBuildingIds() {
		return buildingIds;
	}


	public void setBuildingIds(List<Long> buildingIds) {
		this.buildingIds = buildingIds;
	}


	public String getMessageBodyType() {
		return messageBodyType;
	}


	public void setMessageBodyType(String messageBodyType) {
		this.messageBodyType = messageBodyType;
	}

	

	public String getImgUri() {
		return imgUri;
	}


	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
