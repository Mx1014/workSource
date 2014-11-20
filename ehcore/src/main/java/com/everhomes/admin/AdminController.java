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
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.persist.server.AddPersistServerCommand;
import com.everhomes.persist.server.UpdatePersistServerCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.sharding.Server;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

@RestController
@RequestMapping("/admin")
public class AdminController extends ControllerBase {
    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private AclProvider aclProvider;

    @RequestMapping("addBorder")
    @RestReturn(Border.class)
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
        border.setStatus(cmd.getStatus());
        border.setConfigTag(cmd.getConfigTag());
        border.setDescription(cmd.getDescription());
        this.borderProvider.createBorder(border);

        return new RestResponse(border);
    }
    
    @RequestMapping("updateBorder")
    @RestReturn(Border.class)
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
            border.setStatus(cmd.getStatus());
        }
        this.borderProvider.updateBorder(border);
        return new RestResponse(border);   
    }
    
    @RequestMapping("removeBorder")
    @RestReturn(Border.class)
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
    @RestReturn(value=Border.class, collection=true)
    public RestResponse listBorder() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<Border> borders = this.borderProvider.listAllBorders();
        return new RestResponse(borders);
    }
    
    @RequestMapping("addPersistServer")
    @RestReturn(Server.class)
    public RestResponse addPersistServer(@Valid AddPersistServerCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        Server server = new Server();
        server.setMasterId(cmd.getMasterId());
        server.setAddressUri(cmd.getAddressUri());
        server.setAddressPort(cmd.getAddressPort());
        server.setServerType(cmd.getServerType());
        if(cmd.getStatus() == null)
            server.setStatus(1);
        else
            server.setStatus(cmd.getStatus());
        server.setConfigTag(cmd.getConfigTag());
        server.setDescription(cmd.getDescription());
        
        this.shardingProvider.createServer(server);

        return new RestResponse(server);
    }
    
    @RequestMapping("updatePersistServer")
    @RestReturn(Server.class)
    public RestResponse updatePersistServer(@Valid UpdatePersistServerCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        Server server = this.shardingProvider.getServerById(cmd.getId());
        if(server == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id parameter value");
               
        if(cmd.getMasterId() != null)
            server.setMasterId(cmd.getMasterId());
        if(cmd.getAddressUri() != null)
            server.setAddressUri(cmd.getAddressUri());
        if(cmd.getAddressPort() != null)
            server.setAddressPort(cmd.getAddressPort());
        if(cmd.getServerType() != null)
            server.setServerType(cmd.getServerType());
        if(cmd.getStatus() != null)
            server.setStatus(cmd.getStatus());
        if(cmd.getConfigTag() != null)
            server.setConfigTag(cmd.getConfigTag());
        if(cmd.getDescription() != null)
            server.setDescription(cmd.getDescription());
        
        this.shardingProvider.updateServer(server);
        return new RestResponse(server);   
    }
    
    @RequestMapping("listPersistServer")
    @RestReturn(value=Server.class, collection=true)
    public RestResponse listPersitServers() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<Server> servers = this.shardingProvider.listAllServers();
        return new RestResponse(servers);
    }
}
