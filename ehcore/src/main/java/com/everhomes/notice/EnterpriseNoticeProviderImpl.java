package com.everhomes.notice;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.notice.EnterpriseNoticeStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseNoticeAttachmentsDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseNoticeReceiversDao;
import com.everhomes.server.schema.tables.daos.EhEnterpriseNoticesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseNoticeAttachments;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseNoticeReceivers;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseNotices;
import com.everhomes.server.schema.tables.records.EhEnterpriseNoticeAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseNoticeReceiversRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Record;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectForUpdateStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SortOrder;
import org.jooq.UpdateConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Repository
public class EnterpriseNoticeProviderImpl implements EnterpriseNoticeProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;


    @Override
    @Cacheable(value = "FindEnterpriseNoticeById", key = "#id", unless = "#result == null")
    public EnterpriseNotice findById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record> sql = context.select().from(Tables.EH_ENTERPRISE_NOTICES)
                .where(Tables.EH_ENTERPRISE_NOTICES.ID.eq(id))
                .and(Tables.EH_ENTERPRISE_NOTICES.STATUS.ne(EnterpriseNoticeStatus.DELETED.getCode()));

        Record record = sql.fetchOne();
        return ConvertHelper.convert(record, EnterpriseNotice.class);
    }

    @Override
    public void createEnterpriseNotice(EnterpriseNotice enterpriseNotice) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhEnterpriseNotices.class));

        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        enterpriseNotice.setId(id);
        enterpriseNotice.setCreateTime(now);
        enterpriseNotice.setCreatorUid(UserContext.currentUserId());
        enterpriseNotice.setUpdateTime(now);
        enterpriseNotice.setUpdateUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseNoticesDao dao = new EhEnterpriseNoticesDao(context.configuration());
        dao.insert(enterpriseNotice);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseNotices.class, null);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindEnterpriseNoticeById", key = "#enterpriseNotice.id")})
    public void updateEnterpriseNotice(EnterpriseNotice enterpriseNotice) {
        if (EnterpriseNoticeStatus.DELETED == EnterpriseNoticeStatus.fromCode(enterpriseNotice.getStatus())) {
            enterpriseNotice.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            enterpriseNotice.setDeleteUid(UserContext.currentUserId());
        } else {
            enterpriseNotice.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            enterpriseNotice.setUpdateUid(UserContext.currentUserId());
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseNoticesDao dao = new EhEnterpriseNoticesDao(context.configuration());
        dao.update(enterpriseNotice);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterpriseNotices.class, enterpriseNotice.getId());
    }

    @Override
    public List<EnterpriseNotice> listEnterpriseNoticesByNamespaceId(Integer namespaceId, Long organizationId, Integer offset, Integer pageSize) {
        SelectForUpdateStep<Record12<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, String, Byte>> sql = baseQueryEnterpriseNoticesByNamespaceId(namespaceId, organizationId)
                .orderBy(Tables.EH_ENTERPRISE_NOTICES.STICK_FLAG.desc(), Tables.EH_ENTERPRISE_NOTICES.STICK_TIME.desc(), Tables.EH_ENTERPRISE_NOTICES.CREATE_TIME.sort(SortOrder.DESC)).limit(offset, pageSize);

        Result<Record12<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, String, Byte>> result = sql.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> {
                EnterpriseNotice enterpriseNotice = new EnterpriseNotice();
                enterpriseNotice.setId(r.getValue(Tables.EH_ENTERPRISE_NOTICES.ID));
                enterpriseNotice.setTitle(r.getValue(Tables.EH_ENTERPRISE_NOTICES.TITLE));
                enterpriseNotice.setSummary(r.getValue(Tables.EH_ENTERPRISE_NOTICES.SUMMARY));
                enterpriseNotice.setPublisher(r.getValue(Tables.EH_ENTERPRISE_NOTICES.PUBLISHER));
                enterpriseNotice.setSecretFlag(r.getValue(Tables.EH_ENTERPRISE_NOTICES.SECRET_FLAG));
                enterpriseNotice.setStatus(r.getValue(Tables.EH_ENTERPRISE_NOTICES.STATUS));
                enterpriseNotice.setCreatorUid(r.getValue(Tables.EH_ENTERPRISE_NOTICES.CREATOR_UID));
                enterpriseNotice.setCreateTime(r.getValue(Tables.EH_ENTERPRISE_NOTICES.CREATE_TIME));
                enterpriseNotice.setUpdateUid(r.getValue(Tables.EH_ENTERPRISE_NOTICES.UPDATE_UID));
                enterpriseNotice.setUpdateTime(r.getValue(Tables.EH_ENTERPRISE_NOTICES.UPDATE_TIME));
                enterpriseNotice.setOperatorName(r.getValue(Tables.EH_ENTERPRISE_NOTICES.OPERATOR_NAME));
                enterpriseNotice.setStickFlag(r.getValue(Tables.EH_ENTERPRISE_NOTICES.STICK_FLAG));
                return enterpriseNotice;
            });
        }
        return Collections.emptyList();
    }

    private SelectConditionStep<Record12<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, String, Byte>> baseQueryEnterpriseNoticesByNamespaceId(Integer namespaceId, Long organizationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectConditionStep<Record12<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, String, Byte>> baseSql = context.selectDistinct(
                Tables.EH_ENTERPRISE_NOTICES.ID,
                Tables.EH_ENTERPRISE_NOTICES.TITLE,
                Tables.EH_ENTERPRISE_NOTICES.SUMMARY,
                Tables.EH_ENTERPRISE_NOTICES.SECRET_FLAG,
                Tables.EH_ENTERPRISE_NOTICES.PUBLISHER,
                Tables.EH_ENTERPRISE_NOTICES.STATUS,
                Tables.EH_ENTERPRISE_NOTICES.CREATE_TIME,
                Tables.EH_ENTERPRISE_NOTICES.CREATOR_UID,
                Tables.EH_ENTERPRISE_NOTICES.UPDATE_TIME,
                Tables.EH_ENTERPRISE_NOTICES.UPDATE_UID,
                Tables.EH_ENTERPRISE_NOTICES.OPERATOR_NAME,
                Tables.EH_ENTERPRISE_NOTICES.STICK_FLAG)
                .from(Tables.EH_ENTERPRISE_NOTICES)
                .where(Tables.EH_ENTERPRISE_NOTICES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ENTERPRISE_NOTICES.OWNER_TYPE.eq(EntityType.ORGANIZATIONS.getCode()))
                .and(Tables.EH_ENTERPRISE_NOTICES.OWNER_ID.eq(organizationId))
                .and(Tables.EH_ENTERPRISE_NOTICES.STATUS.ne(EnterpriseNoticeStatus.DELETED.getCode()));
        return baseSql;
    }

    @Override
    public int totalCountEnterpriseNoticesByNamespaceId(Integer namespaceId, Long organizationId) {
        SelectConditionStep<Record12<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, String, Byte>> sql = baseQueryEnterpriseNoticesByNamespaceId(namespaceId, organizationId);
        return sql.fetchCount();
    }

    @Override
    public List<EnterpriseNotice> listEnterpriseNoticesByOwnerId(List<EnterpriseNoticeReceiver> owners, Integer namespaceId, Integer offset, Integer pageSize) {
        if (CollectionUtils.isEmpty(owners)) {
            return Collections.emptyList();
        }

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectOnConditionStep<Record11<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, Byte>> sql = context.selectDistinct(
                Tables.EH_ENTERPRISE_NOTICES.ID,
                Tables.EH_ENTERPRISE_NOTICES.TITLE,
                Tables.EH_ENTERPRISE_NOTICES.SUMMARY,
                Tables.EH_ENTERPRISE_NOTICES.SECRET_FLAG,
                Tables.EH_ENTERPRISE_NOTICES.PUBLISHER,
                Tables.EH_ENTERPRISE_NOTICES.STATUS,
                Tables.EH_ENTERPRISE_NOTICES.CREATE_TIME,
                Tables.EH_ENTERPRISE_NOTICES.CREATOR_UID,
                Tables.EH_ENTERPRISE_NOTICES.UPDATE_TIME,
                Tables.EH_ENTERPRISE_NOTICES.UPDATE_UID,
                Tables.EH_ENTERPRISE_NOTICES.STICK_FLAG)
                .from(Tables.EH_ENTERPRISE_NOTICES)
                .join(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS)
                .on(Tables.EH_ENTERPRISE_NOTICES.ID.eq(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.NOTICE_ID));

        Condition condition0 = Tables.EH_ENTERPRISE_NOTICES.NAMESPACE_ID.eq(namespaceId);
        Condition condition1 = Tables.EH_ENTERPRISE_NOTICES.STATUS.eq(EnterpriseNoticeStatus.ACTIVE.getCode());
        Condition condition2 = Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.RECEIVER_TYPE.eq(owners.get(0).getReceiverType()).and(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.RECEIVER_ID.eq(owners.get(0).getReceiverId()));
        for (int i = 1; i < owners.size(); i++) {
            Condition condition = Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.RECEIVER_TYPE.eq(owners.get(i).getReceiverType()).and(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.RECEIVER_ID.eq(owners.get(i).getReceiverId()));
            condition2 = condition2.or(condition);
        }
        Condition allCondition = condition0.and(condition1).and(condition2);

        sql.where(allCondition).orderBy(Tables.EH_ENTERPRISE_NOTICES.STICK_FLAG.desc(), Tables.EH_ENTERPRISE_NOTICES.STICK_TIME.desc(), Tables.EH_ENTERPRISE_NOTICES.UPDATE_TIME.sort(SortOrder.DESC)).limit(offset, pageSize);

        Result<Record11<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, Byte>> result = sql.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> {
                return buildEnterpriseNoticeSimpleInfo(r);
            });
        }
        return Collections.emptyList();
    }

    private EnterpriseNotice buildEnterpriseNoticeSimpleInfo(Record11<Long, String, String, Byte, String, Byte, Timestamp, Long, Timestamp, Long, Byte> r) {
        EnterpriseNotice enterpriseNotice = new EnterpriseNotice();
        enterpriseNotice.setId(r.getValue(Tables.EH_ENTERPRISE_NOTICES.ID));
        enterpriseNotice.setTitle(r.getValue(Tables.EH_ENTERPRISE_NOTICES.TITLE));
        enterpriseNotice.setSummary(r.getValue(Tables.EH_ENTERPRISE_NOTICES.SUMMARY));
        enterpriseNotice.setPublisher(r.getValue(Tables.EH_ENTERPRISE_NOTICES.PUBLISHER));
        enterpriseNotice.setSecretFlag(r.getValue(Tables.EH_ENTERPRISE_NOTICES.SECRET_FLAG));
        enterpriseNotice.setStatus(r.getValue(Tables.EH_ENTERPRISE_NOTICES.STATUS));
        enterpriseNotice.setCreatorUid(r.getValue(Tables.EH_ENTERPRISE_NOTICES.CREATOR_UID));
        enterpriseNotice.setCreateTime(r.getValue(Tables.EH_ENTERPRISE_NOTICES.CREATE_TIME));
        enterpriseNotice.setUpdateUid(r.getValue(Tables.EH_ENTERPRISE_NOTICES.UPDATE_UID));
        enterpriseNotice.setUpdateTime(r.getValue(Tables.EH_ENTERPRISE_NOTICES.UPDATE_TIME));
        enterpriseNotice.setStickFlag(r.getValue(Tables.EH_ENTERPRISE_NOTICES.STICK_FLAG));
        return enterpriseNotice;
    }

    @Override
    public void createEnterpriseNoticeAttachment(EnterpriseNoticeAttachment attachment) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhEnterpriseNoticeAttachments.class));

        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        attachment.setCreatorUid(UserContext.currentUserId());
        attachment.setStatus(EnterpriseNoticeAttachmentStatus.VALID.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseNoticeAttachmentsDao dao = new EhEnterpriseNoticeAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseNoticeAttachments.class, null);
    }

    @Override
    public void createEnterpriseNoticeReceiver(EnterpriseNoticeReceiver receiver) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhEnterpriseNoticeReceivers.class));

        receiver.setId(id);
        receiver.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        receiver.setCreatorUid(UserContext.currentUserId());
        receiver.setStatus(EnterpriseNoticeReceiverStatus.VALID.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhEnterpriseNoticeReceiversDao dao = new EhEnterpriseNoticeReceiversDao(context.configuration());
        dao.insert(receiver);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterpriseNoticeReceivers.class, null);
    }

    @Override
    @Cacheable(value = "FindEnterpriseNoticeAttachmentsByNoticeId", key = "#enterpriseNoticeId", unless = "#result.size() == 0")
    public List<EnterpriseNoticeAttachment> findEnterpriseNoticeAttachmentsByNoticeId(Long enterpriseNoticeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectConditionStep<Record> sql = context.select().from(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS)
                .where(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS.NOTICE_ID.eq(enterpriseNoticeId))
                .and(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS.STATUS.eq(EnterpriseNoticeAttachmentStatus.VALID.getCode()));

        Result<Record> result = sql.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> ConvertHelper.convert(r, EnterpriseNoticeAttachment.class));
        }
        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "FindEnterpriseNoticeReceiversByNoticeId", key = "#enterpriseNoticeId", unless = "#result.size() == 0")
    public List<EnterpriseNoticeReceiver> findEnterpriseNoticeReceiversByNoticeId(Long enterpriseNoticeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectConditionStep<Record> sql = context.select().from(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS)
                .where(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.NOTICE_ID.eq(enterpriseNoticeId))
                .and(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.STATUS.eq(EnterpriseNoticeReceiverStatus.VALID.getCode()));

        Result<Record> result = sql.fetch();
        if (result != null && result.size() > 0) {
            return result.map(r -> ConvertHelper.convert(r, EnterpriseNoticeReceiver.class));
        }
        return Collections.emptyList();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindEnterpriseNoticeAttachmentsByNoticeId", key = "#enterpriseNoticeId")})
    public void logicDeleteEnterpriseNoticeAttachmentsByNoticeId(Long enterpriseNoticeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateConditionStep<EhEnterpriseNoticeAttachmentsRecord> sql = context.update(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS)
                .set(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS.STATUS, EnterpriseNoticeAttachmentStatus.INVALID.getCode())
                .where(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS.NOTICE_ID.eq(enterpriseNoticeId));
        sql.execute();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindEnterpriseNoticeReceiversByNoticeId", key = "#enterpriseNoticeId")})
    public void logicDeleteEnterpriseNoticeReceiversByNoticeId(Long enterpriseNoticeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

        UpdateConditionStep<EhEnterpriseNoticeReceiversRecord> sql = context.update(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS)
                .set(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.STATUS, EnterpriseNoticeReceiverStatus.INVALID.getCode())
                .where(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.NOTICE_ID.eq(enterpriseNoticeId));
        sql.execute();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindEnterpriseNoticeAttachmentsByNoticeId", key = "#enterpriseNoticeId")})
    public void deleteEnterpriseNoticeAttachmentsByNoticeId(Long enterpriseNoticeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteConditionStep<EhEnterpriseNoticeAttachmentsRecord> sql = context.delete(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS)
                .where(Tables.EH_ENTERPRISE_NOTICE_ATTACHMENTS.NOTICE_ID.eq(enterpriseNoticeId));
        sql.execute();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "FindEnterpriseNoticeReceiversByNoticeId", key = "#enterpriseNoticeId")})
    public void deleteEnterpriseNoticeReceiversByNoticeId(Long enterpriseNoticeId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteConditionStep<EhEnterpriseNoticeReceiversRecord> sql = context.delete(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS)
                .where(Tables.EH_ENTERPRISE_NOTICE_RECEIVERS.NOTICE_ID.eq(enterpriseNoticeId));
        sql.execute();
    }
}
