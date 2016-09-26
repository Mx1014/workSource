package com.everhomes.contentserver;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.AclProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contentserver.AddConfigItemCommand;
import com.everhomes.rest.contentserver.AddContentServerCommand;
import com.everhomes.rest.contentserver.ContentServerDTO;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.sms.SmsProvider;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/contentServer")
public class ContentServerController extends ControllerBase {
    @Autowired
    private ContentServerService contentService;

    @Autowired
    private AclProvider aclProvider;

    @Autowired
    private SmsProvider smsProvider;

    @Autowired
    private ConnectionProvider connectionProvider;

    @RequestMapping(value = "addContentServer")
    @RequireAuthentication(true)
    @RestReturn(ContentServerDTO.class)
    public RestResponse addContentServer(@Valid AddContentServerCommand cmd) {
        // if (!this.aclProvider.checkAccess("system", null,
        // EhUsers.class.getSimpleName(), UserContext.current()
        // .getUser().getId(), Privilege.Write, null)) {
        //
        // throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
        // ErrorCodes.ERROR_ACCESS_DENIED,
        // "Access denied");
        // }
        return new RestResponse(contentService.addContentServer(cmd));
    }

    @RequestMapping("addConfig")
    @RequireAuthentication(true)
    @RestReturn(String.class)
    public RestResponse addConfig(@Valid AddConfigItemCommand cmd) {
        // if (!this.aclProvider.checkAccess("system", null,
        // EhUsers.class.getSimpleName(), UserContext.current()
        // .getUser().getId(), Privilege.Write, null)) {
        //
        // throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
        // ErrorCodes.ERROR_ACCESS_DENIED,
        // "Access denied");
        // }
        contentService.addConfigItem(cmd);
        return new RestResponse("ok");
    }

    @RequestMapping("listContentServers")
    @RequireAuthentication(true)
    @RestReturn(value = ContentServerDTO.class, collection = true)
    public RestResponse listContentServers() {
        // if (!this.aclProvider.checkAccess("system", null,
        // EhUsers.class.getSimpleName(), UserContext.current()
        // .getUser().getId(), Privilege.Read, null)) {
        //
        // throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
        // ErrorCodes.ERROR_ACCESS_DENIED,
        // "Access denied");
        // }
        List<ContentServerDTO> responseObject = contentService.listContservers();
        return new RestResponse(responseObject);
    }

    @RequestMapping("deleteContentServer")
    @RestReturn(value = String.class)
    public RestResponse deleteContentServer(@RequestParam(value = "nodeId", required = true) Long nodeId) {
        contentService.removeContentServer(nodeId);
        return new RestResponse("OK");
    }

    @RequestMapping("uploadFile")
    @RequireAuthentication(true)
    @RestReturn(value = UploadCsFileResponse.class, collection = true)
    public RestResponse uploadFileToContentServer(@RequestParam(value = "attachment") MultipartFile[] files) {
        List<UploadCsFileResponse> csFileResponseList = contentService.uploadFileToContentServer(files);
        
        RestResponse response = new RestResponse(csFileResponseList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
