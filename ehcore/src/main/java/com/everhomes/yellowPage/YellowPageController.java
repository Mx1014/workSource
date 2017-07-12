package com.everhomes.yellowPage;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.yellowPage.*;
import com.everhomes.search.ApartmentRequestInfoSearcher;
import com.everhomes.search.ReserveRequestInfoSearcher;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.search.SettleRequestInfoSearcher;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/yellowPage")
public class YellowPageController  extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageController.class);
    
    private static final String DEFAULT_SORT = "default_order";

    @Autowired
    private YellowPageService yellowPageService;

	@Autowired
	private YellowPageProvider yellowPageProvider;
	
	@Autowired
	private ServiceAllianceRequestInfoSearcher saRequestInfoSearcher;
	
	@Autowired
	private SettleRequestInfoSearcher settleRequestInfoSearcher;

    @Autowired
	private ApartmentRequestInfoSearcher apartmentRequestInfoSearcher;

    @Autowired
    private ReserveRequestInfoSearcher reserveRequestInfoSearcher;

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
	 * <p> 列出服务联盟类型 </p>
	 */
    @RequireAuthentication(false)
	@RequestMapping("listServiceAllianceCategories")
	@RestReturn(value = ServiceAllianceCategoryDTO.class, collection = true)
	public RestResponse listServiceAllianceCategories(ListServiceAllianceCategoriesCommand cmd) {
		return new RestResponse(yellowPageService.listServiceAllianceCategories(cmd));
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
   	 * <p> 服务联盟首页 </p>
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
    public RestResponse listJumpModules() {

        List<JumpModuleDTO> resp = this.yellowPageService.listJumpModules();

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
     * <p>导出申请信息/p>
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

}
