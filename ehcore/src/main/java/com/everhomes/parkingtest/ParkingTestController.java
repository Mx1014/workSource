// @formatter:off
package com.everhomes.parkingtest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;

/**
 * Controller 层
 * @author dingjianmin
 */

@RestDoc(value="ParkingTest controller", site="parkingTest")
@RestController
@RequestMapping("/parkingTest")
public class ParkingTestController extends ControllerBase {/*
    
    @Autowired
    private ParkingTestService parkingTestService;
    
    *//**
     * <b>URL: /parkingTest/listParkingLotsSelect</b>
     * <p>查询练习</p>
     *//*
    //使用缓存
    //@Cacheable(value="FindRegionById", key="#regionId", unless="#result == null")
    //@Cacheable(value = "listParkingLotsSelectCache", key="#cmd.id", unless="#result.size() == 0")
    
    @RequireAuthentication(false)//不用登陆就可以用
    @RequestMapping("listParkingLotsSelect")
    @RestReturn(value=ParkingLotTestResponse.class, collection=true)
    public RestResponse listParkingLotsSelect(ListParkingLotsTestCommand cmd) {
        
    	ParkingLotTestResponse parkingLotListTest = parkingTestService.listParkingLotsTest(cmd);
    	
        RestResponse response = new RestResponse(parkingLotListTest);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    *//**
     * <b>URL: /parkingTest/listParkingLotsUpdate</b>
     * <p>修改练习</p>
     *//*
    @RequireAuthentication(false)//不用登陆就可以用
    @RequestMapping("listParkingLotsUpdate")
    @RestReturn(value=String.class)
    public RestResponse listParkingLotsUpdate(SetListParkingLotsTestCommand cmd) {
        
    	parkingTestService.listParkingLotsTestUpdate(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    *//**
     * <b>URL: /parkingTest/listParkingLotsAdd</b>
     * <p>新增练习</p>
     *//*
    @RequireAuthentication(false)//不用登陆就可以用
    @RequestMapping("listParkingLotsAdd")
    @RestReturn(value=ParkingLotTestDTO.class)
    public RestResponse listParkingLotsAdd(AddListParkingLotsTestCommand cmd) {

        RestResponse response = new RestResponse(parkingTestService.listParkingLotsTestAdd(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    *//**
     * <b>URL: /parking/listParkingLotsDelete</b>
     * <p>删除练习</p>
     *//*
    @RequireAuthentication(false)//不用登陆就可以用
    @RequestMapping("listParkingLotsDelete")
    @RestReturn(value=String.class)
    public RestResponse listParkingLotsDelete(DeleteListParkingLotsTestCommand cmd) {

    	parkingTestService.listParkingLotsTestDelete(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
*/}
