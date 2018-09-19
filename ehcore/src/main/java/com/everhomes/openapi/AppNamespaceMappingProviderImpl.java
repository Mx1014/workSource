// @formatter:off
package com.everhomes.openapi;

import com.everhomes.app.App;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.tables.EhApps;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAppNamespaceMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhAppNamespaceMappings;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
	public List<AppNamespaceMapping> listAppNamespaceMapping(Integer namespaceId, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhAppNamespaceMappings t = Tables.EH_APP_NAMESPACE_MAPPINGS;
        SelectQuery<Record> query = getReadOnlyContext().select().from(t).getQuery();

        if (namespaceId != null) {
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        }

        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }

        if (pageSize > 0) {
            query.addLimit(pageSize + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<AppNamespaceMapping> list = query.fetchInto(AppNamespaceMapping.class);
        if (list.size() > pageSize && pageSize > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
	}
	
	@Override
	public AppNamespaceMapping findAppNamespaceMappingByAppKey(String appKey) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APP_NAMESPACE_MAPPINGS)
			.where(Tables.EH_APP_NAMESPACE_MAPPINGS.APP_KEY.eq(appKey))
			.fetchAny();
		if (record != null) {
			return ConvertHelper.convert(record, AppNamespaceMapping.class);
		}
		return null;
	}

    @Override
    public List<App> listAppsByAppKey(List<String> appKeys) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhApps t = com.everhomes.schema.Tables.EH_APPS;
        return context.selectFrom(t)
                .where(t.APP_KEY.in(appKeys))
                .fetchInto(App.class);
    }

	@Override
	public AppNamespaceMapping findAppNamespaceMappingByNamespaceId(Integer namespaceId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_APP_NAMESPACE_MAPPINGS)
				.where(Tables.EH_APP_NAMESPACE_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
				.fetchAny();
		if (record != null) {
			return ConvertHelper.convert(record, AppNamespaceMapping.class);
		}
		return null;
	}

	public List<App> listAppsByExcludeAppKey(List<String> appKeys) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhApps t = com.everhomes.schema.Tables.EH_APPS;
		return context.selectFrom(t)
				.where(t.APP_KEY.notIn(appKeys))
				.fetchInto(App.class);
	}

	@Override
    public void deleteNamespaceMapping(AppNamespaceMapping mapping) {
        getReadWriteDao().delete(mapping);
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
