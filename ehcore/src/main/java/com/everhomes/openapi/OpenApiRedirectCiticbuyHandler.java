package com.everhomes.openapi;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.openapi.OpenApiRedirectHandler;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq.tian on 2018/4/16.
 */
@Component(OpenApiRedirectHandler.PREFIX + "citicbuy")
public class OpenApiRedirectCiticbuyHandler implements OpenApiRedirectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiRedirectCiticbuyHandler.class);

    private final static char[] HEX_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private final static Charset UTF_8 = Charset.forName("UTF-8");

    private static String SECRET_KEY_CONFIG_KEY = "openapi.redirect.citicbuy.secretKey";
    private static String IV_CONFIG_KEY = "openapi.redirect.citicbuy.iv";

    private static String SECRET_KEY = "9PwXis10alYBRtaP";// 密码
    private static String IV = "6d520560Dc801F75";// 偏移量

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String build(String redirectUrl, Map<String, String[]> parameterMap) {
        initKey();

        Map<String, String> data = new HashMap<>();
        data.put("channel", "mybayapp");
        data.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        User user = UserContext.current().getUser();
        if (user != null) {
            UserIdentifier id = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
            if (id != null) {
                data.put("uid", id.getIdentifierToken());
            }
        }

        String encrypt = "";
        try {
            encrypt = encrypt(StringHelper.toJsonString(data));
        } catch (Exception e) {
            LOGGER.error("Encrypt data error", e);
        }

        Map<String, Object> uriParams = new HashMap<>();
        uriParams.put("param", encrypt);

        return buildURLParams(redirectUrl, uriParams);
    }

    private String buildURLParams(String url, Map<String, Object> uriParams) {
        StringBuilder urlSb = new StringBuilder(url);
        if (url.contains("?")) {
            urlSb.append("&");
        } else {
            urlSb.append("?");
        }
        uriParams.forEach((k, v) -> {
            if (k != null && v != null) {
                urlSb.append(k).append("=").append(String.valueOf(v)).append("&");
            }
        });
        return urlSb.substring(0, urlSb.length() - 1);
    }

    private void initKey() {
        try {
            SECRET_KEY = configurationProvider.getValue(
                    SECRET_KEY_CONFIG_KEY, SECRET_KEY);

            IV = configurationProvider.getValue(
                    IV_CONFIG_KEY, IV);
        } catch (Exception e) {
            //
        }
    }

    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
        byte[] bytes = cipher.doFinal(data.getBytes(UTF_8));
        return toHexString(bytes);
    }

    public String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_CHARS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_CHARS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static AlgorithmParameterSpec makeIv() {
        return new IvParameterSpec(IV.getBytes(UTF_8));
    }

    private static Key makeKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(UTF_8), "AES");
    }

    /*public static void main(String[] args) {
        OpenApiRedirectCiticbuyHandler handler = new OpenApiRedirectCiticbuyHandler();
        String ret = handler.build("http://zuolin.com", null);
        System.out.println(ret);
    }*/
}