// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.general.order.GorderPayType;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.util.RuntimeErrorException;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.SelectConditionStep;
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.print.PrintOrderLockType;
import com.everhomes.rest.print.PrintOrderStatusType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintOrdersDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintOrders;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PascalCaseStrategy;

@Component
public class SiyinPrintOrderProviderImpl implements SiyinPrintOrderProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintOrderProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
    private ScheduleProvider scheduleProvider;

	@Autowired
	public com.everhomes.paySDK.api.PayService sdkPayService;
    @PostConstruct
    public void setup(){
        //启动定时任务
    	String triggerName = "SiyinQueryRecord";
    	String jobName= "SiyinQueryRecord"+System.currentTimeMillis();
    	//每30分钟查询向司印查询一下日志。
    	String cronExpression = "0 */30 * * * ?";
        scheduleProvider.scheduleCronJob(triggerName,jobName,cronExpression,SiyinTaskLogScheduleJob.class , null);
    }

	@Override
	public void createSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintOrders.class));
		siyinPrintOrder.setId(id);
		//司印回调，这里没有用户登录，所有订单创建者在订单对象里面已经设置好。
		siyinPrintOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintOrder.setOperateTime(siyinPrintOrder.getCreateTime());
		getReadWriteDao().insert(siyinPrintOrder);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintOrders.class, null);
	}

	@Override
	public void updateSiyinPrintOrder(SiyinPrintOrder siyinPrintOrder) {
		assert (siyinPrintOrder.getId() != null);
		//司印回调，这里没有用户登录，所有订单创建者在订单对象里面已经设置好。
		siyinPrintOrder.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(siyinPrintOrder);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintOrders.class, siyinPrintOrder.getId());
	}

	@Override
	public SiyinPrintOrder findSiyinPrintOrderById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintOrder.class);
	}
	
	@Override
	public List<SiyinPrintOrder> listSiyinPrintOrder(Timestamp startTime, Timestamp endTime, List<Object> ownerTypeList,
			List<Object> ownerIdList) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(DSL.trueCondition());
		if(startTime!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.CREATE_TIME.ge(startTime));
		}
		if(endTime!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.CREATE_TIME.le(endTime));
		}
		Condition condition = null;
		for (int i = 0; i < ownerTypeList.size(); i++) {
			if(condition == null)
				condition = Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerTypeList.get(i).toString())
					.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(Long.valueOf(ownerIdList.get(i).toString())));
			else
				condition = condition.or(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerTypeList.get(i).toString())
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(Long.valueOf(ownerIdList.get(i).toString()))));
		}
		query = query.and(condition);
		LOGGER.info("listSiyinPrintOrder sql = {}, param = {}.",query.getSQL(),query.getBindValues());
		return query.orderBy(Tables.EH_SIYIN_PRINT_ORDERS.ID.asc())
		.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintOrder.class));
	}
	
	private EhSiyinPrintOrdersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintOrdersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintOrdersDao getDao(DSLContext context) {
		return new EhSiyinPrintOrdersDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}

	@Override
	public List<SiyinPrintOrder> listSiyinPrintUnpaidOrderByUserId(Long userId,String ownerType, Long ownerId) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(Tables.EH_SIYIN_PRINT_ORDERS.CREATOR_UID.eq(userId))
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_SIYIN_PRINT_ORDERS.ORDER_STATUS.eq(PrintOrderStatusType.UNPAID.getCode()));
		LOGGER.info("listSiyinPrintUnpaidOrderByUserId sql = {}, param = {}.",query.getSQL(),query.getBindValues());
		return query.fetch()
				.map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
	
	}

	@Override
	public List<SiyinPrintOrder> listSiyinPrintOrderByUserId(Long userId, Integer pageSize, Long pageAnchor,String ownerType, Long ownerId) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(Tables.EH_SIYIN_PRINT_ORDERS.CREATOR_UID.eq(userId))
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(ownerId));
		if(pageAnchor!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.ID.le(pageAnchor));
		}
		LOGGER.info("listSiyinPrintOrderByUserId sql = {}, param = {}.",query.getSQL(),query.getBindValues());
		return query.orderBy(Tables.EH_SIYIN_PRINT_ORDERS.ID.desc())
				.limit(pageSize)
				.fetch()
				.map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
	}

	@Override
	public void updateSiyinPrintOrderLockFlag(Long id, byte lockFlag) {
		UpdateConditionStep<?> query = getReadWriteContext().update(Tables.EH_SIYIN_PRINT_ORDERS).set(Tables.EH_SIYIN_PRINT_ORDERS.LOCK_FLAG,lockFlag)
			.where(Tables.EH_SIYIN_PRINT_ORDERS.ID.eq(id));
		LOGGER.info("updateSiyinPrintOrderLockFlag sql = {}, param = {}.",query.getSQL(),query.getBindValues());
		query.execute();
	}

	@Override
	public SiyinPrintOrder findSiyinPrintOrderByOrderNo(Long orderNo) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(Tables.EH_SIYIN_PRINT_ORDERS.ORDER_NO.eq(orderNo));
		LOGGER.info("findSiyinPrintOrderByOrderNo sql = {}, param = {}.",query.getSQL(),query.getBindValues());
		List<SiyinPrintOrder> list  = query.fetch().map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

	@Override
	public SiyinPrintOrder findUnpaidUnlockedOrderByUserId(Long userId,Byte jobType,String ownerType, Long ownerId, String printerName) {
		SelectConditionStep<?> query =  getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
			.where(Tables.EH_SIYIN_PRINT_ORDERS.CREATOR_UID.eq(userId))
			.and(Tables.EH_SIYIN_PRINT_ORDERS.ORDER_STATUS.eq(PrintOrderStatusType.UNPAID.getCode()))
			.and(Tables.EH_SIYIN_PRINT_ORDERS.LOCK_FLAG.eq(PrintOrderLockType.UNLOCKED.getCode()))
			.and(Tables.EH_SIYIN_PRINT_ORDERS.JOB_TYPE.eq(jobType))
			.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerType))
			.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(ownerId));
		
		if (!StringUtils.isBlank(printerName)) {
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.PRINTER_NAME.eq(printerName));
		}
		
		LOGGER.info("findUnpaidUnlockedOrderByUserId sql = {}, param = {}.",query.getSQL(),query.getBindValues());
		List<SiyinPrintOrder> list  = query.fetch()
			.map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

	@Override
	public List<SiyinPrintOrder> listSiyinPrintOrderByOwners(List<Object> ownerTypeList,
			List<Object> ownerIdList, Timestamp startTime,
			Timestamp endTime, Byte jobType, Byte orderStatus, String keywords, Long pageAnchor, Integer pageSize, Byte payMode, String payType) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
			.where(DSL.trueCondition());
		Condition condition = null;
		for (int i = 0; i < ownerTypeList.size(); i++) {
			if(condition == null)
				condition = Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerTypeList.get(i).toString())
					.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(Long.valueOf(ownerIdList.get(i).toString())));
			else
				condition = condition.or(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_TYPE.eq(ownerTypeList.get(i).toString())
				.and(Tables.EH_SIYIN_PRINT_ORDERS.OWNER_ID.eq(Long.valueOf(ownerIdList.get(i).toString()))));
		}
		query = query.and(condition);
		if(startTime!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.CREATE_TIME.ge(startTime));
		}
		if(endTime!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.CREATE_TIME.le(endTime));
			
		}
		if(jobType!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.JOB_TYPE.eq(jobType));
		}
		
		if(orderStatus!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.ORDER_STATUS.eq(orderStatus));
		}
		
		if(payMode!=null){
			if (payMode == GorderPayType.ENTERPRISE_PAID.getCode()){
				List<Byte> modes = new ArrayList();
				modes.add(GorderPayType.ENTERPRISE_PAY.getCode());
				modes.add(GorderPayType.WAIT_FOR_ENTERPRISE_PAY.getCode());
				query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.PAY_MODE.in(modes));
			} else
				query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.PAY_MODE.eq(payMode));
		}
		if(payType!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.PAID_TYPE.eq(payType));
		}
		if(keywords!=null){
			condition = Tables.EH_SIYIN_PRINT_ORDERS.NICK_NAME.like("%"+keywords+"%");
			condition = condition.or(Tables.EH_SIYIN_PRINT_ORDERS.CREATOR_COMPANY.like("%"+keywords+"%"));
			condition = condition.or(Tables.EH_SIYIN_PRINT_ORDERS.CREATOR_PHONE.like("%"+keywords+"%"));
			query = query.and(condition);
		}
		
		if(pageAnchor!=null){
			query = query.and(Tables.EH_SIYIN_PRINT_ORDERS.ID.le(pageAnchor));
		}
		
		LOGGER.info("listSiyinPrintOrderByOwners sql = {},param = {}",query.getSQL(),query.getBindValues());
		
		return query.orderBy(Tables.EH_SIYIN_PRINT_ORDERS.ID.desc()).limit(pageSize)
				.fetch()
				.map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
	}

	@Override
	public ListBizPayeeAccountDTO createPersonalPayUserIfAbsent(String userId, String accountCode, String userIdentifier, Object o, Object o1, Object o2) {
		String payerid = OwnerType.USER.getCode()+userId;
		LOGGER.info("createPersonalPayUserIfAbsent payerid = {}, accountCode = {}, userIdenify={}",payerid,accountCode,userIdentifier);
		PayUserDTO payUserList = sdkPayService.createPersonalPayUserIfAbsent(payerid, accountCode);
		if(payUserList==null){
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_CREATE_USER_ACCOUNT,
					"创建个人付款账户失败");
		}
		String s = sdkPayService.bandPhone(payUserList.getId(), userIdentifier);
		ListBizPayeeAccountDTO dto = new ListBizPayeeAccountDTO();
		dto.setAccountId(payUserList.getId());
		dto.setAccountType(payUserList.getUserType()==2? OwnerType.ORGANIZATION.getCode():OwnerType.USER.getCode());//帐号类型，1-个人帐号、2-企业帐号
		dto.setAccountName(payUserList.getUserName());
		dto.setAccountAliasName(payUserList.getUserAliasName());
		if(payUserList.getRegisterStatus()!=null) {
			dto.setAccountStatus(Byte.valueOf(payUserList.getRegisterStatus() + ""));
		}
		return dto;
	}

	@Override
	public SiyinPrintOrder findSiyinPrintOrderByGeneralOrderId(String BizOrderNum) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(Tables.EH_SIYIN_PRINT_ORDERS.GENERAL_ORDER_ID.eq(BizOrderNum));
		List<SiyinPrintOrder> list  = query.fetch().map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	@Override
	public SiyinPrintOrder findSiyinPrintOrderByGeneralBillId(String generalBillId) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_ORDERS)
				.where(Tables.EH_SIYIN_PRINT_ORDERS.GENERAL_BILL_ID.eq(generalBillId));
		List<SiyinPrintOrder> list  = query.fetch().map(r->ConvertHelper.convert(r, SiyinPrintOrder.class));
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}
}
