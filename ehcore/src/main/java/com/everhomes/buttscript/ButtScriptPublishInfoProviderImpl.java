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
import com.everhomes.server.schema.tables.daos.EhButtScriptPublishInfoDao;
import com.everhomes.server.schema.tables.pojos.EhButtScriptPublishInfo;
import com.everhomes.server.schema.tables.records.EhButtScriptPublishInfoRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ButtScriptPublishInfoProviderImpl implements ButtScriptPublishInfoProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptServiceImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public ButtScriptPublishInfo getButtScriptPublishInfoById(Long id) {
        return ConvertHelper.convert(dao().findById(id), ButtScriptPublishInfo.class);
    }

    /**
     * 通过域空间及类型查询发布信息
     * @param namespaceId
     * @param infoType
     * @return
     */
    @Override
    public ButtScriptPublishInfo getButtScriptPublishInfo(Integer namespaceId , String infoType) {

        if(namespaceId == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId is null  .");
        }

        if(StringUtils.isBlank(infoType)){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "infoType is null  .");
        }

        com.everhomes.server.schema.tables.EhButtScriptPublishInfo t = Tables.EH_BUTT_SCRIPT_PUBLISH_INFO;
        List<ButtScriptPublishInfo> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

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
    public void crteateButtScriptPublishInfo(ButtScriptPublishInfo bo) {

        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhButtScriptPublishInfo.class));
        bo.setId(id);
        rwDao().insert(bo);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhButtScriptPublishInfo.class, id);
    }

    @Override
    public void updateButtScriptPublishInfo(ButtScriptPublishInfo bo) {
        rwDao().update(bo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhButtScriptPublishInfo.class, bo.getId());
    }

    @Override
    public void deleteButtScriptPublishInfo(ButtScriptPublishInfo bo) {

        com.everhomes.server.schema.tables.EhButtScriptPublishInfo t = Tables.EH_BUTT_SCRIPT_PUBLISH_INFO;

        rwContext().delete(t)
                .where(t.ID.eq(bo.getId()))
                .execute();

    }

    @Override
    public List<ButtScriptPublishInfo> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhButtScriptPublishInfo t = Tables.EH_BUTT_SCRIPT_PUBLISH_INFO;

        SelectQuery<EhButtScriptPublishInfoRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(t.ID.ge(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.PUBLISH_TIME);
        List<ButtScriptPublishInfo> list = query.fetchInto(ButtScriptPublishInfo.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }


    private EhButtScriptPublishInfoDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhButtScriptPublishInfoDao(context.configuration());
    }

    private EhButtScriptPublishInfoDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhButtScriptPublishInfoDao(context.configuration());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
