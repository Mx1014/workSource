package com.everhomes.warehouse;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.equipment.EquipmentInspectionStandards;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.warehouse.Status;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEquipmentInspectionEquipmentsDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseMaterialCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhWarehouseMaterialsDao;
import com.everhomes.server.schema.tables.daos.EhWarehousesDao;
import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterialCategories;
import com.everhomes.server.schema.tables.pojos.EhWarehouseMaterials;
import com.everhomes.server.schema.tables.pojos.EhWarehouses;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback;
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
    public Warehouses findWarehouse(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehousesRecord> query = context.selectQuery(Tables.EH_WAREHOUSES);
        query.addConditions(Tables.EH_WAREHOUSES.ID.eq(id));
        query.addConditions(Tables.EH_WAREHOUSES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSES.OWNER_ID.eq(ownerId));

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
    public Warehouses findWarehouseByNumber(String warehouseNumber, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehousesRecord> query = context.selectQuery(Tables.EH_WAREHOUSES);
        query.addConditions(Tables.EH_WAREHOUSES.WAREHOUSE_NUMBER.eq(warehouseNumber));
        query.addConditions(Tables.EH_WAREHOUSES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSES.OWNER_ID.eq(ownerId));

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
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_ID.eq(ownerId));

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
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.OWNER_ID.eq(ownerId));

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
    public List<WarehouseMaterialCategories> listAllChildWarehouseMaterialCategories(String superiorPath) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<WarehouseMaterialCategories> result  = new ArrayList<WarehouseMaterialCategories>();
        SelectQuery<EhWarehouseMaterialCategoriesRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES);

        query.addConditions(Tables.EH_WAREHOUSE_MATERIAL_CATEGORIES.PATH.like(superiorPath));
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
    public WarehouseMaterials findWarehouseMaterials(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.ID.eq(id));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_ID.eq(ownerId));

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
    public WarehouseMaterials findWarehouseMaterialsByNumber(String materialNumber, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.MATERIAL_NUMBER.eq(materialNumber));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_ID.eq(ownerId));

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
    public List<WarehouseMaterials> listWarehouseMaterialsByCategory(Long categoryId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseMaterialsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_MATERIALS);
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.CATEGORY_ID.eq(categoryId));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_MATERIALS.OWNER_ID.eq(ownerId));
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

    }

    @Override
    public void updateWarehouseStock(WarehouseStocks stock) {

    }

    @Override
    public WarehouseStocks findWarehouseStocks(Long id, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.ID.eq(id));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
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
    public WarehouseStocks findWarehouseStocksByWarehouseAndMaterial(Long warehouseId, Long materialId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.MATERIAL_ID.eq(materialId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
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
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseStocks> result = new ArrayList<WarehouseStocks>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseStocks.class));
            return null;
        });

        return result;
    }

    @Override
    public Long getWarehouseStockAmount(Long warehouseId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseStocksRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_STOCKS);
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.WAREHOUSE_ID.eq(warehouseId));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
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
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_STOCKS.OWNER_ID.eq(ownerId));
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
        return null;
    }

    @Override
    public WarehouseUnits findWarehouseUnits(Long id, String ownerType, Long ownerId) {
        return null;
    }

    @Override
    public List<WarehouseUnits> listWarehouseMaterialUnits(String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWarehouseUnitsRecord> query = context.selectQuery(Tables.EH_WAREHOUSE_UNITS);
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WAREHOUSE_UNITS.STATUS.eq(Status.ACTIVE.getCode()));

        List<WarehouseUnits> result = new ArrayList<WarehouseUnits>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, WarehouseUnits.class));
            return null;
        });

        return result;
    }
}
