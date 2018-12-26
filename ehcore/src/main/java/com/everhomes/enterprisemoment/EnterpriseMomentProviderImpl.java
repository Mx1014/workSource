// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.enterprisemoment.ScopeType;
import com.everhomes.rest.enterprisemoment.SelectorType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMoments;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class EnterpriseMomentProviderImpl implements EnterpriseMomentProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createEnterpriseMoment(EnterpriseMoment enterpriseMoment) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMoments.class));
		enterpriseMoment.setId(id);
		enterpriseMoment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		enterpriseMoment.setDeleteFlag(TrueOrFalseFlag.FALSE.getCode());
		getReadWriteDao().insert(enterpriseMoment);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseMoments.class, null);
	}

	@Override
	public void updateEnterpriseMoment(EnterpriseMoment enterpriseMoment) {
		getReadWriteDao().update(enterpriseMoment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMoments.class, enterpriseMoment.getId());
	}

	@Override
	public EnterpriseMoment findEnterpriseMomentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), EnterpriseMoment.class);
	}
	
	@Override
	public List<EnterpriseMoment> listEnterpriseMoments(Integer namespaceId, Long organizationId, Long userId, Long tagId, CrossShardListingLocator locator, int pageSize) {

		return listEnterpriseMoments(namespaceId, organizationId, userId, tagId, null, locator, pageSize);
	}

	public List<EnterpriseMoment> listEnterpriseMoments(Integer namespaceId, Long organizationId, Long userId, Long tagId, Condition condition, CrossShardListingLocator locator, int pageSize) {

		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENTS)
				.where(Tables.EH_ENTERPRISE_MOMENTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENTS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_MOMENTS.DELETE_FLAG.ne(TrueOrFalseFlag.TRUE.getCode()));
		if (condition != null) {
			step.and(condition);
		}

		switch (SelectorType.fromCode(tagId)) {
			case ALL:
				//全部不再加限制条件
				break;
			case PUBLISH_BY_SELF:
				//我发布的
				step.and(Tables.EH_ENTERPRISE_MOMENTS.CREATOR_UID.eq(userId));
				break;
			case COMMENT_BY_SELF:
				//我评论的
				step.and(DSL.exists(DSL.selectOne().from(Tables.EH_ENTERPRISE_MOMENT_COMMENTS)
						.where(Tables.EH_ENTERPRISE_MOMENTS.ID.eq(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ENTERPRISE_MOMENT_ID))
						.and(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.CREATOR_UID.eq(userId))));
				break;
			case FAVOURITE_BY_SELF:
				//我点赞的
				step.and(DSL.exists(DSL.selectOne().from(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES)
						.where(Tables.EH_ENTERPRISE_MOMENTS.ID.eq(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.ENTERPRISE_MOMENT_ID))
						.and(Tables.EH_ENTERPRISE_MOMENT_FAVOURITES.CREATOR_UID.eq(userId))));
				break;
			case CUSTOM_TAG:
			default:
				step.and(Tables.EH_ENTERPRISE_MOMENTS.TAG_ID.eq(tagId));
				break;
		}
		step.and(Tables.EH_ENTERPRISE_MOMENTS.ID.lessOrEqual(locator.getAnchor()))
				.orderBy(Tables.EH_ENTERPRISE_MOMENTS.ID.desc()).limit(pageSize);
		Result<Record> records = step.fetch();
		if (CollectionUtils.isEmpty(records)) {
			return new ArrayList<>();
		}
		return records.map(r -> ConvertHelper.convert(r, EnterpriseMoment.class));
	}

	@Override
	public List<EnterpriseMoment> listEnterpriseMoments(Integer namespaceId, Long organizationId, Long userId, Set<Long> orgIds, Long tagId, CrossShardListingLocator locator, int pageSize) {
		//当前版本只有部门
		Condition condition2 = DSL.exists(DSL.selectOne().from(Tables.EH_ENTERPRISE_MOMENT_SCOPES)
				.where(Tables.EH_ENTERPRISE_MOMENTS.ID.eq(Tables.EH_ENTERPRISE_MOMENT_SCOPES.ENTERPRISE_MOMENT_ID))
				.and(Tables.EH_ENTERPRISE_MOMENT_SCOPES.SOURCE_TYPE.eq(ScopeType.ORGANIZATION.getCode()))
				.and(Tables.EH_ENTERPRISE_MOMENT_SCOPES.SOURCE_ID.in(orgIds)));
		Condition condition = Tables.EH_ENTERPRISE_MOMENTS.CREATOR_UID.eq(userId).or(condition2);
		return listEnterpriseMoments(namespaceId, organizationId, userId, tagId, condition, locator, pageSize);

	}

	@Override
	public void deleteEnterpriseMoment(Long momentId) {
		EhEnterpriseMoments moment = getReadOnlyDao().findById(momentId);
		moment.setDeleteFlag((byte) 1);
		moment.setDeleteUid(UserContext.currentUserId());
		moment.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(moment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMoments.class, moment.getId());
	}

	@Override
	public void incrCommentCount(Long id, Integer namespaceId, Long organizationId, int incr) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterpriseMomentsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_MOMENTS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENTS.ID.eq(id));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENTS.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENTS.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_MOMENTS.COMMENT_COUNT, Tables.EH_ENTERPRISE_MOMENTS.COMMENT_COUNT.add(incr));
		updateQuery.execute();
	}

	@Override
	public void incrLikeCount(Long id, Integer namespaceId, Long organizationId, int incr) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterpriseMomentsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_MOMENTS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENTS.ID.eq(id));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENTS.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENTS.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_MOMENTS.LIKE_COUNT, Tables.EH_ENTERPRISE_MOMENTS.LIKE_COUNT.add(incr));
		updateQuery.execute();
	}

	@Override
	public void updateEnterpriseMomentTagName(Integer namespaceId, Long organizationId, Long tagId, String tagName) {
		com.everhomes.server.schema.tables.EhEnterpriseMoments table = Tables.EH_ENTERPRISE_MOMENTS;
		UpdateQuery<EhEnterpriseMomentsRecord> updateQuery = getReadWriteContext().updateQuery(table);
		updateQuery.addValue(table.TAG_NAME, tagName);
		updateQuery.addConditions(table.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(table.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(table.TAG_ID.eq(tagId));
		updateQuery.addConditions(table.DELETE_FLAG.eq((byte)0));
		updateQuery.execute();
	}

	@Override
	public Integer countAllStatusEnterpriseMomentByOrganization(Integer namespaceId, Long organizationId) {
		return getReadOnlyContext().selectOne().from(Tables.EH_ENTERPRISE_MOMENTS)
				.where(Tables.EH_ENTERPRISE_MOMENTS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENTS.ORGANIZATION_ID.eq(organizationId)).fetchCount();
	}
	
	private EhEnterpriseMomentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentsDao getDao(DSLContext context) {
		return new EhEnterpriseMomentsDao(context.configuration());
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
