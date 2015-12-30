package com.everhomes.business.admin;




import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.business.BusinessService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.business.DeleteBusinessCommand;
import com.everhomes.rest.business.admin.CreateBusinessAdminCommand;
import com.everhomes.rest.business.admin.DeletePromoteBusinessAdminCommand;
import com.everhomes.rest.business.admin.ListBusinessesByKeywordAdminCommand;
import com.everhomes.rest.business.admin.ListBusinessesByKeywordAdminCommandResponse;
import com.everhomes.rest.business.admin.PromoteBusinessAdminCommand;
import com.everhomes.rest.business.admin.RecommendBusinessesAdminCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="Business admin controller", site="core")
@RestController
@RequestMapping("/admin/business")
public class BusinessAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAdminController.class);
    
    @Autowired
    private BusinessService businessService;

    /**
     * <b>URL: /admin/business/listBusinessesByKeyword</b>
     * <p>根据关键字查询店铺列表</p>
     */
    @RequestMapping("listBusinessesByKeyword")
    @RestReturn(value=ListBusinessesByKeywordAdminCommandResponse.class)
    public RestResponse listBusinessesByKeyword(@Valid ListBusinessesByKeywordAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListBusinessesByKeywordAdminCommandResponse res = businessService.listBusinessesByKeyword(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/business/createBusiness</b>
     * <p>创建服务店铺</p>
     */
    @RequestMapping("createBusiness")
    @RestReturn(value=String.class)
    public RestResponse createBusiness(@Valid CreateBusinessAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        businessService.createBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    

    /**
     * <b>URL: /admin/business/recommendBusiness</b>
     * <p>推荐/取消推荐服务店铺</p>
     */
    @RequestMapping("recommendBusiness")
    @RestReturn(value=String.class)
    public RestResponse recommendBusiness(@Valid RecommendBusinessesAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        businessService.recommendBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/business/deleteBusiness</b>
     * <p>删除店铺</p>
     */
    @RequestMapping("deleteBusiness")
    @RestReturn(value=String.class)
    public RestResponse deleteBusiness(@Valid DeleteBusinessCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        businessService.deleteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/business/promoteBusiness</b>
     * <p>设置店铺广场可见</p>
     */
    @RequestMapping("promoteBusiness")
    @RestReturn(value=String.class)
    public RestResponse promoteBusiness(@Valid PromoteBusinessAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        businessService.promoteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/business/deletePromoteBusiness</b>
     * <p>删除店铺广场可见</p>
     */
    @RequestMapping("deletePromoteBusiness")
    @RestReturn(value=String.class)
    public RestResponse deletePromoteBusiness(@Valid DeletePromoteBusinessAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        businessService.deletePromoteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
