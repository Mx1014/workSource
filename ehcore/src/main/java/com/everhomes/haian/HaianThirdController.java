// @formatter:off
package com.everhomes.haian;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    	String sign = stringMD5(temp);

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
	/**
	 * 获取加密后的字符串
	 * @param input
	 * @return
	 */
	private String stringMD5(String pw) {
		try {  
			// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）  
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");  
			// 输入的字符串转换成字节数组  
			byte[] inputByteArray = pw.getBytes();  
			// inputByteArray是输入字符串转换得到的字节数组  
			messageDigest.update(inputByteArray);  
			// 转换并返回结果，也是字节数组，包含16个元素  
			byte[] resultByteArray = messageDigest.digest();  
			// 字符数组转换成字符串返回  
			return byteArrayToHex(resultByteArray);  
		} catch (NoSuchAlgorithmException e) {  
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Xianluobo encrypt error");
		}  
	}

	private String byteArrayToHex(byte[] byteArray) {  
       
		// 首先初始化一个字符数组，用来存放每个16进制字符  
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };  
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））  
		char[] resultCharArray =new char[byteArray.length * 2];  
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去  
		int index = 0; 
		for (byte b : byteArray) {  
			resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];  
			resultCharArray[index++] = hexDigits[b& 0xf];  
		}
		// 字符数组组合成字符串返回  
		return new String(resultCharArray);  
	}
}
