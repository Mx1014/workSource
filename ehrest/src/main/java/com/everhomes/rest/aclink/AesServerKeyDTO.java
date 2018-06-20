package com.everhomes.rest.aclink;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id:id</li>
 * <li>doorId:门禁id</li>
 * <li>deviceVer:设备版本 默认0</li>
 * <li>secretVer:密钥版本 默认1</li>
 * <li>secret:密钥</li>
 * <li>createTimeMs:创建时间</li>
 * </ul>
 *
 */
public class AesServerKeyDTO {
	private Long id;
	private Long doorId;
	private Byte deviceVer;
	private Long secretVer;
	private String secret;
	private Long createTimeMs;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Byte getDeviceVer() {
		return deviceVer;
	}
	public void setDeviceVer(Byte deviceVer) {
		this.deviceVer = deviceVer;
	}
	public Long getSecretVer() {
		return secretVer;
	}
	public void setSecretVer(Long secretVer) {
		this.secretVer = secretVer;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public Long getCreateTimeMs() {
		return createTimeMs;
	}
	public void setCreateTimeMs(Long createTimeMs) {
		this.createTimeMs = createTimeMs;
	}
	
	@Override
    public boolean equals(Object obj){
        if (! (obj instanceof AesServerKeyDTO)) {
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
