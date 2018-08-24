package com.everhomes.launchpad;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhCommunityBizsDao;
import com.everhomes.server.schema.tables.daos.EhLaunchPadConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunityBizs;
import com.everhomes.server.schema.tables.pojos.EhLaunchPadConfigs;
import com.everhomes.server.schema.tables.records.EhCommunityBizsRecord;
import com.everhomes.server.schema.tables.records.EhLaunchPadConfigsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LaunchPadConfigProviderImpl implements LaunchPadConfigProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;


    @Override
    public Long createLaunchPadConfig(LaunchPadConfig obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLaunchPadConfigs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadConfigs.class));
        obj.setId(id);
        EhLaunchPadConfigsDao dao = new EhLaunchPadConfigsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateLaunchPadConfig(LaunchPadConfig obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadConfigs.class));
        EhLaunchPadConfigsDao dao = new EhLaunchPadConfigsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhLaunchPadConfigs.class));
        EhLaunchPadConfigsDao dao = new EhLaunchPadConfigsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public LaunchPadConfig findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadConfigs.class, id));
        EhLaunchPadConfigsDao dao = new EhLaunchPadConfigsDao(context.configuration());
        EhLaunchPadConfigs result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, LaunchPadConfig.class);
    }

    @Override
    public LaunchPadConfig findLaunchPadConfig(Byte ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLaunchPadConfigs.class));
        SelectQuery<EhLaunchPadConfigsRecord> query = context.selectQuery(Tables.EH_LAUNCH_PAD_CONFIGS);
        query.addConditions(Tables.EH_LAUNCH_PAD_CONFIGS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_LAUNCH_PAD_CONFIGS.OWNER_ID.eq(ownerId));
        LaunchPadConfig launchPadConfig = query.fetchAnyInto(LaunchPadConfig.class);
        return launchPadConfig;
    }
}
