// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 用户email的id</li>
 * <li>email : 用户email</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class GetPrintUserEmailResponse {
	private Long id;
	private String email;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
}
