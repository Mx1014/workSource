// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.rest.launchpad.ListAllAppsResponse;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.paymentauths.CheckUserAuthsCommand;
import com.everhomes.rest.paymentauths.CheckUserAuthsResponse;
import com.everhomes.rest.paymentauths.ListEnterprisePaymentAuthsCommand;
import com.everhomes.rest.paymentauths.ListEnterprisePaymentAuthsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.*;

import java.util.List;

public interface PaymentAuthsService {

	CheckUserAuthsResponse checkUserAuths(CheckUserAuthsCommand cmd);

	ListEnterprisePaymentAuthsResponse listEnterprisePaymentAuths(ListEnterprisePaymentAuthsCommand cmd);

}