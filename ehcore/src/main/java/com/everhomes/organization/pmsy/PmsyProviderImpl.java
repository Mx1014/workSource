package com.everhomes.organization.pmsy;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.SelectWhereStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.rest.pmsy.PmsyPayerStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPmsyPayers;
import com.everhomes.server.schema.tables.daos.EhPmsyCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhPmsyOrderItemsDao;
import com.everhomes.server.schema.tables.daos.EhPmsyOrdersDao;
import com.everhomes.server.schema.tables.daos.EhPmsyPayersDao;
import com.everhomes.server.schema.tables.pojos.EhPmsyCommunities;
import com.everhomes.server.schema.tables.pojos.EhPmsyOrderItems;
import com.everhomes.server.schema.tables.pojos.EhPmsyOrders;
import com.everhomes.server.schema.tables.records.EhPmsyCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhPmsyOrderItemsRecord;
import com.everhomes.server.schema.tables.records.EhPmsyOrdersRecord;
import com.everhomes.server.schema.tables.records.EhPmsyPayersRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class PmsyProviderImpl implements PmsyProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyProviderImpl.class);
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired 
    private SequenceProvider sequenceProvider;
	
	@Override
	public List<PmsyPayer> listPmPayers(Long id,Integer namespaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyPayers.class));
		SelectWhereStep<EhPmsyPayersRecord> select = context.selectFrom(Tables.EH_PMSY_PAYERS);
		
		Result<EhPmsyPayersRecord> result = select.where(Tables.EH_PMSY_PAYERS.STATUS.eq(PmsyPayerStatus.ACTIVE.getCode()))
			  .and(Tables.EH_PMSY_PAYERS.CREATOR_UID.eq(id))
			  .and(Tables.EH_PMSY_PAYERS.NAMESPACE_ID.eq(namespaceId))
			  .fetch();
		
		return result.map(r -> ConvertHelper.convert(r, PmsyPayer.class));
	}
	
	@Override
	public PmsyPayer findPmPayersById(Long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyPayers.class));
		EhPmsyPayersDao dao = new EhPmsyPayersDao(context.configuration());
		
		return ConvertHelper.convert(dao.fetchOneById(id), PmsyPayer.class);
	}
	
	@Override
	public PmsyPayer findPmPayersByNameAndContact(String userName, String userContact){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyPayers.class));
		SelectWhereStep<EhPmsyPayersRecord> select = context.selectFrom(Tables.EH_PMSY_PAYERS);
		
		EhPmsyPayersRecord result = select.where(Tables.EH_PMSY_PAYERS.USER_NAME.eq(userName))
			  .and(Tables.EH_PMSY_PAYERS.USER_CONTACT.eq(userContact))
			  .fetchOne();
		
		return ConvertHelper.convert(result, PmsyPayer.class);
	}
	
	@Override
	public void createPmPayer(PmsyPayer pmsyPayer){
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmsyPayers.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyPayersDao dao = new EhPmsyPayersDao(context.configuration());
		pmsyPayer.setId(id);
		dao.insert(pmsyPayer);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmsyPayers.class,null);
	}
	
	@Override
	public void updatePmPayer(PmsyPayer pmsyPayer){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyPayersDao dao = new EhPmsyPayersDao(context.configuration());
		
		dao.update(pmsyPayer);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmsyPayers.class,null);
	}
	
	@Override
	public void updatePmsyCommunity(PmsyCommunity pmsyCommunity){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyCommunitiesDao dao = new EhPmsyCommunitiesDao(context.configuration());
		
		dao.update(pmsyCommunity);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmsyCommunities.class,null);
	}
	
	@Override
	public void createPmsyCommunity(PmsyCommunity pmsyCommunity){
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmsyCommunities.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyCommunitiesDao dao = new EhPmsyCommunitiesDao(context.configuration());
		pmsyCommunity.setId(id);
		dao.insert(pmsyCommunity);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmsyCommunities.class,null);
	}
	
	@Override
	public PmsyCommunity findPmsyCommunityById(Long communityId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyCommunities.class));
		SelectConditionStep<EhPmsyCommunitiesRecord> selectConditionStep = context.selectFrom(Tables.EH_PMSY_COMMUNITIES)
			.where(Tables.EH_PMSY_COMMUNITIES.COMMUNITY_ID.eq(communityId));
		
		return ConvertHelper.convert(selectConditionStep.fetchOne(), PmsyCommunity.class);
	}
	
	@Override
	public PmsyCommunity findPmsyCommunityByToken(String communityToken){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyCommunities.class));
		SelectConditionStep<EhPmsyCommunitiesRecord> selectConditionStep = context.selectFrom(Tables.EH_PMSY_COMMUNITIES)
				.where(Tables.EH_PMSY_COMMUNITIES.COMMUNITY_TOKEN.eq(communityToken));
			
		return ConvertHelper.convert(selectConditionStep.fetchOne(), PmsyCommunity.class);
		
	}
	
	@Override
	public void createPmsyOrderItem(PmsyOrderItem pmsyOrderItem){
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmsyOrderItems.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyOrderItemsDao dao = new EhPmsyOrderItemsDao(context.configuration());
		pmsyOrderItem.setId(id);
		dao.insert(pmsyOrderItem);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmsyOrderItems.class,null);
	}
	
	@Override
	public void createPmsyOrderItem(List<PmsyOrderItem> list) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyOrderItemsDao dao = new EhPmsyOrderItemsDao(context.configuration());
		dao.insert(list.stream().map(r -> {
			Long id = sequenceProvider.getNextSequence(NameMapper
					.getSequenceDomainFromTablePojo(EhPmsyOrderItems.class));
			r.setId(id);
			return r;
		}).collect(Collectors.toList()));
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmsyOrderItems.class,null);
		
	}
	
	@Override
	public void updatePmsyOrderItem(PmsyOrderItem pmsyOrderItem) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyOrderItemsDao dao = new EhPmsyOrderItemsDao(context.configuration());
		dao.update(pmsyOrderItem);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmsyOrderItems.class,null);
	}
	@Override
	public List<PmsyOrderItem> ListPmsyOrderItem(Long orderId){
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyOrderItemsDao dao = new EhPmsyOrderItemsDao(context.configuration());
		
		return dao.fetchByOrderId(orderId).stream()
				.map(r -> ConvertHelper.convert(r, PmsyOrderItem.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public void updatePmsyOrder(PmsyOrder pmsyOrder) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyOrdersDao dao = new EhPmsyOrdersDao(context.configuration());
		dao.update(pmsyOrder);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmsyOrders.class,null);
	}
	
	@Override
	public void createPmsyOrder(PmsyOrder pmsyOrder){
		Long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPmsyOrderItems.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPmsyOrdersDao dao = new EhPmsyOrdersDao(context.configuration());
		pmsyOrder.setId(id);
		dao.insert(pmsyOrder);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPmsyOrders.class,null);
	}
	
	@Override
	public List<PmsyOrder> searchBillingOrders(Integer pageSize ,Long communityId ,Long pageAnchor,Timestamp startDate,Timestamp endDate,String userName,String userContact){
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyOrders.class));
		SelectQuery<EhPmsyOrdersRecord> query = context.selectQuery(Tables.EH_PMSY_ORDERS);
		
		if(pageAnchor != null)
			query.addConditions(Tables.EH_PMSY_ORDERS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
		if(startDate != null)
			query.addConditions(Tables.EH_PMSY_ORDERS.CREATE_TIME.gt(startDate));
		if(endDate != null)
			query.addConditions(Tables.EH_PMSY_ORDERS.CREATE_TIME.lt(endDate));
		if(StringUtils.isNotBlank(userName))
			query.addConditions(Tables.EH_PMSY_ORDERS.USER_NAME.eq(userName));
		if(StringUtils.isNotBlank(userContact))
			query.addConditions(Tables.EH_PMSY_ORDERS.USER_CONTACT.eq(userContact));
		query.addConditions(Tables.EH_PMSY_ORDERS.OWNER_ID.eq(communityId));
		query.addConditions(Tables.EH_PMSY_ORDERS.STATUS.eq(PmsyOrderStatus.PAID.getCode()));
		query.addOrderBy(Tables.EH_PMSY_ORDERS.CREATE_TIME.desc());
		query.addLimit(pageSize);
		
		return query.fetch().map(r -> ConvertHelper.convert(r, PmsyOrder.class));
	}
	
	@Override
	public List<PmsyOrderItem> ListBillOrderItems(Long...orderIds){
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyOrderItems.class));
		SelectQuery<EhPmsyOrderItemsRecord> query = context.selectQuery(Tables.EH_PMSY_ORDER_ITEMS);
		
		if(null != orderIds && orderIds.length != 0)
			query.addConditions(Tables.EH_PMSY_ORDER_ITEMS.ORDER_ID.in(orderIds));
		
		return query.fetch().map(r -> ConvertHelper.convert(r, PmsyOrderItem.class));
	}

	@Override
	public PmsyOrder findPmsyOrderById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyOrders.class));
		SelectQuery<EhPmsyOrdersRecord> query = context.selectQuery(Tables.EH_PMSY_ORDERS);
		
		query.addConditions(Tables.EH_PMSY_ORDERS.ID.eq(id));
		EhPmsyOrdersRecord record = query.fetchOne();
		return ConvertHelper.convert(record, PmsyOrder.class);
	}

}
