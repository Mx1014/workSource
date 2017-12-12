// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>sendTypeDTO : 寄件类型列表, 参考 {@link com.everhomes.rest.express.ExpressSendTypeDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ListExpressSendTypesResponse {
	@ItemType(ExpressSendTypeDTO.class)
	private List<ExpressSendTypeDTO> sendTypeDTO;
	
	public ListExpressSendTypesResponse() {
		super();
	}

	public ListExpressSendTypesResponse(List<ExpressSendTypeDTO> sendTypeDTO) {
		super();
		this.sendTypeDTO = sendTypeDTO;
	}

	public List<ExpressSendTypeDTO> getSendTypeDTO() {
		return sendTypeDTO;
	}

	public void setSendTypeDTO(List<ExpressSendTypeDTO> sendTypeDTO) {
		this.sendTypeDTO = sendTypeDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
