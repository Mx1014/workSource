package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>namespaceUserToken : 第三方用户标识</li>
 * 	<li>namespaceId : 命令空间</li>
 * 	<li>randomNum : 随机数</li>
 * 	<li>timestamp : 时间戳</li>
 * 	<li>sign : 签名</li>
 * 	<li>key : 公钥</li>
 * 	<li>userName : 用户名</li>
 * 	<li>deviceIdentifier : 设备标识</li>
 * 	<li>pusherIdentify : </li>
 * <ul>
 * 
 */
public class SynThridUserCommand {
	@NotNull
	private String namespaceUserToken;
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Integer randomNum;
	@NotNull
	private Long timestamp;
	@NotNull
	private String sign;
	@NotNull
	private String key;
	
	private String userName;
    
   private String deviceIdentifier;
    
    private String pusherIdentify;

	public String getNamespaceUserToken() {
        return namespaceUserToken;
    }

    public void setNamespaceUserToken(String namespaceUserToken) {
        this.namespaceUserToken = namespaceUserToken;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(Integer randomNum) {
        this.randomNum = randomNum;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeviceIdentifier() {
		return deviceIdentifier;
	}

	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}

	public String getPusherIdentify() {
        return pusherIdentify;
    }

    public void setPusherIdentify(String pusherIdentify) {
        this.pusherIdentify = pusherIdentify;
    }

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
