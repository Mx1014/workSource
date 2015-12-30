// @formatter:off
package com.everhomes.rest.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.util.Name;
import com.google.gson.Gson;

/**
 * 
 * Defines message(Protocol Data Unit) frames for inter-node communication
 * 
 * @author Kelven Yang
 *
 */
public class PduFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(PduFrame.class);
    
    private String version;
    private String name;
    private Long requestId;
    private Long appId;
    private String payload;
    
    protected static Gson gson = new Gson();
    
    public PduFrame() {
    }

    public String getVersion() {
        return version;
    }

    public PduFrame setVersion(String version) {
        this.version = version;
        return this;
    }
    
    public Long getAppId() {
        return this.appId;
    }
    
    public PduFrame setAppId(Long appId) {
        this.appId = appId;
        return this;
    }

    public String getName() {
        return name;
    }

    public PduFrame setName(String name) {
        this.name = name;
        return this;
    }

    public Long getRequestId() {
        return requestId;
    }

    public PduFrame setRequestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getEncodedPayload() {
        return payload;
    }
    
    public PduFrame setEncodedPayload(String encodedPayload) {
        this.payload = encodedPayload;
        return this;
    }
    
    public <T> T getPayload(Class<T> clz) {
        return gson.fromJson(this.payload, clz);
    }

    public PduFrame setPayload(Object payload) {
        assert(payload != null);
        
        Name payloadName = payload.getClass().getAnnotation(Name.class);
        if(payloadName != null) {
            this.name = payloadName.value();
        } else {
            LOGGER.warn("Payload is not annotated with Name. payload class: " + payload.getClass().getName());
        }
        
        this.payload = gson.toJson(payload);
        return this;
    }
    
    public PduFrame setPayLoadForString(String payload) {
        this.payload = payload;
        return this;
    }
    
    public static PduFrame fromJson(String json) {
        return gson.fromJson(json, PduFrame.class);
    }
    
    public String toJson() {
        return gson.toJson(this);
    }
}
