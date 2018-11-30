// @formatter:off
package com.everhomes.enterprisemoment;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentMessagesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentMessages;
import com.everhomes.server.schema.tables.records.EhEnterpriseMomentMessagesRecord;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnterpriseMomentMessageProviderImpl implements EnterpriseMomentMessageProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createEnterpriseMomentMessage(EnterpriseMomentMessage enterpriseMomentMessage) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentMessages.class));
		enterpriseMomentMessage.setId(id);
		if (enterpriseMomentMessage.getOperateTime() == null) {
			enterpriseMomentMessage.setOperateTime(new Timestamp(System.currentTimeMillis()));
		}
		getReadWriteDao().insert(enterpriseMomentMessage);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseMomentMessages.class, null);
	}

	@Override
	public List<Long> findMessageReceiverListBySourceId(Integer namespaceId, Long organizationId, Long momentId, String sourceType, Long operatorId, Collection<Long> receiverIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Long>> query = context.selectDistinct(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.RECEIVER_UID).from(Tables.EH_ENTERPRISE_MOMENT_MESSAGES);
		Condition condition = Tables.EH_ENTERPRISE_MOMENT_MESSAGES.NAMESPACE_ID.eq(namespaceId);
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ORGANIZATION_ID.eq(organizationId));
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ENTERPRISE_MOMENT_ID.eq(momentId));
		if (receiverIds != null) {
			condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.RECEIVER_UID.in(receiverIds));
		}
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.SOURCE_TYPE.eq(sourceType));
		condition = condition.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.OPERATOR_UID.eq(operatorId));
		query.where(condition);

		Result<Record1<Long>> result = query.fetch();
		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.stream().map(r -> {
			return r.value1();
		}).collect(Collectors.toList());
	}

	@Override
	public List<EnterpriseMomentMessage> listEnterpriseMomentMessage(Integer currentNamespaceId, Long organizationId, Long userId, CrossShardListingLocator locator, int pageSize) {
		Result<Record> records = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_MESSAGES)
				.where(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.NAMESPACE_ID.eq(currentNamespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.RECEIVER_UID.eq(userId))
				.and(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ID.lessOrEqual(locator.getAnchor()))
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ID.desc())
				.limit(pageSize)
				.fetch();
		if (CollectionUtils.isEmpty(records)) {
			return new ArrayList<>();
		}
		return records.map(r -> ConvertHelper.convert(r, EnterpriseMomentMessage.class));
	}

	@Override
	public void markSourceDeleteBySourceId(Integer namespaceId, Long organizationId, Long momentId, String sourceType, Long sourceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterpriseMomentMessages.class));
		UpdateQuery<EhEnterpriseMomentMessagesRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_MOMENT_MESSAGES);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.ENTERPRISE_MOMENT_ID.eq(momentId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.SOURCE_TYPE.eq(sourceType));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.SOURCE_ID.eq(sourceId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_MOMENT_MESSAGES.SOURCE_DELETE_FLAG, TrueOrFalseFlag.TRUE.getCode());
		updateQuery.execute();
	}

	private EhEnterpriseMomentMessagesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentMessagesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentMessagesDao getDao(DSLContext context) {
		return new EhEnterpriseMomentMessagesDao(context.configuration());
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
