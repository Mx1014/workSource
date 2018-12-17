package com.everhomes.visitorsys;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhVisitorSysThirdMappingDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysThirdMapping;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class VisitorSysThirdMappingProviderImpl implements VisitorSysThirdMappingProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createMapping(VisitorSysThirdMapping mapping) {

        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVisitorSysThirdMapping.class));
        mapping.setId(id);
        mapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        mapping.setCreatorUid(UserContext.current().getUser().getId());
        mapping.setOperateTime(mapping.getCreateTime());
        mapping.setOperatorUid(mapping.getCreatorUid());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysThirdMapping.class));
        EhVisitorSysThirdMappingDao dao = new EhVisitorSysThirdMappingDao(context.configuration());
        dao.insert(mapping);
    }

    @Override
    public void deleteMapping(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysThirdMapping.class));
        EhVisitorSysThirdMappingDao dao = new EhVisitorSysThirdMappingDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public List<VisitorSysThirdMapping> findMappingByVisitorId(Long visitorId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhVisitorSysThirdMapping.class));
        SelectQuery query = context.selectQuery(Tables.EH_VISITOR_SYS_THIRD_MAPPING);
        if(visitorId != null)
            query.addConditions(Tables.EH_VISITOR_SYS_THIRD_MAPPING.VISITOR_ID.eq(visitorId));
        return query.fetch().map(record -> ConvertHelper.convert(record,VisitorSysThirdMapping.class));
    }
}
