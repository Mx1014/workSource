// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：内网服务器id</li>
 * <li>name：内网服务器名称</li>
 * </ul>
 */
public class UpdateLocalServersCommand {
	private Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
