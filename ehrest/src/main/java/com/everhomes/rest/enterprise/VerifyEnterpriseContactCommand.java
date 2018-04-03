package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>verifyToken: 邮箱认证的webToken 数据参考:{@link com.everhomes.rest.enterprise.VerifyEnterpriseContactDTO}</li> 
 * </ul>
 * @author wh
 *
 */
public class VerifyEnterpriseContactCommand {
    private  String verifyToken;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getVerifyToken() {
		return verifyToken;
	}

	public void setVerifyToken(String verifyToken) {
		this.verifyToken = verifyToken;
	}

}
