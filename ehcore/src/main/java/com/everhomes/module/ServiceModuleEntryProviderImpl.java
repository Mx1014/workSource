// @formatter:off
package com.everhomes.module;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhReflectionServiceModuleAppsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentRelationsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModulesDao;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.everhomes.server.schema.tables.EhReflectionServiceModuleApps.EH_REFLECTION_SERVICE_MODULE_APPS;

@Component
public class ServiceModuleEntryProviderImpl implements ServiceModuleEntryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleEntryProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Override
    @Cacheable(value = "listServiceModuleEntries", key = "{#moduleId,#entryType}", unless = "#result.size() == 0")
    public List<ServiceModuleEntry> listServiceModuleEntries(Long moduleId, Byte entryType) {
        List<ServiceModuleEntry> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleEntriesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_ENTRIES);
        if(moduleId != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID.eq(moduleId));
        }
        if(entryType != null){
            query.addConditions(Tables.EH_SERVICE_MODULE_ENTRIES.ENTRY_TYPE.eq(entryType));
        }
        query.addOrderBy(Tables.EH_SERVICE_MODULE_ENTRIES.MODULE_ID);
        query.addOrderBy(Tables.EH_SERVICE_MODULE_ENTRIES.ENTRY_TYPE);

        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, ServiceModuleEntry.class));
            return null;
        });
        return results;
    }
}
