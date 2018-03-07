package com.everhomes.banner;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.BannerTargetType;
import com.everhomes.rest.banner.admin.*;
import com.everhomes.rest.banner.targetdata.BannerActivityTargetData;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Created by xq.tian on 2018/3/8.
 */
public class BannerServiceImplTest extends CoreServerTestCase {

    @Autowired
    private BannerService bannerService;


    @Test
    public void countEnabledBannersByScope() {
        CountEnabledBannersByScopeCommand cmd = new CountEnabledBannersByScopeCommand();
        cmd.setNamespaceId(1000000);

        CountEnabledBannersByScopeResponse response = bannerService.countEnabledBannersByScope(cmd);
        assertNotNull(response);
        assertNotNull(response.getList());

        assertTrue(response.getList().size() == 1);
    }

    @Test
    public void reorderBanners() {
        ReorderBannersCommand cmd = new ReorderBannersCommand();
        cmd.setId(10659L);
        cmd.setExchangeId(10661L);

        bannerService.reorderBanners(cmd);
    }

    @Test
    public void updateBannerStatus() {
        UpdateBannerStatusCommand cmd = new UpdateBannerStatusCommand();
        cmd.setId(10659L);
        cmd.setStatus(BannerStatus.ACTIVE.getCode());

        bannerService.updateBannerStatus(cmd);
    }

    @Test
    public void createBanner() {
        UserContext.setCurrentUser(new User(User.SYSTEM_UID));

        CreateBannerCommand cmd = new CreateBannerCommand();
        cmd.setName("name");
        cmd.setNamespaceId(1000000);
        cmd.setPosterPath("cs://1/image/aW1hZ2UvTVRveVlqY3hOelF3TUdReE5EWmpaRFV5TXpsbU5qa3dNR015TjJRd09HTmtPQQ");
        cmd.setScopes(Arrays.asList(240111044331048623L, 2L));
        cmd.setTargetType(BannerTargetType.NONE.getCode());
        cmd.setTargetData("");

        bannerService.createBanner(cmd);
    }

    @Test
    public void updateBanner() {
        UpdateBannerCommand cmd = new UpdateBannerCommand();
        cmd.setId(10660L);
        cmd.setName("update name");
        cmd.setPosterPath("cs://aaaaaaaaa");
        cmd.setTargetType(BannerTargetType.ACTIVITY_DETAIL.getCode());

        BannerActivityTargetData targetData = new BannerActivityTargetData();
        targetData.setActivityId(1L);
        targetData.setActivityName("name");
        targetData.setEntryId(1L);
        targetData.setEntryName("dasdasd");

        cmd.setTargetData(StringHelper.toJsonString(targetData));

        BannerDTO dto = bannerService.updateBanner(cmd);

        assertNotNull(dto);
        assertTrue(dto.getActionType() == ActionType.ROUTER.getCode());
        assertEquals(dto.getActionData(), "{\"url\":\"zl://activity/d?forumId=1&topicId=1\"}");
    }

    @Test
    public void listBanners() {
        ListBannersCommand cmd = new ListBannersCommand();
        cmd.setNamespaceId(1000000);
        cmd.setPageSize(10);
        cmd.setScope(1L);

        ListBannersResponse response = bannerService.listBanners(cmd);
        assertNotNull(response);
        assertNotNull(response.getBanners());
    }
}