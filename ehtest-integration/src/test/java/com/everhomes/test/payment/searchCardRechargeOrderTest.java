// @formatter:off
package com.everhomes.test.payment;

import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.db.AccessSpec;
import com.everhomes.payment.PaymentCardRechargeOrder;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.SearchCardRechargeOrderRestResponse;
import com.everhomes.rest.user.GetUserInfoRestResponse;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuers;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhPaymentCardRechargeOrdersRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class searchCardRechargeOrderTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testSearchCardRechargeOrder() {
    	String ownerType = "";
        Long ownerId = 0L;
        String keyword = "";
        Long pageAnchor = null;
        Integer pageSize = 5;
        
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/payment/searchCardRechargeOrder";
        SearchCardRechargeOrderRestResponse response = httpClientService.restGet(commandRelativeUri, null, SearchCardRechargeOrderRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
//        assertEquals("User should be in 0 namespace", namespaceId, response.getResponse().getNamespaceId());
//        assertEquals("左邻李四", response.getResponse().getNickName());
        
        DSLContext context = dbProvider.getDslContext();
		SelectQuery<EhPaymentCardRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS);
    	
		if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.MOBILE.eq(keyword)));
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.PAID_TYPE.eq(rechargeType));
        
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_TIME.gt(startDate));
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_TIME.lt(endDate));
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_STATUS.eq(rechargeStatus));
        
    	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.PAY_STATUS.eq(CardOrderStatus.PAID.getCode()));
        query.addOrderBy(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        List<EhPaymentCardRechargeOrders> result =  query.fetch().map(r -> 
			ConvertHelper.convert(r, EhPaymentCardRechargeOrders.class));
        assertEquals(5, result.size());
        
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

