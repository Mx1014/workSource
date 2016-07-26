package com.everhomes.officecubicle;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
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
import com.everhomes.rest.officecubicle.OfficeOrderStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhOfficeCubicleCategories;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleOrdersDao;
import com.everhomes.server.schema.tables.daos.EhOfficeCubicleSpacesDao;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleOrders;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleSpaces;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleOrdersRecord;
import com.everhomes.server.schema.tables.records.EhOfficeCubicleSpacesRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class OfficeCubicleProviderImpl implements OfficeCubicleProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<OfficeCubicleCategory> queryCategoriesBySpaceId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_CATEGORIES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_CATEGORIES.SPACE_ID.equal(id);
		step.where(condition);
		List<OfficeCubicleCategory> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_CATEGORIES.ID.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleCategory.class);
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
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleCategoriesRecord record = ConvertHelper.convert(category, EhOfficeCubicleCategoriesRecord.class);
		InsertQuery<EhOfficeCubicleCategoriesRecord> query = context.insertQuery(Tables.EH_OFFICE_CUBICLE_CATEGORIES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhOfficeCubicleCategories.class, null);
	}

	@Override
	public OfficeCubicleSpace getSpaceById(Long id) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleSpacesDao dao = new EhOfficeCubicleSpacesDao(context.configuration());
		EhOfficeCubicleSpaces space = dao.findById(id);
		return ConvertHelper.convert(space, OfficeCubicleSpace.class);
	}

	@Override
	public void updateSpace(OfficeCubicleSpace space) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhOfficeCubicleSpacesDao dao = new EhOfficeCubicleSpacesDao(context.configuration());
		dao.update(space);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhOfficeCubicleSpaces.class, space.getId());

	}

	@Override
	public void deleteCategoriesBySpaceId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhOfficeCubicleCategoriesRecord> step = context.delete(Tables.EH_OFFICE_CUBICLE_CATEGORIES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_CATEGORIES.SPACE_ID.equal(id);
		step.where(condition);
		step.execute();
	}

	@Override
	public List<OfficeCubicleOrder> searchOrders(Long beginDate, Long endDate, String reserveKeyword, String spaceName,
			CrossShardListingLocator locator, Integer pageSize, Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ORDERS.NAMESPACE_ID.eq(currentNamespaceId);
		if (null != beginDate)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.gt(new Timestamp(beginDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.gt(new Timestamp(endDate)));
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
	public List<OfficeCubicleSpace> searchSpaces(String keyWords, CrossShardListingLocator locator, int pageSize,
			Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_SPACES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_SPACES.NAMESPACE_ID.eq(currentNamespaceId);
		if (StringUtils.isNotBlank(keyWords))
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.NAME.like("%" + keyWords + "%").or(
					Tables.EH_OFFICE_CUBICLE_ORDERS.CONTACT_PHONE.like("%" + keyWords + "%")));

		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleSpace> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_SPACES.ID.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleSpace.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public void createOrder(OfficeCubicleOrder order) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhOfficeCubicleOrders.class));
		order.setId(id);
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
		dao.update(order);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, OfficeCubicleOrder.class, order.getId());

	}

	@Override
	public List<OfficeCubicleSpace> querySpacesByCityId(Long cityId, CrossShardListingLocator locator, int pageSize,
			Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_SPACES);
		Condition condition = Tables.EH_OFFICE_CUBICLE_SPACES.NAMESPACE_ID.eq(currentNamespaceId);
		if (null != cityId)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.CITY_ID.lt(cityId));
		if (null != locator && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_OFFICE_CUBICLE_SPACES.ID.lt(locator.getAnchor()));
		step.limit(pageSize);
		step.where(condition);
		List<OfficeCubicleSpace> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_SPACES.ID.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleSpace.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<OfficeCubicleOrder> queryOrdersByUser(Long userId, Integer currentNamespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_OFFICE_CUBICLE_ORDERS);
		Condition condition = Tables.EH_OFFICE_CUBICLE_ORDERS.NAMESPACE_ID.eq(currentNamespaceId);
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVER_UID.lt(userId));
		condition = condition.and(Tables.EH_OFFICE_CUBICLE_ORDERS.STATUS.lt(OfficeOrderStatus.NORMAL.getCode()));
		step.where(condition);
		List<OfficeCubicleOrder> result = step.orderBy(Tables.EH_OFFICE_CUBICLE_ORDERS.RESERVE_TIME.desc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, OfficeCubicleOrder.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

}
