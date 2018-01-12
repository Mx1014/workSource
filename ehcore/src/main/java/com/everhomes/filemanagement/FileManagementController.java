package com.everhomes.filemanagement;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.filemanagement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fileManagement")
public class FileManagementController extends ControllerBase {

    @Autowired
    FileManagementService fileManagementService;

    /**
     * <b>URL: /fileManagement/addFileCatalog</b>
     * <p>1-1.新建文件目录</p>
     */
    @RequestMapping("addFileCatalog")
    @RestReturn(value = FileCatalogDTO.class)
    public RestResponse addFileCatalog(AddFileCatalogCommand cmd) {
        FileCatalogDTO res = fileManagementService.addFileCatalog(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/deleteFileCatalog</b>
     * <p>1-2.删除文件目录</p>
     */
    @RequestMapping("deleteFileCatalog")
    @RestReturn(value = String.class)
    public RestResponse deleteFileCatalog(FileCatalogIdCommand cmd) {
        fileManagementService.deleteFileCatalog(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/updateFileCatalogName</b>
     * <p>1-3.修改文件目录名称</p>
     */
    @RequestMapping("updateFileCatalogName")
    @RestReturn(value = FileCatalogDTO.class)
    public RestResponse updateFileCatalogName(UpdateFileCatalogNameCommand cmd) {
        FileCatalogDTO res = fileManagementService.updateFileCatalogName(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/listFileCatalogs</b>
     * <p>1-4.文件目录列表</p>
     */
    @RequestMapping("listFileCatalogs")
    @RestReturn(value = FileCatalogDTO.class)
    public RestResponse listFileCatalogs(ListFileCatalogsCommand cmd) {
        FileCatalogDTO res = fileManagementService.listFileCatalogs(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/addFileCatalogScopes</b>
     * <p>2-1.新增目录可见人员</p>
     */
    @RequestMapping("addFileCatalogScopes")
    @RestReturn(value = FileCatalogScopeDTO.class, collection = true)
    public RestResponse addFileCatalogScopes(AddFileCatalogScopesCommand cmd) {
        List<FileCatalogDTO> res = fileManagementService.addFileCatalogScopes(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/deleteFileCatalogScopes</b>
     * <p>2-2.删除目录可见人员</p>
     */
    @RequestMapping("deleteFileCatalogScopes")
    @RestReturn(value = String.class)
    public RestResponse deleteFileCatalogScopes(FileCatalogScopesIdCommand cmd) {
        fileManagementService.deleteFileCatalogScopes(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/enableFileCatalogScopeDownload</b>
     * <p>2-3.添加人员下载权限</p>
     */
    @RequestMapping("enableFileCatalogScopeDownload")
    @RestReturn(value = String.class)
    public RestResponse enableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd) {
        fileManagementService.enableFileCatalogScopeDownload(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/disableFileCatalogScopeDownload</b>
     * <p>2-4.取消人员下载权限</p>
     */
    @RequestMapping("disableFileCatalogScopeDownload")
    @RestReturn(value = String.class)
    public RestResponse disableFileCatalogScopeDownload(FileCatalogScopesIdCommand cmd) {
        fileManagementService.disableFileCatalogScopeDownload(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/listFileCatalogScopes</b>
     * <p>2-6.查看目录可见人员</p>
     */
    @RequestMapping("listFileCatalogScopes")
    @RestReturn(value = ListFielCatalogScopeResponse.class)
    public RestResponse listFileCatalogScopes(FileCatalogIdCommand cmd) {
        ListFielCatalogScopeResponse res = fileManagementService.listFileCatalogScopes(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/addFileContent</b>
     * <p>3-1.新增文件、文件夹</p>
     */
    @RequestMapping("addFileContent")
    @RestReturn(value = FileContentDTO.class)
    public RestResponse addFileContent(AddFileContentCommand cmd) {
        FileContentDTO res = fileManagementService.addFileContent(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
