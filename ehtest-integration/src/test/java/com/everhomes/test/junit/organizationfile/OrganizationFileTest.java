package com.everhomes.test.junit.organizationfile;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.objectstorage.OsObjectStatus;
import com.everhomes.rest.organizationfile.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhOsObjectDownloadLogsRecord;
import com.everhomes.server.schema.tables.records.EhOsObjectsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class OrganizationFileTest extends BaseLoginAuthTestCase {

    //1. 文件上传成功后创建文件记录
    private static final String CREATE_ORGANIZATION_FILE_URL = "/orgfile/createOrganizationFile";
    //2. 根据小区列出物业公司给普通公司下载的文件
    private static final String SEARCH_ORGANIZATION_FILE_BY_COMMUNITY_URL = "/orgfile/searchOrganizationFileByCommunity";
    //3. 根据公司列出物业公司给普通公司下载的文件
    private static final String SEARCH_ORGANIZATION_FILE_BY_ORGANIZATION_URL = "/orgfile/searchOrganizationFileByOrganization";
    //4. 创建下载记录（点击下载按钮后调用此接口）
    private static final String CREATE_ORGANIZATION_FILE_DOWNLOAD_LOG_URL = "/orgfile/createOrganizationFileDownloadLog";
    //5. 获取一个文件的下载记录
    private static final String LIST_ORGANIZATION_FILE_DOWNLOAD_LOGS_URL = "/orgfile/listOrganizationFileDownloadLogs";
    //6. 删除文件
    private static final String DELETE_ORGANIZATION_FILE_URL = "/orgfile/deleteOrganizationFile";

    private String userIdentifier = "12900000001";
    private String plainTextPwd = "123456";
    private Integer namespaceId = 0;
    private Long communityId = 24210090697425925L;
    private Long organizationId = 1000750L;

    //1. 文件上传成功后创建文件记录
    @Test
    public void testCreateOrganizationFile() {
        logon();
        CreateOrganizationFileCommand cmd = new CreateOrganizationFileCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        String contentUri = "cs://1/image/aW1hZ2UvTVRwbU5UQTRNbUU1TlRObU5qRmlZVGhtTnpSak1qQTBOekkzWm1JNU9UQm1PQQ==";
        cmd.setContentUri(contentUri);
        CreateOrganizationFileRestResponse response = httpClientService.restPost(CREATE_ORGANIZATION_FILE_URL, cmd, CreateOrganizationFileRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        assertTrue(Objects.equals(response.getResponse().getSize(), "454.88KB"));
        assertTrue(Objects.equals(response.getResponse().getCreatorName(), "左邻管理员1"));

        EhOsObjectsRecord obj = dbProvider.getDslContext().selectFrom(Tables.EH_OS_OBJECTS).where(Tables.EH_OS_OBJECTS.CONTENT_URI.eq(contentUri)).fetchAny();
        assertNotNull(obj);
    }

    //2. 根据小区列出物业公司给普通公司下载的文件
    @Test
    public void testSearchOrganizationFileByCommunity() {
        logon();
        SearchOrganizationFileByCommunityCommand cmd = new SearchOrganizationFileByCommunityCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setCommunityId(communityId);
        cmd.setKeyword("cde");
        // cmd.setPageAnchor(1L);
        cmd.setPageSize(10);
        SearchOrganizationFileByCommunityRestResponse response = httpClientService.restPost(SEARCH_ORGANIZATION_FILE_BY_COMMUNITY_URL, cmd, SearchOrganizationFileByCommunityRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchOrganizationFileResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue(response.getResponse().getList().size() == 1);
    }

    //3. 根据公司列出物业公司给普通公司下载的文件
    @Test
    public void testSearchOrganizationFileByOrganization() {
        logon();
        SearchOrganizationFileByOrganizationCommand cmd = new SearchOrganizationFileByOrganizationCommand();
        cmd.setOrganizationId(organizationId);
        // cmd.setKeyword("");
        // cmd.setPageAnchor(1L);
        cmd.setPageSize(10);
        SearchOrganizationFileByOrganizationRestResponse response = httpClientService.restPost(SEARCH_ORGANIZATION_FILE_BY_ORGANIZATION_URL, cmd, SearchOrganizationFileByOrganizationRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        SearchOrganizationFileResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue(myResponse.getList().size() == 2);
    }

    //4. 创建下载记录（点击下载按钮后调用此接口）
    @Test
    public void testCreateOrganizationFileDownloadLog() {
        logon();
        CreateOrganizationFileDownloadLogCommand cmd = new CreateOrganizationFileDownloadLogCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setFileId(4L);
        CreateOrganizationFileDownloadLogRestResponse response = httpClientService.restPost(CREATE_ORGANIZATION_FILE_DOWNLOAD_LOG_URL, cmd, CreateOrganizationFileDownloadLogRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhOsObjectDownloadLogsRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS)
                .where(Tables.EH_OS_OBJECT_DOWNLOAD_LOGS.OBJECT_ID.eq(4L))
                .fetchAny();
        assertNotNull(record);

        EhOsObjectsRecord objRecord = dbProvider.getDslContext().selectFrom(Tables.EH_OS_OBJECTS)
                .where(Tables.EH_OS_OBJECTS.ID.eq(4L)).fetchAny();
        assertTrue(objRecord.getDownloadCount() == 1);
    }

    //5. 获取一个文件的下载记录
    @Test
    public void testListOrganizationFileDownloadLogs() {
        logon();
        ListOrganizationFileDownloadLogsCommand cmd = new ListOrganizationFileDownloadLogsCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setFileId(5L);
        // cmd.setPageAnchor(1L);
        cmd.setPageSize(10);
        ListOrganizationFileDownloadLogsRestResponse response = httpClientService.restPost(LIST_ORGANIZATION_FILE_DOWNLOAD_LOGS_URL, cmd, ListOrganizationFileDownloadLogsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        ListOrganizationFileDownloadLogsResponse myResponse = response.getResponse();
        assertNotNull(myResponse);

        assertTrue(myResponse.getList().size() == 1);
    }

    //6. 删除文件
    @Test
    public void testDeleteOrganizationFile() {
        logon();
        DeleteOrganizationFileCommand cmd = new DeleteOrganizationFileCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setFileId(6L);
        RestResponseBase response = httpClientService.restPost(DELETE_ORGANIZATION_FILE_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        EhOsObjectsRecord record = dbProvider.getDslContext().selectFrom(Tables.EH_OS_OBJECTS)
                .where(Tables.EH_OS_OBJECTS.STATUS.eq(OsObjectStatus.INACTIVE.getCode()))
                .and(Tables.EH_OS_OBJECTS.ID.eq(6L)).fetchAny();

        assertNotNull(record);
    }

    private void logon() {
        logon(namespaceId, userIdentifier, plainTextPwd);
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/3.4.x-test-data-zuolin_admin_user_160607.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);

        jsonFilePath = "data/json/organizationfile-1.0-test-data-170217.json";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
}