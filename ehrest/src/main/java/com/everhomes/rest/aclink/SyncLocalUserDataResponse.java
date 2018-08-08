// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId:用户id</li>
 * <li>userName:用户姓名</li>
 * <li>phone:电话号码</li>
 * <li>namesapceId:域空间id</li>
 * <li>ownerId:所属组织id</li>
 * <li>ownerName:所属主治名称</li>
 * <li>photoUrl:照片url</li>
 * <li>listDoorAuth:授权列表</li>
 * <li>photoId:照片id</li>
 * </ul>
 *
 */
public class SyncLocalUserDataResponse {

	private Long userId;
	private String userName;
	private String phone;
	private Integer namesapceId;
	private Long ownerId;
	private String ownerName;
	private String photoUrl;
	private List<DoorAuthDTO> listDoorAuth;
	private Long photoId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getNamesapceId() {
		return namesapceId;
	}
	public void setNamesapceId(Integer namesapceId) {
		this.namesapceId = namesapceId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public List<DoorAuthDTO> getListDoorAuth() {
		return listDoorAuth;
	}
	public void setListDoorAuth(List<DoorAuthDTO> listDoorAuth) {
		this.listDoorAuth = listDoorAuth;
	}
	
	public Long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
