// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinUserPrinterMappingProvider {

	void createSiyinUserPrinterMapping(SiyinUserPrinterMapping siyinUserPrinterMapping);

	void updateSiyinUserPrinterMapping(SiyinUserPrinterMapping siyinUserPrinterMapping);

	SiyinUserPrinterMapping findSiyinUserPrinterMappingById(Long id);
	
	SiyinUserPrinterMapping findSiyinUserPrinterMappingByUserAndPrinter(Long userId, String readerName);
	
	List<SiyinUserPrinterMapping> listSiyinUserPrinterMappingByUserId(Long userId, Integer namespaceId);

	List<SiyinUserPrinterMapping> listSiyinUserPrinterMapping();

}