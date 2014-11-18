package com.everhomes.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.border.AddBorderCommand;
import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
import com.everhomes.border.UpdateBorderCommand;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.RestControllerBase;
import com.everhomes.rest.RestResponse;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

@RestController
@RequestMapping("/admin")
public class AdminController extends RestControllerBase {
    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    private AclProvider aclProvider;

    @RequestMapping("addBorder")
    public RestResponse addBorder(@Valid AddBorderCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        Border border = new Border();
        border.setPrivateAddress(cmd.getPrivateAddress());
        border.setPrivatePort(cmd.getPrivatePort());
        border.setPublicAddress(cmd.getPublicAddress());
        border.setPublicPort(cmd.getPublicPort());
        border.setStatus(cmd.getStatus().ordinal());
        border.setConfigTag(cmd.getConfigTag());
        border.setDescription(cmd.getDescription());
        this.borderProvider.createBorder(border);

        return new RestResponse(border);
    }
    
    @RequestMapping("updateBorder")
    public RestResponse updateBorder(@Valid UpdateBorderCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        Border border = this.borderProvider.findBorderById(cmd.getId());
        if(border == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id parameter value");
               
        if(cmd.getConfigTag() != null)
            border.setConfigTag(cmd.getConfigTag());
        if(cmd.getDescription() != null)
            border.setDescription(cmd.getDescription());
        if(cmd.getPrivateAddress() != null)
            border.setPrivateAddress(cmd.getPrivateAddress());
        if(cmd.getPrivatePort() != null)
            border.setPrivatePort(cmd.getPrivatePort());
        if(cmd.getPublicAddress() != null)
            border.setPublicAddress(cmd.getPublicAddress());
        if(cmd.getPublicPort() != null)
            border.setPublicPort(cmd.getPublicPort());
        if(cmd.getStatus() != null) {
            border.setStatus(cmd.getStatus().ordinal());
        }
        this.borderProvider.updateBorder(border);
        return new RestResponse(border);   
    }
    
    @RequestMapping("removeBorder")
    public RestResponse updateBorder(@RequestParam(value="id", required=true) int id) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        Border border = this.borderProvider.findBorderById(id);
        if(border == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id parameter value");

        this.borderProvider.deleteBorderById(id);
        return new RestResponse(border);   
    }
    
    @RequestMapping("listBorder")
    public RestResponse listBorder() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<Border> borders = this.borderProvider.listAllBorders();
        return new RestResponse(borders);
    }
}
