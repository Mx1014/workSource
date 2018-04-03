// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType：范围类型，固定EhOrganizations，如果是左邻运营后台的域名可以定义一个类型  参考{@link com.everhomes.rest.common.EntityType}</li>
 * <li>ownerId：范围具体Id，域名对应的机构id，后面需要讨论是否直接通过域名来获取当前公司</li>
 * <li>menuId：菜单id，只查询menuId下面的子菜单，不填写，默认全部</li>
 * <li>levelStart：第几级开始，左邻运营后台一般都是从1级开始，物业公司管理后台从2级开始，不填写，默认从1级开始</li>
 * </ul>
 */
public class TreeWebMenusCommand {

	private String ownerType;

	private Long ownerId;

	private Long menuId;

	private Integer levelStart;

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Integer getLevelStart() {
		return levelStart;
	}

	public void setLevelStart(Integer levelStart) {
		this.levelStart = levelStart;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
