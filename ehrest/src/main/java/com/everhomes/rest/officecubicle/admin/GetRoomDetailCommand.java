package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId:域空间id</li>
 * <li>ownerId:项目id</li>
 * <li>ownerType:项目类型</li>
 * <li>spaceId:空间id</li>
 * <li>roomId:办公室id</li>
 * <li>status:(0-未开放，1-未预定，2-已预定)</li>
 * </ul>
 */
public class GetRoomDetailCommand {
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long spaceId;
	private Long roomId;
	private Byte status;
    

	



	public Byte getStatus() {
		return status;
	}




	public void setStatus(Byte status) {
		this.status = status;
	}




	public Long getSpaceId() {
		return spaceId;
	}




	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}




	public Long getRoomId() {
		return roomId;
	}




	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}




	public Integer getNamespaceId() {
		return namespaceId;
	}




	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
