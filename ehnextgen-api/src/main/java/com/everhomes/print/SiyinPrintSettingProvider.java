// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintSettingProvider {

	void createSiyinPrintSetting(SiyinPrintSetting siyinPrintSetting);

	void updateSiyinPrintSetting(SiyinPrintSetting siyinPrintSetting);

	SiyinPrintSetting findSiyinPrintSettingById(Long id);
	
	List<SiyinPrintSetting> listSiyinPrintSettingByOwner(String ownerType,Long ownerId);

	List<SiyinPrintSetting> listSiyinPrintSetting();

}