// @formatter:off
package com.everhomes.test.payment;


import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.payment.CardTransactionStatus;
import com.everhomes.rest.payment.SearchCardTransactionsCommand;
import com.everhomes.rest.payment.SearchCardTransactionsRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardTransactions;
import com.everhomes.server.schema.tables.records.EhPaymentCardTransactionsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class searchCardTransactionsTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testSearchCardTransactions() {
    	String ownerType = "";
        Long ownerId = 0L;
        String keyword = "";
        Long pageAnchor = null;
        Integer pageSize = 5;
        String consumeType = "";
        Long startDate = null;
        Long endDate = null;
        Byte status = CardTransactionStatus.PAIDED.getCode();
        
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        SearchCardTransactionsCommand cmd = new SearchCardTransactionsCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setKeyword(keyword);
        cmd.setStatus(status);
        cmd.setConsumeType(consumeType);
        cmd.setStartDate(startDate);
        cmd.setEndDate(endDate);
        cmd.setPageAnchor(pageAnchor);
        cmd.setPageSize(pageSize);
        
        String commandRelativeUri = "/payment/searchCardTransactions";
        SearchCardTransactionsRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchCardTransactionsRestResponse.class,context);
        
        assertNotNull("The reponse of getting card issuer may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
//        assertEquals("User should be in 0 namespace", namespaceId, response.getResponse().getNamespaceId());
//        assertEquals("左邻李四", response.getResponse().getNickName());
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhPaymentCardTransactionsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_TRANSACTIONS);
    	
		if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.OWNER_ID.eq(ownerId));
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARD_TRANSACTIONS.MOBILE.eq(keyword)));
//        if(StringUtils.isNotBlank(consumeType))
//        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.CONSUME_TYPE.eq(consumeType));
        
        if(startDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.TRANSACTION_TIME.gt(new Timestamp(startDate)));
        if(endDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.TRANSACTION_TIME.lt(new Timestamp(endDate)));
        if(status != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.STATUS.eq(status));
        
        query.addOrderBy(Tables.EH_PAYMENT_CARD_TRANSACTIONS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        List<EhPaymentCardTransactions> result =  query.fetch().map(r -> 
			ConvertHelper.convert(r, EhPaymentCardTransactions.class));
        assertEquals(1, result.size());
        
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

