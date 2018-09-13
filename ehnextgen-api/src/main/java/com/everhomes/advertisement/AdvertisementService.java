package com.everhomes.advertisement;

import com.everhomes.rest.advertisement.AdvertisementDetailDTO;
import com.everhomes.rest.advertisement.ChangeAdvertisementOrderCommand;
import com.everhomes.rest.advertisement.CreateAdvertisementCommand;
import com.everhomes.rest.advertisement.DeleteAdvertisementCommand;
import com.everhomes.rest.advertisement.FindAdvertisementCommand;
import com.everhomes.rest.advertisement.ListAdvertisementsCommand;
import com.everhomes.rest.advertisement.ListAdvertisementsResponse;
import com.everhomes.rest.advertisement.UpdateAdvertisementCommand;

public interface AdvertisementService {

	void deleteAdvertisement(DeleteAdvertisementCommand cmd);

	void createAdvertisement(CreateAdvertisementCommand cmd);

	void updateAdvertisement(UpdateAdvertisementCommand cmd);

	ListAdvertisementsResponse listAdvertisements(ListAdvertisementsCommand cmd);

	void changeAdvertisementOrder(ChangeAdvertisementOrderCommand cmd);

	AdvertisementDetailDTO findAdvertisement(FindAdvertisementCommand cmd);

}
