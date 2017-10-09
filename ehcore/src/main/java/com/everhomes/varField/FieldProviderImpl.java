package com.everhomes.varField;

import com.everhomes.customer.CustomerTalent;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.varField.VarFieldStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;

import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.pojos.EhCustomerApplyProjects;
import com.everhomes.server.schema.tables.pojos.EhCustomerCommercials;
import com.everhomes.server.schema.tables.pojos.EhCustomerEconomicIndicators;
import com.everhomes.server.schema.tables.pojos.EhCustomerInvestments;
import com.everhomes.server.schema.tables.pojos.EhCustomerPatents;
import com.everhomes.server.schema.tables.pojos.EhCustomerTalents;
import com.everhomes.server.schema.tables.pojos.EhCustomerTrademarks;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomers;

import com.everhomes.server.schema.tables.daos.EhVarFieldGroupScopesDao;
import com.everhomes.server.schema.tables.daos.EhVarFieldItemScopesDao;
import com.everhomes.server.schema.tables.daos.EhVarFieldScopesDao;
import com.everhomes.server.schema.tables.pojos.EhVarFieldGroupScopes;
import com.everhomes.server.schema.tables.pojos.EhVarFieldItemScopes;
import com.everhomes.server.schema.tables.pojos.EhVarFieldScopes;
import com.everhomes.server.schema.tables.records.EhVarFieldGroupScopesRecord;

import com.everhomes.server.schema.tables.records.EhVarFieldItemScopesRecord;
import com.everhomes.server.schema.tables.records.EhVarFieldScopesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/3.
 */
@Component
public class FieldProviderImpl implements FieldProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createScopeField(ScopeField scopeField) {
        LOGGER.info("createScopeField: {}", StringHelper.toJsonString(scopeField));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVarFieldScopes.class));
        scopeField.setId(id);
        scopeField.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        scopeField.setStatus(VarFieldStatus.ACTIVE.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldScopesDao dao = new EhVarFieldScopesDao(context.configuration());
        dao.insert(scopeField);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhVarFieldScopes.class, null);
    }

    @Override
    public void createScopeFieldGroup(ScopeFieldGroup scopeGroup) {
        LOGGER.info("createScopeFieldGroup: {}", StringHelper.toJsonString(scopeGroup));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVarFieldGroupScopes.class));
        scopeGroup.setId(id);
        scopeGroup.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        scopeGroup.setStatus(VarFieldStatus.ACTIVE.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldGroupScopesDao dao = new EhVarFieldGroupScopesDao(context.configuration());
        dao.insert(scopeGroup);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhVarFieldGroupScopes.class, null);
    }

    @Override
    public void createScopeFieldItem(ScopeFieldItem scopeFieldItem) {
        LOGGER.info("createScopeFieldItem: {}", StringHelper.toJsonString(scopeFieldItem));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVarFieldItemScopes.class));
        scopeFieldItem.setId(id);
        scopeFieldItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        scopeFieldItem.setStatus(VarFieldStatus.ACTIVE.getCode());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldItemScopesDao dao = new EhVarFieldItemScopesDao(context.configuration());
        dao.insert(scopeFieldItem);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhVarFieldItemScopes.class, null);

    }

    @Override
    public ScopeField findScopeField(Long id, Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeField> fields = new ArrayList<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.isNull());
        }
        query.fetch().map((r) -> {
            fields.add(ConvertHelper.convert(r, ScopeField.class));
            return null;
        });

        if(fields.size() > 0) {
            return fields.get(0);
        }
        return null;
    }

    @Override
    public ScopeFieldGroup findScopeFieldGroup(Long id, Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldGroup> groups = new ArrayList<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.isNull());
        }
        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, ScopeFieldGroup.class));
            return null;
        });

        if(groups.size() > 0) {
            return groups.get(0);
        }
        return null;
    }

    @Override
    public ScopeFieldItem findScopeFieldItem(Long id, Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.isNull());
        }
        query.fetch().map((r) -> {
            items.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(items.size() > 0) {
            return items.get(0);
        }
        return null;
    }

    @Override
    public void updateScopeField(ScopeField scopeField) {
        LOGGER.debug("updateScopeField: {}",
                StringHelper.toJsonString(scopeField));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldScopesDao dao = new EhVarFieldScopesDao(context.configuration());
        scopeField.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(scopeField);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVarFieldScopes.class, scopeField.getId());
    }

    @Override
    public void updateScopeFieldGroup(ScopeFieldGroup scopeGroup) {
        LOGGER.debug("updateScopeFieldGroup: {}",
                StringHelper.toJsonString(scopeGroup));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldGroupScopesDao dao = new EhVarFieldGroupScopesDao(context.configuration());
        scopeGroup.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(scopeGroup);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVarFieldGroupScopes.class, scopeGroup.getId());
    }

    @Override
    public void updateScopeFieldItem(ScopeFieldItem scopeFieldItem) {
        LOGGER.debug("updateScopeFieldItem: {}",
                StringHelper.toJsonString(scopeFieldItem));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldItemScopesDao dao = new EhVarFieldItemScopesDao(context.configuration());
        scopeFieldItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(scopeFieldItem);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhVarFieldItemScopes.class, scopeFieldItem.getId());
    }

    @Override
    public Map<Long, ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long communityId, String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeFieldGroup> groups = new HashMap<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.isNull());
        }
        query.fetch().map((r) -> {
            groups.put(r.getId(), ConvertHelper.convert(r, ScopeFieldGroup.class));
            return null;
        });

        return groups;
    }

    @Override
    public List<FieldGroup> listFieldGroups(List<Long> ids) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<FieldGroup> groups = context.select().from(Tables.EH_VAR_FIELD_GROUPS)
                .where(Tables.EH_VAR_FIELD_GROUPS.ID.in(ids))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, FieldGroup.class);
                });

        return groups;
    }

    @Override
    public List<FieldGroup> listFieldGroups(String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<FieldGroup> groups = context.select().from(Tables.EH_VAR_FIELD_GROUPS)
                .where(Tables.EH_VAR_FIELD_GROUPS.MODULE_NAME.eq(moduleName))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, FieldGroup.class);
                });

        return groups;
    }

    @Override
    public Map<Long, ScopeField> listScopeFields(Integer namespaceId, Long communityId, String moduleName, String groupPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeField> fields = new HashMap<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.GROUP_PATH.like(groupPath + "/%"));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.isNull());
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("listScopeFields, sql=" + query.getSQL());
            LOGGER.debug("listScopeFields, bindValues=" + query.getBindValues());
        }
        query.fetch().map((record)-> {
            fields.put(record.getId(), ConvertHelper.convert(record, ScopeField.class));
            return null;
        });

        return fields;
    }

    @Override
    public ScopeField findScopeField(Integer namespaceId, Long communityId, Long fieldId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeField> fields = new ArrayList<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.FIELD_ID.eq(fieldId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.isNull());
        }
        query.fetch().map((record)-> {
            fields.add(ConvertHelper.convert(record, ScopeField.class));
            return null;
        });

        if(fields == null || fields.size() == 0) {
            return null;
        }
        return fields.get(0);
    }

    /**
     *
     *
     */
    @Override
    public void saveFieldGroups(String customerType, Long customerId, List<Object> objects, String simpleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        switch(simpleName){
            case "CustomerTalent":
                EhCustomerTalentsDao dao = new EhCustomerTalentsDao(context.configuration());
                List<EhCustomerTalents> list = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list.add((EhCustomerTalents)objects.get(i));
                }
                dao.insert(list);
                break;
            case "CustomerTrademark":
                EhCustomerTrademarksDao dao1 = new EhCustomerTrademarksDao(context.configuration());
                List<EhCustomerTrademarks> list1 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list1.add((EhCustomerTrademarks)objects.get(i));
                }
                dao1.insert(list1);
                break;
            case "CustomerApplyProject":
                EhCustomerApplyProjectsDao dao2 = new EhCustomerApplyProjectsDao(context.configuration());
                List<EhCustomerApplyProjects> list2 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list2.add((EhCustomerApplyProjects)objects.get(i));
                }
                dao2.insert(list2);
                break;
            case "CustomerCommercial":
                EhCustomerCommercialsDao dao3 = new EhCustomerCommercialsDao(context.configuration());
                List<EhCustomerCommercials> list3 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list3.add((EhCustomerCommercials)objects.get(i));
                }
                dao3.insert(list3);
                break;
            case "CustomerInvestment":
                EhCustomerInvestmentsDao dao4 = new EhCustomerInvestmentsDao(context.configuration());
                List<EhCustomerInvestments> list4 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list4.add((EhCustomerInvestments)objects.get(i));
                }
                dao4.insert(list4);
                break;
            case "CustomerEconomicIndicator":
                EhCustomerEconomicIndicatorsDao dao5 = new EhCustomerEconomicIndicatorsDao(context.configuration());
                List<EhCustomerEconomicIndicators> list5 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list5.add((EhCustomerEconomicIndicators)objects.get(i));
                }
                dao5.insert(list5);
                break;
            case "EnterpriseCustomer":
                EhEnterpriseCustomersDao dao6 = new EhEnterpriseCustomersDao(context.configuration());
                List<EhEnterpriseCustomers> list6 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list6.add((EhEnterpriseCustomers)objects.get(i));
                }
                dao6.insert(list6);
                break;
            case "CustomerPatent":
                EhCustomerPatentsDao dao7 = new EhCustomerPatentsDao(context.configuration());
                List<EhCustomerPatents> list7 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list7.add((EhCustomerPatents)objects.get(i));
                }
                dao7.insert(list7);
                break;
        }
    }

    @Override
    public String findClassNameByGroupDisplayName(String groupDisplayName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhVarFieldGroups t = Tables.EH_VAR_FIELD_GROUPS.as("t");
        return context.select(t.NAME)
                .from(t)
                .where(t.TITLE.eq(groupDisplayName))
                .fetchOne(t.NAME);
    }

    @Override
    public List<Field> listFields(List<Long> ids) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<Field> fields = context.select().from(Tables.EH_VAR_FIELDS)
                .where(Tables.EH_VAR_FIELDS.ID.in(ids))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, Field.class);
                });

        return fields;
    }

    @Override
    public List<Field> listFields(String moduleName, String groupPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<Field> fields = context.select().from(Tables.EH_VAR_FIELDS)
                .where(Tables.EH_VAR_FIELDS.MODULE_NAME.eq(moduleName))
                .and(Tables.EH_VAR_FIELDS.GROUP_PATH.like(groupPath+"%"))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, Field.class);
                });

        return fields;
    }

    @Override
    public List<FieldItem> listFieldItems(List<Long> fieldIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<FieldItem> items = context.select().from(Tables.EH_VAR_FIELD_ITEMS)
                .where(Tables.EH_VAR_FIELD_ITEMS.FIELD_ID.in(fieldIds))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, FieldItem.class);
                });

        return items;
    }

    @Override
    public List<FieldItem> listFieldItems(Long fieldId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<FieldItem> items = context.select().from(Tables.EH_VAR_FIELD_ITEMS)
                .where(Tables.EH_VAR_FIELD_ITEMS.FIELD_ID.eq(fieldId))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, FieldItem.class);
                });

        return items;
    }

    @Override
    public List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId, Long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.eq(fieldId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.isNull());
        }

        query.fetch().map((r) -> {
            items.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        return items;
    }

    @Override
    public Map<Long, ScopeFieldItem> listScopeFieldsItems(List<Long> fieldIds, Integer namespaceId, Long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeFieldItem> items = new HashMap<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.in(fieldIds));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.isNull());
        }

        query.fetch().map((r) -> {
            items.put(r.getId(), ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        return items;
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long communityId, Long itemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_ID.eq(itemId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.isNull());
        }
        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId, String moduleName, String displayName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_DISPLAY_NAME.eq(displayName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.isNull());
        }

        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }
}
