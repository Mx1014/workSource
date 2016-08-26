// @formatter:off
package com.everhomes.test.junit.payment;


import java.io.File;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.device.CreateCertCommand;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.SendCardVerifyCodeCommand;
import com.everhomes.rest.payment.SendCardVerifyCodeRestResponse;
import com.everhomes.rest.payment.SetCardPasswordCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentCards;
import com.everhomes.server.schema.tables.records.EhPaymentCardsRecord;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.util.EncryptionUtils;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class SetCardPasswordTest extends BaseLoginAuthTestCase {
	
	private static final String[] certNames = {"taotaogu.keystore","taotaogu.pin3.crt","taotaogu.server.cer","taotaogu.client.pfx"};
	private static final String[] certPass = {"jxd,123456,123456","","","123456"};
	private static final String[] fileNames = {"jxd.keystore","pin3.crt","server.cer","client.pfx"};

    @Before
    public void setUp() {
        super.setUp();
        testCreateCert();
        
    }
    public void testCreateCert() {
        
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        for(int i=0;i<certNames.length;i++){
        	CreateCertCommand cmd = new CreateCertCommand();
            cmd.setCertKey("");
            cmd.setCertPass(certPass[i]);
            cmd.setCertType(2);
            cmd.setName(certNames[i]);
            String faileName = "data/json/paymentcard/"+fileNames[i];
            String filePath = dbProvider.getAbsolutePathFromClassPath(faileName);
            String commandRelativeUri = "/pusher/createCert";
           // RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
            RestResponse response = httpClientService.postFile(commandRelativeUri, cmd,new File(filePath), RestResponse.class,context);

            assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
            assertTrue("SetCardPassword, response=" + 
                StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        }
        
    }
    
    @Test
    public void testSetCardPassword() {
        
    	String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String mobile = "13265549907";
        String oldPassword = "123456";
        String newPassword = "654321";
        Long cardId = 1L;
    	
        String userIdentifier = "13265549907";
        String plainTexPassword = "123456";
        Integer namespaceId = 999990;
        // 登录时不传namepsace，默认为左邻域空间
        logon(namespaceId, userIdentifier, plainTexPassword);
        
        SetCardPasswordCommand cmd = new SetCardPasswordCommand();
        cmd.setCardId(cardId);
        cmd.setNewPassword(newPassword);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setOldPassword(oldPassword);
        
        String commandRelativeUri = "/payment/setCardPassword";
        RestResponse response = httpClientService.restPost(commandRelativeUri, cmd, RestResponse.class,context);
        
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("SetCardPassword, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        DSLContext context = dbProvider.getDslContext();
        SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
		query.addConditions(Tables.EH_PAYMENT_CARDS.MOBILE.eq(mobile));
		EhPaymentCards result = ConvertHelper.convert(query.fetchOne(), EhPaymentCards.class);
        
		assertNotNull(result);
		assertEquals(EncryptionUtils.hashPassword(newPassword), result.getPassword());
        
    }
    
    public void testSendCardVerifyCode() {
        
    	String ownerType = "community";
        Long ownerId = 240111044331051500L;
        String mobile = "13265549907";
    	
//        String userIdentifier = "13265549907";
//        String plainTexPassword = "123456";
//        Integer namespaceId = 999990;
//        // 登录时不传namepsace，默认为左邻域空间
//        logon(namespaceId, userIdentifier, plainTexPassword);
        
        SendCardVerifyCodeCommand cmd = new SendCardVerifyCodeCommand();
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setMobile(mobile);
        
        String commandRelativeUri = "/payment/sendCardVerifyCode";
        SendCardVerifyCodeRestResponse response = httpClientService.restPost(commandRelativeUri, cmd, SendCardVerifyCodeRestResponse.class,context);
        
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response);
        assertTrue("SetCardPassword, response=" + 
            StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
        
        String verifyCode = response.getResponse().getVerifyCode();
        
        ResetCardPasswordCommand cmd2 = new ResetCardPasswordCommand();
        cmd2.setOwnerId(ownerId);
        cmd2.setOwnerType(ownerType);
        cmd2.setMobile(mobile);
        cmd2.setCardId(1L);
        cmd2.setVerifyCode(verifyCode);
        cmd2.setNewPassword("123456");
        
        String commandRelativeUri2 = "/payment/resetCardPassword";
        RestResponse response2 = httpClientService.restPost(commandRelativeUri2, cmd2, RestResponse.class,context);
        
        assertNotNull("The reponse of updateCardRechargeOrder may not be null", response2);
        assertTrue("SetCardPassword, response2=" + 
            StringHelper.toJsonString(response2), httpClientService.isReponseSuccess(response2));
    }
    
    @After
    public void tearDown() {
    	testSendCardVerifyCode();
    	super.tearDown();
        logoff();
    }
    
    protected void initCustomData() {
        String cardIssuerFilePath = "data/json/3.4.x-test-data-cardissuer_160617.txt";
        String filePath = dbProvider.getAbsolutePathFromClassPath(cardIssuerFilePath);
        dbProvider.loadJsonFileToDatabase(filePath, false);
    }
}

