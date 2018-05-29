package com.everhomes.policy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPolicyCategories;
import com.everhomes.server.schema.tables.daos.EhPolicyCategoriesDao;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PolicyCategoryProviderImpl implements PolicyCategoryProvider{

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public PolicyCategory createCategory(PolicyCategory bean) {
        if(bean.getId() == null) {
            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPolicyCategories.class));
            bean.setId(id);
        }
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyCategories.class));
        EhPolicyCategoriesDao dao = new EhPolicyCategoriesDao(context.configuration());
        dao.insert(bean);
        return bean;
    }

    @Override
    public PolicyCategory updateCategory(PolicyCategory bean) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyCategories.class));
        EhPolicyCategoriesDao dao = new EhPolicyCategoriesDao(context.configuration());
        dao.update(bean);
        return bean;
    }

    @Override
    public void deleteCategory(PolicyCategory bean) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyCategories.class));
        EhPolicyCategoriesDao dao = new EhPolicyCategoriesDao(context.configuration());
        dao.delete(bean);
    }

    @Override
    public List<PolicyCategory> searchCategoryByPolicyId(Long policyId,Byte actFlag) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyCategories.class));
        SelectQuery query = context.selectQuery(Tables.EH_POLICY_CATEGORIES);
        if(null != policyId)
            query.addConditions(Tables.EH_POLICY_CATEGORIES.POLICY_ID.eq(policyId));
        if(null != actFlag)
            query.addConditions(Tables.EH_POLICY_CATEGORIES.ACTIVE_FLAG.eq(actFlag));
        List<PolicyCategory> results = query.fetch().map(r -> ConvertHelper.convert(r,PolicyCategory.class));
        return results;
    }

    @Override
    public List<PolicyCategory> searchPolicyByCategory(Long category) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhPolicyCategories.class));
        SelectQuery query = context.selectQuery(Tables.EH_POLICY_CATEGORIES);
        if(null != category)
            query.addConditions(Tables.EH_POLICY_CATEGORIES.CATEGORY_ID.eq(category));
        query.addConditions(Tables.EH_POLICY_CATEGORIES.ACTIVE_FLAG.eq((byte)1));
        List<PolicyCategory> results = query.fetch().map(r -> ConvertHelper.convert(r,PolicyCategory.class));
        return results;
    }
}
