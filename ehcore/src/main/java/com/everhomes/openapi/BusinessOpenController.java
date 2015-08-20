package com.everhomes.openapi;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.business.BusinessService;
import com.everhomes.business.SyncBusinessCommand;
import com.everhomes.business.SyncDeleteBusinessCommand;
import com.everhomes.business.SyncUserAddShopStatusCommand;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryConstants;
import com.everhomes.category.CategoryDTO;
import com.everhomes.category.CategoryProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@RestDoc(value="Business open Constroller", site="core")
@RestController
@RequestMapping("/openapi")
public class BusinessOpenController extends ControllerBase {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private UserActivityService userActivityService;
    
    /**
     * <b>URL: /openapi/listBizCategories</b> 列出所有商家分类
     */
    @RequestMapping("listBizCategories")
    @RestReturn(value = CategoryDTO.class, collection = true)
    public RestResponse listBizCategories() {
        
        Tuple<String, SortOrder> orderBy = null;
        @SuppressWarnings("unchecked")
        List<Category> entityResultList = this.categoryProvider.listChildCategories(CategoryConstants.CATEGORY_ID_SERVICE,
                CategoryAdminStatus.ACTIVE, orderBy);

        List<CategoryDTO> dtoResultList = entityResultList.stream().map(r -> {
            return ConvertHelper.convert(r, CategoryDTO.class);
        }).collect(Collectors.toList());
        
        return new RestResponse(dtoResultList);
    }
    
    /**
     * <b>URL: /openapi/syncBusiness</b>
     * <p>同步添加/更新店铺</p>
     */
    @RequestMapping("syncBusiness")
    @RestReturn(value=String.class)
    public RestResponse syncBusiness(@Valid SyncBusinessCommand cmd) {
        
        businessService.syncBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/syncDeleteBusiness</b>
     * <p>同步删除店铺</p>
     */
    @RequestMapping("syncDeleteBusiness")
    @RestReturn(value=String.class)
    public RestResponse syncDeleteBusiness(@Valid SyncDeleteBusinessCommand cmd) {
        
        businessService.syncDeleteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/syncUserAddShopStatus</b>
     * <p>同步开店状态</p>
     */
    @RequestMapping("syncUserAddShopStatus")
    @RestReturn(value=String.class)
    public RestResponse syncUserAppliedShopStatus(SyncUserAddShopStatusCommand cmd) {
        
        userActivityService.addUserShop(cmd.getUserId());
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
