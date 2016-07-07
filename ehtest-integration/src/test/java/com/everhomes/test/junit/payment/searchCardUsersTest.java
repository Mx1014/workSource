// @formatter:off
package com.everhomes.test.junit.payment;

import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.everhomes.rest.payment.CardUserDTO;
import com.everhomes.rest.payment.SearchCardUsersCommand;
import com.everhomes.rest.payment.SearchCardUsersRestResponse;
import com.everhomes.rest.user.GetUserInfoRestResponse;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuers;
import com.everhomes.server.schema.tables.pojos.EhPaymentCards;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhPaymentCardsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class searchCardUsersTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
   
    @Test
    public void testSearchCardUsers() {
    
        String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String keyword = "";
        Long pageAnchor = null;
        Integer pageSize = 5;
        
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        SearchCardUsersCommand cmd = new SearchCardUsersCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setKeyword(keyword);
        cmd.setPageAnchor(pageAnchor);
        cmd.setPageSize(pageSize);
        
        String commandRelativeUri = "/payment/searchCardUsers";
        SearchCardUsersRestResponse response = httpClientService.restGet(commandRelativeUri, cmd, SearchCardUsersRestResponse.class,context);
        
        assertNotNull("The reponse may not be null", response);
        assertTrue("response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);

    	if (pageAnchor != null && pageAnchor != 0)
    		query.addConditions(Tables.EH_PAYMENT_CARDS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
    	if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_ID.eq(ownerId));
     
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_PAYMENT_CARDS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARDS.MOBILE.eq(keyword)));
        query.addOrderBy(Tables.EH_PAYMENT_CARDS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        List<EhPaymentCards> result = new ArrayList<>();

        result = query.fetch().map(
        		r -> ConvertHelper.convert(r, EhPaymentCards.class));
        List<CardUserDTO> list = response.getResponse().getRequests();
        assertNotNull(list);
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

