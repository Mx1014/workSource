package com.everhomes.yellowPage.faq;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.DeleteQuery;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceFaqServiceCustomersDao;
import com.everhomes.server.schema.tables.records.EhAllianceFaqServiceCustomersRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.DeleteQueryBuilderCallback;
import com.everhomes.yellowPage.UpdateQueryBuilderCallback;

@Component
public class AllianceFaqServiceCustomerProviderImpl implements AllianceFaqServiceCustomerProvider {

	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhAllianceFaqServiceCustomers TABLE = Tables.EH_ALLIANCE_FAQ_SERVICE_CUSTOMERS;
	
	Class<AllianceFAQServiceCustomer> CLASS = AllianceFAQServiceCustomer.class;
	

	@Override
	public void createServiceCustomer(AllianceFAQServiceCustomer createItem) {
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
		createItem.setId(id);
		createItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		createItem.setCreateUid(UserContext.currentUserId());

		// 使用dao方法
		writeDao().insert(createItem);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void updateServiceCustomer(AllianceFAQServiceCustomer updateItem) {
		int updateCnt = updateSingle(null, q -> {
			q.addValue(TABLE.USER_ID, updateItem.getUserId());
			q.addValue(TABLE.USER_NAME, updateItem.getUserName());
			q.addValue(TABLE.HOTLINE_NUMBER, updateItem.getHotlineNumber());
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(updateItem.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : updateItem.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(updateItem.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(updateItem.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(updateItem.getType()));
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public void deleteServiceCustomer(Long itemId) {

		int deleteCnt = deleteTool(query -> {
			query.addConditions(TABLE.ID.eq(itemId));
		});

		if (deleteCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public AllianceFAQServiceCustomer getServiceCustomer(AllianceCommonCommand cmd) {
		
		List<AllianceFAQServiceCustomer> items =  listTool(null, null, (l, q) -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			return q;
		});
		
		return items.size() > 0 ? items.get(0) : null;
	}


	private EhAllianceFaqServiceCustomersDao getAllianceFAQServiceCustomerDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceFaqServiceCustomersDao(context.configuration());
	}
	
	private int updateTool(List<Long> updateIds, UpdateQueryBuilderCallback callback) {

		UpdateQuery<EhAllianceFaqServiceCustomersRecord> query = updateQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		if (!CollectionUtils.isEmpty(updateIds)) {
			query.addConditions(TABLE.ID.in(updateIds));
		}
		
		return query.execute();
	}
	
	private int deleteTool(DeleteQueryBuilderCallback callback) {

		DeleteQuery<EhAllianceFaqServiceCustomersRecord> query = deleteQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		return query.execute();
	}

	private EhAllianceFaqServiceCustomersDao readDao() {
		return getAllianceFAQServiceCustomerDao(AccessSpec.readOnly());
	}

	private EhAllianceFaqServiceCustomersDao writeDao() {
		return getAllianceFAQServiceCustomerDao(AccessSpec.readWrite());
	}

	public List<AllianceFAQServiceCustomer> listTool(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhAllianceFaqServiceCustomersRecord> query = context.selectQuery(TABLE);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(TABLE.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
		// 必须获取未删除的
		query.addOrderBy(TABLE.ID.asc());
        
        List<AllianceFAQServiceCustomer> list = query.fetchInto(CLASS);
        
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
		List<Long> ids = id == null ? null : Arrays.asList(id);
		
		return updateTool(ids, callback);
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

	private SelectQuery<EhAllianceFaqServiceCustomersRecord> selectQuery() {
		return readOnlyContext().selectQuery(TABLE);
	}

	private UpdateQuery<EhAllianceFaqServiceCustomersRecord> updateQuery() {
		return readWriteContext().updateQuery(TABLE);
	}
	
	private DeleteQuery<EhAllianceFaqServiceCustomersRecord> deleteQuery() {
		return readWriteContext().deleteQuery(TABLE);
	}
	
}
