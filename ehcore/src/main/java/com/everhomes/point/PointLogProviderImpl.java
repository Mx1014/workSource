// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.point.ListPointLogsCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPointLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPointLogs;
import com.everhomes.server.schema.tables.records.EhPointLogsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class PointLogProviderImpl implements PointLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointLog(PointLog pointLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointLogs.class));
		pointLog.setId(id);
		pointLog.setCreateTime(DateUtils.currentTimestamp());
		// pointLog.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointLogs.class, id);
	}

	@Override
	public void updatePointLog(PointLog pointLog) {
		// pointLog.setUpdateTime(DateUtils.currentTimestamp());
		// pointLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointLogs.class, pointLog.getId());
	}

    @Override
    public List<PointLog> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointLogs t = Tables.EH_POINT_LOGS;

        SelectQuery<EhPointLogsRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.lt(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<PointLog> list = query.fetchInto(PointLog.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public List<PointLog> listPointLogs(ListPointLogsCommand cmd, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointLogs t = Tables.EH_POINT_LOGS;
        return this.query(locator, cmd.getPageSize(), (locator1, query) -> {
            if (cmd.getSystemId() != null) {
                query.addConditions(t.SYSTEM_ID.eq(cmd.getSystemId()));
            }
            if (cmd.getUserId() != null) {
                query.addConditions(t.TARGET_UID.eq(cmd.getUserId()));
            }
            if (cmd.getEventName() != null) {
                query.addConditions(t.EVENT_NAME.eq(cmd.getEventName()));
            }
            if (cmd.getArithmeticType() != null) {
                query.addConditions(t.ARITHMETIC_TYPE.eq(cmd.getArithmeticType()));
            }
            if (cmd.getPhone() != null) {
                query.addConditions(t.TARGET_PHONE.eq(cmd.getPhone()));
            }
            if (cmd.getStartTime() != null) {
                query.addConditions(t.CREATE_TIME.gt(new Timestamp(cmd.getStartTime())));
            }
            if (cmd.getEndTime() != null) {
                query.addConditions(t.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
            }
            return query;
        });
    }

	@Override
	public PointLog findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointLog.class);
	}

    @Override
    public PointLog findByUidAndEntity(Integer namespaceId, Long uid, String eventName, String entityType, Long entityId) {
        com.everhomes.server.schema.tables.EhPointLogs t = Tables.EH_POINT_LOGS;
        List<PointLog> logList = this.query(new ListingLocator(), 1, (locator1, query) -> {
            if (namespaceId != null) {
                query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            }
            if (uid != null) {
                query.addConditions(t.TARGET_UID.eq(uid));
            }
            if (eventName != null) {
                query.addConditions(t.EVENT_NAME.startsWith(eventName));
            }
            if (entityType != null && entityId != null) {
                query.addConditions(t.ENTITY_TYPE.eq(entityType));
                query.addConditions(t.ENTITY_ID.eq(entityId));
            }
            return query;
        });
        if (logList.size() > 0) {
            return logList.get(0);
        }
        return null;
    }

    @Override
    public Integer countPointLog(Integer namespaceId, Long systemId, Long uid, String eventName, Long createTime) {
        com.everhomes.server.schema.tables.EhPointLogs t = Tables.EH_POINT_LOGS;
        SelectQuery<EhPointLogsRecord> query = context().selectFrom(t).getQuery();
        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(t.SYSTEM_ID.eq(systemId));
        query.addConditions(t.TARGET_UID.eq(uid));
        query.addConditions(t.EVENT_NAME.startsWith(eventName));
        if (createTime != null) {
            query.addConditions(t.CREATE_TIME.gt(new Timestamp(createTime)));
        }
        return query.fetchCount();
    }

    @Override
    public PointLog findByRuleIdAndEntity(Integer namespaceId, Long uid, Long ruleId, String entityType, Long entityId) {
        com.everhomes.server.schema.tables.EhPointLogs t = Tables.EH_POINT_LOGS;
        List<PointLog> logList = this.query(new ListingLocator(), 1, (locator1, query) -> {
            if (namespaceId != null) {
                query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            }
            if (uid != null) {
                query.addConditions(t.TARGET_UID.eq(uid));
            }
            if (ruleId != null) {
                query.addConditions(t.RULE_ID.eq(ruleId));
            }
            if (entityType != null && entityId != null) {
                query.addConditions(t.ENTITY_TYPE.eq(entityType));
                query.addConditions(t.ENTITY_ID.eq(entityId));
            }
            return query;
        });
        if (logList.size() > 0) {
            return logList.get(0);
        }
        return null;
    }

    private EhPointLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointLogsDao(context.configuration());
	}

	private EhPointLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
