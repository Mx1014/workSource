
package com.everhomes.asset.schedule;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.asset.AssetAppNoticePak;
import com.everhomes.asset.AssetPaymentStrings;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.NoticeInfo;
import com.everhomes.asset.PaymentBillItems;
import com.everhomes.asset.PaymentBills;
import com.everhomes.asset.PaymentFormula;
import com.everhomes.asset.PaymentLateFine;
import com.everhomes.asset.PaymentNoticeConfig;
import com.everhomes.asset.SettledBillRes;
import com.everhomes.asset.standard.AssetStandardProvider;
import com.everhomes.asset.statistic.AssetStatisticProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.NoticeDayType;
import com.everhomes.rest.asset.NoticeMemberIdAndContact;
import com.everhomes.rest.asset.NoticeObj;
import com.everhomes.rest.asset.statistic.BillsDateStrDTO;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserNotificationTemplateCode;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.pojos.EhPaymentLateFine;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.CalculatorUtil;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.CopyUtils;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * @author created by ycx
 * @date 下午2:17:56
 */
@Component
public class AssetSchedule{
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetSchedule.class);
	
	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired
    private ScheduleProvider scheduleProvider;
	
	@Autowired
    private CoordinationProvider coordinationProvider;
	
	@Autowired
    private SequenceProvider sequenceProvider;
	
	@Autowired
    private OrganizationProvider organizationProvider;
	
	@Autowired
    private UserProvider userProvider;
	
	@Autowired
    private LocaleTemplateService localeTemplateService;
	
	@Autowired
    private RolePrivilegeService rolePrivilegeService;
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
    private SmsProvider smsProvider;
	
	@Autowired
    private MessagingService messagingService;
	
	@Autowired
	private AssetStandardProvider assetStandardProvider;
	
	@Autowired
	private AssetStatisticProvider assetStatisticProvider;
	
	/**
	 * 定时任务：定时将未出账单转成已出账单
	 */
	@Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron="0/10 * *  * * ? ")   //每10秒执行一次
    public void updateBillSwitchOnTimeTask() {
    	updateBillSwitchOnTime();
    }
	
	/**
     * 定时任务：更新欠费状态，并计算滞纳金
     */
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron="0/10 * *  * * ? ")   //每10秒执行一次
    public void lateFineCalTask(){
    	lateFineCal();
    }
    
    /**
    * 定时任务：查询所有有设置的园区的账单，拿到最晚交付日，根据map中拿到configs，判断是否符合发送要求，符合则催缴
    */
   @Scheduled(cron = "0 0 12 * * ?")
   //@Scheduled(cron="0/10 * *  * * ? ")   //每10秒执行一次
   public void autoBillNoticeTask() {
	   autoBillNotice();
   }
   
   /**
	 * 定时任务：每天晚上12点定时计算一下欠费天数并更新！(账单组的最晚还款日（eh_payment_bills ： due_day_deadline）)
	 */
   @Scheduled(cron = "0 0 0 * * ?")
   //@Scheduled(cron="0/10 * *  * * ? ")   //每10秒执行一次
   public void updateBillDueDayCountOnTimeTask() {
	   updateBillDueDayCountOnTime();
   }
   
   public void updateBillDueDayCountOnTime() {
       if(RunningFlag.fromCode(scheduleProvider.getRunningFlag())==RunningFlag.TRUE) {
       	//获得账单,分页一次最多10000个，防止内存不够
           int pageSize = 10000;
           long pageAnchor = 1l;
           SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
           Date today = new Date();
           coordinationProvider.getNamedLock(CoordinationLocks.BILL_DUEDAYCOUNT_UPDATE.getCode()).tryEnter(() -> {
           	//根据账单组的最晚还款日（eh_payment_bills ： due_day_deadline）以及当前时间计算欠费天数
           	Long nextPageAnchor = 0l;
               while(nextPageAnchor != null){
                   SettledBillRes res = assetProvider.getSettledBills(pageSize,pageAnchor);
                   List<PaymentBills> bills = res.getBills();
                   //更新账单
                   for(PaymentBills bill : bills){
                       String dueDayDeadline = bill.getDueDayDeadline();
                       try{
                           Date deadline = yyyyMMdd.parse(dueDayDeadline);
                           Long dueDayCount = (today.getTime() - deadline.getTime()) / ((1000*3600*24));
                           if(dueDayCount.compareTo(0l) < 0) {
                           	dueDayCount = null;
                           }
                           assetProvider.updateBillDueDayCount(bill.getId(), dueDayCount);//更新账单欠费天数
                       } catch (Exception e){ continue; };
                   }
                   nextPageAnchor = res.getNextPageAnchor();
               }
           });
       }
   }
   
   public void autoBillNotice() {
       if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
           this.coordinationProvider.getNamedLock("asset_auto_notice").tryEnter(() -> {
               SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
               Calendar today = newClearedCalendar();
               List<PaymentNoticeConfig> configs = assetProvider.listAllNoticeConfigs();
               Map<Long, List<PaymentNoticeConfig>> noticeConfigs = new HashMap<>();
               for (int i = 0; i < configs.size(); i++) {
                   PaymentNoticeConfig config = configs.get(i);
                   if (noticeConfigs.containsKey(config.getOwnerId())) {
                       noticeConfigs.get(config.getOwnerId()).add(config);
                   } else {
                       List<PaymentNoticeConfig> configList = new ArrayList<>();
                       configList.add(config);
                       noticeConfigs.put(config.getOwnerId(), configList);
                   }
               }
               Map<Long, PaymentBills> needNoticeBills = new HashMap<>();
               Map<Long, PaymentNoticeConfig> noticeConfigMap = new HashMap<>();
               // noticeConfig map中存有communityid和notice days
               for (Map.Entry<Long, List<PaymentNoticeConfig>> map : noticeConfigs.entrySet()) {
                   List<PaymentBills> bills = assetProvider.getAllBillsByCommunity(null,map.getKey());
                   for (int i = 0; i < bills.size(); i++) {
                       PaymentBills bill = bills.get(i);
                       if (!needNoticeBills.containsKey(bill.getId())) {
                           List<PaymentNoticeConfig> days = map.getValue();
                           for (int j = 0; j < days.size(); j++) {
                               PaymentNoticeConfig day = days.get(j);
                               String dueDayDeadline = bill.getDueDayDeadline();
                               try {
                                   //比较此config与账单的时间，看是否应该催缴
                                   Calendar deadline = newClearedCalendar();
                                   deadline.setTime(yyyyMMdd.parse(dueDayDeadline));
                                   if(day.getNoticeDayType() != null && day.getNoticeDayType().byteValue() == NoticeDayType.AFTER.getCode()){
                                       deadline.add(Calendar.DAY_OF_MONTH, day.getNoticeDayAfter());
                                   }else{
                                       deadline.add(Calendar.DAY_OF_MONTH, -day.getNoticeDayBefore());
                                   }
                                   //符合催缴的日期设定就催缴
                                   String todayInDate = yyyyMMdd.format(today.getTime());
                                   String deadlineInDate = yyyyMMdd.format(deadline.getTime());
                                   if (todayInDate.equalsIgnoreCase(deadlineInDate)) {
                                       needNoticeBills.put(bill.getId(), bill);
                                       noticeConfigMap.put(bill.getId(), day);
                                   }
                               } catch (Exception e) {
                                   continue;
                               }
                           }
                       }
                   }
               }
               for (Map.Entry<Long, PaymentBills> entry : needNoticeBills.entrySet()) {
                   List<Long> billIds = new ArrayList<>();
                   Set<NoticeInfo> noticeInfoList = new HashSet<>();
                   PaymentBills b = entry.getValue();
                   billIds.add(b.getId());
                   NoticeInfo info = new NoticeInfo();
                   info.setDateStr(b.getDateStr());
                   info.setTargetName(b.getTargetName());
                   info.setAmountOwed(b.getAmountOwed());
                   info.setAmountRecevable(b.getAmountReceivable());
                   info.setAppName(assetProvider.findAppName(b.getNamespaceId()));
                   info.setOwnerId(b.getOwnerId());
                   info.setOwnerType(b.getOwnerType());
                   //增加模板id by wentian sama @ 2018.5.9   u lies to me~
                   info.setUseTemplate(true);
                   PaymentNoticeConfig specificConfig = noticeConfigMap.get(entry.getKey());
                   info.setMsgTemplateId(specificConfig.getNoticeMsgId());
                   info.setAppTemplateId(specificConfig.getNoticeAppId());
                   if(info.getMsgTemplateId() == null && info.getAppTemplateId() == null){
                       continue;
                   }
                   @SuppressWarnings("unchecked")
					List<NoticeObj> noticeObjs = (List<NoticeObj>)new Gson()
                           .fromJson(specificConfig.getNoticeObjs(), new TypeToken<List<NoticeObj>>() {
                           }.getType());
                   if(noticeObjs == null){
                       noticeObjs = new ArrayList<>();
                   }
                   //根据催缴账单催缴
                   info.setPhoneNums(b.getNoticetel());
                   info.setTargetType(b.getTargetType());
                   info.setTargetId(b.getTargetId());
                   // 增加域空间信息
                   info.setNamespaceId(b.getNamespaceId());
                   noticeInfoList.add(info);
                   //待发送人员如如果是定义好的，之类就转成个人，再来一个info
                   List<NoticeMemberIdAndContact> userIds = new ArrayList<>();
                   for (NoticeObj obj : noticeObjs) {
                       Long noticeObjId = obj.getNoticeObjId();
                       String noticeObjType = obj.getNoticeObjType();
                       FlowUserSourceType sourceTypeA = FlowUserSourceType.fromCode(noticeObjType);
                       if(sourceTypeA == null){
                           LOGGER.error("sourceType faild to fromcode, noticeObjType={},noticeConfigMap.getConfigId={}"
                                   ,noticeObjType, noticeConfigMap.get(b.getId()).getId());
                           continue;
                       }
                       switch (sourceTypeA) {
                           // 具体部门
                           case SOURCE_DEPARTMENT:
                               userIds.addAll(getAllMembersFromDepartment(noticeObjId, "USER","UNTRACK"));
                               break;
                           case SOURCE_USER:
                               NoticeMemberIdAndContact c = new NoticeMemberIdAndContact();
                               c.setTargetId(noticeObjId);
                               c.setContactToken(userProvider.findUserTokenOfUser(noticeObjId));
                               userIds.add(c);
                               break;
							default:
								break;
                       }
                   }

                   //组织架构中选择的部门或者个人用户也进行发送短信，注意，概念上来讲这些是通知对象，不是催缴对象 by wentian @2018/5/10
                   for(NoticeMemberIdAndContact uid : userIds){
                       try {
                       	if(uid.getTargetId() == info.getTargetId()){
                               continue;
                           }
                           NoticeInfo newInfo = CopyUtils.deepCopy(info);
                           newInfo.setTargetId(uid.getTargetId());
                           newInfo.setPhoneNums(uid.getContactToken());
                           newInfo.setTargetType(AssetPaymentStrings.EH_USER);
                           noticeInfoList.add(newInfo);
                       } catch (Exception e) {
                           LOGGER.error("failed to have a new notice info, new info is ={}",info,e);
                       }
                   }
                   //一个一个发，因为每个账单的变量注入值不一样
                   LOGGER.info("billIds size should be one, now = {}",billIds.size());
                   NoticeWithTextAndMessage(billIds, new ArrayList<>(noticeInfoList));
               }
               LOGGER.info("done");
           });
       }
   	}
    
	public void updateBillSwitchOnTime(){
    	if(RunningFlag.fromCode(scheduleProvider.getRunningFlag())==RunningFlag.TRUE) {
            coordinationProvider.getNamedLock(CoordinationLocks.BILL_STATUS_UPDATE.getCode()).tryEnter(() -> {
            	//修复issue-36575 【新微创源】企业账单：已出账单依旧在未出账单中
                Calendar c = newClearedCalendar();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String billDateStr = sdf.format(c.getTime());
                assetProvider.updateBillSwitchOnTime(billDateStr);
            });
        }
    }
    
    public void lateFineCal() {
    	if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
    		/**
             * 1. 遍历所有的账单（所有维度），更新账单的欠费状态
             * 2. 遍历所有的账单（所有维度），拿到所有欠费的账单，拿到所有的billItem的itemd
             * 3. 遍历所有的billItem和他们对应的滞纳规则，计算，然后新增滞纳的数据,update bill, 不用锁，一条一条走
             */
            //获得账单,分页一次最多10000个，防止内存不够
            int pageSize = 10000;
            long pageAnchor = 1l;
            SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            coordinationProvider.getNamedLock("update_bill_and_late_fine").tryEnter(()->{
                Long nextPageAnchor = 0l;
                while(nextPageAnchor != null){
                    List<Long> overdueBillIds = new ArrayList<>();
                    SettledBillRes res = assetProvider.getSettledBills(pageSize,pageAnchor);
                    List<PaymentBills> bills = res.getBills();
                    //更新账单
                    for(PaymentBills bill : bills){
                        String dueDayDeadline = bill.getDueDayDeadline();
                        try{
                            Date deadline = yyyyMMdd.parse(dueDayDeadline);
//                        if(bill.getChargeStatus().byteValue() == 0 && deadline.compareTo(today) != 1) {  兼容以前的没有正常欠费状态的账单
                            if(deadline.compareTo(today) != 1) {
                                assetProvider.changeBillToDue(bill.getId());
                                bill.setChargeStatus((byte)1);
                            }
                            if(bill.getChargeStatus().byteValue() == (byte)1) overdueBillIds.add(bill.getId());
                        } catch (Exception e){ continue; };
                    }
                    nextPageAnchor = res.getNextPageAnchor();
                    //这10000个账单中欠费的billItem
                    List<PaymentBillItems> billItems = assetProvider.getBillItemsByBillIds(overdueBillIds);
                    for(int i = 0; i < billItems.size(); i++){
                        PaymentBillItems item = billItems.get(i);
                        //没有关联滞纳金标准的不计算，剔除出更新队列
                        if(item.getLateFineStandardId() == null){
                            billItems.remove(i--);
                            continue;
                        }
                        //计算滞纳金金额
                        //获得欠费的钱
                        BigDecimal amountOwed = new BigDecimal("0");
                        if(item.getAmountOwed() !=null){
                            amountOwed = amountOwed.add(item.getAmountOwed());
                        }else{
                            item.setAmountOwed(new BigDecimal("0"));
                            assetProvider.updatePaymentItem(item);
                        }
                        amountOwed = amountOwed.add(assetProvider.getLateFineAmountByItemId(item.getId()));
                        List<PaymentFormula> formulas = assetStandardProvider.getFormulas(item.getLateFineStandardId());
                        if(formulas.size() != 1) {
                            LOGGER.error("late fine cal error, the corresponding formula is more than one or less than one, the bill item id is "+item.getId());
                        }
                        String formulaJson = formulas.get(0).getFormulaJson();
                        if(formulaJson.contains("qf")) {//issue-34468 【物业缴费】执行接口，报错
                        	formulaJson = formulaJson.replace("qf",amountOwed.toString());
	                        BigDecimal fineAmount = CalculatorUtil.arithmetic(formulaJson);
	                        //开始构造一条滞纳金记录
	                        //查看item是否已经有滞纳金产生了
	                        PaymentLateFine fine = assetProvider.findLastedFine(item.getId());
	                        boolean isInsert = false;
	                        if(fine == null){
	                            isInsert = true;
	                            fine = new PaymentLateFine();
	                            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPaymentLateFine.class));
	                            fine.setId(nextSequence);
	                            fine.setName(item.getChargingItemName() + "滞纳金");
	                            fine.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                            fine.setBillId(item.getBillId());
	                            fine.setBillItemId(item.getId());
	                            fine.setCommunityId(item.getOwnerId());
	                            fine.setNamespaceId(item.getNamespaceId());
	                            fine.setCustomerId(item.getTargetId());
	                            fine.setCustomerType(item.getTargetType());
	                        }
	                        fine.setAmount(fineAmount);
	                        fine.setUpateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	                        assetProvider.updateLateFineAndBill(fine,fineAmount,item.getBillId(), isInsert);
	                        // 重新计算下账单
	                        assetProvider.reCalBillById(item.getBillId());
                        }
                    }
                }
            });
    	}
    }
	
    private Calendar newClearedCalendar() {
        Calendar instance = Calendar.getInstance();
        instance.clear(Calendar.DATE);
        return instance;
    }
    
    private List<NoticeMemberIdAndContact> getAllMembersFromDepartment(Long noticeObjId, String ... types) {
        return organizationProvider.findActiveUidsByTargetTypeAndOrgId( noticeObjId,types);
    }
    
    private void NoticeWithTextAndMessage(List<Long> billIds, List<NoticeInfo> noticeInfos) {
        List<AssetAppNoticePak> uids = new ArrayList<>();
                //客户在系统内，把需要推送的uid放在list中
        List<NoticeInfo> extraInfos = new ArrayList<>();
        for (int i = 0; i < noticeInfos.size(); i++) {
            NoticeInfo noticeInfo = noticeInfos.get(i);
            Long targetId = noticeInfo.getTargetId();
            if (targetId != null && targetId != 0l) {
                if (noticeInfo.getTargetType().equals(AssetTargetType.USER.getCode())) {
                    AssetAppNoticePak pak = createAssetAppNoticePak(noticeInfo);
                    pak.setUid(noticeInfo.getTargetId());
                    uids.add(pak);
                } else if (noticeInfo.getTargetType().equals(AssetTargetType.ORGANIZATION.getCode())) {
                    ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
                    cmd1.setOrganizationId(noticeInfo.getTargetId());
                    cmd1.setActivationFlag((byte)1);
                    cmd1.setOwnerType("EhOrganizations");
                    cmd1.setOwnerId(null);
                    List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
                    for (int j = 0; j < organizationContactDTOS.size(); j++) {
                        AssetAppNoticePak pak = createAssetAppNoticePak(noticeInfo);
                        pak.setUid(organizationContactDTOS.get(j).getTargetId());
                        // 新增一个通知对象，电话改下
                        NoticeInfo info = ConvertHelper.convert(noticeInfo, NoticeInfo.class);
                        info.setPhoneNums(organizationContactDTOS.get(j).getContactToken());

                        extraInfos.add(info);
                        uids.add(pak);
                    }
                    LOGGER.info("notice paks assembled = {}"+uids);
                }
            }
        }
        noticeInfos.addAll(extraInfos);
        try {
            for (int i = 0; i < noticeInfos.size(); i++) {
                NoticeInfo noticeInfo = noticeInfos.get(i);
                String phoneNums = noticeInfo.getPhoneNums();
                if(phoneNums == null){
                    continue;
                }
                String[] telNOs = phoneNums.split(",");
                List<Tuple<String, Object>> variables = new ArrayList<>();
//                Integer nameSpaceId = UserContext.getCurrentNamespaceId();
                Integer nameSpaceId = noticeInfo.getNamespaceId();
                assetService.injectSmsVars(noticeInfo, variables,nameSpaceId);
                //issue-38265 解决“给用户设置自动催缴设置，用户收不到短信提醒”的bug
                String templateLocale = Locale.SIMPLIFIED_CHINESE.toString();
                int templateId = SmsTemplateCode.PAYMENT_NOTICE_CODE;
                boolean smsGo = true;
                try{
                    //从自动催缴来的任务，如果没有模板id，就不催缴。
                    if(noticeInfo.isUseTemplate() != null && noticeInfo.isUseTemplate()){
                        if(noticeInfo.getMsgTemplateId() == null){
                            smsGo = false;
                        }else{
                            templateId = noticeInfo.getMsgTemplateId().intValue();
                        }
                    }
                }catch (Exception e){
                    smsGo = false;
                    LOGGER.error("sms notice failed once, noticeinfo = {}",noticeInfo, e);
                }
                if(smsGo) smsProvider.sendSms(nameSpaceId, telNOs, SmsTemplateCode.SCOPE, templateId, templateLocale, variables);
            }
        } catch(Exception e){
            LOGGER.error("sms notice failed once",e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "sms notice failed once");
        }

        for (int k = 0; k < uids.size(); k++) {
            try {
                AssetAppNoticePak pak = uids.get(k);
                //targetId为0的，也就是untrack的用户，不用发app信息
                if(pak.getUid() == null){
                    continue;
                }
                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
                messageDto.setSenderUid(2L);

                messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                int msgId = UserNotificationTemplateCode.USER_PAYMENT_NOTICE;
                String msgScope = UserNotificationTemplateCode.SCOPE;
                boolean appGo = true;
                try{
                    if(pak.getUseTemplate() != null && pak.getUseTemplate() == true){
                        //自动催缴设置了催缴模板的话，就覆盖默认的
                        if(pak.getTemplateId() != null){
                            msgScope = UserNotificationTemplateCode.ASSET_APP_NOTICE_SCOPE;
                            msgId = pak.getTemplateId().intValue();
                        }else{
                            //从自动催缴来的任务，如果没有模板id，就不催缴
                            appGo = false;
                        }
                    }
                }catch (Exception e){
                    appGo = false;
                    LOGGER.error("app notice failed, pak={}",pak,e);
                }
                String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(null, msgScope, msgId, "zh_CN", pak.getVaribles(), "");
                messageDto.setBody(notifyTextForApplicant);
                messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
                if (!StringUtils.isBlank(notifyTextForApplicant) && appGo) {
                    String pakUidStr = String.valueOf(pak.getUid());
                    LOGGER.info("pakUidStr" + pakUidStr);
                    messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), pakUidStr));
                    messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING
                            , MessageChannelType.USER.getCode(), pakUidStr
                            , messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
                }
            } catch (Exception e) {
                LOGGER.error("WUYE BILL SENDING MESSAGE FAILED", e);
            }
        }
        assetProvider.increaseNoticeTime(billIds);
    }
    
    private AssetAppNoticePak createAssetAppNoticePak(NoticeInfo noticeInfo) {
        AssetAppNoticePak pak = new AssetAppNoticePak();
        try{
            pak.setTemplateId(noticeInfo.getAppTemplateId()==null?null:noticeInfo.getAppTemplateId().intValue());
        }catch (Exception e){
            pak.setTemplateId(null);
            LOGGER.error("pak for app msg failed to get templateId from noticeInfo which is ={}",noticeInfo);
        }
        pak.addToVariables("targetName", noticeInfo.getTargetName());
        pak.addToVariables("dateStr", noticeInfo.getDateStr());
        pak.addToVariables("amount", noticeInfo.getAmountOwed().toPlainString());
        pak.addToVariables("appName", noticeInfo.getAppName());
        pak.setUseTemplate(noticeInfo.isUseTemplate());
        return pak;
    }
    
    /**
     * issue-38508 根据项目+月份统计缴费报表
     * 1、取出eh_payment_bills表中dateStr（年月）
     * 2、取出eh_payment_bill_statistic_community表中dateStr（年月）
     * 3、比较eh_payment_bills表中dateStr（年月） 与 eh_payment_bill_statistic_community表中的dateStr（年月）
     * 	 1）如果eh_payment_bill_statistic_community表中没有对应的年月，那么需要创建该年月的统计结果集记录；
     * 	 2）如果eh_payment_bill_statistic_community表中有对应的年月，那么需要校验eh_payment_bills表中账单数据的updateTime时间，如果大于n-1天，那么该年月的统计结果集应该重新计算；
     */
    public void statisticBillByCommunity() {
    	if(RunningFlag.fromCode(scheduleProvider.getRunningFlag())==RunningFlag.TRUE) {
    		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            coordinationProvider.getNamedLock(CoordinationLocks.STATISTIC_BILL_BY_COMMUNITY.getCode()).tryEnter(() -> {
            	//1、取出eh_payment_bills表中dateStr（年月）
            	List<BillsDateStrDTO> billsDateStrDTOList = assetProvider.getPaymentBillsDatrStr();   
            	
            	//2、取出eh_payment_bill_statistic_community表中dateStr（年月）
            	List<BillsDateStrDTO> statisticDateStrDTOList = assetProvider.getStatisticDatrStr();     
            	
                //3、比较eh_payment_bills表中dateStr（年月） 与 eh_payment_bill_statistic_community表中的dateStr（年月）
                //1）如果eh_payment_bill_statistic_community表中没有对应的年月，那么需要创建该年月的统计结果集记录；
            	List<BillsDateStrDTO> billsDateStrDTOListClone = new ArrayList<BillsDateStrDTO>();
            	billsDateStrDTOListClone.addAll(billsDateStrDTOList);
            	List<BillsDateStrDTO> statisticDateStrDTOListClone = new ArrayList<BillsDateStrDTO>();
            	statisticDateStrDTOListClone.addAll(statisticDateStrDTOList);
            	//eh_payment_bills 与  eh_payment_bill_statistic_community 的差集
            	billsDateStrDTOListClone.removeAll(statisticDateStrDTOListClone);
            	for(BillsDateStrDTO billsDateStrDTO : billsDateStrDTOListClone) {
            		//1）如果eh_payment_bill_statistic_community表中没有对应的年月，那么需要创建该年月的统计结果集记录；
            		assetStatisticProvider.createStatisticByCommnunity(billsDateStrDTO.getNamespaceId(), 
            				billsDateStrDTO.getOwnerId(), billsDateStrDTO.getOwnerType(), billsDateStrDTO.getDateStr());
            	}
            	
            	//2）如果eh_payment_bill_statistic_community表中有对应的年月，那么需要校验eh_payment_bills表中账单数据的updateTime时间，如果大于n-1天，那么该年月的统计结果集应该重新计算；
            	List<BillsDateStrDTO> billsDateStrDTOListClone2 = new ArrayList<BillsDateStrDTO>();
            	billsDateStrDTOListClone2.addAll(billsDateStrDTOList);
            	List<BillsDateStrDTO> statisticDateStrDTOListClone2 = new ArrayList<BillsDateStrDTO>();
            	statisticDateStrDTOListClone2.addAll(statisticDateStrDTOList);
            	//eh_payment_bills 与  eh_payment_bill_statistic_community 的交集
            	billsDateStrDTOListClone2.retainAll(statisticDateStrDTOListClone2);
            	for(BillsDateStrDTO billsDateStrDTO : billsDateStrDTOListClone) {
            		//2）如果eh_payment_bill_statistic_community表中有对应的年月，那么需要校验eh_payment_bills表中账单数据的updateTime时间，如果大于n-1天，那么该年月的统计结果集应该重新计算；
            		
            		
            		
            	}
            	
       
            });
       }
    }
	
}
