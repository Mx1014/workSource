// @formatter:off
package com.everhomes.organizationfile;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.organizationfile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xq.tian on 2017/2/16.
 */
@RequestMapping("/orgfile")
@RestController
public class OrganizationFileController extends ControllerBase {

    private final static RestResponse SUCCESS = new RestResponse() {
        { setErrorDescription("OK"); }
    };

    @Autowired
    private OrganizationFileService organizationFileService;

    /**
     * <p>文件上传成功后创建文件记录</p>
     * <b>URL: /orgfile/createOrganizationFile</b>
     */
    @RequestMapping("createOrganizationFile")
    @RestReturn(OrganizationFileDTO.class)
    public RestResponse createOrganizationFile(CreateOrganizationFileCommand cmd) {
        return response(organizationFileService.createOrganizationFile(cmd));
    }

    /**
     * <p>根据小区列出物业公司给普通公司下载的文件</p>
     * <b>URL: /orgfile/searchOrganizationFileByCommunity</b>
     */
    @RequestMapping("searchOrganizationFileByCommunity")
    @RestReturn(SearchOrganizationFileResponse.class)
    public RestResponse searchOrganizationFileByCommunity(SearchOrganizationFileByCommunityCommand cmd) {
        return response(organizationFileService.searchOrganizationFileByCommunity(cmd));
    }

    /**
     * <p>根据公司列出物业公司给普通公司下载的文件</p>
     * <b>URL: /orgfile/searchOrganizationFileByOrganization</b>
     */
    @RequestMapping("searchOrganizationFileByOrganization")
    @RestReturn(SearchOrganizationFileResponse.class)
    public RestResponse searchOrganizationFileByOrganization(SearchOrganizationFileByOrganizationCommand cmd) {
        return response(organizationFileService.searchOrganizationFileByOrganization(cmd));
    }

    /**
     * <p>创建下载记录（点击下载按钮后调用此接口）</p>
     * <b>URL: /orgfile/createOrganizationFileDownloadLog</b>
     */
    @RequestMapping("createOrganizationFileDownloadLog")
    @RestReturn(OrganizationFileDownloadLogsDTO.class)
    public RestResponse createOrganizationFileDownloadLog(CreateOrganizationFileDownloadLogCommand cmd) {
        return response(organizationFileService.createOrganizationFileDownloadLog(cmd));
    }

    /**
     * <p>获取一个文件的下载记录</p>
     * <b>URL: /orgfile/listOrganizationFileDownloadLogs</b>
     */
    @RequestMapping("listOrganizationFileDownloadLogs")
    @RestReturn(ListOrganizationFileDownloadLogsResponse.class)
    public RestResponse listOrganizationFileDownloadLogs(ListOrganizationFileDownloadLogsCommand cmd) {
        return response(organizationFileService.listOrganizationFileDownloadLogs(cmd));
    }

    /**
     * <p>删除文件</p>
     * <b>URL: /orgfile/deleteOrganizationFile</b>
     */
    @RequestMapping("deleteOrganizationFile")
    @RestReturn(String.class)
    public RestResponse deleteOrganizationFile(DeleteOrganizationFileCommand cmd) {
        organizationFileService.deleteOrganizationFile(cmd);
        return SUCCESS;
    }

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        return response;
    }
}