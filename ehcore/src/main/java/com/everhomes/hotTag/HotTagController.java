package com.everhomes.hotTag;

import java.util.List;

import com.everhomes.rest.hotTag.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.search.HotTagSearcher;

@RestDoc(value = "HotTag Controller", site = "core")
@RestController
@RequestMapping("/hotTag")
public class HotTagController extends ControllerBase {
	
	@Autowired
	private HotTagService hotTagService;
	
	@Autowired
	private HotTagSearcher hotTagSearcher;

	/**
     * <b>URL: /hotTag/listHotTag</b>
     * <p>列热门标签</p>
     */
    @RequestMapping("listHotTag")
    @RestReturn(value=TagDTO.class, collection = true)
    public RestResponse listHotTag(ListHotTagCommand cmd) {
    	
    	List<TagDTO> tags = hotTagService.listHotTag(cmd);
        
        RestResponse response = new RestResponse(tags);
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
    	
    	TagDTO tag = hotTagService.setHotTag(cmd);
        
        RestResponse response = new RestResponse(tag);
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
    	
    	hotTagService.deleteHotTag(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /hotTag/deleteHotTagByName</b>
     * <p>通过名称删除热门标签</p>
     */
    @RequestMapping("deleteHotTagByName")
    @RestReturn(value=String.class)
    public RestResponse deleteHotTagByName(DeleteHotTagByNameCommand cmd) {

        hotTagService.deleteHotTagByName(cmd);

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
    @RestReturn(value=SearchTagResponse.class)
    public RestResponse searchTag(SearchTagCommand cmd) {
        
    	SearchTagResponse resp = hotTagSearcher.query(cmd);    	
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
