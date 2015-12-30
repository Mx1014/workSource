package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>reserveConf: reserveConf信息，参考{@link com.everhomes.rest.videoconf.ConfReservationsDTO}</li>
 * </ul>
 */
public class ListReservationConfResponse {

	private Long nextPageAnchor;
	
	@ItemType(ConfReservationsDTO.class)
    private List<ConfReservationsDTO> reserveConf;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ConfReservationsDTO> getReserveConf() {
		return reserveConf;
	}

	public void setReserveConf(List<ConfReservationsDTO> reserveConf) {
		this.reserveConf = reserveConf;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
