// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId:所属组织Id</li>
 * <li>ownerType：所属组织类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>eventType:事件类型 null 全部 ,0 蓝牙开门, 1 二维码开门,2 远程开门,3 人脸开门</li>
 * <li>keyWord:关键字</li>
 * <li>macAddresses:门禁mac地址列表</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:分页大小</li>
 * <li>validFromMs:开门起始时间</li>
 * <li>validEndMs:开门截止时间</li>
 * </ul>
 *
 */
public class OpenQueryLogCommand {
    private Byte ownerType;
    private Long ownerId;
    private Long eventType;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
	private List<String> macAddresses;
	private String appKey;
	private String signature;
	private Long timestamp;
	private Integer nonce;
	private String crypto;
	private Long validFromMs;
	private Long validEndMs;
 
	
    public Long getValidFromMs() {
		return validFromMs;
	}
	public void setValidFromMs(Long validFromMs) {
		this.validFromMs = validFromMs;
	}
	public Long getValidEndMs() {
		return validEndMs;
	}
	public void setValidEndMs(Long validEndMs) {
		this.validEndMs = validEndMs;
	}
	public Long getEventType() {
        return eventType;
    }
    public void setEventType(Long eventType) {
        this.eventType = eventType;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Long getPageAnchor() {
        return pageAnchor;
    }
    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
 
    public List<String> getMacAddresses() {
		return macAddresses;
	}
	public void setMacAddresses(List<String> macAddresses) {
		this.macAddresses = macAddresses;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getNonce() {
		return nonce;
	}
	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}
	public String getCrypto() {
		return crypto;
	}
	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
