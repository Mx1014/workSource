package com.everhomes.dbsync;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.HandlerMapping;

@Controller
@RequestMapping("/dbsync")
public class SyncDatabaseController {
    @Autowired
    NashornProcessService jsService;
    
    @RequestMapping("/a/{id}/**")
    @ResponseBody
    public DeferredResult<String> foo(@PathVariable String id, Model model, HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        NashornHttpObject obj = new NashornHttpObject(deferredResult);
        obj.setUrl(restOfTheUrl);
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
