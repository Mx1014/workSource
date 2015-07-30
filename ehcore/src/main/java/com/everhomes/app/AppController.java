package com.everhomes.app;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/app")
public class AppController extends ControllerBase {
    
    // TODO, we need to maintain the trust list in database
    private static Set<String> s_trustedApps = new HashSet<>();
    
    static {
        s_trustedApps.add(AppConstants.APPKEY_BORDER);
    }
    
    @RequestMapping("isTrustedApp")
    @RestReturn(value=String.class)
    public RestResponse isTrustedApp(@RequestParam(value="appKey", required=true) String appKey) {
        if(s_trustedApps.contains(appKey))
            return new RestResponse("YES");
        
        return new RestResponse("NO");
    }
}
