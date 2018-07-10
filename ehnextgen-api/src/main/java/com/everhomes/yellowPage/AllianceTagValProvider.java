package com.everhomes.yellowPage;

import java.util.List;

public interface AllianceTagValProvider {

	void deleteTagValByOwnerId(Long ownerId);

	void createTagVal(AllianceTagVal tagVal);

	List<AllianceTagVal> listTagValsByOwnerId(Long ownerId);
	
}
