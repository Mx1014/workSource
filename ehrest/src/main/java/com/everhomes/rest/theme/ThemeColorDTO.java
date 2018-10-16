// @formatter:off
package com.everhomes.rest.theme;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>value: 色值</li>
 * </ul>
 */
public class ThemeColorDTO {
	private Long id;
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
