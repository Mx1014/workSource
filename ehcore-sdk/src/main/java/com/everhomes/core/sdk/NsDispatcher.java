package com.everhomes.core.sdk;

import com.everhomes.tachikoma.commons.sdk.SdkClient;

import java.util.function.Function;

public class NsDispatcher {

    private final static int NS_STD = 2;

    private final SdkClient coreClient = new SdkClient(new CoreSdkSettings());
    private final SdkClient stdClient = new SdkClient(new StdSdkSettings());

    protected <R> R dispatcher(Integer namespaceId, Function<SdkClient, R> function) {
        if (namespaceId != null && namespaceId == NS_STD) {
            return function.apply(stdClient);
        } else {
            return function.apply(coreClient);
        }
    }
}
