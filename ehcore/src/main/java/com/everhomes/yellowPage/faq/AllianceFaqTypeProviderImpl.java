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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceFaqTypesDao;
import com.everhomes.server.schema.tables.records.EhAllianceFaqTypesRecord;
import com.everhomes.server.schema.tables.records.EhAllianceFaqsRecord;
import com.everhomes.server.schema.tables.records.EhAllianceTagRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.AllianceTag;
import com.everhomes.yellowPage.DeleteQueryBuilderCallback;
import com.everhomes.yellowPage.UpdateQueryBuilderCallback;

@Component
public class AllianceFaqTypeProviderImpl implements AllianceFaqTypeProvider{
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhAllianceFaqTypes TABLE = Tables.EH_ALLIANCE_FAQ_TYPES;
	
	Class<AllianceFAQType> CLASS = AllianceFAQType.class;
	

	@Override
	public void createFAQType(AllianceFAQType faqType) {
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
		faqType.setId(id);
		faqType.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		faqType.setCreateUid(UserContext.currentUserId());
		faqType.setDefaultOrder(id);
		faqType.setStatus(YellowPageStatus.ACTIVE.getCode());

		// 使用dao方法
		writeDao().insert(faqType);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void updateFAQType(AllianceFAQType faqType) {
		// 使用dao方法
		writeDao().update(faqType);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, CLASS, null);
	}

	@Override
	public void deleteFAQType(Long faqTypeId) {

		// 当前 item只能更新name,showFlag,order
		int updateCnt = updateSingle(faqTypeId, query -> {
			query.addValue(TABLE.STATUS, YellowPageStatus.INACTIVE.getCode());
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public AllianceFAQType getFAQType(Long faqTypeId) {
		
		List<AllianceFAQType> types =  listTool(null, null, (l, q) -> {
			q.addConditions(TABLE.ID.eq(faqTypeId));
			return q;
		});
		
		return types.size() > 0 ? types.get(0) : null;
	}

	@Override
	public void updateFAQTypeOrder(Long faqTypeId, Long defaultOrderId) {
		int updateCnt = updateSingle(faqTypeId, query -> {
			query.addValue(TABLE.DEFAULT_ORDER, defaultOrderId);
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public List<AllianceFAQType> listFAQTypes(AllianceCommonCommand cmd, ListingLocator locator,
			Integer pageSize, Long pageAnchor) {
		return listTool(pageSize, locator, (l,q)-> {
			q.addConditions(TABLE.NAMESPACE_ID.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			return null;
		});
	}
	
	private EhAllianceFaqTypesDao getAllianceFAQTypeDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceFaqTypesDao(context.configuration());
	}
	
	private int updateTool(List<Long> updateIds, UpdateQueryBuilderCallback callback) {

		if (CollectionUtils.isEmpty(updateIds)) {
			return 0;
		}

		UpdateQuery<EhAllianceFaqTypesRecord> query = updateQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		// 必须是未被删除且非固定的才可以更新
		query.addConditions(TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
		query.addConditions(TABLE.ID.in(updateIds));
		return query.execute();
	}

	private EhAllianceFaqTypesDao readDao() {
		return getAllianceFAQTypeDao(AccessSpec.readOnly());
	}

	private EhAllianceFaqTypesDao writeDao() {
		return getAllianceFAQTypeDao(AccessSpec.readWrite());
	}

	public List<AllianceFAQType> listTool(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhAllianceFaqTypesRecord> query = context.selectQuery(TABLE);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(TABLE.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
		// 必须获取未删除的
		query.addConditions(TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
		query.addOrderBy(TABLE.DEFAULT_ORDER.asc());
		query.addOrderBy(TABLE.ID.asc());
        
        List<AllianceFAQType> list = query.fetchInto(CLASS);
        
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

	private SelectQuery<EhAllianceFaqTypesRecord> selectQuery() {
		return readOnlyContext().selectQuery(TABLE);
	}

	private UpdateQuery<EhAllianceFaqTypesRecord> updateQuery() {
		return readWriteContext().updateQuery(TABLE);
	}

	@Override
	public void deleteFAQTypes(AllianceCommonCommand cmd) {

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
	
	
	private int deleteTool(DeleteQueryBuilderCallback callback) {

		DeleteQuery<EhAllianceFaqTypesRecord> query = deleteQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		return query.execute();
	}
	
	private DeleteQuery<EhAllianceFaqTypesRecord> deleteQuery() {
		return readWriteContext().deleteQuery(TABLE);
	}
	
}
