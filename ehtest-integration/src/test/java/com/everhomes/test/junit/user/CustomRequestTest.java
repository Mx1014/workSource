package com.everhomes.test.junit.user;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.user.AddRequestCommand;
import com.everhomes.rest.user.GetCustomRequestInfoRestResponse;
import com.everhomes.rest.user.GetCustomRequestTemplateByNamespaceRestResponse;
import com.everhomes.rest.user.GetCustomRequestTemplateCommand;
import com.everhomes.rest.user.GetCustomRequestTemplateRestResponse;
import com.everhomes.rest.user.GetRequestInfoCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceRequests;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;

public class CustomRequestTest extends BaseLoginAuthTestCase {
	private static final String GET_CUSTOM_REQUEST_TEMPLATE = "/user/getCustomRequestTemplate";
	private static final String GET_CUSTOM_REQUEST_TEMPLATE_BY_NAMESPACE = "/user/getCustomRequestTemplateByNamespace";
	private static final String ADD_CUSTOM_REQUEST = "/user/addCustomRequest";
	private static final String GET_CUSTOM_REQUEST_INFO = "/user/getCustomRequestInfo";
	
	String ownerType = "community";
	Long ownerId = 240111044331048623L;
	Long categoryId = 1L;
	String templateType = "ServiceAlliance";
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	public void testGetCustomRequestTemplate() {
		
		logon();

		String uri = GET_CUSTOM_REQUEST_TEMPLATE;
		GetCustomRequestTemplateCommand cmd = new GetCustomRequestTemplateCommand();
		cmd.setTemplateType(templateType);
		
		GetCustomRequestTemplateRestResponse resp = httpClientService.restPost(uri, cmd, GetCustomRequestTemplateRestResponse.class);
		assertNotNull(resp);
		assertTrue("BP融资模板".equals(resp.getResponse().getName()));
		
	}
	
	@Test
	public void testGetCustomRequestTemplateByNamespace() {
		
		logon();

		String uri = GET_CUSTOM_REQUEST_TEMPLATE_BY_NAMESPACE;
		
		GetCustomRequestTemplateByNamespaceRestResponse resp = httpClientService.restPost(uri, null, GetCustomRequestTemplateByNamespaceRestResponse.class);
		assertNotNull(resp);
		assertTrue(resp.getResponse().size() == 1);
		assertTrue("BP融资模板".equals(resp.getResponse().get(0).getName()));
		
	}
	
	@Test
	public void testAddCustomRequest() {
		
		logon();

		deleteDbRequest();
		String uri = ADD_CUSTOM_REQUEST;
		
		AddRequestCommand cmd = new AddRequestCommand();
		//怎么去掉转义字符\?
		cmd.setRequestJson("{\\\"name\\\":\\\"haha\\\",\\\"mobile\\\":\\\"12100009999\\\",\\\"organizationName\\\":\\\"fhhd\\\",\\\"cityName\\\":\\\"hjfhfd\\\",\\\"industry\\\":\\\"dhgdfu\\\",\\\"financingStage\\\":\\\"A轮\\\",\\\"financingAmount\\\":9634.00,\\\"transferShares\\\":0.1,\\\"projectDesc\\\":\\\"uuigewrhewuigrghgygwuehrb tuigr\\\"]\\\"}");
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setServiceAllianceId(1L);
		cmd.setTemplateType(templateType);
		cmd.setType(2L);
		httpClientService.restPost(uri, cmd, StringRestResponse.class);
		
		List<EhServiceAllianceRequests> requests = getDbRequest();
		assertNotNull(requests);
		assertTrue("haha".equals(requests.get(0).getName()));
		
	}
	
	@Test
	public void testGetCustomRequestInfo() {
		
		logon();
		
		String uri = GET_CUSTOM_REQUEST_INFO;
		
		GetRequestInfoCommand cmd = new GetRequestInfoCommand();
		
		cmd.setTemplateType(templateType);
		cmd.setId(1L);
		GetCustomRequestInfoRestResponse resp = httpClientService.restPost(uri, cmd, GetCustomRequestInfoRestResponse.class);
		
		
		assertNotNull(resp);
		assertTrue(resp.getResponse().size() == 10);
		
	}
	
	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/yellowpage-test-data.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
		
		jsonFilePath = "data/json/customrequest-test-data.txt";
        fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
        dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
        
	}
	
	@After
	public void tearDown() {
		logoff();
	}
	
	private void logon() {
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		logon(null, userIdentifier, plainTexPassword);
	}
	
	private List<EhServiceAllianceRequests> getDbRequest() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_SERVICE_ALLIANCE_REQUESTS)
				.fetch().map(r -> ConvertHelper.convert(r, EhServiceAllianceRequests.class));
	}
	
	private void deleteDbRequest() {
		DSLContext context = dbProvider.getDslContext();
		context.truncate(Tables.EH_SERVICE_ALLIANCE_REQUESTS).execute();
	}
}
