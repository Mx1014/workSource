// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintPrinterProvider {

	void createSiyinPrintPrinter(SiyinPrintPrinter siyinPrintPrinter);

	void updateSiyinPrintPrinter(SiyinPrintPrinter siyinPrintPrinter);

	SiyinPrintPrinter findSiyinPrintPrinterById(Long id);
	
	SiyinPrintPrinter findSiyinPrintPrinterByReadName(String readerName);

	List<SiyinPrintPrinter> listSiyinPrintPrinter(Integer namespaceId);

	List<SiyinPrintPrinter> findSiyinPrintPrinterByOwnerId(Long ownerId);

	List<SiyinPrintPrinter> findSiyinPrintPrinterByNamespaceId(Integer namespaceId);

}