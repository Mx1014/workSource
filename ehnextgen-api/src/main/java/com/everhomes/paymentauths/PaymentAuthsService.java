// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.rest.paymentauths.*;


import java.util.List;

public interface PaymentAuthsService {

	CheckUserAuthsResponse checkUserAuths(CheckUserAuthsCommand cmd);

	List<EnterprisePaymentAuthsDTO> listEnterprisePaymentAuths(ListEnterprisePaymentAuthsCommand cmd);

	void updateEnterprisePaymentAuths(UpdateEnterprisePaymentAuthsCommand cmd);

}