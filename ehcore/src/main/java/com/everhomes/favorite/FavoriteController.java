package com.everhomes.favorite;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.favorite.FavoriteDTO;

@RestController
@RequestMapping("/favorite")
public class FavoriteController extends ControllerBase {

    @RequestMapping("addFavorite")
    @RestReturn(value=Long.class)
    public RestResponse addFavorite(
        @RequestParam(value = "targetType", required = true) String entityType,
        @RequestParam(value = "targetId", required = true) Long entityId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("removeFavorite")
    @RestReturn(value=Long.class)
    public RestResponse removeFavorite(
        @RequestParam(value = "favoriteId", required = true) Long favoriteId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listFavorites")
    @RestReturn(value=FavoriteDTO.class, collection=true)
    public RestResponse removeFavorite(
        @RequestParam(value = "targetType", required = true) String targetType) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
