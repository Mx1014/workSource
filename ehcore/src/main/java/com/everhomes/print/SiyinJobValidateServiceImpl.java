// @formatter:off
package com.everhomes.print;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.aclink.uclbrt.BASE64Decoder;
import com.everhomes.appurl.AppUrlService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.point.constants.TrueOrFalseFlag;
import com.everhomes.print.job.SiyinPrintMessageJob;
import com.everhomes.print.job.SiyinPrintNotifyJob;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.print.*;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.OSType;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.xml.XMLToJSON;

import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *	司印打印记录验证功能类
 *
 *  @author:dengs 2017年6月23日
 */
@Component
public class SiyinJobValidateServiceImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinJobValidateServiceImpl.class);
	
	@Autowired
	private SiyinPrintEmailProvider siyinPrintEmailProvider;
	@Autowired
	private SiyinPrintOrderProvider siyinPrintOrderProvider;
	@Autowired
	private SiyinPrintPrinterProvider siyinPrintPrinterProvider;
	@Autowired
	private SiyinPrintRecordProvider siyinPrintRecordProvider;
	@Autowired
	private SiyinPrintSettingProvider siyinPrintSettingProvider;
	@Autowired
    private LocaleStringProvider localeStringProvider;
	@Autowired
	private BigCollectionProvider bigCollectionProvider;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	protected AppUrlService appUrlService;
	@Autowired
	private CoordinationProvider coordinationProvider;
	@Autowired
	private ScheduleProvider scheduleProvider;
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private UserProvider userProvider;
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationService organizationService;
	
	
	private Long PRINT_UNPAID_NOTIFY_TIME = 30 * 1000L;
	private Long PRINT_UNPAID_MESSAGE_TIME = 60 * 1000L;
	/**
	 * 整个回调可能频繁发生，由于是非用户登录接口，完全可以放到后台任务中去做。
	 */
	public void jobLogNotification(String jobData) {
		//转记录对象
		Map<?,?> jobMap = getJobMap(jobData);
		SiyinPrintRecord record = convertMapToRecordObject(jobMap);
		if(record==null)
			return ;
		generateOrders(record);
	}
	
	public boolean mfpLogNotification(String jobData) {
		try{
			SiyinPrintRecord record = convertJsonToRecordObject(jobData);
			if(record == null){
				return false;
			}
			generateOrders(record);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private void generateOrders(SiyinPrintRecord record) {
		//记录重复通知
		SiyinPrintRecord oldrecord = siyinPrintRecordProvider.findSiyinPrintRecordByJobId(record.getJobId());
		if(oldrecord!=null){
			return ;
		}
		//根据记录创建订单
		createOrder(record);
		
		//正在打印的任务，减少一个
		if(record.getJobType().byteValue() == PrintJobTypeType.PRINT.getCode()){
			String key = SiyinPrintServiceImpl.REDIS_PRINTING_TASK_COUNT + record.getCreatorUid() + SiyinPrintServiceImpl.PRINT_LOGON_ACCOUNT_SPLIT + record.getOwnerId();
			reducePrintingJobCount(key);
		}
	}

	private SiyinPrintRecord convertJsonToRecordObject(String jobData) {
		SiyinPrintRecord record = new SiyinPrintRecord();
		JSONObject object = JSONObject.parseObject(jobData);
		//user_name发送给司印方的时候，包括了用户id和小区id
		String userIdcommuntiyID = object.getString("user_name").toString();
		String[] ids = userIdcommuntiyID.split(SiyinPrintServiceImpl.PRINT_LOGON_ACCOUNT_SPLIT);
		if(ids.length!=2){//user_name不符合格式
			LOGGER.info("Unknown user_name = {}" , userIdcommuntiyID);
			return null;
		}
		// 校验用户是否正确
		User user = userProvider.findUserById(Long.valueOf(ids[0]));
		if(user == null || user.getId().longValue() != Long.valueOf(ids[0]).longValue()){
			LOGGER.info("Unknown userId = {}" , Long.valueOf(ids[0]));
			return null;
		}
		record.setCreatorUid(Long.valueOf(ids[0]));
		record.setOperatorUid(Long.valueOf(ids[0]));
		record.setOwnerId(Long.valueOf(ids[1]));
		record.setOwnerType(PrintOwnerType.COMMUNITY.getCode());
		Community community = communityProvider.findCommunityById(record.getOwnerId());
		if(community != null)
			record.setNamespaceId(community.getNamespaceId());
		record.setUserDisplayName(userIdcommuntiyID);
		record.setColorSurfaceCount(object.getInteger("color_surface"));
		record.setDuplex(object.getByte("duplex"));
		record.setJobId(object.getString("job_id"));
		record.setDocumentName(object.getString("job_name"));
		record.setJobType(getPrintTypeCode(object.getString("job_type")));
		record.setMonoSurfaceCount(object.getInteger("mono_surface"));
		record.setPaperSize(getPaperSizeCode(object.getString("paper_size")));
		record.setStartTime(object.getString("print_time"));
		record.setSerialNumber(object.getString("serial_number"));
		record.setPrinterName(getPrinterNameBySerialNumber(record.getSerialNumber()));
		record.setJobStatus("FinishJob");
		record.setGroupName("___OAUTH___");
		record.setStatus(CommonStatus.ACTIVE.getCode());
		return record;
	}

	public void createOrder(SiyinPrintRecord record){
		//支付和合并订单，必须上锁。
		coordinationProvider.getNamedLock(CoordinationLocks.PRINT_ORDER_LOCK_FLAG.getCode()).enter(()->{

			List<SiyinPrintSetting> settings = siyinPrintSettingProvider.listSiyinPrintSettingByOwner(PrintOwnerType.COMMUNITY.getCode(), record.getOwnerId());
			//获取价格map
			Map<String, BigDecimal> priceMap = getPriceMap(settings);
			
			// 存在记录，不做重复计算
			// 获取未支付/未锁定的订单，如没有获取到则创建一个新订单
			SiyinPrintOrder order = getPrintOrder(record, priceMap);
           
           //将记录合并到订单上,并更新到数据库
           mergeRecordToOrder(record,order,priceMap);
           dbProvider.execute(r->{
        	   
        	   //订单金额为0，那么设置成支付状态。
				if (order.getOrderTotalFee() == null || order.getOrderTotalFee().compareTo(new BigDecimal(0)) == 0) {
					order.setOrderStatus(PrintOrderStatusType.PAID.getCode());
				} else {
					order.setOrderStatus(PrintOrderStatusType.UNPAID.getCode());
				}
				
				//如果是未支付状态，而且之前未通知过，则发送消息通知
				boolean isNeedAlert = false;
				if (PrintOrderStatusType.UNPAID.getCode().equals(order.getOrderStatus())
						&& TrueOrFalseFlag.FALSE.getCode().equals(order.getUserNotifyFlag())) {
					order.setUserNotifyFlag(TrueOrFalseFlag.TRUE.getCode());
					isNeedAlert = true;
				}
				
	   			if(order.getId() == null){
	   				siyinPrintOrderProvider.createSiyinPrintOrder(order);
	   			}else{
	   				siyinPrintOrderProvider.updateSiyinPrintOrder(order);
	   			}
	   			
	   			if (isNeedAlert) {
	   		        //创建订单成功后建立两个定时任务
	   		        createOrderOverTimeTask(order);
	   			}
	   			
	   			record.setOrderId(order.getId());
	   			siyinPrintRecordProvider.createSiyinPrintRecord(record);
	   			return null;
   			});
          return null;
		});
	}
	
	private void createOrderOverTimeTask(SiyinPrintOrder order) {
		Long unpaidNotifyTime  = configurationProvider.getLongValue("print.unpaid.notify.time", 120 * 60 * 1000L);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = dateFormat.format(new Date());
        String endStr = nowStr.substring(0,nowStr.indexOf(" ")) + " 23:59:59";
        Long overTime = null;
		try {
			overTime = (dateFormat.parse(endStr).getTime() - dateFormat.parse(nowStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long messageTime = 15 * 60 * 60 * 1000L;
		Long unpaidMessageTime  = configurationProvider.getLongValue("print.unpaid.message.time", overTime + messageTime);
		String notifyTextForOther = localeTemplateService.getLocaleTemplateString(SiyinPrintNotificationTemplateCode.SCOPE,
				SiyinPrintNotificationTemplateCode.PRINT_UNPAID_NOTIFY, SiyinPrintNotificationTemplateCode.locale, "", "您有一笔云打印的订单未支付，请及时支付。");
		Map<String, Object> notifyMap = new HashMap<>();
		notifyMap.put("content",notifyTextForOther);
		notifyMap.put("orderNo", order.getOrderNo());
		scheduleProvider.scheduleSimpleJob(
				"siyinprintnotify" +order.getId(),
				"sendNotify" + order.getId(),
				new java.util.Date(order.getCreateTime().getTime() + unpaidNotifyTime),
				SiyinPrintNotifyJob.class,
				notifyMap
		);
		Map<String, Object> messageMap = new HashMap<>();
		messageMap.put("orderNo", order.getOrderNo());
		scheduleProvider.scheduleSimpleJob(
				"siyinprintmessage" +order.getId(),
				"sendMessage" + order.getId(),
				new java.util.Date(order.getCreateTime().getTime() + unpaidMessageTime),
				SiyinPrintMessageJob.class,
				messageMap
		);
	}
	
	private String getAppName(Integer namespaceId) {
		AppUrlDTO appUrlDTO = appUrlService.getAppInfo(new GetAppInfoCommand(namespaceId,OSType.Android.getCode()));
		if (appUrlDTO != null) {
			return appUrlDTO.getName();
		}
		return "";
	}
	/*
	 *减少用户下正在打印的任务的数量 
	 */
	private void reducePrintingJobCount(String key) {
		 ValueOperations<String, String> valueOperations = getValueOperations(key);
         String value = valueOperations.get(key);
         if(value == null){
         	value = "0";
         }
         int taskCount = Integer.valueOf(value)-1;
         taskCount = taskCount<0?0:taskCount;
         valueOperations.set(key, String.valueOf(taskCount),1,TimeUnit.MINUTES);
	}
	
	/**
	 * 获取key在redis操作的valueOperations
	 */
	public ValueOperations<String, String> getValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations;
	}
	
	private Map<?,?> getJobMap(String jobData){
		String copyJobData = jobData;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			copyJobData = new String(decoder.decodeBuffer(copyJobData));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("copyJobData:"+copyJobData);
			return null;
		}
		
        Map<String, Object> object = XMLToJSON.convertOriginalMap(copyJobData);
        Map<?, ?> data = (Map)object.get("data");
        Map<?, ?> job = (Map<?, ?>)data.get("job");
        return job;
	}
	
	public SiyinPrintRecord convertMapToRecordObject(Map<?,?> job) {
		if(job == null)
			return null;
		Object finalResult = job.get("final_result");
		//final_result字段1是成功，0是失败，-1是进行中
		if(finalResult == null || !finalResult.toString().equals("1")){
			return null;
		}
		SiyinPrintRecord record = new SiyinPrintRecord();
		record.setJobId(job.get("job_id").toString());
		record.setJobStatus(job.get("job_status").toString());
		record.setGroupName(job.get("group_name").toString());
		
		//只有 "___OAUTH___" 才做处理
		if(!record.getJobStatus().equals("FinishJob") || !record.getGroupName().equals("___OAUTH___")){
			LOGGER.info("Invaild record = {}" , record);
			return null;
		}
		
		//user_name发送给司印方的时候，包括了用户id和小区id
		String userIdcommuntiyID = job.get("user_name").toString();
		String[] ids = userIdcommuntiyID.split(SiyinPrintServiceImpl.PRINT_LOGON_ACCOUNT_SPLIT);
		if(ids.length!=2){//user_name不符合格式
			LOGGER.info("Unknown user_name = {}" , userIdcommuntiyID);
			return null;
		}
		
		// TODO 校验用户是否正确
		User user = userProvider.findUserById(Long.valueOf(ids[0]));
		if(user == null || user.getId().longValue() != Long.valueOf(ids[0]).longValue()){
			LOGGER.info("Unknown userId = {}" , Long.valueOf(ids[0]));
			return null;
		}
		
		record.setCreatorUid(Long.valueOf(ids[0]));
		record.setOperatorUid(Long.valueOf(ids[0]));
		record.setOwnerType(PrintOwnerType.COMMUNITY.getCode());
		record.setOwnerId(Long.valueOf(ids[1]));
		Community community = communityProvider.findCommunityById(record.getOwnerId());
		if(community != null)
			record.setNamespaceId(community.getNamespaceId());
		record.setUserDisplayName(job.get("user_display_name").toString());
		record.setClientIp(job.get("client_ip").toString());
		record.setClientName(job.get("client_name").toString());
		record.setClientMac(job.get("client_mac").toString());
		record.setDriverName(job.get("driver_name").toString());
		record.setJobType(getPrintTypeCode(job.get("job_type").toString()));
		record.setStartTime(job.get("job_in_time").toString());
		record.setEndTime(job.get("job_out_time").toString());
		record.setDocumentName(job.get("document_name").toString());
		record.setPrinterName(job.get("printer_name").toString());
		record.setPaperSize(getPaperSizeCode(job.get("paper_size").toString()));
		record.setDuplex(Byte.valueOf(job.get("duplex").toString()));
		record.setCopyCount(Integer.valueOf(job.get("copy_count").toString()));
		record.setSurfaceCount(Integer.valueOf(job.get("surface_count").toString()));
		record.setColorSurfaceCount(Integer.valueOf(job.get("color_surface_count").toString()));
		record.setMonoSurfaceCount(Integer.valueOf(job.get("mono_surface_count").toString()));
		record.setPageCount(Integer.valueOf(job.get("page_count").toString()));
		record.setColorPageCount(Integer.valueOf(job.get("color_page_count").toString()));
		record.setMonoPageCount(Integer.valueOf(job.get("mono_page_count").toString()));
		record.setStatus(CommonStatus.ACTIVE.getCode());
		return record;
	}
	
	private Byte getPaperSizeCode(String string) {
		for (PrintPaperSizeType t : PrintPaperSizeType.values()) {
			if (0 == t.getDesc().compareTo(string.toUpperCase())) {
				return t.getCode();
			}
		}
		return PrintPaperSizeType.OTHER_PAPER_SIZE.getCode();

	}


	private Byte getPrintTypeCode(String string) {
		for (PrintJobTypeType t : PrintJobTypeType.values()) {
			if (0 == t.getDesc().compareTo(string.toUpperCase())) {
				return t.getCode();
			}
		}
		return null;
	}
	private SiyinPrintOrder getPrintOrder(SiyinPrintRecord record, Map<String, BigDecimal> priceMap) {
		SiyinPrintOrder unLockOrder = siyinPrintOrderProvider.findUnlockedOrderByUserId(record.getCreatorUid(),record.getJobType(),record.getOwnerType(),record.getOwnerId(), record.getPrinterName());
		
		//检查当前记录是否可以和之前的订单合并
		SiyinPrintOrder order = checkOnCreatingOrder(unLockOrder, record, priceMap);
		if (null != unLockOrder && null == order) {
			//如果不能合并，将旧的订单进行锁定
			unLockOrder.setLockFlag(PrintOrderLockType.LOCKED.getCode());
			siyinPrintOrderProvider.updateSiyinPrintOrder(unLockOrder);
		}
		
        if(order == null){
        	order = new SiyinPrintOrder();
        	order.setNamespaceId(record.getNamespaceId());
        	order.setOwnerType(record.getOwnerType());
        	order.setOwnerId(record.getOwnerId());
        	List<UserIdentifier> userIdentifier = userProvider.listUserIdentifiersOfUser(record.getCreatorUid());
        	order.setCreatorPhone(userIdentifier==null||userIdentifier.size()==0?"":userIdentifier.get(0).getIdentifierToken());
        	order.setDetail("");
        	SiyinPrintEmail email = siyinPrintEmailProvider.findSiyinPrintEmailByUserId(record.getCreatorUid());
        	order.setEmail(email==null?"":email.getEmail());
        	order.setJobType(record.getJobType());
        	order.setPrintDocumentName(record.getDocumentName());
        	order.setLockFlag(PrintOrderLockType.UNLOCKED.getCode());
        	order.setOrderNo(createOrderNo(System.currentTimeMillis()));
        	order.setOrderStatus(PrintOrderStatusType.UNPAID.getCode());
        	order.setOrderTotalFee(new BigDecimal("0"));
        	order.setCreatorUid(record.getCreatorUid());
        	order.setOperatorUid(record.getOperatorUid());
        	order.setPrinterName(record.getPrinterName());
        	User user = userProvider.findUserById(record.getCreatorUid());
    		order.setNickName(user == null?"":user.getNickName());
    		order.setUserNotifyFlag(TrueOrFalseFlag.FALSE.getCode());
    		
    		ListUserRelatedOrganizationsCommand relatedCmd = new ListUserRelatedOrganizationsCommand();
    		List<OrganizationSimpleDTO> list = organizationService.listUserRelateOrgs(relatedCmd, user);
    		String company = "";
    		if(list!=null)
	    		for (OrganizationSimpleDTO org : list) {
	    			company+=org.getName()+SiyinPrintServiceImpl.PRINT_COMPANY_SPLIT;
				}
    		order.setCreatorCompany(company);
        }
		return order;
	}

	private String getPrinterNameBySerialNumber(String serialNumber) {
		
		SiyinPrintPrinter printer = siyinPrintPrinterProvider.findSiyinPrintPrinterByReadName(serialNumber);
		if (null == printer) {
			return null;
		}
		
		return printer.getPrinterName();
	}

	/**
	 * 创建订单编号
	 */
	private Long createOrderNo(Long time) {
		String suffix = String.valueOf(generateRandomNumber(3));
		return Long.valueOf(String.valueOf(time) + suffix);
	}

	/**
	 *
	 * @param n 创建n位随机数
	 * @return
	 */
	private long generateRandomNumber(int n){
		return (long)((Math.random() * 9 + 1) * Math.pow(10, n-1));
	}
	
	private void mergeRecordToOrder(SiyinPrintRecord record, SiyinPrintOrder order, Map<String, BigDecimal> priceMap) {
		
		//订单为新创建的情况
		List<SiyinPrintRecord> list = null;
		if(order.getId() == null){
			list = new ArrayList<SiyinPrintRecord>();
		}else{
			list = siyinPrintRecordProvider.listSiyinPrintRecordByOrderId(record.getCreatorUid(),order.getId(),PrintOwnerType.COMMUNITY.getCode(), record.getOwnerId());
		}
		list.add(record);
		order.setOrderTotalFee(calculateOrderTotalAmount(list,priceMap));
		//详情
		order.setDetail(processDetail(list, PrintJobTypeType.fromCode(record.getJobType())));
		//用户名称冗余存储。
		if(order.getNickName()==null){
			User user = userProvider.findUserById(record.getCreatorUid());
    		order.setNickName(user == null?"":user.getNickName());
    		
		}
		//创建者所在公司，冗余存储。
		if(order.getCreatorCompany() == null || order.getCreatorCompany().trim().length()==0){
			ListUserRelatedOrganizationsCommand relatedCmd = new ListUserRelatedOrganizationsCommand();
			User user = new User();
			user.setId(record.getCreatorUid());
			List<OrganizationSimpleDTO> listdtos = organizationService.listUserRelateOrgs(relatedCmd, user);
			String company = "";
			if(listdtos!=null)
				for (OrganizationSimpleDTO org : listdtos) {
					company+=org.getName()+SiyinPrintServiceImpl.PRINT_COMPANY_SPLIT;
				}
			order.setCreatorCompany(company);
		}
	}
	
	/**
	 * 生成订单描述
	 */
	private String processDetail(List<SiyinPrintRecord> list, PrintJobTypeType jobType) {
		Map<PrintPaperSizeType,Integer> colorSurfaceCounts = new HashMap<PrintPaperSizeType,Integer>(); //依次是 a3,a4,a5,a6,other
		Map<PrintPaperSizeType,Integer> blackWhiteSurfaceCounts = new HashMap<PrintPaperSizeType,Integer>(); //依次是 a3,a4,a5,a6,other
		Integer colorSurfaceCount = 0; //
		Integer blackWhiteSurfaceCount = 0; //
		for (SiyinPrintRecord record : list) {
			if(jobType == PrintJobTypeType.SCAN){
				colorSurfaceCount+=record.getColorSurfaceCount();
				blackWhiteSurfaceCount+=record.getMonoSurfaceCount();
			}else{
				PrintPaperSizeType paperSizeType = PrintPaperSizeType.fromCode(record.getPaperSize());
				Integer colorcount = colorSurfaceCounts.get(paperSizeType);
				colorcount = colorcount==null?0:colorcount;
				colorcount += record.getColorSurfaceCount();
				colorSurfaceCounts.put(paperSizeType, colorcount);
				
				Integer bwcount = blackWhiteSurfaceCounts.get(paperSizeType);
				bwcount = bwcount==null?0:bwcount;
				bwcount += record.getMonoSurfaceCount();
				blackWhiteSurfaceCounts.put(paperSizeType, bwcount);
				
			}
		}
		String detail = "";
		String surface = getLocalActivityString(PrintErrorCode.PRINT_SURFACE,"面");
		if(jobType == PrintJobTypeType.SCAN){
			if(blackWhiteSurfaceCount!=0)
				detail += blackWhiteSurfaceCount+surface+"*"+ PrintColorType.BLACK_WHITE.getDesc()+"\n";
			if(colorSurfaceCount !=0)
				detail += colorSurfaceCount+surface+"*"+ PrintColorType.COLOR.getDesc()+"\n";
		}else{
			for (int i = 0; i < PrintPaperSizeType.values().length; i++) {
				PrintPaperSizeType paperSizeType = PrintPaperSizeType.values()[i];
				Integer bwprice = blackWhiteSurfaceCounts.get(paperSizeType);
				Integer colorprice = colorSurfaceCounts.get(paperSizeType);
				if(bwprice != null && bwprice!=0)
					detail += bwprice+surface+"*"+paperSizeType.getDesc()+"*"+PrintColorType.BLACK_WHITE.getDesc()+"\n";
				if(colorprice != null && colorprice!=0)
					detail += colorprice+surface+"*"+paperSizeType.getDesc()+"*"+PrintColorType.COLOR.getDesc()+"\n";
				
			}
		}
		return detail;
	}

	/**
	 * 获取在map中获取key对应的value，如果为空那么返回 defaultdecimal
	 */
	@SuppressWarnings("unused")
	private BigDecimal getPrice(final Map<String, BigDecimal> priceMap, final String key, final BigDecimal defaultdecimal, final int surfaceCount){
		BigDecimal price = priceMap.get(key);
		if(price == null){
			price = defaultdecimal;
		}
		return price.multiply(new BigDecimal(surfaceCount));
	}


	/**
	 *计算订单价格 
	 */
	private BigDecimal calculateOrderTotalAmount(List<SiyinPrintRecord> list, Map<String, BigDecimal> priceMap) {
		BigDecimal totolamount = new BigDecimal(0);
		for (SiyinPrintRecord record : list) {
			totolamount = totolamount.add(getPriceByRecord(record, priceMap));
		}
		return totolamount;
	}
	
	/**
	 * 获取价格map，key为 （jobType-papersize-colortype）
	 */
	private Map<String, BigDecimal> getPriceMap(List<SiyinPrintSetting> settings) {
		Map<String, BigDecimal> priceMap = getDefaultPriceMap();
		if(settings != null && settings.size()>0){
			for (SiyinPrintSetting setting : settings) {
				PrintSettingType settingType = PrintSettingType.fromCode(setting.getSettingType());
				PrintJobTypeType jobType = PrintJobTypeType.fromCode(setting.getJobType());
				if(settingType == PrintSettingType.PRINT_COPY_SCAN){
					//产品要求复印扫描作为统一价格，目前后台存的打印价格，也是复印价格。
					if(jobType == PrintJobTypeType.PRINT || jobType == PrintJobTypeType.COPY){
						priceMap.put(setting.getJobType()+"-"+setting.getPaperSize()+"-"+PrintColorType.BLACK_WHITE.getCode(), setting.getBlackWhitePrice());
						priceMap.put(setting.getJobType()+"-"+setting.getPaperSize()+"-"+PrintColorType.COLOR.getCode(), setting.getColorPrice());
					}else{
						priceMap.put(setting.getJobType()+"-"+PrintColorType.BLACK_WHITE.getCode(), setting.getBlackWhitePrice());
						priceMap.put(setting.getJobType()+"-"+PrintColorType.COLOR.getCode(), setting.getColorPrice());
					}
				}
			}
		}
		return priceMap;
	}


	/**
	 * 如果没有设置价格，则获取默认价格map
	 */
	private Map<String, BigDecimal> getDefaultPriceMap() {
		Map<String, BigDecimal> priceMap = new HashMap<String, BigDecimal>();
		BigDecimal defaultdecimal = new BigDecimal(configurationProvider.getValue(PrintErrorCode.PRINT_DEFAULT_PRICE,"0.1"));
		for (int i = 0; i < PrintJobTypeType.values().length; i++) {
			PrintJobTypeType jobType = PrintJobTypeType.values()[i];
			if(jobType != PrintJobTypeType.SCAN){//扫描不计算paperSize
				for (int j = 0; j < PrintPaperSizeType.values().length; j++) {
					PrintPaperSizeType paperSizeType =  PrintPaperSizeType.values()[j];
					priceMap.put(jobType.getCode()+"-"+paperSizeType.getCode()+"-"+PrintColorType.BLACK_WHITE.getCode(), defaultdecimal);
					priceMap.put(jobType.getCode()+"-"+paperSizeType.getCode()+"-"+PrintColorType.COLOR.getCode(), defaultdecimal);
				}
			}
			else{
				priceMap.put(jobType.getCode()+"-"+PrintColorType.BLACK_WHITE.getCode(), defaultdecimal);
				priceMap.put(jobType.getCode()+"-"+PrintColorType.COLOR.getCode(), defaultdecimal);
			
			}
		}
		return priceMap;
	}
	
	 private String getLocalActivityString(String code,String defaultText){
		LocaleString localeString = localeStringProvider.find(PrintErrorCode.SCOPE, code, "zh_CN");
		if (localeString != null) {
			return localeString.getText();
		}
		return defaultText;
	 }
	 
	/**
	 * 因为一次打印过程，司印可能通知左邻多次，故这里需要判断此次记录是否属于上一次未锁定的订单
	 * @param unlockOrder
	 * @param record
	 * @param priceMap
	 * @return
	 */
	private SiyinPrintOrder checkOnCreatingOrder(SiyinPrintOrder unlockOrder, SiyinPrintRecord record, Map<String, BigDecimal> priceMap) {
		
		if (null == unlockOrder) {
			return null;
		}
		
		//1.如果打印/扫描/复印 时间距离订单上一次更新时间超过20分钟，则认为是不同的订单。
		if (isOrderOutdated(unlockOrder)) {
			return null;
		}
		
		// 2.如果旧的订单价格为0，新的不为0，认为是不同订单。
		BigDecimal currentPrice = getPriceByRecord(record, priceMap);
		if (unlockOrder.getOrderTotalFee().compareTo(new BigDecimal("0")) ==  0 && currentPrice.compareTo(new BigDecimal("0")) > 0) {
			return null;
		}
		
		return unlockOrder;
	}
	
	private boolean isOrderOutdated(SiyinPrintOrder order) {
		Timestamp orderTime = order.getCreateTime();
		if (null != order.getOperateTime()) {
			orderTime = order.getOperateTime();
		}

		int diffMinutes = configurationProvider.getIntValue(PrintErrorCode.PRINT_SIYIN_ORDER_DIFF_TIME, 20);

		return ((int) (System.currentTimeMillis() - orderTime.getTime())) / (1000 * 60) > diffMinutes;
	}
	
	private BigDecimal getPriceByRecord(SiyinPrintRecord record, Map<String, BigDecimal> priceMap) {

		BigDecimal defaultdecimal = new BigDecimal(
				configurationProvider.getValue(PrintErrorCode.PRINT_DEFAULT_PRICE, "0.0"));
		
		BigDecimal totolamount = new BigDecimal(0);
		String key = "";
		PrintJobTypeType jobType = PrintJobTypeType.fromCode(record.getJobType());
		if (jobType == PrintJobTypeType.SCAN) {// 如果是扫描
			if (record.getColorSurfaceCount() != 0) {// 彩色扫描面数不为空,计算 值
				key = record.getJobType() + "-" + PrintColorType.COLOR.getCode();
				totolamount = totolamount.add(getPrice(priceMap, key, defaultdecimal, record.getColorSurfaceCount()));
			}

			if (record.getMonoSurfaceCount() != 0) {// 黑白计算
				key = record.getJobType() + "-" + PrintColorType.BLACK_WHITE.getCode();
				totolamount = totolamount.add(getPrice(priceMap, key, defaultdecimal, record.getMonoSurfaceCount()));
			}
		} else {// 打印和复印
			if (record.getColorSurfaceCount() != 0) {// 彩色扫描面数不为空,计算 值
				key = record.getJobType() + "-" + record.getPaperSize() + "-" + PrintColorType.COLOR.getCode();
				totolamount = totolamount.add(getPrice(priceMap, key, defaultdecimal, record.getColorSurfaceCount()));
			}

			if (record.getMonoSurfaceCount() != 0) {// 黑白计算
				key = record.getJobType() + "-" + record.getPaperSize() + "-" + PrintColorType.BLACK_WHITE.getCode();
				totolamount = totolamount.add(getPrice(priceMap, key, defaultdecimal, record.getMonoSurfaceCount()));
			}
		}

		return totolamount;
	}
}
