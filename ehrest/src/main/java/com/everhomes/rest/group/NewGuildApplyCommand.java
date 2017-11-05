// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespace_id: namespace_id</li>
 *     <li>group_id: group_id</li>
 *     <li>name: name</li>
 *     <li>phone: phone</li>
 *     <li>email: email</li>
 *     <li>organization_name: organization_name</li>
 *     <li>registered_capital: registered_capital</li>
 *     <li>industryType: industryType</li>
 * </ul>
 */
public class NewGuildApplyCommand {

	private Integer namespace_id;
	private Long group_id;
	private String name;
	private String phone;
	private String email;
	private String organization_name;
	private String registered_capital;
	private String industryType;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
