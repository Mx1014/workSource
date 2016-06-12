package com.everhomes.hotTag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.hotTag.ListHotTagCommand;
import com.everhomes.rest.hotTag.SearchTagCommand;
import com.everhomes.rest.hotTag.SetHotTagCommand;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.rest.hotTag.DeleteHotTagCommand;

@RestDoc(value = "HotTag Controller", site = "core")
@RestController
@RequestMapping("/hotTag")
public class HotTagController extends ControllerBase {

	/**
     * <b>URL: /hotTag/listHotTag</b>
     * <p>列热门标签</p>
     */
    @RequestMapping("listHotTag")
    @RestReturn(value=TagDTO.class, collection = true)
    public RestResponse listHotTag(ListHotTagCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /hotTag/setHotTag</b>
     * <p>设置热门标签</p>
     */
    @RequestMapping("setHotTag")
    @RestReturn(value=TagDTO.class)
    public RestResponse setHotTag(SetHotTagCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /hotTag/deleteHotTag</b>
     * <p>删除热门标签</p>
     */
    @RequestMapping("deleteHotTag")
    @RestReturn(value=String.class)
    public RestResponse deleteHotTag(DeleteHotTagCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /hotTag/searchTag</b>
     * <p>搜素自定义标签</p>
     */
    @RequestMapping("searchTag")
    @RestReturn(value=TagDTO.class, collection = true)
    public RestResponse searchTag(SearchTagCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
