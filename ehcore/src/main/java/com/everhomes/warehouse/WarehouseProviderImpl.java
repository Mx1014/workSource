package com.everhomes.warehouse;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.warehouse.DeliveryFlag;
import com.everhomes.rest.warehouse.SearchWarehouseStockLogsResponse;
import com.everhomes.rest.warehouse.Status;
import com.everhomes.rest.warehouse.WarehouseLogDTO;
import com.everhomes.rest.warehouse.WarehouseMaterialStock;
import com.everhomes.rest.warehouse.WarehouseStockOrderDTO;
import com.everhomes.search.WarehouseStockSearcher;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWarehouseMaterialCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseMaterialsDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseOrdersDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseRequestMaterialsDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseRequestsDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseStockLogsDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseStocksDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseUnitsDao;
import com.everhomes.server.schema.tables.daos.EhWarehousesDao;
import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterialCategories;
import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterials;
import com.everhomes.server.schema.tables.pojos.EhWarehouseRequestMaterials;
import com.everhomes.server.schema.tables.pojos.EhWarehouseRequests;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStockLogs;
import com.everhomes.server.schema.tables.pojos.EhWarehouseStocks;
import com.everhomes.server.schema.tables.pojos.EhWarehouseUnits;
import com.everhomes.server.schema.tables.pojos.EhWarehouses;
import com.everhomes.server.schema.tables.records.EhWarehouseMaterialCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhWarehouseMaterialsRecord;
import com.everhomes.server.schema.tables.records.EhWarehouseRequestMaterialsRecord;
import com.everhomes.server.schema.tables.records.EhWarehouseRequestsRecord;
import com.everhomes.server.schema.tables.records.EhWarehouseStockLogsRecord;
import com.everhomes.server.schema.tables.records.EhWarehouseStocksRecord;
import com.everhomes.server.schema.tables.records.EhWarehouseUnitsRecord;
import com.everhomes.server.schema.tables.records.EhWarehousesRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.IterationMapReduceCallback;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/5/12.
 */
@Component
public class WarehouseProviderImpl implements WarehouseProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private ShardingProvider shardingProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private WarehouseStockSearcher warehouseStockSearcher;

    @Override
    public void creatWarehouse(Warehouses warehouse) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouses.class));

        warehouse.setId(id);
        warehouse.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        warehouse.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("creatWarehouse: " + warehouse);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouses.class, id));
        EhWarehousesDao dao = new EhWarehousesDao(context.configuration());
        dao.insert(warehouse);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouses.class, null);
    }

    @Override
    public void updateWarehouse(Warehouses warehouse) {
        assert(warehouse.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouses.class, warehouse.getId()));
        EhWarehousesDao dao = new EhWarehousesDao(context.configuration());
        warehouse.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(warehouse);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouses.class, warehouse.getId());

    }

    @Override
    public Warehouses findWarehouse(Long id, String ownerType, Long ownerId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehousesRecord> query = context.selectQuery(Tables.EH_WAREHOUSES);
        query.addConditions(Tables.EH_WAREHOUSES.ID.eq(id));
//        query.addConditions(Tables.EH_WAREHOUSES.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSES.COMMUNITY_ID.eq(communityId));

        List<Warehouses> result = new ArrayList<Warehouses>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Warehouses.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public Warehouses findWarehouseByNumber(String warehouseNumber, String ownerType, Long ownerId,Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehousesRecord> query = context.selectQuery(Tables.EH_WAREHOUSES);
        query.addConditions(Tables.EH_WAREHOUSES.WAREHOUSE_NUMBER.eq(warehouseNumber));
//        query.addConditions(Tables.EH_WAREHOUSES.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_WAREHOUSES.STATUS.eq(Status.ACTIVE.getCode()));

        List<Warehouses> result = new ArrayList<Warehouses>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, Warehouses.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public void creatWarehouseMaterialCategories(WarehouseMaterialCategories category) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseMaterialCategories.class));

        category.setId(id);
        category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        category.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        category.setStatus(Status.ACTIVE.getCode());
        category.setPath(category.getPath() + "/" + category.getId());
        LOGGER.info("creatWarehouseMaterialCategories: " + category);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseMaterialCategories.class, id));
        EhWarehouseMaterialCategoriesDao dao = new EhWarehouseMaterialCategoriesDao(context.configuration());
        dao.insert(category);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseMaterialCategories.class, null);
    }

    @Override
    public void updateWarehouseMaterialCategories(WarehouseMaterialCategories category) {
        assert(category.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseMaterialCategories.class, category.getId()));
        EhWarehouseMaterialCategoriesDao dao = new EhWarehouseMaterialCategoriesDao(context.configuration());
        category.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(category);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouseMaterialCategories.class, category.getId());
    }

    @Override
    public WarehouseMaterialCategories findWarehouseMaterialCategories(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialCategoriesRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.eq(id));
//        if (StringUtils.isNotBlank(ownerType)) {
//            query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
//        }
//        if (ownerId != null) {
//            query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_ID.eq(ownerId));
//        }

        List<WarehouseMaterialCategories> result = new ArrayList<WarehouseMaterialCategories>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseMaterialCategories.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public WarehouseMaterialCategories findWarehouseMaterialCategoriesByNumber(String categoryNumber, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialCategoriesRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.CATEGORY_NUMBER.eq(categoryNumber));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseMaterialCategories> result = new ArrayList<WarehouseMaterialCategories>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseMaterialCategories.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public List<WarehouseMaterialCategories> listAllChildWarehouseMaterialCategories(String superiorPath, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<WarehouseMaterialCategories> result  = new ArrayList<WarehouseMaterialCategories>();
        SelectQuery<EhWarehouseMaterialCategoriesRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES);

        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.PATH.like(superiorPath));
//        if(ownerId != null) {
//            query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_ID.eq(ownerId));
//        }
//        if(StringUtils.isNotBlank(ownerType)) {
//            query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
//        }
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.STATUS.eq(Status.ACTIVE.getCode()));


        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseMaterialCategories.class));
            return null;
        });

        return result;
    }

    @Override
    public void creatWarehouseMaterials(WarehouseMaterials materials) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseMaterials.class));

        materials.setId(id);
        materials.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        materials.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        materials.setStatus(Status.ACTIVE.getCode());
        LOGGER.info("creatWarehouseMaterials: " + materials);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseMaterials.class, id));
        EhWarehouseMaterialsDao dao = new EhWarehouseMaterialsDao(context.configuration());
        dao.insert(materials);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseMaterials.class, null);
    }

    @Override
    public void updateWarehouseMaterials(WarehouseMaterials materials) {
        assert(materials.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseMaterials.class, materials.getId()));
        EhWarehouseMaterialsDao dao = new EhWarehouseMaterialsDao(context.configuration());
        materials.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(materials);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouseMaterials.class, materials.getId());
    }

    @Override
    public WarehouseMaterials findWarehouseMaterials(Long id, String ownerType, Long ownerId,Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.ID.eq(id));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.COMMUNITY_ID.eq(communityId));

        List<WarehouseMaterials> result = new ArrayList<WarehouseMaterials>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseMaterials.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public WarehouseMaterials findWarehouseMaterialsByNumber(String materialNumber, String ownerType, Long ownerId,Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.MATERIAL_NUMBER.eq(materialNumber));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_ID.eq(ownerId));
        //eh_warehouse_materials表中已经规定了所属者，所以不再为了增加园区维度而新建scope表，对旧的数据找到所有公司所在的园区进行填写即可(任一个园区？或者全部）
        if(communityId != null){
            query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.COMMUNITY_ID.eq(communityId));
        }
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseMaterials> result = new ArrayList<WarehouseMaterials>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseMaterials.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public List<WarehouseMaterials> listWarehouseMaterialsByCategory(String categoryPath, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.CATEGORY_PATH.like(categoryPath + "%"));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseMaterials> result = new ArrayList<WarehouseMaterials>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseMaterials.class));
            return null;
        });

        return result;
    }

    @Override
    public void creatWarehouseStock(WarehouseStocks stock) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseStocks.class));

        stock.setId(id);
        stock.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        stock.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        stock.setStatus(Status.ACTIVE.getCode());
        LOGGER.info("creatWarehouseStock: " + stock);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseStocks.class, id));
        EhWarehouseStocksDao dao = new EhWarehouseStocksDao(context.configuration());
        dao.insert(stock);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseStocks.class, null);
    }

    @Override
    public void creatWarehouseStockLogs(WarehouseStockLogs log) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseStockLogs.class));

        log.setId(id);
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        LOGGER.info("creatWarehouseStockLogs: " + log);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseStockLogs.class, id));
        EhWarehouseStockLogsDao dao = new EhWarehouseStockLogsDao(context.configuration());
        dao.insert(log);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseStockLogs.class, null);
    }

    @Override
    public WarehouseStockLogs findWarehouseStockLogs(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStockLogsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCK_LOGS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.eq(id));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.OWNER_ID.eq(ownerId));

        List<WarehouseStockLogs> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStockLogs.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public WarehouseStockLogs findWarehouseStockLogs(Long id, String ownerType, Long ownerId, String materialName) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStockLogsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCK_LOGS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.eq(id));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.OWNER_ID.eq(ownerId));
        if(materialName != null){
            List<Long> fetch = context.select(Tables.EH_WAREHOUSE_MATERIALS.ID)
                    .from(Tables.EH_WAREHOUSE_MATERIALS)
                    .where(Tables.EH_WAREHOUSE_MATERIALS.NAME.like("%" + materialName + "%"))
                    .fetch(Tables.EH_WAREHOUSE_MATERIALS.ID);
            query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.MATERIAL_ID.in(fetch));
        }
        List<WarehouseStockLogs> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStockLogs.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public List<WarehouseStockLogs> listWarehouseStockLogs(CrossShardListingLocator locator, Integer pageSize) {
        List<WarehouseStockLogs> logs = new ArrayList<>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhWarehouseStockLogs.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhWarehouseStockLogsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCK_LOGS);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.lt(locator.getAnchor()));
            }
            query.addOrderBy(Tables.EH_WAREHOUSE_STOCK_LOGS.ID.desc());
            query.addLimit(pageSize - logs.size());

            query.fetch().map((r) -> {
                logs.add(ConvertHelper.convert(r, WarehouseStockLogs.class));
                return null;
            });

            if (logs.size() >= pageSize) {
                locator.setAnchor(logs.get(logs.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return logs;
    }

    @Override
    public List<WarehouseRequestMaterials> listWarehouseRequestMaterials(CrossShardListingLocator locator, Integer pageSize) {
        List<WarehouseRequestMaterials> materials = new ArrayList<>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhWarehouseRequestMaterials.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhWarehouseRequestMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUEST_MATERIALS);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.lt(locator.getAnchor()));
            }
            query.addOrderBy(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.desc());
            query.addLimit(pageSize - materials.size());

            query.fetch().map((r) -> {
                materials.add(ConvertHelper.convert(r, WarehouseRequestMaterials.class));
                return null;
            });

            if (materials.size() >= pageSize) {
                locator.setAnchor(materials.get(materials.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return materials;
    }

    @Override
    public void updateWarehouseStock(WarehouseStocks stock) {
        assert(stock.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseStocks.class, stock.getId()));
        EhWarehouseStocksDao dao = new EhWarehouseStocksDao(context.configuration());
        stock.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(stock);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouseStocks.class, stock.getId());
    }

    @Override
    public WarehouseStocks findWarehouseStocks(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.ID.eq(id));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseStocks> result = new ArrayList<WarehouseStocks>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStocks.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public WarehouseStocks findWarehouseStocksByWarehouseAndMaterial(Long warehouseId, Long materialId, String ownerType, Long ownerId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        //增加园区维度的约束,不进行非空判断，对于如何兼容还不确定方向
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseStocks> result = new ArrayList<WarehouseStocks>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStocks.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public List<WarehouseStocks> listWarehouseStocks(Long warehouseId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseStocks> result = new ArrayList<WarehouseStocks>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStocks.class));
            return null;
        });

        return result;
    }

    @Override
    public List<WarehouseStocks> listMaterialStocks(Long materialId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseStocks> result = new ArrayList<WarehouseStocks>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStocks.class));
            return null;
        });

        return result;
    }

    @Override
    public Set<Long> findWarehouseNamespace() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_WEB_MENU_SCOPES.OWNER_ID);
        query.addFrom(Tables.EH_WEB_MENU_SCOPES);
        query.addConditions(Tables.EH_WEB_MENU_SCOPES.OWNER_TYPE.eq("EhNamespaces"));
        query.addConditions(Tables.EH_WEB_MENU_SCOPES.MENU_ID.eq(WarehouseMenuIds.WAREHOUSE_MANAGEMENT));
        List<Long> fetch = query.fetch(Tables.EH_WEB_MENU_SCOPES.OWNER_ID);
        return fetch.stream().collect(Collectors.toSet());
    }

    @Override
    public String findWarehouseMenuName() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_SERVICE_MODULES.NAME);
        query.addFrom(Tables.EH_SERVICE_MODULES);
        query.addConditions(Tables.EH_SERVICE_MODULES.ID.eq(WarehouseMenuIds.WAREHOUSE_MANAGEMENT));
        return query.fetchOne(Tables.EH_SERVICE_MODULES.NAME);
    }

    @Override
    public List<WarehouseStockOrderDTO> listWarehouseStockOrders(String executor, Integer namespaceId, String ownerType, Long ownerId, Byte serviceType, Long pageAnchor, Integer pageSize,Long communityId) {
        List<WarehouseStockOrderDTO> list = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_WAREHOUSE_ORDERS);
        if(executor!=null){
            query.addConditions(Tables.EH_WAREHOUSE_ORDERS.EXECUTOR_NAME.eq(executor));
        }
        if(serviceType!=null){
            query.addConditions(Tables.EH_WAREHOUSE_ORDERS.SERVICE_TYPE.eq(serviceType));
        }
        if(communityId!=null){
            query.addConditions(Tables.EH_WAREHOUSE_ORDERS.COMMUNITY_ID.eq(communityId));
        }
//        query.addConditions(Tables.EH_WAREHOUSE_ORDERS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_ORDERS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_ORDERS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_WAREHOUSE_ORDERS.CREATE_TIME.desc());
        query.addLimit(pageAnchor.intValue(),pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        query.fetch()
                .forEach(r ->{
                    WarehouseStockOrderDTO dto = new WarehouseStockOrderDTO();
                    try{
                        dto.setExecutionTime(sdf.format(r.getValue(Tables.EH_WAREHOUSE_ORDERS.EXECUTOR_TIME)));
                    }catch (Exception e){

                    }
                    dto.setExecutor(r.getValue(Tables.EH_WAREHOUSE_ORDERS.EXECUTOR_NAME));
                    dto.setId(r.getValue(Tables.EH_WAREHOUSE_ORDERS.ID));
                    dto.setIdentity(r.getValue(Tables.EH_WAREHOUSE_ORDERS.IDENTITY));
                    dto.setServiceType(r.getValue(Tables.EH_WAREHOUSE_ORDERS.SERVICE_TYPE));
                    list.add(dto);
                });
        return list;
    }

    @Override
    public WarehouseOrder findWarehouseOrderById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_WAREHOUSE_ORDERS).where(Tables.EH_WAREHOUSE_ORDERS.ID.eq(id))
                .fetchOneInto(WarehouseOrder.class);
    }

    @Override
    public void insertWarehouseOrder(WarehouseOrder order) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhWarehouseOrdersDao dao = new EhWarehouseOrdersDao(context.configuration());
        dao.insert(order);
    }

    @Override
    public void updateWarehouseOrder(WarehouseOrder order) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhWarehouseOrdersDao dao = new EhWarehouseOrdersDao(context.configuration());
        dao.update(order);
    }

    @Override
    public void deleteWarehouseStockLogs(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_WAREHOUSE_STOCK_LOGS)
                .where(Tables.EH_WAREHOUSE_STOCK_LOGS.WAREHOUSE_ORDER_ID.eq(id))
                .execute();
    }

    @Override
    public void insertWarehouseStockLogs(List<EhWarehouseStockLogs> list) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhWarehouseStockLogsDao dao = new EhWarehouseStockLogsDao(context.configuration());
        dao.insert(list);
    }

    @Override
    public void deleteWarehouseOrderById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_WAREHOUSE_ORDERS)
                .where(Tables.EH_WAREHOUSE_ORDERS.ID.eq(id))
                .execute();
    }

    @Override
    public List<Long> findAllMaterialLogIds(Long warehouseOrderId, Long anchor, int pageSize, SearchWarehouseStockLogsResponse response) {
        Integer pageOffset = (anchor.intValue() - 1) * pageSize;
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Integer total = context.select(DSL.count(Tables.EH_WAREHOUSE_STOCK_LOGS.ID))
                .from(Tables.EH_WAREHOUSE_STOCK_LOGS)
                .where(Tables.EH_WAREHOUSE_STOCK_LOGS.WAREHOUSE_ORDER_ID.eq(warehouseOrderId))
                .fetchOne(DSL.count(Tables.EH_WAREHOUSE_STOCK_LOGS.ID));
        response.setTotal(total.longValue());
        return context.select(Tables.EH_WAREHOUSE_STOCK_LOGS.ID)
                .from(Tables.EH_WAREHOUSE_STOCK_LOGS)
                .where(Tables.EH_WAREHOUSE_STOCK_LOGS.WAREHOUSE_ORDER_ID.eq(warehouseOrderId))
                .limit(pageOffset,pageSize)
                .fetch(Tables.EH_WAREHOUSE_STOCK_LOGS.ID);
    }

    @Override
    public WarehouseStocks updateWarehouseStockByPurchase(Long warehouseId, Long materialId, Long purchaseQuantity) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_WAREHOUSE_STOCKS)
                .set(Tables.EH_WAREHOUSE_STOCKS.AMOUNT,Tables.EH_WAREHOUSE_STOCKS.AMOUNT.add(purchaseQuantity))
                .set(Tables.EH_WAREHOUSE_STOCKS.UPDATE_TIME,new Timestamp(DateHelper.currentGMTTime().getTime()))
                .where(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId))
                .and(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId))
                .execute();
        List<WarehouseStocks> warehouseStocks = context.select(Tables.EH_WAREHOUSE_STOCKS.fields())
                .from(Tables.EH_WAREHOUSE_STOCKS)
                .where(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId))
                .and(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId))
                .fetchInto(WarehouseStocks.class);
        if(warehouseStocks != null && warehouseStocks.size() > 0){
            return warehouseStocks.get(0);
        }
        return null;
    }

    @Override
    public String findWarehouseNameByMaterialId(Long materialId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> list = context.select(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID)
                .from(Tables.EH_WAREHOUSE_STOCKS)
                .where(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId))
                .fetch(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID);
        if(list.size() < 1) return null;
        return context.select(Tables.EH_WAREHOUSES.NAME)
                .from(Tables.EH_WAREHOUSES)
                .where(Tables.EH_WAREHOUSES.ID.eq(list.get(0)))
                .fetchOne(Tables.EH_WAREHOUSES.NAME);
    }

    @Override
    public String findWarehouseMaterialCategoryByMaterialId(Long materialId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Long categoryId = context.select(Tables.EH_WAREHOUSE_MATERIALS.CATEGORY_ID)
                .from(Tables.EH_WAREHOUSE_MATERIALS)
                .where(Tables.EH_WAREHOUSE_MATERIALS.ID.eq(materialId))
                .fetchOne(Tables.EH_WAREHOUSE_MATERIALS.CATEGORY_ID);
        return context.select(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.NAME)
                .from(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES)
                .where(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.eq(categoryId))
                .fetchOne(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.NAME);

    }

    @Override
    public String findWarehouseUnitNameById(Long unitId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_WAREHOUSE_UNITS.NAME)
                .from(Tables.EH_WAREHOUSE_UNITS)
                .where(Tables.EH_WAREHOUSE_UNITS.ID.eq(unitId))
                .fetchOne(Tables.EH_WAREHOUSE_UNITS.NAME);
    }

    @Override
    public WarehouseMaterials findWarehouseMaterialById(Long materialId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_WAREHOUSE_MATERIALS)
                .where(Tables.EH_WAREHOUSE_MATERIALS.ID.eq(materialId))
                .fetchOneInto(WarehouseMaterials.class);
    }

    @Override
    public WarehouseMaterialStock findWarehouseStocksByMaterialId(Long materialId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<WarehouseMaterialStock> warehouseMaterialStocks = context.selectFrom(Tables.EH_WAREHOUSE_STOCKS)
                .where(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId))
                .fetchInto(WarehouseMaterialStock.class);
        if(warehouseMaterialStocks.size() > 0){
            return warehouseMaterialStocks.get(0);
        }
        return null;
    }

    @Override
    public void deleteWarehouseRequest(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_WAREHOUSE_REQUESTS)
                .where(Tables.EH_WAREHOUSE_REQUESTS.ID.eq(requestId))
                .execute();
        context.delete(Tables.EH_WAREHOUSE_REQUEST_MATERIALS)
                .where(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.REQUEST_ID.eq(requestId))
                .execute();
    }

    @Override
    public List<WarehouseLogDTO> listMaterialLogsBySupplier(Long supplierId, Long pageAnchor, Integer pageSize) {
        List<WarehouseLogDTO> list = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhWarehouseMaterials m = Tables.EH_WAREHOUSE_MATERIALS.as("m");
        com.everhomes.server.schema.tables.EhWarehouseRequestMaterials rm = Tables.EH_WAREHOUSE_REQUEST_MATERIALS.as("rm");
        com.everhomes.server.schema.tables.EhWarehouseStockLogs logs = Tables.EH_WAREHOUSE_STOCK_LOGS.as("log");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        context.select(rm.REQUEST_ID,m.NAME,rm.AMOUNT,m.REFERENCE_PRICE)
//                .from(rm,m)
//                .where(rm.MATERIAL_ID.eq(m.ID))
//                .and(m.SUPPLIER_ID.eq(supplierId))
//                .limit(pageAnchor.intValue(),pageSize)
//                .fetch()
//                .forEach(r -> {
//                    WarehouseLogDTO dto = new WarehouseLogDTO();
//                    Long requestId = r.getValue(rm.REQUEST_ID);
//                    context.select(Tables.EH_WAREHOUSE_REQUESTS.CREATE_TIME,Tables.EH_USERS.NICK_NAME)
//                            .from(Tables.EH_WAREHOUSE_REQUESTS,Tables.EH_USERS)
//                            .where(Tables.EH_WAREHOUSE_REQUESTS.ID.eq(requestId))
//                            .and(Tables.EH_USERS.ID.eq(Tables.EH_WAREHOUSE_REQUESTS.REQUEST_UID))
//                            .fetch()
//                            .forEach(r1 ->{
//                                dto.setApplicantName(r1.getValue(Tables.EH_USERS.NICK_NAME));
//                                if(r1.getValue(Tables.EH_WAREHOUSE_REQUESTS.CREATE_TIME) != null){
//                                    dto.setApplicationTime(sdf.format
//                                            (r1.getValue(Tables.EH_WAREHOUSE_REQUESTS.CREATE_TIME)));
//                                };
//                            });
//                    dto.setMaterialCategory(findWarehouseMaterialCategoryNameById(r.getValue(m.CATEGORY_ID)));
//                    dto.setMaterialName(r.getValue(m.NAME));
//                    dto.setPurchaseQuantity(r.getValue(rm.AMOUNT));
//                    dto.setUnitPrice(r.getValue(m.REFERENCE_PRICE).toString());
//                    list.add(dto);
//                });
        //实际上是从库存记录中查的
        context.select(m.NAME,logs.DELIVERY_AMOUNT,m.REFERENCE_PRICE,logs.CREATE_TIME,logs.DELIVERY_UID,logs.REQUEST_ID)
                .from(logs,m)
                .where(logs.MATERIAL_ID.eq(m.ID))
                .and(m.SUPPLIER_ID.eq(supplierId))
                .limit(pageAnchor.intValue(),pageSize)
                .fetch()
                .forEach(r -> {
                    WarehouseLogDTO dto = new WarehouseLogDTO();
                    String nickName = "";
                    if(r.getValue(logs.REQUEST_ID)!=null){
                        nickName = userProvider.findUserById(r.getValue(logs.REQUEST_ID)).getNickName();
                    }else if(r.getValue(logs.DELIVERY_UID) != null){
                        nickName = userProvider.findUserById(r.getValue(logs.DELIVERY_UID)).getNickName();
                    }
                    dto.setApplicantName(nickName);
                    if(r.getValue(logs.CREATE_TIME)!=null){
                        dto.setApplicationTime(sdf.format(logs.CREATE_TIME));
                    }
                    dto.setMaterialCategory(findWarehouseMaterialCategoryNameById(r.getValue(m.CATEGORY_ID)));
                    dto.setMaterialName(r.getValue(m.NAME));
                    dto.setPurchaseQuantity(r.getValue(rm.AMOUNT));
                    dto.setUnitPrice(r.getValue(m.REFERENCE_PRICE).toString());
                    list.add(dto);
                });

        return list;
    }

    @Override
    public String findWarehouseMaterialCategoryNameById(Long value) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.NAME)
                .from(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES)
                .where(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.eq(value))
                .fetchOne(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.NAME);
    }

    @Override
    public boolean checkStockExists(Long warehouseId, Long materialId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<Long> fetch = context.select(Tables.EH_WAREHOUSE_STOCKS.ID)
                .from(Tables.EH_WAREHOUSE_STOCKS)
                .where(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId))
                .and(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId))
                .fetch(Tables.EH_WAREHOUSE_STOCKS.ID);
        if(fetch!=null && fetch.size() > 0) return true;
        return false;
    }

    @Override
    public void insertWarehouseStock(WarehouseStocks stock) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWarehouseStocksDao dao = new EhWarehouseStocksDao(context.configuration());
        dao.insert(stock);
    }

    @Override
    public void insertWarehouseStockLog(WarehouseStockLogs logs) {
    	logs.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
    	
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWarehouseStockLogsDao dao = new EhWarehouseStockLogsDao(context.configuration());
        dao.insert(logs);
    }

    @Override
    public String findWarehouseNameById(Long warehouseId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<String> fetch = context.select(Tables.EH_WAREHOUSES.NAME)
                .from(Tables.EH_WAREHOUSES)
                .where(Tables.EH_WAREHOUSES.ID.eq(warehouseId))
                .fetch(Tables.EH_WAREHOUSES.NAME);
        if(fetch.size() == 1){
            return fetch.get(0);
        }
        return "";
    }

    @Override
    public void deleteWarehouseStocks(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        List<Long> fetch = context.select(Tables.EH_WAREHOUSE_STOCKS.ID)
                .from(Tables.EH_WAREHOUSE_STOCKS)
                .where(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(id))
                .fetch(Tables.EH_WAREHOUSE_STOCKS.ID);
        context.delete(Tables.EH_WAREHOUSE_STOCKS)
                .where(Tables.EH_WAREHOUSE_STOCKS.ID.in(fetch))
                .execute();
        for(Long stockId : fetch){
            warehouseStockSearcher.deleteById(stockId);
        }
    }

    @Override
    public String findMaterialSupplierNameByMaterialId(Long materialId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_WAREHOUSE_MATERIALS.SUPPLIER_NAME)
                .from(Tables.EH_WAREHOUSE_MATERIALS)
                .where(Tables.EH_WAREHOUSE_MATERIALS.ID.eq(materialId))
                .fetchOne(Tables.EH_WAREHOUSE_MATERIALS.SUPPLIER_NAME);
    }

    @Override
    public Long findRequisitionId(Long requestId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_WAREHOUSE_REQUESTS.REQUISITION_ID)
                .from(Tables.EH_WAREHOUSE_REQUESTS)
                .where(Tables.EH_WAREHOUSE_REQUESTS.ID.eq(requestId))
                .fetchOne(Tables.EH_WAREHOUSE_REQUESTS.REQUISITION_ID);
    }

    @Override
    public void resetWarehouseStatusForPurchaseOrder(byte status, Long purchaseRequestId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_WAREHOUSE_PURCHASE_ORDERS)
                .set(Tables.EH_WAREHOUSE_PURCHASE_ORDERS.WAREHOUSE_STATUS, status)
                .where(Tables.EH_WAREHOUSE_PURCHASE_ORDERS.ID.eq(purchaseRequestId))
                .execute();
    }

    @Override
    public List<WarehouseRequestMaterials> findAllWarehouseRequestMaterials(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseRequestMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUEST_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.REQUEST_ID.eq(requestId));

        List<WarehouseRequestMaterials> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseRequestMaterials.class));
            return null;
        });
        return result;
    }

    @Override
    public Long getWarehouseStockAmount(Long warehouseId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<Long> stockAmounts = new ArrayList<>();
        query.fetch().map((r) -> {
            stockAmounts.add(r.getAmount());
            return null;
        });
        Long amount = 0L;
        for(Long stockAmount : stockAmounts) {
            amount = amount + stockAmount;
        }
        return amount;
    }

    @Override
    public Long getWarehouseStockAmountByMaterialId(Long materialId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<Long> stockAmounts = new ArrayList<>();
        query.fetch().map((r) -> {
            stockAmounts.add(r.getAmount());
            return null;
        });
        Long amount = 0L;
        for(Long stockAmount : stockAmounts) {
            amount = amount + stockAmount;
        }
        return amount;
    }

    @Override
    public List<WarehouseMaterialCategories> listWarehouseMaterialCategories(CrossShardListingLocator locator, Integer pageSize) {
        List<WarehouseMaterialCategories> categories = new ArrayList<WarehouseMaterialCategories>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhWarehouseMaterialCategories.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhWarehouseMaterialCategoriesRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.STATUS.eq(Status.ACTIVE.getCode()));
            query.addOrderBy(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.ID.desc());
            query.addLimit(pageSize - categories.size());

            query.fetch().map((r) -> {
                categories.add(ConvertHelper.convert(r, WarehouseMaterialCategories.class));
                return null;
            });

            if (categories.size() >= pageSize) {
                locator.setAnchor(categories.get(categories.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return categories;
    }

    @Override
    public List<WarehouseMaterials> listWarehouseMaterials(CrossShardListingLocator locator, Integer pageSize) {
        List<WarehouseMaterials> materials = new ArrayList<WarehouseMaterials>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhWarehouseMaterials.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.STATUS.eq(Status.ACTIVE.getCode()));
            query.addOrderBy(Tables.EH_WAREHOUSE_MATERIALS.ID.desc());
            query.addLimit(pageSize - materials.size());

            query.fetch().map((r) -> {
                materials.add(ConvertHelper.convert(r, WarehouseMaterials.class));
                return null;
            });

            if (materials.size() >= pageSize) {
                locator.setAnchor(materials.get(materials.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return materials;
    }

    @Override
    public List<Warehouses> listWarehouses(CrossShardListingLocator locator, Integer pageSize) {
        List<Warehouses> warehouses = new ArrayList<Warehouses>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhWarehouses.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhWarehousesRecord> query = context.selectQuery(Tables.EH_WAREHOUSES);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_WAREHOUSES.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_WAREHOUSES.STATUS.ne(Status.INACTIVE.getCode()));
            query.addOrderBy(Tables.EH_WAREHOUSES.ID.desc());
            query.addLimit(pageSize - warehouses.size());

            query.fetch().map((r) -> {
                warehouses.add(ConvertHelper.convert(r, Warehouses.class));
                return null;
            });

            if (warehouses.size() >= pageSize) {
                locator.setAnchor(warehouses.get(warehouses.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return warehouses;
    }

    @Override
    public List<WarehouseStocks> listWarehouseStocks(CrossShardListingLocator locator, Integer pageSize) {
        List<WarehouseStocks> stocks = new ArrayList<WarehouseStocks>();

        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhWarehouseStocks.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);

            if(locator.getAnchor() != null && locator.getAnchor() != 0L){
                query.addConditions(Tables.EH_WAREHOUSE_STOCKS.ID.lt(locator.getAnchor()));
            }
            query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));
            query.addOrderBy(Tables.EH_WAREHOUSE_STOCKS.ID.desc());
            query.addLimit(pageSize - stocks.size());

            query.fetch().map((r) -> {
                stocks.add(ConvertHelper.convert(r, WarehouseStocks.class));
                return null;
            });

            if (stocks.size() >= pageSize) {
                locator.setAnchor(stocks.get(stocks.size() - 1).getId());
                return IterationMapReduceCallback.AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return IterationMapReduceCallback.AfterAction.next;
        });

        return stocks;
    }

    @Override
    public WarehouseUnits findWarehouseUnits(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseUnitsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_UNITS);
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.ID.eq(id));
//        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseUnits> result = new ArrayList<WarehouseUnits>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseUnits.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public WarehouseUnits findWarehouseUnitByName(String name, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseUnitsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_UNITS);
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.NAME.eq(name));
//        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseUnits> result = new ArrayList<WarehouseUnits>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseUnits.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public void creatWarehouseUnit(WarehouseUnits unit) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseUnits.class));

        unit.setId(id);
        unit.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        unit.setStatus(Status.ACTIVE.getCode());
        LOGGER.info("creatWarehouseUnit: " + unit);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseUnits.class, id));
        EhWarehouseUnitsDao dao = new EhWarehouseUnitsDao(context.configuration());
        dao.insert(unit);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseUnits.class, null);
    }

    @Override
    public void updateWarehouseUnit(WarehouseUnits unit) {
        assert(unit.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseUnits.class, unit.getId()));
        EhWarehouseUnitsDao dao = new EhWarehouseUnitsDao(context.configuration());
        dao.update(unit);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouseUnits.class, unit.getId());
    }

    @Override
    public List<WarehouseUnits> listWarehouseMaterialUnits(String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseUnitsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_UNITS);
//        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseUnits> result = new ArrayList<WarehouseUnits>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseUnits.class));
            return null;
        });

        return result;
    }

    @Override
    public void creatWarehouseRequest(WarehouseRequests request) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseRequests.class));

        request.setId(id);
        request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        request.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        LOGGER.info("creatWarehouseRequest: " + request);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseRequests.class, id));
        EhWarehouseRequestsDao dao = new EhWarehouseRequestsDao(context.configuration());
        dao.insert(request);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseRequests.class, null);
    }

    @Override
    public void updateWarehouseRequest(WarehouseRequests request) {
        assert(request.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseRequests.class, request.getId()));
        EhWarehouseRequestsDao dao = new EhWarehouseRequestsDao(context.configuration());
        dao.update(request);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouseRequests.class, request.getId());
    }

    @Override
    public void creatWarehouseRequestMaterial(WarehouseRequestMaterials requestMaterial) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarehouseRequestMaterials.class));
        requestMaterial.setId(id);
        //这里toString，会报错，requestId
//        LOGGER.info("creatWarehouseRequestMaterial: " + requestMaterial);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseRequestMaterials.class, id));
        EhWarehouseRequestMaterialsDao dao = new EhWarehouseRequestMaterialsDao(context.configuration());
        dao.insert(requestMaterial);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarehouseRequestMaterials.class, null);
    }

    @Override
    public void updateWarehouseRequestMaterial(WarehouseRequestMaterials requestMaterial) {
        assert(requestMaterial.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarehouseRequestMaterials.class, requestMaterial.getId()));
        EhWarehouseRequestMaterialsDao dao = new EhWarehouseRequestMaterialsDao(context.configuration());
        dao.update(requestMaterial);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarehouseRequestMaterials.class, requestMaterial.getId());
    }

    @Override
    public WarehouseRequestMaterials findWarehouseRequestMaterials(Long requestId, Long warehouseId, Long materialId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseRequestMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUEST_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.REQUEST_ID.eq(requestId));
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.WAREHOUSE_ID.eq(warehouseId));
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.MATERIAL_ID.eq(materialId));

        List<WarehouseRequestMaterials> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseRequestMaterials.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }

    @Override
    public List<WarehouseRequestMaterials> listWarehouseRequestMaterials(Long requestId, String ownerType, Long ownerId,Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseRequestMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUEST_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.REQUEST_ID.eq(requestId));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.COMMUNITY_ID.eq(communityId));

        List<WarehouseRequestMaterials> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseRequestMaterials.class));
            return null;
        });
        return result;
    }

    @Override
    public List<WarehouseRequestMaterials> listUnDeliveryWarehouseRequestMaterials(Long requestId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseRequestMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUEST_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.REQUEST_ID.eq(requestId));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.DELIVERY_FLAG.eq(DeliveryFlag.NO.getCode()));

        List<WarehouseRequestMaterials> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseRequestMaterials.class));
            return null;
        });
        return result;
    }

    @Override
    public List<WarehouseRequestMaterials> listWarehouseRequestMaterials(List<Long> ids, String ownerType, Long ownerId,Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseRequestMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUEST_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.in(ids));
        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.OWNER_TYPE.eq(ownerType));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.COMMUNITY_ID.eq(communityId));

        query.addOrderBy(Tables.EH_WAREHOUSE_REQUEST_MATERIALS.ID.desc());
        List<WarehouseRequestMaterials> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseRequestMaterials.class));
            return null;
        });
        return result;
    }

    @Override
    public WarehouseRequests findWarehouseRequests(Long id, String ownerType, Long ownerId,Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseRequestsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_REQUESTS);
        query.addConditions(Tables.EH_WAREHOUSE_REQUESTS.ID.eq(id));
        if(ownerType != null){
            query.addConditions(Tables.EH_WAREHOUSE_REQUESTS.OWNER_TYPE.eq(ownerType));
        }
//        query.addConditions(Tables.EH_WAREHOUSE_REQUESTS.OWNER_ID.eq(ownerId));
//        query.addConditions(Tables.EH_WAREHOUSE_REQUESTS.COMMUNITY_ID.eq(communityId));

        List<WarehouseRequests> result = new ArrayList<WarehouseRequests>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseRequests.class));
            return null;
        });
        if(result.size()==0)
            return null;

        return result.get(0);
    }
}
