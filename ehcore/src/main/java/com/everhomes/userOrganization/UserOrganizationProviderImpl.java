package com.everhomes.userOrganization;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.organization.UserOrganizationStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserOrganizationsDao;
import com.everhomes.server.schema.tables.pojos.EhUserOrganizations;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/19.
 */
@Component
public class UserOrganizationProviderImpl implements UserOrganizationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOrganizations.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createUserOrganizations(UserOrganizations userOrganizations) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserOrganizations.class));
        userOrganizations.setId(id);
        userOrganizations.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationsDao dao = new EhUserOrganizationsDao(context.configuration());
        dao.insert(userOrganizations);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserOrganizationsDao.class, null);
    }

    @Override
    public void updateUserOrganizations(UserOrganizations userOrganizations) {
        assert (userOrganizations.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationsDao dao = new EhUserOrganizationsDao(context.configuration());
        dao.update(userOrganizations);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganizations.class, userOrganizations.getId());
    }

    @Override
    public UserOrganizations findUserOrganizations(Integer namespaceId, Long organizationId, Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_USER_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(organizationId));
        condition = condition.and(Tables.EH_USER_ORGANIZATIONS.USER_ID.eq(userId));
        Record r = context.select().from(Tables.EH_USER_ORGANIZATIONS).where(condition).fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, UserOrganizations.class);
        return null;
    }

    @Override
    public void deleteUserOrganizations(UserOrganizations userOrganizations) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationsDao dao = new EhUserOrganizationsDao(context.configuration());
        dao.delete(userOrganizations);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganizations.class, userOrganizations.getId());
    }

    @Override
    public UserOrganizations inactiveUserOrganizations(UserOrganizations userOrganizations) {
        assert (userOrganizations.getId() == null);
        userOrganizations.setStatus(UserOrganizationStatus.INACTIVE.getCode());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationsDao dao = new EhUserOrganizationsDao(context.configuration());
        dao.update(userOrganizations);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganizations.class, userOrganizations.getId());
        return userOrganizations;
    }
}
