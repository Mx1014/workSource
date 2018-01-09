package com.everhomes.rentalv2.message_handler;

import com.everhomes.rentalv2.*;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalOrderHandler.RENTAL_ORDER_HANDLER_PREFIX + "default")
public class DefaultRentalMessageHandler implements RentalMessageHandler {

    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private RentalCommonServiceImpl rentalCommonService;

    @Override
    public void cancelOrderSendMessage(RentalOrder rentalBill) {
        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());

//			RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
        if(null == rs)
            return;
        //给负责人推送
        StringBuilder managerContent = new StringBuilder();
        User user = userProvider.findUserById(rentalBill.getRentalUid()) ;
        if (null == user )
            return;
        managerContent.append(user.getNickName());
        managerContent.append("取消了");
        managerContent.append(rentalBill.getResourceName());
        managerContent.append("\n使用详情：");
        managerContent.append(rentalBill.getUseDetail());
        if(null != rentalBill.getRentalCount() ){
            managerContent.append("\n预约数：");
            managerContent.append(rentalBill.getRentalCount());
        }
        rentalCommonService.sendMessageToUser(rs.getChargeUid(), managerContent.toString());
    }
}
