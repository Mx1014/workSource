// @formatter:off
package com.everhomes.openapi;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAppNamespaceMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhAppNamespaceMappings;
import com.everhomes.util.ConvertHelper;

@Component
public class AppNamespaceMappingProviderImpl implements AppNamespaceMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAppNamespaceMapping(AppNamespaceMapping appNamespaceMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAppNamespaceMappings.class));
		appNamespaceMapping.setId(id);
		getReadWriteDao().insert(appNamespaceMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAppNamespaceMappings.class, null);
	}

	@Override
	public void updateAppNamespaceMapping(AppNamespaceMapping appNamespaceMapping) {
		assert (appNamespaceMapping.getId() != null);
		getReadWriteDao().update(appNamespaceMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAppNamespaceMappings.class, appNamespaceMapping.getId());
	}

	@Override
	public AppNamespaceMapping findAppNamespaceMappingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AppNamespaceMapping.class);
	}
	
	@Override
	public List<AppNamespaceMapping> listAppNamespaceMapping() {
		return getReadOnlyContext().select().from(Tables.EH_APP_NAMESPACE_MAPPINGS)
				.orderBy(Tables.EH_APP_NAMESPACE_MAPPINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AppNamespaceMapping.class));
	}
	
	@Override
	public AppNamespaceMapping findAppNamespaceMappingByAppKey(String appKey) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APP_NAMESPACE_MAPPINGS)
			.where(Tables.EH_APP_NAMESPACE_MAPPINGS.APP_KEY.eq(appKey))
			.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, AppNamespaceMapping.class);
		}
		return null;
	}

	private EhAppNamespaceMappingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAppNamespaceMappingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAppNamespaceMappingsDao getDao(DSLContext context) {
		return new EhAppNamespaceMappingsDao(context.configuration());
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
