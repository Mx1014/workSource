package com.everhomes.rest.advertisement;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 	<li>nextPageAnchor: 分页锚点</li>
 *  <li>advertisements: 广告信息，参考{@link com.everhomes.rest.advertisement.AdvertisementDTO}</li>
 * </ul>
 */
public class ListAdvertisementsResponse {
	
	private Long nextPageAnchor;
    @ItemType(AdvertisementDTO.class)
    private List<AdvertisementDTO> advertisements;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AdvertisementDTO> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<AdvertisementDTO> advertisements) {
		this.advertisements = advertisements;
	}
    
    

}
