package com.everhomes.PmNotify;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.pmNotify.PmNotifyConfigurations;
import com.everhomes.pmNotify.PmNotifyProvider;
import com.everhomes.rest.pmNotify.PmNotifyConfigurationStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPmNotifyConfigurationsDao;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionStandards;
import com.everhomes.server.schema.tables.pojos.EhPmNotifyConfigurations;
import com.everhomes.server.schema.tables.records.EhPmNotifyConfigurationsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/9/12.
 */
@Component
public class PmNotifyProviderImpl implements PmNotifyProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmNotifyProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public void createPmNotifyConfigurations(PmNotifyConfigurations configuration) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPmNotifyConfigurations.class));

        configuration.setId(id);
        configuration.setStatus(PmNotifyConfigurationStatus.VAILD.getCode());
        configuration.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        LOGGER.info("createPmNotifyConfigurations: " + configuration);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyConfigurationsDao dao = new EhPmNotifyConfigurationsDao(context.configuration());
        dao.insert(configuration);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmNotifyConfigurations.class, null);
    }

    @Override
    public void updatePmNotifyConfigurations(PmNotifyConfigurations configuration) {
        assert(configuration.getId() != null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhPmNotifyConfigurationsDao dao = new EhPmNotifyConfigurationsDao(context.configuration());
        dao.update(configuration);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmNotifyConfigurations.class, configuration.getId());
    }

    @Override
    public List<PmNotifyConfigurations> listScopePmNotifyConfigurations(String ownerType, Byte scopeType, Long scopeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<PmNotifyConfigurations> result  = new ArrayList<PmNotifyConfigurations>();
        SelectQuery<EhPmNotifyConfigurationsRecord> query = context.selectQuery(Tables.EH_PM_NOTIFY_CONFIGURATIONS);
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_TYPE.eq(scopeType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_ID.eq(scopeId));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.STATUS.eq(PmNotifyConfigurationStatus.VAILD.getCode()));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PmNotifyConfigurations.class));
            return null;
        });

        return result;
    }

    @Override
    public PmNotifyConfigurations findScopePmNotifyConfiguration(Long id, String ownerType, Byte scopeType, Long scopeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<PmNotifyConfigurations> result  = new ArrayList<PmNotifyConfigurations>();
        SelectQuery<EhPmNotifyConfigurationsRecord> query = context.selectQuery(Tables.EH_PM_NOTIFY_CONFIGURATIONS);
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.ID.eq(id));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_TYPE.eq(scopeType));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.SCOPE_ID.eq(scopeId));
        query.addConditions(Tables.EH_PM_NOTIFY_CONFIGURATIONS.STATUS.eq(PmNotifyConfigurationStatus.VAILD.getCode()));
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, PmNotifyConfigurations.class));
            return null;
        });

        if(null != result && 0 != result.size()){
            return result.get(0);
        }
        return null;
    }
}
