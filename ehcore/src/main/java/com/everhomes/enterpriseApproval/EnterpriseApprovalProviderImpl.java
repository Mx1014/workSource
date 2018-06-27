package com.everhomes.enterpriseApproval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseApprovalGroupsDao;
import com.everhomes.server.schema.tables.records.EhEnterpriseApprovalGroupsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseApprovalTemplatesRecord;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalsRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnterpriseApprovalProviderImpl implements EnterpriseApprovalProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public EnterpriseApprovalGroup findEnterpriseApprovalGroup(Long id){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhEnterpriseApprovalGroupsDao dao = new EhEnterpriseApprovalGroupsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), EnterpriseApprovalGroup.class);
    }

    @Override
    public List<EnterpriseApprovalGroup> listEnterpriseApprovalGroups() {
        List<EnterpriseApprovalGroup> results = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseApprovalGroupsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_APPROVAL_GROUPS);
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, EnterpriseApprovalGroup.class));
            return null;
        });
        return results;
    }

    @Override
    public GeneralApproval getGeneralApprovalByTemplateId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId) {
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
    public Integer countGeneralApprovalInTemplateIds(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, List<Long> templateIds){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.APPROVAL_TEMPLATE_ID.in(templateIds));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
        return query.fetchCount();
    }

    @Override
    public List<EnterpriseApprovalTemplate> listEnterpriseApprovalTemplateByModuleId(Long moduleId, boolean defaultFlag) {
        List<EnterpriseApprovalTemplate> results = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhEnterpriseApprovalTemplatesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_APPROVAL_TEMPLATES);
        query.addConditions(Tables.EH_ENTERPRISE_APPROVAL_TEMPLATES.MODULE_ID.eq(moduleId));
        //  可能存在 非系统审批 需要默认生成
        if(defaultFlag)
        query.addConditions(Tables.EH_ENTERPRISE_APPROVAL_TEMPLATES.APPROVAL_ATTRIBUTE.ne(GeneralApprovalAttribute.CUSTOMIZE.getCode()));
        query.fetch().map(r -> {
            results.add(ConvertHelper.convert(r, EnterpriseApprovalTemplate.class));
            return null;
        });
        return results;
    }

    @Override
    public List<Long> listGeneralApprovalIdsByGroupId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long approvalGroupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addSelect(Tables.EH_GENERAL_APPROVALS.ID);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(EnterpriseApprovalCustomField.APPROVAL_GROUP_ID.getField().eq(approvalGroupId));

        List<Long> results = query.fetchInto(Long.class);
        if (results != null && results.size() > 0)
            return results;

        return Collections.singletonList(0L);
    }

    @Override
    public GeneralApproval findEnterpriseApprovalByName(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, String name, Long groupId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVALS);
        query.addConditions(Tables.EH_GENERAL_APPROVALS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS.ne(GeneralApprovalStatus.DELETED.getCode()));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(moduleId));
        query.addConditions(Tables.EH_GENERAL_APPROVALS.APPROVAL_NAME.eq(name));
        query.addConditions(EnterpriseApprovalCustomField.APPROVAL_GROUP_ID.getField().eq(groupId));
        return ConvertHelper.convert(query.fetchAny(), GeneralApproval.class);
    }
}
