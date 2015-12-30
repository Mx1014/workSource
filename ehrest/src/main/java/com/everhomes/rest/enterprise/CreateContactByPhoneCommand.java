package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

/**
 * <ul> 注册流程：最开始，用户未存在，根据手机创建企业用户，从而成为此企业的一个成员
 * <li>phone: 手机号码</li>
 * <li>enterpriseId: 可以具体指定到企业，或者由手机号自动查询到相关企业</li>
 * </ul>
 * @author janson
 *
 */
public class CreateContactByPhoneCommand {
    @NotNull
    private String phone;
    
    private Long enterpriseId;
    private String   name;
    private String   nickName;
    private String   avatar;
    
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Long getEnterpriseId() {
        return enterpriseId;
    }
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    
}
