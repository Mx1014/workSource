// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间，新形势下为了防止有人无脑同步所有域空间的数据，现在域空间必填</li>
 *     <li>location: launchPadLayout 下面所对应的item 的location</li>
 *     <li>name: launchPadLayout 的名称</li>
 * </ul>
 */
public class SyncLaunchPadDataCommand {


	@NotNull
	private Integer namespaceId;

	private String location;

	private String name;

	public SyncLaunchPadDataCommand() {

	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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
