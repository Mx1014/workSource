package com.everhomes.enterprise.admin;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.enterprise.CreateEnterpriseCommand;
import com.everhomes.enterprise.EnterpriseApproveCommand;
import com.everhomes.enterprise.EnterpriseDTO;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Enterprise Admin controller", site="core")
@RestController
@RequestMapping("/admin/enterprise")
public class EnterpriseAdminController {
    /**
     * <b>URL: /admin/enterprise/approve</b>
     * <p>审批加入园区的企业</p>
     * @return String
     */
    @RequestMapping("approve")
    @RestReturn(value=String.class)
    public RestResponse approve(@Valid EnterpriseApproveCommand cmd) {
        return null;
    }
    
    @RequestMapping("reject")
    @RestReturn(value=String.class)
    public RestResponse reject(@Valid EnterpriseApproveCommand cmd) {
        return null;
    }
    
    @RequestMapping("revoke")
    @RestReturn(value=String.class)
    public RestResponse revoke(@Valid EnterpriseApproveCommand cmd) {
        return null;
    }
    
    @RequestMapping("create")
    @RestReturn(value=EnterpriseDTO.class)
    public RestResponse createEnterpriseCommand(@Valid CreateEnterpriseCommand cmd) {
        return null;
    }
    
}
