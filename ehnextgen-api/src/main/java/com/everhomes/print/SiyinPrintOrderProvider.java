// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintOrderProvider {

	void createSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder);

	void updateSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder);

	SiyinPrintOrder findSiyinPrintOrderById(Long id);

	List<SiyinPrintOrder> listSiyinPrintOrder();

}