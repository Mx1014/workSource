package com.everhomes.enterprise.admin;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprise.ApproveContactCommand;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.DeleteEnterpriseCommand;
import com.everhomes.rest.enterprise.EnterpriseApproveCommand;
import com.everhomes.rest.enterprise.EnterpriseDTO;
import com.everhomes.rest.enterprise.ImportEnterpriseDataCommand;
import com.everhomes.rest.enterprise.ListEnterpriseByCommunityIdCommand;
import com.everhomes.rest.enterprise.ListEnterpriseResponse;
import com.everhomes.rest.enterprise.RejectContactCommand;
import com.everhomes.rest.enterprise.UpdateContactorCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value="Enterprise Admin controller", site="core")
@RestController
@RequestMapping("/admin/enterprise")
public class EnterpriseAdminController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseAdminController.class);
    @Autowired
    private EnterpriseService enterpriseService;

    
    /**
     * <b>URL: /admin/enterprise/approve</b>
     * <p>审批加入园区的企业</p>
     * @return String
     */
    @RequestMapping("approve")
    @RestReturn(value=String.class)
    public RestResponse approve(@Valid EnterpriseApproveCommand cmd) {
        this.enterpriseService.approve(UserContext.current().getUser(), cmd.getEnterpriseId(), cmd.getCommunityId());
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    @RequestMapping("reject")
    @RestReturn(value=String.class)
    public RestResponse reject(@Valid EnterpriseApproveCommand cmd) {
        this.enterpriseService.reject(UserContext.current().getUser(), cmd.getEnterpriseId(), cmd.getCommunityId());
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    @RequestMapping("revoke")
    @RestReturn(value=String.class)
    public RestResponse revoke(@Valid EnterpriseApproveCommand cmd) {
        this.enterpriseService.revoke(UserContext.current().getUser(), cmd.getEnterpriseId(), cmd.getCommunityId());
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    @RequestMapping("updateContactor")
    @RestReturn(value=ListEnterpriseResponse.class)
    public RestResponse updateContactor(UpdateContactorCommand cmd) {
    	
    	enterpriseService.updateContactor(cmd);
    	ListEnterpriseByCommunityIdCommand command = new ListEnterpriseByCommunityIdCommand();
    	command.setCommunityId(cmd.getCommunityId());
    	
    	RestResponse res = new RestResponse(enterpriseService.listEnterpriseByCommunityId(command));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
         
        return res;
    }
    
    @RequestMapping("deleteEnterprise")
    @RestReturn(value=ListEnterpriseResponse.class)
    public RestResponse deleteEnterprise(DeleteEnterpriseCommand cmd) {
    	
    	enterpriseService.deleteEnterprise(cmd);
    	ListEnterpriseByCommunityIdCommand command = new ListEnterpriseByCommunityIdCommand();
    	command.setCommunityId(cmd.getCommunityId());
    	 
    	RestResponse res = new RestResponse(enterpriseService.listEnterpriseByCommunityId(command));
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
         
        return res;
    }
    
   
    @RequestMapping("importEnterpriseData")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importEnterpriseData(@Valid ImportEnterpriseDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
		ImportDataResponse importDataResponse = this.enterpriseService.importEnterpriseData(files[0], userId, cmd);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
