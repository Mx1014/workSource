package com.everhomes.general_approval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralApprovalScopeMapDao;
import com.everhomes.server.schema.tables.daos.EhGeneralApprovalsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalScopeMap;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovals;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalScopeMapRecord;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalsRecord;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralApprovalProviderImpl implements GeneralApprovalProvider {
    @Autowired
    private DbProvider dbProvider;


    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createGeneralApproval(GeneralApproval obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralApprovals.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));
        obj.setId(id);
        prepareObj(obj);
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public GeneralApproval updateGeneralApproval(GeneralApproval obj) {
        if(StringUtils.isBlank(obj.getOperatorName())){
            User user = UserContext.current().getUser();
            obj.setOperatorName(user.getNickName());
        }

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));
        obj.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        dao.update(obj);
        return obj;
    }

    @Override
    public void deleteGeneralApproval(GeneralApproval obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovals.class));
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public GeneralApproval getGeneralApprovalById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhGeneralApprovalsDao dao = new EhGeneralApprovalsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), GeneralApproval.class);
    }

    @Override
    public List<GeneralApproval> queryGeneralApprovals(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_GENERAL_APPROVALS.ID.gt(locator.getAnchor()));
        }

        query.addLimit(count);
        List<GeneralApproval> objs = query.fetch().map((r) -> ConvertHelper.convert(r, GeneralApproval.class));

        if (objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(GeneralApproval obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        obj.setUpdateTime(new Timestamp(l2));
        if(StringUtils.isBlank(obj.getOperatorName())){
            User user = UserContext.current().getUser();
            obj.setOperatorName(user.getNickName());
        }
    }

    @Override
    public GeneralApproval getGeneralApprovalByName(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, String approvalName){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.APPROVAL_NAME.eq(approvalName));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
        return query.fetchAnyInto(GeneralApproval.class);
    }

    @Override
    public GeneralApproval getGeneralApprovalByNameAndRunning(Integer namespaceId, Long moduleId, Long ownerId, String ownerType){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        if(ownerType != null) {
            query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
        }
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.eq(GeneralApprovalStatus.RUNNING.getCode()));
        return query.fetchAnyInto(GeneralApproval.class);
    }

    @Override
    public GeneralApproval getGeneralApprovalByAttribute(Integer namespaceId, Long ownerId, String attribute){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.APPROVAL_ATTRIBUTE.eq(attribute));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
        return query.fetchOneInto(GeneralApproval.class);
    }

    @Override
    public void disableApprovalByFormOriginId(Long formOriginId, Long moduleId, String moduleType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhGeneralApprovalsRecord> query = context.updateQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(moduleType));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.FORM_ORIGIN_ID.eq(formOriginId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));

        query.addValue(Tables.EH_GENERAL_APPROVALS.STATUS, GeneralApprovalStatus.INVALID.getCode());
        query.addValue(Tables.EH_GENERAL_APPROVALS.FORM_ORIGIN_ID, 0L);
        query.execute();
    }

    @Caching(evict = {@CacheEvict(value = "GeneralApprovalScopes", key = "#scope.approvalId")})
    @Override
    public void createGeneralApprovalScopeMap(GeneralApprovalScopeMap scope){
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralApprovalScopeMap.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        scope.setId(id);
        scope.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhGeneralApprovalScopeMapDao dao = new EhGeneralApprovalScopeMapDao(context.configuration());
        dao.insert(scope);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGeneralApprovalScopeMap.class, null);
    }

    @Caching(evict = {@CacheEvict(value = "GeneralApprovalScopes", key = "#scope.approvalId")})
    @Override
    public void updateGeneralApprovalScopeMap(GeneralApprovalScopeMap scope){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhGeneralApprovalScopeMapDao dao = new EhGeneralApprovalScopeMapDao(context.configuration());
        dao.update(scope);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGeneralApprovalScopeMap.class, scope.getId());
    }

    @Override
    public GeneralApprovalScopeMap findGeneralApprovalScopeMap(Integer namespaceId, Long approvalId, Long sourceId, String sourceType){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalScopeMapRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP);
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.APPROVAL_ID.eq(approvalId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.SOURCE_ID.eq(sourceId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.SOURCE_TYPE.eq(sourceType));
        return query.fetchAnyInto(GeneralApprovalScopeMap.class);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "GeneralApprovalScopes", key = "#approvalId")})
    public void deleteApprovalScopeMapByApprovalId(Long approvalId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhGeneralApprovalScopeMapRecord> query = context.deleteQuery(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP);
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.APPROVAL_ID.eq(approvalId));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGeneralApprovalScopeMap.class, null);
    }

    //  delete odd detail source data
    @Caching(evict = {@CacheEvict(value = "GeneralApprovalScopes", key = "#approvalId")})
    @Override
    public void deleteOddGeneralApprovalScope(Integer namespaceId, Long approvalId, String sourceType, List<Long> detailIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        DeleteQuery<EhGeneralApprovalScopeMapRecord> query = context.deleteQuery(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP);
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.APPROVAL_ID.eq(approvalId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.SOURCE_TYPE.eq(sourceType));
        if (detailIds.size() > 0)
            query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.SOURCE_ID.notIn(detailIds));
        query.execute();
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGeneralApprovalScopeMap.class, null);
    }

    @Cacheable(value = "GeneralApprovalScopes", key = "#approvalId", unless = "#result.size() == 0")
    @Override
    public List<GeneralApprovalScopeMap> listGeneralApprovalScopes(Integer namespaceId, Long approvalId) {
        List<GeneralApprovalScopeMap> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalScopeMapRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP);
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_SCOPE_MAP.APPROVAL_ID.eq(approvalId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, GeneralApprovalScopeMap.class));
            return null;
        });
        return results;
    }
}
