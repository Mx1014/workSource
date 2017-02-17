package com.everhomes.test.junit.expansion;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.techpark.expansion.ApplyEntryApplyType;
import com.everhomes.rest.techpark.expansion.ApplyEntrySourceType;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryCommand;
import com.everhomes.rest.techpark.expansion.EnterpriseApplyEntryResponse;
import com.everhomes.rest.techpark.expansion.EntryApplyEntryRestResponse;
import com.everhomes.rest.techpark.expansion.EntryListApplyEntrysRestResponse;
import com.everhomes.rest.techpark.expansion.ListEnterpriseApplyEntryCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequestBuildings;
import com.everhomes.server.schema.tables.pojos.EhYellowPages;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class EnterpriseApplayEntryTest extends BaseLoginAuthTestCase {

	private static String APPLY_ENTRY_URI = "/techpark/entry/applyEntry";
	private static String LIST_APPLY_ENTRYS_URI = "/techpark/entry/listApplyEntrys";

	String ownerType = "community";
	Long ownerId = 240111044331048623L;
	Integer namespaceId = 1000000;
	Long organizationId = 10001L;

	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/entry-2.4-test-data.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
		jsonFilePath = "data/json/3.4.x-test-data-userinfo_160618.txt";
		fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
		
		
	}
	
	private void logon() {
		String userIdentifier = "12000000001";
		String plainTexPassword = "123456";
		logon(namespaceId, userIdentifier, plainTexPassword);
	}

	@Test
	public void testApplyEntry() {
		logon();
		EnterpriseApplyEntryCommand cmd = new EnterpriseApplyEntryCommand();
		cmd.setSourceType(ApplyEntrySourceType.FOR_RENT.getCode());
		cmd.setSourceId(51L);
		cmd.setApplyType(ApplyEntryApplyType.RENEW.getCode());
		cmd.setContractId(1L);
		cmd.setCommunityId(2L);
		EntryApplyEntryRestResponse response = httpClientService.restPost(APPLY_ENTRY_URI, cmd,
				EntryApplyEntryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		DSLContext context = dbProvider.getDslContext();
		List<EhEnterpriseOpRequestBuildings> opRequestBuildings = context.select()
				.from(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS)
				.where(Tables.EH_ENTERPRISE_OP_REQUEST_BUILDINGS.BUILDING_ID.eq(1L)).fetch()
				.map(r -> ConvertHelper.convert(r, EhEnterpriseOpRequestBuildings.class));
		assertEquals(1, opRequestBuildings.size());

		ListEnterpriseApplyEntryCommand cmd2 = new ListEnterpriseApplyEntryCommand();
		cmd2.setBuildingId(1L);

		EntryListApplyEntrysRestResponse response2 = httpClientService.restPost(LIST_APPLY_ENTRYS_URI, cmd2,
				EntryListApplyEntrysRestResponse.class);

		assertNotNull("The reponse of getting user info may not be null", response2);
		assertTrue("response= " + StringHelper.toJsonString(response2), httpClientService.isReponseSuccess(response2));
        assertEquals(1, response2.getResponse().getApplyEntrys().size());
	}

}
