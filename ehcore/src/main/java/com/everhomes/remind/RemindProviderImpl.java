package com.everhomes.remind;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.remind.RemindStatus;
import com.everhomes.rest.remind.ShareMemberSourceType;
import com.everhomes.rest.remind.SharingPersonDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRemindCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhRemindCategoryDefaultSharesDao;
import com.everhomes.server.schema.tables.daos.EhRemindDemoCreateLogsDao;
import com.everhomes.server.schema.tables.daos.EhRemindSettingsDao;
import com.everhomes.server.schema.tables.daos.EhRemindSharesDao;
import com.everhomes.server.schema.tables.daos.EhRemindsDao;
import com.everhomes.server.schema.tables.pojos.EhRemindCategories;
import com.everhomes.server.schema.tables.pojos.EhRemindCategoryDefaultShares;
import com.everhomes.server.schema.tables.pojos.EhRemindDemoCreateLogs;
import com.everhomes.server.schema.tables.pojos.EhRemindSettings;
import com.everhomes.server.schema.tables.pojos.EhRemindShares;
import com.everhomes.server.schema.tables.pojos.EhReminds;
import com.everhomes.server.schema.tables.records.EhRemindCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhRemindCategoryDefaultSharesRecord;
import com.everhomes.server.schema.tables.records.EhRemindDemoCreateLogsRecord;
import com.everhomes.server.schema.tables.records.EhRemindSettingsRecord;
import com.everhomes.server.schema.tables.records.EhRemindSharesRecord;
import com.everhomes.server.schema.tables.records.EhRemindsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.PaginationHelper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.apache.tools.ant.types.resources.comparators.Exists;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RemindProviderImpl implements RemindProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(RemindProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    @Cacheable(value = "GetRemindSettingById", key = "#id", unless = "#result == null")
    public RemindSetting getRemindSettingById(Integer id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        EhRemindSettingsDao dao = new EhRemindSettingsDao(context.configuration());
        EhRemindSettings ehRemindSettings = dao.findById(id);
        return ConvertHelper.convert(ehRemindSettings, RemindSetting.class);
    }

    @Override
    @Cacheable(value = "FindRemindSettings", unless = "#result.size() == 0")
    public List<RemindSetting> findRemindSettings() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindSettingsRecord> query = context.selectQuery(Tables.EH_REMIND_SETTINGS);
        query.addOrderBy(Tables.EH_REMIND_SETTINGS.DEFAULT_ORDER.asc());
        Result<EhRemindSettingsRecord> results = query.fetch();

        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> ConvertHelper.convert(r, RemindSetting.class));
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindRemindSettings", allEntries = true)
            , @CacheEvict(value = "GetRemindSettingById", allEntries = true)})
    public void evictRemindSettingsCache() {
    }

    @Override
    public boolean checkCategoryNameExist(CheckRemindCategoryNameExistRequest request) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindCategoriesRecord> query = context.selectQuery(Tables.EH_REMIND_CATEGORIES);
        query.addConditions(Tables.EH_REMIND_CATEGORIES.NAMESPACE_ID.eq(request.getNamespaceId()));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_TYPE.eq(request.getOwnerType()));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_ID.eq(request.getOwnerId()));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(request.getUserId()));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.NAME.eq(request.getName()));
        if (request.getId() != null) {
            query.addConditions(Tables.EH_REMIND_CATEGORIES.ID.ne(request.getId()));
        }
        return query.fetchCount() > 0;
    }

    @Override
    public Integer getNextCategoryDefaultOrder(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_REMIND_CATEGORIES.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_REMIND_CATEGORIES.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_REMIND_CATEGORIES.OWNER_ID.eq(ownerId));
        condition = condition.and(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(userId));

        SelectConditionStep<Record1<Integer>> query = context.select(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.max().add(1)).from(Tables.EH_REMIND_CATEGORIES).where(condition);

        Record1<Integer> result = query.fetchOne();

        if (result != null) {
            return result.value1();
        }
        return null;
    }

    @Override
    public void sortRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId, Integer currentOrder, Integer targetOrder) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategories.class));
        UpdateQuery<EhRemindCategoriesRecord> updateQuery = context.updateQuery(Tables.EH_REMIND_CATEGORIES);
        updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_TYPE.eq(ownerType));
        updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_ID.eq(ownerId));
        updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(userId));
        if (Integer.compare(currentOrder, targetOrder) > 0) {
            updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.lt(currentOrder));
            updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.ge(targetOrder));
            updateQuery.addValue(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER, Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.add(1));
        } else {
            updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.gt(currentOrder));
            updateQuery.addConditions(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.le(targetOrder));
            updateQuery.addValue(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER, Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.add(-1));
        }
        updateQuery.execute();
    }

    @Override
    public void sortReminds(Integer namespaceId, String ownerType, Long ownerId, Long userId, Integer currentOrder, Integer targetOrder) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhReminds.class));
        UpdateQuery<EhRemindsRecord> updateQuery = context.updateQuery(Tables.EH_REMINDS);
        updateQuery.addConditions(Tables.EH_REMINDS.NAMESPACE_ID.eq(namespaceId));
        updateQuery.addConditions(Tables.EH_REMINDS.OWNER_TYPE.eq(ownerType));
        updateQuery.addConditions(Tables.EH_REMINDS.OWNER_ID.eq(ownerId));
        updateQuery.addConditions(Tables.EH_REMINDS.USER_ID.eq(userId));
        if (Integer.compare(currentOrder, targetOrder) > 0) {
            updateQuery.addConditions(Tables.EH_REMINDS.DEFAULT_ORDER.lt(currentOrder));
            updateQuery.addConditions(Tables.EH_REMINDS.DEFAULT_ORDER.ge(targetOrder));
            updateQuery.addValue(Tables.EH_REMINDS.DEFAULT_ORDER, Tables.EH_REMINDS.DEFAULT_ORDER.add(1));
        } else {
            updateQuery.addConditions(Tables.EH_REMINDS.DEFAULT_ORDER.gt(currentOrder));
            updateQuery.addConditions(Tables.EH_REMINDS.DEFAULT_ORDER.le(targetOrder));
            updateQuery.addValue(Tables.EH_REMINDS.DEFAULT_ORDER, Tables.EH_REMINDS.DEFAULT_ORDER.add(-1));
        }
        updateQuery.execute();
    }

    @Override
    public List<RemindCategory> findRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindCategoriesRecord> query = context.selectQuery(Tables.EH_REMIND_CATEGORIES);
        query.addConditions(Tables.EH_REMIND_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(userId));
        query.addOrderBy(Tables.EH_REMIND_CATEGORIES.DEFAULT_ORDER.desc());

        Result<EhRemindCategoriesRecord> results = query.fetch();
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        return results.map(r -> {
            return ConvertHelper.convert(r, RemindCategory.class);
        });
    }

    @Override
    public Long createRemindCategory(RemindCategory remindCategory) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRemindCategories.class));
        remindCategory.setId(id);
        remindCategory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        remindCategory.setCreatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategories.class));
        EhRemindCategoriesDao dao = new EhRemindCategoriesDao(context.configuration());
        dao.insert(remindCategory);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRemindCategories.class, null);

        return id;
    }

    @Override
    public Long updateRemindCategory(RemindCategory remindCategory) {
        remindCategory.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        remindCategory.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategories.class));
        EhRemindCategoriesDao dao = new EhRemindCategoriesDao(context.configuration());
        dao.update(remindCategory);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRemindCategories.class, remindCategory.getId());

        return remindCategory.getId();
    }

    @Override
    public void deleteRemindCategory(RemindCategory remindCategory) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategories.class));
        EhRemindCategoriesDao dao = new EhRemindCategoriesDao(context.configuration());
        dao.delete(remindCategory);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRemindCategories.class, remindCategory.getId());
    }

    @Override
    public RemindCategory getRemindCategory(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long remindCategoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindCategoriesRecord> query = context.selectQuery(Tables.EH_REMIND_CATEGORIES);
        query.addConditions(Tables.EH_REMIND_CATEGORIES.ID.eq(remindCategoryId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(userId));
        EhRemindCategoriesRecord remindCategory = query.fetchOne();
        return ConvertHelper.convert(remindCategory, RemindCategory.class);
    }

    @Override
    public int countUserRemindCategories(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindCategoriesRecord> query = context.selectQuery(Tables.EH_REMIND_CATEGORIES);
        query.addConditions(Tables.EH_REMIND_CATEGORIES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(userId));
        return query.fetchCount();
    }

    @Override
    public void batchCreateRemindCategoryDefaultShare(List<EhRemindCategoryDefaultShares> defaultShares) {
        if (CollectionUtils.isEmpty(defaultShares)) {
            return;
        }
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        for (EhRemindCategoryDefaultShares defaultShare : defaultShares) {
            long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRemindCategoryDefaultShares.class));
            defaultShare.setId(id);
            defaultShare.setCreateTime(now);
            defaultShare.setCreatorUid(UserContext.currentUserId());
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategoryDefaultShares.class));
        EhRemindCategoryDefaultSharesDao dao = new EhRemindCategoryDefaultSharesDao(context.configuration());
        dao.insert(defaultShares);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRemindCategoryDefaultShares.class, null);
    }

    @Override
    public void deleteRemindCategoryDefaultSharesByCategoryId(Long remindCategoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategoryDefaultShares.class));
        DeleteConditionStep<EhRemindCategoryDefaultSharesRecord> delete = context.delete(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES)
                .where(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.REMIND_CATEGORY_ID.eq(remindCategoryId));
        delete.execute();
    }

    @Override
    public void batchDeleteRemindCategoryDefaultShares(List<EhRemindCategoryDefaultShares> remindCategoryDefaultShares) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindCategoryDefaultShares.class));
        EhRemindCategoryDefaultSharesDao dao = new EhRemindCategoryDefaultSharesDao(context.configuration());
        dao.delete(remindCategoryDefaultShares);
    }

    @Override
    public List<RemindCategoryDefaultShare> findShareMemberDetailsByCategoryId(Long categoryId) {
    	if(null == categoryId)
            return Collections.emptyList();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES).where(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.REMIND_CATEGORY_ID.eq(categoryId));
        Result<Record> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> {
                return ConvertHelper.convert(r, RemindCategoryDefaultShare.class);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public Remind getRemindDetail(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long remindId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindsRecord> query = context.selectQuery(Tables.EH_REMINDS);
        query.addConditions(Tables.EH_REMINDS.ID.eq(remindId));
        query.addConditions(Tables.EH_REMINDS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_REMINDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_REMINDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_REMINDS.USER_ID.eq(userId));
        EhRemindsRecord remind = query.fetchOne();
        return ConvertHelper.convert(remind, Remind.class);
    }

    @Override
    public boolean checkRemindShareToUser(Long memberDetailId, Long remindId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindSharesRecord> query = context.selectQuery(Tables.EH_REMIND_SHARES);
        query.addConditions(Tables.EH_REMIND_SHARES.SHARED_SOURCE_ID.eq(memberDetailId));
        query.addConditions(Tables.EH_REMIND_SHARES.SHARED_SOURCE_TYPE.eq(ShareMemberSourceType.MEMBER_DETAIL.getCode()));
        query.addConditions(Tables.EH_REMIND_SHARES.REMIND_ID.eq(remindId));
        return query.fetchCount() > 0;
    }

    @Override
    public Remind getSubscribeRemind(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long subscribeRemindId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindsRecord> query = context.selectQuery(Tables.EH_REMINDS);
        query.addConditions(Tables.EH_REMINDS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_REMINDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_REMINDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_REMINDS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_REMINDS.TRACK_REMIND_ID.eq(subscribeRemindId));
        query.addLimit(1);
        EhRemindsRecord remind = query.fetchOne();
        return ConvertHelper.convert(remind, Remind.class);
    }

    @Override
    public Long createRemind(Remind remind) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhReminds.class));
        remind.setId(id);
        remind.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        remind.setCreatorUid(UserContext.currentUserId());
        remind.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        remind.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhReminds.class));
        EhRemindsDao dao = new EhRemindsDao(context.configuration());
        dao.insert(remind);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhReminds.class, null);

        return id;
    }

    @Override
    public Long updateRemind(Remind remind) {
        remind.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        remind.setOperatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhReminds.class));
        EhRemindsDao dao = new EhRemindsDao(context.configuration());
        dao.update(remind);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReminds.class, remind.getId());

        return remind.getId();
    }

    @Override
    public void deleteRemind(Remind remind) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhReminds.class));
        EhRemindsDao dao = new EhRemindsDao(context.configuration());
        dao.delete(remind);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhReminds.class, remind.getId());
    }

    @Override
    public Integer getNextRemindDefaultOrder(Integer namespaceId, String ownerType, Long ownerId, Long userId, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_REMINDS.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_REMINDS.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_REMINDS.OWNER_ID.eq(ownerId));
        condition = condition.and(Tables.EH_REMINDS.USER_ID.eq(userId));
        if (status != null) {
            condition = condition.and(Tables.EH_REMINDS.STATUS.eq(status));
        }

        SelectConditionStep<Record1<Integer>> query = context.select(Tables.EH_REMINDS.DEFAULT_ORDER.max().add(1)).from(Tables.EH_REMINDS).where(condition);

        Record1<Integer> result = query.fetchOne();

        if (result != null) {
            return result.value1();
        }
        return null;
    }

    @Override
    public List<RemindShare> findShareMemberDetailsByRemindId(Long remindId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_REMIND_SHARES).where(Tables.EH_REMIND_SHARES.REMIND_ID.eq(remindId));
        Result<Record> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> {
                return ConvertHelper.convert(r, RemindShare.class);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public List<SharingPersonDTO> findSharingPersonsBySourceId(Integer namespaceId, String ownerType, Long ownerId, String sourceType, Long sourceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        Condition condition = Tables.EH_REMIND_SHARES.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_REMIND_SHARES.OWNER_TYPE.eq(ownerType));
        condition = condition.and(Tables.EH_REMIND_SHARES.OWNER_ID.eq(ownerId));
        condition = condition.and(Tables.EH_REMIND_SHARES.SHARED_SOURCE_TYPE.eq(sourceType));
        condition = condition.and(Tables.EH_REMIND_SHARES.SHARED_SOURCE_ID.eq(sourceId));

        SelectConditionStep<Record2<Long, String>> query = context.selectDistinct(Tables.EH_REMIND_SHARES.OWNER_USER_ID, Tables.EH_REMIND_SHARES.OWNER_CONTRACT_NAME)
        		.from(Tables.EH_REMIND_SHARES).where(condition);

        Result<Record2<Long, String>> records = query.fetch();

        Set<SharingPersonDTO> results = new HashSet<>();
        if (records != null && records.size() > 0) {
        	results.addAll(records.map(r -> {
                SharingPersonDTO sharingPersonDTO = new SharingPersonDTO();
                sharingPersonDTO.setUserId(r.value1());
                sharingPersonDTO.setContractName(r.value2());
                return sharingPersonDTO;
            	}));
        }
        //增加找分类共享人逻辑
        query = context.selectDistinct(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID, Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME)
                .from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .where(DSL.exists(DSL.selectOne()
                		.from(Tables.EH_REMIND_CATEGORIES)
                		.join(Tables.EH_REMINDS).on(Tables.EH_REMINDS.REMIND_CATEGORY_ID.eq(Tables.EH_REMIND_CATEGORIES.ID))
                		.join(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES).on(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.REMIND_CATEGORY_ID.eq(Tables.EH_REMIND_CATEGORIES.ID))
                		.where(Tables.EH_REMIND_CATEGORIES.USER_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID))
                		.and(Tables.EH_REMINDS.OWNER_TYPE.eq(ownerType))
                		.and(Tables.EH_REMINDS.OWNER_ID.eq(ownerId))
                        .and(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.SHARED_SOURCE_TYPE.eq(sourceType))
                        .and(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.SHARED_SOURCE_ID.eq(sourceId))));
        LOGGER.debug("新增分类共享人 query : "+query);
        records = query.fetch();
        
        if (records != null && records.size() > 0) {
        	results.addAll(records.map(r -> {
                SharingPersonDTO sharingPersonDTO = new SharingPersonDTO();
                sharingPersonDTO.setUserId(r.value1());
                sharingPersonDTO.setContractName(r.value2());
                return sharingPersonDTO;
            	}));
        }
		return new ArrayList<>(results) ;
    }

    @Override
    public void deleteRemindSharesByRemindId(Long remindId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindShares.class));
        DeleteConditionStep<EhRemindSharesRecord> delete = context.delete(Tables.EH_REMIND_SHARES)
                .where(Tables.EH_REMIND_SHARES.REMIND_ID.eq(remindId));
        delete.execute();
    }

    @Override
    public void batchCreateRemindShare(List<EhRemindShares> remindShares) {
        if (CollectionUtils.isEmpty(remindShares)) {
            return;
        }
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        for (EhRemindShares remindShare : remindShares) {
            long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRemindShares.class));
            remindShare.setId(id);
            remindShare.setCreateTime(now);
            remindShare.setCreatorUid(UserContext.currentUserId());
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindShares.class));
        EhRemindSharesDao dao = new EhRemindSharesDao(context.configuration());
        dao.insert(remindShares);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRemindShares.class, null);
    }

    @Override
    public List<Remind> findRemindsByCategoryId(Long remindCategoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_REMINDS).where(Tables.EH_REMINDS.REMIND_CATEGORY_ID.eq(remindCategoryId));
        Result<Record> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> {
                return ConvertHelper.convert(r, Remind.class);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public void deleteRemindsByCategoryId(Long remindCategoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhReminds.class));
        DeleteConditionStep<EhRemindsRecord> delete = context.delete(Tables.EH_REMINDS)
                .where(Tables.EH_REMINDS.REMIND_CATEGORY_ID.eq(remindCategoryId));
        delete.execute();
    }

    @Override
    public List<Remind> findRemindsByTrackRemindIds(List<Long> trackRemindIds) {
        if (CollectionUtils.isEmpty(trackRemindIds)) {
            return Collections.emptyList();
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_REMINDS).where(Tables.EH_REMINDS.TRACK_REMIND_ID.in(trackRemindIds));
        Result<Record> result = query.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> {
                return ConvertHelper.convert(r, Remind.class);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public void deleteRemindsByTrackRemindId(List<Long> trackRemindIds) {
        if (CollectionUtils.isEmpty(trackRemindIds)) {
            return;
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhReminds.class));
        DeleteConditionStep<EhRemindsRecord> delete = context.delete(Tables.EH_REMINDS)
                .where(Tables.EH_REMINDS.TRACK_REMIND_ID.in(trackRemindIds));
        delete.execute();
    }

    @Override
    public List<Remind> findSelfReminds(QuerySelfRemindsCondition request) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindsRecord> query = context.selectQuery(Tables.EH_REMINDS);
        query.addConditions(Tables.EH_REMINDS.NAMESPACE_ID.eq(request.getNamespaceId()));
        query.addConditions(Tables.EH_REMINDS.OWNER_TYPE.eq(request.getOwnerType()));
        query.addConditions(Tables.EH_REMINDS.OWNER_ID.eq(request.getOwnerId()));
        query.addConditions(Tables.EH_REMINDS.USER_ID.eq(request.getUserId()));

        if (StringUtils.hasText(request.getKeyWord())) {
            query.addConditions(Tables.EH_REMINDS.PLAN_DESCRIPTION.like("%" + request.getKeyWord() + "%"));
        }
        if (request.getRemindCategoryId() != null && request.getRemindCategoryId() > 0) {
            query.addConditions(Tables.EH_REMINDS.REMIND_CATEGORY_ID.eq(request.getRemindCategoryId()));
        }
        if (request.getStatus() != null) {
            query.addConditions(Tables.EH_REMINDS.STATUS.eq(request.getStatus().byteValue()));
        }
        query.addOrderBy(Tables.EH_REMINDS.DEFAULT_ORDER.desc());
        if (request.getPageOffset() != null && request.getPageSize() != null) {
            int offset = (int) PaginationHelper.offsetFromPageOffset((long) request.getPageOffset(), request.getPageSize());
            query.addLimit(offset, request.getPageSize());
        }

        Result<EhRemindsRecord> results = query.fetch();
        if (results != null && results.size() > 0) {
            return results.map(r -> {
                return ConvertHelper.convert(r, Remind.class);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public List<Remind> findShareReminds(QueryShareRemindsCondition request) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> query = context.select().from(Tables.EH_REMINDS)
        		.where(Tables.EH_REMINDS.NAMESPACE_ID.eq(request.getNamespaceId()));
        query.and(Tables.EH_REMINDS.OWNER_TYPE.eq(request.getOwnerType()));
        query.and(Tables.EH_REMINDS.OWNER_ID.eq(request.getOwnerId()));
        //满足：存在于共享人列表 或者 存在于分类默认共享人列表
        Condition sharesExitstCondition = DSL.exists(DSL.DSL.selectOne().from(Tables.EH_REMIND_SHARES)
        		.where(Tables.EH_REMINDS.ID.eq(Tables.EH_REMIND_SHARES.REMIND_ID))
        		.and(Tables.EH_REMIND_SHARES.SHARED_SOURCE_ID.eq(request.getCurrentUserDetailId()))
        		.and(Tables.EH_REMIND_SHARES.SHARED_SOURCE_TYPE.eq(ShareMemberSourceType.MEMBER_DETAIL.getCode()))
        		.and(Tables.EH_REMIND_SHARES.OWNER_USER_ID.eq(request.getShareUserId())));

        Condition categorySharesExitstCondition = DSL.exists(DSL.DSL.selectOne().from(Tables.EH_REMIND_SHARES)
        		.where(Tables.EH_REMINDS.REMIND_CATEGORY_ID.eq(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.REMIND_CATEGORY_ID))
        		.and(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.SHARED_SOURCE_ID.eq(request.getCurrentUserDetailId()))
        		.and(Tables.EH_REMIND_CATEGORY_DEFAULT_SHARES.SHARED_SOURCE_TYPE.eq(ShareMemberSourceType.MEMBER_DETAIL.getCode())));
        query.and(sharesExitstCondition.or(categorySharesExitstCondition));
        query.orderBy(Tables.EH_REMINDS.CREATE_TIME.desc());
       
        if (StringUtils.hasText(request.getKeyWord())) {
            query.and(Tables.EH_REMINDS.PLAN_DESCRIPTION.like("%" + request.getKeyWord() + "%"));
        }
        if (request.getStatus() != null) {
            query.and(Tables.EH_REMINDS.STATUS.eq(request.getStatus().byteValue()));
        }

        if (request.getPageOffset() != null && request.getPageSize() != null) {
            int offset = (int) PaginationHelper.offsetFromPageOffset((long) request.getPageOffset(), request.getPageSize());
            query.limit(offset, request.getPageSize());
        }

        Result<EhRemindsRecord> results = query.fetchInto(Tables.EH_REMINDS.asTable());
        if (results != null && results.size() > 0) {
            return results.map(r -> {
                return ConvertHelper.convert(r, Remind.class);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public Long createRemindDemoCreateLog(RemindDemoCreateLog remindDemoCreateLog) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRemindDemoCreateLogs.class));
        remindDemoCreateLog.setId(id);
        remindDemoCreateLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        remindDemoCreateLog.setCreatorUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhRemindDemoCreateLogs.class));
        EhRemindDemoCreateLogsDao dao = new EhRemindDemoCreateLogsDao(context.configuration());
        dao.insert(remindDemoCreateLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRemindDemoCreateLogs.class, null);

        return id;
    }

    @Override
    public boolean checkRemindDemoHasCreated(Integer namespaceId, String ownerType, Long ownerId, Long userId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindDemoCreateLogsRecord> query = context.selectQuery(Tables.EH_REMIND_DEMO_CREATE_LOGS);
        query.addConditions(Tables.EH_REMIND_DEMO_CREATE_LOGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_REMIND_DEMO_CREATE_LOGS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_REMIND_DEMO_CREATE_LOGS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_REMIND_DEMO_CREATE_LOGS.USER_ID.eq(userId));
        return query.fetchCount() > 0;
    }
    @Override
    public List<Remind> findUndoRemindsByRemindTimeByPage(Timestamp remindStartTime,
			Timestamp remindEndTime, int pageSize, int offset){
		        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		        SelectQuery<EhRemindsRecord> query = context.selectQuery(Tables.EH_REMINDS);
		        query.addConditions(Tables.EH_REMINDS.REMIND_TIME.greaterOrEqual(remindStartTime));
		        query.addConditions(Tables.EH_REMINDS.REMIND_TIME.lessOrEqual(remindEndTime));
		        query.addConditions(Tables.EH_REMINDS.STATUS.eq(RemindStatus.UNDO.getCode()));
		        query.addConditions(Tables.EH_REMINDS.REMIND_CATEGORY_ID.gt(Long.valueOf(0)));
		        query.addConditions(Tables.EH_REMINDS.ACT_REMIND_TIME.isNull());

		        query.addLimit(offset, pageSize);

		        Result<EhRemindsRecord> result = query.fetch();

		        if (result == null || result.isEmpty()) {
		            return Collections.emptyList();
		        }

		        return result.map((r) -> {
		            return ConvertHelper.convert(r, Remind.class);
		        });
		    }
    @Override
    public List<Remind> findUndoRemindsByRemindTime(Timestamp remindStartTime, Timestamp remindEndTime, int count) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhRemindsRecord> query = context.selectQuery(Tables.EH_REMINDS);
        query.addConditions(Tables.EH_REMINDS.REMIND_TIME.greaterOrEqual(remindStartTime));
        query.addConditions(Tables.EH_REMINDS.REMIND_TIME.lessOrEqual(remindEndTime));
        query.addConditions(Tables.EH_REMINDS.STATUS.eq(RemindStatus.UNDO.getCode()));
        query.addConditions(Tables.EH_REMINDS.REMIND_CATEGORY_ID.gt(Long.valueOf(0)));
        query.addConditions(Tables.EH_REMINDS.ACT_REMIND_TIME.isNull());

        query.addLimit(count);

        Result<EhRemindsRecord> result = query.fetch();

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        return result.map((r) -> {
            return ConvertHelper.convert(r, Remind.class);
        });
    }
    
    @Override
    public Remind getRemindById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhRemindsDao dao = new EhRemindsDao(context.configuration());
        EhReminds remind = dao.findById(id);
        return ConvertHelper.convert(remind, Remind.class);
    }
}
