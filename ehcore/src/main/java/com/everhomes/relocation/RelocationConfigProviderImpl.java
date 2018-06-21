package com.everhomes.relocation;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhRelocationConfigs;
import com.everhomes.server.schema.tables.daos.EhRelocationConfigsDao;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class RelocationConfigProviderImpl implements RelocationConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public RelocationConfig createRelocationConfig(RelocationConfig bean) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRelocationConfigs.class));
        EhRelocationConfigsDao dao = new EhRelocationConfigsDao(context.configuration());
        if(bean.getId() == null) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRelocationConfigs.class));
            bean.setId(id);
        }
        bean.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(bean);
        return bean;
    }

    @Override
    public RelocationConfig updateRelocationConfig(RelocationConfig bean) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRelocationConfigs.class));
        EhRelocationConfigsDao dao = new EhRelocationConfigsDao(context.configuration());
        bean.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(bean);
        return bean;
    }

    @Override
    public RelocationConfig searchRelocationConfigById(Integer namespaceId, String ownerType, Long ownerId, Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhRelocationConfigs.class));
        SelectQuery query = context.selectQuery(Tables.EH_RELOCATION_CONFIGS);
        if (null != namespaceId)
            query.addConditions(Tables.EH_RELOCATION_CONFIGS.NAMESPACE_ID.eq(namespaceId));
        if (StringUtils.isNotEmpty(ownerType))
            query.addConditions(Tables.EH_RELOCATION_CONFIGS.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            query.addConditions(Tables.EH_RELOCATION_CONFIGS.OWNER_ID.eq(ownerId));
        if (null != id)
            query.addConditions(Tables.EH_RELOCATION_CONFIGS.ID.eq(id));
        Record result = query.fetchOne();
        return null != result ? ConvertHelper.convert(result, RelocationConfig.class) : null;
    }
}
