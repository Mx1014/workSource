package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceOperateServicesDao;
import com.everhomes.server.schema.tables.records.EhAllianceOperateServicesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class AllianceOperateServiceProviderImpl implements AllianceOperateServiceProvider {

	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhAllianceOperateServices TABLE = Tables.EH_ALLIANCE_OPERATE_SERVICES;
	
	Class<AllianceOperateService> CLASS = AllianceOperateService.class;
	

	@Override
	public void createOperateService(AllianceOperateService createItem) {
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
		createItem.setId(id);
		createItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		createItem.setCreateUid(UserContext.currentUserId());
		createItem.setDefaultOrder(id);

		// 使用dao方法
		writeDao().insert(createItem);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void updateOperateService(AllianceOperateService updateItem) {
		// 使用dao方法
		writeDao().update(updateItem);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, CLASS, null);
	}

	@Override
	public void deleteOperateService(Long itemId) {

		int deleteCnt = deleteTool(query -> {
			query.addConditions(TABLE.ID.eq(itemId));
		});

		if (deleteCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}
	
	@Override
	public void deleteOperateServices(AllianceCommonCommand cmd) {
		int deleteCnt = deleteTool(q -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
		});

		if (deleteCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public AllianceOperateService getOperateService(Long itemId) {
		
		List<AllianceOperateService> types =  listTool(null, null, (l, q) -> {
			q.addConditions(TABLE.ID.eq(itemId));
			return q;
		});
		
		return types.size() > 0 ? types.get(0) : null;
	}

	@Override
	public void updateOperateServiceOrder(Long itemId, Long defaultOrderId) {
		int updateCnt = updateSingle(itemId, query -> {
			query.addValue(TABLE.DEFAULT_ORDER, defaultOrderId);
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public List<AllianceOperateService> listOperateServices(AllianceCommonCommand cmd) {
		return listTool(null, null, (l, q) -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			return null;
		});
	}
	

	private EhAllianceOperateServicesDao getAllianceOperateServiceDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceOperateServicesDao(context.configuration());
	}
	
	private int updateTool(List<Long> updateIds, UpdateQueryBuilderCallback callback) {

		if (CollectionUtils.isEmpty(updateIds)) {
			return 0;
		}

		UpdateQuery<EhAllianceOperateServicesRecord> query = updateQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		query.addConditions(TABLE.ID.in(updateIds));
		return query.execute();
	}
	
	private int deleteTool(DeleteQueryBuilderCallback callback) {

		DeleteQuery<EhAllianceOperateServicesRecord> query = deleteQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		return query.execute();
	}

	private EhAllianceOperateServicesDao readDao() {
		return getAllianceOperateServiceDao(AccessSpec.readOnly());
	}

	private EhAllianceOperateServicesDao writeDao() {
		return getAllianceOperateServiceDao(AccessSpec.readWrite());
	}

	public List<AllianceOperateService> listTool(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhAllianceOperateServicesRecord> query = context.selectQuery(TABLE);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(TABLE.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
		// 必须获取未删除的
		query.addOrderBy(TABLE.DEFAULT_ORDER.asc());
		query.addOrderBy(TABLE.ID.asc());
        
        List<AllianceOperateService> list = query.fetchInto(CLASS);
        
        // 设置锚点
        if (null != locator && null != list) {
    		if (realPageSize > 0 && list.size() > realPageSize) {
    			locator.setAnchor(list.get(list.size() - 1).getId());
    			list.remove(list.size() - 1);
    		} else {
    			locator.setAnchor(null);
    		}
        }

        return list;
    }

	private int updateSingle(Long id, UpdateQueryBuilderCallback callback) {
		return updateTool(Arrays.asList(id), callback);
	}

	private int updateMulti(List<Long> updateIds, UpdateQueryBuilderCallback callback) {
		return updateTool(updateIds, callback);
	}

	private DSLContext readOnlyContext() {
		return dbProvider.getDslContext(AccessSpec.readOnly());
	}

	private DSLContext readWriteContext() {
		return dbProvider.getDslContext(AccessSpec.readWrite());
	}

	private SelectQuery<EhAllianceOperateServicesRecord> selectQuery() {
		return readOnlyContext().selectQuery(TABLE);
	}

	private UpdateQuery<EhAllianceOperateServicesRecord> updateQuery() {
		return readWriteContext().updateQuery(TABLE);
	}
	
	private DeleteQuery<EhAllianceOperateServicesRecord> deleteQuery() {
		return readWriteContext().deleteQuery(TABLE);
	}
	
}
