package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>namespaceUserToken : 第三方用户标识</li>
 * 	<li>namespaceId : 命令空间</li>
 * 	<li>signature : 签名</li>
 * 	<li>randomNum : 随机数</li>
 * 	<li>timestamp : 时间戳</li>
 * 	<li>appKey : 公钥</li>
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

    @Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
