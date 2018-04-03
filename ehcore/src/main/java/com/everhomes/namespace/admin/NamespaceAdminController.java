package com.everhomes.namespace.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.namespace.admin.CreateNamespaceCommand;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.namespace.admin.UpdateNamespaceCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="namespace admin controller", site="core")
@RestController
@RequestMapping("/admin/namespace")
public class NamespaceAdminController extends ControllerBase {
	
	@Autowired
	private NamespacesService namespacesService;
	
	/**
	 * 
	 * <b>URL: /admin/namespace/listNamespace</b>
	 * <p>域空间列表</p>
	 */
	@RequestMapping("listNamespace")
	@RestReturn(value=NamespaceInfoDTO.class, collection=true)
	public RestResponse listNamespace(){
//    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		return new RestResponse(namespacesService.listNamespace());
	}
	
	/**
	 * 
	 * <b>URL: /admin/namespace/createNamespace</b>
	 * <p>创建域空间</p>
	 */
	@RequestMapping("createNamespace")
	@RestReturn(NamespaceInfoDTO.class)
	public RestResponse createNamespace(CreateNamespaceCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		return new RestResponse(namespacesService.createNamespace(cmd));
	}
	
	/**
	 * 
	 * <b>URL: /admin/namespace/updateNamespace</b>
	 * <p>更新域空间</p>
	 */
	@RequestMapping("updateNamespace")
	@RestReturn(NamespaceInfoDTO.class)
	public RestResponse updateNamespace(UpdateNamespaceCommand cmd){
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		return new RestResponse(namespacesService.updateNamespace(cmd));
	}
	
}
