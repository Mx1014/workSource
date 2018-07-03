// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>imgUri：图片uri</li>
 * <li>imgUrl：图片url</li>
 * <li>userType:用户类型0正式用户1访客</li>
 * <li>authId:授权id</li>
 * </ul>
 */
public class SetFacialRecognitionPhotoCommand {
	private String imgUri;
	private String imgUrl;
	private Long ownerId;
	private Byte ownerType;
	private Byte userType;
	private Long authId;
	public Byte getUserType() {
		return userType;
	}
	public void setUserType(Byte userType) {
		this.userType = userType;
	}
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public String getImgUri() {
		return imgUri;
	}
	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Byte getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
