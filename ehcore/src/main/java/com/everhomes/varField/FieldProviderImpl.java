package com.everhomes.varField;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.varField.VarFieldStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
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
    public void createFieldItem(FieldItem item) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVarFieldItems.class));
        item.setId(id);
        item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldItemsDao dao = new EhVarFieldItemsDao(context.configuration());
        dao.insert(item);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhVarFieldItems.class, item.getId());
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
    public ScopeField findScopeField(Long id, Integer namespaceId,Long  ownerId, Long communityId, Long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeField> fields = new ArrayList<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        
        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.CATEGORY_ID.isNull());
        }

        if(ownerId!=null){
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.OWNER_ID.eq(ownerId));
        }

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
    public ScopeFieldGroup findScopeFieldGroup(Long id, Integer namespaceId, Long communityId, Long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldGroup> groups = new ArrayList<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        
        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.CATEGORY_ID.isNull());
        }
        
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
    public ScopeFieldItem findScopeFieldItem(Long id, Integer namespaceId, Long communityId, Long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        
        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.isNull());
        }
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
    public Map<Long, ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId,Long ownerId, Long communityId, String moduleName, Long categoryId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeFieldGroup> groups = new HashMap<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        if (ownerId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.OWNER_ID.eq(ownerId));

        }
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.CATEGORY_ID.isNull());
        }
        
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
    public FieldGroup findFieldGroup(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhVarFieldGroupsDao dao = new EhVarFieldGroupsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), FieldGroup.class);
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
    public List<Long> listFieldGroupRanges(String moduleName,String moduleType){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<Long> ids = new ArrayList<>();
        SelectQuery<EhVarFieldGroupRangesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_RANGES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_RANGES.MODULE_TYPE.eq(moduleType));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_RANGES.MODULE_NAME.eq(moduleName));

        query.fetch().map((record)-> ids.add(record.getGroupId()));
        return ids;
    }



    @Override
    public Map<Long, ScopeField> listScopeFields(Integer namespaceId,Long  ownerId, Long communityId, String moduleName, String groupPath, Long categoryId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeField> fields = new HashMap<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        //动态字段跟园区走，不跟管理公司
        if (ownerId != null && ownerId != 0) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.OWNER_ID.eq(ownerId));
        }
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.MODULE_NAME.eq(moduleName));
        //query.addConditions(Tables.EH_VAR_FIELD_SCOPES.GROUP_PATH.like(groupPath + "/%"));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        
        if(groupPath != null) {
        	query.addConditions(Tables.EH_VAR_FIELD_SCOPES.GROUP_PATH.like(groupPath + "/%"));
        }
        
        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_SCOPES.CATEGORY_ID.isNull());
        }
        
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

    @Override
    public Field findField(Long groupId, String fieldName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<Field> fields = new ArrayList<>();
        SelectQuery<EhVarFieldsRecord> query = context.selectQuery(Tables.EH_VAR_FIELDS);
        query.addConditions(Tables.EH_VAR_FIELDS.NAME.eq(fieldName));
        query.addConditions(Tables.EH_VAR_FIELDS.GROUP_ID.eq(groupId));
        query.addConditions(Tables.EH_VAR_FIELDS.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        query.fetch().map((record)-> {
            fields.add(ConvertHelper.convert(record, Field.class));
            return null;
        });

        if(fields == null || fields.size() == 0) {
            return null;
        }
        return fields.get(0);
    }

    @Override
    public ScopeField findScopeField(Integer namespaceId, Long communityId, Long groupId, String fieldDisplayName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeField> fields = new ArrayList<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.FIELD_DISPLAY_NAME.eq(fieldDisplayName));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.GROUP_ID.eq(groupId));
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

    @Override
    public Field findField(String moduleName, String name, String groupPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<Field> fields = new ArrayList<>();
        SelectQuery<EhVarFieldsRecord> query = context.selectQuery(Tables.EH_VAR_FIELDS);
        query.addConditions(Tables.EH_VAR_FIELDS.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELDS.NAME.eq(name));
        query.addConditions(Tables.EH_VAR_FIELDS.GROUP_PATH.like(groupPath + "%"));
        query.addConditions(Tables.EH_VAR_FIELDS.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        query.fetch().map((record)-> {
            fields.add(ConvertHelper.convert(record, Field.class));
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
            case "CustomerCertificate":
                EhCustomerCertificatesDao dao8 = new EhCustomerCertificatesDao(context.configuration());
                List<EhCustomerCertificates> list8 = new ArrayList<>();
                for(int i = 0; i < objects.size(); i ++){
                    list8.add((EhCustomerCertificates)objects.get(i));
                }
                dao8.insert(list8);
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
    public FieldGroup findGroupByGroupDisplayName(String groupDisplayName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_VAR_FIELD_GROUPS)
                .where(Tables.EH_VAR_FIELD_GROUPS.TITLE.eq(groupDisplayName))
                .fetchAnyInto(FieldGroup.class);
    }

    @Override
    public FieldGroup findGroupByGroupLogicName(String groupLogicName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_VAR_FIELD_GROUPS)
                .where(Tables.EH_VAR_FIELD_GROUPS.NAME.eq(groupLogicName))
                .fetchAnyInto(FieldGroup.class);
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
                .and(Tables.EH_VAR_FIELDS.GROUP_PATH.like(groupPath+"/%"))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, Field.class);
                });

        return fields;
    }

    @Override
    public List<Long> listFieldRanges(String moduleName,String moduleType,String groupPath){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<Long> ids = new ArrayList<>();
        SelectQuery<EhVarFieldRangesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_RANGES);
        query.addConditions(Tables.EH_VAR_FIELD_RANGES.MODULE_TYPE.eq(moduleType));
        query.addConditions(Tables.EH_VAR_FIELD_RANGES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_RANGES.GROUP_PATH.like(groupPath+"/%"));

        query.fetch().map((record)-> ids.add(record.getFieldId()));
        return ids;
    }

    @Override
    public List<Field> listMandatoryFields(String moduleName, Long groupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

       return context.select().from(Tables.EH_VAR_FIELDS)
                .where(Tables.EH_VAR_FIELDS.MODULE_NAME.eq(moduleName))
                .and(Tables.EH_VAR_FIELDS.GROUP_ID.eq(groupId))
                .and(Tables.EH_VAR_FIELDS.MANDATORY_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()))
                .fetchInto(Field.class);

    }

    @Override
    public List<FieldItem> listFieldItems(List<Long> fieldIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<FieldItem> items = context.select().from(Tables.EH_VAR_FIELD_ITEMS)
                .where(Tables.EH_VAR_FIELD_ITEMS.FIELD_ID.in(fieldIds))
                .and(Tables.EH_VAR_FIELD_ITEMS.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
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
                .and(Tables.EH_VAR_FIELD_ITEMS.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, FieldItem.class);
                });

        return items;
    }

    @Override
    public List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId, Long communityId,Long ownerId, Long categoryId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.eq(fieldId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        
        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.eq(categoryId));
        }

        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.isNull());
        }

        if(ownerId!=null){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        }

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
    public Map<Long, ScopeFieldItem> listScopeFieldsItems(List<Long> fieldIds,Long ownerId, Integer namespaceId, Long communityId, Long categoryId,String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeFieldItem> items = new HashMap<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        if (ownerId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        }
        if(StringUtils.isNotBlank(moduleName)){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName));
        }
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.in(fieldIds));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.isNull());
        }

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
    public Map<Long, ScopeFieldItem> listScopeFieldsItems(List<Long> fieldIds, Integer namespaceId, Long communityId, Long categoryId, String moduleName){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        Map<Long, ScopeFieldItem> items = new HashMap<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.in(fieldIds));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));


        if(StringUtils.isNotBlank(moduleName)){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName));
        }
        if(categoryId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.eq(categoryId));
        }
        if(categoryId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.CATEGORY_ID.isNull());
        }

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
    public ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId,Long ownerId, Long communityId, Long itemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_ID.eq(itemId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));
        if (ownerId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        }

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
    public ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, Long communityId,Long ownerId, String moduleName, String displayName) {
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

        if(ownerId!=null){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
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
    public ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId,Long ownerId, Long communityId, String moduleName, Long fieldId, String displayName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_DISPLAY_NAME.eq(displayName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.eq(fieldId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(ownerId!=null){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        }

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
    public ScopeFieldItem findScopeFieldItemByBusinessValue(Integer namespaceId,Long ownerId,String ownerType, Long communityId, String moduleName, Long fieldId, Byte businessValue) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.eq(fieldId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.BUSINESS_VALUE.eq(businessValue));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        if(ownerId!=null){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        }
        if(org.apache.commons.lang.StringUtils.isNotBlank(ownerType)){
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_TYPE.eq(ownerType));
        }
        if(communityId != null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        if(communityId == null) {
            query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.isNull());
        }

        LOGGER.debug("findScopeFieldItemByBusinessValue, sql=" + query.getSQL());
        LOGGER.debug("findScopeFieldItemByBusinessValue, bindValues=" + query.getBindValues());
        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }
    
    //add by tangcen
	@Override
	public FieldItem findFieldItemByBusinessValue(Long fieldId, Byte businessValue) {
		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		 Record record = context.select()
		 						.from(Tables.EH_VAR_FIELD_ITEMS)
		 						.where(Tables.EH_VAR_FIELD_ITEMS.FIELD_ID.eq(fieldId))
		 						.and(Tables.EH_VAR_FIELD_ITEMS.BUSINESS_VALUE.eq(businessValue))
		 						.fetchOne();
		 return ConvertHelper.convert(record, FieldItem.class);
	}
    

    @Override
    public List<Long> checkCustomerField(Integer namespaceId,Long ownerId, Long communityId, String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        Condition itemCondition = Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName)
                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
//                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        if (communityId != null) {
            itemCondition = itemCondition.and(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        }
        Condition fieldCondition = Tables.EH_VAR_FIELD_SCOPES.MODULE_NAME.eq(moduleName)
                .and(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
                .and(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
//                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        if (communityId != null) {
            fieldCondition = fieldCondition.and(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.eq(communityId));
        }

        Condition groupCondition = Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName)
                .and(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
                .and(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
//                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.OWNER_ID.eq(ownerId));
        if (communityId != null) {
            groupCondition = groupCondition.and(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        }
        return context.select(Tables.EH_VAR_FIELD_ITEM_SCOPES.ID).from(Tables.EH_VAR_FIELD_ITEM_SCOPES).where(itemCondition)
                .unionAll(context.select(Tables.EH_VAR_FIELD_SCOPES.ID).from(Tables.EH_VAR_FIELD_SCOPES).where(fieldCondition))
                .unionAll(context.select(Tables.EH_VAR_FIELD_GROUP_SCOPES.ID).from(Tables.EH_VAR_FIELD_GROUP_SCOPES).where(groupCondition))
                .fetchInto(Long.class);
    }


    @Override
    public Field findFieldById(Long fieldId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhVarFieldsDao dao = new EhVarFieldsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(fieldId), Field.class);
    }
    @Override
    public FieldItem findFieldItemByItemId(Long itemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
       return  context.selectFrom(Tables.EH_VAR_FIELD_ITEMS)
                .where(Tables.EH_VAR_FIELD_ITEMS.ID.eq(itemId))
                .fetchOneInto(FieldItem.class);
    }

    @Override
    public void changeFilterStatus(Integer namespaceId, Long communityId, String moduleName, Long userId, String groupPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        context.update(Tables.EH_VAR_FIELD_SCOPE_FILTERS)
                .set(Tables.EH_VAR_FIELD_SCOPE_FILTERS.STATUS, VarFieldStatus.INACTIVE.getCode())
                .where(Tables.EH_VAR_FIELD_SCOPE_FILTERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.MODULE_NAME.eq(moduleName))
                .and((Tables.EH_VAR_FIELD_SCOPE_FILTERS.USER_ID.eq(userId)))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.GROUP_PATH.eq(groupPath))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.STATUS.eq(VarFieldStatus.ACTIVE.getCode())).execute();
    }


    @Override
    public void createFieldScopeFilter(VarFieldScopeFilter filter){
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVarFieldScopeFilters.class));
        filter.setId(id);
        filter.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        filter.setCreateUid(UserContext.currentUserId());

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhVarFieldScopeFiltersDao dao = new EhVarFieldScopeFiltersDao(context.configuration());
        dao.insert(filter);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhVarFieldScopeFilters.class, filter.getId());
    }

    @Override
    public List<VarFieldScopeFilter> listFieldScopeFilter(Integer namespaceId, Long communityId, String moduleName, Long userId, String groupPath){
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<VarFieldScopeFilter> fieldFilters = context.select().from(Tables.EH_VAR_FIELD_SCOPE_FILTERS)
                .where(Tables.EH_VAR_FIELD_SCOPE_FILTERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.COMMUNITY_ID.eq(communityId))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.MODULE_NAME.eq(moduleName))
                .and((Tables.EH_VAR_FIELD_SCOPE_FILTERS.USER_ID.eq(userId)))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.GROUP_PATH.eq(groupPath))
                .and(Tables.EH_VAR_FIELD_SCOPE_FILTERS.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, VarFieldScopeFilter.class);
                });

        return fieldFilters;
    }


}
