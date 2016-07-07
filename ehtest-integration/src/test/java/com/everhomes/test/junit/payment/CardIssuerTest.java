// @formatter:off
package com.everhomes.test.junit.payment;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.payment.CardIssuerDTO;
import com.everhomes.rest.payment.ListCardIssuerCommand;
import com.everhomes.rest.payment.ListCardIssuerRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.StringHelper;

public class CardIssuerTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testListCardIssuer() {
        String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/payment/listCardIssuer";
        ListCardIssuerCommand cmd = new ListCardIssuerCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        ListCardIssuerRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, ListCardIssuerRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectJoinStep<Record> query = context.select(Tables.EH_PAYMENT_CARD_ISSUERS.fields()).from(Tables.EH_PAYMENT_CARD_ISSUERS);
    	query.join(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES).on(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.ISSUER_ID
    			.eq(Tables.EH_PAYMENT_CARD_ISSUERS.ID));
        query.where(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.OWNER_TYPE.eq(ownerType))
        	.and(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.OWNER_ID.eq(ownerId));
        
        List<EhPaymentCardIssuers> result = new ArrayList<>();
        
        query.fetch().forEach(
        		r -> {
        			EhPaymentCardIssuers issuer = new EhPaymentCardIssuers();
        			issuer.setId(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.ID));
        			issuer.setName(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.NAME));
        			issuer.setDescription(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.DESCRIPTION));
        			issuer.setPayUrl(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.PAY_URL));
        			issuer.setAlipayRechargeAccount(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.ALIPAY_RECHARGE_ACCOUNT));
        			issuer.setWeixinRechargeAccount(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.WEIXIN_RECHARGE_ACCOUNT));
        			issuer.setVendorName(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.VENDOR_NAME));
        			issuer.setVendorData(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.VENDOR_DATA));
        			issuer.setCreateTime(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.CREATE_TIME));
        			issuer.setStatus(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.STATUS));
        			result.add(issuer);
        		});
        List<CardIssuerDTO> list = response.getResponse();
        assertEquals(list.size(), result.size());
        assertEquals(1, result.size());
        
    }
    
//    @Test
//    public void testCardIssuer() {
//        Integer namespaceId = 0;
//        Long id = 1L;
//        String userIdentifier = "12000000001";
//        String plainTexPassword = "123456";
//        // 登录时不传namepsace，默认为左邻域空间   
//        logon(null, userIdentifier, plainTexPassword);
//        
//        String commandRelativeUri = "/payment/listCardIssuer";
//        ListCardIssuerRestResponse response = httpClientService.restGet(commandRelativeUri, null, ListCardIssuerRestResponse.class,context);
//        
//        assertNotNull("The reponse of getting card issuer may not be null", response);
//        assertTrue("The user info should be get from server, response=" + 
//            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
////        assertEquals("User should be in 0 namespace", namespaceId, response.getResponse().getNamespaceId());
////        assertEquals("左邻李四", response.getResponse().getNickName());
//        
//        DSLContext context = dbProvider.getDslContext();
//        SelectQuery<EhPaymentCardIssuersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_ISSUERS);
//
//        query.addConditions(Tables.EH_PAYMENT_CARD_ISSUERS.ID.eq(id));
//        EhPaymentCardIssuers issuer =  ConvertHelper.convert(query.fetchOne(), EhPaymentCardIssuers.class);
//        assertNotNull("issuer cannot null", issuer);
//        assertEquals(new Long(1), issuer.getId());
//        
//    }
    
    @After
    public void tearDown() {
    	super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/3.4.x-test-data-cardissuer_160617.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

