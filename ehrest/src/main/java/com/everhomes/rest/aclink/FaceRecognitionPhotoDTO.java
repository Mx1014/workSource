// @formatter:off
package com.everhomes.rest.aclink;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id</li>
 * <li>userId：用户id</li>
 * <li>imgUri：图片uri</li>
 * <li>imgUrl：图片url</li>
 * <li>syncTime：上次同步时间</li>
 * <li>crearteTime:创建时间</li>
 * <li>syncStatus:是否需同步 1是0否</li>
 * </ul>
 *
 */
public class FaceRecognitionPhotoDTO {
	private Long id;
	private Long userId;
	private String imgUri;
	private String imgUrl;
	private Timestamp syncTime;
	private Timestamp crearteTime;
	private Byte syncStatus;
	private Byte userType;
	private Long authId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Timestamp getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Timestamp syncTime) {
		this.syncTime = syncTime;
	}
	public Byte getSyncStatus() {
		return syncStatus;
	}
	public void setSyncStatus(Byte syncStatus) {
		this.syncStatus = syncStatus;
	}
	
	public Timestamp getCrearteTime() {
		return crearteTime;
	}
	public void setCrearteTime(Timestamp crearteTime) {
		this.crearteTime = crearteTime;
	}
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
	@Override
    public boolean equals(Object obj){
        if (! (obj instanceof FaceRecognitionPhotoDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
	
	@Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
