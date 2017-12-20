package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalSiteRulesDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ordinateTime：纵坐标时间</li>
 * <li>cellDTOS：单元格列表{@link com.everhomes.rest.reserve.ReserveCellDTO}</li>
 * </ul>
 */
public class ReserveCellOrdinateDTO {
	private Long ordinateTime;
	@ItemType(ReserveCellDTO.class)
	private List<ReserveCellDTO> cellDTOS;

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getOrdinateTime() {
		return ordinateTime;
	}

	public void setOrdinateTime(Long ordinateTime) {
		this.ordinateTime = ordinateTime;
	}

	public List<ReserveCellDTO> getCellDTOS() {
		return cellDTOS;
	}

	public void setCellDTOS(List<ReserveCellDTO> cellDTOS) {
		this.cellDTOS = cellDTOS;
	}
}
