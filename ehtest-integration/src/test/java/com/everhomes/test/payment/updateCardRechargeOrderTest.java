// @formatter:off
package com.everhomes.test.payment;


import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.UpdateCardRechargeOrderCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardRechargeOrders;
import com.everhomes.server.schema.tables.records.EhPaymentCardRechargeOrdersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class updateCardRechargeOrderTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateCardRechargeOrder() {
        Long id = 1L;
        Byte rechargeStatus = CardRechargeStatus.COMPLETE.getCode();
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        UpdateCardRechargeOrderCommand cmd = new UpdateCardRechargeOrderCommand();
        cmd.setId(id);
        cmd.setRechargeStatus(rechargeStatus);
        
        String commandRelativeUri = "/payment/updateCardRechargeOrder";
        RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
        
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("updateCardRechargeOrder, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhPaymentCardRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS);
		query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.ID.eq(id));
		EhPaymentCardRechargeOrders result = ConvertHelper.convert(query.fetchOne(), EhPaymentCardRechargeOrders.class);
        
		assertNotNull(result);
		assertEquals(new Long(1), result.getId());
		assertEquals(rechargeStatus, result.getRechargeStatus());
        
    }
    
    @After
    public void tearDown() {
    	super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/paymentcard/3.4.x-test-data-cardissuer_160617.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

