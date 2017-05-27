package com.everhomes.domain;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ListServiceModulePrivilegesCommand;
import com.everhomes.rest.acl.ListServiceModulesCommand;
import com.everhomes.rest.acl.ServiceModuleAssignmentRelationDTO;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.domain.DomainDTO;
import com.everhomes.rest.domain.GetDomainInfoCommand;
import com.everhomes.rest.module.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestDoc(value = "domain controller", site = "core")
@RestController
@RequestMapping("/domain")
public class DomainController extends ControllerBase {

    @Autowired
    private ServiceModuleService serviceModuleService;

    /**
     * <b>URL: /domain/getDomainInfo</b>
     * <p>
     * 获取域名配置信息
     * </p>
     */
    @RequestMapping("getDomainInfo")
    @RestReturn(value = DomainDTO.class)
    public RestResponse getDomainInfo(@Valid GetDomainInfoCommand cmd) {
        DomainDTO dto = new DomainDTO();
        dto.setOwnerType(EntityType.ZUOLIN_ADMIN.getCode());
        dto.setOwnerId(0L);
        dto.setNamespaceId(0);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
