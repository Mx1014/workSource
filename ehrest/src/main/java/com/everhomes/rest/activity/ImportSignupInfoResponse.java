//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 异常信息dtos {@link com.everhomes.rest.activity.ImportSignupErrorDTO}</li>
 * </ul>
 */
public class ImportSignupInfoResponse {

	@ItemType(ImportSignupErrorDTO.class)
	private List<ImportSignupErrorDTO> dtos;

	public List<ImportSignupErrorDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ImportSignupErrorDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
