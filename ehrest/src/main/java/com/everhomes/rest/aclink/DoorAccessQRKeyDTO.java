package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: key 的唯一 ID</li>
 * <li>doorGroupId: key 所在的组 ID </li>
 * <li>doorName: 组名字</li>
 * <li>qrCodeKey: 生成二维码相关的密钥 </li>
 * <li>qrDriver: QR 门禁类型 参考 {@link com.everhomes.rest.aclink.DoorAccessDriverType}</li>
 * <li>hardwares: 钥匙支持的设备列表 </li>
 * <li>extra: 额外对象描述</li>
 * </ul>
 * @author janson
 *
 */
public class DoorAccessQRKeyDTO {
    private Long id;
    private Long doorGroupId;
    private String doorName;
    
    private String qrCodeKey;
    private String qrDriver;
    private Long     creatorUid;
    
    private Long     currentTime;
    private Long 		qrImageTimeout;
    private Long     createTimeMs;
    private Long     expireTimeMs;
    
    private Byte     status;
    
    @ItemType(String.class)
    private List<String> hardwares;
    
    private String extra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoorGroupId() {
        return doorGroupId;
    }

    public void setDoorGroupId(Long doorGroupId) {
        this.doorGroupId = doorGroupId;
    }

    public String getDoorName() {
        return doorName;
    }

    public void setDoorName(String doorName) {
        this.doorName = doorName;
    }

    public String getQrCodeKey() {
        return qrCodeKey;
    }

    public void setQrCodeKey(String qrCodeKey) {
        this.qrCodeKey = qrCodeKey;
    }

    public String getQrDriver() {
        return qrDriver;
    }

    public void setQrDriver(String qrDriver) {
        this.qrDriver = qrDriver;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getCreateTimeMs() {
        return createTimeMs;
    }

    public void setCreateTimeMs(Long createTimeMs) {
        this.createTimeMs = createTimeMs;
    }

    public Long getExpireTimeMs() {
        return expireTimeMs;
    }

    public void setExpireTimeMs(Long expireTimeMs) {
        this.expireTimeMs = expireTimeMs;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    
    public List<String> getHardwares() {
        return hardwares;
    }

    public void setHardwares(List<String> hardwares) {
        this.hardwares = hardwares;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

	public Long getQrImageTimeout() {
		return qrImageTimeout;
	}

	public void setQrImageTimeout(Long qrImageTimeout) {
		this.qrImageTimeout = qrImageTimeout;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
