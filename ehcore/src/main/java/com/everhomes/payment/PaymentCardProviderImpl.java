package com.everhomes.payment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.payment.CardOrderStatus;
import com.everhomes.rest.payment.CardRechargeStatus;
import com.everhomes.rest.payment.PaymentCardStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPaymentCardRechargeOrdersDao;
import com.everhomes.server.schema.tables.daos.EhPaymentCardTransactionsDao;
import com.everhomes.server.schema.tables.daos.EhPaymentCardsDao;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuerCommunities;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuers;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardRechargeOrders;
import com.everhomes.server.schema.tables.pojos.EhPaymentCardTransactions;
import com.everhomes.server.schema.tables.pojos.EhPaymentCards;
import com.everhomes.server.schema.tables.records.EhPaymentCardIssuerCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhPaymentCardIssuersRecord;
import com.everhomes.server.schema.tables.records.EhPaymentCardRechargeOrdersRecord;
import com.everhomes.server.schema.tables.records.EhPaymentCardTransactionsRecord;
import com.everhomes.server.schema.tables.records.EhPaymentCardsRecord;
import com.everhomes.util.ConvertHelper;


@Component
public class PaymentCardProviderImpl implements PaymentCardProvider{
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Override
    public List<PaymentCardIssuerCommunity> listPaymentCardIssuerCommunity(Long ownerId,String ownerType){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardIssuerCommunities.class));
    	SelectQuery<EhPaymentCardIssuerCommunitiesRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES);
    	if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.OWNER_ID.eq(ownerId));
        
        List<PaymentCardIssuerCommunity> result = new ArrayList<PaymentCardIssuerCommunity>();
        
        result = query.fetch().map(
        		r -> ConvertHelper.convert(r, PaymentCardIssuerCommunity.class));
        return result;
    }
    
    @Override
    public List<PaymentCardIssuer> listPaymentCardIssuer(Long ownerId,String ownerType){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardIssuers.class));
    	SelectJoinStep<Record> query = context.select(Tables.EH_PAYMENT_CARD_ISSUERS.fields()).from(Tables.EH_PAYMENT_CARD_ISSUERS);
    	query.join(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES).on(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.ISSUER_ID
    			.eq(Tables.EH_PAYMENT_CARD_ISSUERS.ID));
        query.where(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.OWNER_TYPE.eq(ownerType))
        	.and(Tables.EH_PAYMENT_CARD_ISSUER_COMMUNITIES.OWNER_ID.eq(ownerId));
        
        List<PaymentCardIssuer> result = new ArrayList<PaymentCardIssuer>();
        
        query.fetch().forEach(
        		r -> {
        			PaymentCardIssuer issuer = new PaymentCardIssuer();
        			issuer.setId(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.ID));
        			issuer.setName(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.NAME));
        			issuer.setDescription(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.DESCRIPTION));
        			issuer.setPayUrl(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.PAY_URL));
        			issuer.setAlipayRechargeAccount(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.ALIPAY_RECHARGE_ACCOUNT));
        			issuer.setWeixinRechargeAccount(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.WEIXIN_RECHARGE_ACCOUNT));
        			issuer.setVendorName(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.VENDOR_NAME));
        			issuer.setVendorData(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.VENDOR_DATA));
        			issuer.setCreateTime(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.CREATE_TIME));
        			issuer.setStatus(r.getValue(Tables.EH_PAYMENT_CARD_ISSUERS.STATUS));
        			result.add(issuer);
        		});
        return result;
    }
    @Override
    public PaymentCardIssuer findPaymentCardIssuerById(Long issuerId){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardIssuers.class));
    	SelectQuery<EhPaymentCardIssuersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_ISSUERS);

        if(issuerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_ISSUERS.ID.eq(issuerId));
        
        return ConvertHelper.convert(query.fetchOne(), PaymentCardIssuer.class);
    }
    
    @Override
    public void createPaymentCard(PaymentCard paymentCard){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPaymentCards.class));
    	paymentCard.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentCardsDao dao = new EhPaymentCardsDao(context.configuration());
		dao.insert(paymentCard);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentCards.class, null);
    	
    }
    
    @Override
    public void updatePaymentCard(PaymentCard paymentCard){
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentCardsDao dao = new EhPaymentCardsDao(context.configuration());
		dao.update(paymentCard);
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPaymentCards.class, null);
    	
    }
    
    @Override
    public void createPaymentCardRechargeOrder(PaymentCardRechargeOrder paymentCardRechargeOrder){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPaymentCardRechargeOrders.class));
    	paymentCardRechargeOrder.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentCardRechargeOrdersDao dao = new EhPaymentCardRechargeOrdersDao(context.configuration());
		dao.insert(paymentCardRechargeOrder);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentCardRechargeOrders.class, null);
    	
    }
    
    @Override
    public PaymentCard findPaymentCardById(Long cardId){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCards.class));
    	SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
        if(cardId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.ID.eq(cardId));
        return ConvertHelper.convert(query.fetchOne(), PaymentCard.class);
    }
    
    @Override
    public PaymentCard findPaymentCardByCardNo(String cardNo,String vendorName){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCards.class));
    	SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
        if(cardNo != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.CARD_NO.eq(cardNo));
        query.addConditions(Tables.EH_PAYMENT_CARDS.VENDOR_NAME.eq(vendorName));
        return ConvertHelper.convert(query.fetchOne(), PaymentCard.class);
    }
    
    @Override
    public List<PaymentCard> listPaymentCard(Long ownerId,String ownerType,Long userId){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCards.class));
    	SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
    	if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_ID.eq(ownerId));
        if(userId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_PAYMENT_CARDS.STATUS.eq(PaymentCardStatus.ACTIVE.getCode()));
        List<PaymentCard> result = new ArrayList<PaymentCard>();
        
        result = query.fetch().map(
        		r -> ConvertHelper.convert(r, PaymentCard.class));
        return result;
    }
    
    @Override
    public Integer countPaymentCard(Long ownerId,String ownerType,Long userId){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCards.class));
    	SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
    	if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_ID.eq(ownerId));
        if(userId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.USER_ID.eq(userId));
        query.addConditions(Tables.EH_PAYMENT_CARDS.STATUS.eq(PaymentCardStatus.ACTIVE.getCode()));
        return query.fetchCount();
    }
    
    @Override
    public List<PaymentCard> searchCardUsers(Long ownerId,String ownerType,String keyword,Long pageAnchor,Integer pageSize){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCards.class));
    	SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);

    	if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PAYMENT_CARDS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
    	if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_ID.eq(ownerId));
     
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_PAYMENT_CARDS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARDS.MOBILE.eq(keyword)));

        query.addOrderBy(Tables.EH_PAYMENT_CARDS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        List<PaymentCard> result = new ArrayList<PaymentCard>();
        result = query.fetch().map(
        		r -> ConvertHelper.convert(r, PaymentCard.class));
        return result;
    }
    
    @Override
    public Integer countCardUsers(Long ownerId,String ownerType,Integer namespaceId){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCards.class));
    	SelectQuery<EhPaymentCardsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARDS);
        query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_TYPE.eq(ownerType));
        query.addConditions(Tables.EH_PAYMENT_CARDS.OWNER_ID.eq(ownerId));
        query.addConditions(Tables.EH_PAYMENT_CARDS.NAMESPACE_ID.eq(namespaceId));

        return query.fetchCount();
    }
    
    @Override
    public List<PaymentCardRechargeOrder> searchCardRechargeOrder(String ownerType,Long ownerId,Timestamp startDate,Timestamp endDate,
    		String rechargeType,Byte rechargeStatus,String keyword,Long pageAnchor,Integer pageSize){
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardRechargeOrders.class));
		SelectQuery<EhPaymentCardRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS);
    	
		if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.OWNER_ID.eq(ownerId));
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.MOBILE.eq(keyword)));
        if(StringUtils.isNotBlank(rechargeType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.PAID_TYPE.eq(rechargeType));
        
        if(startDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_TIME.gt(startDate));
        if(endDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_TIME.lt(endDate));
        if(rechargeStatus != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.RECHARGE_STATUS.eq(rechargeStatus));
        
    	query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.PAY_STATUS.eq(CardOrderStatus.PAID.getCode()));
        query.addOrderBy(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        return  query.fetch().map(r -> 
			ConvertHelper.convert(r, PaymentCardRechargeOrder.class));
		
    }
    
    @Override
    public PaymentCardRechargeOrder findPaymentCardRechargeOrderById(Long orderId){
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardRechargeOrders.class));
		SelectQuery<EhPaymentCardRechargeOrdersRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS);
    	
		if (orderId != null)
			query.addConditions(Tables.EH_PAYMENT_CARD_RECHARGE_ORDERS.ID.eq(orderId));
        
        return ConvertHelper.convert(query.fetchOne(), PaymentCardRechargeOrder.class);
		
    }
    
    @Override
    public void updatePaymentCardRechargeOrder(PaymentCardRechargeOrder order){
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardRechargeOrders.class));
		EhPaymentCardRechargeOrdersDao dao = new EhPaymentCardRechargeOrdersDao(context.configuration());
    	
		dao.update(order);
        
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPaymentCardRechargeOrders.class, null);
		
    }
    
    @Override
    public List<PaymentCardTransaction> searchCardTransactions(String ownerType,Long ownerId,Timestamp startDate,Timestamp endDate,
    		String consumeType,Byte status,String keyword,Long pageAnchor,Integer pageSize){
    	
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPaymentCardRechargeOrders.class));
		SelectQuery<EhPaymentCardTransactionsRecord> query = context.selectQuery(Tables.EH_PAYMENT_CARD_TRANSACTIONS);
    	
		if (pageAnchor != null && pageAnchor != 0)
			query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.CREATE_TIME.lt(new Timestamp(pageAnchor)));
        if(StringUtils.isNotBlank(ownerType))
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.OWNER_TYPE.eq(ownerType));
        if(ownerId != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.OWNER_ID.eq(ownerId));
        if(StringUtils.isNotBlank(keyword))
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.USER_NAME.eq(keyword)
        			.or(Tables.EH_PAYMENT_CARD_TRANSACTIONS.MOBILE.eq(keyword)));
//        if(StringUtils.isNotBlank(consumeType))
//        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.PAID_TYPE.eq(consumeType));
        
        if(startDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.TRANSACTION_TIME.gt(startDate));
        if(endDate != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.TRANSACTION_TIME.lt(endDate));
        if(status != null)
        	query.addConditions(Tables.EH_PAYMENT_CARD_TRANSACTIONS.STATUS.eq(status));
        
        query.addOrderBy(Tables.EH_PAYMENT_CARD_TRANSACTIONS.CREATE_TIME.desc());
        if(pageSize != null)
        	query.addLimit(pageSize);
        
        return  query.fetch().map(r -> 
			ConvertHelper.convert(r, PaymentCardTransaction.class));
		
    }
    
    
    @Override
    public void createPaymentCardTransaction(PaymentCardTransaction paymentCardTransaction){
    	
    	long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhPaymentCardTransactions.class));
    	paymentCardTransaction.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhPaymentCardTransactionsDao dao = new EhPaymentCardTransactionsDao(context.configuration());
		dao.insert(paymentCardTransaction);
		
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPaymentCardTransactions.class, null);
    	
    }
}
