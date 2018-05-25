// @formatter:off
package com.everhomes.user;


import com.everhomes.rest.launchpadbase.ContextDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.util.WebTokenGenerator;
import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.Base64;

public class SceneTokenGenerator {


    /**
     * 生成Base64的WebToken
     * @param object
     * @return
     */
    public String toBase64WebToken(ContextDTO object) {
        return base64SafeUrlEncode(object.toString());
    }

    /**
     * 解析webtoken，支持ContextDTO的Base64加密token和SceneTokenDTO通过WebTokenGenerator生成的token
     * 此方法的目的是去掉ContextDTO，并保持原有WebTokenGenerator生成的token的兼容
     * @param tokenString
     * @return
     */
    public static SceneTokenDTO fromWebToken(String tokenString) {

        try {
            String s = base64SafeUrlDecode(tokenString);
            Gson gson = new Gson();
            ContextDTO t = gson.fromJson(s, ContextDTO.class);

            if(t != null){
                SceneTokenDTO dto = new SceneTokenDTO();
                dto.setNamespaceId(UserContext.getCurrentNamespaceId());
                dto.setUserId(UserContext.currentUserId());

                //优先解析成PARK_TOURIST，然后是ENTERPRISE，然后是FAMILY
                if(t.getCommunityId() != null){
                    dto.setEntityId(t.getCommunityId());
                    dto.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());
                    dto.setScene(SceneType.PARK_TOURIST.getCode());
                }else if(t.getOrgId() != null){
                    dto.setEntityId(t.getOrgId());
                    dto.setEntityType(UserCurrentEntityType.ENTERPRISE.getCode());
                    dto.setScene(SceneType.ENTERPRISE.getCode());
                }else if(t.getFamilyId() != null){
                    dto.setEntityId(t.getFamilyId());
                    dto.setEntityType(UserCurrentEntityType.FAMILY.getCode());
                    dto.setScene(SceneType.FAMILY.getCode());
                }else {
                    throw new Exception();
                }

                return dto;
            }

        }catch (Exception ex){
            //try WebTokenGenerator
        }


        SceneTokenDTO dto = WebTokenGenerator.getInstance().fromWebToken(tokenString, SceneTokenDTO.class);
        return dto;
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
