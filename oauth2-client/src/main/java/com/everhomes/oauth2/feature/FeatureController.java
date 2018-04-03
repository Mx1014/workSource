package com.everhomes.oauth2.feature;

import com.everhomes.oauth2.endpoint.SdkConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

@Controller
@Scope("session")
public class FeatureController {

    @Autowired
    private AccessTokenManager tokenManager;

    @RequestMapping("/home")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        return "home";
    }

    @RequestMapping("/getUserInfo")
    public Object getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //可以不取 SessionId 而取其它合适的值
        String state = request.getSession().getId();

        if(tokenManager.getAccessToken(state) == null) {
            URI uri = new URI(String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=basic&state=%s#oauth2_redirect",
                    SdkConstants.API_AUTHORIZE_SERVICE_URI,
                    SdkConstants.API_KEY,
                    URLEncoder.encode(SdkConstants.CLIENT_REDIRECT_URI, "UTF-8"),
                    state
                    ));

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            return new ResponseEntity<Object>(httpHeaders, HttpStatus.SEE_OTHER);
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(new URI("/result#clear_flag"));
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
//            return "redirect:/result";
        }
    }

    @RequestMapping("/result")
    public String result(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        String state = request.getSession().getId();
        String token = this.tokenManager.getAccessToken(state);

        // acquire access token through OAuth2 provider's token service
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(String.format("%s/getUserInfo", SdkConstants.API_OAUTH2API_URI));

            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.addHeader("Authorization", String.format("Bearer %s", Base64.encodeBase64String(token.getBytes("UTF-8"))));

            CloseableHttpResponse res = httpclient.execute(post);
            try {
                HttpEntity resEntity = res.getEntity();
                if (resEntity != null) {
                    String responseString = IOUtils.toString(resEntity.getContent(), "UTF-8");

                    model.addAttribute("userInfo", responseString);
                }

                EntityUtils.consume(resEntity);
            } finally {
                res.close();
            }
        } finally {
            httpclient.close();
        }

        return "result";
    }
}
