// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentFavouritesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentFavourites;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentFavouritesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterpriseMomentFavouriteProviderImpl implements EnterpriseMomentFavouriteProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public int createEnterpriseMomentFavourite(EnterpriseMomentFavourite enterpriseMomentFavourite) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentFavourites.class));
		enterpriseMomentFavourite.setId(id);
		enterpriseMomentFavourite.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnterpriseMomentFavouritesRecord record = ConvertHelper.convert(enterpriseMomentFavourite, EhEnterpriseMomentFavouritesRecord.class);
		InsertQuery<EhEnterpriseMomentFavouritesRecord> insertQuery = context.insertQuery(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES);
		insertQuery.addRecord(record);
		insertQuery.onDuplicateKeyIgnore(true);
		return insertQuery.execute();
	}

	@Override
	public void deleteEnterpriseMomentFavourite(EnterpriseMomentFavourite enterpriseMomentFavourite) {
		getReadWriteDao().delete(enterpriseMomentFavourite);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMomentFavourites.class, enterpriseMomentFavourite.getId());
	}

	@Override
	public EnterpriseMomentFavourite findEnterpriseMomentFavouriteById(Integer namespaceId, Long organizationId, Long userId, Long momentId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseMomentFavouritesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES);
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.USER_ID.eq(userId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID.eq(momentId));
		query.addLimit(1);

		EhEnterpriseMomentFavouritesRecord record = query.fetchOne();
		return ConvertHelper.convert(record, EnterpriseMomentFavourite.class);
	}

	@Override
	public List<EnterpriseMomentFavourite> listEnterpriseMomentFavourite(Long enterpriseMomentId, Integer namespaceId, Long organizationId, Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES)
				.where(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID.eq(enterpriseMomentId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ID.le(pageAnchor))
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ID.desc()).limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, EnterpriseMomentFavourite.class));
	}

	@Override
	public List<EnterpriseMomentFavourite> listEnterpriseMomentFavourite(Integer namespaceId, Long organizationId, Long momentId, Integer pageSize) {
		Result<Record> records = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES)
				.where(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID.eq(momentId))
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ID.asc())
				.fetch();
		if (CollectionUtils.isEmpty(records)) {
			return new ArrayList<>();
		}
		return records.map(r -> ConvertHelper.convert(r, EnterpriseMomentFavourite.class));
	}

	@Override
	public Integer countFavourites(Integer namespaceId, Long organizationId, Long momentId) {
		return getReadOnlyContext().selectDistinct(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.DETAIL_ID)
				.from(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES)
				.where(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID.eq(momentId))
				.fetchCount();
	}
	
	@Override
	public Integer countFavourites(Integer namespaceId, Long organizationId, Long momentId, Long userId) {
		return getReadOnlyContext().selectDistinct(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.DETAIL_ID)
				.from(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES)
				.where(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID.eq(momentId))
				.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.USER_ID.eq(userId))
				.fetchCount();
	}

	@Override
	public List<Long> findFavoiriteUserIds(Integer namespaceId, Long organizationId, Long momentId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Long>> query = context.selectDistinct(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.CREATOR_UID).from(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES);
		Condition condition = Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ORGANIZATION_ID.eq(organizationId));
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID.eq(momentId));
		query.where(condition);
		Result<Record1<Long>> result = query.fetch();
		if (result == null || result.size() == 0) {
			return new ArrayList<>(0);
		}
		return result.map(r -> {
			return r.value1();
		});
	}

	private EhEnterpriseMomentFavouritesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentFavouritesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentFavouritesDao getDao(DSLContext context) {
		return new EhEnterpriseMomentFavouritesDao(context.configuration());
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


}
