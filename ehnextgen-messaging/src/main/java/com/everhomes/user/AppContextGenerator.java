// @formatter:off
package com.everhomes.user;


import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.util.WebTokenGenerator;
import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.Base64;

public class AppContextGenerator {


    /**
     * 生成Base64的WebToken
     * @param object
     * @return
     */
    public static String toBase64WebToken(AppContext object) {
        return base64SafeUrlEncode(object.toString());
    }

    /**
     * 解析webtoken，支持ContextDTO的Base64加密token和SceneTokenDTO通过WebTokenGenerator生成的token
     * 此方法的目的是去掉ContextDTO，并保持原有WebTokenGenerator生成的token的兼容
     * @param tokenString
     * @return
     */
    public static AppContext fromWebToken(String tokenString) {


        if(tokenString == null){
            return null;
        }


        //先尝试用ContextDTO解析
        try {
            String s = base64SafeUrlDecode(tokenString);
            Gson gson = new Gson();
            AppContext context = gson.fromJson(s, AppContext.class);
            return context;
        }catch (Exception ex){
            //try WebTokenGenerator
        }


        //在尝试用SceneTokenDTO解析
        try {
            SceneTokenDTO dto = WebTokenGenerator.getInstance().fromWebToken(tokenString, SceneTokenDTO.class);

            if(dto != null){
                AppContext context = new AppContext();
                switch (UserCurrentEntityType.fromCode(dto.getEntityType())){
                    case COMMUNITY:
                    case COMMUNITY_COMMERCIAL:
                    case COMMUNITY_RESIDENTIAL:
                        context.setCommunityId(dto.getEntityId());
                        break;
                    case ORGANIZATION:
                    case ENTERPRISE:
                        context.setOrganizationId(dto.getEntityId());
                        break;
                    case FAMILY:
                        context.setFamilyId(dto.getEntityId());
                        break;
                    default:
                            context = null;
                }

                return context;
            }

        }catch (Exception ex){
            //nothing return null
        }


        return null;
    }



    private static String base64SafeUrlDecode(String path) {
        String result = path.replace("_", "/").replace("-", "+");
        int length = result.length();
        if (length % 4 == 2) {
            result += "==";
        } else if (length % 4 == 3) {
            result += "=";
        }
        return new String(Base64.getDecoder().decode(result.getBytes(Charset.forName("utf-8"))));
    }

    public static String base64SafeUrlEncode(String path) {
        byte[] code = Base64.getEncoder().encode(path.getBytes(Charset.forName("utf-8")));
        String str = new String(code, Charset.forName("utf-8")).replace("/", "_").replace("+", "-").replace("=", "");
        return str;
    }

}
