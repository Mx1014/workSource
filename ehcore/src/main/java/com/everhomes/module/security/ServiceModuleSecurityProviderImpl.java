package com.everhomes.module.security;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleSecuritiesDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleSecurities;
import com.everhomes.server.schema.tables.records.EhServiceModuleSecuritiesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ServiceModuleSecurityProviderImpl implements ServiceModuleSecurityProvider {
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public ServiceModuleSecurity findServiceModuleSecurity(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhServiceModuleSecuritiesRecord> query = context.selectQuery(Tables.EH_SERVICE_MODULE_SECURITIES);
        query.addConditions(Tables.EH_SERVICE_MODULE_SECURITIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_SERVICE_MODULE_SECURITIES.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_SERVICE_MODULE_SECURITIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_SERVICE_MODULE_SECURITIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_SERVICE_MODULE_SECURITIES.USER_ID.eq(userId));

        EhServiceModuleSecuritiesRecord record = query.fetchOne();
        return ConvertHelper.convert(record, ServiceModuleSecurity.class);
    }

    @Override
    public void createServiceModuleSecurity(ServiceModuleSecurity createServiceModuleSecurity) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceModuleSecurities.class));
        createServiceModuleSecurity.setId(id);
        createServiceModuleSecurity.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleSecurities.class));
        EhServiceModuleSecuritiesDao dao = new EhServiceModuleSecuritiesDao(context.configuration());
        dao.insert(createServiceModuleSecurity);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceModuleSecurities.class, null);
    }

    @Override
    public void updateServiceModuleSecurity(ServiceModuleSecurity updateServiceModuleSecurity) {
        updateServiceModuleSecurity.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhServiceModuleSecurities.class));
        EhServiceModuleSecuritiesDao dao = new EhServiceModuleSecuritiesDao(context.configuration());
        dao.update(updateServiceModuleSecurity);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleSecurities.class, updateServiceModuleSecurity.getId());
    }
}
