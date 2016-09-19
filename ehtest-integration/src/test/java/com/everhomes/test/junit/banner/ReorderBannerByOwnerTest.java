// @formatter:off
package com.everhomes.test.junit.banner;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.BannerOrder;
import com.everhomes.rest.banner.BannerScope;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.banner.ReorderBannerByOwnerCommand;
import com.everhomes.rest.common.ScopeType;
import com.everhomes.server.schema.Tables;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class ReorderBannerByOwnerTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testReorderBanners() {
        Integer namespaceId = 2;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        byte scopeType = ScopeType.COMMUNITY.getCode();
        long scopeId = 12345678L;
        BannerScope scope = new BannerScope();
        scope.setScopeCode(scopeType);
        scope.setScopeId(scopeId);
        
        ReorderBannerByOwnerCommand cmd = new ReorderBannerByOwnerCommand();
        cmd.setOwnerType("organization");
        cmd.setOwnerId(1000750L);
        cmd.setScope(scope);
        
        List<BannerOrder> upcs = new ArrayList<>();
        BannerOrder ubbc = new BannerOrder();
        Integer order1 = 5;
        ubbc.setDefaultOrder(order1);
        ubbc.setId(1L);
        upcs.add(ubbc);
        
        ubbc = new BannerOrder();
        Integer order2 = 1;
        ubbc.setDefaultOrder(order2);
        ubbc.setId(2L);
        upcs.add(ubbc);
        
        ubbc = new BannerOrder();
        Integer order3 = 3;
        ubbc.setDefaultOrder(order3);
        ubbc.setId(3L);
        upcs.add(ubbc);
        
        cmd.setBanners(upcs);
        
        String commandRelativeUri = "/banner/reorderBannerByOwner";
        RestResponse response = httpClientService.restGet(commandRelativeUri, cmd, RestResponse.class, context);
        
        assertNotNull("The reponse of may not be null", response);
        assertTrue("The banner should be created, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        List<BannerDTO> dtos = context.selectFrom(Tables.EH_BANNERS)
        		.where(Tables.EH_BANNERS.STATUS.ne(BannerStatus.DELETE.getCode()))
        		.and(Tables.EH_BANNERS.SCOPE_CODE.eq(scopeType))
        		.and(Tables.EH_BANNERS.SCOPE_ID.eq(scopeId))
        		.orderBy(Tables.EH_BANNERS.ORDER.asc()).fetchInto(BannerDTO.class);
        
        assertEquals(order2, dtos.get(0).getOrder());
        assertEquals(order3, dtos.get(1).getOrder());
        assertEquals(order1, dtos.get(2).getOrder());
    }
    
    @After
    public void tearDown() {
        super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String jsonFilePath = "data/json/create-banner-by-owner-reorder-test-data.txt";
        String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
    }
}

