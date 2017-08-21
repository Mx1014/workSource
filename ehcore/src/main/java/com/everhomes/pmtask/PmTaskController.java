// @formatter:off
package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.pmtask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryDTO;

@RestDoc(value="Pmtask controller", site="pmtask")
@RestController
@RequestMapping("/pmtask")
public class PmTaskController extends ControllerBase {

	@Autowired
	private PmTaskService pmTaskService;
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
       * <b>URL: /pmtask/listAllTaskCategories</b>
       * <p>获取所有服务类型列表</p>
       */
      @RequestMapping("listAllTaskCategories")
      @RestReturn(value=CategoryDTO.class, collection=true)
      public RestResponse listAllTaskCategories(ListAllTaskCategoriesCommand cmd) {
    	  List<CategoryDTO> res = pmTaskService.listAllTaskCategories(cmd);
          RestResponse response = new RestResponse(res);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/createTaskOperatePerson</b>
       * <p>新增任务人员</p>
       */
      @RequestMapping("createTaskOperatePerson")
      @RestReturn(value=String.class)
      public RestResponse createTaskOperatePerson(CreateTaskOperatePersonCommand cmd) {
    	  pmTaskService.createTaskOperatePerson(cmd);
          RestResponse response = new RestResponse();
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/deleteTaskOperatePerson</b>
       * <p>删除任务人员</p>
       */
      @RequestMapping("deleteTaskOperatePerson")
      @RestReturn(value=String.class)
      public RestResponse deleteTaskOperatePerson(DeleteTaskOperatePersonCommand cmd) {
    	  pmTaskService.deleteTaskOperatePerson(cmd);
          RestResponse response = new RestResponse();
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
       * <b>URL: /pmtask/createTaskByUser</b>
       * <p>创建新任务</p>
       */
      @RequestMapping("createTaskByUser")
      @RestReturn(value=PmTaskDTO.class)
      public RestResponse createTaskByUser(CreateTaskCommand cmd) {
    	  //TODO: 添加服务地点类型
    	  PmTaskDTO dto = pmTaskService.createTask(cmd);
          RestResponse response = new RestResponse(dto);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /pmtask/createTaskByOrg</b>
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
      
      /**
       * <b>URL: /pmtask/updateTaskByOrg</b>
       * <p>编辑任务 管理员</p>
       */
      @RequestMapping("updateTaskByOrg")
      @RestReturn(value=String.class)
      public RestResponse updateTaskByOrg(UpdateTaskCommand cmd) {

    	  pmTaskService.updateTaskByOrg(cmd);
          RestResponse response = new RestResponse();
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
	 * <b>URL: /pmtask/revisit</b>
	 * <p>回访</p>
	 */
	@RequestMapping("revisit")
	@RestReturn(value=String.class)
	public RestResponse revisit(RevisitCommand cmd) {
		pmTaskService.revisit(cmd);
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
	 * <b>URL: /pmtask/listAuthorizationCommunityByUser</b>
	 * <p>授权人员 管理小区列表</p>
	 */
	@RequestMapping("listAuthorizationCommunityByUser")
	@RestReturn(value=ListAuthorizationCommunityByUserResponse.class)
	public RestResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
        //TODO:
		ListAuthorizationCommunityByUserResponse resp = pmTaskService.listAuthorizationCommunityByUser(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	
	/**
	 * <b>URL: /pmtask/getTaskLog</b>
	 * <p>获取任务完成详情或任务关闭理由</p>
	 */
	@RequestMapping("getTaskLog")
	@RestReturn(value=PmTaskLogDTO.class)
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
     * <b>URL: /pmtask/listOperatePersonnels</b>
     * <p>查看任务执行人员或维修人员</p>
     */
    @RequestMapping("listOperatePersonnels")
    @RestReturn(value=ListOperatePersonnelsResponse.class)
    public RestResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd){

    	ListOperatePersonnelsResponse resp = pmTaskService.listOperatePersonnels(cmd);
  	  	RestResponse res = new RestResponse(resp);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        
        return res;
    }
    
    /**
     * <b>URL: /pmtask/getUserRelatedAddressesByCommunity</b>
     * <p>根据园区/小区 获取用户相关的地址列表</p>
     */
    @RequestMapping("getUserRelatedAddressesByCommunity")
    @RestReturn(value=GetUserRelatedAddressByCommunityResponse.class)
    public RestResponse getUserRelatedAddressesByCommunity(GetUserRelatedAddressesByCommunityCommand cmd) {
    	GetUserRelatedAddressByCommunityResponse resp = pmTaskService.getUserRelatedAddressesByCommunity(cmd);
        RestResponse response = new RestResponse(resp);
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
     * <b>URL: /pmtask/getNamespaceHandler</b>
     * <p>获取handler</p>
     */
    @RequestMapping("getNamespaceHandler")
    @RestReturn(value=NamespaceHandlerDTO.class)
    public RestResponse getNamespaceHandler(GetNamespaceHandlerCommand cmd) {
    	NamespaceHandlerDTO dto = pmTaskService.getNamespaceHandler(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/exportTasks</b>
     * <p>任务导出</p>
     */
    @RequestMapping("exportTasks")
    public void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp, HttpServletRequest req) {
  	  	pmTaskService.exportTasks(cmd, resp, req);
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
     * <b>URL: /pmtask/searchTaskCategoryStatistics</b>
     * <p>搜索分类统计列表</p>
     */
    @RequestMapping("searchTaskCategoryStatistics")
    @RestReturn(value=SearchTaskCategoryStatisticsResponse.class)
    public RestResponse searchTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
    	SearchTaskCategoryStatisticsResponse res = pmTaskService.searchTaskCategoryStatistics(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/searchTaskOperatorStatistics</b>
     * <p>搜索执行人员统计列表</p>
     */
    @RequestMapping("searchTaskOperatorStatistics")
    @RestReturn(value=SearchTaskOperatorStatisticsResponse.class)
    public RestResponse searchTaskOperatorStatistics(SearchTaskOperatorStatisticsCommand cmd) {
    	SearchTaskOperatorStatisticsResponse resp = pmTaskService.searchTaskOperatorStatistics(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/exportTaskOperatorStatistics</b>
     * <p>导出执行人员统计列表</p>
     */
    @RequestMapping("exportTaskOperatorStatistics")
    public void exportTaskOperatorStatistics(SearchTaskOperatorStatisticsCommand cmd, HttpServletResponse resp) {
    	pmTaskService.exportTaskOperatorStatistics(cmd, resp);
    }
    
    /**
     * <b>URL: /pmtask/exportTaskCategoryStatistics</b>
     * <p>导出分类统计列表</p>
     */
    @RequestMapping("exportTaskCategoryStatistics")
    public void exportTaskCategoryStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp) {
    	pmTaskService.exportTaskCategoryStatistics(cmd, resp);
    }
    
    /**
     * <b>URL: /pmtask/getTaskCategoryStatistics</b>
     * <p>获取所有项目分类统计</p>
     */
    @RequestMapping("getTaskCategoryStatistics")
    @RestReturn(value=TaskCategoryStatisticsDTO.class)
    public RestResponse getTaskCategoryStatistics(SearchTaskStatisticsCommand cmd) {
    	TaskCategoryStatisticsDTO res = pmTaskService.getTaskCategoryStatistics(cmd);
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
    public void exportListStatistics(SearchTaskStatisticsCommand cmd, HttpServletResponse resp) {
    	pmTaskService.exportListStatistics(cmd, resp);
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
    public void exportStatistics(GetStatisticsCommand cmd, HttpServletResponse resp) {
    	pmTaskService.exportStatistics(cmd, resp);

    }
    
    /**
     * <b>URL: /pmtask/syncFromDb</b>
     * <p>同步索引</p>
     */
    @RequestMapping("syncFromDb")
    @RestReturn(value=String.class)
    public RestResponse syncPmTask() {
    	pmTaskSearch.syncPmTask();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/synchronizedData</b>
     * <p>迁移数据</p>
     */
    @RequestMapping("synchronizedData")
    @RestReturn(value=String.class)
    public RestResponse synchronizedData(SearchTasksCommand cmd) {
        pmTaskService.synchronizedData(cmd);
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
    	pmTaskService.createTaskTargetStatistics();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/deleteTaskHistoryAddress</b>
     * <p>删除物业服务历史地址</p>
     */
    @RequestMapping("deleteTaskHistoryAddress")
    @RestReturn(value=String.class)
    public RestResponse deleteTaskHistoryAddress(DeleteTaskHistoryAddressCommand cmd) {

        pmTaskService.deleteTaskHistoryAddress(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/createTaskHistoryAddress</b>
     * <p>添加物业服务历史地址</p>
     */
    @RequestMapping("createTaskHistoryAddress")
    @RestReturn(value=PmTaskHistoryAddressDTO.class)
    public RestResponse createTaskHistoryAddress(CreateTaskHistoryAddressCommand cmd) {

        RestResponse response = new RestResponse(pmTaskService.createTaskHistoryAddress(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/exportTasksCard</b>
     * <p>导出维修单</p>
     */
    @RequestMapping("exportTasksCard")
    @RestReturn(value = String.class)
    public RestResponse exportTasksCard(ExportTasksCardCommand cmd, HttpServletResponse response) {

        pmTaskService.exportTasksCard(cmd, response);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
}