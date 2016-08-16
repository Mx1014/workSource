// @formatter:off
package com.everhomes.pmtask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.organization.AssginOrgTopicCommand;
import com.everhomes.rest.organization.CheckOfficalPrivilegeBySceneCommand;
import com.everhomes.rest.organization.CheckOfficalPrivilegeCommand;
import com.everhomes.rest.organization.CheckOfficalPrivilegeResponse;
import com.everhomes.rest.organization.ListTopicsByTypeCommand;
import com.everhomes.rest.organization.ListTopicsByTypeCommandResponse;
import com.everhomes.rest.organization.ListUserTaskCommand;
import com.everhomes.rest.organization.SearchTopicsByTypeCommand;
import com.everhomes.rest.organization.SearchTopicsByTypeResponse;
import com.everhomes.rest.organization.SetOrgTopicStatusCommand;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CommunityTaskDTO;
import com.everhomes.rest.pmtask.CreateCategoryCommand;
import com.everhomes.rest.pmtask.CreateNewTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListCategoriesCommand;
import com.everhomes.rest.pmtask.ListCategoriesResponse;
import com.everhomes.rest.pmtask.ListMyTasksCommand;
import com.everhomes.rest.pmtask.ListMyTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.SetTaskStatusCommand;
import com.everhomes.search.OrganizationSearcher;

@RestController
@RequestMapping("/pmtask")
public class PmTaskController extends ControllerBase {

	@Autowired
	private PmTaskService pmTaskService;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;

//    /**
//     * <b>URL: /org/listUserTask</b>
//     * <p>查询分配给自己的任务</p>
//     */
//    @RequestMapping("listUserTask")
//    @RestReturn(value=ListTopicsByTypeCommandResponse.class)
//    public RestResponse listUserTask(ListUserTaskCommand cmd){
//    	ListTopicsByTypeCommandResponse tasks = this.pmTaskService.listUserTask(cmd);
//        
//        RestResponse response = new RestResponse(tasks);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    /**
//     * <b>URL: /org/searchTopicsByType</b>
//     * <p>搜索任务贴</p>
//     */
//    @RequestMapping("searchTopicsByType")
//	@RestReturn(value=SearchTopicsByTypeResponse.class)
//	public RestResponse searchTopicsByType(SearchTopicsByTypeCommand cmd) {
//    	SearchTopicsByTypeResponse result= pmTaskService.searchTopicsByType(cmd);
//		RestResponse response = new RestResponse(result);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
//      /**
//       * <b>URL: /org/checkOfficalPrivilegeByScene</b>
//       * <p>检查是否具有官方的权限</p>
//       */
//      @RequestMapping("checkOfficalPrivilegeByScene")
//      @RestReturn(value=CheckOfficalPrivilegeResponse.class)
//      public RestResponse checkOfficalPrivilegeByScene(CheckOfficalPrivilegeBySceneCommand cmd){
//    	  RestResponse res = new RestResponse(pmTaskService.checkOfficalPrivilegeByScene(cmd));
//          res.setErrorCode(ErrorCodes.SUCCESS);
//          res.setErrorDescription("OK");
//          
//          return res;
//      }
//      
//      /**
//       * <b>URL: /org/checkOfficalPrivilege</b>
//       * <p>检查是否具有官方的权限</p>
//       */
//      @RequestMapping("checkOfficalPrivilege")
//      @RestReturn(value=CheckOfficalPrivilegeResponse.class)
//      public RestResponse checkOfficalPrivilege(CheckOfficalPrivilegeCommand cmd){
//    	  RestResponse res = new RestResponse(pmTaskService.checkOfficalPrivilege(cmd));
//          res.setErrorCode(ErrorCodes.SUCCESS);
//          res.setErrorDescription("OK");
//          
//          return res;
//      }
      
      /*------------------------------------------------- */
      
      /**
       * <b>URL: /org/listTaskCategories</b>
       * <p>获取服务类型列表</p>
       */
      @RequestMapping("listTaskCategories")
      @RestReturn(value=ListTaskCategoriesResponse.class)
      public RestResponse listTaskCategories(ListTaskCategoriesCommand cmd) {
    	  ListTaskCategoriesResponse  res = pmTaskService.listTaskCategories(cmd);
          RestResponse response = new RestResponse(res);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /org/listTaskCategories</b>
       * <p>新建服务类型</p>
       */
      @RequestMapping("listTaskCategories")
      @RestReturn(value=ListPostCommandResponse.class)
      public RestResponse createTaskCategory(CreateTaskCategoryCommand cmd) {
      	  pmTaskService.createTaskCategory(cmd);
          RestResponse response = new RestResponse();
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /org/listTaskCategories</b>
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
       * <b>URL: /org/listCategories</b>
       * <p>获取类型列表</p>
       */
      @RequestMapping("listCategories")
      @RestReturn(value=ListCategoriesResponse.class)
      public RestResponse listCategories(ListCategoriesCommand cmd) {
    	  ListCategoriesResponse res = pmTaskService.listCategories(cmd);
          RestResponse response = new RestResponse(res);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /org/listTaskCategories</b>
       * <p>新建类型</p>
       */
      @RequestMapping("createCategory")
      @RestReturn(value=String.class)
      public RestResponse createCategory(CreateCategoryCommand cmd) {
      	  pmTaskService.createCategory(cmd);
          RestResponse response = new RestResponse();
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /org/listTaskCategories</b>
       * <p>删除类型</p>
       */
      @RequestMapping("deleteCategory")
      @RestReturn(value=ListPostCommandResponse.class)
      public RestResponse deleteCategory(DeleteCategoryCommand cmd) {
      	  pmTaskService.deleteCategory(cmd);
          RestResponse response = new RestResponse();
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /org/createNewTask</b>
       * <p>创建新任务</p>
       */
      @RequestMapping("createNewTask")
      @RestReturn(value=String.class)
      public RestResponse createNewTask(CreateNewTaskCommand cmd) {
      	  pmTaskService.createNewTask(cmd);
          RestResponse response = new RestResponse();
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
      /**
       * <b>URL: /org/getTaskDetail</b>
       * <p>获取任务详情</p>
       */
      @RequestMapping("getTaskDetail")
      @RestReturn(value=CommunityTaskDTO.class)
      public RestResponse getTaskDetail(GetTaskDetailCommand cmd) {
    	  CommunityTaskDTO  res = pmTaskService.getTaskDetail(cmd);
          RestResponse response = new RestResponse(res);
          response.setErrorCode(ErrorCodes.SUCCESS);
          response.setErrorDescription("OK");
          return response;
      }
      
  	/**
  	 * <b>URL: /org/assignTask</b>
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
     
	//8. 报修贴修改状态
	/**
	 * <b>URL: /org/setTaskStatus</b>
	 * <p>设置任务状态：待处理、处理中、已处理、其它</p>
	 */
	@RequestMapping("setTaskStatus")
	@RestReturn(value=String.class)
	public RestResponse setTaskStatus(SetTaskStatusCommand cmd) {
		pmTaskService.setTaskStatus(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /org/evaluateTask</b>
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
     * <b>URL: /org/listMyTasks</b>
     * <p>获取任务列表</p>
     */
    @RequestMapping("listMyTasks")
    @RestReturn(value=ListMyTasksResponse.class)
    public RestResponse listMyTasks(ListMyTasksCommand cmd) {
    	ListMyTasksResponse  res = pmTaskService.listMyTasks(cmd);
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
      
}