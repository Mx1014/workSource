// @formatter:off
package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CancelTaskCommand;
import com.everhomes.rest.pmtask.CloseTaskCommand;
import com.everhomes.rest.pmtask.GetPrivilegesCommand;
import com.everhomes.rest.pmtask.GetPrivilegesDTO;
import com.everhomes.rest.pmtask.GetTaskLogCommand;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetStatisticsCommand;
import com.everhomes.rest.pmtask.GetStatisticsResponse;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListUserTasksCommand;
import com.everhomes.rest.pmtask.ListUserTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.PmTaskLogDTO;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.CompleteTaskCommand;

@RestDoc(value="Pmtask controller", site="pmtask")
@RestController
@RequestMapping("/pmtask")
public class PmTaskController extends ControllerBase {

	@Autowired
	private PmTaskService pmTaskService;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	@Autowired
	private PmTaskSearch pmTaskSearch;

	/**
     * <b>URL: /pmtask/getPrivileges</b>
     * <p>获取权限列表</p>
     */
    @RequestMapping("getPrivileges")
    @RestReturn(value=GetPrivilegesDTO.class)
    public RestResponse getPrivileges(GetPrivilegesCommand cmd) {
  	    GetPrivilegesDTO res = pmTaskService.getPrivileges(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
      /**
       * <b>URL: /pmtask/listTaskCategories</b>
       * <p>获取服务类型列表</p>
       */
      @RequestMapping("listTaskCategories")
      @RestReturn(value=ListTaskCategoriesResponse.class)
      public RestResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
    	  ListTaskCategoriesResponse res = pmTaskService.listTaskCategories(cmd);
          RestResponse response = new RestResponse(res);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/createTaskCategory</b>
       * <p>新建服务类型</p>
       */
      @RequestMapping("createTaskCategory")
      @RestReturn(value=CategoryDTO.class)
      public RestResponse createTaskCategory(CreateTaskCategoryCommand cmd) {
    	  CategoryDTO dto = pmTaskService.createTaskCategory(cmd);
          RestResponse response = new RestResponse(dto);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/deleteTaskCategory</b>
       * <p>删除服务类型</p>
       */
      @RequestMapping("deleteTaskCategory")
      @RestReturn(value=String.class)
      public RestResponse deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
      	  pmTaskService.deleteTaskCategory(cmd);
          RestResponse response = new RestResponse();
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/createTask</b>
       * <p>创建新任务</p>
       */
      @RequestMapping("createTask")
      @RestReturn(value=PmTaskDTO.class)
      public RestResponse createTask(CreateTaskCommand cmd) {
    	  PmTaskDTO dto = pmTaskService.createTask(cmd);
          RestResponse response = new RestResponse(dto);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/getTaskDetail</b>
       * <p>获取任务详情</p>
       */
      @RequestMapping("getTaskDetail")
      @RestReturn(value=PmTaskDTO.class)
      public RestResponse getTaskDetail(GetTaskDetailCommand cmd) {
    	  PmTaskDTO  res = pmTaskService.getTaskDetail(cmd);
          RestResponse response = new RestResponse(res);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
  	/**
  	 * <b>URL: /pmtask/assignTask</b>
  	 * <p>报修贴分配人员</p>
  	 */
  	@RequestMapping("assignTask")
  	@RestReturn(value=String.class)
  	public RestResponse assignTask(@Valid AssignTaskCommand cmd) {
  		pmTaskService.assignTask(cmd);
  		RestResponse response = new RestResponse();
  		response.setErrorCode(ErrorCodes.SUCCESS);
  		response.setErrorDescription("OK");
  		return response;
  	}
     
	/**
	 * <b>URL: /pmtask/completeTask</b>
	 * <p>完成任务</p>
	 */
	@RequestMapping("completeTask")
	@RestReturn(value=String.class)
	public RestResponse completeTask(CompleteTaskCommand cmd) {
		pmTaskService.completeTask(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pmtask/closeTask</b>
	 * <p>关闭任务</p>
	 */
	@RequestMapping("closeTask")
	@RestReturn(value=String.class)
	public RestResponse closeTask(CloseTaskCommand cmd) {
		pmTaskService.closeTask(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pmtask/cancelTask</b>
	 * <p>取消任务</p>
	 */
	@RequestMapping("cancelTask")
	@RestReturn(value=String.class)
	public RestResponse cancelTask(CancelTaskCommand cmd) {
		pmTaskService.cancelTask(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pmtask/evaluateTask</b>
	 * <p>评价任务</p>
	 */
	@RequestMapping("evaluateTask")
	@RestReturn(value=String.class)
	public RestResponse evaluateTask(EvaluateTaskCommand cmd) {
		pmTaskService.evaluateTask(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /pmtask/getTaskLog</b>
	 * <p>获取任务完成详情或任务关闭理由</p>
	 */
	@RequestMapping("getTaskLog")
	@RestReturn(value=String.class)
	public RestResponse getTaskLog(GetTaskLogCommand cmd) {
		PmTaskLogDTO dto = pmTaskService.getTaskLog(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
  	
	/**
     * <b>URL: /pmtask/listUserTasks</b>
     * <p>获取任务列表</p>
     */
    @RequestMapping("listUserTasks")
    @RestReturn(value=ListUserTasksResponse.class)
    public RestResponse listUserTasks(ListUserTasksCommand cmd) {
    	ListUserTasksResponse res = pmTaskService.listUserTasks(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/searchTasks</b>
     * <p>搜索任务</p>
     */
    @RequestMapping("searchTasks")
    @RestReturn(value=SearchTasksResponse.class)
    public RestResponse searchTasks(SearchTasksCommand cmd) {
  	    SearchTasksResponse res = pmTaskService.searchTasks(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/exportTasks</b>
     * <p>任务导出</p>
     */
    @RequestMapping("exportTasks")
    public void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp) {
  	  	pmTaskService.exportTasks(cmd, resp);
    }
    
    /**
     * <b>URL: /pmtask/searchTaskStatistics</b>
     * <p>搜索统计列表</p>
     */
    @RequestMapping("searchTaskStatistics")
    @RestReturn(value=SearchTaskStatisticsResponse.class)
    public RestResponse searchTaskStatistics(SearchTaskStatisticsCommand cmd) {
    	SearchTaskStatisticsResponse res = pmTaskService.searchTaskStatistics(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/exportListStatistics</b>
     * <p>导出统计列表</p>
     */
    @RequestMapping("exportListStatistics")
    @RestReturn(value=SearchTaskStatisticsResponse.class)
    public RestResponse exportListStatistics(SearchTaskStatisticsCommand cmd) {
    	SearchTaskStatisticsResponse res = pmTaskService.searchTaskStatistics(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/getStatistics</b>
     * <p>获取统计详情</p>
     */
    @RequestMapping("getStatistics")
    @RestReturn(value=GetStatisticsResponse.class)
    public RestResponse getStatistics(GetStatisticsCommand cmd) {
    	GetStatisticsResponse res = pmTaskService.getStatistics(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/exportStatistics</b>
     * <p>导出统计详情</p>
     */
    @RequestMapping("exportStatistics")
    @RestReturn(value=GetStatisticsResponse.class)
    public RestResponse exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp) {
    	pmTaskService.exportStatistics(cmd, resp);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/syncFromDb</b>
     * <p>同步索引</p>
     */
    @RequestMapping("syncFromDb")
    @RestReturn(value=String.class)
    public RestResponse syncFromDb() {
    	pmTaskSearch.syncFromDb();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 
    
    /**
     * <b>URL: /pmtask/createStatistics</b>
     * <p>统计</p>
     */
    @RequestMapping("createStatistics")
    @RestReturn(value=String.class)
    public RestResponse createStatistics() {
    	pmTaskService.createStatistics();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}