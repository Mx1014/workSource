// @formatter:off
package com.everhomes.pmtask;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.entity.EntityType;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.paySDK.pojo.PayOrderDTO;
import com.everhomes.pmtask.zhuzong.ZhuzongAddresses;
import com.everhomes.pmtask.zhuzong.ZhuzongCreateTask;
import com.everhomes.pmtask.zhuzong.ZhuzongTaskDetail;
import com.everhomes.pmtask.zhuzong.ZhuzongTasks;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommandResponse;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmtask.*;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.RequireAuthentication;
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

	@Autowired
    private PortalService portalService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

//	/**
//     * <b>URL: /pmtask/getPrivileges</b>
//     * <p>获取权限列表</p>
//     */
//    @RequestMapping("getPrivileges")
//    @RestReturn(value=GetPrivilegesDTO.class)
//    public RestResponse getPrivileges(GetPrivilegesCommand cmd) {
//  	    GetPrivilegesDTO res = pmTaskService.getPrivileges(cmd);
//        RestResponse response = new RestResponse(res);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
	
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
     * <b>URL: /pmtask/liskPmtaskBuildings</b>
     * <p>查询报修楼栋</p>
     */
    @RequestMapping("liskPmtaskBuildings")
    @RestReturn(value=ListBuildingCommandResponse.class)
    public RestResponse liskPmtaskBuildings(ListBuildingCommand cmd) {

        ListBuildingCommandResponse buildings = pmTaskService.listBuildings(cmd);
        RestResponse response = new RestResponse(buildings);
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
       * <p>获取任务详情-越空间</p>
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
      
//  	/**
//  	 * <b>URL: /pmtask/assignTask</b>
//  	 * <p>报修贴分配人员</p>
//  	 */
//  	@RequestMapping("assignTask")
//  	@RestReturn(value=String.class)
//  	public RestResponse assignTask(@Valid AssignTaskCommand cmd) {
//  		pmTaskService.assignTask(cmd);
//  		RestResponse response = new RestResponse();
//  		response.setErrorCode(ErrorCodes.SUCCESS);
//  		response.setErrorDescription("OK");
//  		return response;
//  	}
     
//	/**
//	 * <b>URL: /pmtask/completeTask</b>
//	 * <p>完成任务</p>
//	 */
//	@RequestMapping("completeTask")
//	@RestReturn(value=String.class)
//	public RestResponse completeTask(CompleteTaskCommand cmd) {
//		pmTaskService.completeTask(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
//	/**
//	 * <b>URL: /pmtask/closeTask</b>
//	 * <p>关闭任务</p>
//	 */
//	@RequestMapping("closeTask")
//	@RestReturn(value=String.class)
//	public RestResponse closeTask(CloseTaskCommand cmd) {
//		pmTaskService.closeTask(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
//	/**
//	 * <b>URL: /pmtask/cancelTask</b>
//	 * <p>取消任务</p>
//	 */
//	@RequestMapping("cancelTask")
//	@RestReturn(value=String.class)
//	public RestResponse cancelTask(CancelTaskCommand cmd) {
//		pmTaskService.cancelTask(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
//	/**
//	 * <b>URL: /pmtask/revisit</b>
//	 * <p>回访</p>
//	 */
//	@RequestMapping("revisit")
//	@RestReturn(value=String.class)
//	public RestResponse revisit(RevisitCommand cmd) {
//		pmTaskService.revisit(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
//	/**
//	 * <b>URL: /pmtask/evaluateTask</b>
//	 * <p>评价任务</p>
//	 */
//	@RequestMapping("evaluateTask")
//	@RestReturn(value=String.class)
//	public RestResponse evaluateTask(EvaluateTaskCommand cmd) {
//
//		pmTaskService.evaluateTask(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /pmtask/listOrganizationCommunityByUser</b>
	 * <p>获取机构人员 办公地点小区列表</p>
	 */
	@RequestMapping("listOrganizationCommunityByUser")
	@RestReturn(value=ListOrganizationCommunityByUserResponse.class)
	public RestResponse listOrganizationCommunityByUser(ListOrganizationCommunityByUserCommand cmd) {
        //TODO:
		ListAuthorizationCommunityByUserResponse resp = pmTaskService.listOrganizationCommunityByUser(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /pmtask/listOrganizationCommunityBySceneToken</b>
     * <p>获取机构人员 办公地点小区列表</p>
     */
    @RequestMapping("listOrganizationCommunityBySceneToken")
    @RestReturn(value=ListOrganizationCommunityBySceneTokenResponse.class)
    public RestResponse listOrganizationCommunityBySceneToken(ListOrganizationCommunityBySceneTokenCommand cmd) {
        ListOrganizationCommunityBySceneTokenResponse resp = pmTaskService.listOrganizationCommunityBySceneToken(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/listAuthorizationCommunityByUser</b>
     * <p>授权人员 管理小区列表</p>
     */
//    @RequestMapping("listAuthorizationCommunityByUser")
//    @RestReturn(value=ListAuthorizationCommunityByUserResponse.class)
//    public RestResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
//        //TODO:
//        ListAuthorizationCommunityByUserResponse resp = pmTaskService.listAuthorizationCommunityByUser(cmd);
//        RestResponse response = new RestResponse(resp);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }


//	/**
//	 * <b>URL: /pmtask/getTaskLog</b>
//	 * <p>获取任务完成详情或任务关闭理由</p>
//	 */
//	@RequestMapping("getTaskLog")
//	@RestReturn(value=PmTaskLogDTO.class)
//	public RestResponse getTaskLog(GetTaskLogCommand cmd) {
//		PmTaskLogDTO dto = pmTaskService.getTaskLog(cmd);
//		RestResponse response = new RestResponse(dto);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
  	
	/**
     * <b>URL: /pmtask/listUserTasks</b>
     * <p>获取任务列表-越空间使用</p>
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
     * <b>URL: /pmtask/searchTasksWithoutAuth</b>
     * <p>搜索任务(无权限校验)</p>
     */
    @RequestMapping("searchTasksWithoutAuth")
    @RestReturn(value=SearchTasksResponse.class)
    public RestResponse searchTasksWithoutAuth(SearchTasksCommand cmd) {
        SearchTasksResponse res = pmTaskService.searchTasksWithoutAuth(cmd);
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
     * <b>URL: /pmtask/getIfHideRepresent</b>
     * <p>判断是否隐藏代发按钮</p>
     */
    @RequestMapping("getIfHideRepresent")
    @RestReturn(value=GetIfHideRepresentResponse.class)
    public RestResponse getIfHideRepresent(GetIfHideRepresentCommand cmd) {
        GetIfHideRepresentResponse dto = pmTaskService.getIfHideRepresent(cmd);
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

//    /**
//     * <b>URL: /pmtask/synchronizedData</b>
//     * <p>迁移数据</p>
//     */
//    @RequestMapping("synchronizedData")
//    @RestReturn(value=String.class)
//    public RestResponse synchronizedData(SearchTasksCommand cmd) {
//        pmTaskService.synchronizedData(cmd);
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    
    /**
     * <b>URL: /pmtask/syncTaskStatistics</b>
     * <p>统计</p>
     */
    @RequestMapping("syncTaskStatistics")
    public void createStatistics(HttpServletResponse resp) {
    	pmTaskService.syncTaskStatistics(resp);
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

    /**
     * <b>URL: /pmtask/listApartmentsByBuildingName</b>
     * <p>根据小区Id、楼栋号查询门牌列表</p>
     */
    @RequestMapping("listApartmentsByBuildingName")
    @RestReturn(value=ListApartmentByBuildingNameCommandResponse.class)
    public RestResponse listApartmentsByBuildingName(@Valid ListApartmentByBuildingNameCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        ListApartmentByBuildingNameCommandResponse result = this.pmTaskService.listApartmentsByBuildingName(cmd);
        RestResponse resp = new RestResponse();
        if(EtagHelper.checkHeaderEtagOnly(30,result.hashCode()+"", request, response)) {
            resp.setResponseObject(result);
        }

        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pmtask/changeTaskState</b>
     * <p>提供给一碑的回调接口</p>
     */
    @RequestMapping("changeTaskState")
    @RestReturn(value = String.class)
    public RestResponse changeTaskState(UpdateTasksStatusCommand cmd){
        pmTaskService.changeTasksStatus(cmd);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

     /*---------------------------- start 以下接口是服务器转发 调用第三方报修系统----------------------------------*/
    /**
     * <b>URL: /pmtask/queryThirdAddress</b>
     * <p>获取第三方地址信息</p>
     */
    @RequestMapping("queryThirdAddress")
    @RestReturn(value = String.class)
    public RestResponse queryThirdAddress(HttpServletRequest req){
        Object addresses = pmTaskService.getThirdAddress(req);
        RestResponse resp = new RestResponse(addresses);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pmtask/createThirdTask</b>
     * <p>创建第三方报修</p>
     */
    @RequestMapping("createThirdTask")
    @RestReturn(value = String.class)
    public RestResponse createThirdTask(HttpServletRequest req){
        Object task = pmTaskService.createThirdTask(req);
        RestResponse resp = new RestResponse(task);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pmtask/listThirdTasks</b>
     * <p>获取第三方报修列表</p>
     */
    @RequestMapping("listThirdTasks")
    @RestReturn(value = String.class)
    public RestResponse listThirdTasks(HttpServletRequest req){
        Object tasks = pmTaskService.listThirdTasks(req);
        RestResponse resp = new RestResponse(tasks);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pmtask/getThirdTaskDetail</b>
     * <p>获取第三方报修详情</p>
     */
    @RequestMapping("getThirdTaskDetail")
    @RestReturn(value = String.class)
    public RestResponse getThirdTaskDetail(HttpServletRequest req){
        Object detail = pmTaskService.getThirdTaskDetail(req);
        RestResponse resp = new RestResponse(detail);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pmtask/queryThirdCategories</b>
     * <p>获取第三方地址信息</p>
     */
    @RequestMapping("queryThirdCategories")
    @RestReturn(value = String.class)
    public RestResponse queryThirdCategories(HttpServletRequest req){
        Object addresses = pmTaskService.getThirdCategories(req);
        RestResponse resp = new RestResponse(addresses);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /pmtask/syncCategories</b>
     * <p>给不同项目拷贝分类(用后删除)</p>
     */
    @RequestMapping("syncCategories")
    @RestReturn(value=String.class)
    public RestResponse syncCategories() {
        pmTaskService.syncCategories();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /*---------------------------- start 以下接口是为了给客户端打rest包，已经废弃----------------------------------*/
	/**
     * <b>URL: /pmtask/getPrivileges</b>
     * <p>获取权限列表</p>
     */
    @RequestMapping("getPrivileges")
    @RestReturn(value=GetPrivilegesDTO.class)
    @Deprecated
    public RestResponse getPrivileges(GetPrivilegesCommand cmd) {
        RestResponse response = new RestResponse();
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
    @Deprecated
    public RestResponse listOperatePersonnels(ListOperatePersonnelsCommand cmd){
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }
    /**
     * <b>URL: /pmtask/testcheck</b>
     * <p>提供给一碑的回调接口</p>
     */
    @RequestMapping("testcheck")
    @RestReturn(value = String.class)
    public RestResponse testcheck(SearchTasksCommand cmd){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Boolean flag =false;
        if (null != cmd.getTaskCategoryId()) {
            ListServiceModuleAppsCommand listServiceModuleAppsCommand = new ListServiceModuleAppsCommand();
            listServiceModuleAppsCommand.setNamespaceId(namespaceId);
            listServiceModuleAppsCommand.setModuleId(FlowConstants.PM_TASK_MODULE);
            listServiceModuleAppsCommand.setCustomTag(String.valueOf(cmd.getTaskCategoryId()));
            ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(listServiceModuleAppsCommand);
//            if (null != apps && null != apps.getServiceModuleApps() && apps.getServiceModuleApps().size() > 0) {
//                flag = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOwnerId(), cmd.getCurrentOrgId(), PrivilegeConstants.PMTASK_LIST, apps.getServiceModuleApps().get(0).getOriginId(), null,cmd.getCurrentCommunityId());
//                System.out.print(flag) ;
//            }
        }
        RestResponse resp = new RestResponse(flag);
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

/*------------------------------- 3.5报修统计 -------------------------------*/
    /**
     * <b>URL: /pmtask/getStatSurvey</b>
     * <p>根据项目Id与起止时间查询统计概况</p>
     */
    @RequestMapping("getStatSurvey")
    @RestReturn(value=PmTaskStatDTO.class)
    public RestResponse getStatSurvey(GetTaskStatCommand cmd){
        PmTaskStatDTO res = pmTaskService.getStatSurvey(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/getStatByCategory</b>
     * <p>根据服务类型统计报修数据</p>
     */
    @RequestMapping("getStatByCategory")
    @RestReturn(value=PmTaskStatSubDTO.class,collection = true)
    public RestResponse getStatByCategory(GetTaskStatCommand cmd){
        List<PmTaskStatSubDTO> res = pmTaskService.getStatByCategory(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/getStatByCreator</b>
     * <p>根据来源统计报修数据</p>
     */
    @RequestMapping("getStatByCreator")
    @RestReturn(value=PmTaskStatDTO.class,collection = true)
    public RestResponse getStatByCreator(GetTaskStatCommand cmd){
        List<PmTaskStatDTO> res = pmTaskService.getStatByCreator(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/getStatByStatus</b>
     * <p>根据状态统计报修数据</p>
     */
    @RequestMapping("getStatByStatus")
    @RestReturn(value=PmTaskStatDTO.class,collection = true)
    public RestResponse getStatByStatus(GetTaskStatCommand cmd){
        List<PmTaskStatDTO> res = pmTaskService.getStatByStatus(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/getStatByArea</b>
     * <p>根据区域统计报修数据</p>
     */
    @RequestMapping("getStatByArea")
    @RestReturn(value=PmTaskStatSubDTO.class,collection = true)
    public RestResponse getStatByArea(GetTaskStatCommand cmd){
        List<PmTaskStatSubDTO> res = pmTaskService.getStatByArea(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/exportTaskStat</b>
     * <p>导出统计列表</p>
     */
    @RequestMapping("exportTaskStat")
    public void exportTaskStat(GetTaskStatCommand cmd, HttpServletResponse resp) {
        pmTaskService.exportTaskStat(cmd, resp);
    }

    /**
     * <b>URL: /pmtask/searchOrgTasks</b>
     * <p>查询企业所有的报修</p>
     */
    @RequestMapping("searchOrgTasks")
    @RestReturn(value=SearchTasksByOrgDTO.class,collection = true)
    public RestResponse searchOrgasks(SearchOrgTasksCommand cmd) {
        List<SearchTasksByOrgDTO> res = pmTaskService.searchOrgTasks(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


/*------------------------------- 3.6评价统计 -------------------------------*/
    /**
     * <b>URL: /pmtask/getEvalStat</b>
     * <p>查询项目评价统计</p>
     */
    @RequestMapping("getEvalStat")
    @RestReturn(value=PmTaskEvalStatDTO.class,collection = true)
    public RestResponse getEvalStat(GetEvalStatCommand cmd){
        List<PmTaskEvalStatDTO> res = pmTaskService.getEvalStat(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

/*------------------------------- 3.7 -------------------------------*/
    /**
     * <b>URL: /pmtask/admin/setPmTaskConfig</b>
     * <p>管理-通用设置</p>
     */
    @RequestMapping("admin/setPmTaskConfig")
    @RestReturn(value=PmTaskConfigDTO.class)
    public RestResponse setPmTaskConfig(SetPmTaskConfigCommand cmd) {
        RestResponse response = new RestResponse(pmTaskService.setPmTaskConfig(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/admin/getPmTaskConfig</b>
     * <p>管理-通用设置查询</p>
     */
    @RequestMapping("admin/getPmTaskConfig")
    @RestReturn(value=PmTaskConfigDTO.class)
    public RestResponse getPmTaskConfig(GetPmTaskConfigCommand cmd) {
        PmTaskConfigDTO dto = pmTaskService.searchPmTaskConfigByProject(cmd);
        RestResponse response = new RestResponse(dto == null ? new PmTaskConfigDTO() : dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/createOrderDetails</b>
     * <p>新增费用清单</p>
     */
    @RequestMapping("createOrderDetails")
    @RestReturn(value=String.class)
    public RestResponse createOrderDetails(CreateOrderDetailsCommand cmd) {
        pmTaskService.createOrderDetails(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/modifyOrderDetails</b>
     * <p>修改费用清单</p>
     */
    @RequestMapping("modifyOrderDetails")
    @RestReturn(value=String.class)
    public RestResponse modifyOrderDetails(CreateOrderDetailsCommand cmd) {
        pmTaskService.modifyOrderDetails(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/getOrderDetails</b>
     * <p>查询费用清单明细</p>
     */
    @RequestMapping("getOrderDetails")
    @RestReturn(value=PmTaskOrderDTO.class)
    public RestResponse getOrderDetails(GetOrderDetailsCommand cmd) {
        RestResponse response = new RestResponse(pmTaskService.searchOrderDetailsByTaskId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/syncOrderDetails</b>
     * <p>从工作流表单同步费用清单数据</p>
     */
    @RequestMapping("syncOrderDetails")
    @RestReturn(value=String.class)
    public RestResponse syncOrderDetails() {
        pmTaskService.syncOrderDetails();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/clearOrderDetails</b>
     * <p></p>
     */
    @RequestMapping("clearOrderDetails")
    @RestReturn(value=String.class)
    public RestResponse clearOrderDetails() {
        pmTaskService.clearOrderDetails();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pmtask/listPayeeAccounts</b>
     * <p>列出当前项目下所有的收款方账户</p>
     */
    @RequestMapping("listPayeeAccounts")
    @RestReturn(value = ListBizPayeeAccountDTO.class, collection = true)
    public RestResponse listPayeeAccounts(ListPayeeAccountsCommand cmd){
        List<ListBizPayeeAccountDTO> list = pmTaskService.listPayeeAccounts(cmd);
        RestResponse restResponse = new RestResponse(list);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /pmtask/payBills</b>
     * <p>支付</p>
     */
    @RequestMapping("payBills")
    @RestReturn(PreOrderDTO.class)
    public RestResponse payBills(CreatePmTaskOrderCommand cmd){
        PreOrderDTO response = pmTaskService.payBills(cmd);
        RestResponse restResponse = new RestResponse(response);
        restResponse.setErrorCode(ErrorCodes.SUCCESS);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /pmtask/payNotify</b>
     * <p>支付模块回调接口，通知支付结果</p>
     */
    @RequestMapping("payNotify")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse payNotify(@Valid OrderPaymentNotificationCommand cmd) {
        pmTaskService.payNotify(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /pmtask/listBills</b>
//     * <p>支付</p>
//     */
//    @RequestMapping("listBills")
//    @RestReturn(value = PayOrderDTO.class,collection = true)
//    public RestResponse listBills(ListBillsCommand cmd){
//        List<PayOrderDTO> response = pmTaskService.listBills(cmd);
//        RestResponse restResponse = new RestResponse(response);
//        restResponse.setErrorCode(ErrorCodes.SUCCESS);
//        restResponse.setErrorDescription("OK");
//        return restResponse;
//    }

}