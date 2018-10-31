package com.everhomes.core.sdk;

import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
import com.everhomes.rest.domain.GetDomainInfoRestResponse;
import com.everhomes.tachikoma.commons.sdk.SdkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NsDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(NsDispatcher.class);

    private final static int NS_DEFAULT = Integer.MIN_VALUE;
    private final static int NS_STD = 2;

    private final Map<Integer, SdkClient> sdkClientMap = new HashMap<>();
    {
        sdkClientMap.put(NS_STD, new SdkClient(new StdSdkSettings()));
        sdkClientMap.put(NS_DEFAULT, new SdkClient(new CoreSdkSettings()));
    }

    protected <R> R dispatcher(@NotNull Integer namespaceId, @NotNull Function<SdkClient, R> function) {
        return dispatcher(null, namespaceId, function);
    }

    protected <R> R dispatcher(String host, Integer namespaceId, Function<SdkClient, R> function) {
        if (host == null) {
            SdkClient sdkClient = sdkClientMap.get(namespaceId);
            if (sdkClient == null) {
                sdkClient = sdkClientMap.get(NS_DEFAULT);
            }
            return function.apply(sdkClient);
        }

        GetDomainInfoCommand cmd = new GetDomainInfoCommand();
        cmd.setDomain(host);

        SdkClient defaultClient = sdkClientMap.get(NS_DEFAULT);
        DomainDTO domain = getDomain(defaultClient, host);
        if (domain != null && host.equalsIgnoreCase(domain.getDomain())) {
            return function.apply(defaultClient);
        }

        for (SdkClient sdkClient : sdkClientMap.values()) {
            if (sdkClient != defaultClient) {
                DomainDTO dto = getDomain(sdkClient, host);
                if (dto != null && host.equalsIgnoreCase(dto.getDomain())) {
                    return function.apply(sdkClient);
                }
            }
        }

        LOGGER.warn("Dispatcher failed to dispatcher, host={}, ns={}, func={}", host, namespaceId, function);
        return null;
    }

    private DomainDTO getDomain(SdkClient sdkClient, String host) {
        GetDomainInfoCommand cmd = new GetDomainInfoCommand();
        cmd.setDomain(host);

        GetDomainInfoRestResponse response;
        try {
            response = sdkClient.restCall("post", "/evh/domain/getDomainInfo", cmd, GetDomainInfoRestResponse.class);
            return response.getResponse();
        } catch (Exception e) {
            LOGGER.warn("Dispatcher getDomainInfo error, host={}", host);
        }
        return null;
    }
}
