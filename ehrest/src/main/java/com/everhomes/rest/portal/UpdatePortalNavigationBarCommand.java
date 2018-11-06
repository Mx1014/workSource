// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.rest.launchpadbase.IndexType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 门户导航栏的id</li>
 *     <li>name: 主页签名称</li>
 *     <li>description: 描述</li>
 *     <li>type: 主页签类型，参考{@link IndexType}</li>
 *     <li>configJson: 配置信息</li>
 *     <li>iconUri: icon 图片</li>
 *     <li>selectedIconUri: icon选中图片</li>
 * </ul>
 */
public class UpdatePortalNavigationBarCommand {

	private Long id;

	private String name;

	private String description;

	private Byte type;

	private String configJson;

	private String iconUri;

	private String selectedIconUri;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getConfigJson() {
		return configJson;
	}

	public void setConfigJson(String configJson) {
		this.configJson = configJson;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getSelectedIconUri() {
		return selectedIconUri;
	}

	public void setSelectedIconUri(String selectedIconUri) {
		this.selectedIconUri = selectedIconUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
