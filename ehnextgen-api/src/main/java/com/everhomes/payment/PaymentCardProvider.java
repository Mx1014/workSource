package com.everhomes.payment;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;

public interface PaymentCardProvider {
	List<PaymentCardIssuerCommunity> listPaymentCardIssuerCommunity(Long ownerId,String ownerType);

	void updatePaymentCardIssuerCommunity(PaymentCardIssuerCommunity community);
	
	List<PaymentCard> listPaymentCard(Long ownerId,String ownerType,Long userId);
	
	List<PaymentCardIssuer> listPaymentCardIssuer(Long ownerId,String ownerType);
	
	PaymentCardIssuer findPaymentCardIssuerById(Long issuerId);
	PaymentCardIssuer findPaymentCardIssuerByToken(String token);
	void createPaymentCard(PaymentCard paymentCard);
	
	void updatePaymentCardIssuer(PaymentCardIssuer paymentCardIssuer);
	
	PaymentCard findPaymentCardById(Long cardId);
	
	void createPaymentCardRechargeOrder(PaymentCardRechargeOrder paymentCardRechargeOrder);
	
	List<PaymentCard> searchCardUsers(Long ownerId,String ownerType,String keyword,Byte status,Long pageAnchor,Integer pageSize);
	
	List<PaymentCardRechargeOrder> searchCardRechargeOrder(String ownerType,Long ownerId,Timestamp startDate,Timestamp endDate,
    		String rechargeType,Byte rechargeStatus,String keyword,Long pageAnchor,Integer pageSize);
	
	List<PaymentCardTransaction> searchCardTransactions(String ownerType,Long ownerId,Timestamp startDate,Timestamp endDate,
    		String consumeType,Byte status,String keyword,Long pageAnchor,Integer pageSize);
	
	PaymentCardRechargeOrder findPaymentCardRechargeOrderById(Long orderId);

	PaymentCardRechargeOrder findPaymentCardRechargeOrderByBizOrderNum(String bizOrderNum);
	
	void updatePaymentCardRechargeOrder(PaymentCardRechargeOrder order);
	
	PaymentCard findPaymentCardByCardNo(String cardNo,String vendorName);
	
	void createPaymentCardTransaction(PaymentCardTransaction paymentCardTransaction);
	
	Integer countCardUsers(Long ownerId,String ownerType,Integer namespaceId);
	
	void updatePaymentCard(PaymentCard paymentCard);
	
	Integer countPaymentCard(Long ownerId,String ownerType,Long userId);
	
	PaymentCardTransaction findPaymentCardTransactionByCondition(String token,String cardNo);
	
	List<PaymentCardTransaction> listCardTransactions(Integer pageSize,
			Timestamp startDate, Timestamp endDate,List<Byte> statuses,
			CrossShardListingLocator locator);
	
	List<PaymentCardRechargeOrder> listPaymentCardRechargeOrders(Integer pageSize,
			Timestamp startDate, Timestamp endDate,List<Byte> statuses,
			CrossShardListingLocator locator);

	List<PaymentCardAccount> listPaymentCardAccounts(String ownerType, Long ownerId);

	void deleteAccounts(String ownerType, Long ownerId);

	void createPaymentCardAccount(PaymentCardAccount account);
}
