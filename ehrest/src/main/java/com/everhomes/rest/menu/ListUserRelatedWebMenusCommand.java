// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.rest.common.PortalCommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>menuId：菜单id，只查询menuId下面的子菜单，不填写，默认全部</li>
 * <li>levelStart：第几级开始，左邻运营后台一般都是从1级开始，物业公司管理后台从2级开始，不填写，默认从1级开始</li>
 * </ul>
 */
public class ListUserRelatedWebMenusCommand extends PortalCommand{

	private Long menuId;

	private Integer levelStart;

	private Long userId;

	private Long currentOrgId;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
