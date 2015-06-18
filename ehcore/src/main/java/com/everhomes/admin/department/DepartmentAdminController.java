package com.everhomes.admin.department;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.department.CreateDepartmentCommand;
import com.everhomes.department.CreateDepartmentCommunityCommand;
import com.everhomes.department.CreateDepartmentMemberCommand;
import com.everhomes.department.DepartmentService;
import com.everhomes.department.ListDepartmentsCommand;
import com.everhomes.department.ListDepartmentsCommandResponse;
import com.everhomes.discover.RestReturn;
import com.everhomes.pm.CreatePropMemberCommand;
import com.everhomes.pm.ListPropAddressMappingCommand;
import com.everhomes.pm.ListPropAddressMappingCommandResponse;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/admin/dpt")
public class DepartmentAdminController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentAdminController.class);

	@Autowired
	DepartmentService departmentService;
	
	/**
     * <b>URL: /admin/dpt/createDepartment</b>
     * <p>创建政府机构</p>
     * @return 添加的结果
     */
    @RequestMapping("createDepartment")
    @RestReturn(value=String.class)
    public RestResponse createDepartment(@Valid CreateDepartmentCommand cmd) {
    	departmentService.createDepartment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/dpt/createDepartmentMember</b>
     * <p>创建政府机构成员</p>
     * @return 添加的结果
     */
    @RequestMapping("createDepartmentMember")
    @RestReturn(value=String.class)
    public RestResponse createDepartmentMember(@Valid CreateDepartmentMemberCommand cmd) {
    	departmentService.createDepartmentMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/dpt/createDepartmentCommunity</b>
     * <p>创建政府机构对应的小区列表</p>
     * @return 添加的结果
     */
    @RequestMapping("createDepartmentCommunity")
    @RestReturn(value=String.class)
    public RestResponse createDepartmentCommunity(@Valid CreateDepartmentCommunityCommand cmd) {
    	departmentService.createDepartmentCommunity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/dpt/listDepartments</b>
     * <p>列出政府机构表（）</p>
     */
    @RequestMapping("listDepartments")
    @RestReturn(value=ListDepartmentsCommandResponse.class)
    public RestResponse listDepartments(@Valid ListDepartmentsCommand cmd) {
    	ListDepartmentsCommandResponse commandResponse = departmentService.listDepartments(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
