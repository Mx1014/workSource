// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.everhomes.util.StringHelper;
/**
 * 注册
 * @author elians
 *<ul>
 *<li>type:标识类型,email或者mobile</li>
 *<li>identifierToken:手机号或者邮箱</li>
 *<li>ifExistsThenOverride:如果identifier已经存在是否进行覆盖</li>
 *<ul>
 */
public class SignupCommand {
    @Pattern(regexp = "mobile|email")
    @NotNull
    String type;
    
    @NotNull
    String token;
    
    
    Integer ifExistsThenOverride;
    
    public SignupCommand() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public Integer getIfExistsThenOverride() {
        return this.ifExistsThenOverride;
    }
    
    public void setIfExistsThenOverride(Integer ifExistsThenOverride) {
        this.ifExistsThenOverride = ifExistsThenOverride;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
