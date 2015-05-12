package com.everhomes.search;

import org.elasticsearch.client.Client;

public interface TransportClientFactory {
    Client getClient() throws Exception;
}
