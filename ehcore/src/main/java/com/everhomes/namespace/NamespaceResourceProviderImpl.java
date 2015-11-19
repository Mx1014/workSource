// @formatter:off
package com.everhomes.namespace;

import java.util.List;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.schema.tables.daos.EhNamespacesDao;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;

/**
 * NamespaceResource management implementation
 *
 */
@Component
public class NamespaceResourceProviderImpl implements NamespaceResourceProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceResourceProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

    @Override
    public NamespaceResource findNamespaceResourceById(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhNamespacesDao dao = new EhNamespacesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(namespaceId), NamespaceResource.class);
    }

    @Cacheable(value = "listResourceByNamespace", key="#namespaceId", unless="#result.size() == 0")
    @Override
    public List<NamespaceResource> listResourceByNamespace(Integer namespaceId, NamespaceResourceType type) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        context.select().from(Tables.EH_NAMESPACE_RESOURCES);
        List<NamespaceResource> list = context.select().from(Tables.EH_NAMESPACE_RESOURCES)
            .where(Tables.EH_NAMESPACE_RESOURCES.NAMESPACE_ID.eq(namespaceId))
            .and(Tables.EH_NAMESPACE_RESOURCES.RESOURCE_TYPE.eq(type.getCode()))
            .fetch().map((r) -> {
                return ConvertHelper.convert(r, NamespaceResource.class);
            });
        
        return list;
    }
}
