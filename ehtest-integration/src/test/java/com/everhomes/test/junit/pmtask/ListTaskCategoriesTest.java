// @formatter:off
package com.everhomes.test.junit.pmtask;

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
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeOrderDTO;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.CardUserDTO;
import com.everhomes.rest.payment.PaidTypeStatus;
import com.everhomes.rest.payment.SearchCardRechargeOrderCommand;
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

public class ListTaskCategoriesTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testSearchCardRechargeOrder() {
    	String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String keyword = "";
        Long pageAnchor = null;
        Integer pageSize = 5;
        String paidType = PaidTypeStatus.ALIPAY.getCode();
        Long startDate = null;
        Long endDate = null;
//        Byte rechargeStatus = CardRechargeStatus.RECHARGED.getCode();
        Byte rechargeStatus = null;
        
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        SearchCardRechargeOrderCommand cmd = new SearchCardRechargeOrderCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setKeyword(keyword);
        cmd.setRechargeStatus(rechargeStatus);
        cmd.setRechargeType(paidType);
        cmd.setStartDate(startDate);
        cmd.setEndDate(endDate);
        cmd.setPageAnchor(pageAnchor);
        cmd.setPageSize(pageSize);
        
        String commandRelativeUri = "/payment/searchCardRechargeOrder";
        SearchCardRechargeOrderRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchCardRechargeOrderRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
		SelectQuery<EhPaymentCardRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS);
    	
		if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.MOBILE.eq(keyword)));
        if(StringUtils.isNotBlank(paidType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.PAID_TYPE.eq(paidType));
        
        if(startDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_TIME.gt(new Timestamp(startDate)));
        if(endDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_TIME.lt(new Timestamp(endDate)));
        if(rechargeStatus != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_STATUS.eq(rechargeStatus));
        
    	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.PAY_STATUS.eq(CardOrderStatus.PAID.getCode()));
        query.addOrderBy(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.CREATE_TIME.desc());
        	query.addLimit(pageSize);
        
        List<EhPaymentCardRechargeOrders> result =  query.fetch().map(r -> 
			ConvertHelper.convert(r, EhPaymentCardRechargeOrders.class));
        List<CardRechargeOrderDTO> list = response.getResponse().getRequests();
        assertEquals(list.size(), result.size());
        
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

