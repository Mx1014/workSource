// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.uniongroup.*;
import com.everhomes.search.UniongroupSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uniongroup")
public class UniongroupController extends ControllerBase {

    @Autowired
    private UniongroupService uniongroupService;

    @Autowired
    private UniongroupSearcher uniongroupSearcher;

    /**
     * <p>保存组配置</p>
     * <b>URL: /uniongroup/saveUniongroupConfigures</b>
     */
    @RequestMapping("saveUniongroupConfigures")
    @RestReturn(String.class)
    public RestResponse createUniongroupConfigures(SaveUniongroupConfiguresCommand cmd) {
        uniongroupService.saveUniongroupConfigures(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取组配置</p>
     * <b>URL: /uniongroup/getConfiguresListByGroupId</b>
     */
    @RequestMapping("getConfiguresListByGroupId")
    @RestReturn(value = UniongroupConfiguresDTO.class, collection = true)
    public RestResponse getConfiguresListByGroupId(GetUniongroupConfiguresCommand cmd) {
        RestResponse response = new RestResponse(uniongroupService.getConfiguresListByGroupId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取组关系</p>
     * <b>URL: /uniongroup/listUniongroupMemberDetailsByGroupId</b>
     */
    @RequestMapping("listUniongroupMemberDetailsByGroupId")
    @RestReturn(value = UniongroupMemberDetailsDTO.class, collection = true)
    public RestResponse listUniongroupMemberDetailsByGroupId(ListUniongroupMemberDetailsCommand cmd){
        RestResponse response = new RestResponse(uniongroupService.listUniongroupMemberDetailsByGroupId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取组关系(条件查询)</p>
     * <b>URL: /uniongroup/listUniongroupMemberDetailsWithCondition</b>
     */
    @RequestMapping("listUniongroupMemberDetailsWithCondition")
    @RestReturn(value = UniongroupMemberDetailsDTO.class, collection = true)
    public RestResponse listUniongroupMemberDetailsWithCondition(ListUniongroupMemberDetailsWithConditionCommand cmd){
        RestResponse response = new RestResponse(uniongroupService.listUniongroupMemberDetailsWithCondition(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /uniongroup/syncUniongroupDetailsIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncUniongroupDetailsIndex")
    @RestReturn(value=String.class)
    public RestResponse syncUniongroupDetailsIndex() {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        uniongroupSearcher.syncUniongroupDetailsIndexs();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

}