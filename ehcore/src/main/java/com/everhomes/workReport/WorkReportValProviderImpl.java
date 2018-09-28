package com.everhomes.workReport;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.workReport.ListWorkReportsValCommand;
import com.everhomes.rest.workReport.WorkReportReadStatus;
import com.everhomes.rest.workReport.WorkReportStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkReportValProviderImpl implements WorkReportValProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private WorkReportTimeService workReportTimeService;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createWorkReportVal(WorkReportVal val) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportVals.class));
        val.setId(id);
        val.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        val.setUpdateTime(val.getCreateTime());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        dao.insert(val);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportVals.class, null);
        return val.getId();
    }

    @Override
    public void updateWorkReportVal(WorkReportVal val) {
        val.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        dao.update(val);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportVals.class, val.getId());
    }

    @Override
    public WorkReportVal getWorkReportValById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportValsDao dao = new EhWorkReportValsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), WorkReportVal.class);
    }

    @Override
    public WorkReportVal getValidWorkReportValById(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VALS);
        query.addConditions(Tables.EH_WORK_REPORT_VALS.ID.eq(id));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.STATUS.eq(WorkReportStatus.VALID.getCode()));
        return query.fetchOneInto(WorkReportVal.class);
    }

    @Override
    public List<WorkReportVal> listWorkReportValsByApplierIds(
            Integer namespaceId, Integer pageOffset, Integer pageSize, Long ownerId, String ownerType, List<Long> applierIds) {
        List<WorkReportVal> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VALS);
        query.addConditions(Tables.EH_WORK_REPORT_VALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.STATUS.eq(WorkReportStatus.VALID.getCode()));
        if (applierIds != null && applierIds.size() > 0)
            query.addConditions(Tables.EH_WORK_REPORT_VALS.APPLIER_USER_ID.in(applierIds));
        //  set the pageOffset condition
        query.addLimit(pageOffset, pageSize + 1);
        query.addOrderBy(Tables.EH_WORK_REPORT_VALS.UPDATE_TIME.desc());

        //  return back
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportVal.class));
            return null;
        });
        return results;
    }

    @Override
    public List<WorkReportVal> listWorkReportValsByReceiverId(
            Integer namespaceId, Integer pageOffset, Long receiverId, ListWorkReportsValCommand cmd) {
        List<WorkReportVal> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.selectQuery();
        query.addFrom(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addJoin(Tables.EH_WORK_REPORT_VALS, JoinType.JOIN,
                Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(Tables.EH_WORK_REPORT_VALS.ID));

        //  EH_WORK_REPORT_VALS conditions.
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_ID.eq(cmd.getOwnerId()));
        query.addConditions(Tables.EH_WORK_REPORT_VALS.OWNER_TYPE.eq(cmd.getOwnerType()));
        if (cmd.getReportId() != null)
            query.addConditions(Tables.EH_WORK_REPORT_VALS.REPORT_ID.eq(cmd.getReportId()));
        if(cmd.getApplierIds() != null && cmd.getApplierIds().size()>0)
            query.addConditions(Tables.EH_WORK_REPORT_VALS.APPLIER_USER_ID.in(cmd.getApplierIds()));
        if (cmd.getStartTime() != null && cmd.getEndTime() != null) {
            query.addConditions(Tables.EH_WORK_REPORT_VALS.REPORT_TIME.ge(workReportTimeService.toSqlDate(cmd.getStartTime())));
            query.addConditions(Tables.EH_WORK_REPORT_VALS.REPORT_TIME.le(workReportTimeService.toSqlDate(cmd.getEndTime())));
        }

        //  EH_WORK_REPORT_VAL_RECEIVER_MAP conditions.
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        if (cmd.getReadStatus() != null)
            query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS.eq(cmd.getReadStatus()));
        query.addLimit(pageOffset, cmd.getPageSize() + 1);
        query.addOrderBy(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.CREATE_TIME.desc());

        //  return back
        query.fetch().map(r -> {
            WorkReportVal reportVal = new WorkReportVal();
            reportVal.setId(r.getValue(Tables.EH_WORK_REPORT_VALS.ID));
            reportVal.setReportId(r.getValue(Tables.EH_WORK_REPORT_VALS.REPORT_ID));
            reportVal.setReportType(r.getValue(Tables.EH_WORK_REPORT_VALS.REPORT_TYPE));
            reportVal.setReportTime(r.getValue(Tables.EH_WORK_REPORT_VALS.REPORT_TIME));
            reportVal.setApplierName(r.getValue(Tables.EH_WORK_REPORT_VALS.APPLIER_NAME));
            reportVal.setApplierAvatar(r.getValue(Tables.EH_WORK_REPORT_VALS.APPLIER_AVATAR));
            reportVal.setReadStatus(r.getValue(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS));
            reportVal.setUpdateTime(r.getValue(Tables.EH_WORK_REPORT_VALS.UPDATE_TIME));
            results.add(reportVal);
            return null;
        });
        return results;
    }

    @Caching(evict = {@CacheEvict(value = "listReportValReceiversByValId", key = "#receiver.reportValId")})
    @Override
    public void createWorkReportValReceiverMap(WorkReportValReceiverMap receiver) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportValReceiverMap.class));
        receiver.setId(id);
        receiver.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        receiver.setUpdateTime(receiver.getCreateTime());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValReceiverMapDao dao = new EhWorkReportValReceiverMapDao(context.configuration());
        dao.insert(receiver);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportValReceiverMap.class, null);
    }

    @Caching(evict = {@CacheEvict(value = "listReportValReceiversByValId", key = "#receiver.reportValId")})
    @Override
    public void updateWorkReportValReceiverMap(WorkReportValReceiverMap receiver) {
        receiver.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValReceiverMapDao dao = new EhWorkReportValReceiverMapDao(context.configuration());
        dao.update(receiver);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportValReceiverMap.class, receiver.getId());
    }

    @Caching(evict = {@CacheEvict(value = "listReportValReceiversByValId", key = "#reportValId")})
    @Override
    public void deleteReportValReceiverByValId(Long reportValId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportValReceiverMapRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(reportValId));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMap.class, null);
    }

    @Override
    public WorkReportValReceiverMap getWorkReportValReceiverByReceiverId(Integer namespaceId, Long reportValId, Long receiverId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(reportValId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        return query.fetchOneInto(WorkReportValReceiverMap.class);
    }

    @Cacheable(value = "listReportValReceiversByValId", key = "#reportValId", unless = "#result.size() == 0")
    @Override
    public List<WorkReportValReceiverMap> listReportValReceiversByValId(Long reportValId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.REPORT_VAL_ID.eq(reportValId));
        List<WorkReportValReceiverMap> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportValReceiverMap.class));
            return null;
        });
        return results;
    }

    @Override
    public Integer countUnReadWorkReportsVal(Integer namespaceId, Long organizationId, Long receiverId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS.eq(WorkReportReadStatus.UNREAD.getCode()));
        return query.fetchCount();
    }

    @Override
    public void markWorkReportsValReading(Integer namespaceId, Long organizationId, Long receiverId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhWorkReportValReceiverMapRecord> query = context.updateQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        query.addValue(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.READ_STATUS, WorkReportReadStatus.READ.getCode());
        query.addValue(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.UPDATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP.RECEIVER_USER_ID.eq(receiverId));
        query.execute();
    }

    @Override
    public Long createWorkReportValComment(WorkReportValComment comment) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportValComments.class));
        comment.setId(id);
        comment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValCommentsDao dao = new EhWorkReportValCommentsDao(context.configuration());
        dao.insert(comment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportValComments.class, null);
        return id;
    }

    @Override
    public void deleteWorkReportValComment(WorkReportValComment comment) {
        comment.setStatus(WorkReportStatus.INVALID.getCode());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValCommentsDao dao = new EhWorkReportValCommentsDao(context.configuration());
        dao.update(comment);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportValComments.class, comment.getId());
    }

    @Override
    public WorkReportValComment getWorkReportValCommentById(Long commentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhWorkReportValCommentsDao dao = new EhWorkReportValCommentsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(commentId), WorkReportValComment.class);
    }

    @Override
    public List<WorkReportValComment> listWorkReportValComments(Integer namespaceId, Long reportValId, Integer offset, Integer pageSize) {
        List<WorkReportValComment> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValCommentsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_COMMENTS);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENTS.REPORT_VAL_ID.eq(reportValId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENTS.STATUS.eq(WorkReportStatus.VALID.getCode()));
        query.addOrderBy(Tables.EH_WORK_REPORT_VAL_COMMENTS.CREATE_TIME.desc());
        query.addLimit(offset, pageSize + 1);
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportValComment.class));
            return null;
        });
        return results;
    }

    @Override
    public Integer countWorkReportValComments(Integer namespaceId, Long reportValId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValCommentsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_COMMENTS);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENTS.REPORT_VAL_ID.eq(reportValId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENTS.STATUS.eq(WorkReportStatus.VALID.getCode()));
        return query.fetchCount();
    }

    @Override
    public void createWorkReportValCommentAttachment(WorkReportValCommentAttachment attachment) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportValCommentAttachments.class));
        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValCommentAttachmentsDao dao = new EhWorkReportValCommentAttachmentsDao(context.configuration());
        dao.insert(attachment);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportValCommentAttachments.class, null);
    }

    @Override
    public void deleteCommentAttachmentsByCommentId(Integer namespaceId, Long commentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhWorkReportValCommentAttachmentsRecord> query = context.updateQuery(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS);
        query.addValue(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS.STATUS, WorkReportStatus.INVALID.getCode());
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS.COMMENT_ID.eq(commentId));
        query.execute();
    }

    @Override
    public List<WorkReportValCommentAttachment> listWorkReportValCommentAttachments(Integer namespaceId, List<Long> commentIds) {
        List<WorkReportValCommentAttachment> results = new ArrayList<>();

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        SelectQuery<EhWorkReportValCommentAttachmentsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS.COMMENT_ID.in(commentIds));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_COMMENT_ATTACHMENTS.STATUS.eq(WorkReportStatus.VALID.getCode()));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportValCommentAttachment.class));
            return null;
        });
        return results;
    }

    @Override
    public Long createWorkReportValReceiverMsg(WorkReportValReceiverMsg msg){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWorkReportValReceiverMsg.class));
        msg.setId(id);
        msg.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValReceiverMsgDao dao = new EhWorkReportValReceiverMsgDao(context.configuration());
        dao.insert(msg);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhWorkReportValReceiverMsg.class, null);
        return msg.getId();
    }

    @Override
    public void deleteReportValReceiverMsgByValId(Long reportValId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportValReceiverMsgRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG.REPORT_VAL_ID.eq(reportValId));
        query.execute();
    }

    @Override
    public void deleteReportValReceiverMsg() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhWorkReportValReceiverMsgRecord> query = context.deleteQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG.REMINDER_TIME.lt(new Timestamp(DateHelper.currentGMTTime().getTime())));
        query.execute();
    }

    @Override
    public void updateWorkReportValReceiverMsg(WorkReportValReceiverMsg msg) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhWorkReportValReceiverMsgDao dao = new EhWorkReportValReceiverMsgDao(context.configuration());
        dao.update(msg);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportValReceiverMsg.class, msg.getId());
    }

    @Override
    public List<WorkReportValReceiverMsg> listReportValReceiverMsgByTime(java.sql.Timestamp startTime, java.sql.Timestamp endTime){
        List<WorkReportValReceiverMsg> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMsgRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG.REMINDER_TIME.ge(startTime));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG.REMINDER_TIME.le(endTime));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportValReceiverMsg.class));
            return null;
        });
        return results;
    }

    @Override
    public List<WorkReportValReceiverMsg> listReportValReceiverMsgByReportTime(Long reportId, java.sql.Date reportTime){
        List<WorkReportValReceiverMsg> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMsgRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG);
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG.REPORT_ID.eq(reportId));
        query.addConditions(Tables.EH_WORK_REPORT_VAL_RECEIVER_MSG.REPORT_TIME.eq(reportTime));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, WorkReportValReceiverMsg.class));
            return null;
        });
        return results;
    }

    @Override
    public List<WorkReportValReceiverMap> listWorkReportReceivers() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValReceiverMapRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VAL_RECEIVER_MAP);
        return query.fetchInto(WorkReportValReceiverMap.class);
    }

    @Override
    public List<WorkReportVal> listWorkReportVals(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhWorkReportValsRecord> query = context.selectQuery(Tables.EH_WORK_REPORT_VALS);
        return query.fetchInto(WorkReportVal.class);
    }
}