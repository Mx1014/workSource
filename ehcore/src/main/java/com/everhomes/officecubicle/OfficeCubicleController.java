package com.everhomes.officecubicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.CityDTO;
import com.everhomes.rest.officecubicle.GetSpaceListCommand;
import com.everhomes.rest.officecubicle.admin.GetSpaceListAdminResponse;

/**
 * <ul>
 * <li>工位预定的控制器</li>
 * <li>提供给web-app端的用户操作，查询空间，查询详情，下订单和删除（不可见）订单</li>
 * </ul>
 */
@RestDoc(value="OfficeCubicle controller", site="officecubicle")
@RestController
@RequestMapping("/officecubicle")
public class OfficeCubicleController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleController.class);

    /**
     * <b>URL: /officecubicle/getCityList</b> 
     * <p>工位预定-获取城市列表</p>
     */
    @RequestMapping("getCityList")
    @RestReturn(value=CityDTO.class ,collection=true)
    public RestResponse getCityList() {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getSpaceList</b> 
     * <p>工位预定-获取空间列表</p>
     */
    @RequestMapping("getSpaceList")
    @RestReturn(value=GetSpaceListResponse.class )
    public RestResponse getSpaceList(GetSpaceListCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }

}
