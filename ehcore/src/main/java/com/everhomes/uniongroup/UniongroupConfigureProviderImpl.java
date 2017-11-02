// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationMemberDetailsMapper;
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
import com.everhomes.util.RecordHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UniongroupConfigureProviderImpl.class);

    private Integer DEFAULT_VERSION_CODE = 0;

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
    public UniongroupConfigures findUniongroupConfiguresByCurrentId(Integer namespaceId, Long currentId, String groupType, Integer versionCode, String currentType) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        if(currentType != null){
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(currentType));
        }
        if (groupType != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
        }
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_ID.eq(currentId));
        if (versionCode != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        }
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
    public List<UniongroupConfigures> listUniongroupConfiguresByGroupId(Integer namespaceId, Long groupId, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID.eq(groupId));
        if(versionCode != null){
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        }else{
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));

        }
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
    public List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType) {
        return this.listOrgCurrentIdsOfUniongroupConfigures(namespaceId, enterpriseId, groupType, null);
    }

    @Override
    public List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(UniongroupTargetType.ORGANIZATION.getCode()));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
        if (versionCode != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        }
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
    public List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType) {
        return this.listDetailCurrentIdsOfUniongroupConfigures(namespaceId, enterpriseId, groupType, null);
    }

    @Override
    public List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE.eq(UniongroupTargetType.MEMBERDETAIL.getCode()));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
        if (versionCode != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        }
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
    public void deleteUniongroupConfigresByCurrentIds(Integer namespaceId, List<Long> currentIds, String groupType, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhUniongroupConfiguresRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_ID.in(currentIds));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
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
//        List<UniongroupMemberDetail> list = new ArrayList<>();
        dao.insert(uniongroupMemberDetail);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUniongroupMemberDetails.class, id);
    }

    @Override
    public void batchCreateUniongroupConfigres(List<EhUniongroupConfigures> unionConfiguresist) {
        this.batchCreateUniongroupConfigresToVersion(unionConfiguresist, null);
    }

    @Override
    public void batchCreateUniongroupConfigresToVersion(List<EhUniongroupConfigures> unionConfiguresist, Integer n2) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        List<EhUniongroupConfigures> list = new ArrayList<>();
        long beginId = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupConfigures.class),unionConfiguresist.size());
        for(EhUniongroupConfigures r : unionConfiguresist){
            r.setId(beginId);
            r.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            r.setOperatorUid(UserContext.current().getUser().getId());
            if(n2 != null){
                r.setVersionCode(n2);
            }
            list.add(r);
            beginId ++;
        }
        EhUniongroupConfiguresDao dao = new EhUniongroupConfiguresDao(context.configuration());
        LOGGER.debug("batchCreateUniongroupConfiguresDao data.size()" + list.size());
        dao.insert(list);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUniongroupMemberDetails.class, null);
    }

    @Override
    public void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList) {
        this.batchCreateUniongroupMemberDetailToVersion(unionDetailList, null);
    }

    @Override
    public void batchCreateUniongroupMemberDetailToVersion(List<EhUniongroupMemberDetails> unionDetailList, Integer n2) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        List<EhUniongroupMemberDetails> list = new ArrayList<>();
        long beginId = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupMemberDetails.class), unionDetailList.size());
        for(EhUniongroupMemberDetails r : unionDetailList){
            r.setId(beginId);
            r.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            r.setOperatorUid(UserContext.current().getUser().getId());
            if(n2 != null){
                r.setVersionCode(n2);
            }
            list.add(r);
            beginId ++;
        }
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        LOGGER.debug("batchCreateUniongroupMemberDetail data.size()" + list.size());
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
    public List<UniongroupMemberDetail> findUniongroupMemberDetailByDetailIdWithoutGroupType(Integer namespaceId, Long detailId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(detailId));
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
    public UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType) {
        return this.findUniongroupMemberDetailByDetailId(namespaceId, detailId, groupType, null);
    }

    @Override
    public UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(detailId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType));
        if (versionCode == null) {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode));
        }
        EhUniongroupMemberDetailsRecord record = query.fetchAny();
        if (record != null) {
            return ConvertHelper.convert(record, UniongroupMemberDetail.class);
        }
        return null;
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId){
        return this.listUniongroupMemberDetail(namespaceId,groupId,ownerId,null);
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId, Integer versionCode) {
        Integer versionCodeRight = versionCode != null?versionCode:DEFAULT_VERSION_CODE;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<UniongroupMemberDetail> list = context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.UPDATE_TIME,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.OPERATOR_UID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.NAMESPACE_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_TYPE,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN).from(Tables.EH_UNIONGROUP_MEMBER_DETAILS).leftOuterJoin(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID))
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCodeRight))
                .fetch().map(r -> {
                    return RecordHelper.convert(r, UniongroupMemberDetail.class);
                });
        if (list != null && list.size() != 0) {
            return list;
        }
        return null;
    }

    //获取当前版本
    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Long groupId, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<UniongroupMemberDetail> list = context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.UPDATE_TIME,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.OPERATOR_UID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.NAMESPACE_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_TYPE,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN).from(Tables.EH_UNIONGROUP_MEMBER_DETAILS).leftOuterJoin(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID))
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode == null ? DEFAULT_VERSION_CODE:versionCode))
                .fetch().map(r -> {
                    return RecordHelper.convert(r, UniongroupMemberDetail.class);
                });
        if (list != null && list.size() != 0) {
            return list;
        }
        return null;
//        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
//        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
//        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId));
//        List<EhUniongroupMemberDetailsRecord> records = query.fetch();
//        List<UniongroupMemberDetail> result = new ArrayList<>();
//        if (records != null) {
//            records.stream().map(r -> {
//                result.add(ConvertHelper.convert(r, UniongroupMemberDetail.class));
//                return null;
//            }).collect(Collectors.toList());
//        }
//        if (result != null && result.size() != 0) {
//            return result;
//        }
//        return null;
    }

    @Override
    public void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds, String groupType) {
        this.deleteUniongroupMemberDetailsByDetailIds(detailIds, groupType, null);
    }

    @Override
    public void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds, String groupType, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        DeleteQuery<EhUniongroupMemberDetailsRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        if (versionCode == null) {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode));
        }
        if (groupType != null) {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType));

        }
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.in(detailIds));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, null);
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetailByGroupType(Integer namespaceId, Long ownerId, String groupType, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<UniongroupMemberDetail> list = new ArrayList<>();
        list = context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.UPDATE_TIME,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.OPERATOR_UID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.NAMESPACE_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_TYPE,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN).from(Tables.EH_UNIONGROUP_MEMBER_DETAILS).leftOuterJoin(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID))
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode))
                .fetch().map(r -> {
                    return RecordHelper.convert(r, UniongroupMemberDetail.class);
                });

        if (list != null && list.size() != 0) {
            return list;
        }
        return null;
    }


    //  added by RN
    @Override
    public Integer countUnionGroupMemberDetailsByOrgId(Integer namespaceId, Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.select(DSL.countDistinct(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID))
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .fetchOneInto(Integer.class);
    }

    @Override
    public Integer countUnionGroupMemberDetailsByGroupId(Integer namespaceId, Long groupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        return context.select(DSL.countDistinct(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID))
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .fetchOneInto(Integer.class);
    }

    @Override
    public List<Object[]> listUniongroupMemberDetailsCount(Integer namespaceId, List<Long> groupIds, Long ownerId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID, DSL.countDistinct(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID))
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
    public void deleteUniongroupConfigresByCurrentIdAndGroupTypeAndVersion(Long detailId, String groupType, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhUniongroupConfiguresRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_ID.eq(detailId));
        if (groupType != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
        }
        if (versionCode != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        }
        query.execute();
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetailsByUserName(Long ownerId, String userName) {
         
        return  listUniongroupMemberDetailsByUserName(ownerId,userName,DEFAULT_VERSION_CODE);
    }

    @Override
    public List<UniongroupMemberDetail> listUniongroupMemberDetailsByUserName(Long ownerId, String userName, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.UPDATE_TIME,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.OPERATOR_UID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.NAMESPACE_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_ID,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.TARGET_TYPE,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME,
                Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN).from(Tables.EH_UNIONGROUP_MEMBER_DETAILS).join(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
                .on(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID))
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.CONTACT_NAME.like("%"+userName+"%"))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode))
                .fetch().map(r -> {
                    return RecordHelper.convert(r, UniongroupMemberDetail.class);
                });
    }
    @Override
    public void updateUniongroupConfiguresVersion(Integer namespaceId, String groupType, Long enterpriseId, Integer v1, Integer v2) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        UpdateQuery<EhUniongroupConfiguresRecord> query = context.updateQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addValue(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE, v2);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(v1));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.execute();
    }

    @Override
    public void updateUniongroupMemberDetailsVersion(Integer namespaceId, String groupType, Long enterpriseId, Integer v1, Integer v2) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        UpdateQuery<EhUniongroupMemberDetailsRecord> query = context.updateQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addValue(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE, v2);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(v1));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.execute();
    }

    @Override
    public void deleteUniongroupConfigresByEnterpriseIdAndGroupType(Integer namespaceId, String groupType, Long enterpriseId, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhUniongroupConfiguresRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, null);
    }

    @Override
    public void deleteUniongroupMemberDetailsByEnterpriseIdAndGroupType(Integer namespaceId, String groupType, Long enterpriseId, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhUniongroupMemberDetailsRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupMemberDetailsDao.class, null);
    }

//    @Override
//    public void cloneGroupTypeDataToVersion(Integer namespaceId, Long enterpriseId, String groupType, Integer n1, Integer n2) {
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
////        InsertQuery<EhUniongroupConfiguresRecord> query = context.insertQuery(Tables.EH_UNIONGROUP_CONFIGURES);
////        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUniongroupConfigures.class));
////        context.execute("SET @id = " + id);
////        Field<Long> f1 = DSL.field("@id:=@id+1", Long.class);
////
////        Insert<EhUniongroupConfiguresRecord> qq =  context.insertInto(Tables.EH_UNIONGROUP_CONFIGURES).select(context.select(f1,
////                Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID,
////                Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID,
////                Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE,
////                Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID,
////                Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_ID,
////                Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_TYPE,
////                Tables.EH_UNIONGROUP_CONFIGURES.CURRENT_NAME,
////                Tables.EH_UNIONGROUP_CONFIGURES.OPERATOR_UID,
////                Tables.EH_UNIONGROUP_CONFIGURES.UPDATE_TIME,
////                Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE
////                ).from(Tables.EH_UNIONGROUP_CONFIGURES));
////
////        String q = qq.getSQL();
////
//////        context.execute("SET @id = " + id + ";\r\n" + q + ";");
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(1),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(2),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(3),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(4),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(5),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(6),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(7),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(8),
//////                Tables.EH_UNIONGROUP_CONFIGURES.field(9)
//////                ).from(Tables.EH_UNIONGROUP_CONFIGURES).execute();
//    }

    @Override
    public List<EhUniongroupConfigures> listUniongroupConfigures(Integer namespaceId, String groupType, Long enterpriseId, Long groupId, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupConfiguresRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        List<EhUniongroupConfigures> result = new ArrayList<>();
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.NAMESPACE_ID.eq(namespaceId));
        if(groupType != null){
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_TYPE.eq(groupType));
        }
        if(enterpriseId != null){
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(enterpriseId));
        }
        if(groupId != null){
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID.eq(groupId));

        }
        if(versionCode != null){
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        }else{
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));

        }
        List<EhUniongroupConfiguresRecord> records = query.fetch();
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, EhUniongroupConfigures.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && result.size() != 0) {
            return result;
        }
        return null;
    }

    @Override
    public List<EhUniongroupMemberDetails> listUniongroupMemberDetail(Integer namespaceId, String groupType, Long enterpriseId, Long groupId, Integer versionCode) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUniongroupMemberDetailsRecord> query = context.selectQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        List<EhUniongroupMemberDetails> result = new ArrayList<>();
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId));
        if(groupType != null){
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_TYPE.eq(groupType));
        }
        if(enterpriseId != null){
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(enterpriseId));
        }
        if(groupId != null){
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId));

        }
        if(versionCode != null){
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode));
        }else{
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(DEFAULT_VERSION_CODE));

        }
        List<EhUniongroupMemberDetailsRecord> records = query.fetch();
        if (records != null) {
            records.stream().map(r -> {
                result.add(ConvertHelper.convert(r, EhUniongroupMemberDetails.class));
                return null;
            }).collect(Collectors.toList());
        }
        if (result != null && result.size() != 0) {
            return result;
        }
        return null;
    }

    @Override
    public void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId) {
        this.deleteUniongroupConfigresByGroupId(groupId, organizationId, null);
    }

    @Override
    public void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhUniongroupConfiguresRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_CONFIGURES);
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.GROUP_ID.eq(groupId));
        query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.ENTERPRISE_ID.eq(organizationId));
        if (versionCode != null) {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(versionCode));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_CONFIGURES.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        }
        query.execute();
    }

    @Override
    public void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId) {
        this.deleteUniongroupMemberDetailByGroupId(groupId, organizationId, null);
    }

    @Override
    public void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId, Integer versionCode) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhUniongroupMemberDetailsRecord> query = context.deleteQuery(Tables.EH_UNIONGROUP_MEMBER_DETAILS);
        if (versionCode == null) {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(DEFAULT_VERSION_CODE));
        } else {
            query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.VERSION_CODE.eq(versionCode));
        }
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID.eq(groupId));
        query.addConditions(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(organizationId));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, null);
    }


    @Override
    public void deleteUniongroupMemberDetails(UniongroupMemberDetail uniongroupMemberDetail) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhUniongroupMemberDetailsDao dao = new EhUniongroupMemberDetailsDao(context.configuration());
        dao.delete(uniongroupMemberDetail);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUniongroupConfiguresDao.class, uniongroupMemberDetail.getId());
    }

    @Override
    public List<Object[]> listUniongroupMemberGroupIds(Integer namespaceId, Long ownerId) {
        return getReadOnlyContext().select(Tables.EH_UNIONGROUP_MEMBER_DETAILS.DETAIL_ID,
                Tables.EH_UNIONGROUP_MEMBER_DETAILS.GROUP_ID)
                .from(Tables.EH_UNIONGROUP_MEMBER_DETAILS)
                .where(Tables.EH_UNIONGROUP_MEMBER_DETAILS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_UNIONGROUP_MEMBER_DETAILS.ENTERPRISE_ID.eq(ownerId))
                .fetchInto(Object[].class);
    }

    @Override
    public List listDetailNotInUniongroup(Integer namespaceId, Long organizationId, String contactName, Integer versionCode, Long departmentId) {
        CrossShardListingLocator locator = new CrossShardListingLocator(0L);
        return listDetailNotInUniongroup(namespaceId, organizationId, contactName, versionCode, departmentId, 99999,locator);
    }

    @Override
    public List listDetailNotInUniongroup(Integer namespaceId, Long organizationId, String contactName, Integer versionCode, Long departmentId, Integer pageSize, CrossShardListingLocator locator) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        pageSize = pageSize + 1;
        /**modify by lei lv,增加了detail表，部分信息挪到detail表里去取**/
        TableLike t1 = Tables.EH_ORGANIZATION_MEMBER_DETAILS.as("t1");
        TableLike t2 = Tables.EH_UNIONGROUP_MEMBER_DETAILS.as("t2");
        TableLike t3 = Tables.EH_ORGANIZATION_MEMBERS.as("t3");
        SelectJoinStep step = null;
        if(versionCode != null){
            step = context.select(t1.fields()).from(t1).leftOuterJoin(t2).on(t2.field("detail_id").eq(t1.field("id"))).and(t2.field("version_code").eq(versionCode)).leftOuterJoin(t3).on(t1.field("id").eq(t3.field("detail_id"))).and(t3.field("status").eq(OrganizationMemberStatus.ACTIVE.getCode()));
        }else{
            step = context.select(t1.fields()).from(t1).leftOuterJoin(t2).on(t2.field("detail_id").eq(t1.field("id")));
        }

        Condition condition = t1.field("organization_id").eq(organizationId).and(t1.field("namespace_id").eq(namespaceId)).and(t2.field("detail_id").isNull());
        if (null != locator.getAnchor())
            condition = condition.and(t1.field("id").gt(locator.getAnchor()));

        if(StringUtils.isNotEmpty(contactName)){
            condition = condition.and(t1.field("contact_name").like(contactName +"%"));
        }
        if(departmentId != null){
            condition = condition.and(t3.field("organization_id").eq(departmentId));
        }
        condition = condition.and(t1.field("id").in(context.selectDistinct(Tables.EH_ORGANIZATION_MEMBERS.DETAIL_ID).from(Tables.EH_ORGANIZATION_MEMBERS).where(Tables.EH_ORGANIZATION_MEMBERS.STATUS.eq(OrganizationMemberStatus.ACTIVE.getCode()))));


        List<OrganizationMemberDetails> details = step.where(condition).groupBy(t1.field("target_id")).orderBy(t1.field("id")).limit(pageSize).fetch().map(new OrganizationMemberDetailsMapper());
        LOGGER.debug("listDetailNotInUniongroup 's sql is :" + step.where(condition).getSQL());

        locator.setAnchor(null);
        if (details.size() >= pageSize) {
            details.remove(details.size() - 1);
            locator.setAnchor(details.get(details.size() - 1).getId());
        }

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
