package com.everhomes.officecubicle;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleChargeUsersDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleConfigs;
import com.everhomes.user.UserContext;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rentalv2.RentalCloseDate;
import com.everhomes.rentalv2.RentalResource;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rest.officecubicle.OfficeOrderStatus;
import com.everhomes.rest.officecubicle.OfficeStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOfficeCubicleAttachments;
import com.everhomes.server.schema.tables.EhOfficeCubicleCategories;
import com.everhomes.server.schema.tables.EhOfficeCubicleChargeUsers;
import com.everhomes.server.schema.tables.EhOfficeCubicleRefundRule;
import com.everhomes.server.schema.tables.EhOfficeCubicleRoom;
import com.everhomes.server.schema.tables.EhOfficeCubicleStation;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleOrdersDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleRentOrdersDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleRoomDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleSpacesDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleStationDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2DefaultRulesDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2ResourceTypesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleOrders;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRentOrders;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSpaces;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleStationRent;
import com.everhomes.server.schema.tables.pojos.EhRentalv2DefaultRules;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceTypes;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleChargeUsersRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleOrdersRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleRefundRuleRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleRentOrdersRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleRoomRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleSpacesRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleStationRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleStationRentRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2CloseDatesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourcesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2TimeIntervalRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class OfficeCubicleProviderImpl implements OfficeCubicleProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
	@Override
	public List<OfficeCubicleCategory> queryCategoriesBySpaceId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_CATEGORIES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_CATEGORIES.SPACE_ID.equal(id);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_CATEGORIES.STATUS.equal(CommonStatus.ACTIVE.getCode()));
		step.where(condition);
		List<OfficeCubicleCategory> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_CATEGORIES.ID.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleCategory.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<OfficeCubicleAttachment> listAttachmentsBySpaceId(Long id,Byte ownerType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.OWNER_ID.equal(id);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.TYPE.equal(ownerType));
		step.where(condition);
		List<OfficeCubicleAttachment> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.ID.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleAttachment.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	
	@Override
	public List<OfficeCubicleRefundRule> listRefundRuleBySpaceId(Long spaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_REFUND_RULE);
		Condition condition = Tables.EH_OFFICE_CUBICLE_REFUND_RULE.SPACE_ID.equal(spaceId);
		step.where(condition);
		List<OfficeCubicleRefundRule> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_REFUND_RULE.ID.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleRefundRule.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	
	@Override
	public void createSpace(OfficeCubicleSpace space) {
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleSpaces.class));
		space.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleSpacesRecord record = ConvertHelper.convert(space, EhOfficeCubicleSpacesRecord.class);
		InsertQuery<EhOfficeCubicleSpacesRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_SPACES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleSpaces.class, null);
	}

	@Override
	public void createCategory(OfficeCubicleCategory category) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleCategories.class));
		category.setId(id);
		category.setStatus(CommonStatus.ACTIVE.getCode());
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleCategoriesRecord record = ConvertHelper.convert(category, EhOfficeCubicleCategoriesRecord.class);
		InsertQuery<EhOfficeCubicleCategoriesRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_CATEGORIES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleCategories.class, null);
	}
	

	@Override
	public void createAttachments(OfficeCubicleAttachment attachment) {
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleAttachments.class));
		attachment.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleAttachmentsRecord record = ConvertHelper.convert(attachment, EhOfficeCubicleAttachmentsRecord.class);
		InsertQuery<EhOfficeCubicleAttachmentsRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleAttachments.class, null);
	}

	@Override
	public void createRefundRule(OfficeCubicleRefundRule refundRule) {
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleRefundRule.class));
		refundRule.setId(id);
		refundRule.setCreateTime(new Timestamp(System.currentTimeMillis()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleRefundRuleRecord record = ConvertHelper.convert(refundRule, EhOfficeCubicleRefundRuleRecord.class);
		InsertQuery<EhOfficeCubicleRefundRuleRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_REFUND_RULE);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleRefundRule.class, null);
	}
	
	@Override
	public OfficeCubicleSpace getSpaceById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleSpacesDao dao = new EhOfficeCubicleSpacesDao(context.configuration());
		EhOfficeCubicleSpaces space = dao.findById(id);
		return ConvertHelper.convert(space, OfficeCubicleSpace.class);
	}

	@Override
	public OfficeCubicleSpace getSpaceByOwnerId(Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleSpacesRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_SPACES);
		query.addConditions(Tables.EH_OFFICE_CUBICLE_SPACES.OWNER_ID.eq(ownerId));
		return ConvertHelper.convert(query.fetchAny(), OfficeCubicleSpace.class);

	}
	
	@Override
	public BigDecimal getRoomMinPrice(Long spaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Record record = context.select(DSL.min(Tables.EH_OFFICE_CUBICLE_ROOM.PRICE)).from(Tables.EH_OFFICE_CUBICLE_ROOM)
				.where(Tables.EH_OFFICE_CUBICLE_ROOM.SPACE_ID.eq(spaceId)).fetchOne();
		return record.getValue(DSL.min(Tables.EH_OFFICE_CUBICLE_ROOM.PRICE));
	}
	
	@Override
	public BigDecimal getRoomMaxPrice(Long spaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Record record = context.select(DSL.max(Tables.EH_OFFICE_CUBICLE_ROOM.PRICE)).from(Tables.EH_OFFICE_CUBICLE_ROOM)
				.where(Tables.EH_OFFICE_CUBICLE_ROOM.SPACE_ID.eq(spaceId)).fetchOne();
		return record.getValue(DSL.max(Tables.EH_OFFICE_CUBICLE_ROOM.PRICE));
	}
	@Override
	public BigDecimal getStationMinPrice(Long spaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Record record = context.select(DSL.min(Tables.EH_OFFICE_CUBICLE_STATION.PRICE)).from(Tables.EH_OFFICE_CUBICLE_STATION)
				.where(Tables.EH_OFFICE_CUBICLE_STATION.SPACE_ID.eq(spaceId)).fetchOne();
		return record.getValue(DSL.min(Tables.EH_OFFICE_CUBICLE_STATION.PRICE));
	}
	
	@Override
	public BigDecimal getStationMaxPrice(Long spaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Record record = context.select(DSL.max(Tables.EH_OFFICE_CUBICLE_STATION.PRICE)).from(Tables.EH_OFFICE_CUBICLE_STATION)
				.where(Tables.EH_OFFICE_CUBICLE_STATION.SPACE_ID.eq(spaceId)).fetchOne();
		return record.getValue(DSL.max(Tables.EH_OFFICE_CUBICLE_STATION.PRICE));
	}
	
	@Override
	public void updateSpace(OfficeCubicleSpace space) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleSpacesDao dao = new EhOfficeCubicleSpacesDao(context.configuration());
		dao.update(space);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleSpaces.class, space.getId());

	}
	
	@Override
	public void deleteChargeUsers(Long spaceId) {
	    dbProvider.execute(r -> {
	    	//删除原来的设置
	    	getReadWriteContext().delete(Tables.EH_OFFICE_CUBICLE_CHARGE_USERS)
			.where(Tables.EH_OFFICE_CUBICLE_CHARGE_USERS.SPACE_ID.eq(spaceId));
		    return null;
	    });
	}
	
	@Override
	public void deleteRefundRule(Long spaceId) {
	    dbProvider.execute(r -> {
	    	//删除原来的设置
	    	getReadWriteContext().delete(Tables.EH_OFFICE_CUBICLE_REFUND_RULE)
			.where(Tables.EH_OFFICE_CUBICLE_REFUND_RULE.SPACE_ID.eq(spaceId));
		    return null;
	    });
	}
	
	@Override
	public void createChargeUsers(OfficeCubicleChargeUser user) {
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleChargeUsers.class));
		user.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));
		EhOfficeCubicleChargeUsersRecord record = ConvertHelper.convert(user, EhOfficeCubicleChargeUsersRecord.class);
		InsertQuery<EhOfficeCubicleChargeUsersRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_CHARGE_USERS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleChargeUsers.class, null);
	}

	@Override
	public void updateRoom(OfficeCubicleRoom room) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleRoomDao dao = new EhOfficeCubicleRoomDao(context.configuration());
		dao.update(room);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleRoom.class, room.getId());

	}
	
	@Override
	public void updateCubicle(OfficeCubicleStation station) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleStationDao dao = new EhOfficeCubicleStationDao(context.configuration());
		dao.update(station);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleRoom.class, station.getId());

	}
	
	@Override
	public void deleteCategoriesBySpaceId(Long id) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		DeleteWhereStep<EhOfficeCubicleCategoriesRecord> step = context.delete(Tables.EH_OFFICE_CUBICLE_CATEGORIES);
//		Condition condition = Tables.EH_OFFICE_CUBICLE_CATEGORIES.SPACE_ID.equal(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.update(Tables.EH_OFFICE_CUBICLE_CATEGORIES)
				.set(Tables.EH_OFFICE_CUBICLE_CATEGORIES.STATUS,CommonStatus.INACTIVE.getCode())
				.where(Tables.EH_OFFICE_CUBICLE_CATEGORIES.SPACE_ID.equal(id))
				.execute();
	}

	@Override
	public List<OfficeCubicleOrder> searchOrders(String ownerType,Long ownerId,Long beginDate, Long endDate, String reserveKeyword, String spaceName,
												 CrossShardListingLocator locator, Integer pageSize, Integer currentNamespaceId, Byte workFlowStatus
												) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ORDERS.NAMESPACE_ID.eq(currentNamespaceId);
		if(ownerType!=null) {
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.OWNER_TYPE.eq(ownerType));
		}
		if(ownerId!=null) {
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.OWNER_ID.eq(ownerId));
		}
		if(workFlowStatus !=null){
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.WORK_FLOW_STATUS.eq(workFlowStatus));
		}
		if (null != beginDate)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.gt(new Timestamp(beginDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.lt(new Timestamp(endDate)));
		if (StringUtils.isNotBlank(reserveKeyword))
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVER_NAME.like("%" + reserveKeyword + "%").or(
					Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_CONTACT_TOKEN.like("%" + reserveKeyword + "%")));
		if (StringUtils.isNotBlank(spaceName))
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.SPACE_NAME.like("%" + spaceName + "%"));
		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleOrder> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleOrder.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	
	@Override
	public List<OfficeCubicleRentOrder> searchCubicleOrders(String ownerType,Long ownerId,Long beginDate, Long endDate,
												 CrossShardListingLocator locator, Integer pageSize, Integer currentNamespaceId,String paidType,
												Byte paidMode, Byte requestType, Byte rentType, Byte orderStatus) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.NAMESPACE_ID.eq(currentNamespaceId);
		if(ownerType!=null) {
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.OWNER_TYPE.eq(ownerType));
		}
		if(ownerId!=null) {
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.OWNER_ID.eq(ownerId));
		}
		if (null != beginDate)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.BEGIN_TIME.gt(new Timestamp(beginDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.END_TIME.lt(new Timestamp(endDate)));
		if (null != paidType)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.PAID_TYPE.eq(paidType));
		if (null != paidMode)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.PAID_MODE.eq(paidMode));
		if (null != requestType)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.REQUEST_TYPE.eq(requestType));
		if (null != rentType)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.RENT_TYPE.eq(rentType));
		if (null != orderStatus)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.ORDER_STATUS.eq(orderStatus));
		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleRentOrder> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.OPERATE_TIME.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleRentOrder.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	
	@Override
	public List<OfficeCubicleStationRent> searchCubicleStationRent(Long spaceId,
												 Integer currentNamespaceId, Byte rentType, Byte stationType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_STATION_RENT);
		Condition condition = Tables.EH_OFFICE_CUBICLE_STATION_RENT.NAMESPACE_ID.eq(currentNamespaceId);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_STATION_RENT.BEGIN_TIME.gt(new Timestamp(System.currentTimeMillis())));
		condition.and(Tables.EH_OFFICE_CUBICLE_STATION_RENT.END_TIME.lt(new Timestamp(System.currentTimeMillis())));
		if (null != rentType)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_STATION_RENT.RENT_TYPE.eq(rentType));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_STATION_RENT.STATUS.eq((byte)0));
		step.where(condition);
		List<OfficeCubicleStationRent> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_STATION_RENT.OPERATE_TIME.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleStationRent.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	
	@Override
	public void deleteAttachmentsBySpaceId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhOfficeCubicleAttachmentsRecord> step = context.delete(Tables.EH_OFFICE_CUBICLE_ATTACHMENTS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ATTACHMENTS.OWNER_ID.equal(id);
		step.where(condition);
		step.execute();
	}

	@Override
	public List<OfficeCubicleSpace> searchSpaces(String ownerType,Long ownerId,String keyWords, CrossShardListingLocator locator, int pageSize,
			Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select(Tables.EH_OFFICE_CUBICLE_SPACES.fields()).from(Tables.EH_OFFICE_CUBICLE_SPACES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_SPACES.NAMESPACE_ID.eq(currentNamespaceId) ;
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.STATUS.eq(OfficeStatus.NORMAL.getCode()));
		if(ownerId!=null) {
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.OWNER_ID.eq(ownerId));
		}
		if(ownerType!=null) {
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.OWNER_TYPE.eq(ownerType));
		}
		if (StringUtils.isNotBlank(keyWords)) {
			step.join(Tables.EH_USERS).on(Tables.EH_USERS.ID.eq(Tables.EH_OFFICE_CUBICLE_SPACES.MANAGER_UID));
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.NAME.like("%" + keyWords + "%")
					.or(Tables.EH_OFFICE_CUBICLE_SPACES.CONTACT_PHONE.like("%" + keyWords + "%"))
					.or(Tables.EH_USERS.NICK_NAME.like("%" + keyWords + "%")));
		}

		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleSpace> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_SPACES.ID.desc()).fetchInto(OfficeCubicleSpace.class);
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	
	
	@Override
	public void createOrder(OfficeCubicleOrder order) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleOrders.class));
		order.setId(id);
		order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		order.setUpdateTime(order.getCreateTime());
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleOrdersRecord record = ConvertHelper.convert(order, EhOfficeCubicleOrdersRecord.class);
		InsertQuery<EhOfficeCubicleOrdersRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_ORDERS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleOrders.class, null);
	}

	@Override
	public OfficeCubicleOrder getOrderById(Long orderId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleOrdersDao dao = new EhOfficeCubicleOrdersDao(context.configuration());
		EhOfficeCubicleOrders order = dao.findById(orderId);
		return ConvertHelper.convert(order, OfficeCubicleOrder.class);
	}

	@Override
	public void updateOrder(OfficeCubicleOrder order) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleOrdersDao dao = new EhOfficeCubicleOrdersDao(context.configuration());
		order.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		dao.update(order);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, OfficeCubicleOrder.class, order.getId());

	}

	@Override
	public List<OfficeCubicleSpace> querySpacesByCityId(String ownerType,Long ownerId,Long cityId, CrossShardListingLocator locator, int pageSize,
			Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select(Tables.EH_OFFICE_CUBICLE_SPACES.fields()).from(Tables.EH_OFFICE_CUBICLE_RANGES,Tables.EH_OFFICE_CUBICLE_SPACES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_TYPE.eq(ownerType);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_ID.eq(ownerId));
		condition =  condition.and(Tables.EH_OFFICE_CUBICLE_RANGES.SPACE_ID.eq(Tables.EH_OFFICE_CUBICLE_SPACES.ID));

		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.NAMESPACE_ID.eq(currentNamespaceId));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.STATUS.eq(OfficeStatus.NORMAL.getCode()));
		if (null != cityId)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.CITY_ID.eq(cityId));
		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleSpace> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_SPACES.ID.desc()).fetch().map(new DefaultRecordMapper(Tables.EH_OFFICE_CUBICLE_SPACES.recordType(), OfficeCubicleSpace.class));
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<OfficeCubicleOrder> queryOrdersByUser(Long userId, Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ORDERS.NAMESPACE_ID.eq(currentNamespaceId);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVER_UID.eq(userId));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.STATUS.eq(OfficeOrderStatus.NORMAL.getCode()));
		step.where(condition);
		List<OfficeCubicleOrder> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleOrder.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<OfficeCubicleRefundRule> findRefundRule(Long spaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_REFUND_RULE);
		Condition condition = Tables.EH_OFFICE_CUBICLE_REFUND_RULE.SPACE_ID.eq(spaceId);
		step.where(condition);
		List<OfficeCubicleRefundRule> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_REFUND_RULE.CREATE_TIME.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleRefundRule.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	/**
	 * 金地同步数据使用
	 */
	@Override
	public List<OfficeCubicleOrder> listStationByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
			Long pageAnchor, int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS)
			.where(Tables.EH_OFFICE_CUBICLE_ORDERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_OFFICE_CUBICLE_ORDERS.UPDATE_TIME.eq(new Timestamp(timestamp)))
			.and(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.gt(pageAnchor))
			.orderBy(Tables.EH_OFFICE_CUBICLE_ORDERS.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, OfficeCubicleOrder.class));
		}
		return new ArrayList<OfficeCubicleOrder>();
	}

	/**
	 * 金地同步数据使用
	 */
	@Override
	public List<OfficeCubicleOrder> listStationByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS)
			.where(Tables.EH_OFFICE_CUBICLE_ORDERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_OFFICE_CUBICLE_ORDERS.UPDATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_OFFICE_CUBICLE_ORDERS.UPDATE_TIME.asc(), Tables.EH_OFFICE_CUBICLE_ORDERS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, OfficeCubicleOrder.class));
		}
		return new ArrayList<OfficeCubicleOrder>();
	}

	@Override
	public List<OfficeCubicleSpace> listEmptyOwnerSpace() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_SPACES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_SPACES.OWNER_TYPE.isNull();
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.OWNER_ID.isNull());

		step.where(condition);
		List<OfficeCubicleSpace> result = step.fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleSpace.class);
		});
		return result;
	}

	@Override
	public List<OfficeCubicleOrder> listEmptyOwnerOrders() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ORDERS.OWNER_TYPE.isNull();
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.OWNER_ID.isNull());
		step.where(condition);
		List<OfficeCubicleOrder> result = step.fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleOrder.class);
		});
		return result;
	}

	@Override
	public List<OfficeCubicleSpace> listAllSpaces(long pageAnchor, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select().from(Tables.EH_OFFICE_CUBICLE_SPACES)
				.where(Tables.EH_OFFICE_CUBICLE_SPACES.ID.gt(pageAnchor))
				.and(Tables.EH_OFFICE_CUBICLE_SPACES.STATUS.eq((byte)2))
				.limit(pageSize)
				.fetch().map(r->ConvertHelper.convert(r,OfficeCubicleSpace.class));

	}

	@Override
	public List<OfficeCubicleSpace> querySpacesByCityName(String ownerType, Long ownerId, String provinceName,
			String cityName, CrossShardListingLocator locator, int pageSize, Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select(Tables.EH_OFFICE_CUBICLE_SPACES.fields()).from(Tables.EH_OFFICE_CUBICLE_RANGES,Tables.EH_OFFICE_CUBICLE_SPACES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_TYPE.eq(ownerType);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_RANGES.OWNER_ID.eq(ownerId));
		condition =  condition.and(Tables.EH_OFFICE_CUBICLE_RANGES.SPACE_ID.eq(Tables.EH_OFFICE_CUBICLE_SPACES.ID));

		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.NAMESPACE_ID.eq(namespaceId));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.STATUS.eq(OfficeStatus.NORMAL.getCode()));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.CITY_NAME.eq(cityName));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.PROVINCE_NAME.eq(provinceName));
		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleSpace> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_SPACES.ID.desc()).fetch().map(new DefaultRecordMapper(Tables.EH_OFFICE_CUBICLE_SPACES.recordType(), OfficeCubicleSpace.class));
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public void updateSpaceByProvinceAndCity(Integer currentNamespaceId, String provinceName, String cityName) {
		dbProvider.getDslContext(AccessSpec.readWrite()).update(Tables.EH_OFFICE_CUBICLE_SPACES)
				.set(Tables.EH_OFFICE_CUBICLE_SPACES.STATUS,OfficeStatus.DELETED.getCode())
				.where(Tables.EH_OFFICE_CUBICLE_SPACES.NAMESPACE_ID.equal(currentNamespaceId))
				.and(Tables.EH_OFFICE_CUBICLE_SPACES.PROVINCE_NAME.equal(provinceName))
				.and(Tables.EH_OFFICE_CUBICLE_SPACES.CITY_NAME.equal(cityName))
				.execute();
	}

	@Override
	public void createConfig(OfficeCubicleConfig bean) {
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleConfigs.class));
		bean.setId(id);
		bean.setCreatorUid(UserContext.currentUserId());
		bean.setCreateTime(new Timestamp(System.currentTimeMillis()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleConfigsDao dao = new EhOfficeCubicleConfigsDao(context.configuration());
		dao.insert(bean);
	}

	@Override
	public void updateConfig(OfficeCubicleConfig bean) {
		bean.setUpdaterUid(UserContext.currentUserId());
		bean.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleConfigsDao dao = new EhOfficeCubicleConfigsDao(context.configuration());
		dao.update(bean);
	}

	@Override
	public OfficeCubicleConfig findConfigByOwnerId(String ownerType, Long ownerId) {
		assert(null != ownerId && StringUtils.isNotEmpty(ownerType));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<OfficeCubicleConfig> result = context.select().from(Tables.EH_OFFICE_CUBICLE_CONFIGS)
				.where(Tables.EH_OFFICE_CUBICLE_CONFIGS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_OFFICE_CUBICLE_CONFIGS.OWNER_TYPE.eq(ownerType)
				.and(Tables.EH_OFFICE_CUBICLE_CONFIGS.STATUS.eq((byte)2)))
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleConfig.class));
		if(null != result && result.size() > 0){
			return result.get(0);
		}
		return null;
	}


	

	
	@Override
	public void createCubicleSite(OfficeCubicleStation station) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Resources.class));
		station.setId(id);
		station.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		station.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleStationRecord record = ConvertHelper.convert(station,
				EhOfficeCubicleStationRecord.class);
		InsertQuery<EhOfficeCubicleStationRecord> query = context
				.insertQuery(Tables.EH_OFFICE_CUBICLE_STATION);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Resources.class, null);
	}

	@Override
	public void createCubicleRoom(OfficeCubicleRoom room) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Resources.class));
		room.setId(id);
		room.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		room.setCreatorUid(UserContext.currentUserId());
		room.setNamespaceId(UserContext.getCurrentNamespaceId());
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleRoomRecord record = ConvertHelper.convert(room,
				EhOfficeCubicleRoomRecord.class);
		InsertQuery<EhOfficeCubicleRoomRecord> query = context
				.insertQuery(Tables.EH_OFFICE_CUBICLE_ROOM);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleRoom.class, null);
	}
	
	@Override
	public void createCubicleRentOrder(OfficeCubicleRentOrder order) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Resources.class));
		order.setId(id);
		order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		order.setCreatorUid(UserContext.currentUserId());
		order.setNamespaceId(UserContext.getCurrentNamespaceId());
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleRentOrdersRecord record = ConvertHelper.convert(order,
				EhOfficeCubicleRentOrdersRecord.class);
		InsertQuery<EhOfficeCubicleRentOrdersRecord> query = context
				.insertQuery(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleRentOrders.class, null);
	}
	
	@Override
    public void updateCubicleRentOrder(OfficeCubicleRentOrder order) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhOfficeCubicleRentOrdersDao dao = new EhOfficeCubicleRentOrdersDao(context.configuration());
        
        dao.update(order);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleRentOrders.class, order.getId());

    }
	
	@Override
	public OfficeCubicleRentOrder findOfficeCubicleRentOrderByBizOrderNum(String bizOrderNum) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleRentOrdersRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS);
		query.addConditions(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.BIZ_ORDER_NO.eq(bizOrderNum));
		return ConvertHelper.convert(query.fetchAny(), OfficeCubicleRentOrder.class);
	}

	@Override
	public OfficeCubicleRentOrder findOfficeCubicleRentOrderById(Long orderId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleRentOrdersRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS);
		query.addConditions(Tables.EH_OFFICE_CUBICLE_RENT_ORDERS.ID.eq(orderId));
		return ConvertHelper.convert(query.fetchAny(), OfficeCubicleRentOrder.class);
	}
	

	@Override
	public List<OfficeCubicleChargeUser> findChargeUserBySpaceId(Long spaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<OfficeCubicleChargeUser> result = context.select().from(Tables.EH_OFFICE_CUBICLE_CHARGE_USERS)
				.where(Tables.EH_OFFICE_CUBICLE_CHARGE_USERS.SPACE_ID.eq(spaceId))
				.fetch().map(r -> ConvertHelper.convert(r, OfficeCubicleChargeUser.class));
		if(null != result && result.size() > 0){
			return result;
		}
		return null;
	}
	
	
	@Override
	public void createCubicleStationRent(OfficeCubicleStationRent stationRent) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Resources.class));
		stationRent.setId(id);
		stationRent.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		stationRent.setCreatorUid(UserContext.currentUserId());
		stationRent.setNamespaceId(UserContext.getCurrentNamespaceId());
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleStationRentRecord record = ConvertHelper.convert(stationRent,
				EhOfficeCubicleStationRentRecord.class);
		InsertQuery<EhOfficeCubicleStationRentRecord> query = context
				.insertQuery(Tables.EH_OFFICE_CUBICLE_STATION_RENT);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleStationRent.class, null);
	}
	
	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	@Override
	public List<OfficeCubicleStation> getOfficeCubicleStation(Long ownerId, String ownerType,Long spaceId, Long roomId, Byte rentFlag, String keyword,Byte status,Long stationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleStationRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_STATION);
		query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.OWNER_ID.eq(ownerId));
		query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.OWNER_TYPE.eq(ownerType));
		if (spaceId != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.SPACE_ID.eq(spaceId));
		if (roomId != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.ASSOCIATE_ROOM_ID.eq(roomId));
		if (rentFlag != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.RENT_FLAG.eq(rentFlag));
		if (keyword != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.STATION_NAME.like("%" + keyword +"%"));
		if (status != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.STATUS.eq(status));
		if (stationId != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.ID.eq(stationId));
		return query.fetch().map(r->ConvertHelper.convert(r, OfficeCubicleStation.class));
	}
	
	@Override
	public OfficeCubicleStation getOfficeCubicleStationById(Long stationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleStationRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_STATION);
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION.ID.eq(stationId));
		return ConvertHelper.convert(query.fetchAny(), OfficeCubicleStation.class);
	}
	
	@Override
	public OfficeCubicleRoom getOfficeCubicleRoomyId(Long roomId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleRoomRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_ROOM);
			query.addConditions(Tables.EH_OFFICE_CUBICLE_ROOM.ID.eq(roomId));
		return ConvertHelper.convert(query.fetchAny(), OfficeCubicleRoom.class);
	}
	
	
	@Override
	public List<OfficeCubicleStationRent> getOfficeCubicleStationRent(Long spaceId, Byte rentType,Byte stationType,Long stationId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleStationRentRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_STATION_RENT);
		query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION_RENT.SPACE_ID.eq(spaceId));
		if (rentType!=null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION_RENT.RENT_TYPE.eq(rentType));
		if (stationType != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION_RENT.STATION_TYPE.eq(stationType));
		if (stationId != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_STATION_RENT.STATION_ID.eq(stationId));
		return query.fetch().map(r->ConvertHelper.convert(r, OfficeCubicleStationRent.class));
	}
	
	@Override
	public List<OfficeCubicleRoom> getOfficeCubicleRoom(Long ownerid, String ownerType,Long spaceId,Byte rentFlag, Byte status, Long roomId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhOfficeCubicleRoomRecord> query = context.selectQuery(Tables.EH_OFFICE_CUBICLE_ROOM);
		query.addConditions(Tables.EH_OFFICE_CUBICLE_ROOM.SPACE_ID.eq(spaceId));
		if (rentFlag != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_ROOM.RENT_FLAG.eq(rentFlag));
		if (status != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_ROOM.STATUS.eq(status));
		if (roomId != null)
			query.addConditions(Tables.EH_OFFICE_CUBICLE_ROOM.ID.eq(roomId));
		return query.fetch().map(r->ConvertHelper.convert(r, OfficeCubicleRoom.class));
	}
	
    @Override
    public void deleteRoom(OfficeCubicleRoom room){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOfficeCubicleRoomDao dao = new EhOfficeCubicleRoomDao(context.configuration());
    	dao.delete(room);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleRoom.class, room.getId());
    }
    
    @Override
    public void deleteStation(OfficeCubicleStation station){
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhOfficeCubicleStationDao dao = new EhOfficeCubicleStationDao(context.configuration());
    	dao.delete(station);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleStation.class, station.getId());
    }
}
