// @formatter:off
package com.everhomes.docking;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DockingMappingProviderImpl implements DockingMappingProvider {
    @Autowired 
    private SequenceProvider sequenceProvider;
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public DockingMapping findDockingMappingByScopeAndName(Integer namespaceId, String scope, String name) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDockingMappings.class));

        SelectQuery<EhDockingMappingsRecord> query = context.selectQuery(Tables.EH_DOCKING_MAPPINGS);
        query.addConditions(Tables.EH_DOCKING_MAPPINGS.SCOPE.eq(scope));
        query.addConditions(Tables.EH_DOCKING_MAPPINGS.NAME.eq(name));

        return ConvertHelper.convert(query.fetchAny(), DockingMapping.class);
    }

    @Override
    public DockingMapping findDockingMappingByScopeAndMappingValue(Integer namespaceId, String scope, String mappingValue) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDockingMappings.class));

        SelectQuery<EhDockingMappingsRecord> query = context.selectQuery(Tables.EH_DOCKING_MAPPINGS);
        query.addConditions(Tables.EH_DOCKING_MAPPINGS.SCOPE.eq(scope));
        query.addConditions(Tables.EH_DOCKING_MAPPINGS.MAPPING_VALUE.eq(mappingValue));

        return ConvertHelper.convert(query.fetchAny(), DockingMapping.class);
    }

    @Override
    public DockingMapping findDockingMappingById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhDockingMappings.class));

        SelectQuery<EhDockingMappingsRecord> query = context.selectQuery(Tables.EH_DOCKING_MAPPINGS);
        query.addConditions(Tables.EH_DOCKING_MAPPINGS.ID.eq(id));

        return ConvertHelper.convert(query.fetchAny(), DockingMapping.class);
    }


    @Override
    public void createDockingMapping(DockingMapping dockingMapping){

        if (null == dockingMapping.getId()) {
            long id = sequenceProvider.getNextSequence(NameMapper
                    .getSequenceDomainFromTablePojo(EhDockingMappings.class));
            dockingMapping.setId(id);
        }

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDockingMappingsDao dao = new EhDockingMappingsDao(context.configuration());
		dao.insert(dockingMapping);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhDockingMappings.class, null);
    }

    @Override
    public void deleteDockingMapping(DockingMapping dockingMapping){

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDockingMappingsDao dao = new EhDockingMappingsDao(context.configuration());
        dao.delete(dockingMapping);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDockingMappings.class, null);
    }

    @Override
    public void updateDockingMapping(DockingMapping dockingMapping){

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDockingMappingsDao dao = new EhDockingMappingsDao(context.configuration());
        dao.update(dockingMapping);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDockingMappings.class, null);
    }
}
