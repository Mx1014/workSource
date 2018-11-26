package com.everhomes.openapi.zhenzhihui;

import com.alibaba.fastjson.JSON;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.ControllerBase;
import com.everhomes.entity.EntityType;
import com.everhomes.payment.taotaogu.AESCoder;
import com.everhomes.rest.openapi.zhenzhihui.TicketCommand;
import com.everhomes.server.schema.tables.pojos.EhTickets;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RequireAuthentication;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 对接圳智慧
 */
@RestController
@RequestMapping("/openapi/zhenzhihui")
public class ZhenzhihuiOpenController extends ControllerBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhenzhihuiOpenController.class);
    static final String KEY = "A6465651FDDC5E4B";

    @Autowired
    private TicketProvider ticketProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ContentServerService contentServerService;

    /**
     *
     * <b>URL: /openapi/zhenzhihui/getUserInfo</b>
     * <p>根据ticket获取用户信息 </p>
     * @return
     */
    @RequireAuthentication(false)
    @RequestMapping("getUserInfo")
    public Map getUserInfo(TicketCommand cmd){

        String ticketStr = cmd.getTicket();
        assert StringUtils.isNotEmpty(ticketStr);

        // 根据ticket获取用户信息
        EhTickets tickets = ticketProvider.getUserIdByTicket(ticketStr);
        if (tickets != null ) {
            Long userId = tickets.getUserId();
            User user = userProvider.findUserById(userId);
            String openid = user.getUuid();
            String nickname = user.getNickName();
            Byte sex = user.getGender();
            String code = tickets.getRedirectCode();

            String imgUrl = contentServerService.parserUri(user.getAvatar(), EntityType.USER.getCode(), user.getId());

            // 组装信息
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("openid", openid);
            userInfo.put("nickname", nickname);
            userInfo.put("sex", Byte.toString(sex));
            userInfo.put("headimgurl", imgUrl);
            userInfo.put("code", code);

            String userInfoJsonStr = JSON.toJSONString(userInfo);

            String encryptedUserInfo = null;
            try {
                // 加密信息
                encryptedUserInfo = Base64.encodeBase64String(AESCoder.encrypt(userInfoJsonStr.getBytes("utf-8"), KEY.getBytes("utf-8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, String> wappedUserInfo = new HashMap<>();
            wappedUserInfo.put("parameter", encryptedUserInfo);
            return wappedUserInfo;
        }
        return null;
    }

}
