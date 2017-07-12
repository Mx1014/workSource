package com.everhomes.test.junit.rental;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.RentalOwnerType;
import com.everhomes.rest.rentalv2.RentalType;
import com.everhomes.rest.rentalv2.admin.AdminQueryDefaultRuleRestResponse;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateDefaultRuleAdminCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalv2DefaultRules;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalDefaultRuleTest extends BaseLoginAuthTestCase {
	private Long launchPadItemId = 510L;
	private String ownerType = RentalOwnerType.ORGANIZATION.getCode();
	private Long ownerId = 1L;

	@Before
	public void setUp() {
		super.setUp(); 
	}
	@Test
	public void Test(){
		testQueryDefaultRule();
		testUpdateDefaultRule();
	}
	private void truncateRentalTable() {
 

	}

	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}

	public void testQueryDefaultRule() {

		Integer namespaceId = 0;
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		QueryDefaultRuleAdminCommand cmd = new QueryDefaultRuleAdminCommand();
		cmd.setResourceTypeId(this.launchPadItemId);
		cmd.setOwnerId(this.ownerId);
		cmd.setOwnerType(this.ownerType);
		String commandRelativeUri = "/rental/admin/queryDefaultRule";
		AdminQueryDefaultRuleRestResponse response = httpClientService.restPost(commandRelativeUri,
				cmd, AdminQueryDefaultRuleRestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		assertNotNull("The default rule not null ", response.getResponse());
		System.out.print(response.getResponse().toString());
		List<EhRentalv2DefaultRules> result = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext();
		context.select()
				.from(Tables.EH_RENTALV2_DEFAULT_RULES)
				.where(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_TYPE.eq(cmd
						.getOwnerType()))
				.and(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_ID.eq(cmd
						.getOwnerId()))
				.and(Tables.EH_RENTALV2_DEFAULT_RULES.RESOURCE_TYPE_ID.eq(cmd
						.getResourceTypeId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhRentalv2DefaultRules.class));
					return null;
				});
		assertEquals(1, result.size());
	}

	
	public void testUpdateDefaultRule() {
		Integer namespaceId = 0;
		String userIdentifier = "root";
		String plainTexPassword = "123456";
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		UpdateDefaultRuleAdminCommand cmd = new UpdateDefaultRuleAdminCommand();
		cmd.setOwnerType(this.ownerType);
		cmd.setOwnerId(this.ownerId);
		cmd.setResourceTypeId(this.launchPadItemId);
		cmd.setExclusiveFlag(NormalFlag.NONEED.getCode());
		cmd.setUnit(0.5);
		cmd.setAutoAssign(NormalFlag.NEED.getCode());
		cmd.setMultiUnit(NormalFlag.NEED.getCode());
		cmd.setNeedPay(NormalFlag.NEED.getCode());
		cmd.setMultiTimeInterval(NormalFlag.NEED.getCode());
		cmd.setAttachments(new ArrayList<AttachmentConfigDTO>());
		AttachmentConfigDTO attachement = new AttachmentConfigDTO();
		attachement.setAttachmentType(AttachmentType.ATTACHMENT.getCode());
		attachement.setMustOptions(NormalFlag.NONEED.getCode());
		cmd.getAttachments().add(attachement);
		cmd.setRentalType(RentalType.DAY.getCode());
		cmd.setRentalEndTime(1 * 60 * 60 * 24 * 100L);
		cmd.setRentalStartTime(10 * 60 * 60 * 24 * 100L);

		// cmd.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
		cmd.setBeginDate(new Date().getTime());
		// 当前时间+100天
		cmd.setEndDate(new Date().getTime() + 1000 * 60 * 60 * 24 * 100L);
		cmd.setOpenWeekday(new ArrayList<Integer>());
		cmd.getOpenWeekday().add(1);
		cmd.getOpenWeekday().add(2);
		cmd.getOpenWeekday().add(3);
		cmd.getOpenWeekday().add(4);
		cmd.setCloseDates(null);
		cmd.setWorkdayPrice(new BigDecimal(100));
		cmd.setWeekendPrice(new BigDecimal(200));
		cmd.setSiteCounts(10.0);
		cmd.setCancelTime(0L);
		cmd.setRefundFlag(NormalFlag.NEED.getCode());
		cmd.setRefundRatio(30);
		String commandRelativeUri = "/rental/admin/updateDefaultRule";
		RestResponse response = httpClientService.restGet(commandRelativeUri,
				cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));

		List<EhRentalv2DefaultRules> result = new ArrayList<>();
		DSLContext context = dbProvider.getDslContext();
		context.select()
				.from(Tables.EH_RENTALV2_DEFAULT_RULES)
				.where(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_TYPE.eq(cmd
						.getOwnerType()))
				.and(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_ID.eq(cmd
						.getOwnerId()))
				.and(Tables.EH_RENTALV2_DEFAULT_RULES.RESOURCE_TYPE_ID.eq(cmd
						.getResourceTypeId()))
				.fetch()
				.map((r) -> {
					result.add(ConvertHelper.convert(r,
							EhRentalv2DefaultRules.class));
					return null;
				});
		assertEquals(1, result.size()); 
		
	}
	
	
} 