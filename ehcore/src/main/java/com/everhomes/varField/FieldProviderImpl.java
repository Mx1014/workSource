package com.everhomes.varField;

import com.everhomes.customer.CustomerTalent;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
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
import com.everhomes.server.schema.tables.records.EhVarFieldItemScopesRecord;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/3.
 */
@Component
public class FieldProviderImpl implements FieldProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public List<ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldGroup> groups = context.select().from(Tables.EH_VAR_FIELD_GROUP_SCOPES)
                .where(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, ScopeFieldGroup.class);
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
    public List<ScopeField> listScopeFields(Integer namespaceId, String moduleName, String groupPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeField> fields = context.select().from(Tables.EH_VAR_FIELD_SCOPES)
                .where(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_VAR_FIELD_SCOPES.MODULE_NAME.eq(moduleName))
                .and(Tables.EH_VAR_FIELD_SCOPES.GROUP_PATH.like(groupPath + "%"))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, ScopeField.class);
                });

        return fields;
    }

    @Override
    public ScopeField findScopeField(Integer namespaceId, Long fieldId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeField> fields = context.select().from(Tables.EH_VAR_FIELD_SCOPES)
                .where(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_VAR_FIELD_SCOPES.FIELD_ID.eq(fieldId))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, ScopeField.class);
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
    public List<ScopeFieldItem> listScopeFieldItems(List<Long> fieldIds, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = context.select().from(Tables.EH_VAR_FIELD_ITEM_SCOPES)
                .where(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.in(fieldIds))
                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, ScopeFieldItem.class);
                });

        return items;
    }

    @Override
    public List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = context.select().from(Tables.EH_VAR_FIELD_ITEM_SCOPES)
                .where(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.eq(fieldId))
                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, ScopeFieldItem.class);
                });

        return items;
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long itemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_ID.eq(itemId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));

        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByDisplayName(Integer namespaceId, String moduleName, String displayName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        List<ScopeFieldItem> item = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_DISPLAY_NAME.eq(displayName));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));

        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }
}
