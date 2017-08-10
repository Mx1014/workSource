// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationMemberDetailsMapper;
import com.everhomes.organization.pmsy.OrganizationMemberRecordMapper;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
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
import org.jooq.impl.DSL;
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
    public UniongroupConfigures findUniongroupConfiguresByCurrentId(Integer namespaceId, Long currentId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_ID.eq(currentId));
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
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID.eq(groupId));
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
    public List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(UniongroupTargetType.ORGANIZATION.getCode()));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        Result result = query.fetch();
        if (result != null) {
            return result.getValues("current_id", Long.class);
        }
        return null;
    }

    @Override
    public List<Long> listOrgCurrentIdsOfUniongroupConfiguresByGroupId(Integer namespaceId, Long enterpriseId, Long groupId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(UniongroupTargetType.ORGANIZATION.getCode()));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID.eq(groupId));
        Result result = query.fetch();
        if (result != null) {
            return result.getValues("current_id", Long.class);
        }
        return null;
    }

    @Override
    public List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(UniongroupTargetType.MEMBERDETAIL.getCode()));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        Result result = query.fetch();
        if (result != null) {
            return result.getValues("current_id", Long.class);
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

    @Override
    public void deleteUniongroupConfigresByOrgIds(Integer namespaceId, List<Long> orgIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUniongroupConfiguresDao dao = new EhUniongroupConfiguresDao(context.configuration());
        DeleteQuery<EhUniongroupConfiguresRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_ID.in(orgIds));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(UniongroupTargetType.ORGANIZATION.getCode()));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, null);
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
    public UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(detailId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType));
        EhUniongroupMemberDetailsRecord record = query.fetchAny();
        if(record != null) {
            return ConvertHelper.convert(record, UniongroupMemberDetail.class);
        }
        return null;
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId));
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
    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Long groupId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId));
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
    public void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        DeleteQuery<EhUniongroupMemberDetailsRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.in(detailIds));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, null);
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetailByGroupType(Integer namespaceId, Long ownerId, Long groupId, String groupType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId));
        if(groupId!= null && groupId != 0L){
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId));
        }
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


    //  added by RN
    @Override
    public Integer countUnionGroupMemberDetailsByOrgId(Integer namespaceId, Long ownerId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.select(DSL.countDistinct(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID))
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .fetchOneInto(Integer.class);
    }

    @Override
    public List<Object[]> listUniongroupMemberDetailsCount(Integer namespaceId, List<Long> groupIds, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return   context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID, DSL.countDistinct(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID))
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.in(groupIds))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .groupBy(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID)
                .fetchInto(Object[].class);
    }

    @Override
    public List<Object[]> listUniongroupMemberDetailsInfo(Integer namespaceId, Long groupId, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.CONTACT_TOKEN,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.TARGET_ID, Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID)
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .fetchInto(Object[].class);
    }

    @Override
    public void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_UNIONGROUP_CONFIGURES)
                .where(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID.eq(groupId))
                .and(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(organizationId))
                .execute();
    }

    @Override
    public void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(organizationId))
                .execute();
    }

    @Override
    public List<Object[]> listUniongroupMemberGroupIds(Integer namespaceId, Long ownerId){
        return getReadOnlyContext().select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID)
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .fetchInto(Object[].class);
    }

    @Override
    public List listDetailNotInUniongroup(Integer namespaceId, Long organizationId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t1");
        TableLike t2 = Tables.EH_UNIONGROUP_MEMBER_DETAILS.as("t2");
        SelectJoinStep step = context.select(t1.fields()).from(t1).leftOuterJoin(t2).on(t2.field("detail_id").eq(t1.field("id")));
        Condition condition = t1.field("organization_id").eq(organizationId).and(t1.field("namespace_id").eq(namespaceId)).and(t2.field("detail_id").isNull());
        List<OrganizationMemberDetails> details = step.where(condition).fetch().map(new OrganizationMemberDetailsMapper());
        step.close();
        return details;
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
