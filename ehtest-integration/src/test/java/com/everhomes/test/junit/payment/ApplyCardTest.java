// @formatter:off
package com.everhomes.test.junit.payment;


import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.ApplyCardRestResponse;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.UpdateCardRechargeOrderCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPaymentCardsDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhPaymentCards;
import com.everhomes.server.schema.tables.records.EhPaymentCardRechargeOrdersRecord;
import com.everhomes.server.schema.tables.records.EhPaymentCardsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.util.EncryptionUtils;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class ApplyCardTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testApplyCard() {
    	
    	
    	String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String mobile = "13265549907";
        String password = "123456";
        Long issuerId = 1L;
    	
        DSLContext dslcontext = dbProvider.getDslContext();
        EhPaymentCardsDao dao = new EhPaymentCardsDao(dslcontext.configuration());
		EhPaymentCards card = ConvertHelper.convert(dao.fetchByMobile(mobile).isEmpty()?null:dao.fetchByMobile(mobile).get(0), EhPaymentCards.class);
		
		if(card != null)
			dao.delete(card);
        
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        ApplyCardCommand cmd = new ApplyCardCommand();
        cmd.setIssuerId(issuerId);
        cmd.setMobile(mobile);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setPassword(password);
        
        String commandRelativeUri = "/payment/applyCard";
        ApplyCardRestResponse response = httpClientService.restPost(commandRelativeUri, cmd, ApplyCardRestResponse.class,context);
        
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
		query.addConditions(Tables.EH_PAYMENT_CARDS.MOBILE.eq(mobile));
		EhPaymentCards result = ConvertHelper.convert(query.fetchOne(), EhPaymentCards.class);
        
		assertNotNull(result);
		assertEquals(mobile, result.getMobile());
		assertEquals(EncryptionUtils.hashPassword(password), result.getPassword());
        
    }
    
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

