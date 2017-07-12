// @formatter:off
package  com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;


/**
 * <ul> 注册流程，绑定已有用户到企业：根据已有用户ID创建企业用户，从而成为此企业的一个成员
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>email: 邮箱地址-通过这个验证用户是否在本小区的某公司</li>
 * <li>organizationId: 加入公司的id</li>
 * </ul>
 * @author wh
 *
 */
public class ApplyForEnterpriseContactByEmailCommand {
    //Use current user as userId
    //@NotNull

//	private String sceneToken; 
    private String email;
    private Long organizationId;
     
//    public String getSceneToken() {
//		return sceneToken;
//	}
//
//	public void setSceneToken(String sceneToken) {
//		this.sceneToken = sceneToken;
//	}

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	} 
}
