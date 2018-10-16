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
import com.everhomes.server.schema.tables.daos.EhButtScriptConfigDao;
import com.everhomes.server.schema.tables.daos.EhButtScriptPublishInfoDao;
import com.everhomes.server.schema.tables.pojos.EhButtInfoTypeEventMapping;
import com.everhomes.server.schema.tables.pojos.EhButtScriptConfig;
import com.everhomes.server.schema.tables.pojos.EhButtScriptPublishInfo;
import com.everhomes.server.schema.tables.records.EhButtScriptConfigRecord;
import com.everhomes.server.schema.tables.records.EhButtScriptPublishInfoRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ButtScriptConfigProviderImpl implements ButtScriptConfigProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<ButtScriptConfig> findButtScriptConfigByNamespaceId(Integer namespaceId ,Byte status) {
        if(namespaceId == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId is null  .");
        }

        com.everhomes.server.schema.tables.EhButtScriptConfig t = Tables.EH_BUTT_SCRIPT_CONFIG;
        List<ButtScriptConfig> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            if(status != null){
                query.addConditions(t.STATUS.eq(status));
            }


            return query;
        });

        if(list != null && list.size() > 0){
            return list;
        }
        return null ;
    }

    @Override
    public ButtScriptConfig findButtScriptConfig(Integer namespaceId , String infoType) {
        if(namespaceId == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId is null  .");
        }

        if(StringUtils.isBlank(infoType)){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "infoType is null  .");
        }

        com.everhomes.server.schema.tables.EhButtScriptConfig t = Tables.EH_BUTT_SCRIPT_CONFIG;
        List<ButtScriptConfig> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

            query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(t.INFO_TYPE.eq(infoType));

            return query;
        });

        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null ;
    }

    @Override
    public void updateButtScriptConfig(ButtScriptConfig bo) {
        rwDao().update(bo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhButtScriptConfig.class, bo.getId());
    }

    @Override
    public ButtScriptConfig crteateButtScriptConfig(ButtScriptConfig bo) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhButtScriptConfig.class));
        bo.setId(id);
        rwDao().insert(bo);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhButtScriptConfig.class, id);
        return bo ;
    }

    @Override
    public ButtScriptConfig getButtScriptConfigById(Long id) {
        return ConvertHelper.convert(dao().findById(id), ButtScriptConfig.class);
    }

    @Override
    public List<ButtScriptConfig> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhButtScriptConfig t = Tables.EH_BUTT_SCRIPT_CONFIG;

        SelectQuery<EhButtScriptConfigRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(t.ID.ge(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID);
        List<ButtScriptConfig> list = query.fetchInto(ButtScriptConfig.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    private EhButtScriptConfigDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhButtScriptConfigDao(context.configuration());
    }

    private EhButtScriptConfigDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhButtScriptConfigDao(context.configuration());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
