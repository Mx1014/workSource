package com.everhomes.policy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPoliciesDao;
import com.everhomes.server.schema.tables.pojos.EhPolicies;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PolicyProviderImpl implements PolicyProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Policy createPolicy(Policy policy) {
        if(policy.getId() == null) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPolicies.class));
            policy.setId(id);
        }
        policy.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicies.class));
        EhPoliciesDao dao = new EhPoliciesDao(context.configuration());
        dao.insert(policy);
        return policy;
    }

    @Override
    public Policy updatePolicy(Policy policy) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicies.class));
        policy.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        EhPoliciesDao dao = new EhPoliciesDao(context.configuration());
        dao.update(policy);
        return policy;
    }

    @Override
    public void deletePolicyById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicies.class));
        EhPoliciesDao dao = new EhPoliciesDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    public Policy searchPolicyById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicies.class));
        EhPoliciesDao dao = new EhPoliciesDao(context.configuration());
        EhPolicies result = dao.findById(id);
        return ConvertHelper.convert(result,Policy.class);
    }

    @Override
    public List<Policy> listPoliciesByTitle(Integer namespaceId, String ownerType, List<Long> ownerIds, String title, Long pageAnchor, Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicies.class));
        SelectQuery query = context.selectQuery(Tables.EH_POLICIES);
        if(null != namespaceId)
            query.addConditions(Tables.EH_POLICIES.NAMESPACE_ID.eq(namespaceId));
        if(StringUtils.isNotEmpty(ownerType))
            query.addConditions(Tables.EH_POLICIES.OWNER_TYPE.eq(ownerType));
        if(ownerIds.size() > 0)
            query.addConditions(Tables.EH_POLICIES.OWNER_ID.in(ownerIds));
        if(null != title)
            query.addConditions(Tables.EH_POLICIES.TITLE.like("%" + title + "%"));
        if(null != pageAnchor)
            query.addConditions(Tables.EH_POLICIES.ID.lt(pageAnchor));
        query.addOrderBy(Tables.EH_POLICIES.ID.desc());
        query.addLimit(pageSize);
        return query.fetch().map(r->ConvertHelper.convert(r,Policy.class));
    }

    @Override
    public List<Policy> searchPoliciesByIds(Integer namespaceId, String ownerType,Long ownerId, List<Long> ids, Long pageAnchor, Integer pageSize) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicies.class));
        SelectQuery query = context.selectQuery(Tables.EH_POLICIES);
        if(null != namespaceId)
            query.addConditions(Tables.EH_POLICIES.NAMESPACE_ID.eq(namespaceId));
        if(StringUtils.isNotEmpty(ownerType))
            query.addConditions(Tables.EH_POLICIES.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
            query.addConditions(Tables.EH_POLICIES.OWNER_ID.in(ownerId,-1L));
        if(ids.size() > 0)
            query.addConditions(Tables.EH_POLICIES.ID.in(ids));
        if(null != pageAnchor)
            query.addConditions(Tables.EH_POLICIES.ID.lt(pageAnchor));
        query.addOrderBy(Tables.EH_POLICIES.ID.desc());
        query.addLimit(pageSize);
        return query.fetch().map(r->ConvertHelper.convert(r,Policy.class));
    }
}
