package com.everhomes.oauth2.endpoint;

import com.google.gson.Gson;
import com.everhomes.oauth2.feature.AccessTokenManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
public class EndPointController {

    @Autowired
    private AccessTokenManager tokenManager;

    @RequestMapping("/redirect")
    public String home(
        @RequestParam(value="code", required = true) String code,
        @RequestParam(value="state", required = true) String state,
        HttpServletRequest request, HttpServletResponse response) throws IOException {

        // acquire access token through OAuth2 provider's token service
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(SdkConstants.API_TOKEN_SERVICE_URI);

            String formData = String.format("grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s", code,
                    URLEncoder.encode(SdkConstants.CLIENT_REDIRECT_URI, "UTF-8"),
                    SdkConstants.API_KEY);

            String credential = String.format("%s:%s", SdkConstants.API_KEY, SdkConstants.API_SECRET);

            post.setEntity(new StringEntity(formData));
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.addHeader("Authorization", String.format("Basic %s", Base64.encodeBase64String(credential.getBytes("UTF-8"))));

            CloseableHttpResponse res = httpclient.execute(post);
            try {
                HttpEntity resEntity = res.getEntity();
                if (resEntity != null) {
                    Gson gson = new Gson();
                    String responseString = IOUtils.toString(resEntity.getContent(), "UTF-8");
                    OAuth2AccessTokenResponse tokenResponse = gson.fromJson(responseString, OAuth2AccessTokenResponse.class);

                    this.tokenManager.setAccessToken(state, tokenResponse.getAccess_token());
                }

                EntityUtils.consume(resEntity);
            } finally {
                res.close();
            }
        } finally {
            httpclient.close();
        }

        return "redirect:/result";
    }
}
