// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>status：1有效 0失效 {@link com.everhomes.rest.aclink.AesUserKeyStatus}</li>
 * <li>keyId: 钥匙id</li>
 * <li>createTimeMs：创建时间</li>
 * <li>creatorUid：创建者id</li>
 * <li>keyType：普通((byte)0), 临时((byte)1), 管理员((byte)2) {@link com.everhomes.rest.aclink.AesUserKeyType}</li>
 * <li>userId：用户id</li>
 * <li>secret</li>
 * <li>expireTimeMs：有效时间</li>
 * <li>doorId：门禁id</li>
 * <li>id</li>
 * <li>hardwareId：mac地址</li>
 * <li>doorName：门禁名称</li>
 * <li>authId：授权id</li>
 * <li>macCopy</li>
 * <li>rightRemote:远程开门权限 1有 0无</li>
 * <li>rightFaceOpen:扫脸开门权限 1有0无</li>
 * </ul>
 */
public class AesUserKeyDTO implements Comparable<AesUserKeyDTO>{
    private Byte     status;
    private Byte     keyId;
    private Long     createTimeMs;
    private Long     creatorUid;
    private Byte     keyType;
    private Long     userId;
    private String     secret;
    private Long     expireTimeMs;
    private Long     doorId;
    private Long     id;
    private String hardwareId;
    private String doorName;
    private Long authId;
    private String macCopy;
    private Byte rightRemote;
    private Byte rightFaceOpen;

    

    public Byte getStatus() {
        return status;
    }



    public void setStatus(Byte status) {
        this.status = status;
    }



    public Byte getKeyId() {
        return keyId;
    }



    public void setKeyId(Byte keyId) {
        this.keyId = keyId;
    }



    public Long getCreateTimeMs() {
        return createTimeMs;
    }



    public void setCreateTimeMs(Long createTimeMs) {
        this.createTimeMs = createTimeMs;
    }



    public Long getCreatorUid() {
        return creatorUid;
    }



    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }



    public Byte getKeyType() {
        return keyType;
    }



    public void setKeyType(Byte keyType) {
        this.keyType = keyType;
    }



    public Long getUserId() {
        return userId;
    }



    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public String getSecret() {
        return secret;
    }



    public void setSecret(String secret) {
        this.secret = secret;
    }



    public Long getExpireTimeMs() {
        return expireTimeMs;
    }



    public void setExpireTimeMs(Long expireTimeMs) {
        this.expireTimeMs = expireTimeMs;
    }



    public Long getDoorId() {
        return doorId;
    }



    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getHardwareId() {
        return hardwareId;
    }



    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }



    public String getDoorName() {
        return doorName;
    }



    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }



    public Long getAuthId() {
        return authId;
    }



    public void setAuthId(Long authId) {
        this.authId = authId;
    }



    public String getMacCopy() {
		return macCopy;
	}



	public void setMacCopy(String macCopy) {
		this.macCopy = macCopy;
	}



	public Byte getRightRemote() {
		return rightRemote;
	}



	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
	}



	public Byte getRightFaceOpen() {
		return rightFaceOpen;
	}



	public void setRightFaceOpen(Byte rightFaceOpen) {
		this.rightFaceOpen = rightFaceOpen;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	@Override
	public int compareTo(AesUserKeyDTO o) {
		if(this.authId < o.getAuthId()){
			return 1;
		}
		return -1;
	}
}
