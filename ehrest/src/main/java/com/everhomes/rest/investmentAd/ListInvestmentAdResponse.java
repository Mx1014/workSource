package com.everhomes.rest.investmentAd;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 	<li>nextPageAnchor: 分页锚点</li>
 *  <li>advertisements: 广告信息，参考{@link com.everhomes.rest.investmentAd.InvestmentAdDTO}</li>
 * </ul>
 */
public class ListInvestmentAdResponse {
	
	private Long nextPageAnchor;
    @ItemType(InvestmentAdDTO.class)
    private List<InvestmentAdDTO> advertisements;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<InvestmentAdDTO> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<InvestmentAdDTO> advertisements) {
		this.advertisements = advertisements;
	}
    
    

}
