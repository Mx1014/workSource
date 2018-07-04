package com.everhomes.launchpadbase;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.launchpad.CommunityBizService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value="LaunchPadBase controller", site="core")
@RestController
@RequestMapping("/communityBiz")
public class CommunityBizController extends ControllerBase {

    @Autowired
    private CommunityBizService communityBizService;


    /**
     * <b>URL: /communityBiz/createCommunityBiz</b>
     * <p>新建园区电商入口</p>
     */
    @RequestMapping("createCommunityBiz")
    @RestReturn(value=CommunityBizDTO.class)
    public RestResponse createCommunityBiz(CreateCommunityBizCommand cmd) {
        CommunityBizDTO res = communityBizService.CreateCommunityBiz(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /communityBiz/UpdateCommunityBizCommand</b>
     * <p>更新园区电商入口</p>
     */
    @RequestMapping("updateCommunityBiz")
    @RestReturn(value=CommunityBizDTO.class)
    public RestResponse updateCommunityBiz(UpdateCommunityBizCommand cmd) {
        CommunityBizDTO res = communityBizService.updateCommunityBiz(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /communityBiz/deleteCommunityBiz</b>
     * <p>删除园区电商入口</p>
     */
    @RequestMapping("deleteCommunityBiz")
    @RestReturn(value=String.class)
    public RestResponse deleteCommunityBiz(DeleteCommunityBizCommand cmd) {
        communityBizService.deleteCommunityBiz(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /communityBiz/findCommunityBiz</b>
     * <p>查询园区电商入口</p>
     */
    @RequestMapping("findCommunityBiz")
    @RestReturn(value=CommunityBizDTO.class)
    public RestResponse findCommunityBiz(FindCommunityBizCommand cmd) {
        CommunityBizDTO res = communityBizService.findCommunityBiz(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /communityBiz/findCommunityBizForApp</b>
     * <p>查询园区电商入口</p>
     */
    @RequestMapping("findCommunityBizForApp")
    @RestReturn(value=CommunityBizDTO.class)
    @RequireAuthentication(false)
    public RestResponse findCommunityBizForApp(FindCommunityBizForAppCommand cmd) {
        CommunityBizDTO res = communityBizService.findCommunityBizForApp(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
