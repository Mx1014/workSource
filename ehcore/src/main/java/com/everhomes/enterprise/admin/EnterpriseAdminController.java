package com.everhomes.enterprise.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.enterprise.CreateEnterpriseCommand;
import com.everhomes.enterprise.DeleteEnterpriseCommand;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseApproveCommand;
import com.everhomes.enterprise.EnterpriseDTO;
import com.everhomes.enterprise.EnterpriseService;
import com.everhomes.enterprise.ListEnterpriseByCommunityIdCommand;
import com.everhomes.enterprise.ListEnterpriseResponse;
import com.everhomes.enterprise.UpdateContactorCommand;
import com.everhomes.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="Enterprise Admin controller", site="core")
@RestController
@RequestMapping("/admin/enterprise")
public class EnterpriseAdminController extends ControllerBase {
    @Autowired
    EnterpriseService enterpriseService;
    
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
    
    @RequestMapping("createEnterprise")
    @RestReturn(value=EnterpriseDTO.class)
    public RestResponse createEnterpriseCommand(@Valid CreateEnterpriseCommand cmd) {
//        Enterprise entry = ConvertHelper.convert(cmd, Enterprise.class);
        
        EnterpriseDTO dto = this.enterpriseService.createEnterprise(cmd);
        RestResponse res = new RestResponse(dto);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    @RequestMapping("updateEnterprise")
    @RestReturn(value=EnterpriseDTO.class)
    public RestResponse updateEnterprise(@Valid UpdateEnterpriseCommand cmd) {
        
        EnterpriseDTO dto = this.enterpriseService.updateEnterprise(cmd);
        RestResponse res = new RestResponse(dto);
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
}
