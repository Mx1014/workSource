package com.everhomes.yellowPage;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.yellowPage.AddYellowPageCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.DeleteServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.DeleteYellowPageCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseDetailCommand;
import com.everhomes.rest.yellowPage.GetServiceAllianceEnterpriseListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageDetailCommand;
import com.everhomes.rest.yellowPage.GetYellowPageListCommand;
import com.everhomes.rest.yellowPage.GetYellowPageTopicCommand;
import com.everhomes.rest.yellowPage.ListServiceAllianceCategoriesCommand;
import com.everhomes.rest.yellowPage.ServiceAllianceCategoryDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceDTO;
import com.everhomes.rest.yellowPage.ServiceAllianceListResponse;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCategoryCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceCommand;
import com.everhomes.rest.yellowPage.UpdateServiceAllianceEnterpriseCommand;
import com.everhomes.rest.yellowPage.UpdateYellowPageCommand;
import com.everhomes.rest.yellowPage.YellowPageDTO;
import com.everhomes.rest.yellowPage.YellowPageListResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
 

@RestController
@RequestMapping("/yellowPage")
public class YellowPageController  extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(YellowPageController.class);
    
    private static final String DEFAULT_SORT = "default_order";

    @Autowired
    private YellowPageService yellowPageService;

	@Autowired
	private CategoryProvider categoryProvider;
    
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
	@RequestMapping("listServiceAllianceCategories")
	@RestReturn(value = ServiceAllianceCategoryDTO.class, collection = true)
	public RestResponse listServiceAllianceCategories(ListServiceAllianceCategoriesCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Tuple<String, SortOrder> orderBy = new Tuple<String, SortOrder>(DEFAULT_SORT, SortOrder.ASC);;
		@SuppressWarnings("unchecked")
		List<Category> entityResultList = this.categoryProvider.listChildCategories(namespaceId, 
				CategoryConstants.CATEGORY_ID_YELLOW_PAGE, CategoryAdminStatus.ACTIVE, orderBy);

		List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
			return ConvertHelper.convert(r, CategoryDTO.class);
		}).collect(Collectors.toList());

		return new RestResponse(dtoResultList);
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
}
