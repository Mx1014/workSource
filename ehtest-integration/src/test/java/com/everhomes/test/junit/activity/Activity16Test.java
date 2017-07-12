package com.everhomes.test.junit.activity;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.activity.*;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class Activity16Test extends BaseLoginAuthTestCase {

    //24. 设置活动成果
    private static final String SET_ACTIVITY_ACHIEVEMENT_URL = "/activity/setActivityAchievement";
    //25. 查询活动成果
    private static final String GET_ACTIVITY_ACHIEVEMENT_URL = "/activity/getActivityAchievement";
    //26. 添加活动附件
    private static final String CREATE_ACTIVITY_ATTACHMENT_URL = "/activity/createActivityAttachment";
    //27. 删除活动附件
    private static final String DELETE_ACTIVITY_ATTACHMENT_URL = "/activity/deleteActivityAttachment";
    //28. 查询活动附件列表
    private static final String LIST_ACTIVITY_ATTACHMENTS_URL = "/activity/listActivityAttachments";
    //29. 下载活动附件
    private static final String DOWNLOAD_ACTIVITY_ATTACHMENT_URL = "/activity/downloadActivityAttachment";
    //30. 添加活动物资
    private static final String CREATE_ACTIVITY_GOODS_URL = "/activity/createActivityGoods";
    //31. 修改活动物资
    private static final String UPDATE_ACTIVITY_GOODS_URL = "/activity/updateActivityGoods";
    //32. 删除活动物资
    private static final String DELETE_ACTIVITY_GOODS_URL = "/activity/deleteActivityGoods";
    //33. 查询活动物资列表
    private static final String LIST_ACTIVITY_GOODS_URL = "/activity/listActivityGoods";
    //34. 查询活动物资
    private static final String GET_ACTIVITY_GOODS_URL = "/activity/getActivityGoods";

    //24. 设置活动成果
    @Test
    public void testSetActivityAchievement() {
        logon();
        SetActivityAchievementCommand cmd = new SetActivityAchievementCommand();
        cmd.setActivityId(1L);
        cmd.setAchievement("");
        cmd.setAchievementType("");

        RestResponseBase response = httpClientService.restPost(SET_ACTIVITY_ACHIEVEMENT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //25. 查询活动成果
    @Test
    public void testGetActivityAchievement() {
        logon();
        GetActivityAchievementCommand cmd = new GetActivityAchievementCommand();
        cmd.setActivityId(1L);

        GetActivityAchievementRestResponse response = httpClientService.restPost(GET_ACTIVITY_ACHIEVEMENT_URL, cmd, GetActivityAchievementRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // GetActivityAchievementResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //26. 添加活动附件
    @Test
    public void testCreateActivityAttachment() {
        logon();
        CreateActivityAttachmentCommand cmd = new CreateActivityAttachmentCommand();
        cmd.setActivityId(1L);
        cmd.setName("");
        cmd.setContentType("");
        cmd.setContentUri("");

        RestResponseBase response = httpClientService.restPost(CREATE_ACTIVITY_ATTACHMENT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //27. 删除活动附件
    @Test
    public void testDeleteActivityAttachment() {
        logon();
        DeleteActivityAttachmentCommand cmd = new DeleteActivityAttachmentCommand();
        cmd.setAttachmentId(1L);
        cmd.setActivityId(1L);

        RestResponseBase response = httpClientService.restPost(DELETE_ACTIVITY_ATTACHMENT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //28. 查询活动附件列表
    @Test
    public void testListActivityAttachments() {
        logon();
        ListActivityAttachmentsCommand cmd = new ListActivityAttachmentsCommand();
        cmd.setActivityId(1L);
        cmd.setPageAnchor(1L);
        cmd.setPageSize(0);

        ListActivityAttachmentsRestResponse response = httpClientService.restPost(LIST_ACTIVITY_ATTACHMENTS_URL, cmd, ListActivityAttachmentsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // ListActivityAttachmentsResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //29. 下载活动附件
    @Test
    public void testDownloadActivityAttachment() {
        logon();
        DownloadActivityAttachmentCommand cmd = new DownloadActivityAttachmentCommand();
        cmd.setAttachmentId(1L);
        cmd.setActivityId(1L);

        RestResponseBase response = httpClientService.restPost(DOWNLOAD_ACTIVITY_ATTACHMENT_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //30. 添加活动物资
    @Test
    public void testCreateActivityGoods() {
        logon();
        CreateActivityGoodsCommand cmd = new CreateActivityGoodsCommand();
        cmd.setActivityId(1L);
        cmd.setName("");
        cmd.setPrice(new BigDecimal("1"));
        cmd.setQuantity(0);
        cmd.setTotalPrice(new BigDecimal("1"));
        cmd.setHandlers("");

        RestResponseBase response = httpClientService.restPost(CREATE_ACTIVITY_GOODS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //31. 修改活动物资
    @Test
    public void testUpdateActivityGoods() {
        logon();
        UpdateActivityGoodsCommand cmd = new UpdateActivityGoodsCommand();
        cmd.setId(1L);
        cmd.setActivityId(1L);
        cmd.setName("");
        cmd.setPrice(new BigDecimal("1"));
        cmd.setQuantity(0);
        cmd.setTotalPrice(new BigDecimal("1"));
        cmd.setHandlers("");

        RestResponseBase response = httpClientService.restPost(UPDATE_ACTIVITY_GOODS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //32. 删除活动物资
    @Test
    public void testDeleteActivityGoods() {
        logon();
        DeleteActivityGoodsCommand cmd = new DeleteActivityGoodsCommand();
        cmd.setActivityId(1L);
        cmd.setGoodId(1L);

        RestResponseBase response = httpClientService.restPost(DELETE_ACTIVITY_GOODS_URL, cmd, RestResponseBase.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
    }

    //33. 查询活动物资列表
    @Test
    public void testListActivityGoods() {
        logon();
        ListActivityGoodsCommand cmd = new ListActivityGoodsCommand();
        cmd.setActivityId(1L);
        cmd.setPageAnchor(1L);
        cmd.setPageSize(0);

        ListActivityGoodsRestResponse response = httpClientService.restPost(LIST_ACTIVITY_GOODS_URL, cmd, ListActivityGoodsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // ListActivityGoodsResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    //34. 查询活动物资
    @Test
    public void testGetActivityGoods() {
        logon();
        GetActivityGoodsCommand cmd = new GetActivityGoodsCommand();
        cmd.setActivityId(1L);
        cmd.setGoodId(1L);

        GetActivityGoodsRestResponse response = httpClientService.restPost(GET_ACTIVITY_GOODS_URL, cmd, GetActivityGoodsRestResponse.class);
        assertNotNull(response);
        assertTrue("response = " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

        // GetActivityGoodsResponse myResponse = response.getResponse();
        // assertNotNull(myResponse);
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void initCustomData() {
        String jsonFilePath = "data/json/1.0.0-activity-test-data-161018.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }

    @After
    public void tearDown() {
        logoff();
    }

    private void logon() {
        String userIdentifier = "tt";
        String plainTexPassword = "123456";
        Integer namespaceId = 999995;
        logon(namespaceId, userIdentifier, plainTexPassword);
    }
}
