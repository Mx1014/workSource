package com.everhomes.enterpriseApproval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhEnterpriseApprovalGroupsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseApprovalProviderImpl implements EnterpriseApprovalProvider{

    @Autowired
    private DbProvider dbProvider;

    @Override
    public List<EnterpriseApprovalGroup> listEnterpriseApprovalGroups() {
        List<EnterpriseApprovalGroup> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseApprovalGroupsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_APPROVAL_GROUPS);
        query.fetch().map(r ->{
            results.add(ConvertHelper.convert(r, EnterpriseApprovalGroup.class));
            return null;
        });
        return results;
    }
}
