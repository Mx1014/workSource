// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>listDoorAccess:组内门禁{@link com.everhomes.rest.aclink.DoorAccessDTO}</li>
 * </ul>
 *
 */
public class ListDoorAccessByGroupIdResponse {
	private List<DoorAccessDTO> listDoorAccess;
	
	public List<DoorAccessDTO> getListDoorAccess() {
		return listDoorAccess;
	}

	public void setListDoorAccess(List<DoorAccessDTO> listDoorAccess) {
		this.listDoorAccess = listDoorAccess;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
