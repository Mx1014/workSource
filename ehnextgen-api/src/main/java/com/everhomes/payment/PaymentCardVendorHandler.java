// @formatter:off
package com.everhomes.payment;

import java.util.List;

import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.CardTransactionOfMonth;
import com.everhomes.rest.payment.ListCardInfoCommand;
import com.everhomes.rest.payment.ListCardTransactionsCommand;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.SetCardPasswordCommand;

public interface PaymentCardVendorHandler {
    String PAYMENTCARD_VENDOR_PREFIX = "PaymentCardVendor-";
    
    List<CardInfoDTO> getCardInfoByVendor(ListCardInfoCommand cmd);
    
    CardInfoDTO applyCard(ApplyCardCommand cmd,PaymentCardIssuer cardIssuer);
    
    void setCardPassword(SetCardPasswordCommand cmd,PaymentCard paymentCard);
    
    void resetCardPassword(ResetCardPasswordCommand cmd,PaymentCard paymentCard);
    
    List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd,PaymentCard card);
    
    String getCardPaidQrCodeByVendor(PaymentCard paymentCard);
    
    void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card);
}
