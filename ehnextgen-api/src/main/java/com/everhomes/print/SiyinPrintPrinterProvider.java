// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintPrinterProvider {

	void createSiyinPrintPrinter(SiyinPrintPrinter siyinPrintPrinter);

	void updateSiyinPrintPrinter(SiyinPrintPrinter siyinPrintPrinter);

	SiyinPrintPrinter findSiyinPrintPrinterById(Long id);

	List<SiyinPrintPrinter> listSiyinPrintPrinter();

}