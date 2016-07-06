package com.everhomes.test.junit.expansion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.techpark.expansion.CreateLeasePromotionCommand;
import com.everhomes.rest.techpark.expansion.LeasePromotionType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class expansionTest  extends BaseLoginAuthTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }
    

    protected void initCustomData() { 
        String userInfoFilePath = "data/json/3.4.x-test-data-create-organization_member_160605.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(userInfoFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
        
    }
    @After
    public void tearDown() {
        logoff();
    }

    //测试添加园区入驻招租
    @Test
    public void testCreateLeasePromotion() {
    	//admin add 
        Integer namespaceId = 0;
        String userIdentifier = "root";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/techpark/entry/createLeasePromotion";
        
        CreateLeasePromotionCommand cmd = new CreateLeasePromotionCommand();
        cmd.setBuildingId(1L);
        cmd.setSubject("园区入驻招租");
        cmd.setEnterTime(new Date().getTime());
        cmd.setRentAreas("20");
        cmd.setDescription("详情就是不知");
        cmd.setContacts("联系人");
        cmd.setContactPhone("135-2333-2233");
        cmd.setPosterUri("cs://w32423423423423423432423");
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        
        List<EhLeasePromotions> resultLP = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(Tables.EH_LEASE_PROMOTIONS) 
            .fetch().map((r) -> {
            	resultLP.add(ConvertHelper.convert(r, EhLeasePromotions.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user info may not be null", resultLP);
        assertEquals(1, resultLP.size());
        assertEquals(LeasePromotionType.ORDINARY.getCode(), resultLP.get(0).getRentType());
        
    }

    //测试添加工位申请招租
    @Test
    public void testCreateOfficeLeasePromotion() {
    	//admin add 
        Integer namespaceId = 0;
        String userIdentifier = "root";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/techpark/entry/createLeasePromotion";
        
        CreateLeasePromotionCommand cmd = new CreateLeasePromotionCommand();
        cmd.setBuildingId(1L);
        cmd.setSubject("园区入驻招租");
        cmd.setEnterTime(new Date().getTime());
        cmd.setRentAreas("20");
        cmd.setDescription("详情就是不知");
        cmd.setContacts("联系人");
        cmd.setContactPhone("135-2333-2233");
        cmd.setPosterUri("cs://w32423423423423423432423");
        cmd.setRentType(LeasePromotionType.OFFICE_CUBICLE.getCode());
        RestResponseBase response = httpClientService.restGet(commandRelativeUri, cmd, RestResponseBase.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        
        List<EhLeasePromotions> resultLP = new ArrayList<>();
        
        DSLContext context = dbProvider.getDslContext();
        context.select().from(Tables.EH_LEASE_PROMOTIONS) 
            .fetch().map((r) -> {
            	resultLP.add(ConvertHelper.convert(r, EhLeasePromotions.class));
                return null;
            });
        
        assertNotNull("The reponse of getting user info may not be null", resultLP);
        assertEquals(1, resultLP.size());
        assertEquals(LeasePromotionType.OFFICE_CUBICLE.getCode(), resultLP.get(0).getRentType());
    }
    
}
