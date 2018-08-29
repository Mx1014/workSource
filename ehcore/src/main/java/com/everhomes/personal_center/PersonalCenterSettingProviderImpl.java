package com.everhomes.personal_center;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.rest.personal_center.PersonalCenterSettingStatus;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPersonalCenterSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhPersonalCenterSettings;
import com.everhomes.server.schema.tables.records.EhPersonalCenterSettingsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class PersonalCenterSettingProviderImpl implements PersonalCenterSettingProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createPersonalCenterSetting(PersonalCenterSetting obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPersonalCenterSettings.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));
        obj.setId(id);
        prepareObj(obj);
        EhPersonalCenterSettingsDao dao = new EhPersonalCenterSettingsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updatePersonalCenterSetting(PersonalCenterSetting obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));
        EhPersonalCenterSettingsDao dao = new EhPersonalCenterSettingsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deletePersonalCenterSetting(PersonalCenterSetting obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));
        EhPersonalCenterSettingsDao dao = new EhPersonalCenterSettingsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public PersonalCenterSetting getPersonalCenterSettingById(Long id) {
        try {
        PersonalCenterSetting[] result = new PersonalCenterSetting[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));

        result[0] = context.select().from(Tables.EH_PERSONAL_CENTER_SETTINGS)
            .where(Tables.EH_PERSONAL_CENTER_SETTINGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, PersonalCenterSetting.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<PersonalCenterSetting> queryPersonalCenterSettings(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));

        SelectQuery<EhPersonalCenterSettingsRecord> query = context.selectQuery(Tables.EH_PERSONAL_CENTER_SETTINGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<PersonalCenterSetting> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PersonalCenterSetting.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    @Override
    public List<PersonalCenterSetting> queryActivePersonalCenterSettings(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));

        SelectQuery<EhPersonalCenterSettingsRecord> query = context.selectQuery(Tables.EH_PERSONAL_CENTER_SETTINGS);
        query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.STATUS.eq(PersonalCenterSettingStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_PERSONAL_CENTER_SETTINGS.REGION.asc());
        query.addOrderBy(Tables.EH_PERSONAL_CENTER_SETTINGS.SORT_NUM.asc());
        List<PersonalCenterSetting> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PersonalCenterSetting.class);
        });
        return objs;
    }

    @Override
    public List<PersonalCenterSetting> queryDefaultPersonalCenterSettings() {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));

        SelectQuery<EhPersonalCenterSettingsRecord> query = context.selectQuery(Tables.EH_PERSONAL_CENTER_SETTINGS);
        query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.NAMESPACE_ID.eq(0));
        query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.STATUS.eq(PersonalCenterSettingStatus.ACTIVE.getCode()));
        query.addOrderBy(Tables.EH_PERSONAL_CENTER_SETTINGS.REGION.asc());
        query.addOrderBy(Tables.EH_PERSONAL_CENTER_SETTINGS.SORT_NUM.asc());
        List<PersonalCenterSetting> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PersonalCenterSetting.class);
        });
        return objs;
    }

    @Override
    public List<PersonalCenterSetting> queryPersonalCenterSettingsByNamespaceIdAndVersion(Integer namespaceId, Integer version) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPersonalCenterSettings.class));

        SelectQuery<EhPersonalCenterSettingsRecord> query = context.selectQuery(Tables.EH_PERSONAL_CENTER_SETTINGS);
        query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_PERSONAL_CENTER_SETTINGS.VERSION.eq(version));
        query.addOrderBy(Tables.EH_PERSONAL_CENTER_SETTINGS.REGION.asc());
        query.addOrderBy(Tables.EH_PERSONAL_CENTER_SETTINGS.SORT_NUM.asc());
        List<PersonalCenterSetting> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, PersonalCenterSetting.class);
        });
        return objs;
    }

    @Override
    public int countPersonalCenterSettingVersion(Integer namespaceId, Timestamp dayStartTime, Timestamp dayEndTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectJoinStep<Record1<Integer>> step = context.select(DSL.countDistinct(Tables.EH_PERSONAL_CENTER_SETTINGS.VERSION)).from(Tables.EH_PERSONAL_CENTER_SETTINGS);
        Condition condition = Tables.EH_PERSONAL_CENTER_SETTINGS.NAMESPACE_ID.eq(namespaceId);
        condition.and(Tables.EH_PERSONAL_CENTER_SETTINGS.CREATE_TIME.ge(dayStartTime)).and(Tables.EH_PERSONAL_CENTER_SETTINGS.CREATE_TIME.le(dayEndTime));
        return step.where(condition).fetchOneInto(Integer.class);
    }

    private void prepareObj(PersonalCenterSetting obj) {
    }
}
