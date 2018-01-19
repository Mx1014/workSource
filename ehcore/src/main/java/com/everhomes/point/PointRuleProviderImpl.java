// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.point.ListPointRulesCommand;
import com.everhomes.rest.point.PointCommonStatus;
import com.everhomes.rest.point.PointRuleDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPointRuleCategories;
import com.everhomes.server.schema.tables.daos.EhPointRulesDao;
import com.everhomes.server.schema.tables.pojos.EhPointRules;
import com.everhomes.server.schema.tables.records.EhPointRuleConfigsRecord;
import com.everhomes.server.schema.tables.records.EhPointRulesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class PointRuleProviderImpl implements PointRuleProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @CacheEvict(value = "PointRule", allEntries = true)
    @Override
    public void createPointRule(PointRule pointRule) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointRules.class));
        pointRule.setId(id);
        pointRule.setCreateTime(DateUtils.currentTimestamp());
        pointRule.setCreatorUid(UserContext.currentUserId());
        rwDao().insert(pointRule);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointRules.class, id);
    }

    @CacheEvict(value = "PointRule", allEntries = true)
    @Override
    public void updatePointRule(PointRule pointRule) {
        pointRule.setUpdateTime(DateUtils.currentTimestamp());
        pointRule.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointRule);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointRules.class, pointRule.getId());
    }

    @Override
    public List<PointRule> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;

        SelectQuery<EhPointRulesRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }
        query.addConditions(t.STATUS.ne(PointCommonStatus.INACTIVE.getCode()));
        // query.addConditions(t.DISPLAY_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()));

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<PointRule> list = query.fetchInto(PointRule.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Cacheable(value = "PointRule", key = "{#root.methodName, #root.args}")
    @Override
    public PointRule findById(Long id) {
        return ConvertHelper.convert(dao().findById(id), PointRule.class);
    }

    @CacheEvict(value = "PointRule", allEntries = true)
    @Override
    public void createPointRules(List<PointRule> pointRules) {
        Timestamp createTime = DateUtils.currentTimestamp();
        Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPointRules.class), pointRules.size());
        for (PointRule rule : pointRules) {
            rule.setId(id++);
            rule.setCreateTime(createTime);
        }
        rwDao().insert(Arrays.asList(pointRules.toArray(new PointRule[pointRules.size()])));
    }

    @Override
    public List<PointRuleDTO> listPointRules(ListPointRulesCommand cmd, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointRules rule = Tables.EH_POINT_RULES;
        com.everhomes.server.schema.tables.EhPointRuleConfigs config = Tables.EH_POINT_RULE_CONFIGS;
        EhPointRuleCategories category = Tables.EH_POINT_RULE_CATEGORIES;

        SelectQuery<EhPointRuleConfigsRecord> configQuery = context().selectFrom(config).getQuery();

        Table<EhPointRuleConfigsRecord> subT = configQuery.asTable("subT");

        Field<?>[] ruleFields = rule.fields();
        Field<?>[] configFields = subT.fields();

        List<Field<?>> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(ruleFields));
        fieldList.addAll(Arrays.asList(configFields));
        fieldList.add(category.DISPLAY_NAME);

        SelectQuery<Record> query = context().select(fieldList.toArray(new Field[fieldList.size()])).getQuery();
        query.addFrom(rule);

        if (cmd.getSystemId() != null) {
            configQuery.addConditions(config.SYSTEM_ID.eq(cmd.getSystemId()));
        }

        JoinType joinType = JoinType.LEFT_OUTER_JOIN;
        if (Objects.equals(cmd.getStatus(), PointCommonStatus.DISABLED.getCode())) {
            joinType = JoinType.JOIN;
        }

        query.addJoin(subT, joinType, rule.ID.eq(subT.field(config.RULE_ID)));
        query.addJoin(category, JoinType.LEFT_OUTER_JOIN, rule.CATEGORY_ID.eq(category.ID));

        if (cmd.getCategoryId() != null) {
            query.addConditions(rule.CATEGORY_ID.eq(cmd.getCategoryId()));
        }
        if (cmd.getArithmeticType() != null) {
            query.addConditions(rule.ARITHMETIC_TYPE.eq(cmd.getArithmeticType()));
        }

        query.addConditions(rule.STATUS.ne(PointCommonStatus.INACTIVE.getCode()));
        query.addConditions(rule.DISPLAY_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()));

        if (locator.getAnchor() != null) {
            query.addConditions(rule.ID.le(locator.getAnchor()));
        }
        if (pageSize > 0) {
            query.addLimit(pageSize + 1);
        }
        query.addOrderBy(rule.ID.desc());

        if (cmd.getStatus() != null) {
            query.addHaving(subT.field(config.STATUS).eq(cmd.getStatus()).or(subT.field(config.STATUS).isNull()));
        }

        List<PointRuleDTO> list = query.fetch().map(r -> {
            PointRuleDTO pointRule = new PointRuleDTO();
            pointRule.setId(r.getValue(rule.ID));
            pointRule.setNamespaceId(r.getValue(config.NAMESPACE_ID));
            pointRule.setCategoryId(r.getValue(rule.CATEGORY_ID));
            pointRule.setCategoryName(r.getValue(category.DISPLAY_NAME));
            pointRule.setArithmeticType(r.getValue(rule.ARITHMETIC_TYPE));
            pointRule.setDisplayName(r.getValue(rule.DISPLAY_NAME));
            pointRule.setModuleId(r.getValue(rule.MODULE_ID));
            pointRule.setExtra(r.getValue(rule.EXTRA));

            Long systemId = r.getValue(subT.field(config.SYSTEM_ID));
            if (systemId != null) {
                pointRule.setSystemId(systemId);
                pointRule.setDescription(r.getValue(subT.field(config.DESCRIPTION)));
                pointRule.setLimitType(r.getValue(subT.field(config.LIMIT_TYPE)));
                pointRule.setLimitData(r.getValue(subT.field(config.LIMIT_DATA)));
                pointRule.setStatus(r.getValue(subT.field(config.STATUS)));
                pointRule.setPoints(r.getValue(subT.field(config.POINTS)));
            } else {
                pointRule.setDescription(r.getValue(rule.DESCRIPTION));
                pointRule.setLimitType(r.getValue(rule.LIMIT_TYPE));
                pointRule.setLimitData(r.getValue(rule.LIMIT_DATA));
                pointRule.setStatus(r.getValue(rule.STATUS));
                pointRule.setPoints(r.getValue(rule.POINTS));
            }
            return pointRule;
        });
        if (list.size() > pageSize && pageSize > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Cacheable(value = "PointRule", key = "{#root.methodName, #root.args}")
    @Override
    public List<PointRule> listPointRuleByIds(List<Long> ruleIds) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            query.addConditions(t.ID.in(ruleIds));
            query.addConditions(t.STATUS.ne(PointCommonStatus.INACTIVE.getCode()));
            return query;
        });
    }

    @Cacheable(value = "PointRule", key = "{#root.methodName, #root.args}")
    @Override
    public List<PointRule> listPointRuleByCategoryId(Long categoryId) {
        com.everhomes.server.schema.tables.EhPointRules t = Tables.EH_POINT_RULES;
        return this.query(new ListingLocator(), -1, (locator, query) -> {
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
            query.addConditions(t.STATUS.ne(PointCommonStatus.INACTIVE.getCode()));
            return query;
        });
    }

    private EhPointRulesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointRulesDao(context.configuration());
    }

    private EhPointRulesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointRulesDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
