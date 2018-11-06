package com.everhomes.archives;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.archives.ArchivesOperationStatus;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArchivesProviderImpl implements ArchivesProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createArchivesStickyContacts(ArchivesStickyContacts stickyContact) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesStickyContacts.class));
        stickyContact.setId(id);
        stickyContact.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        stickyContact.setOperatorUid(UserContext.currentUserId());
        stickyContact.setUpdateTime(stickyContact.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        dao.insert(stickyContact);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesStickyContacts.class, null);
    }

    @Override
    public void updateArchivesStickyContacts(ArchivesStickyContacts stickyContact) {
        stickyContact.setOperatorUid(UserContext.currentUserId());
        stickyContact.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        dao.update(stickyContact);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesStickyContacts.class, stickyContact.getId());
    }

    @Override
    public void deleteArchivesStickyContacts(ArchivesStickyContacts stickyContact) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        dao.deleteById(stickyContact.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesStickyContacts.class, stickyContact.getId());
    }

    @Override
    public void deleteArchivesStickyContactsByDetailIds(Integer namespaceId, List<Long> detailIds){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhArchivesStickyContactsRecord> query = context.deleteQuery(Tables.EH_ARCHIVES_STICKY_CONTACTS);
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.DETAIL_ID.in(detailIds));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWorkReportScopeMap.class, null);
    }

    @Override
    public ArchivesStickyContacts findArchivesStickyContactsById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ArchivesStickyContacts.class);
    }

    @Override
    public List<Long> listArchivesStickyContactsIds(Integer namespaceId, Long organizationId, Integer stickCount) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_ARCHIVES_STICKY_CONTACTS.DETAIL_ID)
                .from(Tables.EH_ARCHIVES_STICKY_CONTACTS)
                .where(Tables.EH_ARCHIVES_STICKY_CONTACTS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ARCHIVES_STICKY_CONTACTS.ORGANIZATION_ID.eq(organizationId))
                .orderBy(Tables.EH_ARCHIVES_STICKY_CONTACTS.UPDATE_TIME.desc())
                .limit(stickCount)
                .fetchInto(Long.class);
    }

    @Override
    public ArchivesStickyContacts findArchivesStickyContactsByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesStickyContactsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_STICKY_CONTACTS);
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.DETAIL_ID.eq(detailId));
        if (query.fetch() != null) {
            return ConvertHelper.convert(query.fetchAny(), ArchivesStickyContacts.class);
        } else
            return null;
    }

    @Override
    public void deleteArchivesStickyContactsByDetailId(Integer namespaceId, Long detailId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        DeleteQuery query = context.deleteQuery(Tables.EH_ARCHIVES_STICKY_CONTACTS);
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_STICKY_CONTACTS.DETAIL_ID.eq(detailId));
        query.execute();
    }

    @Override
    public void createArchivesDismissEmployee(ArchivesDismissEmployees dismissEmployee) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesDismissEmployees.class));
        dismissEmployee.setId(id);
        dismissEmployee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dismissEmployee.setOperatorUid(UserContext.currentUserId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesDismissEmployeesDao dao = new EhArchivesDismissEmployeesDao(context.configuration());
        dao.insert(dismissEmployee);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesDismissEmployees.class, null);
    }

    @Override
    public void updateArchivesDismissEmployee(ArchivesDismissEmployees dismissEmployee){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesDismissEmployeesDao dao = new EhArchivesDismissEmployeesDao(context.configuration());
        dao.update(dismissEmployee);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesDismissEmployees.class, dismissEmployee.getId());
    }

    @Override
    public List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesDismissEmployeesRecord> query = context.selectQuery(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES);
        if(namespaceId != null && namespaceId.longValue() != 0)
        query.addConditions(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(condition);

        //  计算从第几行开始读
        int pageOffset = (offset -1) * (count - 1);

        query.addOrderBy(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DISMISS_TIME.desc());
        query.addLimit(pageOffset,count);
        List<ArchivesDismissEmployees> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ArchivesDismissEmployees.class));
            return null;
        });
        if (0 != results.size()) {
            return results;
        }
        return null;
    }

    @Override
    public ArchivesDismissEmployees getArchivesDismissEmployeesByDetailId(Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesDismissEmployeesRecord> query = context.selectQuery(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES);
        query.addConditions(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DETAIL_ID.eq(detailId));
        return query.fetchAnyInto(ArchivesDismissEmployees.class);
    }

    @Override
    public void deleteArchivesDismissEmployees(ArchivesDismissEmployees dismissEmployee) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesDismissEmployeesDao dao = new EhArchivesDismissEmployeesDao(context.configuration());
        dao.deleteById(dismissEmployee.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesDismissEmployees.class, dismissEmployee.getId());
    }

    @Override
    public List<Long> listDismissEmployeeDetailIdsByDepartmentIds(List<Long> departmentIds){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesDismissEmployeesRecord> query = context.selectQuery(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES);
        query.addSelect(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DETAIL_ID);
        query.addConditions(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DEPARTMENT_ID.in(departmentIds));
        List<Long> results = query.fetchInto(Long.class);
        if (null == results || results.size() == 0)
            return null;
        return results;
    }

    @Override
    public void createOperationalConfiguration(ArchivesOperationalConfiguration config) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesOperationalConfigurations.class));
        config.setId(id);
        config.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        config.setOperatorUid(UserContext.currentUserId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesOperationalConfigurationsDao dao = new EhArchivesOperationalConfigurationsDao(context.configuration());
        dao.insert(config);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesOperationalConfigurations.class, null);
    }

    @Override
    public void deleteLastConfiguration(Integer namespaceId, List<Long> detailIds, Byte operationType){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        UpdateQuery<EhArchivesOperationalConfigurationsRecord> query = context.updateQuery(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS);
        query.addValue(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.STATUS, ArchivesOperationStatus.CANCEL.getCode());
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.DETAIL_ID.in(detailIds));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_TYPE.eq(operationType));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_DATE.gt(ArchivesUtil.currentDate()));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.STATUS.eq(ArchivesOperationStatus.PENDING.getCode()));
        query.execute();
    }

    @Override
    public void updateOperationalConfiguration(ArchivesOperationalConfiguration config) {
        config.setOperatorUid(UserContext.currentUserId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesOperationalConfigurationsDao dao = new EhArchivesOperationalConfigurationsDao(context.configuration());
        dao.update(config);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesOperationalConfigurations.class, config.getId());
    }

    @Override
    public ArchivesOperationalConfiguration findConfigurationByDetailId(Integer namespaceId, Long organizationId, Byte type, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesOperationalConfigurationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS);
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.DETAIL_ID.eq(detailId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_TYPE.eq(type));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.STATUS.eq(ArchivesOperationStatus.PENDING.getCode()));
        return query.fetchAnyInto(ArchivesOperationalConfiguration.class);

    }

    @Override
    public ArchivesOperationalConfiguration findPendingConfigurationByDetailId(Integer namespaceId, Long detailId, Byte operationType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesOperationalConfigurationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS);
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.DETAIL_ID.eq(detailId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_TYPE.eq(operationType));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_DATE.gt(ArchivesUtil.currentDate()));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.STATUS.eq(ArchivesOperationStatus.PENDING.getCode()));
        return query.fetchAnyInto(ArchivesOperationalConfiguration.class);
    }

    @Override
    public List<ArchivesOperationalConfiguration> listPendingConfigurationsInDetailIds(Integer namespaceId, List<Long> detailIds, Byte operationType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesOperationalConfigurationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS);
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.DETAIL_ID.in(detailIds));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_TYPE.eq(operationType));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_DATE.gt(ArchivesUtil.currentDate()));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.STATUS.eq(ArchivesOperationStatus.PENDING.getCode()));
        List<ArchivesOperationalConfiguration> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ArchivesOperationalConfiguration.class));
            return null;
        });
        return results;
    }

    @Override
    public List<ArchivesOperationalConfiguration> listPendingConfigurations(Date date){
        List<ArchivesOperationalConfiguration> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesOperationalConfigurationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS);
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.OPERATION_DATE.eq(date));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_CONFIGURATIONS.STATUS.eq(ArchivesOperationStatus.PENDING.getCode()));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ArchivesOperationalConfiguration.class));
            return null;
        });
        return results;
    }

    @Override
    public void createOperationalLog(ArchivesOperationalLog log) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesOperationalLogs.class));
        log.setId(id);
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesOperationalLogsDao dao = new EhArchivesOperationalLogsDao(context.configuration());
        dao.insert(log);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesOperationalLogs.class, null);
    }

    @Override
    public List<ArchivesOperationalLog> listArchivesLogs(Long organizationId, Long detailId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesOperationalLogsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_OPERATIONAL_LOGS);
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_LOGS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ARCHIVES_OPERATIONAL_LOGS.DETAIL_ID.eq(detailId));
        query.addOrderBy(Tables.EH_ARCHIVES_OPERATIONAL_LOGS.CREATE_TIME.desc(),Tables.EH_ARCHIVES_OPERATIONAL_LOGS.OPERATION_TYPE.desc());
        List<ArchivesOperationalLog> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ArchivesOperationalLog.class));
            return null;
        });
        return results;
    }

    @Override
    public void createArchivesNotifications(ArchivesNotifications notification){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesNotifications.class));
        notification.setId(id);
        notification.setOperatorUid(UserContext.currentUserId());
        notification.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));


        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesNotificationsDao dao = new EhArchivesNotificationsDao(context.configuration());
        dao.insert(notification);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesNotifications.class, null);
    }

    @Override
    public ArchivesNotifications findArchivesNotificationsByOrganizationId(Integer namespaceId, Long organizationId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesNotificationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_NOTIFICATIONS);
        query.addConditions(Tables.EH_ARCHIVES_NOTIFICATIONS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_NOTIFICATIONS.ORGANIZATION_ID.eq(organizationId));
        return query.fetchOneInto(ArchivesNotifications.class);
    }

    @Override
    public void updateArchivesNotifications(ArchivesNotifications notification){
        notification.setOperatorUid(UserContext.currentUserId());
        notification.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesNotificationsDao dao = new EhArchivesNotificationsDao(context.configuration());
        dao.update(notification);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesNotifications.class, notification.getId());
    }

    @Override
    public List<ArchivesNotifications> listArchivesNotifications(Integer weekDay, Integer time){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesNotificationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_NOTIFICATIONS);
        query.addConditions(Tables.EH_ARCHIVES_NOTIFICATIONS.NOTIFY_DAY.eq(weekDay));
        query.addConditions(Tables.EH_ARCHIVES_NOTIFICATIONS.NOTIFY_TIME.eq(time));
        List<ArchivesNotifications> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ArchivesNotifications.class));
            return null;
        });
        if (0 != results.size()) {
            return results;
        }
        return null;
    }

    @Override
    public List<OrganizationMemberDetails> listDetailsWithoutCheckInTime() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_DETAILS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.isNull());
        return query.fetchInto(OrganizationMemberDetails.class);
    }

    @Override
    public OrganizationMember findOrganizationMemberWithoutStatusByDetailId(Long detailId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId));
        return query.fetchAnyInto(OrganizationMember.class);
    }

    @Override
    public List<Long> listDismissalDetailIds(){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesDismissEmployeesRecord> query = context.selectQuery(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES);
        query.addSelect(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.DETAIL_ID);
        return query.fetchInto(Long.class);
    }

    @Override
    public List<OrganizationMemberDetails> listDetailsWithoutDismissalStatus(List<Long> detailIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMemberDetailsRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBER_DETAILS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.notIn(detailIds));
        return query.fetchInto(OrganizationMemberDetails.class);
    }

    @Override
    public OrganizationMember findOrganizationMemberWithStatusByDetailId(Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhOrganizationMembersRecord> query = context.selectQuery(Tables.EH_ORGANIZATION_MEMBERS);
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID.eq(detailId));
        query.addConditions(Tables.EH_ORGANIZATION_MEMBERS.STATUS.notIn(OrganizationMemberStatus.INACTIVE.getCode(),OrganizationMemberStatus.REJECT.getCode()));
        return query.fetchAnyInto(OrganizationMember.class);
    }
}
