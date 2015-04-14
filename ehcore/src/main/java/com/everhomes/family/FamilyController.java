package com.everhomes.family;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyMemberDTO;
import com.everhomes.family.FamilyMembershipRequestDTO;
import com.everhomes.family.ListNearbyNeighborUserCommand;
import com.everhomes.family.NeighborUserDTO;
import com.everhomes.family.UpdateFamilyInfoCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserInfo;

@RestController
@RequestMapping("/family")
public class FamilyController extends ControllerBase {
    
    @RequestMapping("findFamilyByKeyword")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse findFamilyByKeyword(
        @RequestParam(value = "keyword", required = true) String keyword) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("getOwningFamilyById")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse getOwningFamilyById(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // familyId should be one of the families that user is currently in relation with
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("join")
    @RestReturn(value=String.class)
    public RestResponse join(
        @RequestParam(value = "familyId", required = true) Long familyId) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("leave")
    @RestReturn(value=String.class)
    public RestResponse leave(
        @RequestParam(value = "familyId", required = true) Long familyId) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("follow")
    @RestReturn(value=String.class)
    public RestResponse follow(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "aliasName", required = false) String aliasName) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("unfollow")
    @RestReturn(value=String.class)
    public RestResponse follow(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listFollowedFamilies")
    @RestReturn(value=FamilyDTO.class, collection=true)
    public RestResponse listFollowedFamilies() {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("setFollowedFamilyAlias")
    @RestReturn(value=String.class)
    public RestResponse setFollowedFamilyAlias(@RequestParam(value = "familyId", required = true) Long familyId,
            @RequestParam(value = "aliasName", required = true) String aliasName) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listFollowers")
    @RestReturn(value=UserInfo.class, collection=true)
    public RestResponse listFollowers(@RequestParam(value = "familyId", required = true) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("get")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse get(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("ejectMember")
    @RestReturn(value=String.class)
    public RestResponse ejectMember(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "memberUid", required = true) Long memberUid,
        @RequestParam(value = "reason", required = false) String reason) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("approveMember")
    @RestReturn(value=String.class)
    public RestResponse approveMember(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "memberUid", required = true) Long memberUid) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listOwningFamilyMembers")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listOwningFamilyMembers(@RequestParam(value = "familyId", required = true) Long familyId) {
            
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listFamilyRequests")
    @RestReturn(value=FamilyMembershipRequestDTO.class, collection=true)
    public RestResponse listFamilyRequests(@RequestParam(value = "familyId", required = false) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("setCurrentFamily")
    @RestReturn(value=String.class)
    public RestResponse setCurrentFamily(@RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("setPrimaryFamily")
    @RestReturn(value=String.class)
    public RestResponse setPrimaryFamily(@RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("updateFamilyInfo")
    @RestReturn(value=String.class)
    public RestResponse updateFamilyInfo(@Valid UpdateFamilyInfoCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNeighborUsers(@RequestParam(value = "familyId", required = false) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listNearbyNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNearbyNeighborUsers(@Valid ListNearbyNeighborUserCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
