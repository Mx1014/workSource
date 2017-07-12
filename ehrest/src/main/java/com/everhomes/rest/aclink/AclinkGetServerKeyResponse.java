package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class AclinkGetServerKeyResponse {
	private Long     id;
   private String  version;
   private String groupName;
   private Long groupId;
   private String     name;
   private String hardwareId;
   private String     description;
   private Long     creatorUserId;
   private Byte     ownerType;
   private Byte     doorType;
   private Long     ownerId;
   private String key0;
   private String key1;
   
	public Long getId() {
	return id;
}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getHardwareId() {
		return hardwareId;
	}
	
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getCreatorUserId() {
		return creatorUserId;
	}
	
	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	
	public Byte getOwnerType() {
		return ownerType;
	}
	
	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}
	
	public Byte getDoorType() {
		return doorType;
	}
	
	public void setDoorType(Byte doorType) {
		this.doorType = doorType;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getKey0() {
		return key0;
	}
	
	public void setKey0(String key0) {
		this.key0 = key0;
	}
	
	public String getKey1() {
		return key1;
	}
	
	public void setKey1(String key1) {
		this.key1 = key1;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
