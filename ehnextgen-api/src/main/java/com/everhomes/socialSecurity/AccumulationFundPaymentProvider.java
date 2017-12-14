// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface AccumulationFundPaymentProvider {

	void createAccumulationFundPayment(AccumulationFundPayment accumulationFundPayment);

	void updateAccumulationFundPayment(AccumulationFundPayment accumulationFundPayment);

	AccumulationFundPayment findAccumulationFundPaymentById(Long id);

	List<AccumulationFundPayment> listAccumulationFundPayment();

}