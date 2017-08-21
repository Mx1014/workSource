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
import com.everhomes.server.schema.tables.pojos.EhArchivesContactsSticky;
import com.everhomes.server.schema.tables.pojos.EhArchivesDismissEmployees;
import com.everhomes.server.schema.tables.records.EhArchivesContactsStickyRecord;
import com.everhomes.server.schema.tables.records.EhArchivesDismissEmployeesRecord;
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
    public void createArchivesContactsSticky(ArchivesContactsSticky profileContactsSticky) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesContactsSticky.class));
        profileContactsSticky.setId(id);
        profileContactsSticky.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        profileContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        profileContactsSticky.setUpdateTime(profileContactsSticky.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.insert(profileContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesContactsSticky.class, null);
    }

    @Override
    public void updateArchivesContactsSticky(ArchivesContactsSticky profileContactsSticky) {
        profileContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        profileContactsSticky.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.update(profileContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhArchivesContactsSticky.class, profileContactsSticky.getId());
    }

    @Override
    public void deleteArchivesContactsSticky(ArchivesContactsSticky profileContactsSticky) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        dao.deleteById(profileContactsSticky.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhArchivesContactsSticky.class, profileContactsSticky.getId());
    }

    @Override
    public ArchivesContactsSticky findArchivesContactsStickyById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhArchivesContactsStickyDao dao = new EhArchivesContactsStickyDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ArchivesContactsSticky.class);
    }

    @Override
    public List<Long> listArchivesContactsStickyIds(Integer namespaceId, Long organizationId) {
        List<Long> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_ARCHIVES_CONTACTS_STICKY.DETAIL_ID)
                .from(Tables.EH_ARCHIVES_CONTACTS_STICKY)
                .where(Tables.EH_ARCHIVES_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_ARCHIVES_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId))
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
    public void createArchivesDismissEmployee(ArchivesDismissEmployees profileDismissEmployee) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhArchivesDismissEmployees.class));
        profileDismissEmployee.setId(id);
        profileDismissEmployee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        profileDismissEmployee.setOperatorUid(UserContext.current().getUser().getId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhArchivesDismissEmployeesDao dao = new EhArchivesDismissEmployeesDao(context.configuration());
        dao.insert(profileDismissEmployee);

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

        /*if (anchor != null)
            query.addConditions(Tables.EH_ARCHIVES_DISMISS_EMPLOYEES.ID.gt(anchor));*/
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

    /*  List<Long> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_PROFILE_DISMISS_EMPLOYEES.DETAIL_ID)
                .from(Tables.EH_PROFILE_DISMISS_EMPLOYEES)
                .where(Tables.EH_PROFILE_DISMISS_EMPLOYEES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_PROFILE_DISMISS_EMPLOYEES.CREATE_TIME.lt(new Timestamp(anchor)))
                .orderBy(Tables.EH_PROFILE_DISMISS_EMPLOYEES.CREATE_TIME.desc())
                .limit(count)
                .fetchInto(ArchivesDismissEmployees.class);*/

}
