package com.everhomes.rentalv2.message_handler;

import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.*;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX + "vip_parking")
public class VipParkingRentalMessageHandler implements RentalMessageHandler {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;
    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {

        RentalResourceType type = rentalv2Provider.findRentalResourceTypeById(rentalBill.getResourceTypeId());

        Map<String, String> map = new HashMap<>();
        map.put("resourceTypeName", type.getName());
        String content = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
                RentalNotificationTemplateCode.RENTAL_USER_CANCEL_ORDER, RentalNotificationTemplateCode.locale, map, "");

        //给预约人推送
        rentalCommonService.sendRouterMessageToUser(rentalBill.getRentalUid(), content,
                rentalBill.getId(), rentalBill.getResourceType());
    }

    @Override
    public void addOrderSendMessage(RentalOrder rentalBill) {

    }
}
