// @formatter:off
package com.everhomes.enterprisemoment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseMomentScopesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseMomentScopes;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class EnterpriseMomentScopeProviderImpl implements EnterpriseMomentScopeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createEnterpriseMomentScope(EnterpriseMomentScope enterpriseMomentScope) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseMomentScopes.class));
		enterpriseMomentScope.setId(id);
		enterpriseMomentScope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().insert(enterpriseMomentScope);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseMomentScopes.class, null);
	}

	@Override
	public void updateEnterpriseMomentScope(EnterpriseMomentScope enterpriseMomentScope) {
		assert (enterpriseMomentScope.getId() != null);
		getReadWriteDao().update(enterpriseMomentScope);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseMomentScopes.class, enterpriseMomentScope.getId());
	}

	@Override
	public EnterpriseMomentScope findEnterpriseMomentScopeById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), EnterpriseMomentScope.class);
	}
	
	@Override
	public List<EnterpriseMomentScope> listEnterpriseMomentScope() {
		return getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_SCOPES)
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_SCOPES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, EnterpriseMomentScope.class));
	}

	@Override
	public List<EnterpriseMomentScope> listEnterpriseMomentScopeByMomentId(Integer namespaceId, Long organizationId, Long momentId) {
		Result<Record> records = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_MOMENT_SCOPES)
				.where(Tables.EH_ENTERPRISE_MOMENT_SCOPES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_MOMENT_SCOPES.ENTERPRISE_MOMENT_ID.eq(momentId))
				.orderBy(Tables.EH_ENTERPRISE_MOMENT_SCOPES.ID.asc())
				.fetch();
		if (CollectionUtils.isEmpty(records)) {
			return new ArrayList<>();
		}
		return records.map(r -> ConvertHelper.convert(r, EnterpriseMomentScope.class));
	}

	private EhEnterpriseMomentScopesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterpriseMomentScopesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterpriseMomentScopesDao getDao(DSLContext context) {
		return new EhEnterpriseMomentScopesDao(context.configuration());
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
