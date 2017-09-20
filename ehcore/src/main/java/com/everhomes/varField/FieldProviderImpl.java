package com.everhomes.varField;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.varField.VarFieldStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
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
    public ScopeField findScopeField(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhVarFieldScopesDao dao = new EhVarFieldScopesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ScopeField.class);
    }

    @Override
    public ScopeFieldGroup findScopeFieldGroup(Long id, Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldGroup> groups = new ArrayList<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.ID.eq(id));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

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
    public List<ScopeFieldGroup> listScopeFieldGroup(String moduleName, Integer namespaceId, Long communityId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

        List<ScopeFieldGroup> result = new ArrayList<>();
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, ScopeFieldGroup.class));
            return null;
        });

        return result;
    }

    @Override
    public ScopeFieldItem findScopeFieldItem(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhVarFieldItemScopesDao dao = new EhVarFieldItemScopesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ScopeFieldItem.class);
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
    public List<ScopeFieldGroup> listScopeFieldGroups(Integer namespaceId, Long communityId, String moduleName) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<ScopeFieldGroup> groups = new ArrayList<>();
        SelectQuery<EhVarFieldGroupScopesRecord> query = context.selectQuery(Tables.EH_VAR_FIELD_GROUP_SCOPES);
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.COMMUNITY_ID.eq(communityId));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.MODULE_NAME.eq(moduleName));
        query.addConditions(Tables.EH_VAR_FIELD_GROUP_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

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
        query.addConditions(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

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
                .and(Tables.EH_VAR_FIELD_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
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
                .and(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()))
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
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

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
        query.addConditions(Tables.EH_VAR_FIELD_ITEM_SCOPES.STATUS.eq(VarFieldStatus.ACTIVE.getCode()));

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

        query.fetch().map((r) -> {
            item.add(ConvertHelper.convert(r, ScopeFieldItem.class));
            return null;
        });

        if(item.size()==0)
            return null;

        return item.get(0);
    }
}
