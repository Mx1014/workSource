package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**

 *         <ul>
 *         <li>namespaceId:域空间ID</li>
 *         <li>phone:手机号</li>
 *         </ul>
 */
public class findUserByPhoneCommand {
    private Integer namespaceId;
    private String phone ;
    
    

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
