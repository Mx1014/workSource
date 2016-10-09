package com.everhomes.test.junit.community;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.db.DbProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.community.admin.CreateCommunityCommand;
import com.everhomes.rest.locale.ListLocaleTemplateCommand;
import com.everhomes.rest.locale.ListLocaleTemplateResponse;
import com.everhomes.rest.locale.LocaleTemplateDTO;
import com.everhomes.rest.locale.UpdateLocaleTemplateCommand;
import com.everhomes.rest.locale.admin.LocaleListLocaleTemplateRestResponse;
import com.everhomes.rest.locale.admin.LocaleUpdateLocaleTemplateRestResponse;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.admin.CreateNamespaceCommand;
import com.everhomes.rest.namespace.admin.NamespaceCreateNamespaceRestResponse;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.rest.namespace.admin.NamespaceListNamespaceRestResponse;
import com.everhomes.rest.namespace.admin.NamespaceUpdateNamespaceRestResponse;
import com.everhomes.rest.namespace.admin.UpdateNamespaceCommand;
import com.everhomes.rest.version.CreateVersionCommand;
import com.everhomes.rest.version.DeleteVersionCommand;
import com.everhomes.rest.version.ListVersionInfoResponse;
import com.everhomes.rest.version.UpdateVersionCommand;
import com.everhomes.rest.version.VersionInfoDTO;
import com.everhomes.rest.version.VersionRealmDTO;
import com.everhomes.rest.version.admin.VersionCreateVersionRestResponse;
import com.everhomes.rest.version.admin.VersionListVersionInfoRestResponse;
import com.everhomes.rest.version.admin.VersionListVersionRealmRestResponse;
import com.everhomes.rest.version.admin.VersionUpdateVersionRestResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhVersionUpgradeRules;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class CommunityImportTest extends BaseLoginAuthTestCase {
	
	@Before
	public void setUp() {
		super.setUp();
		logon();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/community-import-2.0.0-test-data-community-160822.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
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

	@Test
	public void testListVersionRealm(){
		VersionListVersionRealmRestResponse restResponse = httpClientService.restPost("/admin/version/listVersionRealm", null, VersionListVersionRealmRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		List<VersionRealmDTO> list = restResponse.getResponse();
		assertNotNull(list);
		assertEquals(list.size(), 2);
		assertNotNull(list.get(0).getRealm());
	}
	
	@Test
	public void testListVersionInfo(){
		VersionListVersionInfoRestResponse restResponse = httpClientService.restPost("/admin/version/listVersionInfo", null, VersionListVersionInfoRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		ListVersionInfoResponse response = restResponse.getResponse();
		assertNotNull(response);
		assertEquals(response.getNextPageAnchor(), null);
		assertEquals(response.getVersionList().size(), 2);
		assertNotNull(response.getVersionList().get(0).getRealm());
		assertNotNull(response.getVersionList().get(0).getId());
	}
	
	@Test
	public void testCreateVersion(){
		CreateVersionCommand cmd = new CreateVersionCommand();
		cmd.setRealmId(1L);
		cmd.setMinVersion("3.0.0");
		cmd.setMaxVersion("4.0.2");
		cmd.setTargetVersion("4.0.2");
		cmd.setForceUpgrade((byte) 0);
		cmd.setDownloadUrl("http://www.baidu.com/tt");
		cmd.setUpgradeDescription("tt update");
		VersionCreateVersionRestResponse restResponse = httpClientService.restPost("/admin/version/createVersion", cmd, VersionCreateVersionRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		VersionInfoDTO versionInfoDTO = restResponse.getResponse();
		assertNotNull(versionInfoDTO);
		assertNotNull(versionInfoDTO.getRealm());
		assertNotNull(versionInfoDTO.getId());
		assertNotNull(versionInfoDTO.getUrlId());
		assertNotNull(versionInfoDTO.getTargetVersion());
		
		DSLContext context = dbProvider.getDslContext();
		Integer count = context.selectCount().from(Tables.EH_VERSION_UPGRADE_RULES).fetchOne().getValue(0, Integer.class);
		assertEquals(count.intValue(), 3);
		
		count = context.selectCount().from(Tables.EH_VERSION_REALM).fetchOne().getValue(0, Integer.class);
		assertEquals(count.intValue(), 2);
		
		count = context.selectCount().from(Tables.EH_VERSION_URLS).fetchOne().getValue(0, Integer.class);
		assertEquals(count.intValue(), 3);
	}
	
	@Test
	public void testUpdateVersion(){
		UpdateVersionCommand cmd = new UpdateVersionCommand();
		cmd.setId(1L);
		cmd.setRealmId(1L);
		cmd.setMinVersion("3.0.0");
		cmd.setMaxVersion("4.0.2");
		cmd.setTargetVersion("4.0.2");
		cmd.setForceUpgrade((byte) 1);
		cmd.setUrlId(1L);
		cmd.setDownloadUrl("http://www.baidu.com/tt");
		cmd.setUpgradeDescription("tt update");
		VersionUpdateVersionRestResponse restResponse = httpClientService.restPost("/admin/version/updateVersion", cmd, VersionUpdateVersionRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		VersionInfoDTO versionInfoDTO = restResponse.getResponse();
		assertNotNull(versionInfoDTO);
		assertNotNull(versionInfoDTO.getRealm());
		assertNotNull(versionInfoDTO.getTargetVersion());
		
		DSLContext context = dbProvider.getDslContext();
		Result<Record> result = context.select().from(Tables.EH_VERSION_UPGRADE_RULES).where("id=1").fetch();
		assertNotNull(result);
		List<EhVersionUpgradeRules> rules = result.map(r->ConvertHelper.convert(r, EhVersionUpgradeRules.class));
		assertEquals(1, rules.size());
		assertEquals("4.0.2", rules.get(0).getTargetVersion());
		assertEquals((byte)1, rules.get(0).getForceUpgrade().byteValue());
	}
	
	@Test
	public void testDeleteVersionById(){
		DeleteVersionCommand cmd = new DeleteVersionCommand();
		cmd.setId(1L);
		cmd.setUrlId(1L);
		RestResponse restResponse = httpClientService.restPost("/admin/version/deleteVersionById", cmd, RestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		DSLContext context = dbProvider.getDslContext();
		Result<Record> result = context.select().from(Tables.EH_VERSION_UPGRADE_RULES).where("id=1").fetch();
		assertTrue(result == null || result.size()==0);
		
		result = context.select().from(Tables.EH_VERSION_URLS).where("id=1").fetch();
		assertTrue(result == null || result.size()==0);
		
	}
	
	@Test
	public void testListNamespace(){
		NamespaceListNamespaceRestResponse restResponse = httpClientService.restPost("/admin/namespace/listNamespace", null, NamespaceListNamespaceRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		List<NamespaceInfoDTO> list = restResponse.getResponse();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertNotNull(list.get(0).getName());
		assertNotNull(list.get(0).getResourceType());
	}
	
	@Test
	public void testCreateNamespace(){
		CreateNamespaceCommand cmd = new CreateNamespaceCommand();
		cmd.setName("唐彤哈哈");
		cmd.setResourceType(NamespaceCommunityType.COMMUNITY_COMMERCIAL.getCode());
		NamespaceCreateNamespaceRestResponse restResponse = httpClientService.restPost("/admin/namespace/createNamespace", cmd, NamespaceCreateNamespaceRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		NamespaceInfoDTO namespaceInfoDTO = restResponse.getResponse();
		assertNotNull(namespaceInfoDTO);
		assertNotNull(namespaceInfoDTO.getId());
		assertNotNull(namespaceInfoDTO.getName());
		assertNotNull(namespaceInfoDTO.getResourceType());
		
		DSLContext context = dbProvider.getDslContext();
		Result<Record> result = context.select().from(com.everhomes.schema.Tables.EH_NAMESPACES).fetch();
		assertTrue(result != null && result.size()==3);
	}

	@Test
	public void testUpdateNamespace(){
		UpdateNamespaceCommand cmd = new UpdateNamespaceCommand();
		cmd.setId(999995);
		cmd.setName("我爱嘉园");
		cmd.setResourceType(NamespaceCommunityType.COMMUNITY_COMMERCIAL.getCode());
		NamespaceUpdateNamespaceRestResponse restResponse = httpClientService.restPost("/admin/namespace/updateNamespace", cmd, NamespaceUpdateNamespaceRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		NamespaceInfoDTO namespaceInfoDTO = restResponse.getResponse();
		assertNotNull(namespaceInfoDTO);
		assertNotNull(namespaceInfoDTO.getId());
		assertNotNull(namespaceInfoDTO.getName());
		assertNotNull(namespaceInfoDTO.getResourceType());
		
		DSLContext context = dbProvider.getDslContext();
		Result<Record> result = context.select().from(com.everhomes.schema.Tables.EH_NAMESPACES).where("id=999995").fetch();
		assertNotNull(result);
		assertEquals(1, result.size());;
		assertEquals("我爱嘉园", result.get(0).getValue("name",String.class));
		
		result = context.select().from(Tables.EH_NAMESPACE_DETAILS).where("namespace_id=999995").fetch();
		assertNotNull(result);
		assertEquals(1, result.size());;
		assertEquals(NamespaceCommunityType.COMMUNITY_COMMERCIAL.getCode(), result.get(0).getValue("resource_type",String.class));
	}
	
	@Test
	public void testListLocaleTemplate(){
		ListLocaleTemplateCommand cmd = new ListLocaleTemplateCommand();
		cmd.setKeyword("您");
		LocaleListLocaleTemplateRestResponse restResponse = httpClientService.restPost("/admin/locale/listLocaleTemplate", cmd, LocaleListLocaleTemplateRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		ListLocaleTemplateResponse response = restResponse.getResponse();
		assertNotNull(response);
		assertNull(response.getNextPageAnchor());
		assertEquals(1, response.getTemplateList().size());
		
		cmd = new ListLocaleTemplateCommand();
		cmd.setPageSize(1);
		restResponse = httpClientService.restPost("/admin/locale/listLocaleTemplate", cmd, LocaleListLocaleTemplateRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		response = restResponse.getResponse();
		assertNotNull(response);
		assertEquals(1L, response.getNextPageAnchor().longValue());
		assertEquals(1, response.getTemplateList().size());
		
		cmd = new ListLocaleTemplateCommand();
		cmd.setPageSize(1);
		cmd.setPageAnchor(1L);
		restResponse = httpClientService.restPost("/admin/locale/listLocaleTemplate", cmd, LocaleListLocaleTemplateRestResponse.class);
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		response = restResponse.getResponse();
		assertNotNull(response);
		assertEquals(null, response.getNextPageAnchor());
		assertEquals(1, response.getTemplateList().size());
	}
	
	@Test
	public void testUpdateLocaleTemplate(){
		UpdateLocaleTemplateCommand cmd = new UpdateLocaleTemplateCommand();
		cmd.setId(1L);
		cmd.setDescription("唐彤描述");
		cmd.setText("唐彤修改");
		LocaleUpdateLocaleTemplateRestResponse restResponse = httpClientService.restPost("/admin/locale/updateLocaleTemplate", cmd, LocaleUpdateLocaleTemplateRestResponse.class);
		
		assertNotNull(restResponse);
		assertTrue("response= " + StringHelper.toJsonString(restResponse), httpClientService.isReponseSuccess(restResponse));
		
		LocaleTemplateDTO response = restResponse.getResponse();
		assertNotNull(response);
		assertNotNull(response.getScope());
		
		DSLContext context = dbProvider.getDslContext();
		Result<Record> result = context.select().from(Tables.EH_LOCALE_TEMPLATES).where("id=1").fetch();
		assertNotNull(result);
		assertEquals(1, result.size());;
		assertEquals("唐彤描述", result.get(0).getValue("description",String.class));
		assertEquals("唐彤修改", result.get(0).getValue("text",String.class));
	}
}
