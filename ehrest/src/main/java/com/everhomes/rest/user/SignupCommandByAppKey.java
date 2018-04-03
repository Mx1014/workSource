// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.everhomes.util.StringHelper;
/**
 * 注册
 * @author elians
 *<ul>
 *<li>type:标识类型,email或者mobile</li>
 *<li>userIdentifier:手机号或者邮箱，此command拷贝自{@link com.everhomes.rest.user.SignupCommand}，
 *       由于原来使用token字段来填手机号，但token属于特殊字段，会导致Webtoken解释异常，故在新接口把字段名称修改一下</li>
 *<li>ifExistsThenOverride:如果identifier已经存在是否进行覆盖</li>
 *<li>channel_id:渠道</li>
 *<li>namespaceId:名字空间ID</li>
 * <li>regionCode:区号</li>
 *<ul>
 */
public class SignupCommandByAppKey {
    @Pattern(regexp = "mobile|email")
    @NotNull
    private String type;
    
    @NotNull
    //private String token;
    private String userIdentifier;
    
    private Long channel_id;

    @NotNull
    private Integer regionCode;

    private Integer ifExistsThenOverride;
    
    private Integer namespaceId;
    
    public Long getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(Long channel_id) {
		this.channel_id = channel_id;
	}

	public SignupCommandByAppKey() {
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public Integer getIfExistsThenOverride() {
        return this.ifExistsThenOverride;
    }
    
    public void setIfExistsThenOverride(Integer ifExistsThenOverride) {
        this.ifExistsThenOverride = ifExistsThenOverride;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
