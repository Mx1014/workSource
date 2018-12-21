// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentCommentsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentComments;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentCommentsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
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
public class EnterpriseMomentCommentProviderImpl implements EnterpriseMomentCommentProvider {
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private SequenceProvider sequenceProvider;

    @Override
    public void createEnterpriseMomentComment(EnterpriseMomentComment enterpriseMomentComment) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentComments.class));
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        enterpriseMomentComment.setId(id);
        enterpriseMomentComment.setCreateTime(now);
        enterpriseMomentComment.setOperateTime(now);
        getReadWriteDao().insert(enterpriseMomentComment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseMomentComments.class, null);
    }

	@Override
	public void deleteEnterpriseMomentComment(EnterpriseMomentComment enterpriseMomentComment) {
		assert (enterpriseMomentComment.getId() != null);
		getReadWriteDao().delete(enterpriseMomentComment);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMomentComments.class, enterpriseMomentComment.getId());
	}

	@Override
	public EnterpriseMomentComment findEnterpriseMomentCommentById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), EnterpriseMomentComment.class);
	}

	@Override
	public List<EnterpriseMomentComment> listEnterpriseMomentCommentsDesc(Integer namespaceId, Long organizationId, Long momentId, Long pageAnchor, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseMomentCommentsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_MOMENT_COMMENTS);
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ENTERPRISE_MOMENT_ID.eq(momentId));

		if (pageAnchor != null) {
			query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ID.le(pageAnchor));
		}
		query.addOrderBy(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ID.desc());
		query.addLimit(pageSize);

		Result<EhEnterpriseMomentCommentsRecord> record = query.fetch();
		if (record == null || record.size() == 0) {
			return new ArrayList<>();
		}
		return record.map(r -> ConvertHelper.convert(r, EnterpriseMomentComment.class));
	}

	@Override
	public List<EnterpriseMomentComment> listEnterpriseMomentCommentsAsc(Integer namespaceId, Long organizationId, Long momentId, Long pageAnchor, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseMomentCommentsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_MOMENT_COMMENTS);
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ENTERPRISE_MOMENT_ID.eq(momentId));

		if (pageAnchor != null) {
			query.addConditions(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ID.ge(pageAnchor));
		}
		query.addOrderBy(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ID.asc());
		query.addLimit(pageSize);

		Result<EhEnterpriseMomentCommentsRecord> record = query.fetch();
		if (record == null || record.size() == 0) {
			return new ArrayList<>();
		}
		return record.map(r -> ConvertHelper.convert(r, EnterpriseMomentComment.class));
	}

	@Override
	public int countEnterpriseMomentComments(Integer namespaceId, Long organizationId, Long momentId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Integer>> query = context.select(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ID.count()).from(Tables.EH_ENTERPRISE_MOMENT_COMMENTS);
		Condition condition = Tables.EH_ENTERPRISE_MOMENT_COMMENTS.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ORGANIZATION_ID.eq(organizationId));
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ENTERPRISE_MOMENT_ID.eq(momentId));
		query.where(condition);
		Record1<Integer> result = query.fetchOne();
		if (result == null) {
			return 0;
		}
		return result.value1();
	}

	@Override
	public List<Long> findCommentUserIds(Integer namespaceId, Long organizationId, Long momentId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectJoinStep<Record1<Long>> query = context.selectDistinct(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.CREATOR_UID).from(Tables.EH_ENTERPRISE_MOMENT_COMMENTS);
		Condition condition = Tables.EH_ENTERPRISE_MOMENT_COMMENTS.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ORGANIZATION_ID.eq(organizationId));
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_COMMENTS.ENTERPRISE_MOMENT_ID.eq(momentId));
		query.where(condition);
		Result<Record1<Long>> result = query.fetch();
		if (result == null || result.size() == 0) {
			return new ArrayList<>(0);
		}
		return result.map(r -> {
			return r.value1();
		});
	}

	private EhEnterpriseMomentCommentsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentCommentsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentCommentsDao getDao(DSLContext context) {
		return new EhEnterpriseMomentCommentsDao(context.configuration());
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
