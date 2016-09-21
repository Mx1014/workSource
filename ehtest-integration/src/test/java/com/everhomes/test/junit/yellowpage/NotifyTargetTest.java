package com.everhomes.test.junit.yellowpage;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.yellowPage.AddNotifyTargetCommand;
import com.everhomes.rest.yellowPage.DeleteNotifyTargetCommand;
import com.everhomes.rest.yellowPage.ListNotifyTargetsCommand;
import com.everhomes.rest.yellowPage.ListNotifyTargetsRestResponse;
import com.everhomes.rest.yellowPage.SetNotifyTargetStatusCommand;
import com.everhomes.rest.yellowPage.VerifyNotifyTargetCommand;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceNotifyTargets;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class NotifyTargetTest extends BaseLoginAuthTestCase {

	private static final String ADD_NOTIFY_TARGET_URI = "/yellowPage/addNotifyTarget";
	private static final String DELETE_NOTIFY_TARGET_URI = "/yellowPage/deleteNotifyTarget";
	private static final String SET_NOTIFY_TARGET_STATUS_URI = "/yellowPage/setNotifyTargetStatus";
	private static final String LIST_NOTIFY_TARGETS_URI = "/yellowPage/listNotifyTargets";
	private static final String VERIFY_NOTIFY_TARGET_URI = "/yellowPage/verifyNotifyTarget";
	private static final String SEARCH_REQUEST_INFO_URI = "/yellowPage/searchRequestInfo";
	
	
	String ownerType = "community";
	Long ownerId = 240111044331048623L;
	Long categoryId = 1L;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/yellowpage-test-data.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	@Test
	public void testAddNotifyTarget() {
		
		logon();

		StringRestResponse resp = addTarget();
		
		assertNotNull(resp);
		assertTrue("response= " + StringHelper.toJsonString(resp), httpClientService.isReponseSuccess(resp));

		List<EhServiceAllianceNotifyTargets> targets = getDbTargets();
		assertNotNull(targets);
		assertTrue("name".equals(targets.get(0).getName()));
		assertTrue(targets.get(0).getStatus() == 1);
	}
	
	@Test
	public void testDeleteNotifyTarget() {
		
		logon();

		addTarget();
		List<EhServiceAllianceNotifyTargets> targets = getDbTargets();
		assertNotNull(targets);
		assertEquals(1, targets.size());
		
		String uri = DELETE_NOTIFY_TARGET_URI;
		DeleteNotifyTargetCommand cmd = new DeleteNotifyTargetCommand();
		cmd.setId(targets.get(0).getId());
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		httpClientService.restPost(uri, cmd, StringRestResponse.class);
		
		List<EhServiceAllianceNotifyTargets> t = getDbTargets();
		assertNotNull(t);
		assertEquals(0, t.size());
	}
	
	@Test
	public void testSetNotifyTargetStatus() {
		
		logon();

		addTarget();
		List<EhServiceAllianceNotifyTargets> targets = getDbTargets();
		assertNotNull(targets);
		assertTrue(targets.get(0).getStatus() == 1);
		
		String uri = SET_NOTIFY_TARGET_STATUS_URI;
		SetNotifyTargetStatusCommand cmd = new SetNotifyTargetStatusCommand();
		cmd.setId(targets.get(0).getId());
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setStatus((byte) 0);
		httpClientService.restPost(uri, cmd, StringRestResponse.class);
		
		List<EhServiceAllianceNotifyTargets> t = getDbTargets();
		assertNotNull(t);
		assertTrue(t.get(0).getStatus() == 0);
	}
	
	@Test
	public void testListNotifyTargets() {
		
		logon();

		addTarget();
		List<EhServiceAllianceNotifyTargets> targets = getDbTargets();
		assertNotNull(targets);
		assertTrue(targets.get(0).getStatus() == 1);
		
		String uri = LIST_NOTIFY_TARGETS_URI;
		ListNotifyTargetsCommand cmd = new ListNotifyTargetsCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setContactType(ContactType.EMAIL.getCode());
		cmd.setCategoryId(categoryId);
		ListNotifyTargetsRestResponse resp = httpClientService.restPost(uri, cmd, ListNotifyTargetsRestResponse.class);
		
		
		assertNotNull(resp);
		assertTrue(resp.getResponse().getDtos().size() == 1);
		assertTrue("name".equals(resp.getResponse().getDtos().get(0).getName()));
	}
	
	@Test
	public void testVerifyNotifyTarget() {
		
		logon();

		addTarget();
		List<EhServiceAllianceNotifyTargets> targets = getDbTargets();
		assertNotNull(targets);
		assertTrue(targets.get(0).getStatus() == 1);
		
		String uri = VERIFY_NOTIFY_TARGET_URI;
		VerifyNotifyTargetCommand cmd = new VerifyNotifyTargetCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setCategoryId(categoryId);
		cmd.setContactToken("12345343244");
		StringRestResponse resp = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		
		
		assertNotNull(resp);
		assertTrue(YellowPageServiceErrorCode.ERROR_NOTIFY_TARGET_NOT_REGISTER == resp.getErrorCode());
	}
	
	private StringRestResponse addTarget() {
		
		AddNotifyTargetCommand cmd = new AddNotifyTargetCommand();

		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setCategoryId(categoryId);
		cmd.setContactToken("fdhfgue@mail.com");
		cmd.setContactType(ContactType.EMAIL.getCode());
		cmd.setName("name");
		
		String uri = ADD_NOTIFY_TARGET_URI;
		StringRestResponse response = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		
		return response;
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
	
	private List<EhServiceAllianceNotifyTargets> getDbTargets() {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_SERVICE_ALLIANCE_NOTIFY_TARGETS)
				.fetch().map(r -> ConvertHelper.convert(r, EhServiceAllianceNotifyTargets.class));
	}
}
