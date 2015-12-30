package com.everhomes.app;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.app.TrustedAppCommand;
import com.everhomes.rest.app.VerifyAppUrlBindingCommand;

@RestController
@RequestMapping("/appkey")
public class AppController extends ControllerBase {
    
    // TODO, we need to maintain the trust list in database
    private static Set<String> s_trustedApps = new HashSet<>();
    
    static {
        s_trustedApps.add(AppConstants.APPKEY_BORDER);
        s_trustedApps.add(AppConstants.APPKEY_BIZ);
        s_trustedApps.add(AppConstants.APPKEY_APP);
    }
    
    @RequestMapping("isTrustedApp")
    @RestReturn(value=String.class)
    public RestResponse isTrustedApp(@Valid TrustedAppCommand cmd) {
        if(s_trustedApps.contains(cmd.getAppKey()))
            return new RestResponse("YES");
        
        return new RestResponse("NO");
    }
    
    @RequestMapping("isValidAppUrlBinding")
    @RestReturn(value=String.class)
    public RestResponse isValidAppUrlBinding(@Valid VerifyAppUrlBindingCommand cmd) {
        
        // TODO verify signature
        return new RestResponse("YES");
    }
}
