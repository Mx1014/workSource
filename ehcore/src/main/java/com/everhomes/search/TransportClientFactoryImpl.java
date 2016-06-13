package com.everhomes.search;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransportClientFactoryImpl implements TransportClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportClientFactoryImpl.class);
    
    @Value("${elastic.nodes.hosts}")
    private String nodeHosts;
    
    @Value("${elastic.nodes.ports}")
    private String nodePorts;
    
    @Value("${cluster.name:elasticsearch}")
    private String clusterName;
    
    private Client client = null;
    
    @PostConstruct
    public void setup() {
        try {
            client = setupClient();    
        }
        catch(Exception e) {
            LOGGER.error("Cannot setup search client. " + e.getMessage());
        }
        
    }
    
    private InetSocketTransportAddress toAddress(String address) {
        if (address == null) return null;
        
        String[] splitted = address.split(":");
        int port = 9300;
        if (splitted.length > 1) {
            port = Integer.parseInt(splitted[1]);
        }
        
        return new InetSocketTransportAddress(splitted[0], port);
    }
    
    private Client setupClient() {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName)
                .build();
        
        TransportClient proxyClient = new TransportClient(settings);
        
        String[] hosts = nodeHosts.split(",");
        String[] ports = nodePorts.split(",");
        if(hosts.length != ports.length) {
            LOGGER.error("configuration for elastic search hosts is error");
            return null;
        }
        
        for(int i = 0; i < hosts.length; i++) {
            String addr = hosts[i] + ":" + ports[i];
            proxyClient.addTransportAddress(toAddress(addr));
        }
        
        return proxyClient;
        
    }

    @Override
    public Client getClient() throws Exception {
        if(null == client) {
            throw new Exception("Elastic search initialize error");
        }
        return client;
    }

}
