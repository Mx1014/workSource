// @formatter:off
package com.everhomes.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.app.App;
import com.everhomes.border.AddBorderCommand;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderDTO;
import com.everhomes.border.BorderProvider;
import com.everhomes.border.UpdateBorderCommand;
import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.codegen.GeneratorContext;
import com.everhomes.codegen.ObjectiveCGenerator;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestMethod;
import com.everhomes.discover.RestReturn;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.persist.server.AddPersistServerCommand;
import com.everhomes.persist.server.UpdatePersistServerCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.rpc.server.PingRequestPdu;
import com.everhomes.rpc.server.PingResponsePdu;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.sharding.Server;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.LoginToken;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserLoginDTO;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.ReflectionHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * Infrastructure Administration API controller
 * 
 * @author Kelven Yang
 *
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private NamespaceProvider nsProvider;
    
    @Autowired
    private BorderConnectionProvider borderConnectionProvider;
    
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    
    @Autowired
    private UserService userService;
    
    @Value("#{T(java.util.Arrays).asList('${source.jars}')}")
    private List<String> jars;
    
    @Value("#{T(java.util.Arrays).asList('${source.excludes}')}")
    private List<String> excludes;
    
    @Value("${destination.dir}")
    private String destinationDir;
    
    @Value("${class.name.prefix}")
    private String classNamePrefix;
    
    @Value("${serialization.serializable}")
    private String serializable;

    @Value("${serialization.helper}")
    private String serializationHelper;

    @Value("${objc.header.ext}")
    private String headerFileExtention;
    
    @Value("${objc.source.ext}")
    private String sourceFileExtention;

    @Value("${objc.response.base}")
    private String restResponseBase;    
    
    @RequestMapping("codegen")
    @RestReturn(String.class)
    public RestResponse codegen(@RequestParam(value="language", required=true) String language) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        if(!language.equalsIgnoreCase("objc"))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Only objc (objective C) is currently supported");

        GeneratorContext context = new GeneratorContext();
        context.setClassNamePrefix(this.classNamePrefix);
        context.setDestinationDir(this.destinationDir);
        context.setHeaderFileExtention(this.headerFileExtention);
        context.setSerializable(this.serializable);
        context.setSerializationHelper(this.serializationHelper);
        context.setSourceFileExtention(this.sourceFileExtention);
        context.setRestResponseBase(restResponseBase);
        
        ObjectiveCGenerator generator = new ObjectiveCGenerator();
        // generator.generatePojos(BorderDTO.class, context);

        // generate REST POJO objects
        jars.stream().forEach((jar)-> {
            try {
                Set<Class<?>> classes = ReflectionHelper.loadClassesInJar(jar);
                
                for(Class<?> clz: classes) {
                    if(!shouldExclude(clz)) {
                        generator.generatePojos(clz, context);
                    } else {
                        LOGGER.info("Skip {} since it matches exclusion configuration", clz.getName());
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Unable to open {}", jar, e);
            }
        });

        // generator controller API response objects
        List<RestMethod> apiMethods = ControllerBase.getRestMethodList();
        for(RestMethod restMethod: apiMethods)
            generator.generateControllerPojos(restMethod, context);
        
        return new RestResponse("OK");
    }
    
    private boolean shouldExclude(Class<?> clz) {
        if(excludes != null) {
            for(String excludeExpr : excludes) {
                if(clz.getName().matches(excludeExpr))
                    return true;
            }
        }
        return false;
    }
    
    @RequestMapping("addBorder")
    @RestReturn(BorderDTO.class)
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
        
        //LOGGER.info("" + borderConnectionProvider.broadcastToAllBorders(0, null));

        return new RestResponse(ConvertHelper.convert(border, BorderDTO.class));
    }
    
    @RequestMapping("updateBorder")
    @RestReturn(BorderDTO.class)
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
        return new RestResponse(ConvertHelper.convert(border, BorderDTO.class));   
    }
    
    @RequestMapping("removeBorder")
    @RestReturn(BorderDTO.class)
    public RestResponse updateBorder(@RequestParam(value="id", required=true) int id) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        Border border = this.borderProvider.findBorderById(id);
        if(border == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id parameter value");

        this.borderProvider.deleteBorderById(id);
        return new RestResponse(ConvertHelper.convert(border, BorderDTO.class));   
    }
    
    @RequestMapping("listBorder")
    @RestReturn(value=BorderDTO.class, collection=true)
    public RestResponse listBorder() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<Border> borders = this.borderProvider.listAllBorders();
        return new RestResponse(borders.stream().map((r) -> {return ConvertHelper.convert(r, BorderDTO.class);}).collect(Collectors.toList()));
    }
    
    @RequestMapping("pingBorder")
    @RestReturn(String.class)
    public DeferredResult<RestResponse> pingBorder(@RequestParam(value="id", required=true) int id) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        final DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>();
        
        long requestId = LocalSequenceGenerator.getNextSequence();
        PingRequestPdu request = new PingRequestPdu();
        String bigBody = "";
        for(int i = 0; i < 500; i++) {
            bigBody += " ping border ";
        }
        //request.setBody("ping border");
        request.setBody(bigBody);
        BorderConnection connection = borderConnectionProvider.getBorderConnection(id);
        try {
            connection.sendMessage(requestId, request);
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "IO exception");
        }

        String subject = LocalBusMessageClassRegistry.getMessageClassSubjectName(PingResponsePdu.class);
        localBusSubscriberBuilder.build(subject + "." + requestId, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                    Object pingResponse, String path) {
                long time = System.currentTimeMillis() - ((PingResponsePdu)pingResponse).getStartTick();
                RestResponse response = new RestResponse("Received ping response in " + time + " ms");
                deferredResult.setResult(response);
                
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                RestResponse response = new RestResponse("Ping timed out");
                deferredResult.setResult(response);
            }
        }).setTimeout(5000).create();
        
        return deferredResult;
    }
    
    @RequestMapping("addPersistServer")
    @RestReturn(ServerDTO.class)
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

        return new RestResponse(ConvertHelper.convert(server, ServerDTO.class));
    }
    
    @RequestMapping("updatePersistServer")
    @RestReturn(ServerDTO.class)
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
        return new RestResponse(ConvertHelper.convert(server, ServerDTO.class));   
    }
    
    @RequestMapping("listPersistServer")
    @RestReturn(value=ServerDTO.class, collection=true)
    public RestResponse listPersitServers() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<Server> servers = this.shardingProvider.listAllServers();
        return new RestResponse(servers.stream().map((r)-> {return ConvertHelper.convert(r, ServerDTO.class); }).collect(Collectors.toList()));
    }
    
    @RequestMapping("addNamespace")
    @RestReturn(NamespaceDTO.class)
    public RestResponse addNamespace(@RequestParam(value="name", required=true) String name) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        Namespace ns = new Namespace();
        ns.setName(name);
        this.nsProvider.createNamespace(ns);
        
        return new RestResponse(ConvertHelper.convert(ns, NamespaceDTO.class));
    }
    
    @RequestMapping("updateNamespace")
    @RestReturn(NamespaceDTO.class)
    public RestResponse updateNamespace(
            @RequestParam(value="id", required=true) int id, 
            @RequestParam(value="name", required=true) String name) {
        
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }

        Namespace ns = this.nsProvider.findNamespaceById(id);
        if(ns == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id parameter");
        ns.setName(name);
        this.nsProvider.updateNamespace(ns);
        return new RestResponse(ConvertHelper.convert(ns, NamespaceDTO.class));   
    }
    
    @RequestMapping("listNamespace")
    @RestReturn(value=NamespaceDTO.class, collection=true)
    public RestResponse listNamespaces() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        List<Namespace> namespaces = this.nsProvider.listNamespaces();
        return new RestResponse(namespaces.stream().map((r)-> { return ConvertHelper.convert(r, NamespaceDTO.class); }).collect(Collectors.toList()));
    }
    
    @RequestMapping("registerLogin")
    @RestReturn(value=UserLoginDTO.class)
    public RestResponse registerLogin(
        @RequestParam(value="borderId", required=true) int borderId, 
        @RequestParam(value="loginToken", required=true) String loginToken) {

        if(UserContext.current().getCallerApp() == null || UserContext.current().getCallerApp().getId().longValue() != App.APPID_EXTENSION)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
            
        LOGGER.info("Register login connection. border id: " + borderId + ", login token: " + loginToken);
        LoginToken token = LoginToken.fromTokenString(loginToken);
        if(token == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unrecoginized login token");
        
        UserLogin login = this.userService.registerLoginConnection(token, borderId);
        if(login == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid login token");
            
        return new RestResponse(login.toDto());
    }
    
    @RequestMapping("unregisterLogin")
    @RestReturn(value=UserLoginDTO.class)
    public RestResponse unregisterLogin(
        @RequestParam(value="borderId", required=true) int borderId, 
        @RequestParam(value="loginToken", required=true) String loginToken) {
    
        if(UserContext.current().getCallerApp() == null || UserContext.current().getCallerApp().getId().longValue() != App.APPID_EXTENSION)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        
        LOGGER.info("Register login connection. border id: " + borderId + ", login token: " + loginToken);
        LoginToken token = LoginToken.fromTokenString(loginToken);
        if(token == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unrecoginized login token");
        
        UserLogin login = this.userService.unregisterLoginConnection(token, borderId);
        if(login == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid login token");
            
        return new RestResponse(login.toDto());
    }
    
    @RequestMapping("listLogin")
    @RestReturn(value=UserLoginDTO.class, collection=true)
    public RestResponse listLogin(
        @RequestParam(value="uid", required=true) long uid) {
    
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        List<UserLogin> logins = this.userService.listUserLogins(uid);
        return new RestResponse(logins.stream().map((r) -> { return r.toDto(); }).collect(Collectors.toList()));
    }
}
