package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMeterCategoriesDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterCategories;
import com.everhomes.server.schema.tables.records.EhEnergyMeterCategoriesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.everhomes.server.schema.Tables.EH_ENERGY_METER_CATEGORIES;

/**
 * Created by xq.tian on 2016/10/31.
 */
@Repository
public class EnergyMeterCategoryProviderImpl implements EnergyMeterCategoryProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public EnergyMeterCategory findById(Integer namespaceId, Long id) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_CATEGORIES.ID.eq(id))
                .fetchOneInto(EnergyMeterCategory.class);
    }

    @Override
    public EnergyMeterCategory findById( Long id) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.ID.eq(id))
                .fetchOneInto(EnergyMeterCategory.class);
    }

    @Override
    public EnergyMeterCategory findByName(Integer namespaceId, Long communityId, String name) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId))
                .and(EH_ENERGY_METER_CATEGORIES.COMMUNITY_ID.eq(communityId))
                .and(EH_ENERGY_METER_CATEGORIES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .and(EH_ENERGY_METER_CATEGORIES.NAME.eq(name))
                .fetchAnyInto(EnergyMeterCategory.class);
    }

    @Override
    public List<EnergyMeterCategory> listMeterCategories(Integer namespaceId, Byte categoryType, Long ownerId, String ownerType, List<Long> communityIds) {
        List<EnergyMeterCategory> categories = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEnergyMeterCategoriesRecord> query = context.selectQuery(EH_ENERGY_METER_CATEGORIES);
        query.addConditions(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
//        query.addConditions(EH_ENERGY_METER_CATEGORIES.OWNER_ID.eq(ownerId));
//        query.addConditions(EH_ENERGY_METER_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(categoryType));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));

        if(communityIds != null && communityIds.size()>0) {
            query.addConditions(EH_ENERGY_METER_CATEGORIES.COMMUNITY_ID.in(communityIds));
        }

        query.fetch().map((r) -> {
            categories.add(ConvertHelper.convert(r, EnergyMeterCategory.class));
            return null;
        });

        return categories;

    }

    @Override
    public List<EnergyMeterCategory> listOrgGeneralMeterCategories(Integer namespaceId, Byte categoryType, Long ownerId, Long communityId) {
        List<EnergyMeterCategory> categories = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEnergyMeterCategoriesRecord> query = context.selectQuery(EH_ENERGY_METER_CATEGORIES);
        query.addConditions(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(categoryType));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));

        if(communityId != null ) {
            query.addConditions(EH_ENERGY_METER_CATEGORIES.COMMUNITY_ID.eq(communityId));
        }

        query.fetch().map((r) -> {
            categories.add(ConvertHelper.convert(r, EnergyMeterCategory.class));
            return null;
        });

        return categories;
    }

    @Override
    public List<EnergyMeterCategory> listMeterCategories(Integer namespaceId, Byte categoryType, Long ownerId, String ownerType, Long communityId, Timestamp lastUpdateTime) {
        List<EnergyMeterCategory> categories = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEnergyMeterCategoriesRecord> query = context.selectQuery(EH_ENERGY_METER_CATEGORIES);
        query.addConditions(EH_ENERGY_METER_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(categoryType));
        query.addConditions(EH_ENERGY_METER_CATEGORIES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()));

        if(communityId != null) {
            query.addConditions(EH_ENERGY_METER_CATEGORIES.COMMUNITY_ID.eq(communityId));
        }

        if(ownerId!=null){
            query.addConditions(Tables.EH_ENERGY_METER_CATEGORIES.OWNER_ID.eq(ownerId));
        }

        if(lastUpdateTime != null) {
            query.addConditions(EH_ENERGY_METER_CATEGORIES.CREATE_TIME.gt(lastUpdateTime)
            .or(EH_ENERGY_METER_CATEGORIES.UPDATE_TIME.gt(lastUpdateTime)));
        }
        query.fetch().map((r) -> {
            categories.add(ConvertHelper.convert(r, EnergyMeterCategory.class));
            return null;
        });

        return categories;
    }

    @Override
    public List<EnergyMeterCategory> listMeterCategories(List<Long> categoryIds, Byte categoryType) {
        return context().selectFrom(EH_ENERGY_METER_CATEGORIES)
                .where(EH_ENERGY_METER_CATEGORIES.ID.in(categoryIds))
                .and(EH_ENERGY_METER_CATEGORIES.CATEGORY_TYPE.eq(categoryType))
                .and(EH_ENERGY_METER_CATEGORIES.STATUS.eq(EnergyCommonStatus.ACTIVE.getCode()))
                .fetchInto(EnergyMeterCategory.class);
    }

    @Override
    public long createEnergyMeterCategory(EnergyMeterCategory category) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnergyMeterCategories.class));
        category.setId(id);
        category.setCreatorUid(UserContext.current().getUser().getId());
        category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rwDao().insert(category);
        return id;
    }

    @Override
    public void updateEnergyMeterCategory(EnergyMeterCategory category) {
        category.setUpdateUid(UserContext.current().getUser().getId());
        category.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        rwDao().update(category);
    }

    @Override
    public void deleteEnergyMeterCategory(EnergyMeterCategory category) {
        rwDao().delete(category);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    // 可读写的dao
    private EhEnergyMeterCategoriesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhEnergyMeterCategoriesDao(context.configuration());
    }
}
