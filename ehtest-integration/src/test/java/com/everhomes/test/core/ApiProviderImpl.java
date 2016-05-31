// @formatter:off
package com.everhomes.test.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.everhomes.rest.StringRestResponse;

@Component
public class ApiProviderImpl implements ApiProvider {
    @Autowired
    private HttpClientService httpClientService;
    
    @Value("${admin.username:root}")
    private String adminUserName;
    
    /** 服务器地址 */
    @Value("${admin.password:123456}")
    private String adminPassword;
    
    public void syncSequence() {
        UserContext context = new UserContext(httpClientService);
        
        boolean rootLogin = false;
        
        try {
            rootLogin = context.logon(0, adminUserName, adminPassword);
            if(!rootLogin) {
                throw new IllegalStateException("Failed to login before synchronizing sequence, user=" + adminUserName);
            }
            
            String commandRelativeUri = "/admin/syncSequence";
            StringRestResponse response = httpClientService.restPost(commandRelativeUri, null, StringRestResponse.class, context);
            if(!httpClientService.isReponseSuccess(response)) {
                throw new IllegalStateException("Failed to sync the sequence, context=" + context);
            }
        } finally {
            if(rootLogin) {
                context.logoff();
            }
        }
        
    }
}
