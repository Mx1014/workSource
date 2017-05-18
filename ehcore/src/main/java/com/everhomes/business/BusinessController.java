package com.everhomes.business;


import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.business.*;
import com.everhomes.util.RequireAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="Business controller", site="core")
@RestController
@RequestMapping("/business")
public class BusinessController extends ControllerBase {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * <b>URL: /business/findBusinessById</b>
     *
     */
    @RequestMapping("findBusinessById")
    @RestReturn(value=BusinessDTO.class)
    public RestResponse findBusinessById(@Valid FindBusinessByIdCommand cmd) {
        
    	BusinessDTO res = businessService.findBusinessById(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/listBusinessByKeyword</b>
     *
     */
    @RequestMapping("listBusinessByKeyword")
    @RestReturn(value=ListBusinessByKeywordCommandResponse.class)
    public RestResponse listBusinessByKeyword(@Valid ListBusinessByKeywordCommand cmd) {
        
    	ListBusinessByKeywordCommandResponse res = businessService.listBusinessByKeyword(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /business/getBusinessesByCategory</b>
     * <p>根据分类获取该类别下的店铺列表</p>
     */
    @RequestMapping("getBusinessesByCategory")
    @RestReturn(value=GetBusinessesByCategoryCommandResponse.class)
    public RestResponse getBusinessesByCategory(@Valid GetBusinessesByCategoryCommand cmd) {
        
        GetBusinessesByCategoryCommandResponse res = businessService.getBusinessesByCategory(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("updateBusinessDistance")
    @RestReturn(value=String.class)
    public RestResponse updateBusinessDistance(@Valid UpdateBusinessDistanceCommand cmd) {
        businessService.updateBusinessDistance(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    
    /**
     * <b>URL: /business/updateBusiness</b>
     * <p>更新店铺信息</p>
     */
    @RequestMapping("updateBusiness")
    @RestReturn(value=String.class)
    public RestResponse updateBusiness(@Valid UpdateBusinessCommand cmd) {
        
        businessService.updateBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/deleteBusiness</b>
     * <p>删除店铺</p>
     */
    @RequestMapping("deleteBusiness")
    @RestReturn(value=String.class)
    public RestResponse deleteBusiness(@Valid DeleteBusinessCommand cmd) {
        
        businessService.deleteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/favoriteBusiness</b>
     * <p>店铺收藏（放到服务市场首页）</p>
     */
    @RequestMapping("favoriteBusiness")
    @RestReturn(value=String.class)
    public RestResponse favoriteBusiness(FavoriteBusinessCommand cmd) {
        
        businessService.favoriteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/favoriteBusinesses</b>
     * <p>店铺收藏（放到服务市场首页）</p>
     */
    @RequestMapping("favoriteBusinesses")
    @RestReturn(value=String.class)
    public RestResponse favoriteBusinesses(FavoriteBusinessesCommand cmd) {
        
        businessService.favoriteBusinesses(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/cancelFavoriteBusiness</b>
     * <p>用户取消收藏</p>
     */
    @RequestMapping("cancelFavoriteBusiness")
    @RestReturn(value=String.class)
    public RestResponse cancelFavoriteBusiness(CancelFavoriteBusinessCommand cmd) {
        
        businessService.cancelFavoriteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取电商运营数据</p>
     * <b>URL: /business/listBusinessPromotionEntities</b>
     */
    @RequestMapping("listBusinessPromotionEntities")
    @RestReturn(value = ListBusinessPromotionEntitiesReponse.class)
    @RequireAuthentication(false)
    public RestResponse listBusinessPromotionEntities(ListBusinessPromotionEntitiesCommand cmd){
        RestResponse response = new RestResponse(businessService.listBusinessPromotionEntities(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>创建电商运营数据</p>
     * <b>URL: /business/createBusinessPromotion</b>
     */
    @RequestMapping("createBusinessPromotion")
    @RestReturn(value = String.class)
    public RestResponse createBusinessPromotion(CreateBusinessPromotionCommand cmd){
        businessService.createBusinessPromotion(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>测试事务</p>
     * <b>URL: /business/testTransaction</b>
     */
    // @RequireAuthentication(false)
    @RequestMapping("testTransaction")
    @RestReturn(value = String.class)
    public RestResponse testTransaction(){
        businessService.testTransaction();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>切换电商运营数据源</p>
     * <b>URL: /business/switchBusinessPromotionDataSource</b>
     */
    @RequestMapping("switchBusinessPromotionDataSource")
    @RestReturn(value = String.class)
    public RestResponse switchBusinessPromotionDataSource(SwitchBusinessPromotionDataSourceCommand cmd){
        businessService.switchBusinessPromotionDataSource(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
