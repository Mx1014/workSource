// @formatter:off
package com.everhomes.print;

import java.util.List;

public interface SiyinPrintBusinessPayeeAccountProvider {

	void createSiyinPrintBusinessPayeeAccount(SiyinPrintBusinessPayeeAccount siyinPrintBusinessPayeeAccount);

	void updateSiyinPrintBusinessPayeeAccount(SiyinPrintBusinessPayeeAccount siyinPrintBusinessPayeeAccount);

	SiyinPrintBusinessPayeeAccount findSiyinPrintBusinessPayeeAccountById(Long id);

	List<SiyinPrintBusinessPayeeAccount> listSiyinPrintBusinessPayeeAccount();

}