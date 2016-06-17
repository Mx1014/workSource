// @formatter:off
package com.everhomes.test.payment;

import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.user.GetUserInfoRestResponse;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.server.schema.tables.daos.EhUsersDao;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class CardIssuerTest extends BaseLoginAuthTestCase {
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testCardIssuer() {
        Integer namespaceId = 0;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        // 登录时不传namepsace，默认为左邻域空间
        logon(null, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/payment/listCardIssuer";
        GetUserInfoRestResponse response = httpClientService.restGet(commandRelativeUri, null, GetUserInfoRestResponse.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertEquals("User should be in 0 namespace", namespaceId, response.getResponse().getNamespaceId());
        assertEquals("左邻李四", response.getResponse().getNickName());
        
        List<EhUserIdentifiers> result = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext();
        context.select().from(EH_USER_IDENTIFIERS)
            .where(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(userIdentifier))
            .and(EH_USER_IDENTIFIERS.CLAIM_STATUS.eq(IdentifierClaimStatus.CLAIMED.getCode()))
            .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
            .fetch().map((r) -> {
                result.add(ConvertHelper.convert(r, EhUserIdentifiers.class));
                return null;
            });
        assertEquals(1, result.size());
        assertEquals("User should be in 0 namespace", namespaceId, result.get(0).getNamespaceId());
        
        EhUsersDao dao = new EhUsersDao(context.configuration());
        EhUsers user = dao.findById(result.get(0).getOwnerUid());
        assertNotNull(user);
        assertEquals("左邻李四", user.getNickName());
    }
    
    @Test
    public void testTechparkNamespaceUserInfo() {
        Integer namespaceId = 1000000;
        String userIdentifier = "12000000001";
        String plainTexPassword = "123456";
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        String commandRelativeUri = "/user/getUserInfo";
        GetUserInfoRestResponse response = httpClientService.restGet(commandRelativeUri, null, GetUserInfoRestResponse.class);
        
        assertNotNull("The reponse of getting user info may not be null", response);
        assertTrue("The user info should be get from server, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        assertEquals("User should be in techpark namespace", namespaceId, response.getResponse().getNamespaceId());
        assertEquals("科技园李四", response.getResponse().getNickName());
    }
    
    @After
    public void tearDown() {
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/3.4.x-test-data-cardissuer_160617.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

