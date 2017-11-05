// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
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
