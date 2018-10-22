package com.everhomes.openapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.AppProvider;
import com.everhomes.archives.ArchivesService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.archives.ArchivesContactDTO;
import com.everhomes.rest.archives.ListArchivesContactsCommand;
import com.everhomes.rest.archives.ListArchivesContactsResponse;
import com.everhomes.rest.archives.OpenListArchivesContactsCommand;
import com.everhomes.rest.openapi.techpark.CustomerResponse;
import com.everhomes.rest.openapi.techpark.SyncDataCommand;
import com.everhomes.rest.organization.GetArchivesContactCommand;
import com.everhomes.rest.organization.ListAllChildrenOrganizationsCommand;
import com.everhomes.rest.organization.ListAllTreeOrganizationsCommand;
import com.everhomes.rest.organization.OpenListAllChildrenOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationMenuResponse;
import com.everhomes.rest.organization.OrganizationTreeDTO;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value="Synch Info Controller", site="synchInfo")
@RestController
@RequestMapping("/openapi/org")
public class OrganizationOpenController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationOpenController.class);

	@Autowired
	private OrganizationService organizationService; 
	
	@Autowired
	private ArchivesService archivesService; 
	
	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;
	
    /**
     * <b>URL: /openapi/org/listAllChildrenOrganizations</b>
     * <p>获取层级菜单</p>
     */
    @RequestMapping("listAllChildrenOrganizations")
    @RestReturn(value = OrganizationMenuResponse.class)
    public RestResponse listAllChildrenOrganizations(@Valid OpenListAllChildrenOrganizationsCommand cmd) {
        RestResponse response = new RestResponse(organizationService.openListAllChildrenOrganizations(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /openapi/org/listArchivesContacts</b>
     * <p>通讯录成员列表</p>
     */
    @RequestMapping("listArchivesContacts")
    @RestReturn(value = ListArchivesContactsResponse.class)
    public RestResponse
    listArchivesContacts(OpenListArchivesContactsCommand cmd){

		AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
		if (appNamespaceMapping == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
					"not exist app namespace mapping");
		}
		cmd.setNamespaceId(appNamespaceMapping.getNamespaceId());
		UserContext.setCurrentNamespaceId(appNamespaceMapping.getNamespaceId());
        ListArchivesContactsResponse res = archivesService.listArchivesContacts(ConvertHelper.convert(cmd, ListArchivesContactsCommand.class));
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * <b>URL: /openapi/org/getArchivesContact</b>
     * <p>获取员工基础信息</p>
     */
    @RequestMapping("getArchivesContact")
    @RestReturn(value = ArchivesContactDTO.class)
    public RestResponse getArchivesContact(@Valid GetArchivesContactCommand cmd) {
        RestResponse response = new RestResponse(archivesService.getArchivesContact(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
