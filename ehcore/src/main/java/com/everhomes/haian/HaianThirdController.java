// @formatter:off
package com.everhomes.haian;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.everhomes.parking.handler.Utils;
import com.everhomes.util.MD5Utils;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value="Haian controller", site="haian")
@RestController
@RequestMapping("/haian")
public class HaianThirdController extends ControllerBase {
    
    @Autowired
    private UserProvider userProvider;
    
    /**
     * <b>URL: /haian/loginInsurobot</b>
     * <p>海岸 险萝卜 服务 加密</p>
     */
    @RequestMapping("loginInsurobot")
    public void loginInsurobot(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        
    	User user = UserContext.current().getUser();
    	UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
    	
    	String mobile = userIdentifier.getIdentifierToken();
    	String temp = mobile + "|" + InsurobotConstant.SECRET_KEY;
    	String sign = MD5Utils.getMD5(temp);

    	StringBuilder sb = new StringBuilder();
    	sb.append(InsurobotConstant.XIANLUOBO_URL).append("?")
    		.append("provider=").append(InsurobotConstant.PROVIDER)
    		.append("&mobile=").append(mobile)
    		.append("&sign=").append(sign)
    		.append("&channelId=").append(InsurobotConstant.CHANNELID)
    		.append("&actId=").append(InsurobotConstant.ACTID)
    		.append("&fkw=").append(InsurobotConstant.FKW)
    		.append("&carNo=");
    	
    	resp.sendRedirect(sb.toString());
    }

}
