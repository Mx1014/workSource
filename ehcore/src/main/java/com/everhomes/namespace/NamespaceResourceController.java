// @formatter:off
package com.everhomes.namespace;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.namespace.GetNamespaceDetailCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.namespace.NamespaceDetailDTO;
import com.everhomes.util.RequireAuthentication;

/**
 * Namespace resource REST API controller
 *
 */
@RestController
@RequestMapping("/namespace")
public class NamespaceResourceController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResourceController.class);

    @Autowired
    private NamespaceResourceService namespaceResourceService;
    
    /**
     * <b>URL: /namespace/listCommunityByNamespace</b>
     * <p>根据命名空间下列出机构所管辖的小区信息</p>
     */
    //checked
    @RequireAuthentication(false)
    @RequestMapping("listCommunityByNamespace")
    @RestReturn(value=ListCommunityByNamespaceCommandResponse.class)
    public RestResponse listCommunityByNamespace(@Valid ListCommunityByNamespaceCommand cmd) {
        ListCommunityByNamespaceCommandResponse commandResponse = namespaceResourceService.listCommunityByNamespace(cmd);
        
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /namespace/getNamespaceDetail</b>
     * <p>获取命名空间的一些配置信息</p>
     */
    //checked
    @RequireAuthentication(false)
    @RequestMapping("getNamespaceDetail")
    @RestReturn(value=NamespaceDetailDTO.class)
    public RestResponse getNamespaceDetail(GetNamespaceDetailCommand cmd) {
        NamespaceDetailDTO dto = namespaceResourceService.getNamespaceDetail(cmd);
        
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
