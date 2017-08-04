// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintSettingProvider {

	SiyinPrintSetting findSiyinPrintSettingById(Long id);
	
	List<SiyinPrintSetting> listSiyinPrintSettingByOwner(String ownerType,Long ownerId);

	List<SiyinPrintSetting> listSiyinPrintSetting();

	void createSiyinPrintSettings(List<SiyinPrintSetting> list,String ownerType, Long ownerId);

}