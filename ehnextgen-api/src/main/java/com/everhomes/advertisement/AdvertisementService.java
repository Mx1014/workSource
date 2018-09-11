package com.everhomes.advertisement;

import com.everhomes.rest.advertisement.CreateAdvertisementCommand;
import com.everhomes.rest.advertisement.DeleteAdvertisementCommand;

public interface AdvertisementService {

	void deleteAdvertisement(DeleteAdvertisementCommand cmd);

	void createAdvertisement(CreateAdvertisementCommand cmd);

}
