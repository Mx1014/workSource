package com.everhomes.archives;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhArchivesContactsStickyDao;
import com.everhomes.server.schema.tables.daos.EhArchivesDismissEmployeesDao;
import com.everhomes.server.schema.tables.daos.EhArchivesFormsDao;
import com.everhomes.server.schema.tables.pojos.EhArchivesContactsSticky;
import com.everhomes.server.schema.tables.pojos.EhArchivesDismissEmployees;
import com.everhomes.server.schema.tables.pojos.EhArchivesForms;
import com.everhomes.server.schema.tables.records.EhArchivesContactsStickyRecord;
import com.everhomes.server.schema.tables.records.EhArchivesDismissEmployeesRecord;
import com.everhomes.server.schema.tables.records.EhArchivesFormsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void createArchivesContactsSticky(ArchivesContactsSticky archivesContactsSticky) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesContactsSticky.class));
        archivesContactsSticky.setId(id);
        archivesContactsSticky.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        archivesContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        archivesContactsSticky.setUpdateTime(archivesContactsSticky.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.insert(archivesContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesContactsSticky.class, null);
    }

    @Override
    public void updateArchivesContactsSticky(ArchivesContactsSticky archivesContactsSticky) {
        archivesContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        archivesContactsSticky.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.update(archivesContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesContactsSticky.class, archivesContactsSticky.getId());
    }

    @Override
    public void deleteArchivesContactsSticky(ArchivesContactsSticky archivesContactsSticky) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.deleteById(archivesContactsSticky.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesContactsSticky.class, archivesContactsSticky.getId());
    }

    @Override
    public ArchivesContactsSticky findArchivesContactsStickyById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ArchivesContactsSticky.class);
    }

    @Override
    public List<Long> listArchivesContactsStickyIds(Integer namespaceId, Long organizationId, Integer stickCount) {
        List<Long> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_ARCHIVES_CONTACTS_STICKY.DETAIL_ID)
                .from(Tables.EH_ARCHIVES_CONTACTS_STICKY)
                .where(Tables.EH_ARCHIVES_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ARCHIVES_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId))
                .limit(stickCount)
                .fetchInto(Long.class);
    }

    @Override
    public ArchivesContactsSticky findArchivesContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhArchivesContactsStickyRecord> query = context.selectQuery(Tables.EH_ARCHIVES_CONTACTS_STICKY);
        query.addConditions(Tables.EH_ARCHIVES_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_ARCHIVES_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_ARCHIVES_CONTACTS_STICKY.DETAIL_ID.eq(detailId));
        if (query.fetch() != null) {
            return ConvertHelper.convert(query.fetchOne(), ArchivesContactsSticky.class);
        } else
            return null;
    }

    @Override
    public void createArchivesDismissEmployee(ArchivesDismissEmployees archivesDismissEmployee) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesDismissEmployees.class));
        archivesDismissEmployee.setId(id);
        archivesDismissEmployee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        archivesDismissEmployee.setOperatorUid(UserContext.current().getUser().getId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesDismissEmployeesDao dao = new EhArchivesDismissEmployeesDao(context.configuration());
        dao.insert(archivesDismissEmployee);

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
}
