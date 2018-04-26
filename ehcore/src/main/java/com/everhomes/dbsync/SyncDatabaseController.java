package com.everhomes.dbsync;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.HandlerMapping;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.StringHelper;

@Controller
@RequestMapping("/dbsync")
public class SyncDatabaseController {
    @Autowired
    NashornProcessService jsService;
    
    @Autowired
    private SyncDatabaseService syncDatabaseService;
    
    @Autowired
    private SyncAppProvider syncAppProvider;
    
    @Autowired
    private SyncMappingProvider syncMappingProvider;
    
    @RequestMapping("/query/**")
    @ResponseBody
    public DeferredResult<RestResponse> doQuery(HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
        
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> bodyMap = new HashMap<String, String>();
        for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
        		if(!entry.getKey().equals("signature") && !entry.getKey().equals("appKey")) {
        			bodyMap.put(entry.getKey(), StringUtils.join(entry.getValue(), ','));
        		}
        	}
        
        DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>();
        NashornHttpObject obj = new NashornHttpObject(deferredResult);
        obj.setUrl(restOfTheUrl);
        
        String query = "/dbsync/query/";
        String subQuery = restOfTheUrl.substring(query.length());
        String[] ss = subQuery.split("/");
        if(ss.length < 3) {
        	res.setErrorDescription("request error");
        	deferredResult.setErrorResult(res);
        	return deferredResult;
        }
        
        SyncApp app = syncAppProvider.findSyncAppByName(ss[0]);
        if(app == null) {
        	res.setErrorDescription("app not found");
        	deferredResult.setErrorResult(res);
        	return deferredResult;
        }
        
        SyncMapping map = syncMappingProvider.findSyncMappingByName(ss[1].trim());
        if(map == null) {
        	res.setErrorDescription("mapping not found");
        	deferredResult.setErrorResult(res);
        	return deferredResult;
        }
        
        obj.setAppName(app.getName());
        obj.setMapName(map.getName());
        obj.setQuery(ss[2]);
        obj.setBody(StringHelper.toJsonString(bodyMap));
        
        jsService.push(obj);
        
        return deferredResult;
    }
    
//    @RequestMapping("/abc")
//    public RestResponse foo2(@Valid String cmd, HttpServletRequest request) {
//        String restOfTheUrl = (String) request.getAttribute(
//                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        RestResponse response = new RestResponse(restOfTheUrl);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
}
