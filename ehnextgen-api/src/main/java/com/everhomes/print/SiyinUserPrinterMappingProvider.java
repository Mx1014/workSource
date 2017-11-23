// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinUserPrinterMappingProvider {

	void createSiyinUserPrinterMapping(SiyinUserPrinterMapping siyinUserPrinterMapping);

	void updateSiyinUserPrinterMapping(SiyinUserPrinterMapping siyinUserPrinterMapping);

	SiyinUserPrinterMapping findSiyinUserPrinterMappingById(Long id);

	List<SiyinUserPrinterMapping> listSiyinUserPrinterMapping();

}