package com.everhomes.payment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.ListCardInfoCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;

@Component
public class PaymentCardServiceImpl implements PaymentCardService{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCardServiceImpl.class);

    @Autowired
    private PaymentCardProvider paymentCardProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    public List<CardInfoDTO> listCardInfo(ListCardInfoCommand cmd){
    	User user = UserContext.current().getUser();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

    	
    	
    	return null;
    }
    
    private PaymentCardVendorHandler getPaymentCardVendorHandler(String vendorName) {
    	PaymentCardVendorHandler handler = null;
        
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = PaymentCardVendorHandler.PAYMENTCARD_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        
        return handler;
    }
}
