// @formatter:off
package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;


/**
 * <ul> 注册流程，绑定已有用户到企业：根据已有用户ID创建企业用户，从而成为此企业的一个成员
 * <li>userId: 用户ID</li>
 * <li>enterpriseId: 可以具体指定到企业，或者由手机号自动查询到相关企业</li>
 * </ul>
 * @author janson
 *
 */
public class CreateContactByUserIdCommand {
    //Use current user as userId
    //@NotNull
    private Long userId;
    
    private Long enterpriseId;
    private Long groupId;
    private String   name;
    private String   nickName;
    private String   avatar;
    private String applyGroup;
    
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
    public String getApplyGroup() {
        return applyGroup;
    }
    public void setApplyGroup(String applyGroup) {
        this.applyGroup = applyGroup;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
