package com.everhomes.decoration;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.decoration.DecorationAttachmentDTO;
import com.everhomes.rest.decoration.DecorationRequestStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class DecorationProviderImpl implements  DecorationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecorationProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createDecorationSetting(DecorationSetting setting) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationSetting.class));
        setting.setId(id);
        setting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationSettingRecord record = ConvertHelper.convert(setting,EhDecorationSettingRecord.class);
        InsertQuery<EhDecorationSettingRecord> query = context
                .insertQuery(Tables.EH_DECORATION_SETTING);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationSetting.class, null);
    }

    @Override
    public void updateDecorationSetting(DecorationSetting setting) {
        assert (setting.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationSettingDao dao = new EhDecorationSettingDao(context.configuration());
        dao.update(setting);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationSetting.class,
                setting.getId());
    }

    @Override
    public DecorationSetting getDecorationSetting(Integer namespaceId, Long communityId, String ownerType, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_SETTING);
        Condition condition = Tables.EH_DECORATION_SETTING.COMMUNITY_ID.eq(communityId);
        if (null != namespaceId)
            condition = condition.and(Tables.EH_DECORATION_SETTING.NAMESPACE_ID.eq(namespaceId));
        if (null != ownerType)
            condition = condition.and(Tables.EH_DECORATION_SETTING.OWNER_TYPE.eq(ownerType));
        if (null != ownerId)
            condition = condition.and(Tables.EH_DECORATION_SETTING.OWNER_ID.eq(ownerId));
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        DecorationSetting setting = ConvertHelper.convert(record,DecorationSetting.class);
        return setting;
    }

    @Override
    public DecorationSetting getDecorationSettingById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_SETTING);
        Condition condition = Tables.EH_DECORATION_SETTING.ID.eq(id);
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        DecorationSetting setting = ConvertHelper.convert(record,DecorationSetting.class);
        return setting;
    }

    @Override
    public void createDecorationAttachment(DecorationAttachment attachment) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationAtttachment.class));
        attachment.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationAtttachmentRecord record = ConvertHelper.convert(attachment,EhDecorationAtttachmentRecord.class);
        InsertQuery<EhDecorationAtttachmentRecord> query = context
                .insertQuery(Tables.EH_DECORATION_ATTTACHMENT);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationAtttachment.class, null);
    }

    @Override
    public List<DecorationAttachmentDTO> listDecorationAttachmentBySettingId(Long settingId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_ATTTACHMENT);
        Condition condition = Tables.EH_DECORATION_ATTTACHMENT.SETTING_ID.eq(settingId);
        step.where(condition);
        List<DecorationAttachmentDTO> list = step.fetch().map(r-> ConvertHelper.convert(r,DecorationAttachmentDTO.class)
        );
        return list;
    }

    @Override
    public void deleteDecorationBySettingId(Long settingId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhDecorationAtttachmentRecord> step = context
                .delete(Tables.EH_DECORATION_ATTTACHMENT);
        Condition condition = Tables.EH_DECORATION_ATTTACHMENT.SETTING_ID.eq(settingId);
        step.where(condition).execute();
    }

    @Override
    public DecorationRequest getRequestById(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_REQUESTS);
        Condition condition = Tables.EH_DECORATION_REQUESTS.ID.eq(requestId);
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        return ConvertHelper.convert(record,DecorationRequest.class);
    }

    @Override
    public List<DecorationRequest> queryDecorationRequests(Integer namespaceId, Long communityId, Long startTime,
                                                           Long endTime, String address, Byte status, String keyword, Byte cancelFlag,
                                                           Integer pageSize, ListingLocator locator) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_REQUESTS);
        Condition condition = Tables.EH_DECORATION_REQUESTS.COMMUNITY_ID.eq(communityId);
        if (null != namespaceId)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.NAMESPACE_ID.eq(namespaceId));
        if (null != startTime)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.CREATE_TIME.gt(new Timestamp(startTime)));
        if (null != endTime)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.CREATE_TIME.lt(new Timestamp(endTime)));
        if (null != address)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.ADDRESS.like("%"+address+"%"));
        if (null != status)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.STATUS.eq(status));
        if (null != keyword)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.APPLY_NAME.eq(keyword).or(Tables.EH_DECORATION_REQUESTS.APPLY_PHONE.eq(keyword))
                    .or(Tables.EH_DECORATION_REQUESTS.APPLY_COMPANY.eq(keyword)));
        if (null != cancelFlag)
            if (cancelFlag == 0)
                condition = condition.and(Tables.EH_DECORATION_REQUESTS.CANCEL_FLAG.eq((byte) 0));
            else
                condition = condition.and(Tables.EH_DECORATION_REQUESTS.CANCEL_FLAG.in((byte) 1, (byte) 2));

        if (null != locator)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.ID.lt(locator.getAnchor()));
        step.where(condition);
        if (null != pageSize)
            step.limit(pageSize);
        return step.orderBy(Tables.EH_DECORATION_REQUESTS.ID.desc()).fetch().map(r->ConvertHelper.convert(r,DecorationRequest.class));
    }

    @Override
    public List<DecorationRequest> queryUserRelateRequests(Integer namespaceId, Long communityId, String phone, Integer pageSize,
                                                           ListingLocator locator) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select(Tables.EH_DECORATION_REQUESTS.fields()).from(Tables.EH_DECORATION_REQUESTS).leftOuterJoin(Tables.EH_DECORATION_WORKERS).on(
                Tables.EH_DECORATION_REQUESTS.ID.eq(Tables.EH_DECORATION_WORKERS.REQUEST_ID));
        Condition condition = Tables.EH_DECORATION_REQUESTS.APPLY_PHONE.eq(phone);
        condition = condition.or(Tables.EH_DECORATION_REQUESTS.DECORATOR_PHONE.eq(phone).and(
                Tables.EH_DECORATION_REQUESTS.STATUS.gt(DecorationRequestStatus.APPLY.getCode())));
        condition = condition.or(Tables.EH_DECORATION_WORKERS.PHONE.eq(phone));
        if (namespaceId != null)
            condition.and(Tables.EH_DECORATION_REQUESTS.NAMESPACE_ID.eq(namespaceId));
        if (communityId != null)
            condition.and(Tables.EH_DECORATION_REQUESTS.COMMUNITY_ID.eq(communityId));
        if (locator != null)
            condition = condition.and(Tables.EH_DECORATION_REQUESTS.ID.lt(locator.getAnchor()));
        step.where(condition);
        if (pageSize != null)
            step.limit(pageSize);
        return step.orderBy(Tables.EH_DECORATION_REQUESTS.ID.desc()).fetch().map(r->{
            DecorationRequest request = new DecorationRequest();
            request.setId(r.getValue(Tables.EH_DECORATION_REQUESTS.ID));
            request.setNamespaceId(r.getValue(Tables.EH_DECORATION_REQUESTS.NAMESPACE_ID));
            request.setCommunityId(r.getValue(Tables.EH_DECORATION_REQUESTS.COMMUNITY_ID));
            request.setCreateTime(r.getValue(Tables.EH_DECORATION_REQUESTS.CREATE_TIME));
            request.setStartTime(r.getValue(Tables.EH_DECORATION_REQUESTS.START_TIME));
            request.setEndTime(r.getValue(Tables.EH_DECORATION_REQUESTS.END_TIME));
            request.setApplyUid(r.getValue(Tables.EH_DECORATION_REQUESTS.APPLY_UID));
            request.setApplyCompany(r.getValue(Tables.EH_DECORATION_REQUESTS.APPLY_COMPANY));
            request.setApplyPhone(r.getValue(Tables.EH_DECORATION_REQUESTS.APPLY_PHONE));
            request.setApplyName(r.getValue(Tables.EH_DECORATION_REQUESTS.APPLY_NAME));
            request.setAddress(r.getValue(Tables.EH_DECORATION_REQUESTS.ADDRESS));
            request.setDecoratorUid(r.getValue(Tables.EH_DECORATION_REQUESTS.DECORATOR_UID));
            request.setDecoratorCompanyId(r.getValue(Tables.EH_DECORATION_REQUESTS.DECORATOR_COMPANY_ID));
            request.setDecoratorCompany(r.getValue(Tables.EH_DECORATION_REQUESTS.DECORATOR_COMPANY));
            request.setDecoratorPhone(r.getValue(Tables.EH_DECORATION_REQUESTS.DECORATOR_PHONE));
            request.setDecoratorName(r.getValue(Tables.EH_DECORATION_REQUESTS.DECORATOR_NAME));
            request.setDecoratorQrid(r.getValue(Tables.EH_DECORATION_REQUESTS.DECORATOR_QRID));
            request.setCancelFlag(r.getValue(Tables.EH_DECORATION_REQUESTS.CANCEL_FLAG));
            request.setStatus(r.getValue(Tables.EH_DECORATION_REQUESTS.STATUS));
            request.setCancelReason(r.getValue(Tables.EH_DECORATION_REQUESTS.CANCEL_REASON));
            request.setRefoundAmount(r.getValue(Tables.EH_DECORATION_REQUESTS.REFOUND_AMOUNT));
            request.setRefoundComment(r.getValue(Tables.EH_DECORATION_REQUESTS.REFOUND_COMMENT));
            return request;
        });
    }

    @Override
    public DecorationWorker getDecorationWorkerById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.ID.eq(id);
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        return ConvertHelper.convert(record,DecorationWorker.class);
    }

    @Override
    public DecorationWorker queryDecorationWorker(Long requestId, String phone) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.REQUEST_ID.eq(requestId);
        condition = condition.and(Tables.EH_DECORATION_WORKERS.PHONE.eq(phone));
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        return ConvertHelper.convert(record,DecorationWorker.class);
    }

    @Override
    public List<DecorationWorker> listWorkersByRequestId(Long requestId, String keyWord, ListingLocator locator, Integer pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.REQUEST_ID.eq(requestId);
        if (!StringUtils.isBlank(keyWord))
            condition = condition.and(Tables.EH_DECORATION_WORKERS.NAME.eq(keyWord).or(
                    Tables.EH_DECORATION_WORKERS.PHONE.eq(keyWord)));
        if(null!=locator && locator.getAnchor() != null)
            condition = condition.and(Tables.EH_DECORATION_WORKERS.ID.gt(locator.getAnchor()));
        if (pageSize!=null)
            step.limit(pageSize);
        step.where(condition);
        return step.orderBy(Tables.EH_DECORATION_WORKERS.ID.desc()).fetch().map(r->ConvertHelper.convert(r,DecorationWorker.class));
    }

    @Override
    public void createDecorationWorker(DecorationWorker worker) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationWorkers.class));
        worker.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationWorkersRecord record = ConvertHelper.convert(worker,EhDecorationWorkersRecord.class);
        InsertQuery<EhDecorationWorkersRecord> query = context
                .insertQuery(Tables.EH_DECORATION_WORKERS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationWorkers.class, null);
    }

    @Override
    public void updateDecorationWorker(DecorationWorker worker) {
        assert (worker.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationWorkersDao dao = new EhDecorationWorkersDao(context.configuration());
        dao.update(worker);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationWorkers.class,
                worker.getId());
    }

    @Override
    public void deleteDecorationWorkerById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhDecorationWorkersRecord> step = context
                .delete(Tables.EH_DECORATION_WORKERS);
        Condition condition = Tables.EH_DECORATION_WORKERS.ID.eq(id);
        step.where(condition).execute();
    }

    @Override
    public void createDecorationCompany(DecorationCompany company) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationCompanies.class));
        company.setId(id);
        company.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationCompaniesRecord record = ConvertHelper.convert(company,EhDecorationCompaniesRecord.class);
        InsertQuery<EhDecorationCompaniesRecord> query = context
                .insertQuery(Tables.EH_DECORATION_COMPANIES);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationCompanies.class, null);
    }

    @Override
    public List<DecorationCompany> listDecorationCompanies(Integer namespaceId, String keyword) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_COMPANIES);
        Condition condition = Tables.EH_DECORATION_COMPANIES.NAMESPACE_ID.eq(namespaceId);
        if (!StringUtils.isBlank(keyword))
            condition = condition.and(Tables.EH_DECORATION_COMPANIES.NAME.like("%"+keyword+"%"));
        step.where(condition);
        return step.fetch().map(r->ConvertHelper.convert(r,DecorationCompany.class));
    }

    @Override
    public DecorationCompany getDecorationCompanyById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_COMPANIES);
        Condition condition = Tables.EH_DECORATION_COMPANIES.ID.eq(id);
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        return ConvertHelper.convert(record,DecorationCompany.class);
    }

    @Override
    public void createDecorationRequest(DecorationRequest request) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationRequests.class));
        request.setId(id);
        request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationRequestsRecord record = ConvertHelper.convert(request,EhDecorationRequestsRecord.class);
        InsertQuery<EhDecorationRequestsRecord> query = context
                .insertQuery(Tables.EH_DECORATION_REQUESTS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationRequests.class, null);
    }

    @Override
    public void updateDecorationRequest(DecorationRequest request) {
        assert (request.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationRequestsDao dao = new EhDecorationRequestsDao(context.configuration());
        dao.update(request);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationRequests.class,
                request.getId());
    }

    @Override
    public List<DecorationFee> listDecorationFeeByRequestId(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_FEE);
        Condition condition = Tables.EH_DECORATION_FEE.REQUEST_ID.eq(requestId);
        step.where(condition);
        return step.fetch().map(r->ConvertHelper.convert(r,DecorationFee.class));
    }

    @Override
    public void deleteDecorationFeeByRequestId(Long requestId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteWhereStep<EhDecorationFeeRecord> step = context
                .delete(Tables.EH_DECORATION_FEE);
        Condition condition = Tables.EH_DECORATION_FEE.REQUEST_ID.eq(requestId);
        step.where(condition).execute();
    }

    @Override
    public void createDecorationFee(DecorationFee fee) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationFee.class));
        fee.setId(id);
        fee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationFeeRecord record = ConvertHelper.convert(fee,EhDecorationFeeRecord.class);
        InsertQuery<EhDecorationFeeRecord> query = context
                .insertQuery(Tables.EH_DECORATION_FEE);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationRequests.class, null);
    }

    @Override
    public void createApprovalVals(DecorationApprovalVal val) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationApprovalVals.class));
        val.setId(id);
        val.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationApprovalValsRecord record = ConvertHelper.convert(val,EhDecorationApprovalValsRecord.class);
        InsertQuery<EhDecorationApprovalValsRecord> query = context
                .insertQuery(Tables.EH_DECORATION_APPROVAL_VALS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationApprovalVals.class, null);
    }

    @Override
    public void deleteApprovalValByRequestId(Long requestId,Long approvalId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_DECORATION_APPROVAL_VALS).set(Tables.EH_DECORATION_APPROVAL_VALS.DELETE_FLAG,(byte)1)
                .where(Tables.EH_DECORATION_APPROVAL_VALS.REQUEST_ID.eq(requestId).and(Tables.EH_DECORATION_APPROVAL_VALS.APPROVAL_ID.eq(approvalId)))
                .execute();
    }

    @Override
    public void updateApprovalVals(DecorationApprovalVal val) {
        assert (val.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationApprovalValsDao dao = new EhDecorationApprovalValsDao(context.configuration());
        dao.update(val);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationApprovalVals.class,
                val.getId());
    }

    @Override
    public DecorationApprovalVal getApprovalValById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_APPROVAL_VALS);
        Condition condition = Tables.EH_DECORATION_APPROVAL_VALS.ID.eq(id);
        step.where(condition);
        Record record = step.fetchOne();
        if (record == null)
            return  null;
        return ConvertHelper.convert(record,DecorationApprovalVal.class);
    }

    @Override
    public List<DecorationApprovalVal> listApprovalVals(Long requestId,Long approvalId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_APPROVAL_VALS);
        Condition condition = Tables.EH_DECORATION_APPROVAL_VALS.REQUEST_ID.eq(requestId);
        if (null != approvalId)
            condition = condition.and(Tables.EH_DECORATION_APPROVAL_VALS.APPROVAL_ID.eq(approvalId));
        condition = condition.and(Tables.EH_DECORATION_APPROVAL_VALS.DELETE_FLAG.ne((byte)1));
        step.where(condition);
        return step.fetch().map(r->ConvertHelper.convert(r,DecorationApprovalVal.class));
    }

    @Override
    public List<DecorationCompanyChief> listChiefsByCompanyId(Long companyId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_DECORATION_COMPANY_CHIEFS);
        Condition condition = Tables.EH_DECORATION_COMPANY_CHIEFS.COMPANY_ID.eq(companyId);
        step.where(condition);
        return step.fetch().map(r->ConvertHelper.convert(r,DecorationCompanyChief.class));
    }

    @Override
    public void createCompanyChief(DecorationCompanyChief chief) {
        long id = sequenceProvider.getNextSequence(NameMapper
                .getSequenceDomainFromTablePojo(EhDecorationCompanyChiefs.class));
        chief.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationCompanyChiefsRecord record = ConvertHelper.convert(chief,EhDecorationCompanyChiefsRecord.class);
        InsertQuery<EhDecorationCompanyChiefsRecord> query = context
                .insertQuery(Tables.EH_DECORATION_COMPANY_CHIEFS);
        query.setRecord(record);
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhDecorationCompanyChiefs.class, null);
    }

    @Override
    public void updateCompanyChief(DecorationCompanyChief chief) {
        assert (chief.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhDecorationCompanyChiefsDao dao = new EhDecorationCompanyChiefsDao(context.configuration());
        dao.update(chief);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhDecorationApprovalVals.class,
                chief.getId());
    }
}
