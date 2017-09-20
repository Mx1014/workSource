package com.everhomes.archives;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhArchivesConfigurationsRecord;
import com.everhomes.server.schema.tables.records.EhArchivesDismissEmployeesRecord;
import com.everhomes.server.schema.tables.records.EhArchivesFormsRecord;
import com.everhomes.server.schema.tables.records.EhArchivesStickyContactsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
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
        stickyContact.setOperatorUid(UserContext.current().getUser().getId());
        stickyContact.setUpdateTime(stickyContact.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        dao.insert(stickyContact);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesStickyContacts.class, null);
    }

    @Override
    public void updateArchivesStickyContacts(ArchivesStickyContacts stickyContact) {
        stickyContact.setOperatorUid(UserContext.current().getUser().getId());
        stickyContact.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        dao.update(stickyContact);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesStickyContacts.class, stickyContact.getId());
    }

    @Override
    public void deleteArchivesStickyContacts(ArchivesStickyContacts stickyContact) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        dao.deleteById(stickyContact.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesStickyContacts.class, stickyContact.getId());
    }

    @Override
    public ArchivesStickyContacts findArchivesStickyContactsById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhArchivesStickyContactsDao dao = new EhArchivesStickyContactsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ArchivesStickyContacts.class);
    }

    @Override
    public List<Long> listArchivesStickyContactsIds(Integer namespaceId, Long organizationId, Integer stickCount) {
        List<Long> results = new ArrayList<>();
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
            return ConvertHelper.convert(query.fetchOne(), ArchivesStickyContacts.class);
        } else
            return null;
    }

    @Override
    public void createArchivesDismissEmployee(ArchivesDismissEmployees dismissEmployee) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesDismissEmployees.class));
        dismissEmployee.setId(id);
        dismissEmployee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dismissEmployee.setOperatorUid(UserContext.current().getUser().getId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesDismissEmployeesDao dao = new EhArchivesDismissEmployeesDao(context.configuration());
        dao.insert(dismissEmployee);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesDismissEmployees.class, null);
    }

    @Override
    public List<ArchivesDismissEmployees> listArchivesDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesDismissEmployeesRecord> query = context.selectQuery(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES);
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
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }

    @Override
    public void createArchivesForm(ArchivesFroms form){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesForms.class));
        form.setId(id);
        form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormsDao dao = new EhArchivesFormsDao(context.configuration());
        dao.insert(form);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesForms.class, null);
    }

    @Override
    public void updateArchivesForm(ArchivesFroms form){
        form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesFormsDao dao = new EhArchivesFormsDao(context.configuration());
        dao.update(form);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesForms.class, form.getId());

    }

    @Override
    public ArchivesFroms findArchivesFormOriginId(Integer namespaceId, Long organizationId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesFormsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_FORMS);
        query.addConditions(Tables.EH_ARCHIVES_FORMS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_FORMS.ORGANIZATION_ID.eq(organizationId));
        return query.fetchOneInto(ArchivesFroms.class);
    }

    @Override
    public void createArchivesConfigurations(ArchivesConfigurations configuration){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesConfigurations.class));
        configuration.setId(id);
        configuration.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        configuration.setOperatorUid(UserContext.current().getUser().getId());
        configuration.setNamespaceId(UserContext.getCurrentNamespaceId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesConfigurationsDao dao = new EhArchivesConfigurationsDao(context.configuration());
        dao.insert(configuration);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesConfigurations.class, null);
    }

    @Override
    public void updateArchivesConfigurations(ArchivesConfigurations configuration) {
        configuration.setOperatorUid(UserContext.current().getUser().getId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesConfigurationsDao dao = new EhArchivesConfigurationsDao(context.configuration());
        dao.update(configuration);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesConfigurations.class, configuration.getId());
    }

    @Override
    public List<ArchivesConfigurations> listArchivesConfigurations(Date date){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesConfigurationsRecord> query = context.selectQuery(Tables.EH_ARCHIVES_CONFIGURATIONS);
//        query.addConditions(Tables.EH_ARCHIVES_CONFIGURATIONS.NAMESPACE_ID.eq(namespaceId));
//        query.addConditions(Tables.EH_ARCHIVES_CONFIGURATIONS.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ARCHIVES_CONFIGURATIONS.OPERATION_TIME.eq(date));
        List<ArchivesConfigurations> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ArchivesConfigurations.class));
            return null;
        });
        if (null != results && 0 != results.size()) {
            return results;
        }
        return null;
    }

    @Override
    public void createArchivesLogs(ArchivesLogs log){
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesLogs.class));
        log.setId(id);
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setNamespaceId(UserContext.getCurrentNamespaceId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesLogsDao dao = new EhArchivesLogsDao(context.configuration());
        dao.insert(log);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesLogs.class, null);
    }

}
