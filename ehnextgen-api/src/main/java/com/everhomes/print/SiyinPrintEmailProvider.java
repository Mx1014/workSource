// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintEmailProvider {

	void createSiyinPrintEmail(SiyinPrintEmail siyinPrintEmail);

	void updateSiyinPrintEmail(SiyinPrintEmail siyinPrintEmail);

	SiyinPrintEmail findSiyinPrintEmailById(Long id);

	List<SiyinPrintEmail> listSiyinPrintEmail();

	SiyinPrintEmail findSiyinPrintEmailByUserId(Long userId);

}