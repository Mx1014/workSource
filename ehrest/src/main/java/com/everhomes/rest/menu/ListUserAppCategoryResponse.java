// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 入口分类 + 菜单, 参考{@link AppCategoryDTO}</li>
 * </ul>
 */
public class ListUserAppCategoryResponse {


	private List<AppCategoryDTO> dtos;

	public List<AppCategoryDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<AppCategoryDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
