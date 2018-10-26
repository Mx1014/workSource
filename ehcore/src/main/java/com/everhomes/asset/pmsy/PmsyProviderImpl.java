package com.everhomes.asset.pmsy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.pmsy.PmsyCommunity;
import com.everhomes.organization.pmsy.PmsyOrder;
import com.everhomes.organization.pmsy.PmsyOrderItem;
import com.everhomes.organization.pmsy.PmsyPayer;
import com.everhomes.organization.pmsy.PmsyProvider;
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
		
		/*Result<EhPmsyPayersRecord> result = select.where(Tables.EH_PMSY_PAYERS.STATUS.eq(PmsyPayerStatus.ACTIVE.getCode()))
			  .and(Tables.EH_PMSY_PAYERS.CREATOR_UID.eq(id))
			  .and(Tables.EH_PMSY_PAYERS.NAMESPACE_ID.eq(namespaceId)).orderBy(Tables.EH_PMSY_PAYERS.CREATE_TIME.desc())
			  .fetch();*/
		//由于历史原因，海岸馨服务的namespaceId已经不是0，所以匹配namespaceId不可能匹配上，在此不做namespaceId校验（杨崇鑫）
		Result<EhPmsyPayersRecord> result = select.where(Tables.EH_PMSY_PAYERS.STATUS.eq(PmsyPayerStatus.ACTIVE.getCode()))
				  .and(Tables.EH_PMSY_PAYERS.CREATOR_UID.eq(id))
				  .orderBy(Tables.EH_PMSY_PAYERS.CREATE_TIME.desc())
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
				.getSequenceDomainFromTablePojo(EhPmsyOrders.class));
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
		query.addConditions(Tables.EH_PMSY_ORDERS.STATUS.gt(PmsyOrderStatus.PAID.getCode()));
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

	@Override
	public List<PmsyOrder> listPmsyOrders(Integer pageSize,
			Timestamp startDate, Timestamp endDate,List<Byte> statuses,
			CrossShardListingLocator locator) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyOrders.class));
		List<PmsyOrder> results = new ArrayList<PmsyOrder>();
		SelectQuery<EhPmsyOrdersRecord> query = context.selectQuery(Tables.EH_PMSY_ORDERS);
		if(null != locator.getAnchor())
			query.addConditions(Tables.EH_PMSY_ORDERS.ID.gt(locator.getAnchor()));
		if(null != startDate)
			query.addConditions(Tables.EH_PMSY_ORDERS.CREATE_TIME.ge(startDate));
		if(null != endDate)
			query.addConditions(Tables.EH_PMSY_ORDERS.CREATE_TIME.lt(endDate));
		if(null != statuses && statuses.size() > 0){
			query.addConditions(Tables.EH_PMSY_ORDERS.STATUS.in(statuses));
		}
		query.addOrderBy(Tables.EH_PMSY_ORDERS.ID.asc());
		query.addLimit(pageSize + 1);
		
		query.fetch().map(r -> {
			results.add(ConvertHelper.convert(r, PmsyOrder.class));
			return null;
		});
		
		locator.setAnchor(null);
		if(results.size() > pageSize){
			results.remove(results.size() - 1);
			locator.setAnchor(results.get(results.size() -1).getId());
		}
		return results;
	}
	
	public void updatePmsyOrderItemByOrderId(Long orderId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.update(Tables.EH_PMSY_ORDER_ITEMS)
	        .set(Tables.EH_PMSY_ORDER_ITEMS.STATUS,(byte)1) //1 ：表示已支付
	        .where(Tables.EH_PMSY_ORDER_ITEMS.ORDER_ID.eq(orderId))
	        .execute();
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPmsyOrderItems.class,null);
	}
	
	public List<PmsyOrder> findPmsyOrderItemsByOrderId(String billId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyOrders.class));
		List<PmsyOrder> results = new ArrayList<PmsyOrder>();
		SelectQuery<EhPmsyOrderItemsRecord> query = context.selectQuery(Tables.EH_PMSY_ORDER_ITEMS);
		query.addConditions(Tables.EH_PMSY_ORDER_ITEMS.BILL_ID.eq(billId));
		query.addConditions(Tables.EH_PMSY_ORDER_ITEMS.STATUS.eq((byte)1));
		query.fetch().map(r -> {
			results.add(ConvertHelper.convert(r, PmsyOrder.class));
			return null;
		});
		return results;
	}	
}
