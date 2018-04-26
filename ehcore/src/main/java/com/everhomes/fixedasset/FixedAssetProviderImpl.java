package com.everhomes.fixedasset;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.fixedasset.FixedAssetCategoryStatus;
import com.everhomes.rest.fixedasset.FixedAssetStatisticsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhFixedAssetOperationLogs;
import com.everhomes.server.schema.tables.daos.EhFixedAssetCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhFixedAssetOperationLogsDao;
import com.everhomes.server.schema.tables.daos.EhFixedAssetsDao;
import com.everhomes.server.schema.tables.pojos.EhFixedAssetCategories;
import com.everhomes.server.schema.tables.pojos.EhFixedAssets;
import com.everhomes.server.schema.tables.records.EhFixedAssetCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhFixedAssetDefaultCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhFixedAssetOperationLogsRecord;
import com.everhomes.server.schema.tables.records.EhFixedAssetsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Repository
public class FixedAssetProviderImpl implements FixedAssetProvider {

    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<FixedAssetDefaultCategory> listAllFixedAssetDefaultCategories() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetDefaultCategoriesRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_DEFAULT_CATEGORIES);
        query.addOrderBy(Tables.EH_FIXED_ASSET_DEFAULT_CATEGORIES.DEFAULT_ORDER.asc());

        Result<EhFixedAssetDefaultCategoriesRecord> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> ConvertHelper.convert(r, FixedAssetDefaultCategory.class));
        }
        return Collections.emptyList();
    }

    @Override
    public Integer createFixedAssetCategory(FixedAssetCategory fixedAssetCategory) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFixedAssetCategories.class));
        fixedAssetCategory.setId(id.intValue());
        fixedAssetCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        fixedAssetCategory.setCreatorUid(UserContext.currentUserId());
        fixedAssetCategory.setPath(fixedAssetCategory.getPath() + "/" + id);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFixedAssetCategories.class));
        EhFixedAssetCategoriesDao dao = new EhFixedAssetCategoriesDao(context.configuration());
        dao.insert(fixedAssetCategory);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFixedAssetCategories.class, null);
        return id.intValue();
    }

    @Override
    public Integer updateFixedAssetCategory(FixedAssetCategory fixedAssetCategory) {
        fixedAssetCategory.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        fixedAssetCategory.setOperatorUid(UserContext.currentUserId());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFixedAssetCategories.class));
        EhFixedAssetCategoriesDao dao = new EhFixedAssetCategoriesDao(context.configuration());
        dao.update(fixedAssetCategory);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFixedAssetCategories.class, fixedAssetCategory.getId().longValue());
        return fixedAssetCategory.getId();
    }

    @Override
    public boolean isFixedAssetItemNoExist(CheckFixedAssetItemNoExistRequest request) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetsRecord> query = context.selectQuery(Tables.EH_FIXED_ASSETS);
        query.addConditions(Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(request.getNamespaceId()));
        query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(request.getOwnerType()));
        query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(request.getOwnerId()));
        query.addConditions(Tables.EH_FIXED_ASSETS.ITEM_NO.eq(request.getItemNo()));
        query.addConditions(Tables.EH_FIXED_ASSETS.DELETE_UID.eq(Long.valueOf(0)));
        if (request.getSelfFixedAssetId() != null) {
            query.addConditions(Tables.EH_FIXED_ASSETS.ID.ne(request.getSelfFixedAssetId()));
        }
        return query.fetchCount() > 0;
    }

    @Override
    public List<FixedAssetCategory> getAllSubFixedAssetCategories(String parentPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetCategoriesRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_CATEGORIES);
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.PATH.like(parentPath + "/%"));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.STATUS.eq(FixedAssetCategoryStatus.VALID.getCode()));
        Result<EhFixedAssetCategoriesRecord> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> ConvertHelper.convert(r, FixedAssetCategory.class));
        }
        return Collections.emptyList();
    }

    @Override
    public FixedAssetCategory getFixedAssetCategoryById(Integer id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetCategoriesRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_CATEGORIES);
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.ID.eq(id));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.STATUS.eq(FixedAssetCategoryStatus.VALID.getCode()));
        EhFixedAssetCategoriesRecord record = query.fetchOne();

        return ConvertHelper.convert(record, FixedAssetCategory.class);
    }

    @Override
    public Integer getMaxDefaultOrderCurrentLevel(Integer namespaceId, String ownerType, Long ownerId, Integer parentId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> query = context.select(Tables.EH_FIXED_ASSET_CATEGORIES.DEFAULT_ORDER.max()).from(Tables.EH_FIXED_ASSET_CATEGORIES);
        Condition condition = Tables.EH_FIXED_ASSET_CATEGORIES.NAMESPACE_ID.eq(namespaceId)
                .and(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_ID.eq(ownerId))
                .and(Tables.EH_FIXED_ASSET_CATEGORIES.PARENT_ID.eq(parentId));
        query.where(condition);

        Record1<Integer> record = query.fetchOne();

        if (record == null) {
            return Integer.valueOf(0);
        }
        return record.value1() == null ? 0 : record.value1();
    }

    @Override
    public boolean isCategoryNameExistInSameLevel(CheckFixedAssetCategoryNameExistRequest request) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetCategoriesRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_CATEGORIES);
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.NAMESPACE_ID.eq(request.getNamespaceId()));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_TYPE.eq(request.getOwnerType()));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_ID.eq(request.getOwnerId()));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.PARENT_ID.eq(request.getParentId()));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.NAME.eq(request.getCategoryName()));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.STATUS.eq(FixedAssetCategoryStatus.VALID.getCode()));
        if (request.getSelfCategoryId() != null) {
            query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.ID.ne(request.getSelfCategoryId()));
        }

        return query.fetchCount() > 0;
    }

    @Override
    public Integer countFixedAssetCategoriesIgnoreStatus(Integer namespaceId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetCategoriesRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_CATEGORIES);
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_ID.eq(ownerId));
        return query.fetchCount();
    }

    @Override
    public List<FixedAssetCategory> findFixedAssetCategories(Integer namespaceId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetCategoriesRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_CATEGORIES);
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FIXED_ASSET_CATEGORIES.STATUS.eq(FixedAssetCategoryStatus.VALID.getCode()));
        query.addOrderBy(Tables.EH_FIXED_ASSET_CATEGORIES.DEFAULT_ORDER.asc());

        Result<EhFixedAssetCategoriesRecord> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> ConvertHelper.convert(r, FixedAssetCategory.class));
        }
        return Collections.emptyList();
    }

    @Override
    public Long createFixedAsset(FixedAsset fixedAsset) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFixedAssets.class));
        fixedAsset.setId(id);
        fixedAsset.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        fixedAsset.setCreatorUid(UserContext.currentUserId());
        fixedAsset.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        fixedAsset.setOperatorUid(UserContext.currentUserId());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFixedAssets.class));
        EhFixedAssetsDao dao = new EhFixedAssetsDao(context.configuration());
        dao.insert(fixedAsset);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFixedAssets.class, null);
        return id;
    }

    @Override
    public Long updateFixedAsset(FixedAsset fixedAsset) {
        fixedAsset.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        fixedAsset.setOperatorUid(UserContext.currentUserId());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhFixedAssetsDao dao = new EhFixedAssetsDao(context.configuration());
        dao.update(fixedAsset);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFixedAssets.class, fixedAsset.getId());
        return fixedAsset.getId();
    }

    @Override
    public FixedAsset getFixedAssetDetail(Long fixedAssetId, Integer namespaceId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetsRecord> query = context.selectQuery(Tables.EH_FIXED_ASSETS);
        query.addConditions(Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FIXED_ASSETS.ID.eq(fixedAssetId));
        query.addConditions(Tables.EH_FIXED_ASSETS.DELETE_UID.eq(Long.valueOf(0)));

        EhFixedAssetsRecord record = query.fetchOne();
        if (record != null) {
            return ConvertHelper.convert(record, FixedAsset.class);
        }
        return null;
    }

    @Override
    public FixedAsset getFixedAssetDetailByItemNum(String itemNum, Integer namespaceId, String ownerType, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetsRecord> query = context.selectQuery(Tables.EH_FIXED_ASSETS);
        query.addConditions(Tables.EH_FIXED_ASSETS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_FIXED_ASSETS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_FIXED_ASSETS.ITEM_NO.eq(itemNum));
        query.addConditions(Tables.EH_FIXED_ASSETS.DELETE_UID.eq(Long.valueOf(0)));

        EhFixedAssetsRecord record = query.fetchOne();
        if (record != null) {
            return ConvertHelper.convert(record, FixedAsset.class);
        }
        return null;
    }

    @Override
    public List<FixedAsset> findFixedAssets(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetsRecord> query = context.selectQuery(Tables.EH_FIXED_ASSETS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FIXED_ASSETS.ID.le(locator.getAnchor()));
        }
        query.addConditions(Tables.EH_FIXED_ASSETS.DELETE_UID.eq(Long.valueOf(0)));
        query.addOrderBy(Tables.EH_FIXED_ASSETS.CREATE_TIME.desc(), Tables.EH_FIXED_ASSETS.ID.desc());
        query.addLimit(count + 1);

        Result<EhFixedAssetsRecord> result = query.fetch();

        List<FixedAsset> objs = result.map((r) -> {
            return ConvertHelper.convert(r, FixedAsset.class);
        });

        if (objs != null && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public FixedAssetStatisticsDTO getFixedAssetsStatistic(Condition condition) {
        FixedAssetStatisticsDTO fixedAssetStatisticsDTO = new FixedAssetStatisticsDTO();
        if (condition == null) {
            return fixedAssetStatisticsDTO;
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record2<Integer, BigDecimal>> query = context.select(Tables.EH_FIXED_ASSETS.ID.count(), Tables.EH_FIXED_ASSETS.PRICE.sum()).from(Tables.EH_FIXED_ASSETS);

        condition = condition.and(Tables.EH_FIXED_ASSETS.DELETE_UID.eq(Long.valueOf(0)));
        query.where(condition);

        Result<Record2<Integer, BigDecimal>> result = query.fetch();
        if (result != null) {
            Integer count = result.get(0).value1();
            BigDecimal totalAmount = result.get(0).value2();
            fixedAssetStatisticsDTO.setTotalCount(count != null ? count.longValue() : 0);
            fixedAssetStatisticsDTO.setTotalAmount(totalAmount != null ? totalAmount.setScale(2, RoundingMode.HALF_UP).doubleValue() : 0);
        }
        return fixedAssetStatisticsDTO;
    }

    @Override
    public Long createFixedAssetOperationLog(FixedAssetOperationLog fixedAssetOperationLog) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFixedAssetOperationLogs.class));
        fixedAssetOperationLog.setId(id);
        fixedAssetOperationLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        fixedAssetOperationLog.setCreatorUid(UserContext.currentUserId());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFixedAssetOperationLogs.class));
        EhFixedAssetOperationLogsDao dao = new EhFixedAssetOperationLogsDao(context.configuration());
        dao.insert(fixedAssetOperationLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFixedAssetOperationLogs.class, null);
        return id;
    }

    @Override
    public List<FixedAssetOperationLog> getFixedAssetOperationLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhFixedAssetOperationLogsRecord> query = context.selectQuery(Tables.EH_FIXED_ASSET_OPERATION_LOGS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FIXED_ASSET_OPERATION_LOGS.ID.le(locator.getAnchor().longValue()));
        }
        query.addOrderBy(Tables.EH_FIXED_ASSET_OPERATION_LOGS.CREATE_TIME.desc());
        query.addLimit(count + 1);

        Result<EhFixedAssetOperationLogsRecord> result = query.fetch();

        List<FixedAssetOperationLog> objs = result.map((r) -> {
            return ConvertHelper.convert(r, FixedAssetOperationLog.class);
        });

        if (objs != null && objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId().longValue());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

}
