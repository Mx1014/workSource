package com.everhomes.userOrganization;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.organization.UserOrganizationStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhOrganizationMemberEducationsDao;
import com.everhomes.server.schema.tables.daos.EhUserOrganizationDao;
import com.everhomes.server.schema.tables.pojos.EhUserOrganization;
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
public class userOrganizationProviderImpl implements UserOrganizationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOrganization.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createUserOrganization(UserOrganization userOrganization) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserOrganization.class));
        userOrganization.setId(id);
        userOrganization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationDao dao = new EhUserOrganizationDao(context.configuration());
        dao.insert(userOrganization);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserOrganization.class, null);
    }

    @Override
    public void updateUserOrganization(UserOrganization userOrganization) {
        assert (userOrganization.getId() == null);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationDao dao = new EhUserOrganizationDao(context.configuration());
        dao.update(userOrganization);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganization.class, userOrganization.getId());
    }

    @Override
    public UserOrganization findUserOrganization(Integer namespaceId, Long organizationId, Long userId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_USER_ORGANIZATION.NAMESPACE_ID.eq(namespaceId);
        condition = condition.and(Tables.EH_USER_ORGANIZATION.ORGANIZATION_ID.eq(organizationId));
        condition = condition.and(Tables.EH_USER_ORGANIZATION.USER_ID.eq(userId));
        Record r = context.select().from(Tables.EH_USER_ORGANIZATION).where(condition).fetchAny();
        if (r != null)
            return ConvertHelper.convert(r, UserOrganization.class);
        return null;
    }

    @Override
    public void deleteUserOrganization(UserOrganization userOrganization) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationDao dao = new EhUserOrganizationDao(context.configuration());
        dao.delete(userOrganization);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganization.class, userOrganization.getId());
    }

    @Override
    public UserOrganization inactiveUserOrganization(UserOrganization userOrganization) {
        assert (userOrganization.getId() == null);
        userOrganization.setStatus(UserOrganizationStatus.INACTIVE.getCode());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserOrganizationDao dao = new EhUserOrganizationDao();
        dao.update(userOrganization);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserOrganization.class, userOrganization.getId());
        return userOrganization;
    }
}
