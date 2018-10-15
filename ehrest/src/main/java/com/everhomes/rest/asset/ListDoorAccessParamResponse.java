package com.everhomes.rest.asset;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> doorAccessParamDTOs: 门禁设备列表，参考{@link com.everhomes.rest.asset.DoorAccessParamDTO}</li>
 * </ul>
 */
public class ListDoorAccessParamResponse {

	@ItemType(DoorAccessParamDTO.class)
	private List<DoorAccessParamDTO> doorAccessParamDTOs;

	public List<DoorAccessParamDTO> getDoorAccessParamDTOs() {
		return doorAccessParamDTOs;
	}

	public void setDoorAccessParamDTOs(List<DoorAccessParamDTO> doorAccessParamDTOs) {
		this.doorAccessParamDTOs = doorAccessParamDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
