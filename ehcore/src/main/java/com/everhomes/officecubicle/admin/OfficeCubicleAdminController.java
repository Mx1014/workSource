package com.everhomes.officecubicle.admin;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.officecubicle.admin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.officecubicle.OfficeCubicleService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.DeleteSpaceCommand;

/**
 * <ul>
 * <li>工位预定的控制器</li>
 * <li>提供给web-app端的用户操作，查询空间，查询详情，下订单和删除（不可见）订单</li>
 * </ul>
 */
@RestDoc(value="OfficeCubicle controller", site="officecubicle")
@RestController
@RequestMapping("/officecubicle")
public class OfficeCubicleAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleAdminController.class);
    @Autowired
    private OfficeCubicleService officeCubicleService;
    /**
     * <b>URL: /officecubicle/searchSpaces</b> 
     * <p>空间管理-获取空间列表</p>
     */
    @RequestMapping("searchSpaces")
    @RestReturn(value=SearchSpacesAdminResponse.class )
    public RestResponse searchSpaces(SearchSpacesAdminCommand cmd) {
    	SearchSpacesAdminResponse resp = this.officeCubicleService.searchSpaces(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }

    /**
     * <b>URL: /officecubicle/listRegions</b>
     * <p>城市管理-获取区域列表</p>
     */
    @RequestMapping("listRegions")
    @RestReturn(value=ListRegionsResponse.class )
    public RestResponse listRegions(ListRegionsCommand cmd) {
        ListRegionsResponse resp = this.officeCubicleService.listRegions(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/listProvinceAndCites</b>
     * <p>城市管理-获取已经设定的省份和城市</p>
     */
    @RequestMapping("listProvinceAndCites")
    @RestReturn(value=ListCitiesResponse.class )
    public RestResponse listProvinceAndCites(ListCitiesCommand cmd) {
        ListCitiesResponse resp = this.officeCubicleService.listProvinceAndCites(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    

    /**
     * <b>URL: /officecubicle/listCities</b>
     * <p>城市管理-获取已经设定的城市列表</p>
     */
    @RequestMapping("listCities")
    @RestReturn(value=ListCitiesResponse.class )
    public RestResponse listCities(ListCitiesCommand cmd) {
        ListCitiesResponse resp = this.officeCubicleService.listCities(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/deleteCity</b>
     * <p>城市管理-删除城市</p>
     */
    @RequestMapping("deleteCity")
    @RestReturn(value=String.class )
    public RestResponse deleteCity(DeleteCityCommand cmd) {
       this.officeCubicleService.deleteCity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/createOrUpdateCity</b>
     * <p>城市管理-创建/更新城市</p>
     */
    @RequestMapping("createOrUpdateCity")
    @RestReturn(value=String.class )
    public RestResponse createOrUpdateCity(CreateOrUpdateCityCommand cmd) {
        this.officeCubicleService.createOrUpdateCity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/reOrderCity</b>
     * <p>城市管理-城市顺序交换</p>
     */
    @RequestMapping("reOrderCity")
    @RestReturn(value=String.class )
    public RestResponse reOrderCity(ReOrderCityCommand cmd) {
        this.officeCubicleService.reOrderCity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }
    
    /**
     * <b>URL: /officecubicle/getCityById</b>
     * <p>城市管理-根据id获取城市详情</p>
     */
    @RequestMapping("getCityById")
    @RestReturn(value=CityDTO.class )
    public RestResponse getCityById(GetCityByIdCommand cmd) {
        RestResponse response = new RestResponse(this.officeCubicleService.getCityById(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/copyCities</b>
     * <p>城市管理-复制默认配置</p>
     */
    @RequestMapping("copyCities")
    @RestReturn(value=ListCitiesResponse.class)
    public RestResponse copyCities(CopyCitiesCommand cmd) {
        ListCitiesResponse resp = this.officeCubicleService.copyCities(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /officecubicle/removeCustomizedCities</b>
     * <p>城市管理-恢复默认配置</p>
     */
    @RequestMapping("removeCustomizedCities")
    @RestReturn(value=ListCitiesResponse.class)
    public RestResponse removeCustomizedCities(CopyCitiesCommand cmd) {
        ListCitiesResponse resp = this.officeCubicleService.removeCustomizedCities(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /officecubicle/getProjectCustomize</b>
     * <p>城市管理-获取自定义配置状态</p>
     */
    @RequestMapping("getProjectCustomize")
    @RestReturn(value=String.class)
    public RestResponse getProjectCustomize(GetCustomizeCommand cmd) {
        Byte flag = this.officeCubicleService.getProjectCustomize(cmd);
        RestResponse response = new RestResponse(flag);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /officecubicle/addSpace</b>
     * <p>空间管理-添加空间</p>
     */
    @RequestMapping("addSpace")
    @RestReturn(value=String.class )
    public RestResponse addSpace(AddSpaceCommand cmd) {
    	this.officeCubicleService.addSpace(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/updateSpace</b> 
     * <p>空间管理-更新空间</p>
     */
    @RequestMapping("updateSpace")
    @RestReturn(value=String.class )
    public RestResponse updateSpace(UpdateSpaceCommand cmd) {

    	this.officeCubicleService.updateSpace(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    

    /**
     * <b>URL: /officecubicle/deleteSpace</b> 
     * <p>空间管理-删除空间</p>
     */
    @RequestMapping("deleteSpace")
    @RestReturn(value=String.class )
    public RestResponse deleteSpace(DeleteSpaceCommand cmd) {
    	 this.officeCubicleService.deleteSpace(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    

    /**
     * <b>URL: /officecubicle/searchSpaceOrders</b> 
     * <p>预定详情-获取列表</p>
     */
    @RequestMapping("searchSpaceOrders")
    @RestReturn(value=SearchSpaceOrdersResponse.class )
    public RestResponse searchSpaceOrders(SearchSpaceOrdersCommand cmd) {

        SearchSpaceOrdersResponse resp = this.officeCubicleService.searchSpaceOrders(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
	/**
	 * <b>URL: /officecubicle/exprotSpaceOrders</b>
	 * <p>
	 * 导出预订详情
	 * </p>
	 */
	@RequestMapping("exprotSpaceOrders")
	public String exprotSpaceOrders(@Valid SearchSpaceOrdersCommand cmd,HttpServletResponse response) {
//		HttpServletResponse commandResponse = rentalService.exportRentalBills(cmd, response );
		HttpServletResponse commandResponse = this.officeCubicleService.exportSpaceOrders(cmd,response);
		return null;
	}

    /**
     * <b>URL: /officecubicle/dataMigration</b>
     * <p>
     * 数据迁移
     * </p>
     */
    @RequestMapping("dataMigration")
    public RestResponse dataMigration() {
        this.officeCubicleService.dataMigration();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }



    
}
