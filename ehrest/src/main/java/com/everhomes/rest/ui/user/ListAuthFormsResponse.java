// @formatter:off
package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>sourceDto : 表单列表，现在 参考 {@link com.everhomes.rest.ui.user.FormSourceDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月6日
 */
public class ListAuthFormsResponse {
	
	@ItemType(FormSourceDTO.class)
	List<FormSourceDTO> sourceDto;

	public List<FormSourceDTO> getSourceDto() {
		return sourceDto;
	}

	public void setSourceDto(List<FormSourceDTO> sourceDto) {
		this.sourceDto = sourceDto;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
