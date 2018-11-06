package com.everhomes.filemanagement;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.filemanagement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * <b>URL: /fileManagement/updateFileCatalog</b>
     * <p>1-3.修改文件目录</p>
     */
    @RequestMapping("updateFileCatalog")
    @RestReturn(value = FileCatalogDTO.class)
    public RestResponse updateFileCatalog(UpdateFileCatalogCommand cmd) {
        FileCatalogDTO res = fileManagementService.updateFileCatalog(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/getFileCatalog</b>
     * <p>1-4.查看文件目录</p>
     */
    @RequestMapping("getFileCatalog")
    @RestReturn(value = FileCatalogDTO.class)
    public RestResponse getFileCatalog(FileCatalogIdCommand cmd) {
        FileCatalogDTO res = fileManagementService.getFileCatalog(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/listFileCatalogs</b>
     * <p>2-1.文件目录列表</p>
     */
    @RequestMapping("listFileCatalogs")
    @RestReturn(value = ListFileCatalogResponse.class)
    public RestResponse listFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse res = fileManagementService.listFileCatalogs(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /fileManagement/listAvailableFileCatalogs</b>
     * <p>2-2.有效文件目录列表</p>
     */
    @RequestMapping("listAvailableFileCatalogs")
    @RestReturn(value = ListFileCatalogResponse.class)
    public RestResponse listAvailableFileCatalogs(ListFileCatalogsCommand cmd) {
        ListFileCatalogResponse res = fileManagementService.listAvailableFileCatalogs(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /fileManagement/searchFiles</b>
     * <p>2-3.目录全局搜索</p>
     */
    @RequestMapping("searchFiles")
    @RestReturn(value = SearchFileResponse.class)
    public RestResponse searchFiles(SearchFileCommand cmd) {
        SearchFileResponse res = fileManagementService.searchFiles(cmd);
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

    /**
     * <b>URL: /fileManagement/deleteFileContents</b>
     * <p>3-2.删除文件、文件夹</p>
     */
    @RequestMapping("deleteFileContents")
    @RestReturn(value = String.class)
    public RestResponse deleteFileContents(DeleteFileContentCommand cmd) {
        fileManagementService.deleteFileContents(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/updateFileContentName</b>
     * <p>3-3.修改文件、文件夹名称</p>
     */
    @RequestMapping("updateFileContentName")
    @RestReturn(value = FileContentDTO.class)
    public RestResponse updateFileContentName(UpdateFileContentNameCommand cmd) {
        FileContentDTO res = fileManagementService.updateFileContentName(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /fileManagement/listFileContents</b>
     * <p>3-4.查看文件列表</p>
     */
    @RequestMapping("listFileContents")
    @RestReturn(value = ListFileContentResponse.class)
    public RestResponse listFileContents(ListFileContentCommand cmd) {
        ListFileContentResponse res = fileManagementService.listFileContents(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
 
	/**
	 * <p>移动文件</p>
	 * <b>URL: /fileManagement/moveFileContent</b>
	 */
	@RequestMapping("moveFileContent")
	@RestReturn(String.class)
	public RestResponse moveFileContent(MoveFileContentCommand cmd){
		fileManagementService.moveFileContent(cmd);
		return new RestResponse();
	}

    /**
     * <p>根据后缀查询文件图标列表</p>
     * <b>URL: /fileManagement/getFileIconList</b>
     */
    @RequestMapping("getFileIconList")
    @RestReturn(GetFileIconListResponse.class)
    public RestResponse getFileIconList(){
        return new RestResponse(fileManagementService.getFileIconList());
    }

    /**
     * <p>获取目录文件夹的树状结构</p>
     * <b>URL: /fileManagement/listAllFloders</b>
     */
    @RequestMapping("listAllFloders")
    @RestReturn(ListAllFlodersResponse.class)
    public RestResponse listAllFloders(ListAllFlodersCommand cmd){
        return new RestResponse(fileManagementService.listAllFloders(cmd));
    }

}