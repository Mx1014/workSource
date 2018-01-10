package com.everhomes.rentalv2.message_handler;

import com.everhomes.parking.ParkingProvider;
import com.everhomes.rentalv2.*;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {

        //给预约人推送
        StringBuilder content = new StringBuilder();
//        content.append(user.getNickName());
        content.append("取消了");
//        content.append(rentalBill.getResourceName());
//        content.append("\n使用详情：");
//        content.append(rentalBill.getUseDetail());
//        if(null != rentalBill.getRentalCount() ){
//            managerContent.append("\n预约数：");
//            managerContent.append(rentalBill.getRentalCount());
//        }
        rentalCommonService.sendMessageToUser(rentalBill.getRentalUid(), content.toString());
    }
}
