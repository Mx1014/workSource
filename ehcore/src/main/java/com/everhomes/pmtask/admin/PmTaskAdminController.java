// @formatter:off
package com.everhomes.pmtask.admin;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.pmtask.PmTaskSearch;
import com.everhomes.pmtask.PmTaskService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.GetStatisticsCommand;
import com.everhomes.rest.pmtask.GetStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;

@RestDoc(value="Pmtask controller", site="pmtask")
@RestController
@RequestMapping("/admin/pmtask")
public class PmTaskAdminController extends ControllerBase {

	@Autowired
	private PmTaskService pmTaskService;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	@Autowired
	private PmTaskSearch pmTaskSearch;
      
      /**
       * <b>URL: /admin/pmtask/createTaskByOrg</b>
       * <p>创建新任务</p>
       */
      @RequestMapping("createTaskByOrg")
      @RestReturn(value=PmTaskDTO.class)
      public RestResponse createTaskByOrg(CreateTaskCommand cmd) {
    	  PmTaskDTO dto = pmTaskService.createTaskByOrg(cmd);
          RestResponse response = new RestResponse(dto);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
}