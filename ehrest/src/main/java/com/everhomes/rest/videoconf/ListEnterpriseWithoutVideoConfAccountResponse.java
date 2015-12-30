package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterprises: enterprises信息，参考{@link com.everhomes.rest.videoconf.EnterpriseWithoutVideoConfAccountDTO}</li>
 * </ul>
 */
public class ListEnterpriseWithoutVideoConfAccountResponse {
	
	@ItemType(EnterpriseWithoutVideoConfAccountDTO.class)
	private List<EnterpriseWithoutVideoConfAccountDTO> enterprises;
	
	public List<EnterpriseWithoutVideoConfAccountDTO> getEnterprises() {
		return enterprises;
	}

	public void setEnterprises(
			List<EnterpriseWithoutVideoConfAccountDTO> enterprises) {
		this.enterprises = enterprises;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
