// @formatter:off
package com.everhomes.organizationfile;

import com.everhomes.rest.organizationfile.*;

/**
 * Created by xq.tian on 2017/2/16.
 */
public interface OrganizationFileService {

    /**
     * 文件上传成功后创建文件记录
     */
    OrganizationFileDTO createOrganizationFile(CreateOrganizationFileCommand cmd);

    /**
     * 根据小区列出物业公司给普通公司下载的文件
     */
    SearchOrganizationFileResponse searchOrganizationFileByCommunity(SearchOrganizationFileByCommunityCommand cmd);

    /**
     * 根据公司列出物业公司给普通公司下载的文件
     */
    SearchOrganizationFileResponse searchOrganizationFileByOrganization(SearchOrganizationFileByOrganizationCommand cmd);

    /**
     * 创建一条文件的下载记录
     */
    OrganizationFileDownloadLogsDTO createOrganizationFileDownloadLog(CreateOrganizationFileDownloadLogCommand cmd);

    /**
     * 查询文件的下载记录
     */
    ListOrganizationFileDownloadLogsResponse listOrganizationFileDownloadLogs(ListOrganizationFileDownloadLogsCommand cmd);

    /**
     * 删除文件
     */
    void deleteOrganizationFile(DeleteOrganizationFileCommand cmd);
}
