package com.everhomes.yellowPage;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.servicehotline.GetChatGroupListCommand;
import com.everhomes.rest.servicehotline.GetChatGroupListResponse;
import com.everhomes.rest.servicehotline.GetChatRecordListCommand;
import com.everhomes.rest.servicehotline.GetChatRecordListResponse;
import com.everhomes.rest.yellowPage.*;
import com.everhomes.rest.yellowPage.stat.ClickTypeDTO;
import com.everhomes.rest.yellowPage.stat.ListClickStatCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailCommand;
import com.everhomes.rest.yellowPage.stat.ListClickStatDetailResponse;
import com.everhomes.rest.yellowPage.stat.ListClickStatResponse;
import com.everhomes.rest.yellowPage.stat.ListInterestStatResponse;
import com.everhomes.rest.yellowPage.stat.ListStatCommonCommand;
import com.everhomes.rest.yellowPage.stat.TestClickStatCommand;
import com.everhomes.rest.yellowPage.stat.ListServiceTypeNamesCommand;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.yellowPage.stat.AllianceClickStatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/yellowPage")
public class YellowPageController  extends ControllerBase {

    @Autowired
    private YellowPageService yellowPageService;

	@Autowired
	private AllianceOnlineServiceI allianceOnlineService;
	
	@Autowired
	private ServiceAllianceRequestInfoSearcher saRequestInfoSearcher;
	
	@Autowired
	private AllianceStandardService allianceStandardService;


	@Autowired
	AllianceClickStatService allianceClickStatService;

	@RequireAuthentication(false)
    @RequestMapping("getYellowPageDetail")
    @RestReturn(value=YellowPageDTO.class)
    public RestResponse getYellowPageDetail(@Valid GetYellowPageDetailCommand cmd) {
    	YellowPageDTO res = this.yellowPageService.getYellowPageDetail(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }

    /**
	 * <b>URL: /yellowPage/getYellowPageTopic</b>
	 * <p>获取黄页topic-创客空间,服务联盟等 </p>
	 */
    @RequireAuthentication(false)
    @RequestMapping("getYellowPageTopic")
    @RestReturn(value=YellowPageDTO.class)
    public RestResponse getYellowPageTopic(@Valid GetYellowPageTopicCommand cmd) {
    	YellowPageDTO res = this.yellowPageService.getYellowPageTopic(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    @RequireAuthentication(false)
    @RequestMapping("getYellowPageList")
    @RestReturn(value=YellowPageListResponse.class)
    public RestResponse getYellowPageList(@Valid GetYellowPageListCommand cmd) {
    	YellowPageListResponse res = this.yellowPageService.getYellowPageList(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    
    @RequestMapping("addYellowPage")
    @RestReturn(value=String.class)
    public RestResponse addYellowPage(@Valid AddYellowPageCommand cmd) {
    	 this.yellowPageService.addYellowPage(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    @RequestMapping("deleteYellowPage")
    @RestReturn(value=String.class)
    public RestResponse deleteYellowPage(@Valid DeleteYellowPageCommand cmd) {
    	 this.yellowPageService.deleteYellowPage(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }

    /**
	 * <b>URL: /yellowPage/updateYellowPage</b>
	 * <p>更新黄页 </p>
	 */
    
    @RequestMapping("updateYellowPage")
    @RestReturn(value=String.class)
    public RestResponse updateYellowPage(@Valid UpdateYellowPageCommand cmd) {
    	 this.yellowPageService.updateYellowPage(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    
    /**
	 * <b>URL: /yellowPage/updateServiceAllianceCategory</b>
	 * <p> 新建或修改服务联盟类型 </p>
	 */
    @RequestMapping("updateServiceAllianceCategory")
    @RestReturn(value=String.class)
    public RestResponse updateServiceAllianceCategory(@Valid UpdateServiceAllianceCategoryCommand cmd) {
    	 this.yellowPageService.updateServiceAllianceCategory(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/deleteServiceAllianceCategory</b>
   	 * <p> 删除服务联盟类型 </p>
   	 */
    @RequestMapping("deleteServiceAllianceCategory")
    @RestReturn(value=String.class)
    public RestResponse deleteServiceAllianceCategory(@Valid DeleteServiceAllianceCategoryCommand cmd) {
   	 
    	this.yellowPageService.deleteServiceAllianceCategory(cmd);
   	 
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
   }
    
    /**
	 * <b>URL: /yellowPage/listServiceAllianceCategories</b> 
	 * <p> 列出服务联盟类型，前端使用 </p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("listServiceAllianceCategories")
	@RestReturn(value = ServiceAllianceCategoryDTO.class, collection = true)
	public RestResponse listServiceAllianceCategories(ListServiceAllianceCategoriesCommand cmd) {
		return new RestResponse(yellowPageService.listServiceAllianceCategories(cmd));
	}

    /**
	 * <b>URL: /yellowPage/listServiceAllianceCategoriesAdmin</b>
	 * <p> 列出服务联盟类型，后台使用 </p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("listServiceAllianceCategoriesAdmin")
	@RestReturn(value=ListServiceAllianceCategoriesAdminResponse.class)
	public RestResponse listServiceAllianceCategoriesAdmin(ListServiceAllianceCategoriesCommand cmd) {
		return new RestResponse(yellowPageService.listServiceAllianceCategoriesByAdmin(cmd));
	}
    
    /**
	 * <b>URL: /yellowPage/getServiceAllianceDisplayMode</b>
	 * <p>获取服务联盟机构的展示类型</p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("getServiceAllianceDisplayMode")
	@RestReturn(value = ServiceAllianceDisplayModeDTO.class)
	public RestResponse getServiceAllianceDisplayMode(GetServiceAllianceDisplayModeCommand cmd) {
		return new RestResponse(yellowPageService.getServiceAllianceDisplayMode(cmd));
	}

    /**
   	 * <b>URL: /yellowPage/getServiceAllianceEnterpriseDetail</b>
   	 * <p> 服务联盟企业详情 </p>
   	 */
    @RequireAuthentication(false)
    @RequestMapping("getServiceAllianceEnterpriseDetail")
    @RestReturn(value=ServiceAllianceDTO.class)
    public RestResponse getServiceAllianceEnterpriseDetail(@Valid GetServiceAllianceEnterpriseDetailCommand cmd) {
    	
    	ServiceAllianceDTO res = this.yellowPageService.getServiceAllianceEnterpriseDetail(cmd);
    	 
    	RestResponse response = new RestResponse(res);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }

    /**
   	 * <b>URL: /yellowPage/getServiceAlliance</b>
   	 * <p> 客户端/前端获取服务联盟首页 </p>
   	 */
    @RequireAuthentication(false)
    @RequestMapping("getServiceAlliance")
    @RestReturn(value=ServiceAllianceDTO.class)
    public RestResponse getServiceAlliance(@Valid GetServiceAllianceCommand cmd) {
    	ServiceAllianceDTO res = this.yellowPageService.getServiceAlliance(cmd);
    	 RestResponse response = new RestResponse(res);
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/ListServiceAllianceEnterprise</b>
   	 * <p> 服务联盟企业列表 </p>
   	 */
    @RequireAuthentication(false)
    @RequestMapping("ListServiceAllianceEnterprise")
    @RestReturn(value=ServiceAllianceListResponse.class)
    public RestResponse ListServiceAllianceEnterprise(@Valid GetServiceAllianceEnterpriseListCommand cmd) {
    	
    	ServiceAllianceListResponse res = this.yellowPageService.getServiceAllianceEnterpriseList(cmd);
    	 
    	RestResponse response = new RestResponse(res);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/updateServiceAlliance</b>
   	 * <p> 新增或修改服务联盟首页 </p>
   	 */
    @RequestMapping("updateServiceAlliance")
    @RestReturn(value=String.class)
    public RestResponse updateServiceAlliance(@Valid UpdateServiceAllianceCommand cmd) {
    	 this.yellowPageService.updateServiceAlliance(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/deleteServiceAllianceEnterprise</b>
   	 * <p> 删除服务联盟企业 </p>
   	 */
    @RequestMapping("deleteServiceAllianceEnterprise")
    @RestReturn(value=String.class)
    public RestResponse deleteServiceAllianceEnterprise(@Valid DeleteServiceAllianceEnterpriseCommand cmd) {
    	 this.yellowPageService.deleteServiceAllianceEnterprise(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }

    /**
   	 * <b>URL: /yellowPage/updateServiceAllianceEnterprise</b>
   	 * <p> 新增或修改服务联盟企业 </p>
   	 */
    @RequestMapping("updateServiceAllianceEnterprise")
    @RestReturn(value=String.class)
    @XssExclude
    public RestResponse updateServiceAllianceEnterprise(@Valid UpdateServiceAllianceEnterpriseCommand cmd) {
    	 this.yellowPageService.updateServiceAllianceEnterprise(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/updateServiceAllianceEnterpriseDefaultOrder</b>
   	 * <p> 更新服务联盟企业顺序 </p>
   	 */
    @RequestMapping("updateServiceAllianceEnterpriseDefaultOrder")
    @RestReturn(value=ServiceAllianceListResponse.class)
    public RestResponse updateServiceAllianceEnterpriseDefaultOrder(@Valid UpdateServiceAllianceEnterpriseDefaultOrderCommand cmd) {
    	 RestResponse response = new RestResponse(this.yellowPageService.updateServiceAllianceEnterpriseDefaultOrder(cmd));
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/updateServiceAllianceEnterpriseDisplayFlag</b>
   	 * <p> 更新服务联盟企业是否显示在app端  </p>
   	 */
    @RequestMapping("updateServiceAllianceEnterpriseDisplayFlag")
    @RestReturn(value=String.class)
    public RestResponse updateServiceAllianceEnterpriseDisplayFlag(@Valid UpdateServiceAllianceEnterpriseDisplayFlagCommand cmd) {
    	 this.yellowPageService.updateServiceAllianceEnterpriseDisplayFlag(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
	 * <b>URL: /yellowPage/addNotifyTarget</b>
	 * <p> 增加推送接收管理员</p>
	 */
    @RequestMapping("addNotifyTarget")
    @RestReturn(value = String.class)
    public RestResponse addTarget(@Valid AddNotifyTargetCommand cmd) {
    	
    	this.yellowPageService.addTarget(cmd);
    	 
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
	 * <b>URL: /yellowPage/deleteNotifyTarget</b>
	 * <p> 删除推送接收管理员</p>
	 */
    @RequestMapping("deleteNotifyTarget")
    @RestReturn(value = String.class)
    public RestResponse deleteNotifyTarget(@Valid DeleteNotifyTargetCommand cmd) {
    	
    	this.yellowPageService.deleteNotifyTarget(cmd);
		 
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
	 * <b>URL: /yellowPage/setNotifyTargetStatus</b>
	 * <p> 设置接收管理员推送开启状态</p>
	 */
    @RequestMapping("setNotifyTargetStatus")
    @RestReturn(value = String.class)
    public RestResponse setNotifyTargetStatus(@Valid SetNotifyTargetStatusCommand cmd) {

    	this.yellowPageService.setNotifyTargetStatus(cmd);
    	
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
	 * <b>URL: /yellowPage/listNotifyTargets</b>
	 * <p> 获取推送接收管理员列表</p>
	 */
    @RequestMapping("listNotifyTargets")
    @RestReturn(value = ListNotifyTargetsResponse.class)
    public RestResponse listNotifyTargets(@Valid ListNotifyTargetsCommand cmd) {

    	ListNotifyTargetsResponse resp = this.yellowPageService.listNotifyTargets(cmd);
    	
    	RestResponse response = new RestResponse(resp);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }

    /**
     * <b>URL: /yellowPage/listJumpModules</b>
     * <p> 获取跳转模块列表</p>
     */
    @RequestMapping("listJumpModules")
    @RestReturn(value = JumpModuleDTO.class, collection = true)
    public RestResponse listJumpModules(ListJumpModulesCommand cmd) {

        List<JumpModuleDTO> resp = this.yellowPageService.listJumpModules(cmd);

        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /yellowPage/verifyNotifyTarget</b>
	 * <p> 检验手机号是否已存在且是否注册</p>
	 */
    @RequestMapping("verifyNotifyTarget")
    @RestReturn(value = String.class)
    public RestResponse verifyNotifyTarget(@Valid VerifyNotifyTargetCommand cmd) {

    	this.yellowPageService.verifyNotifyTarget(cmd);

    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }

    /**
     * <b>URL: /yellowPage/searchRequestInfo</b>
     * <p> 搜索申请信息</p>
     */
    @RequestMapping("searchRequestInfo")
    @RestReturn(value = SearchRequestInfoResponse.class)
    public RestResponse searchRequestInfo(@Valid SearchRequestInfoCommand cmd) {
        SearchRequestInfoResponse resp = this.saRequestInfoSearcher.searchRequestInfo(cmd);

        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /yellowPage/exportRequestInfo</b>
     * <p>导出申请信息</p>
     */
    @RequestMapping("exportRequestInfo")
    @RestReturn(value=String.class)
    public RestResponse exportRequestInfo(@Valid SearchRequestInfoCommand cmd, HttpServletResponse resp) {
        this.saRequestInfoSearcher.exportRequestInfo(cmd, resp);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /yellowPage/searchOneselfRequestInfo</b>
     * <p> 搜索自己的申请信息-app</p>
     */
    @RequestMapping("searchOneselfRequestInfo")
    @RestReturn(value = SearchRequestInfoResponse.class)
    public RestResponse searchOneselfRequestInfo(@Valid SearchOneselfRequestInfoCommand cmd) {
        SearchRequestInfoResponse resp = this.saRequestInfoSearcher.searchOneselfRequestInfo(cmd);

        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /yellowPage/searchOrgRequestInfo</b>
     * <p> 搜索企业申请信息</p>
     */
    @RequestMapping("searchOrgRequestInfo")
    @RestReturn(value = SearchRequestInfoResponse.class)
    public RestResponse searchOrgRequestInfo(@Valid SearchOrgRequestInfoCommand cmd) {
        SearchRequestInfoResponse resp = this.saRequestInfoSearcher.searchOrgRequestInfo(cmd);

        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /yellowPage/listAttachments</b>
     * <p> 查询服务机构附件列表 </p>
     */
    @RequestMapping("listAttachments")
    @RestReturn(value = ListAttachmentsResponse.class)
    public RestResponse listAttachments(ListAttachmentsCommand cmd) {
    	ListAttachmentsResponse resp = this.yellowPageService.listAttachments(cmd);
    	
    	RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/getCategoryIdByEntryId</b> 
   	 * <p> 通过机构 </p>
   	 */
   	@RequestMapping("getCategoryIdByEntryId")
   	@RestReturn(value = GetCategoryIdByEntryIdResponse.class)
   	public RestResponse getCategoryIdByEntryId(GetCategoryIdByEntryIdCommand cmd) {
   		return new RestResponse(yellowPageService.getCategoryIdByEntryId(cmd));
   	}

    /**
   	 * <b>URL: /yellowPage/syncSARequestInfo</b>
   	 * <p> 同步申请信息</p>
   	 */
       @RequestMapping("syncSARequestInfo")
       @RestReturn(value = String.class)
       public RestResponse syncSARequestInfo() {
       	
       	this.saRequestInfoSearcher.syncFromDb();
       	 
       	RestResponse response = new RestResponse();
       	response.setErrorCode(ErrorCodes.SUCCESS);
       	response.setErrorDescription("OK");
       	return response;
       }

       
   /**
  	 * <b>URL: /yellowPage/syncOldForm</b>
  	 * <p> 同步旧表单到自定义表单信息</p>
  	 */
      @RequestMapping("syncOldForm")
      @RestReturn(value = String.class)
      @Deprecated
      public RestResponse syncOldForm() {
      	
      	this.yellowPageService.syncOldForm();
      	 
      	RestResponse response = new RestResponse();
      	response.setErrorCode(ErrorCodes.SUCCESS);
      	response.setErrorDescription("OK");
      	return response;
      }
      
      /**
    	 * <b>URL: /yellowPage/syncServiceAllianceApplicationRecords</b>
    	 * <p> 申请记录迁移到表中存储</p>
    	 */
    @RequestMapping("syncServiceAllianceApplicationRecords")
    @RestReturn(value = String.class)
    @Deprecated
    public RestResponse syncServiceAllianceApplicationRecords() {
    	
    	this.yellowPageService.syncServiceAllianceApplicationRecords();
    	 
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    
    /**
   	 * <b>URL: /yellowPage/listServiceAllianceProviders</b> 
   	 * <p> 获取服务商  </p>
   	 */
   	@RequestMapping("listServiceAllianceProviders")
   	@RestReturn(value = ListServiceAllianceProvidersResponse.class)
   	public RestResponse listServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd) {
   		ListServiceAllianceProvidersResponse resp = yellowPageService.listServiceAllianceProviders(cmd);
    	RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   	
    /**
   	 * <b>URL: /yellowPage/addServiceAllianceProvider</b> 
   	 * <p> 增加服务商  </p>
   	 */
    @RequestMapping("addServiceAllianceProvider")
    @RestReturn(value=String.class)
    public RestResponse addServiceAllianceProvider(@Valid AddServiceAllianceProviderCommand cmd) {
    	 yellowPageService.addServiceAllianceProvider(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/deleteServiceAllianceProvider</b> 
   	 * <p> 删除服务商  </p>
   	 */
    @RequestMapping("deleteServiceAllianceProvider")
    @RestReturn(value=String.class)
    public RestResponse deleteServiceAllianceProvider(@Valid DeleteServiceAllianceProviderCommand cmd) {
    	 yellowPageService.deleteServiceAllianceProvider(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    
    /**
   	 * <b>URL: /yellowPage/updateServiceAllianceProvider</b> 
   	 * <p> 更新服务商  </p>
   	 */
    @RequestMapping("updateServiceAllianceProvider")
    @RestReturn(value=String.class)
    public RestResponse updateServiceAllianceProvider(@Valid UpdateServiceAllianceProviderCommand cmd) {
    	 yellowPageService.updateServiceAllianceProvider(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/applyExtraAllianceEvent</b> 
   	 * <p> 申请新的事件  用于工作流中新增</p>
   	 */
    @RequestMapping("applyExtraAllianceEvent")
    @RestReturn(value=String.class)
    public RestResponse applyExtraAllianceEvent(@Valid ApplyExtraAllianceEventCommand cmd) {
    	 yellowPageService.applyExtraAllianceEvent(cmd);
    	 RestResponse response = new RestResponse();
         response.setErrorCode(ErrorCodes.SUCCESS);
         response.setErrorDescription("OK");
         return response;
    }
    
    /**
   	 * <b>URL: /yellowPage/getExtraAllianceEvent</b> 
   	 * <p> 获取新建的事件</p>
   	 */
    @RequestMapping("getExtraAllianceEvent")
   	@RestReturn(value = GetExtraAllianceEventResponse.class)
	public RestResponse getExtraAllianceEvent(@Valid GetExtraAllianceEventCommand cmd) {
		GetExtraAllianceEventResponse resp = yellowPageService.getExtraAllianceEvent(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
    
    /**
   	 * <b>URL: /yellowPage/exportServiceAllianceProviders</b> 
   	 * <p> 导出服务商信息  </p>
   	 */
   	@RequestMapping("exportServiceAllianceProviders")
   	@RestReturn(value = String.class)
   	public RestResponse exportServiceAllianceProviders(ListServiceAllianceProvidersCommand cmd,  HttpServletResponse httpResp) {
   		yellowPageService.exportServiceAllianceProviders(cmd, httpResp);
        return new RestResponse();
    }
   	
    /**
   	 * <b>URL: /yellowPage/listOnlineServices</b> 
   	 * <p> 获取某个服务的客服列表  </p>
   	 */
   	@RequestMapping("listOnlineServices")
   	@RestReturn(value = ListOnlineServicesResponse.class)
	public RestResponse listOnlineServices(ListOnlineServicesCommand cmd) {
   		ListOnlineServicesResponse resp = yellowPageService.listOnlineServices(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
   	
   	
	/**
	 * <b>URL: /yellowPage/getChatGroupList</b>
     * <p>获取所有会话列表</p>
     * <p>根据条件获取会话列表。 相同的两个人的会话定义为一个会话。</p>
     * <p>例： 客服A与用户A的有100条聊天记录，归为 “客服A-用户A”一个会话</p>
	 * <p>客服A与用户B的有1条聊天记录，归为 “客服A-用户B”一个会话</p>
	 * <p>客服A与用户C的无聊天记录，不属于会话。</p>
	 */
	@RequestMapping("getChatGroupList")
	@RestReturn(value=GetChatGroupListResponse.class)
	public RestResponse getChatGroupList(@Valid GetChatGroupListCommand cmd) {
		GetChatGroupListResponse chatGroup = allianceOnlineService.getChatGroupList(cmd);
		RestResponse response =  new RestResponse(chatGroup);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /yellowPage/getChatRecordList</b>
     * <p>获取单个客服与用户的聊天记录</p>
	 */
	@RequestMapping("getChatRecordList")
	@RestReturn(value=GetChatRecordListResponse.class)
	public RestResponse getChatRecordList(@Valid GetChatRecordListCommand cmd) {
		GetChatRecordListResponse chatRecordList = allianceOnlineService.getChatRecordList(cmd);
		RestResponse response =  new RestResponse(chatRecordList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <b>URL: /yellowPage/exportChatRecordList</b>
     * <p>导出单个客服与用户的聊天记录</p>
	 */
    @RequestMapping("exportChatRecordList")
    @RestReturn(value = String.class)
	public RestResponse exportChatRecordList(GetChatRecordListCommand cmd, HttpServletResponse httpResponse) {
		allianceOnlineService.exportChatRecordList(cmd, httpResponse);
		return new RestResponse();
	}
    
	/**
	 * <b>URL: /yellowPage/exportMultiChatRecordList</b>
     * <p>导出多个客服与用户的聊天记录</p>
	 */
    @RequestMapping("exportMultiChatRecordList")
    @RestReturn(value = String.class)
	public RestResponse exportMultiChatRecordList(GetChatGroupListCommand cmd, HttpServletResponse httpResponse) {
		allianceOnlineService.exportMultiChatRecordList(cmd, httpResponse);
		return new RestResponse();
	}
    
    
	/**
	 * <b>URL: /yellowPage/updateAllianceTag</b>
	 * <p>
	 * 新建/修改标签
	 * </p>
	 */
	@RequestMapping("updateAllianceTag")
	@RestReturn(String.class)
	public RestResponse updateAllianceTag(UpdateAllianceTagCommand cmd){
		yellowPageService.updateAllianceTag(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /yellowPage/getAllianceTagList</b>
	 * <p>
	 * 查询标签
	 * </p>
	 */
	@RequestMapping("getAllianceTagList")
	@RestReturn(GetAllianceTagResponse.class)
	@RequireAuthentication(false)
	public RestResponse getAllianceTagList(GetAllianceTagCommand cmd){
		GetAllianceTagResponse resp = yellowPageService.getAllianceTagList(cmd);

		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /yellowPage/getFormList</b>
	 * <p>
	 * 获取某个项目下的表单列表
	 * </p>
	 */
	@RequestMapping("getFormList")
	@RestReturn(GetFormListResponse.class)
	public RestResponse getFormList(GetFormListCommand cmd){
		GetFormListResponse resp = allianceStandardService.getFormList(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /yellowPage/getWorkFlowList</b>
	 * <p>
	 * 获取某个项目下的工作流列表
	 * </p>
	 */
	@RequestMapping("getWorkFlowList")
	@RestReturn(GetWorkFlowListResponse.class)
	public RestResponse getWorkFlowList(GetWorkFlowListCommand cmd){
		GetWorkFlowListResponse resp = allianceStandardService.getWorkFlowList(cmd);
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /yellowPage/enableSelfDefinedConfig</b>
	 * <p>
	 * 开启自定义配置
	 * </p>
	 */
	@RequestMapping("enableSelfDefinedConfig")
	@RestReturn(String.class)
	public RestResponse enableSelfDefinedConfig(GetSelfDefinedStateCommand cmd){
		allianceStandardService.enableSelfDefinedConfig(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /yellowPage/disableSelfDefinedConfig</b>
	 * <p>
	 * 关闭自定义配置
	 * </p>
	 */
	@RequestMapping("disableSelfDefinedConfig")
	@RestReturn(String.class)
	public RestResponse disableSelfDefinedConfig(GetSelfDefinedStateCommand cmd){
		allianceStandardService.disableSelfDefinedConfig(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /yellowPage/getSelfDefinedState</b>
	 * <p>
	 * 获取自定义配置状态
	 * </p>
	 */
	@RequestMapping("getSelfDefinedState")
	@RestReturn(GetSelfDefinedStateResponse.class)
	public RestResponse getSelfDefinedState(GetSelfDefinedStateCommand cmd){
		RestResponse response = new RestResponse(allianceStandardService.getSelfDefinedState(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /*
	 * <b>URL: /yellowPage/listInterestStat</b>
	 * <p> 用户行为统计总览 </p>
	 */
	@RequestMapping("listInterestStat")
	@RestReturn(ListInterestStatResponse.class)
	public RestResponse listInterestStat(ListStatCommonCommand cmd) {
		return new RestResponse(allianceClickStatService.listInterestStat(cmd));
	}

    /**
	 * <b>URL: /yellowPage/exportInterestStat</b>
	 * <p> 导出用户行为统计总览 </p>
	 */
	@RequestMapping("exportInterestStat")
    @RestReturn(value = String.class)
	public RestResponse exportInterestStat(ListStatCommonCommand cmd, HttpServletRequest request, HttpServletResponse resp) {
		allianceClickStatService.exportInterestStat(cmd, request, resp);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

    /**
	 * <b>URL: /yellowPage/listClickStat</b>
	 * <p> 用户点击明细统计 </p>
	 */

	@RequestMapping("listClickStat")
	@RestReturn(ListClickStatResponse.class)
	public RestResponse listClickStat(ListClickStatCommand cmd) {
		return new RestResponse(allianceClickStatService.listClickStat(cmd));
	}

    /**
	 * <b>URL: /yellowPage/exportClickStat</b>
	 * <p> 导出用户点击明细统计 </p>
	 */
	@RequestMapping("exportClickStat")
    @RestReturn(value = String.class)
	public RestResponse exportClickStat(ListClickStatCommand cmd, HttpServletRequest request, HttpServletResponse resp) {
		allianceClickStatService.exportClickStat(cmd, request, resp);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

    /**
	 * <b>URL: /yellowPage/listClickStatDetail</b>
	 * <p> 用户点击明细 </p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("listClickStatDetail")
	@RestReturn(ListClickStatDetailResponse.class)
	public RestResponse listClickStatDetail(ListClickStatDetailCommand cmd) {
		return new RestResponse(allianceClickStatService.listClickStatDetail(cmd));
	}

    /**
	 * <b>URL: /yellowPage/exportClickStatDetail</b>
	 * <p> 导出用户点击明细 </p>
	 */
	@RequestMapping("exportClickStatDetail")
    @RestReturn(value = String.class)
	public RestResponse exportClickStatDetail(ListClickStatDetailCommand cmd, HttpServletRequest request, HttpServletResponse resp) {
		allianceClickStatService.exportClickStatDetail(cmd, request, resp);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

    /**
	 * <b>URL: /yellowPage/listClickTypes</b>
	 * <p> 获取所有点击类型 </p>
	 */
	@RequestMapping("listClickTypes")
    @RestReturn(value = ClickTypeDTO.class, collection = true)
	public RestResponse listClickTypes() {
        return new RestResponse(allianceClickStatService.listClickTypes());
	}

    /**
	 * <b>URL: /yellowPage/listServiceNames</b>
	 * <p> 获取所有服务名称 </p>
	 */
	@RequestMapping("listServiceNames")
    @RestReturn(value = IdNameDTO.class, collection = true)
	public RestResponse listServiceNames(ListServiceNamesCommand cmd) {
        return new RestResponse(allianceClickStatService.listServiceNames(cmd));
	}

    /**
	 * <b>URL: /yellowPage/listServiceTypeNames</b>
	 * <p> 获取所有服务类型名称 </p>
	 */
	@RequestMapping("listServiceTypeNames")
    @RestReturn(value = IdNameDTO.class, collection = true)
	public RestResponse listServiceTypeNames(ListServiceTypeNamesCommand cmd) {
        return new RestResponse(allianceClickStatService.listServiceTypeNames(cmd));
	}

    /**
	 * <b>URL: /yellowPage/updateServiceTypeOrders</b>
	 * <p> 更新服务类型之间的顺序 </p>
	 */
	@RequestMapping("updateServiceTypeOrders")
    @RestReturn(value = String.class)
	public RestResponse updateServiceTypeOrders(UpdateServiceTypeOrdersCommand cmd) {
		yellowPageService.updateServiceTypeOrders(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	/**
	 * <b>URL: /yellowPage/transferApprovalToForm</b>
	 * <p>
	 * 获取用户服务记录
	 * </p>
	 */
	@RequestMapping("transferApprovalToForm")
	@RestReturn(value = String.class)
	public RestResponse transferApprovalToForm(ListServiceTypeNamesCommand cmd) {
		if (cmd.getOwnerId() == null || !cmd.getOwnerId().equals(1802L)) {
			return new RestResponse();
		}
		
		String ret = allianceStandardService.transferApprovalToForm();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription(ret);
		return response;
	}
	
	/**
	 * <b>URL: /yellowPage/transferMainAllianceOwnerType</b>
	 * <p>
	 * 更新alliane的ownerType
	 * </p>
	 */
	@RequestMapping("transferMainAllianceOwnerType")
	@RestReturn(value = String.class)
	public RestResponse transferMainAllianceOwnerType(UpdateAllianceTagCommand cmd) {
		if (cmd.getOwnerId() == null || !cmd.getOwnerId().equals(1802L)) {
			return new RestResponse();
		}
		
		String ret = allianceStandardService.transferMainAllianceOwnerType();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription(ret);
		return response;
	}
	
	/**
	 * <b>URL: /yellowPage/transferAllianceModuleUrl</b>
	 * <p>
	 * 更新moduleUrl
	 * </p>
	 */
	@RequestMapping("transferAllianceModuleUrl")
	@RestReturn(value = String.class)
	public RestResponse transferAllianceModuleUrl(UpdateAllianceTagCommand cmd) {
		if (cmd.getOwnerId() == null || !cmd.getOwnerId().equals(1802L)) {
			return new RestResponse();
		}
		
		String ret = allianceStandardService.transferAllianceModuleUrl();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription(ret);
		return response;
	}
	
	/**
	 * <b>URL: /yellowPage/transferPadItems</b>
	 * <p>
	 * 获取用户服务记录
	 * </p>
	 */
	@RequestMapping("transferPadItems")
	@RestReturn(value = String.class)
	public RestResponse transferPadItems(UpdateAllianceTagCommand cmd) {
		if (cmd.getOwnerId() == null || !cmd.getOwnerId().equals(1802L)) {
			return new RestResponse();
		}
		
		String ret = allianceStandardService.transferPadItems();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription(ret);
		return response;
	}
	
}
