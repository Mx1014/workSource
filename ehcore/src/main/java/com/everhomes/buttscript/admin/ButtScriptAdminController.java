package com.everhomes.buttscript.admin;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.buttscript.ButtScriptService;
import com.everhomes.configurations.ConfigurationsService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.buttscript.*;
import com.everhomes.rest.configurations.admin.*;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * buttScript  Controller
 * @author huanglm
 *
 */
@RestDoc(value="ButtScript Admin controller", site="core")
@RestController
@RequestMapping("/admin/buttScript")
public class ButtScriptAdminController extends ControllerBase{
	
    @Autowired
	private ButtScriptService buttScriptService ;

	/**
     * <b>URL: /admin/buttScript/getScriptByNamespace</b>
     * <p>按域空间查询脚本信息接口</p>
     */
	@RequestMapping("getScriptByNamespace")
    @RestReturn(value=ScriptDTO.class)
	public RestResponse getScriptByNamespace( GetScriptCommand cmd ) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		ScriptDTO resultDTO = buttScriptService.getScriptByNamespace(cmd);
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}

	/**
     * <b>URL: /admin/buttScript/saveScript</b>
     * <p>2)保存脚本信息接口(含保存并发布)</p>
     */
	@XssExclude
	@RequestMapping("saveScript")
    @RestReturn(value=ScriptVersionInfoDTO.class)
	public RestResponse saveScript( SaveScriptCommand cmd ) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		ScriptVersionInfoDTO resultDTO = buttScriptService.saveScript(cmd);
		RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
	}

	/**
     * <b>URL: /admin/buttScript/findScriptVersionInfoByNamespaceId</b>
     * <p>3)通过域空间查询所有版本脚本信息接口</p>
     */
	@RequestMapping("findScriptVersionInfoByNamespaceId")
    @RestReturn(value=ScriptVersionInfoResponse.class)
	public RestResponse findScriptVersionInfoByNamespaceId( FindScriptVersionInfoCommand cmd ) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		ScriptVersionInfoResponse res = buttScriptService.findScriptVersionInfoByNamespaceId(cmd);
		RestResponse response = new RestResponse(res);
        setResponseSuccess(response);
        return response;
	}


	/**
     * <b>URL: /admin/buttScript/publishScriptVersion</b>
     * <p>4)发布版本接口</p>
     */
	@RequestMapping("publishScriptVersion")
    @RestReturn(value=String.class)
	public RestResponse publishScriptVersion( PublishScriptVersionCommand cmd ) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		buttScriptService.publishScriptVersion(cmd);
		RestResponse response = new RestResponse();
        setResponseSuccess(response);
        return response;
	}


	/**
     * <b>URL: /admin/buttScript/findScriptInfoType</b>
     * <p>5)查询域空间下脚本分类接口</p>
     */
	@RequestMapping("findScriptInfoType")
    @RestReturn(value=ScriptnInfoTypeResponse.class)
	public RestResponse findScriptInfoType( FindScriptInfoTypeCommand cmd ) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		ScriptnInfoTypeResponse res = buttScriptService.findScriptInfoType(cmd);
		RestResponse response = new RestResponse(res);
        setResponseSuccess(response);
        return response;
	}

	/**
	 * <b>URL: /admin/buttScript/publishScriptVersionCancel</b>
	 * <p>6)取消版本发布接口</p>
	 */
	@RequestMapping("publishScriptVersionCancel")
	@RestReturn(value=String.class)
	public RestResponse publishScriptVersionCancel( PublishScriptVersionCancelCommand  cmd ) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
		buttScriptService.publishScriptVersionCancel(cmd);
		RestResponse response = new RestResponse();
		setResponseSuccess(response);
		return response;
	}

	/**
	 * <p>设置response 成功信息</p>
	 * @param response
	 */
	private void setResponseSuccess(RestResponse response){
		if(response == null ) return ;
		
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
	}
}
