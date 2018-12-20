package com.everhomes.organization.pm;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.energy.EnergyMeterType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhDefaultChargingItemPropertiesDao;
import com.everhomes.server.schema.tables.daos.EhDefaultChargingItemsDao;
import com.everhomes.server.schema.tables.pojos.EhDefaultChargingItemProperties;
import com.everhomes.server.schema.tables.pojos.EhDefaultChargingItems;
import com.everhomes.server.schema.tables.records.EhDefaultChargingItemPropertiesRecord;
import com.everhomes.server.schema.tables.records.EhDefaultChargingItemsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/10/27.
 */
@Component
public class DefaultChargingItemProviderImpl implements DefaultChargingItemProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChargingItemProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createDefaultChargingItem(DefaultChargingItem defaultChargingItem) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDefaultChargingItems.class));
        defaultChargingItem.setId(id);
        defaultChargingItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        defaultChargingItem.setCreateUid(UserContext.current().getUser().getId());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDefaultChargingItems.class));
        EhDefaultChargingItemsDao dao = new EhDefaultChargingItemsDao(context.configuration());
        dao.insert(defaultChargingItem);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDefaultChargingItems.class, id);

    }

    @Override
    public void createDefaultChargingItemProperty(DefaultChargingItemProperty property) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhDefaultChargingItemProperties.class));
        property.setId(id);
        property.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        property.setCreateUid(UserContext.current().getUser().getId());

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDefaultChargingItemProperties.class));
        EhDefaultChargingItemPropertiesDao dao = new EhDefaultChargingItemPropertiesDao(context.configuration());
        dao.insert(property);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDefaultChargingItemProperties.class, id);
    }

    @Override
    public DefaultChargingItem findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhDefaultChargingItemsDao dao = new EhDefaultChargingItemsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), DefaultChargingItem.class);
    }

    @Override
    public List<DefaultChargingItem> listDefaultChargingItems(Integer namespaceId, Long communityId, String ownerType, Long ownerId, Long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        //修复缺陷 #45399 【智富汇】【缴费管理】计价条款异常，加categoryId
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_DEFAULT_CHARGING_ITEMS);
        query.addJoin(Tables.EH_PAYMENT_BILL_GROUPS, Tables.EH_DEFAULT_CHARGING_ITEMS.BILL_GROUP_ID.eq(Tables.EH_PAYMENT_BILL_GROUPS.ID));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(categoryId));

        List<DefaultChargingItem> result = new ArrayList<>();
        query.fetch().map((r) -> {
        	DefaultChargingItem dto = new DefaultChargingItem();
        	dto.setId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.ID));
        	dto.setChargingItemId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_ITEM_ID));
            dto.setChargingStandardId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_STANDARD_ID));
        	dto.setFormula(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.FORMULA));
            dto.setFormulaType(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.FORMULA_TYPE));
        	dto.setBillingCycle(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.BILLING_CYCLE));
        	dto.setChargingVariables(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES));
        	dto.setChargingStartTime(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_START_TIME));
        	dto.setChargingExpiredTime(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_EXPIRED_TIME));
        	dto.setStatus(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.STATUS));
        	dto.setBillGroupId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.BILL_GROUP_ID));
            result.add(dto);
            return null;
        });

        return result;
    }

    @Override
    public void updateDefaultChargingItem(DefaultChargingItem defaultChargingItem) {
        assert(defaultChargingItem.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDefaultChargingItems.class));
        EhDefaultChargingItemsDao dao = new EhDefaultChargingItemsDao(context.configuration());
        defaultChargingItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        defaultChargingItem.setOperatorUid(UserContext.current().getUser().getId());
        dao.update(defaultChargingItem);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDefaultChargingItems.class, defaultChargingItem.getId());
    }

    @Override
    public void updateDefaultChargingItemProperty(DefaultChargingItemProperty property) {
        assert(property.getId() != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhDefaultChargingItemProperties.class));
        EhDefaultChargingItemPropertiesDao dao = new EhDefaultChargingItemPropertiesDao(context.configuration());
        dao.update(property);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDefaultChargingItemProperties.class, property.getId());
    }

    @Override
    public List<DefaultChargingItemProperty> findByItemId(Long defaultChargingItemId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhDefaultChargingItemPropertiesRecord> query = context.selectQuery(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES);
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.DEFAULT_CHARGING_ITEM_ID.eq(defaultChargingItemId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<DefaultChargingItemProperty> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, DefaultChargingItemProperty.class));
            return null;
        });

        return result;
    }

    @Override
    public List<DefaultChargingItemProperty> findByPropertyId(Byte propertyType, Long propertyId, Byte meterType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.ID, Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.NAMESPACE_ID,
                Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.DEFAULT_CHARGING_ITEM_ID, Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.PROPERTY_TYPE,
                Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.PROPERTY_ID, Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.STATUS,
                Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.CREATE_UID, Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.CREATE_TIME);
        query.addFrom(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES);
        query.addJoin(Tables.EH_DEFAULT_CHARGING_ITEMS, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_DEFAULT_CHARGING_ITEMS.ID.eq(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.DEFAULT_CHARGING_ITEM_ID));

        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.PROPERTY_ID.eq(propertyId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.PROPERTY_TYPE.eq(propertyType));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.STATUS.eq(CommonStatus.ACTIVE.getCode()));
        if(meterType != null) {
            if(EnergyMeterType.ELECTRIC.equals(EnergyMeterType.fromCode(meterType))) {
                query.addJoin(Tables.EH_PAYMENT_CHARGING_ITEMS, JoinType.LEFT_OUTER_JOIN,
                        Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_ITEM_ID));
                query.addConditions(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME.eq("电费"));
            } else if(EnergyMeterType.WATER.equals(EnergyMeterType.fromCode(meterType))) {
                query.addJoin(Tables.EH_PAYMENT_CHARGING_ITEMS, JoinType.LEFT_OUTER_JOIN,
                        Tables.EH_PAYMENT_CHARGING_ITEMS.ID.eq(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_ITEM_ID));
                query.addConditions(Tables.EH_PAYMENT_CHARGING_ITEMS.NAME.eq("水费"));
            }
        }

        List<DefaultChargingItemProperty> result = new ArrayList<>();
        query.fetch().map((r) -> {
            DefaultChargingItemProperty property = new DefaultChargingItemProperty();
            property.setId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.ID));
            property.setNamespaceId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.NAMESPACE_ID));
            property.setDefaultChargingItemId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.DEFAULT_CHARGING_ITEM_ID));
            property.setPropertyType(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.PROPERTY_TYPE));
            property.setPropertyId(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.PROPERTY_ID));
            property.setStatus(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.STATUS));
            property.setCreateUid(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.CREATE_UID));
            property.setCreateTime(r.getValue(Tables.EH_DEFAULT_CHARGING_ITEM_PROPERTIES.CREATE_TIME));
            result.add(property);
            return null;
        });

        return result;
    }
}
