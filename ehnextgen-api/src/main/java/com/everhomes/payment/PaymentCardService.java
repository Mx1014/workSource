package com.everhomes.payment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.payment.*;

public interface PaymentCardService {

	List<CardInfoDTO> listCardInfo(ListCardInfoCommand cmd);
	
	List<CardIssuerDTO> listCardIssuer(ListCardIssuerCommand cmd);
	
	CardInfoDTO applyCard(ApplyCardCommand cmd);
	
	CommonOrderDTO rechargeCard(RechargeCardCommand cmd);

    PreOrderDTO rechargeCardV2(RechargeCardCommand cmd);
	
	SearchCardUsersResponse searchCardUsers(SearchCardUsersCommand cmd);
	
	GetCardUserStatisticsDTO getCardUserStatistics(GetCardUserStatisticsCommand cmd);
	
	SearchCardRechargeOrderResponse searchCardRechargeOrder(SearchCardRechargeOrderCommand cmd);
	
	SearchCardTransactionsResponse searchCardTransactions(SearchCardTransactionsCommand cmd);
	
	void setCardPassword(SetCardPasswordCommand cmd);
    
    void resetCardPassword(ResetCardPasswordCommand cmd);
    
    SendCardVerifyCodeDTO sendCardVerifyCode(SendCardVerifyCodeCommand cmd);
    
    ListCardTransactionsResponse listCardTransactions(ListCardTransactionsCommand cmd);
    
    GetCardPaidQrCodeDTO getCardPaidQrCode(GetCardPaidQrCodeCommand cmd);
    
    GetCardPaidResultDTO getCardPaidResult(GetCardPaidResultCommand cmd);
    
    NotifyEntityDTO notifyPaidResult(NotifyEntityCommand cmd);
    
    void exportCardUsers(SearchCardUsersCommand cmd,HttpServletResponse response);
    
    void exportCardRechargeOrder(SearchCardRechargeOrderCommand cmd,HttpServletResponse response);
    
    void exportCardTransactions(SearchCardTransactionsCommand cmd,HttpServletResponse response);
    
    void updateCardRechargeOrder(UpdateCardRechargeOrderCommand cmd);

    void payNotify(OrderPaymentNotificationCommand cmd);

    void refundNotify(OrderPaymentNotificationCommand cmd);

    PaymentCardHotlineDTO getHotline(GetHotlineCommand cmd);

    void updateHotline(UpdateHotlineCommand cmd);

    void freezeCard(FreezeCardCommand cmd);

    void unbunleCard(Long cardId);

    CardInfoDTO getCardInfo(Long cardId);

    void refundOrderV2(Long orderId);
}
