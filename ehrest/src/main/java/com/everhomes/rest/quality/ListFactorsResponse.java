package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>factorsByGroup: 参考com.everhomes.rest.quality.FactorsDTO</li>
 *  <li>pageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListFactorsResponse {

	@ItemType(FactorsDTO.class)
	private List<FactorsDTO> factorsdto;
	
	private Long nextPageAnchor;
	
	public ListFactorsResponse(Long nextPageAnchor, List<FactorsDTO> factorsdto) {
        this.nextPageAnchor = nextPageAnchor;
        this.factorsdto = factorsdto;
    }

	public List<FactorsDTO> getFactorsdto() {
		return factorsdto;
	}

	public void setFactorsdto(List<FactorsDTO> factorsdto) {
		this.factorsdto = factorsdto;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
