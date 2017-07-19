// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>expressSendModeDTO : 封装类型列表, 参考 {@link com.everhomes.rest.express.ExpressPackageTypeDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ListExpressPackageTypesResponse {
	@ItemType(ExpressPackageTypeDTO.class)
	private List<ExpressPackageTypeDTO> expressPackageTypeDTO;

	public List<ExpressPackageTypeDTO> getExpressPackageTypeDTO() {
		return expressPackageTypeDTO;
	}

	public void setExpressPackageTypeDTO(List<ExpressPackageTypeDTO> expressPackageTypeDTO) {
		this.expressPackageTypeDTO = expressPackageTypeDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
