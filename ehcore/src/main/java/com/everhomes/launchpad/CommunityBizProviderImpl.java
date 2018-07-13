package com.everhomes.launchpad;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhCommunityBizs;
import com.everhomes.server.schema.tables.daos.EhCommunityBizsDao;
import com.everhomes.server.schema.tables.records.EhCommunityBizsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommunityBizProviderImpl implements CommunityBizProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createCommunityBiz(CommunityBiz obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhCommunityBizs.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityBizs.class));
        obj.setId(id);
        EhCommunityBizsDao dao = new EhCommunityBizsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateCommunityBiz(CommunityBiz obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityBizs.class));
        EhCommunityBizsDao dao = new EhCommunityBizsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteCommunityBiz(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunityBizs.class));
        EhCommunityBizsDao dao = new EhCommunityBizsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public CommunityBiz getCommunityBizById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunityBizs.class, id));
        EhCommunityBizsDao dao = new EhCommunityBizsDao(context.configuration());
        EhCommunityBizs result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, CommunityBiz.class);
    }

    @Override
    public CommunityBiz findCommunityBiz(Long organizationId, Long communityId, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunityBizs.class));
        SelectQuery<EhCommunityBizsRecord> query = context.selectQuery(Tables.EH_COMMUNITY_BIZS);

        //默认配置和独立配置
        if(organizationId != null){
            query.addConditions(Tables.EH_COMMUNITY_BIZS.ORGANIZATION_ID.eq(organizationId));
        }else if(communityId != null){
            query.addConditions(Tables.EH_COMMUNITY_BIZS.COMMUNITY_ID.eq(communityId));
        }

        if(status != null){
            query.addConditions(Tables.EH_COMMUNITY_BIZS.STATUS.eq(status));
        }
        CommunityBiz communityBiz = query.fetchAnyInto(CommunityBiz.class);
        return communityBiz;
    }
}
