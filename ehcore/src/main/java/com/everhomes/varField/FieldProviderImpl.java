package com.everhomes.varField;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhVarFieldItemScopes;
import com.everhomes.server.schema.tables.records.EhVarFieldGroupScopesRecord;
import com.everhomes.server.schema.tables.records.EhVarFieldItemScopesRecord;
import com.everhomes.server.schema.tables.records.EhVarFieldScopesRecord;
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
    public List<ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long communityId, String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldGroup> groups = new ArrayList<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName));

        query.fetch().map((r) -> {
            groups.add(ConvertHelper.convert(r, ScopeFieldGroup.class));
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
    public List<ScopeField> listScopeFields(Integer namespaceId, Long communityId, String moduleName, String groupPath) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeField> fields = new ArrayList<>();
        SelectQuery<EhVarFieldScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.GROUP_PATH.like(groupPath + "%"));

        query.fetch().map((record)-> {
            fields.add(ConvertHelper.convert(record, ScopeField.class));
            return null;
        });

        return fields;
    }

    @Override
    public ScopeField findScopeField(Integer namespaceId, Long communityId, Long fieldId) {
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
    public List<ScopeFieldItem> listScopeFieldItems(Long fieldId, Integer namespaceId, Long communityId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = new ArrayList<>();
        SelectQuery<EhVarFieldItemScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_ITEM_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.eq(fieldId));

        query.fetch().map((r) -> {
            items.add(ConvertHelper.convert(r, ScopeFieldItem.class));
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

        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }
}
