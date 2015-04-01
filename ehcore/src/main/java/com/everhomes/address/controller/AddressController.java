package com.everhomes.address.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.address.AddressService;
import com.everhomes.address.BuildingDTO;
import com.everhomes.address.CommunityDTO;
import com.everhomes.address.ListAppartmentByKeywordCommand;
import com.everhomes.address.ListBuildingByKeywordCommand;
import com.everhomes.address.ListCommunityByKeywordCommand;
import com.everhomes.address.ListNearbyCommunityCommand;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/address")
public class AddressController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
    
    @Autowired
    private AddressService addressService;
    
    @RequestMapping("listNearbyCommunities")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse list(@Valid ListNearbyCommunityCommand cmd) {
        Tuple<Integer, List<CommunityDTO>> results = this.addressService.listNearbyCommunities(cmd);
        RestResponse response =  new RestResponse(results.second());

        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listCommunitiesByKeyword")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse listCommunitiesByKeyword(@Valid ListCommunityByKeywordCommand cmd) {
        Tuple<Integer, List<CommunityDTO>> results = this.addressService.listCommunitiesByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());
        
        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listBuildingsByKeyword")
    @RestReturn(value=BuildingDTO.class, collection=true)
    public RestResponse listBuildingsByKeyword(@Valid ListBuildingByKeywordCommand cmd) {
        Tuple<Integer, List<BuildingDTO>> results = this.addressService.listBuildingsByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());
        
        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listAppartmentsByKeyword")
    @RestReturn(value=String.class, collection=true)
    public RestResponse listAppartmentsByKeyword(@Valid ListAppartmentByKeywordCommand cmd) {
        Tuple<Integer, List<String>> results = this.addressService.listAppartmentsByKeyword(cmd);
        RestResponse response = new RestResponse(results.second());
        
        response.setErrorCode(results.first());
        response.setErrorDescription("OK");
        return response;
    }
}
