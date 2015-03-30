package com.everhomes.address.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.address.CommunityDTO;
import com.everhomes.address.ListNearbyCommunityCommand;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/address")
public class AddressController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);
    
    @RequireAuthentication(false)
    @RequestMapping("listNearbyCommunities")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse list(@Valid ListNearbyCommunityCommand cmd) {

        // ???
        return new RestResponse(null);
    }
}
