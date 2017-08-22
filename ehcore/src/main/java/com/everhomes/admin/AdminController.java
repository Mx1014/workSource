// @formatter:off
package com.everhomes.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.activity.ActivityService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bus.LocalBusMessageClassRegistry;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.codegen.GeneratorContext;
import com.everhomes.codegen.JavaGenerator;
import com.everhomes.codegen.ObjectiveCGenerator;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.ItemType;
import com.everhomes.discover.RestMethod;
import com.everhomes.discover.RestReturn;
import com.everhomes.mail.MailHandler;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.admin.AppCreateCommand;
import com.everhomes.rest.admin.DecodeContentPathCommand;
import com.everhomes.rest.admin.EncodeWebTokenCommand;
import com.everhomes.rest.admin.GetSequenceCommand;
import com.everhomes.rest.admin.NamespaceDTO;
import com.everhomes.rest.admin.SampleCommand;
import com.everhomes.rest.admin.SampleEmbedded;
import com.everhomes.rest.admin.SampleObject;
import com.everhomes.rest.admin.ServerDTO;
import com.everhomes.rest.border.AddBorderCommand;
import com.everhomes.rest.border.BorderDTO;
import com.everhomes.rest.border.UpdateBorderCommand;
import com.everhomes.rest.persist.server.AddPersistServerCommand;
import com.everhomes.rest.persist.server.UpdatePersistServerCommand;
import com.everhomes.rest.repeat.ExpressionDTO;
import com.everhomes.rest.rpc.server.PingRequestPdu;
import com.everhomes.rest.rpc.server.PingResponsePdu;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.ListLoginByPhoneCommand;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.RegisterLoginCommand;
import com.everhomes.rest.user.SendMessageTestCommand;
import com.everhomes.rest.user.SendMessageTestResponse;
import com.everhomes.rest.user.UserLoginDTO;
import com.everhomes.rest.user.UserLoginResponse;
import com.everhomes.rest.admin.DecodeWebTokenCommand;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.sequence.SequenceService;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.sharding.Server;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
//import com.everhomes.util.ConsoleOutputFilter;
//import com.everhomes.util.ConsoleOutputListener;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.FileHelper;
import com.everhomes.util.ReflectionHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.ZipHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
    
    @Autowired
    private SequenceService sequenceService;
    
    @Autowired
    private AppProvider appProvider;
    
    @Autowired
    private ActivityService activityService;
    
    @Value("#{T(java.util.Arrays).asList('${source.jars}')}")
    private List<String> jars;
    
    @Value("#{T(java.util.Arrays).asList('${source.excludes}')}")
    private List<String> excludes;
    
    @Value("${destination.dir}")
    private String destinationDir;
    
    @Value("${destination.dir.java}")
    private String destinationJavaDir;
    
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
    
    @Value("${javadoc.root}")
    private String javadocRoot;

    @Autowired
    private UserProvider userProvider;
    
    @RequireAuthentication(false)
    @RequestMapping("sample")
    @RestReturn(value=SampleObject.class, collection=true)
    public RestResponse sample(@Valid SampleCommand cmd) {
        LOGGER.info("Cmd: {}", cmd);
        
        List<SampleObject> responseList = new ArrayList<>();
        SampleObject obj = new SampleObject();
        obj.setByteValue((byte)1);
        obj.setIntValue(2);
        obj.setLongValue(3L);
        obj.setFloatValue(4.0f);
        obj.setDblValue(5.0);
        obj.setJavaDate(new Date());
        obj.setSqlTimestamp(new Timestamp(new Date().getTime()));
        
        List<SampleEmbedded> embeddedList = new ArrayList<>();
        SampleEmbedded embedded = new SampleEmbedded();
        embedded.setName("emmbeded name1");
        embedded.setValue("embedded value1");
        embeddedList.add(embedded);
        embedded = new SampleEmbedded();
        embedded.setName("emmbeded name2");
        embedded.setValue("embedded value2");
        embeddedList.add(embedded);
        obj.setEmbeddedList(embeddedList);
        
        Map<String, String> embeddedMap = new HashMap<>();
        embeddedMap.put("map name1", "map value1");
        embeddedMap.put("map name2", "map value2");
        obj.setEmbeddedMap(embeddedMap);
        responseList.add(obj);
        
        RestResponse response = new RestResponse(responseList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("codegen")
    @RestReturn(value=String.class, collection=true)
    public RestResponse codegen(@RequestParam(value="language", required=true) String language) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        
        if(!language.equalsIgnoreCase("objc") && !language.equalsIgnoreCase("java"))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Only objc (objective C) and java are currently supported");

        GeneratorContext context = new GeneratorContext();
        context.setClassNamePrefix(this.classNamePrefix);
        context.setDestinationDir(StringHelper.interpolate(this.destinationDir));
        context.setHeaderFileExtention(this.headerFileExtention);
        context.setSerializable(this.serializable);
        context.setSerializationHelper(this.serializationHelper);
        context.setSourceFileExtention(this.sourceFileExtention);
        context.setRestResponseBase(restResponseBase);
        context.setContextParam("dest.dir.java", StringHelper.interpolate(this.destinationJavaDir));
        LOGGER.info("Set destination of generating java, path={}", this.destinationJavaDir);
        
        checkItemTypeTest();
        
        if(language.equalsIgnoreCase("objc")) {
            ObjectiveCGenerator generator = new ObjectiveCGenerator();
            // generator.generatePojos(BorderDTO.class, context);
    
            // generate REST POJO objects
            jars.stream().forEach((jar)-> {
                try {
                    LOGGER.info("Load classes in jar, jarPath={}", jar);
                    Set<Class<?>> classes = ReflectionHelper.loadClassesInJar(StringHelper.interpolate(jar));
                    
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
            List<RestMethod> apiMethods = ControllerBase.getRestMethodList(javadocRoot, "core");
            for(RestMethod restMethod: apiMethods)
                generator.generateControllerPojos(restMethod, context);
            
            // generate API constants
            generator.generateApiConstants(apiMethods, context);
            
            // 改为使用标准maven打包方式，不再在此生成 by lqs 20160325
//            File srcDir = new File(this.destinationDir);
//            File dstFile = srcDir.getParentFile();
//            if(!dstFile.exists()) {
//                dstFile.mkdirs();
//            }
//            dstFile = new File(dstFile, "ehng-ios.zip");
//            
//            ZipHelper.compress(this.destinationDir, dstFile.getAbsolutePath());
        } else {
            JavaGenerator generator = new JavaGenerator();

            // generator controller API response objects
            List<RestMethod> apiMethods = ControllerBase.getRestMethodList(javadocRoot, "core");
            for (RestMethod restMethod : apiMethods)
                generator.generateControllerPojos(restMethod, context);
            
            // generate API constants
            String packageName = this.getClass().getPackage().getName();
            String[] tokens = packageName.split("\\.");
            tokens[tokens.length - 1] = "rest";

            context.setContextParam("ApiConstantPackage", StringUtils.join(tokens, '.'));
            generator.generateApiConstants(apiMethods, context);
        }
        
//        List<String> errorList = CodeGenContext.current().getErrorMessages();
//        CodeGenContext.clear();
//        if(errorList == null || errorList.size() == 0) {
//            return new RestResponse("OK");
//        } else {
//            RestResponse response = new RestResponse(errorList);
//            response.setErrorScope(ErrorCodes.SCOPE_GENERAL);
//            response.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
//            return response;
//        }
        return new RestResponse("OK");
    }
    
    private void checkItemTypeTest() {
        Field[] fields = ExpressionDTO.class.getDeclaredFields();
    
        if(fields != null) {
            for(Field field : fields) {
                LOGGER.debug("Find field for ExpressionDTO, fieldName={}", field.getName());
                if("expression".equals(field.getName())) {
                    ItemType itemType = field.getAnnotation(ItemType.class);
                    if(itemType != null) {
                        Class<?> itemClz = itemType.value();
                        String clsName = null;
                        if(itemClz != null) {
                            clsName = itemClz.getName();
                        }
                        LOGGER.debug("Find item type, clsName={}", clsName);
                    }
                } 
            }
        } else {
            LOGGER.debug("No field found for ExpressionDTO");
        }
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
    
    @RequestMapping("syncSequence")
    @RestReturn(String.class)
    public RestResponse syncSequence() {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
            UserContext.current().getUser().getId(), Privilege.Write, null)) {

            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }

         sequenceService.syncSequence();
        return new RestResponse("OK");
    }
    
    @RequestMapping("getSequence")
    @RestReturn(String.class)
    public RestResponse getSequence(GetSequenceCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
            UserContext.current().getUser().getId(), Privilege.Write, null)) {

            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }

         sequenceService.syncSequence();
        return new RestResponse("OK");
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
        
        try {
            connection.sendMessage(requestId, request);
            return deferredResult;
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "IO exception");
        }
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
        List<NamespaceDTO> dtos = namespaces.stream().map((r)-> { return ConvertHelper.convert(r, NamespaceDTO.class); }).collect(Collectors.toList());
        if(dtos != null) {
            NamespaceDTO dto = new NamespaceDTO();
            dto.setId(0);
            dto.setName("左邻默认");
            dtos.add(dto);
        }
        return new RestResponse(dtos);
    }
    
    @RequestMapping("registerLogin")
    @RestReturn(value=UserLoginDTO.class)
    public RestResponse registerLogin(@Valid RegisterLoginCommand cmd) {
        int borderId = cmd.getBorderId();
        String loginToken = cmd.getLoginToken();

        if(UserContext.current().getCallerApp() == null || UserContext.current().getCallerApp().getId().longValue() != App.APPID_EXTENSION)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
            
        LOGGER.info("Register login connection. border id: " + borderId + ", login token: " + loginToken);
        LoginToken token = WebTokenGenerator.getInstance().fromWebToken(loginToken, LoginToken.class);
        if(token == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unrecoginized login token");
        
        UserLogin login = this.userService.registerLoginConnection(token, borderId, cmd.getBorderSessionId());
        if(login == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid login token");
            
        return new RestResponse(login.toDto());
    }
    
    @RequestMapping("unregisterLogin")
    @RestReturn(value=UserLoginDTO.class)
    public RestResponse unregisterLogin(@Valid RegisterLoginCommand cmd) {
        int borderId = cmd.getBorderId();
        String loginToken = cmd.getLoginToken();
    
        if(UserContext.current().getCallerApp() == null || UserContext.current().getCallerApp().getId().longValue() != App.APPID_EXTENSION)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        
        LOGGER.info("Register login connection. border id: " + borderId + ", login token: " + loginToken);
        LoginToken token = WebTokenGenerator.getInstance().fromWebToken(loginToken, LoginToken.class);
        if(token == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Unrecoginized login token");
        
        UserLogin login = this.userService.unregisterLoginConnection(token, borderId, cmd.getBorderSessionId());
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
    
    @RequestMapping("listLoginByPhone")
    @RestReturn(value=UserLoginResponse.class)
    public RestResponse listLoginByPhone(ListLoginByPhoneCommand cmd) {
    
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
            UserContext.current().getUser().getId(), Privilege.Visible, null)) {
        
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        

        return new RestResponse(this.userService.listLoginsByPhone(cmd));
    }
    
    @RequestMapping("createApp")
    @RestReturn(value=String.class)
    public RestResponse createApp(@Valid AppCreateCommand cmd) {
        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(), 
                UserContext.current().getUser().getId(), Privilege.Visible, null)) {
            
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
            }
        
        App app = new App();
        app.setAppKey(cmd.getAppKey());
        app.setCreatorUid(UserContext.current().getUser().getId());
        app.setDescription(cmd.getDescription());
        app.setName(cmd.getName());
        app.setSecretKey(cmd.getSecretKey());
        app.setStatus((byte)1);
        this.appProvider.createApp(app);
        
        return new RestResponse("ok");
    }
    
    @RequireAuthentication(false)
    @RequestMapping(value="getIosZip")
    public ModelAndView getIosZip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        java.io.BufferedInputStream bis = null;  
        java.io.BufferedOutputStream bos = null;  
        try {  
            File srcDir = new File(this.destinationDir);
            File dstFile = srcDir.getParentFile();
            if(!dstFile.exists()) {
                dstFile.mkdirs();
            }
            File zipFile = new File(dstFile, "ehng-ios.zip");
            String fileName = zipFile.getName();
            
            long fileLength = zipFile.length();
            response.setContentType("application/octet-stream;");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));  

            bis = new BufferedInputStream(new FileInputStream(zipFile));
            bos = new BufferedOutputStream(response.getOutputStream());
            
            FileHelper.readAndWriteStream(bis, bos);
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Failed to download the package file");
        } finally {  
            FileHelper.closeInputStream(bis);
            FileHelper.closeOuputStream(bos);
        }          

        return null;
    }
    
    /**
     * 
     * 解开webtoken
     * @return
     */
    @RequestMapping("decodeWebToken")
    @RestReturn(String.class)
    public RestResponse decodeWebToken(@Valid DecodeWebTokenCommand cmd) {        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        String webToken = cmd.getWebToken();
        String tokenType = cmd.getTokenType();
        
        if(webToken == null || webToken.trim().length() == 0) {
            LOGGER.error("Web token may not be empty, cmd=" + cmd);
            throw new IllegalArgumentException("Web token may not be empty");
        }
        
        if(tokenType == null || tokenType.trim().length() == 0) {
            LOGGER.error("Token type may not be empty, cmd=" + cmd);
            throw new IllegalArgumentException("Token type may not be empty");
        }
        
        @SuppressWarnings("rawtypes")
        Class clsObj = null;
        try {
            clsObj = Class.forName(tokenType);
        } catch (Exception e) {
            LOGGER.error("Invalid token type, cmd=" + cmd, e);
            throw new IllegalArgumentException("Invalid token type");
        }
        
        @SuppressWarnings("unchecked")
        Object obj = WebTokenGenerator.getInstance().fromWebToken(webToken, clsObj);
        
        RestResponse response = new RestResponse(StringHelper.toJsonString(obj));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * 
     * 解开content server产生的路径
     * @return
     */
    @RequestMapping("decodeContentPath")
    @RestReturn(String.class)
    public RestResponse decodeContentPath(@Valid DecodeContentPathCommand cmd) {        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        String path = cmd.getPath();
        if(path == null || path.trim().length() == 0) {
            LOGGER.error("Content path may not be empty, cmd=" + cmd);
            throw new IllegalArgumentException("Content path may not be empty");
        }
        
        String url = com.everhomes.contentserver.Generator.decodeUrl(path);
        RestResponse response = new RestResponse(url);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * 
     * 在线消息测试
     * @return
     */
    @RequestMapping("messageTest")
    @RestReturn(SendMessageTestResponse.class)
    public RestResponse sendMessageTest(@Valid SendMessageTestCommand cmd) {
        SendMessageTestResponse msgResp = new SendMessageTestResponse();
        msgResp.setText(userService.sendMessageTest(cmd));
        RestResponse response = new RestResponse(msgResp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    @RequestMapping("pushTest")
    @RestReturn(SendMessageTestResponse.class)
    public RestResponse pushMessageTest(@Valid SendMessageTestCommand cmd) {
        SendMessageTestResponse msgResp = new SendMessageTestResponse();
        msgResp.setText(userService.pushMessageTest(cmd));
        RestResponse response = new RestResponse(msgResp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    /**
     * 
     * 生成webtoken
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping("encodeWebToken")
    @RestReturn(String.class)
    public RestResponse encodeWebToken(@Valid EncodeWebTokenCommand cmd) {        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        String json = cmd.getJson();
        String tokenType = cmd.getTokenType();
        
        if(json == null || json.trim().length() == 0) {
            LOGGER.error("json may not be empty, cmd=" + cmd);
            throw new IllegalArgumentException("json may not be empty");
        }
        
        if(tokenType == null || tokenType.trim().length() == 0) {
            LOGGER.error("Token type may not be empty, cmd=" + cmd);
            throw new IllegalArgumentException("Token type may not be empty");
        }
        
        @SuppressWarnings("rawtypes")
        Class clsObj = null;
        Object tokenObj = null;
        try {
            clsObj = Class.forName(tokenType);
            tokenObj = clsObj.newInstance();
        } catch (Exception e) {
            LOGGER.error("Invalid token type, cmd=" + cmd, e);
            throw new IllegalArgumentException("Invalid token type");
        }
        
        Gson gson = new Gson(); 
        tokenObj = gson.fromJson(json, clsObj);
        
        Object obj = WebTokenGenerator.getInstance().toWebToken(tokenObj);
        
        RestResponse response = new RestResponse(StringHelper.toJsonString(obj));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        
        return response;
    }
    
    @RequestMapping("warningActivity")
    @RestReturn(String.class)
    public RestResponse warningActivity(){
    	activityService.activityWarningSchedule();
    	return new RestResponse();
    }
    
//    @RequestMapping(value="sseConsole")
//    public ResponseBodyEmitter sseConsole() {
//        if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
//                UserContext.current().getUser().getId(), Privilege.Visible, null)) {
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
//        }
//        
//        SseEmitter emitter = new ConsoleOutputSseEmitter(Long.MAX_VALUE);
//        return emitter;
//    }
    
//    private static class ConsoleOutputSseEmitter extends SseEmitter implements ConsoleOutputListener {
//        public ConsoleOutputSseEmitter(Long timeout) {
//            super(timeout);
//            ConsoleOutputFilter.subscribe(this);
//        }
//        
//        @Override
//        public void onConsoleOutput(String output) {
//            try {
//                send(SseEmitter.event().name("SSE.Console").data(output));
//            } catch (Throwable e) {
//                ConsoleOutputFilter.unsubscribe(this);
//                complete();
//            }
//        }
//    }
    
    @RequestMapping("testSendMail")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse testSendMail(@RequestParam("toMail") String toMail){
    	String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        handler.sendMail(0, null, toMail, "the mail subject", "the mail body");
    	return new RestResponse();
    }
    
}
