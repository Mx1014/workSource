package com.everhomes.test.junit.richtext;

import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.StringRestResponse;
import com.everhomes.rest.richtext.GetRichTextByTokenCommand;
import com.everhomes.rest.richtext.GetRichTextByTokenRestResponse;
import com.everhomes.rest.richtext.GetRichTextCommand;
import com.everhomes.rest.richtext.GetRichTextRestResponse;
import com.everhomes.rest.richtext.RichTextContentType;
import com.everhomes.rest.richtext.RichTextResourceType;
import com.everhomes.rest.richtext.UpdateRichTextCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRichTexts;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;

public class RichTextTest extends BaseLoginAuthTestCase {
	
	private static final String UPDATE_RT_URI = "/richText/updateRichText";
	private static final String GET_RT_URI = "/richText/getRichText";
	private static final String TOKEN_GET_RT_URI = "/richText/getRichTextByToken";

	String ownerType = "community";
	Long ownerId = 240111044331048623L;
	
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/richtext-test-data.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	@Test
	public void testUpdateRichText() {
		String uri = UPDATE_RT_URI;
		logon();
		
		UpdateRichTextCommand cmd = new UpdateRichTextCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setResourceType(RichTextResourceType.ABOUT.getCode());
		cmd.setContentType(RichTextContentType.RICHTEXT.getCode());
		cmd.setContent("content");
		
		StringRestResponse response = httpClientService.restPost(uri, cmd, StringRestResponse.class);
		assertNotNull(response);
		
		List<EhRichTexts> rt = getDbRT(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getResourceType());
		assertNotNull(rt);
		assertEquals(1, rt.size());
		assertTrue("content".equals(rt.get(0).getContent()));
		
	}
	
	@Test
	public void testGetRichText() {
		String uri = GET_RT_URI;
		logon();
		
		GetRichTextCommand cmd = new GetRichTextCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(ownerType);
		cmd.setResourceType(RichTextResourceType.INTRODUCTION.getCode());
		
		GetRichTextRestResponse response = httpClientService.restPost(uri, cmd, GetRichTextRestResponse.class);
		assertNotNull(response);
		
		assertTrue("fdggewg".equals(response.getResponse().getContent()));
		
	}
	
	@Test
	public void testGetRichTextByToken() {
		String uri = TOKEN_GET_RT_URI;
		
		GetRichTextByTokenCommand cmd = new GetRichTextByTokenCommand();
		cmd.setRtToken("SyaumeP94Cd-GJRrvznX6HmxecX4GCuYUOGGg7aAhCfQ1-H9rxMMHVkc5PwEi2fHav4cxjWLH6v82PtNG0v8pAHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fivG9UdjUdUdButtLdWJtvw");
		GetRichTextByTokenRestResponse response = httpClientService.restPost(uri, cmd, GetRichTextByTokenRestResponse.class);
		assertNotNull(response);
		
		assertTrue("fdggewg".equals(response.getResponse().getContent()));
		
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
	
	private List<EhRichTexts> getDbRT(Long ownerId, String ownerType, String resourceType) {
		DSLContext context = dbProvider.getDslContext();
		return context.select().from(Tables.EH_RICH_TEXTS).
				where(Tables.EH_RICH_TEXTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_RICH_TEXTS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_RICH_TEXTS.RESOURCE_TYPE.eq(resourceType))
				.fetch().map(r -> ConvertHelper.convert(r, EhRichTexts.class));
	}
}
