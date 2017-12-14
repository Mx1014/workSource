// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface AccumulationFundBaseProvider {

	void createAccumulationFundBase(AccumulationFundBase accumulationFundBase);

	void updateAccumulationFundBase(AccumulationFundBase accumulationFundBase);

	AccumulationFundBase findAccumulationFundBaseById(Long id);

	List<AccumulationFundBase> listAccumulationFundBase();

}