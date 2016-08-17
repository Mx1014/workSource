// @formatter:off
package com.everhomes.namespace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.namespace.ConfigNamespaceCommand;
import com.everhomes.rest.namespace.CreateNamespaceCommand;
import com.everhomes.rest.namespace.CreateNamespaceResponse;

/**
 * Namespace REST API controller
 *
 */
@RestController
@RequestMapping("/admin/namespace")
public class NamespacesAdminController extends ControllerBase {
    
    @Autowired
    private NamespacesService namespacesService;
    
    /**
	 * <b>URL: /admin/namespace/createNamespace</b>
	 * <p>
	 * 创建一个域空间
	 * </p>
	 */
    @RequestMapping("createNamespace")
    @RestReturn(CreateNamespaceResponse.class)
    public RestResponse createNamespace(CreateNamespaceCommand cmd){
    	return new RestResponse(namespacesService.createNamespace(cmd));
    }
    
    /**
     * 
     * <b>URL: /admin/namespace/configNamespace</b>
     * <p>小区导入第一步，配置域空间</p>
     */
    @RequestMapping("configNamespace")
    @RestReturn(String.class)
    public RestResponse configNamespace(ConfigNamespaceCommand cmd){
    	namespacesService.configNamespace(cmd);
    	return new RestResponse();
    }
    
}
