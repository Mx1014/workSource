package com.everhomes.pay.oauth2;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.order.PayProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PayOauth2BaseController {
    @Autowired
    protected AccessTokenManager tokenManager;

    @Autowired
    protected ConfigurationProvider configProvider;

    @Autowired
    protected PayProvider payProvider;
}
