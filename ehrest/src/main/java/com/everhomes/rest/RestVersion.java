package com.everhomes.rest;

import com.everhomes.util.Version;

/**
 * REST API versioning control
 * 
 * @author Kelven Yang
 *
 */
public class RestVersion {
    public static Version current() {
        
        // TODO retrieve version tag info from package meta
        return new Version();
    }
}
