package com.everhomes.varField;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public List<ScopeFieldItem> listScopeFieldItems(List<Long> fieldIds) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldItem> items = context.select().from(Tables.EH_VAR_FIELD_ITEM_SCOPES)
                .where(Tables.EH_VAR_FIELD_ITEM_SCOPES.FIELD_ID.in(fieldIds))
                .fetch().map((record)-> {
                    return ConvertHelper.convert(record, ScopeFieldItem.class);
                });

        return items;
    }

    @Override
    public ScopeFieldItem findScopeFieldItemByFieldItemId(Integer namespaceId, Long itemId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        ScopeFieldItem item = context.select().from(Tables.EH_VAR_FIELD_ITEM_SCOPES)
                .where(Tables.EH_VAR_FIELD_ITEM_SCOPES.ITEM_ID.eq(itemId))
                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.NAMESPACE_ID.eq(namespaceId))
                .fetchAny().map((record)-> {
            return ConvertHelper.convert(record, ScopeFieldItem.class);
        });
        return item;
    }
}
