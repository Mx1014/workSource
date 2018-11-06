package com.everhomes.contentserver;

import com.everhomes.acl.AclProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contentserver.*;
import com.everhomes.sms.SmsProvider;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

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
    
    /**
     * 
     * <b>URL: /contentServer/queryUploadId</b>
     * <p>获取二维码 Id</p>
     * @return uploadId 的字符串
     */
    @RequestMapping("queryUploadId")
    @RequireAuthentication(false)
    @RestReturn(value = String.class)
    public RestResponse queryUploadId() {
        String id = contentService.newUploadId();
        RestResponse response = new RestResponse(id);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * 
     * <b>URL: /contentServer/waitScan</b>
     * <p>等待二维码被扫描</p>
     * @return OK 成功
     */
    @RequestMapping("waitScan")
    @RequireAuthentication(false)
    @RestReturn(value = UploadFileInfoDTO.class)
    public DeferredResult<RestResponse> waitScan(@Valid UploadIdCommand cmd) {  
        return contentService.waitScan(cmd.getUploadId());
    }

    /**
     * 
     * <b>URL: /contentServer/signalScanEvent</b>
     * <p>通知服务器有人扫描，并带上相关的参数；如果出现 uploadId 过期 等错误，会在外层 errorCode 中有相关错误</p>
     * @return OK 成功
     */
    @RequestMapping("signalScanEvent")
    @RestReturn(value = String.class)
    public RestResponse signalScanEvent(UploadFileInfoCommand cmd) {
        String ok = contentService.signalScanEvent(cmd);
        RestResponse response = new RestResponse(ok);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /contentServer/waitComplete</b>
     * <p> 进到界面之后，一致调用这个函数等 app/WX 的通知；
     * 如果出现 uploadId 过期 等错误，会在外层 errorCode 中有相关错误;
     * 如果返回值：complete，则上传完成；continue 则可继续上传；timeout 则超时；
     * </p>
     * @return OK 成功
     */
    @RequestMapping("waitComplete")
    @RestReturn(value = String.class)
    public DeferredResult<RestResponse> waitComplete(@Valid UploadIdCommand cmd) {
        return contentService.waitComplete(cmd.getUploadId());
    }
    
    /**
     * 
     * <b>URL: /contentServer/updateUploadInfo</b>
     * <p>更新上传信息，每上传完成一个文件，都要调用这个函数；
     * 如果出现 uploadId 过期 等错误，会在外层 errorCode 中有相关错误;
     * </p>
     * @return OK 成功
     */
    @RequestMapping("updateUploadInfo")
    @RestReturn(value = String.class)
    public RestResponse updateUploadInfo(@Valid UploadFileInfoCommand cmd) {
        String ok = contentService.updateUploadInfo(cmd);
        RestResponse response = new RestResponse(ok);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /contentServer/queryUploadResult</b>
     * <p>获取上传文件的结果</p>
     * @return OK 成功
     */
    @RequestMapping("queryUploadResult")
    @RestReturn(value = UploadFileInfoDTO.class)
    public RestResponse queryUploadResult(@Valid UploadIdCommand cmd) {    
        UploadFileInfoDTO dto = contentService.queryUploadResult(cmd.getUploadId());
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /contentServer/markReadComplete</b>
     * <p>获取上传文件的结果</p>
     * @return OK 成功
     */
    @RequestMapping("markReadComplete")
    @RestReturn(value = String.class)
    public RestResponse markReadComplete(UploadIdCommand cmd) {
        UploadFileInfoDTO dto = contentService.queryUploadResult(cmd.getUploadId());
        RestResponse response = new RestResponse();
        if(dto != null) {
            response.setErrorCode(ErrorCodes.SUCCESS);
            response.setErrorDescription("OK");
        } else {
            response.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
            response.setErrorDescription("Error");            
        }
        
        return response;
    }

    /**
     *
     * <b>URL: /contentServer/parseSharedUri</b>
     * <p>解析图片 uri</p>
     */
    @RequestMapping("parseSharedUri")
    @RestReturn(value = CsFileLocationDTO.class)
    public RestResponse parseURI(@Valid ParseURICommand cmd) {
        String url = contentService.parseSharedUri(cmd.getUri());
        CsFileLocationDTO dto = new CsFileLocationDTO();
        dto.setUrl(url);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /contentServer/uploadFileByUrl</b>
     * <p>上传图片</p>
     */
    @RequestMapping("uploadFileByUrl")
    @RestReturn(value = CsFileLocationDTO.class)
    public RestResponse uploadFileByUrl(@Valid UploadFileCommand cmd) {
        CsFileLocationDTO dto = contentService.uploadFileByUrl(cmd.getFileName(), cmd.getUrl());
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
