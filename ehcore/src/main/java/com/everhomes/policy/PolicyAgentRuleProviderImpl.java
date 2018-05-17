package com.everhomes.policy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPolicyAgentRules;
import com.everhomes.server.schema.tables.daos.EhPolicyAgentRulesDao;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PolicyAgentRuleProviderImpl implements PolicyAgentRuleProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public PolicyAgentRule createPolicyAgentRule(PolicyAgentRule bean) {
        if(bean.getId() == null) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPolicyAgentRules.class));
            bean.setId(id);
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyAgentRules.class));
        EhPolicyAgentRulesDao dao = new EhPolicyAgentRulesDao(context.configuration());
        bean.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(bean);
        return bean;
    }

    @Override
    public PolicyAgentRule updatePolicyAgentRule(PolicyAgentRule bean) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyAgentRules.class));
        EhPolicyAgentRulesDao dao = new EhPolicyAgentRulesDao(context.configuration());
        bean.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(bean);
        return bean;
    }

    @Override
    public PolicyAgentRule searchPolicyAgentRuleById(Integer namespaceId, String ownerType, Long ownerId, Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyAgentRules.class));
        SelectQuery query = context.selectQuery(Tables.EH_POLICY_AGENT_RULES);
        if(null != namespaceId)
            query.addConditions(Tables.EH_POLICY_AGENT_RULES.NAMESPACE_ID.eq(namespaceId));
        if(null != ownerType)
            query.addConditions(Tables.EH_POLICY_AGENT_RULES.OWNER_TYPE.eq(ownerType));
        if(null != ownerId)
            query.addConditions(Tables.EH_POLICY_AGENT_RULES.OWNER_ID.eq(ownerId));
        if(null != id)
            query.addConditions(Tables.EH_POLICY_AGENT_RULES.ID.eq(id));
        Record result = query.fetchOne();
        return result == null ? null : ConvertHelper.convert(result,PolicyAgentRule.class);
    }
}
