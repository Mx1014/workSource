package com.everhomes.organization.pm;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
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
import org.jooq.SelectQuery;
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
    public List<DefaultChargingItem> listDefaultChargingItems(Integer namespaceId, Long communityId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhDefaultChargingItemsRecord> query = context.selectQuery(Tables.EH_DEFAULT_CHARGING_ITEMS);
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_DEFAULT_CHARGING_ITEMS.STATUS.eq(CommonStatus.ACTIVE.getCode()));

        List<DefaultChargingItem> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, DefaultChargingItem.class));
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
}
