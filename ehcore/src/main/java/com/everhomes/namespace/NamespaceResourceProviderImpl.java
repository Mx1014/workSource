// @formatter:off
package com.everhomes.namespace;

import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.namespace.NamespaceResourceType;
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

    @Cacheable(value = "listResourceByNamespace", key="{#namespaceId, #type}", unless="#result.size() == 0")
    @Override
    public List<NamespaceResource> listResourceByNamespace(Integer namespaceId, NamespaceResourceType type) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<NamespaceResource> list = context.select().from(Tables.EH_NAMESPACE_RESOURCES)
            .where(Tables.EH_NAMESPACE_RESOURCES.NAMESPACE_ID.eq(namespaceId))
            .and(Tables.EH_NAMESPACE_RESOURCES.RESOURCE_TYPE.eq(type.getCode()))
            .fetch().map((r) -> {
                return ConvertHelper.convert(r, NamespaceResource.class);
            });
        
        return list;
    }
    
    @Cacheable(value = "listResourceByNamespaceLocator", key="{#namespaceId, #type, #count}", unless="#result.size() == 0")
    @Override
    public List<NamespaceResource> listResourceByNamespace(
    		ListingLocator locator, int count, Integer namespaceId,
    		NamespaceResourceType type) {
    	 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
    	 Condition cond = Tables.EH_NAMESPACE_RESOURCES.NAMESPACE_ID.eq(namespaceId);
    	 cond = cond.and(Tables.EH_NAMESPACE_RESOURCES.RESOURCE_TYPE.eq(type.getCode()));
    	 if(null != locator.getAnchor()){
    		 cond = cond.and(Tables.EH_NAMESPACE_RESOURCES.ID.gt(locator.getAnchor()));
    	 }
         List<NamespaceResource> list = context.select().from(Tables.EH_NAMESPACE_RESOURCES)
             .where(cond)
             .orderBy(Tables.EH_NAMESPACE_RESOURCES.ID.asc())
             .limit(count)
             .fetch().map((r) -> {
                 return ConvertHelper.convert(r, NamespaceResource.class);
             });
    	return null;
    }
    
    @Cacheable(value = "findNamespaceDetailByNamespaceId", key="#namespaceId", unless="#result == null")
    @Override
    public NamespaceDetail findNamespaceDetailByNamespaceId(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<NamespaceDetail> list = context.select().from(Tables.EH_NAMESPACE_DETAILS)
            .where(Tables.EH_NAMESPACE_DETAILS.NAMESPACE_ID.eq(namespaceId))
            .fetch().map((r) -> {
                return ConvertHelper.convert(r, NamespaceDetail.class);
            });
        
        // eh_namespace_details表是eh_namespaces表的扩展属性表，每个namespace_id只会有一行记录
        if(list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
