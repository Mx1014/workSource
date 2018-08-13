// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：内网服务器id</li>
 * <li>name：内网服务器名称</li>
 * <li>uuid:配对码,用mac地址</li>
 * </ul>
 */
public class UpdateLocalServersCommand {
	private Long id;
	private String name;
	private String uuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
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
