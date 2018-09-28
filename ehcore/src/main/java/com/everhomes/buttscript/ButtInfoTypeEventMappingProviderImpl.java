package com.everhomes.buttscript;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhButtInfoTypeEventMappingDao;
import com.everhomes.server.schema.tables.pojos.EhButtInfoTypeEventMapping;
import com.everhomes.server.schema.tables.pojos.EhButtScriptPublishInfo;
import com.everhomes.server.schema.tables.records.EhButtInfoTypeEventMappingRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ButtInfoTypeEventMappingProviderImpl implements ButtInfoTypeEventMappingProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptLastCommitProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<ButtInfoTypeEventMapping> findButtInfoTypeEventMapping(String eventName , Integer namespaceId) {

        if(namespaceId == null){
            LOGGER.info("namespaceId is null .");
            return null ;
        }

        if(StringUtils.isBlank(eventName)){
            LOGGER.info("eventName is null .");
            return null ;
        }

        com.everhomes.server.schema.tables.EhButtInfoTypeEventMapping t = Tables.EH_BUTT_INFO_TYPE_EVENT_MAPPING;
        List<ButtInfoTypeEventMapping> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

            query.addConditions(t.EVENT_NAME.eq(eventName));
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));

            return query;
        });

        if(list != null && list.size() > 0){
            return list;
        }
        return null ;
    }

    @Override
    public List<ButtInfoTypeEventMapping> findByInfoType(String infoType, Integer namespaceId) {
        if(namespaceId == null){
            LOGGER.info("namespaceId is null .");
            return null ;
        }

        if(StringUtils.isBlank(infoType)){
            LOGGER.info("infoType is null .");
            return null ;
        }

        com.everhomes.server.schema.tables.EhButtInfoTypeEventMapping t = Tables.EH_BUTT_INFO_TYPE_EVENT_MAPPING;
        List<ButtInfoTypeEventMapping> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

            query.addConditions(t.INFO_TYPE.eq(infoType));
            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));

            return query;
        });

        if(list != null && list.size() > 0){
            return list;
        }
        return null ;
    }

    @Override
    public ButtInfoTypeEventMapping crteateButtInfoTypeEventMapping(ButtInfoTypeEventMapping bo) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhButtInfoTypeEventMapping.class));
        bo.setId(id);
        rwDao().insert(bo);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhButtInfoTypeEventMapping.class, id);
        return bo ;
    }

    @Override
    public void updateButtInfoTypeEventMapping(ButtInfoTypeEventMapping bo) {
        rwDao().update(bo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhButtInfoTypeEventMapping.class, bo.getId());
    }

    @Override
    public void deleteButtInfoTypeEventMapping(ButtInfoTypeEventMapping bo) {

        com.everhomes.server.schema.tables.EhButtInfoTypeEventMapping t = Tables.EH_BUTT_INFO_TYPE_EVENT_MAPPING;

        rwContext().delete(t)
                .where(t.ID.eq(bo.getId()))
                .execute();
    }

    @Override
    public ButtInfoTypeEventMapping getButtInfoTypeEventMappingById(Long id) {
        return  ConvertHelper.convert(dao().findById(id), ButtInfoTypeEventMapping.class);
    }

    @Override
    public List<ButtInfoTypeEventMapping> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhButtInfoTypeEventMapping t = Tables.EH_BUTT_INFO_TYPE_EVENT_MAPPING;

        SelectQuery<EhButtInfoTypeEventMappingRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(t.ID.ge(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.SYNC_FLAG.desc());
        List<ButtInfoTypeEventMapping> list = query.fetchInto(ButtInfoTypeEventMapping.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }


    private EhButtInfoTypeEventMappingDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhButtInfoTypeEventMappingDao(context.configuration());
    }

    private EhButtInfoTypeEventMappingDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhButtInfoTypeEventMappingDao(context.configuration());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
