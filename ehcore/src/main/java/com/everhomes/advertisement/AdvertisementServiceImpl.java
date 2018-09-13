package com.everhomes.advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.advertisement.AdvertisementDetailDTO;
import com.everhomes.rest.advertisement.ChangeAdvertisementOrderCommand;
import com.everhomes.rest.advertisement.CreateAdvertisementCommand;
import com.everhomes.rest.advertisement.DeleteAdvertisementCommand;
import com.everhomes.rest.advertisement.FindAdvertisementCommand;
import com.everhomes.rest.advertisement.ListAdvertisementsCommand;
import com.everhomes.rest.advertisement.ListAdvertisementsResponse;
import com.everhomes.rest.advertisement.UpdateAdvertisementCommand;

@Component
public class AdvertisementServiceImpl implements AdvertisementService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

	@Override
	public void deleteAdvertisement(DeleteAdvertisementCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createAdvertisement(CreateAdvertisementCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAdvertisement(UpdateAdvertisementCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListAdvertisementsResponse listAdvertisements(ListAdvertisementsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeAdvertisementOrder(ChangeAdvertisementOrderCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AdvertisementDetailDTO findAdvertisement(FindAdvertisementCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
