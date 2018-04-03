package com.everhomes.test.junit.org;

import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.organization.ApplyForEnterpriseContactByEmailCommand;
import com.everhomes.rest.organization.ListOrganizationsByEmailCommand;
import com.everhomes.rest.organization.ListOrganizationsByEmailRestResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.UserListUserRelatedScenesRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailVarifyTest extends BaseLoginAuthTestCase {
	private static final String CREATE_ENTERPRISE_URI = "/admin/org/createEnterprise";
	private static final String UPDATE_ENTERPRISE_URI = "/admin/org/updateEnterprise";
	private static final String LIST_ENTERPRISES_URI = "/admin/org/listEnterpriseByCommunityId";
	private static final String LIST_ORGS_BY_EMAIL_URI = "/org/listOrganizationsByEmail";
	private static final String APPLY_CONTACT_BY_MAIL_URI = "/org/applyForEnterpriseContactByEmail";
	private static final String VERIFY_ENTERPRISE_CONTACT_URI = "/org/verifyEnterpriseContact";  
	Integer namespaceId = 0;
	String userIdentifier = "root"; 
	String plainTexPassword = "123456";
	String ownerType = PunchOwnerType.ORGANIZATION.getCode();
	Long ownerId = 100600L;
	@Autowired
	private SearchProvider searchProvider;

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testCreateNews() {
		Long id = createEnterprise(CREATE_ENTERPRISE_URI);
		updateEnterprise(id);
		listOrgsByEmailWrongEmail();
		Long orgId=listOrgsByEmailSuccess();
//		assertEquals(orgId.longValue(), id.longValue());
//		applyContact(id);
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/3.4.x-test-data-news-organization-160627.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
		jsonFilePath = "/data/json/1.0.0-email-auth-test-data-161028.txt";
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

	private Long createEnterprise( String uri) {
		logon(null, userIdentifier, plainTexPassword);
		
		CreateEnterpriseCommand cmd = new CreateEnterpriseCommand();
		cmd.setName("公司名");
		cmd.setEmailDomain("@zulin.com");
		cmd.setAddress("321");
		RestResponse response = httpClientService.restGet(uri, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		
		DSLContext dslContext = dbProvider.getDslContext(); 
		
		List<EhOrganizations> orgs = new ArrayList<EhOrganizations>();
		dslContext
				.select()
				.from(Tables.EH_ORGANIZATIONS).orderBy(Tables.EH_ORGANIZATIONS.CREATE_TIME.desc())
//				.where(Tables.EH_ORGANIZATIONS.ID.eq(cmd.getRentalSiteId()))
				.fetch() 
				.map((r) -> {
					orgs.add(ConvertHelper.convert(r,
							EhOrganizations.class));
					return null;
				});
		assertEquals(cmd.getName(), orgs.get(0).getName());
		assertEquals(cmd.getEmailDomain(), orgs.get(0).getStringTag1()); 
		return orgs.get(0).getId();
	}
	
	private void updateEnterprise(Long id){

		logon(null, userIdentifier, plainTexPassword);
		
		UpdateEnterpriseCommand cmd = new UpdateEnterpriseCommand();
		cmd.setId(id);
		cmd.setName("公司名2");
		cmd.setEmailDomain("@zuolin.com");
		cmd.setAddress("321");
		RestResponse response = httpClientService.restGet(UPDATE_ENTERPRISE_URI, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		DSLContext dslContext = dbProvider.getDslContext(); 
		
		List<EhOrganizations> orgs = new ArrayList<EhOrganizations>();
		dslContext
				.select()
				.from(Tables.EH_ORGANIZATIONS).orderBy(Tables.EH_ORGANIZATIONS.CREATE_TIME.desc())
//				.where(Tables.EH_ORGANIZATIONS.ID.eq(cmd.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					orgs.add(ConvertHelper.convert(r,
							EhOrganizations.class));
					return null;
				});
		assertEquals(cmd.getName(), orgs.get(0).getName());
		assertEquals(cmd.getEmailDomain(), orgs.get(0).getStringTag1()); 
	}

	private String getSceneToken() {
		String uri = "/ui/user/listUserRelatedScenes";
		UserListUserRelatedScenesRestResponse response = httpClientService.restPost(uri, null,
				UserListUserRelatedScenesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		List<SceneDTO> list = response.getResponse();
		assertTrue("list size should be greater than 0", list != null && list.size() > 0);

		return list.get(0).getSceneToken();
	}


	private void listOrgsByEmailWrongEmail(){

		logon(null, userIdentifier, plainTexPassword);
		
		ListOrganizationsByEmailCommand cmd = new ListOrganizationsByEmailCommand();
		cmd.setEmail("abc@acc.com");
//		cmd.setSceneToken(getSceneToken());
		
		ListOrganizationsByEmailRestResponse response = httpClientService.restGet(LIST_ORGS_BY_EMAIL_URI, cmd, ListOrganizationsByEmailRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
//		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
//		List<OrganizationDTO> orgs  = response.getResponse();
		assertEquals(OrganizationServiceErrorCode.ERROR_EMAIL_NOT_EXISTS, response.getErrorCode().intValue());
		
	}

	private Long listOrgsByEmailSuccess(){

		logon(null, userIdentifier, plainTexPassword);
		
		ListOrganizationsByEmailCommand cmd = new ListOrganizationsByEmailCommand();
		cmd.setEmail("han.wu@zuolin.com");
		//	cmd.setSceneToken(getSceneToken());
		
		ListOrganizationsByEmailRestResponse response = httpClientService.restGet(LIST_ORGS_BY_EMAIL_URI, cmd, ListOrganizationsByEmailRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
		List<OrganizationDTO> orgs  = response.getResponse();
		
//		assertEquals(1, orgs.get(0));
		
		return null;
	}
	
	private void applyContact(Long id ){

		logon(null, userIdentifier, plainTexPassword);
		
		ApplyForEnterpriseContactByEmailCommand cmd = new ApplyForEnterpriseContactByEmailCommand();
		cmd.setEmail("han.wu@zuolin.com");
		cmd.setOrganizationId(id);
		//cmd.setSceneToken(getSceneToken());
		
		RestResponse response = httpClientService.restGet(UPDATE_ENTERPRISE_URI, cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));
	}
	
}
