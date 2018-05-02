package com.everhomes.enterpriseApproval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhEnterpriseApprovalGroupsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseApprovalTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

    @Override
    public GeneralApproval getGeneralApprovalByTemplateId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.APPROVAL_TEMPLATE_ID.eq(templateId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
        return query.fetchAnyInto(GeneralApproval.class);
    }

    @Override
    public List<EnterpriseApprovalTemplate> listEnterpriseApprovalTemplateByModuleId(Long moduleId) {
        List<EnterpriseApprovalTemplate> results = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhEnterpriseApprovalTemplatesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_APPROVAL_TEMPLATES);
        query.addConditions(Tables.EH_ENTERPRISE_APPROVAL_TEMPLATES.MODULE_ID.eq(moduleId));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, EnterpriseApprovalTemplate.class));
            return null;
        });
        return results;
    }
}
