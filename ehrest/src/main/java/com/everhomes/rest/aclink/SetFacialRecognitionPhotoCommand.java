// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>imgUri：图片uri</li>
 * <li>imgUrl：图片url</li>
 * </ul>
 */
public class SetFacialRecognitionPhotoCommand {
	private String imgUri;
	private String imgUrl;
	private Long ownerId;
	private Byte ownerType;
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
