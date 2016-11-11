// @formatter:off
package  com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul> 注册流程，绑定已有用户到企业：根据已有用户ID创建企业用户，从而成为此企业的一个成员
 * <li>communityId: 取一个园区的企业</li>
 * <li>email: 邮箱地址-通过这个验证用户是否在本小区的某公司</li>
 * </ul>
 * @author wh
 *
 */
public class ListOrganizationsByEmailCommand {
    //Use current user as userId
    //@NotNull

	private Long communityId; 
    private String email;
      

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
}
