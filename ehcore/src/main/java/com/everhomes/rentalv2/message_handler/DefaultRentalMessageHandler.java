package com.everhomes.rentalv2.message_handler;

import com.everhomes.rentalv2.*;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sw on 2018/1/5.
 */
@Component(RentalMessageHandler.RENTAL_MESSAGE_HANDLER_PREFIX + "default")
public class DefaultRentalMessageHandler implements RentalMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRentalMessageHandler.class);

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

    @Override
    public void addOrderSendMessage(RentalOrder rentalBill) {
        User user = this.userProvider.findUserById(rentalBill.getRentalUid()) ;
        if (null == user )
            return;

        RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());

//			RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
        if(null == rs)
            return;

        try{
            Map<String, String>map = new HashMap<>();
            map.put("userName", user.getNickName());
            map.put("resourceName", rentalBill.getResourceName());
            map.put("useDetail", rentalBill.getUseDetail());
            map.put("rentalCount", rentalBill.getRentalCount() ==null ? "1" : String.valueOf(rentalBill.getRentalCount()));
            rentalCommonService.sendMessageCode(rs.getChargeUid(), map, RentalNotificationTemplateCode.RENTAL_ADMIN_NOTIFY);
        }catch(Exception e){
            LOGGER.error("SEND MESSAGE FAILED", e);
        }
    }
}
