package com.everhomes.rest.officecubicle;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.order.PayMethodDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>preDTO: {@link com.everhomes.rest.order.PreOrderDTO}</li>
 * </ul>
 */
public class CreateOfficeCubicleOrderResponse {

    private PreOrderDTO preDTO;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public PreOrderDTO getPreDTO() {
		return preDTO;
	}

	public void setPreDTO(PreOrderDTO preDTO) {
		this.preDTO = preDTO;
	}


	
}
