// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>expressSendModeDTO : 寄件方式列表, 参考 {@link com.everhomes.rest.express.ExpressSendModeDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ListExpressSendModesResponse {
	@ItemType(ExpressSendModeDTO.class)
	private List<ExpressSendModeDTO> expressSendModeDTO;
	
	public List<ExpressSendModeDTO> getExpressSendModeDTO() {
		return expressSendModeDTO;
	}

	public void setExpressSendModeDTO(List<ExpressSendModeDTO> expressSendModeDTO) {
		this.expressSendModeDTO = expressSendModeDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
