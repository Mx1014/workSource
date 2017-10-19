package com.everhomes.uniongroup;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUniongroupVersionDao;
import com.everhomes.server.schema.tables.pojos.EhUniongroupVersion;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by wuhan on 2017/10/19.
 */
public class UniongroupVersionProviderImpl implements  UniongroupVersionProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    private EhUniongroupVersionDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhUniongroupVersionDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhUniongroupVersionDao getDao(DSLContext context) {
        return new EhUniongroupVersionDao(context.configuration());
    }

    private DSLContext getReadWriteContext() {
        return getContext(AccessSpec.readWrite());
    }

    private DSLContext getReadOnlyContext() {
        return getContext(AccessSpec.readOnly());
    }

    private DSLContext getContext(AccessSpec accessSpec) {
        return dbProvider.getDslContext(accessSpec);
    }


    @Override
    public void createUniongroupVersion(UniongroupVersion uniongroupVersion) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupVersion.class));
        uniongroupVersion.setId(id);
//		UniongroupVersion.setUpdateTime(UniongroupVersion.getCreateTime());
//		UniongroupVersion.setOperatorUid(UniongroupVersion.getCreatorUid());
        getReadWriteDao().insert(uniongroupVersion);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUniongroupVersion.class, null);
    }

    @Override
    public void updateUniongroupVersion(UniongroupVersion uniongroupVersion) {
        assert (uniongroupVersion.getId() != null);
//		UniongroupVersion.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		UniongroupVersion.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(uniongroupVersion);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupVersion.class, uniongroupVersion.getId());
    }

    @Override
    public UniongroupVersion findUniongroupVersion(Long enterpriseId, String groupType) {
        List<UniongroupVersion> result = getReadOnlyContext().select().from(Tables.EH_UNIONGROUP_VERSION)
                .where(Tables.EH_UNIONGROUP_VERSION.ENTERPRISE_ID.eq(enterpriseId))
                .and(Tables.EH_UNIONGROUP_VERSION.GROUP_TYPE.eq(groupType))
                .orderBy(Tables.EH_SALARY_EMPLOYEES.ID.asc())
                .fetch().map(r -> ConvertHelper.convert(r, UniongroupVersion.class));
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

}
