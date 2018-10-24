package com.everhomes.yellowPage.faq;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhAllianceFaqTypesDao;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

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

		// 使用dao方法
		getAllianceFAQTypeDao(AccessSpec.readWrite()).insert(faqType);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.CREATE, CLASS, null);
		
	}

	@Override
	public void updateFAQType(AllianceFAQType faqType) {
		// 使用dao方法
		getAllianceFAQTypeDao(AccessSpec.readWrite()).update(faqType);

		// 广播给从数据库
		DaoHelper.publishDaoAction(DaoAction.MODIFY, CLASS, null);
	}

	@Override
	public void deleteFAQType(Long faqTypeId) {
		
	}

	@Override
	public AllianceFAQType getFAQType(Long faqTypeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFAQTypeOrders(Long upFAQTypeId, Long lowFAQTypeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<AllianceFAQType> listFAQTypes(AllianceCommonCommand cmd, CrossShardListingLocator locator,
			Integer pageSize, Long pageAnchor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private EhAllianceFaqTypesDao getAllianceFAQTypeDao(AccessSpec arg0) {
		DSLContext context = dbProvider.getDslContext(arg0);
		return new EhAllianceFaqTypesDao(context.configuration());
	}
	
//	private int updateTool(List<Long> updateIds, UpdateQueryBuilderCallback callback) {
//
//		if (CollectionUtils.isEmpty(updateIds)) {
//			throw RuntimeErrorException.errorWith(EnterpriseSettleErrorCode.SCOPE,
//					EnterpriseSettleErrorCode.ERROR_INPUT_UPDATE_ITEMS_EMPTY.getCode(),
//					EnterpriseSettleErrorCode.ERROR_INPUT_UPDATE_ITEMS_EMPTY.getInfo());
//		}
//
//		UpdateQuery<EhEntpriseSettleChkItemsRecord> query = updateQuery();
//
//		if (callback != null) {
//			callback.buildCondition(query);
//		}
//
//		// 必须是未被删除且非固定的才可以更新
//		query.addConditions(TABLE.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));
//		query.addConditions(TABLE.IS_FIXED.eq(TrueOrFalseFlag.FALSE.getCode()));
//		query.addConditions(TABLE.ID.in(updateIds));
//		return query.execute();
//	}
//
//	private EhEntpriseSettleChkItemsDao readDao() {
//		return itemDao(AccessSpec.readOnly());
//	}
//
//	private EhEntpriseSettleChkItemsDao writeDao() {
//		return itemDao(AccessSpec.readWrite());
//	}
//
//	private EhEntpriseSettleChkItemsDao itemDao(AccessSpec accessSpec) {
//		DSLContext context = dbProvider.getDslContext(accessSpec);
//		return new EhEntpriseSettleChkItemsDao(context.configuration());
//	}
//	
	
}
