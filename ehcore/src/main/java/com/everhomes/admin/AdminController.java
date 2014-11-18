package com.everhomes.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.border.AddBorderCommand;
import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
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
    public RestResponse signup(@Valid AddBorderCommand cmd) {
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

        return new RestResponse(border.getId());
    }
}
