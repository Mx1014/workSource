// @formatter:off
package com.everhomes.paymentAuths;

import com.everhomes.rest.launchpad.ListAllAppsResponse;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.paymentAuths.CheckUserAuthsCommand;
import com.everhomes.rest.paymentAuths.CheckUserAuthsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.*;

import java.util.List;

public interface PaymentAuthsService {

	CheckUserAuthsResponse checkUserAuths(CheckUserAuthsCommand cmd);

}