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
import com.everhomes.rest.rentalv2.LoopType;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.rest.rentalv2.RentalOwnerType;
import com.everhomes.rest.rentalv2.RentalSiteStatus;
import com.everhomes.rest.rentalv2.RentalType;
import com.everhomes.rest.rentalv2.admin.AddRentalSiteRulesAdminCommand;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.rentalv2.admin.DiscountType;
import com.everhomes.rest.rentalv2.admin.TimeIntervalDTO;
import com.everhomes.rest.rentalv2.admin.UpdateRentalSiteDiscountAdminCommand;
import com.everhomes.rest.rentalv2.admin.UpdateRentalSiteRulesAdminCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ConfigAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class RentalAdminRuleTest extends BaseLoginAuthTestCase {

	Integer namespaceId = 0;
	String userIdentifier = "root";
	String plainTexPassword = "123456";

	private Long launchPadItemId = 510L;
	private String ownerType = RentalOwnerType.COMMUNITY.getCode();
	private Long ownerId = 419L;
	private Long organizationId = 1L;
	private Long rentalSiteId = 2L;

	@Before
	public void setUp() {
		super.setUp();
		truncateRentalTable();
		initSrouceData();


        // 清除缓存
        clearRedisCache();
        
        // 同步索引
        syncSequence();
        
	}

	private void truncateRentalTable() {

		String serverInitfilePath = "data/tables/rental2.0_truncate_tables.sql";
		dbProvider.runClassPathSqlFile(serverInitfilePath);

	}

	@Test
	public void testAddRentalSiteRules() {

		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);

		String commandRelativeUri = "/rental/admin/addRentalSiteRules";

		AddRentalSiteRulesAdminCommand cmd = new AddRentalSiteRulesAdminCommand();
		cmd.setRentalSiteId(rentalSiteId);
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
		attachement = new AttachmentConfigDTO();
		attachement.setAttachmentType(AttachmentType.SHOW_CONTENT.getCode());
		attachement.setMustOptions(NormalFlag.NEED.getCode());
		cmd.getAttachments().add(attachement);

		cmd.setRentalType(RentalType.DAY.getCode());
		cmd.setRentalEndTime(1 * 60 * 60 * 24 * 1000L);
		cmd.setRentalStartTime(10 * 60 * 60 * 24 * 1000L);

		// cmd.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
		cmd.setBeginDate(new Date().getTime());
		// 当前时间+49天7周
		cmd.setEndDate(new Date().getTime() + 1000 * 60 * 60 * 24 * 49L);
		cmd.setOpenWeekday(new ArrayList<Integer>()); 
		cmd.getOpenWeekday().add(1);
		cmd.getOpenWeekday().add(2);
		cmd.getOpenWeekday().add(3);
		cmd.getOpenWeekday().add(4);
		cmd.getOpenWeekday().add(5);
		cmd.getOpenWeekday().add(6);
		cmd.getOpenWeekday().add(7);
		
		cmd.setCloseDates(null);
		cmd.setWorkdayPrice(new BigDecimal(100));
		cmd.setWeekendPrice(new BigDecimal(200));
		cmd.setSiteCounts(10.0);
		cmd.setSiteNumbers(new ArrayList<>());
		for(int i =1;i<=10;i++)
			cmd.getSiteNumbers().add(i+"号的资源");
		cmd.setCancelTime(0L);
		cmd.setRefundFlag(NormalFlag.NEED.getCode());
		cmd.setRefundRatio(30);

		RestResponse response = httpClientService.restGet(commandRelativeUri,
				cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalv2Cells> resultRules1 = new ArrayList<EhRentalv2Cells>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(cmd
						.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultRules1.add(ConvertHelper.convert(r,
							EhRentalv2Cells.class));
					return null;
				});
		// 7周，其中每周7天也就是49天有效的，每天10场所，按日租，为280个单元格
		assertEquals(490, resultRules1.size());

		List<EhRentalv2Resources> resultSite1 = new ArrayList<EhRentalv2Resources>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_RESOURCES)
				.where(Tables.EH_RENTALV2_RESOURCES.ID.eq(cmd.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultSite1.add(ConvertHelper.convert(r,
							EhRentalv2Resources.class));
					return null;
				});
		// 添加规则会修改资源表
		assertEquals(cmd.getMultiUnit(), resultSite1.get(0).getMultiUnit());

		List<EhRentalv2ConfigAttachments> resultConfigAttach1 = new ArrayList<EhRentalv2ConfigAttachments>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS)
				.where(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID.eq(cmd
						.getRentalSiteId()))
				.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
						.eq(EhRentalv2Resources.class.getSimpleName()))
				.fetch()
				.map((r) -> {
					resultConfigAttach1.add(ConvertHelper.convert(r,
							EhRentalv2ConfigAttachments.class));
					return null;
				});
		// 增加了两个附件设置
		assertEquals(2, resultConfigAttach1.size());

		// 按小时设置

		cmd.setRefundFlag(NormalFlag.NONEED.getCode());
		cmd.setAttachments(new ArrayList<AttachmentConfigDTO>()); 
		attachement = new AttachmentConfigDTO();
		attachement.setAttachmentType(AttachmentType.SHOW_CONTENT.getCode());
		attachement.setMustOptions(NormalFlag.NEED.getCode());
		cmd.getAttachments().add(attachement);

		cmd.setRentalType(RentalType.HOUR.getCode());
		cmd.setTimeStep(2.0);
		cmd.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
		TimeIntervalDTO timeIntervalDTO = new TimeIntervalDTO();
		timeIntervalDTO.setBeginTime(10.0);
		timeIntervalDTO.setEndTime(17.0);
		cmd.getTimeIntervals().add(timeIntervalDTO);

		timeIntervalDTO = new TimeIntervalDTO();
		timeIntervalDTO.setBeginTime(18.0);
		timeIntervalDTO.setEndTime(20.0);
		cmd.getTimeIntervals().add(timeIntervalDTO);
		response = httpClientService.restGet(commandRelativeUri, cmd,
				RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		List<EhRentalv2Cells> resultRules2 = new ArrayList<EhRentalv2Cells>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(cmd
						.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultRules2.add(ConvertHelper.convert(r,
							EhRentalv2Cells.class));
					return null;
				});
		// 49天有效的，每天10场所，10-17点18-20，2小时为一个周期 每天4个单元格 总共
		assertEquals(1960, resultRules2.size());

		List<EhRentalv2Resources> resultSite2 = new ArrayList<EhRentalv2Resources>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_RESOURCES)
				.where(Tables.EH_RENTALV2_RESOURCES.ID.eq(cmd.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultSite2.add(ConvertHelper.convert(r,
							EhRentalv2Resources.class));
					return null;
				});
		// 添加规则会修改资源表
		assertEquals(cmd.getMultiUnit(), resultSite2.get(0).getMultiUnit());
		assertEquals(cmd.getRefundFlag(), resultSite2.get(0).getRefundFlag());

		List<EhRentalv2ConfigAttachments> resultConfigAttach2 = new ArrayList<EhRentalv2ConfigAttachments>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS)
				.where(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID.eq(cmd
						.getRentalSiteId()))
				.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
						.eq(EhRentalv2Resources.class.getSimpleName()))
				.fetch()
				.map((r) -> {
					resultConfigAttach2.add(ConvertHelper.convert(r,
							EhRentalv2ConfigAttachments.class));
					return null;
				});
		// 之前的2个附件应该删掉，增加新的附件 1个
		assertEquals(1, resultConfigAttach2.size());
 

		// 3號資源

		cmd.setRentalSiteId(3L);
		cmd.setAutoAssign(NormalFlag.NONEED.getCode());
		cmd.setRefundFlag(NormalFlag.NONEED.getCode());
		cmd.setAttachments(new ArrayList<AttachmentConfigDTO>()); 
		attachement = new AttachmentConfigDTO();
		attachement.setAttachmentType(AttachmentType.SHOW_CONTENT.getCode());
		attachement.setMustOptions(NormalFlag.NEED.getCode());
		cmd.getAttachments().add(attachement);
		attachement = new AttachmentConfigDTO();
		attachement.setAttachmentType(AttachmentType.TEXT_REMARK.getCode());
		attachement.setMustOptions(NormalFlag.NEED.getCode());
		cmd.getAttachments().add(attachement);

		cmd.setRentalType(RentalType.HOUR.getCode());
		cmd.setTimeStep(2.0);
		cmd.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
		timeIntervalDTO = new TimeIntervalDTO();
		timeIntervalDTO.setBeginTime(10.0);
		timeIntervalDTO.setEndTime(17.0);
		cmd.getTimeIntervals().add(timeIntervalDTO);

		timeIntervalDTO = new TimeIntervalDTO();
		timeIntervalDTO.setBeginTime(18.0);
		timeIntervalDTO.setEndTime(20.0);
		cmd.getTimeIntervals().add(timeIntervalDTO);
		response = httpClientService.restGet(commandRelativeUri, cmd,
				RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		List<EhRentalv2Cells> resultRules3 = new ArrayList<EhRentalv2Cells>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(cmd
						.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultRules3.add(ConvertHelper.convert(r,
							EhRentalv2Cells.class));
					return null;
				});
		// 49天有效的，1场所，10-17点18-20，2小时为一个周期 每天4个单元格 总共
		assertEquals(196, resultRules3.size());

		List<EhRentalv2Resources> resultSite3 = new ArrayList<EhRentalv2Resources>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_RESOURCES)
				.where(Tables.EH_RENTALV2_RESOURCES.ID.eq(cmd.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultSite3.add(ConvertHelper.convert(r,
							EhRentalv2Resources.class));
					return null;
				});
		// 添加规则会修改资源表
		assertEquals(cmd.getMultiUnit(), resultSite3.get(0).getMultiUnit());
		assertEquals(cmd.getRefundFlag(), resultSite3.get(0).getRefundFlag());

		List<EhRentalv2ConfigAttachments> resultConfigAttach3 = new ArrayList<EhRentalv2ConfigAttachments>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS)
				.where(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID.eq(cmd
						.getRentalSiteId()))
				.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
						.eq(EhRentalv2Resources.class.getSimpleName()))
				.fetch()
				.map((r) -> {
					resultConfigAttach3.add(ConvertHelper.convert(r,
							EhRentalv2ConfigAttachments.class));
					return null;
				});
		// 之前的2个附件应该删掉，增加新的附件 1个
		assertEquals(2, resultConfigAttach3.size()); 
		
	}
	@Test
	public void testUpdateRentalSiteSimpleRules() { 
		initHourRulesData();
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/rental/admin/updateRentalSiteRules";
		
		UpdateRentalSiteRulesAdminCommand cmd = new UpdateRentalSiteRulesAdminCommand();
		cmd.setRuleId(282L);
		cmd.setBeginDate(1467079200000L);
		// 当前时间+14天
		cmd.setEndDate(1467180000000L);
		cmd.setBeginTime(7.0);
		cmd.setEndTime(16.0);
		cmd.setHalfsiteOriginalPrice(new BigDecimal(150));
		cmd.setHalfsitePrice(new BigDecimal(100));
		cmd.setOriginalPrice(new BigDecimal(250));
		cmd.setPrice(new BigDecimal(150));
		cmd.setStatus(RentalSiteStatus.NORMAL.getCode());
		cmd.setLoopType(LoopType.EVERYDAY.getCode());
		
		RestResponse response = httpClientService.restGet(commandRelativeUri,
				cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		DSLContext dslContext = dbProvider.getDslContext();
		List<EhRentalv2Cells> resultRules1 = new ArrayList<EhRentalv2Cells>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.ID.eq(282L))
				.fetch()
				.map((r) -> {
					resultRules1.add(ConvertHelper.convert(r,
							EhRentalv2Cells.class));
					return null;
				});
		// 28号的更新成功，之后的应该不更新
		assertEquals(cmd.getOriginalPrice().doubleValue(), resultRules1.get(0).getOriginalPrice().doubleValue());
//		assertEquals(cmd.getHalfsiteOriginalPrice().doubleValue(), resultRules1.get(0).getHalfsiteOriginalPrice().doubleValue());
 
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.ID.eq(916L))
				.fetch()
				.map((r) -> {
					resultRules1.add(ConvertHelper.convert(r,
							EhRentalv2Cells.class));
					return null;
				});
		// 28号的更新成功，之后的应该不更新
		assertEquals(null, resultRules1.get(1).getOriginalPrice());
//		assertEquals(null, resultRules1.get(1).getHalfsiteOriginalPrice());
		

	}
	@Test
	public void testUpdateRentalSiteDiscount() {
		// 登录时不传namepsace，默认为左邻域空间
		logon(null, userIdentifier, plainTexPassword);
		String commandRelativeUri = "/rental/admin/updateRentalSiteDiscount";
		
		UpdateRentalSiteDiscountAdminCommand cmd = new UpdateRentalSiteDiscountAdminCommand(); 
		cmd.setRentalSiteId(this.rentalSiteId);
		cmd.setDiscountType(DiscountType.FULL_MOENY_CUT_MONEY.getCode());
		cmd.setFullPrice(new BigDecimal(1000));
		//cmd.setCutprice(new BigDecimal(300));
		RestResponse response = httpClientService.restGet(commandRelativeUri,
				cmd, RestResponse.class, context);

		assertNotNull("The reponse of may not be null", response);
		assertTrue("The user scenes should be get from server, response="
				+ StringHelper.toJsonString(response),
				httpClientService.isReponseSuccess(response));
		DSLContext dslContext = dbProvider.getDslContext();

		List<EhRentalv2Resources> resultSite1 = new ArrayList<EhRentalv2Resources>();
		dslContext
				.select()
				.from(Tables.EH_RENTALV2_RESOURCES)
				.where(Tables.EH_RENTALV2_RESOURCES.ID.eq(cmd.getRentalSiteId()))
				.fetch()
				.map((r) -> {
					resultSite1.add(ConvertHelper.convert(r,
							EhRentalv2Resources.class));
					return null;
				});
		assertEquals(cmd.getDiscountType(), resultSite1.get(0).getDiscountType());
		//assertEquals(cmd.getCutprice().doubleValue(), resultSite1.get(0).getCutPrice().doubleValue());
		assertEquals(cmd.getFullPrice().doubleValue(), resultSite1.get(0).getFullPrice().doubleValue());
	}
	@After
	public void tearDown() {
		super.tearDown();
		logoff();
	}

	protected void initSrouceData() {
		String sourceInfoFilePath = "data/json/rental2.0-test-data-resource-160627.txt";
		String filePath = dbProvider
				.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}
	
	protected void initHourRulesData() {
		String sourceInfoFilePath = "data/json/rental2.0-test-data-siterules-hour-160628.txt";
		String filePath = dbProvider
				.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}
	
	protected void initItemsData() {
		String sourceInfoFilePath = "data/json/rental2.0-test-data-items-160627.txt";
		String filePath = dbProvider
				.getAbsolutePathFromClassPath(sourceInfoFilePath);
		dbProvider.loadJsonFileToDatabase(filePath, false);
	}

}
