// @formatter:off
package com.everhomes.payment;

import java.util.List;

import com.everhomes.rest.payment.*;

public interface PaymentCardVendorHandler {
    String PAYMENTCARD_VENDOR_PREFIX = "PaymentCardVendor-";
    
    List<CardInfoDTO> getCardInfoByVendor(ListCardInfoCommand cmd);

    default CardInfoDTO getCardInfo(PaymentCard paymentCard){return null;}
    
    CardInfoDTO applyCard(ApplyCardCommand cmd,PaymentCardIssuer cardIssuer);

    default void setCardPassword(SetCardPasswordCommand cmd,PaymentCard paymentCard){}

    default void resetCardPassword(ResetCardPasswordCommand cmd,PaymentCard paymentCard){}
    
    List<CardTransactionOfMonth> listCardTransactions(ListCardTransactionsCommand cmd,PaymentCard card);

    default String getCardPaidQrCodeByVendor(PaymentCard paymentCard){return null;}
    
    void rechargeCard(PaymentCardRechargeOrder order, PaymentCard card);

    default void freezeCard(FreezeCardCommand cmd){}

    default void unbundleCard(PaymentCard paymentCard){}
}
