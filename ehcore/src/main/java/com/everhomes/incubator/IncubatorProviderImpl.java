package com.everhomes.incubator;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhIncubatorAppliesDao;
import com.everhomes.server.schema.tables.daos.EhIncubatorApplyAttachmentsDao;
import com.everhomes.server.schema.tables.pojos.EhIncubatorApplies;
import com.everhomes.server.schema.tables.pojos.EhIncubatorApplyAttachments;
import com.everhomes.server.schema.tables.records.EhIncubatorAppliesRecord;
import com.everhomes.server.schema.tables.records.EhIncubatorApplyAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhIncubatorProjectTypesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class IncubatorProviderImpl implements IncubatorProvider {

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createIncubatorApply(IncubatorApply incubatorApply) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhIncubatorApplies.class));
        incubatorApply.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhIncubatorAppliesDao dao = new EhIncubatorAppliesDao(context.configuration());
        dao.insert(incubatorApply);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhIncubatorApplies.class, null);
    }

    @Override
    public void updateIncubatorApply(IncubatorApply incubatorApply) {
        assert(incubatorApply.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhIncubatorAppliesDao dao = new EhIncubatorAppliesDao(context.configuration());
        dao.update(incubatorApply);

        DaoHelper.publishDaoAction(DaoAction.MODIFY,EhIncubatorApplies.class, incubatorApply.getId());
    }

    @Override
    public List<IncubatorApply> listIncubatorApplies(Integer namespaceId, Long applyUserId, String keyWord, Byte approveStatus, Byte needReject, Integer pageOffset, Integer pageSize, Byte orderBy) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhIncubatorAppliesRecord> query = context.selectQuery(Tables.EH_INCUBATOR_APPLIES);

        if(namespaceId != null){
            query.addConditions(Tables.EH_INCUBATOR_APPLIES.NAMESPACE_ID.eq(namespaceId));
        }
        if(applyUserId != null){
            query.addConditions(Tables.EH_INCUBATOR_APPLIES.APPLY_USER_ID.eq(applyUserId));
        }

        //关键字搜索
        if(!StringUtils.isEmpty(keyWord)){
            Condition keyWordCondition = Tables.EH_INCUBATOR_APPLIES.PROJECT_NAME.contains(keyWord);
            keyWordCondition = keyWordCondition.or(Tables.EH_INCUBATOR_APPLIES.TEAM_NAME.contains(keyWord));
            keyWordCondition = keyWordCondition.or(Tables.EH_INCUBATOR_APPLIES.CHARGER_NAME.contains(keyWord));
            keyWordCondition = keyWordCondition.or(Tables.EH_INCUBATOR_APPLIES.CHARGER_PHONE.contains(keyWord));
            query.addConditions(keyWordCondition);
        }

        if(approveStatus != null){
            query.addConditions(Tables.EH_INCUBATOR_APPLIES.APPROVE_STATUS.eq(approveStatus));
        }
        if(needReject != null && needReject.byteValue() == 0){
            query.addConditions(Tables.EH_INCUBATOR_APPLIES.APPROVE_STATUS.ne((byte)1));
        }

        //排序 默认、0-创建时间，1-审核时间
        if(orderBy == null || orderBy == 0){
            query.addOrderBy(Tables.EH_INCUBATOR_APPLIES.CREATE_TIME.desc());
        }else if(orderBy == 1) {
            query.addOrderBy(Tables.EH_INCUBATOR_APPLIES.APPROVE_TIME.desc());
        }

        Integer offset = (pageOffset - 1 ) * (pageSize -1);

        query.addLimit(offset, pageSize);
        List<IncubatorApply> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, IncubatorApply.class));
            return null;
        });

        return result;
    }

    @Override
    public List<IncubatorProjectType> listIncubatorProjectType() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhIncubatorProjectTypesRecord> query = context.selectQuery(Tables.EH_INCUBATOR_PROJECT_TYPES);
        List<IncubatorProjectType> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, IncubatorProjectType.class));
            return null;
        });

        return result;
    }

    @Override
    public IncubatorApply findIncubatorApplyById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhIncubatorAppliesDao dao = new EhIncubatorAppliesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), IncubatorApply.class);
    }


    @Override
    public void createAttachment(IncubatorApplyAttachment attachment) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhIncubatorApplyAttachments.class));

        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhIncubatorApplyAttachmentsDao dao = new EhIncubatorApplyAttachmentsDao(context.configuration());
        dao.insert(attachment);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhIncubatorApplyAttachmentsDao.class, null);
    }


    @Override
    public IncubatorApplyAttachment findByAttachmentId(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhIncubatorApplyAttachmentsDao dao = new EhIncubatorApplyAttachmentsDao(context.configuration());
        EhIncubatorApplyAttachments result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, IncubatorApplyAttachment.class);
    }

    @Override
    public List<IncubatorApplyAttachment> listAttachmentsByApplyId(Long applyId, Byte type) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhIncubatorApplyAttachmentsRecord> query = context.selectQuery(Tables.EH_INCUBATOR_APPLY_ATTACHMENTS);

        query.addConditions(Tables.EH_INCUBATOR_APPLY_ATTACHMENTS.INCUBATOR_APPLY_ID.eq(applyId));
        if(type != null){
            query.addConditions(Tables.EH_INCUBATOR_APPLY_ATTACHMENTS.TYPE.eq(type));
        }

        List<IncubatorApplyAttachment> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, IncubatorApplyAttachment.class));
            return null;
        });

        return result;
    }
}
