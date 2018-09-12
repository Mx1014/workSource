package com.everhomes.rest.advertisement;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.expansion.BuildingForRentDTO;

public class ListAdvertisementsResponse {
	
	private Long nextPageAnchor;
    
    @ItemType(AdvertisementDTO.class)
    private List<AdvertisementDTO> advertisements;

}
