// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUniongroupConfiguresDao;
import com.everhomes.server.schema.tables.daos.EhUniongroupMemberDetailsDao;
import com.everhomes.server.schema.tables.pojos.EhUniongroupConfigures;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.server.schema.tables.records.EhUniongroupConfiguresRecord;
import com.everhomes.server.schema.tables.records.EhUniongroupMemberDetailsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UniongroupConfigureProviderImpl implements UniongroupConfigureProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    /**
     * UniongroupConfigures
     **/
    @Override
    public void createUniongroupConfigures(UniongroupConfigures uniongroupConfigures) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupConfigures.class));
        uniongroupConfigures.setId(id);
        uniongroupConfigures.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        uniongroupConfigures.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().insert(uniongroupConfigures);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUniongroupConfigures.class, id);
    }

    @Override
    public void updateUniongroupConfigures(UniongroupConfigures uniongroupConfigures) {
        assert (uniongroupConfigures.getId() != null);
        uniongroupConfigures.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        uniongroupConfigures.setOperatorUid(UserContext.current().getUser().getId());
        getReadWriteDao().update(uniongroupConfigures);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfigures.class, uniongroupConfigures.getId());
    }

    @Override
    public UniongroupConfigures findUniongroupConfiguresById(Long id) {
        assert (id != null);
        return ConvertHelper.convert(getReadOnlyDao().findById(id), UniongroupConfigures.class);
    }

    @Override
    public UniongroupConfigures findUniongroupConfiguresByTargetId(Integer namespaceId, Long TargetId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.TARGETID.eq(TargetId));
        EhUniongroupConfiguresRecord record = query.fetchOne();
        if (record != null) {
            return ConvertHelper.convert(record, UniongroupConfigures.class);
        }
        return null;
    }

    @Override
    public List<UniongroupConfigures> listUniongroupConfigures(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        List<EhUniongroupConfiguresRecord> records = query.fetch();
        List<UniongroupConfigures> result = new ArrayList<>();
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, UniongroupConfigures.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && result.size() != 0) {
            return result;
        }
        return null;
    }

    @Override
    public List<UniongroupConfigures> listUniongroupConfiguresByGroupId(Integer namespaceId, Long groupId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUPID.eq(groupId));
        List<EhUniongroupConfiguresRecord> records = query.fetch();
        List<UniongroupConfigures> result = new ArrayList<>();
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, UniongroupConfigures.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && result.size() != 0) {
            return result;
        }
        return null;
    }

    @Override
    public List<Long> listOrgTargetIdsOfUniongroupConfigures(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        Result result = query.fetch();
        if (result != null) {
            return result.getValues("targetId", Long.class);
        }
        return null;
    }

    @Override
    public void deleteUniongroupConfigres(UniongroupConfigures uniongroupConfigures) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUniongroupConfiguresDao dao = new EhUniongroupConfiguresDao(context.configuration());
        dao.delete(uniongroupConfigures);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, uniongroupConfigures.getId());
    }


    /**
     * UniongroupMemberDetail
     **/
    @Override
    public void createUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupMemberDetails.class));
        uniongroupMemberDetail.setId(id);
        uniongroupMemberDetail.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        uniongroupMemberDetail.setOperatorUid(UserContext.current().getUser().getId());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        List<UniongroupMemberDetail> list = new ArrayList<>();
        dao.insert(uniongroupMemberDetail);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUniongroupMemberDetails.class, id);
    }

    @Override
    public void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        List<EhUniongroupMemberDetails> list = new ArrayList<>();
        unionDetailList.stream().map(r -> {
            Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupMemberDetails.class));
            r.setId(id);
            r.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            r.setOperatorUid(UserContext.current().getUser().getId());
            list.add(r);
            return null;
        }).collect(Collectors.toList());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        dao.insert(list);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUniongroupMemberDetails.class, null);
    }

    @Override
    public void updateUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail) {
        assert (uniongroupMemberDetail.getId() != null);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        uniongroupMemberDetail.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        uniongroupMemberDetail.setOperatorUid(UserContext.current().getUser().getId());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        dao.update(uniongroupMemberDetail);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupMemberDetails.class, uniongroupMemberDetail.getId());
    }

    @Override
    public UniongroupMemberDetail findUniongroupMemberDetailById(Long id) {
        assert (id != null);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), UniongroupMemberDetail.class);
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUPID.eq(groupId));
        List<EhUniongroupMemberDetailsRecord> records = query.fetch();
        List<UniongroupMemberDetail> result = new ArrayList<>();
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, UniongroupMemberDetail.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && result.size() != 0) {
            return result;
        }
        return null;
    }

    @Override
    public void deleteUniongroupMemberDetailsByDetailIds(Integer namespaceId, List<Long> detailIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        DeleteQuery<EhUniongroupMemberDetailsRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAILID.in(detailIds));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, null);
    }


    /**
     * Configure
     **/

    private EhUniongroupConfiguresDao getReadWriteDao() {
        return getDao(getReadWriteContext());
    }

    private EhUniongroupConfiguresDao getReadOnlyDao() {
        return getDao(getReadOnlyContext());
    }

    private EhUniongroupConfiguresDao getDao(DSLContext context) {
        return new EhUniongroupConfiguresDao(context.configuration());
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
}
