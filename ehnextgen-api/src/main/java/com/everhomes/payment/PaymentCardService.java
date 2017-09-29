package com.everhomes.payment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardIssuerDTO;
import com.everhomes.rest.payment.GetCardPaidQrCodeCommand;
import com.everhomes.rest.payment.GetCardPaidQrCodeDTO;
import com.everhomes.rest.payment.GetCardPaidResultCommand;
import com.everhomes.rest.payment.GetCardPaidResultDTO;
import com.everhomes.rest.payment.GetCardUserStatisticsCommand;
import com.everhomes.rest.payment.GetCardUserStatisticsDTO;
import com.everhomes.rest.payment.ListCardInfoCommand;
import com.everhomes.rest.payment.ListCardIssuerCommand;
import com.everhomes.rest.payment.ListCardTransactionsCommand;
import com.everhomes.rest.payment.ListCardTransactionsResponse;
import com.everhomes.rest.payment.NotifyEntityCommand;
import com.everhomes.rest.payment.NotifyEntityDTO;
import com.everhomes.rest.payment.RechargeCardCommand;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.SearchCardRechargeOrderCommand;
import com.everhomes.rest.payment.SearchCardRechargeOrderResponse;
import com.everhomes.rest.payment.SearchCardTransactionsCommand;
import com.everhomes.rest.payment.SearchCardTransactionsResponse;
import com.everhomes.rest.payment.SearchCardUsersCommand;
import com.everhomes.rest.payment.SearchCardUsersResponse;
import com.everhomes.rest.payment.SendCardVerifyCodeCommand;
import com.everhomes.rest.payment.SendCardVerifyCodeDTO;
import com.everhomes.rest.payment.SetCardPasswordCommand;
import com.everhomes.rest.payment.UpdateCardRechargeOrderCommand;

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
}
