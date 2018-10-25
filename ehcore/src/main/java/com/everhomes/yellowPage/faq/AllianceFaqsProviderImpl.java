package com.everhomes.yellowPage.faq;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.DSLContext;
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
import com.everhomes.server.schema.tables.daos.EhAllianceFaqsDao;
import com.everhomes.server.schema.tables.records.EhAllianceFaqsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

@Component
public class AllianceFaqsProviderImpl implements AllianceFaqsProvider {
	
	@Autowired
	DbProvider dbProvider;
	
	@Autowired
	SequenceProvider sequenceProvider;
	
	com.everhomes.server.schema.tables.EhAllianceFaqs TABLE = Tables.EH_ALLIANCE_FAQS;
	
	Class<AllianceFAQ> CLASS = AllianceFAQ.class;
	

	@Override
	public void createFAQ(AllianceFAQ createItem) {
		// 设置动态属性 如id，createTime
		Long id = sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(CLASS));
		createItem.setId(id);
		createItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		createItem.setCreateUid(UserContext.currentUserId());
		createItem.setStatus(YellowPageStatus.ACTIVE.getCode());
		createItem.setDefaultOrder(id);

		// 使用dao方法
		writeDao().insert(createItem);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void updateFAQ(AllianceFAQ updateItem) {
		// 使用dao方法
		writeDao().update(updateItem);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, CLASS, null);
	}

	@Override
	public void deleteFAQ(Long itemId) {

		int updateCnt = updateSingle(itemId, query -> {
			query.addValue(TABLE.STATUS, YellowPageStatus.INACTIVE.getCode());
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public AllianceFAQ getFAQ(Long itemId) {
		
		List<AllianceFAQ> types =  listTool(null, null, (l, q) -> {
			q.addConditions(TABLE.ID.eq(itemId));
			return q;
		});
		
		return types.size() > 0 ? types.get(0) : null;
	}

	@Override
	public void updateFAQOrder(Long itemId, Long defaultOrderId) {
		int updateCnt = updateSingle(itemId, query -> {
			query.addValue(TABLE.DEFAULT_ORDER, defaultOrderId);
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	}

	@Override
	public List<AllianceFAQ> listFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize,
			Long pageAnchor, Long faqType, Byte topFlag, String keyword, Byte orderType, Byte sortType) {
		return listTool(pageSize, locator, (l, q) -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			if (null != faqType) {
				q.addConditions(TABLE.TYPE_ID.eq(faqType));
			}
			
			if (null != topFlag) {
				q.addConditions(TABLE.TOP_FLAG.eq(topFlag));
			}
			
			if (StringUtils.isBlank(keyword)) {
				q.addConditions(TABLE.TITLE.like(DSL.concat("%", keyword, "%")));
			}
			
			if (null != orderType) {
				buildOrderBy(q, orderType, sortType);
			}
			
			return null;
		});
	}
	
	private void buildOrderBy(SelectQuery<? extends Record> q, Byte orderType, Byte sortType) {
		
		if (AllianceFaqOrderType.SOLVE_TIMES.getCode().equals(orderType)) {
			if (null != sortType && sortType > 0) {
				q.addOrderBy(TABLE.SOLVE_TIMES.asc());
			} else {
				q.addOrderBy(TABLE.SOLVE_TIMES.desc());
			}
			
			return;
		}
		
		if (null != sortType && sortType > 0) {
			q.addOrderBy(TABLE.UN_SOLVE_TIMES.asc());
		} else {
			q.addOrderBy(TABLE.UN_SOLVE_TIMES.desc());
		}
	}

	private EhAllianceFaqsDao getAllianceFAQDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceFaqsDao(context.configuration());
	}
	
	private int updateTool(List<Long> updateIds, UpdateQueryBuilderCallback callback) {

		if (CollectionUtils.isEmpty(updateIds)) {
			return 0;
		}

		UpdateQuery<EhAllianceFaqsRecord> query = updateQuery();

		if (callback != null) {
			callback.buildCondition(query);
		}

		// 必须是未被删除且非固定的才可以更新
		query.addConditions(TABLE.STATUS.eq(YellowPageStatus.ACTIVE.getCode()));
		query.addConditions(TABLE.ID.in(updateIds));
		return query.execute();
	}

	private EhAllianceFaqsDao readDao() {
		return getAllianceFAQDao(AccessSpec.readOnly());
	}

	private EhAllianceFaqsDao writeDao() {
		return getAllianceFAQDao(AccessSpec.readWrite());
	}

	public List<AllianceFAQ> listTool(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhAllianceFaqsRecord> query = context.selectQuery(TABLE);
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
        
        List<AllianceFAQ> list = query.fetchInto(CLASS);
        
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

	private SelectQuery<EhAllianceFaqsRecord> selectQuery() {
		return readOnlyContext().selectQuery(TABLE);
	}

	private UpdateQuery<EhAllianceFaqsRecord> updateQuery() {
		return readWriteContext().updateQuery(TABLE);
	}

	@Override
	public void updateTopFAQFlag(Long itemId, byte topFlag) {

		int updateCnt = updateSingle(itemId, query -> {
			query.addValue(TABLE.TOP_FLAG, topFlag);
		});

		if (updateCnt > 0) {
			DaoHelper.publishDaoAction(DaoAction.MODIFY,  CLASS, null);
		}
	
	}

	@Override
	public List<AllianceFAQ> listTopFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize,
			Long pageAnchor) {
		return listTool(pageSize, locator, (l, q) -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			q.addConditions(TABLE.TOP_FLAG.eq(TrueOrFalseFlag.TRUE.getCode()));
			q.addOrderBy(TABLE.TOP_ORDER);
			return null;
		});
	}
	
}
