package com.everhomes.profile;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhProfileDismissEmployeesDao;
import com.everhomes.server.schema.tables.pojos.EhProfileDismissEmployees;
import com.everhomes.server.schema.tables.daos.EhProfileContactsStickyDao;
import com.everhomes.server.schema.tables.pojos.EhProfileContactsSticky;
import com.everhomes.server.schema.tables.records.EhProfileContactsStickyRecord;
import com.everhomes.server.schema.tables.records.EhProfileDismissEmployeesRecord;
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
public class ProfileProviderImpl implements ProfileProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createProfileContactsSticky(ProfileContactsSticky profileContactsSticky) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProfileContactsSticky.class));
        profileContactsSticky.setId(id);
        profileContactsSticky.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        profileContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        profileContactsSticky.setUpdateTime(profileContactsSticky.getCreateTime());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        dao.insert(profileContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProfileContactsSticky.class, null);
    }

    @Override
    public void updateProfileContactsSticky(ProfileContactsSticky profileContactsSticky) {
        profileContactsSticky.setOperatorUid(UserContext.current().getUser().getId());
        profileContactsSticky.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        dao.update(profileContactsSticky);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProfileContactsSticky.class, profileContactsSticky.getId());
    }

    @Override
    public void deleteProfileContactsSticky(ProfileContactsSticky profileContactsSticky) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        dao.deleteById(profileContactsSticky.getId());
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhProfileContactsSticky.class, profileContactsSticky.getId());
    }

    @Override
    public ProfileContactsSticky findProfileContactsStickyById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhProfileContactsStickyDao dao = new EhProfileContactsStickyDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ProfileContactsSticky.class);
    }

    @Override
    public List<Long> listProfileContactsStickyIds(Integer namespaceId, Long organizationId) {
        List<Long> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_PROFILE_CONTACTS_STICKY.DETAIL_ID)
                .from(Tables.EH_PROFILE_CONTACTS_STICKY)
                .where(Tables.EH_PROFILE_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_PROFILE_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId))
                .fetchInto(Long.class);
    }

    @Override
    public ProfileContactsSticky findProfileContactsStickyByDetailIdAndOrganizationId(Integer namespaceId, Long organizationId, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhProfileContactsStickyRecord> query = context.selectQuery(Tables.EH_PROFILE_CONTACTS_STICKY);
        query.addConditions(Tables.EH_PROFILE_CONTACTS_STICKY.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_PROFILE_CONTACTS_STICKY.ORGANIZATION_ID.eq(organizationId));
        query.addConditions(Tables.EH_PROFILE_CONTACTS_STICKY.DETAIL_ID.eq(detailId));
        if (query.fetch() != null) {
            return ConvertHelper.convert(query.fetchOne(), ProfileContactsSticky.class);
        } else
            return null;
    }

    @Override
    public void createProfileDismissEmployee(ProfileDismissEmployees profileDismissEmployee) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProfileDismissEmployees.class));
        profileDismissEmployee.setId(id);
        profileDismissEmployee.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        profileDismissEmployee.setOperatorUid(UserContext.current().getUser().getId());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProfileDismissEmployeesDao dao = new EhProfileDismissEmployeesDao(context.configuration());
        dao.insert(profileDismissEmployee);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProfileDismissEmployees.class, null);
    }

    @Override
    public List<ProfileDismissEmployees> listProfileDismissEmployees(Integer offset, Integer count, Integer namespaceId, Condition condition) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhProfileDismissEmployeesRecord> query = context.selectQuery(Tables.EH_PROFILE_DISMISS_EMPLOYEES);
        query.addConditions(Tables.EH_PROFILE_DISMISS_EMPLOYEES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(condition);

        //  计算从第几行开始读
        int pageOffset = (offset -1) * (count - 1);

        /*if (anchor != null)
            query.addConditions(Tables.EH_PROFILE_DISMISS_EMPLOYEES.ID.gt(anchor));*/
        query.addOrderBy(Tables.EH_PROFILE_DISMISS_EMPLOYEES.DISMISS_TIME.desc());
        query.addLimit(pageOffset,count);
        List<ProfileDismissEmployees> results = new ArrayList<>();
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, ProfileDismissEmployees.class));
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
                .fetchInto(ProfileDismissEmployees.class);*/

}
