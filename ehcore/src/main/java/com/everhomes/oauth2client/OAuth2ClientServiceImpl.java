// @formatter:off
package com.everhomes.oauth2client;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.oauth2client.handler.OAuth2ClientHandler;
import com.everhomes.oauth2client.handler.OAuth2ClientHandlerConstant;
import com.everhomes.rest.oauth2client.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xq.tian on 2017/3/6.
 */
@Service
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2ClientServiceImpl.class);

    @Autowired
    private OAuth2ClientTokenProvider oAuth2ClientTokenProvider;

    @Autowired
    private OAuth2ServerProvider oAuth2ServerProvider;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    private final Map<String, OAuth2ClientHandler> handlerMap = new HashMap<>();

    @Override
    public String getRedirectUrl(String vendor, String serviceUrl) {

        OAuth2Server server = oAuth2ServerProvider.findOAuth2ServerByVendor(vendor);
        if (server == null) {
            LOGGER.error("oauth2 server not exists for vendor={}", vendor);
            throw RuntimeErrorException.errorWith(OAuth2ClientServiceErrorCode.SCOPE,
                    OAuth2ClientServiceErrorCode.ERROR_OAUTH2_SERVER_NOT_EXISTS, "oauth2 server not exists for vendor=%s", vendor);
        }

        OAuth2ClientHandler handler = this.getHandler(vendor);
        User user = UserContext.current().getUser();
        Long userId = 0L;
        if (user != null) {
            userId = user.getId();
        }
        OAuth2ClientToken accessToken = oAuth2ClientTokenProvider.findLastTokenByUserAndVendor(userId, vendor, OAuth2ClientTokenType.ACCESS_TOKEN);

        String redirectUrl = getHomeUrl() + serviceUrl;
        if (accessToken != null) {
            if (accessToken.expire()) {
                boolean success = refreshToken(vendor, server, handler, accessToken);
                // 刷新失败，返回授权页面
                if (!success) {
                    redirectUrl = handler.getAuthorizeUrl(server);
                }
            }
        } else {
            // 返回授权页面
            redirectUrl = handler.getAuthorizeUrl(server);
            getOrPutServiceUrlToCache(userId, vendor, getHomeUrl() + serviceUrl);
        }
        return redirectUrl;
    }

    private String getOrPutServiceUrlToCache(Long userId, String vendor, String serviceUrl) {
        Accessor accessor = bigCollectionProvider.getMapAccessor("oauth2Client", "");
        ValueOperations op = accessor.getTemplate().opsForValue();
        String key = String.format("oauth2Client:%s:%s", userId, vendor);
        if (serviceUrl == null) {
            serviceUrl = (String) op.get(key);
            if (serviceUrl == null) {
                LOGGER.error("service url might be expire, userId={}, vendor={}", userId, vendor);
                throw RuntimeErrorException.errorWith(OAuth2ClientServiceErrorCode.SCOPE,
                        OAuth2ClientServiceErrorCode.ERROR_SERVICE_URL_NOT_FOUND_ERROR, "service url might be expire");
            }
        } else {
            // 设置1个小时过期时间
            op.set(key, serviceUrl, 1, TimeUnit.HOURS);
        }
        return serviceUrl;
    }

    @Override
    public String getAccessToken(String vendor, String code) {
        OAuth2ClientHandler handler = this.getHandler(vendor);

        OAuth2Server server = oAuth2ServerProvider.findOAuth2ServerByVendor(vendor);
        if (server == null) {
            LOGGER.error("oauth2 server not exists for vendor={}", vendor);
            throw RuntimeErrorException.errorWith(OAuth2ClientServiceErrorCode.SCOPE,
                    OAuth2ClientServiceErrorCode.ERROR_OAUTH2_SERVER_NOT_EXISTS, "oauth2 server not exists for vendor=%s", vendor);
        }

        // 业务handler获取token
        Tuple<OAuth2ClientToken, OAuth2ClientToken> tuple = handler.getToken(server, code);

        User user = UserContext.current().getUser();
        Long userId = 0L;
        if (user != null) {
            userId = user.getId();
        }
        tuple.first().setGrantorUid(userId);
        tuple.second().setGrantorUid(userId);

        // 保存到数据库
        oAuth2ClientTokenProvider.createToken(tuple.first());
        oAuth2ClientTokenProvider.createToken(tuple.second());

        // 重定向到业务的页面
        return getOrPutServiceUrlToCache(userId, vendor, null);
        // return handler.getServiceUrl(server);
    }

    @Override
    public OAuth2ClientApiResponse api(String vendor, OAuth2ClientApiCommand cmd) {
        OAuth2Server server = oAuth2ServerProvider.findOAuth2ServerByVendor(vendor);
        User currUser = UserContext.current().getUser();
        OAuth2ClientHandler handler = this.getHandler(vendor);
        OAuth2ClientToken accessToken = oAuth2ClientTokenProvider.findLastTokenByUserAndVendor(currUser.getId(), vendor, OAuth2ClientTokenType.ACCESS_TOKEN);
        if (accessToken.expire()) {
            refreshToken(vendor, server, handler, accessToken);
            accessToken = oAuth2ClientTokenProvider.findLastTokenByUserAndVendor(currUser.getId(), vendor, OAuth2ClientTokenType.ACCESS_TOKEN);
        }
        return handler.api(server, accessToken, cmd);
    }

    @Override
    public void updateOAuth2Server(UpdateOAuth2ServerCommand cmd) {
        if (cmd.getId() != null) {
            OAuth2Server oAuth2Server = oAuth2ServerProvider.findOAuth2ServerById(cmd.getId());
            if (cmd.getVendor() != null) {
                oAuth2Server.setVendor(cmd.getVendor());
            }
            if (cmd.getClientId() != null) {
                oAuth2Server.setClientId(cmd.getClientId());
            }
            if (cmd.getClientSecret() != null) {
                oAuth2Server.setClientSecret(cmd.getClientSecret());
            }
            if (cmd.getAuthorizeUrl() != null) {
                oAuth2Server.setAuthorizeUrl(cmd.getAuthorizeUrl());
            }
            if (cmd.getTokenUrl() != null) {
                oAuth2Server.setTokenUrl(cmd.getTokenUrl());
            }
            if (cmd.getGrantType() != null) {
                oAuth2Server.setGrantType(cmd.getGrantType());
            }
            if (cmd.getRedirectUri() != null) {
                oAuth2Server.setRedirectUri(cmd.getRedirectUri());
            }
            if (cmd.getResponseType() != null) {
                oAuth2Server.setResponseType(cmd.getResponseType());
            }
            if (cmd.getScope() != null) {
                oAuth2Server.setScope(cmd.getScope());
            }
            if (cmd.getState() != null) {
                oAuth2Server.setState(cmd.getState());
            }
            oAuth2ServerProvider.updateOauth2Server(oAuth2Server);
        }
    }

    @Override
    public OAuth2ServerDTO createOAuth2Server(CreateOAuth2ServerCommand cmd) {
        OAuth2Server oAuth2Server = ConvertHelper.convert(cmd, OAuth2Server.class);
        oAuth2ServerProvider.createOAuth2Server(oAuth2Server);
        return ConvertHelper.convert(oAuth2Server, OAuth2ServerDTO.class);
    }

    @Override
    public OAuth2ServerDTO getOAuth2Server(GetOAuth2ServerCommand cmd) {
        OAuth2Server oAuth2Server = oAuth2ServerProvider.findOAuth2ServerById(cmd.getId());
        return ConvertHelper.convert(oAuth2Server, OAuth2ServerDTO.class);
    }

    private boolean refreshToken(String vendor, OAuth2Server server, OAuth2ClientHandler handler, OAuth2ClientToken accessToken) {
        User user = UserContext.current().getUser();
        Long userId = 0L;
        if (user != null) {
            userId = user.getId();
        }
        OAuth2ClientToken refreshToken = oAuth2ClientTokenProvider.findLastTokenByUserAndVendor(userId, vendor, OAuth2ClientTokenType.REFRESH_TOKEN);
        Tuple<OAuth2ClientToken, OAuth2ClientToken> tuple = handler.refreshToken(server, refreshToken, accessToken);
        if (tuple != null) {
            tuple.first().setGrantorUid(userId);
            tuple.second().setGrantorUid(userId);

            oAuth2ClientTokenProvider.createToken(tuple.first());
            oAuth2ClientTokenProvider.createToken(tuple.second());
            return true;
        } else {
            LOGGER.error("refresh token error");
            return false;
        }
    }

    private OAuth2ClientHandler getHandler(String vendor) {
        OAuth2ClientHandler handler = handlerMap.get(vendor);
        if (handler == null) {
            handler = PlatformContext.getComponent(OAuth2ClientHandlerConstant.OAUTH2_HANDLER + vendor);
            if (handler != null) {
                handlerMap.put(vendor, handler);
            } else {
                // 通用的handler
                handler = this.getHandler(OAuth2ClientHandlerConstant.HANDLER_GENERAL);
            }
        }
        return handler;
    }

    private String getHomeUrl() {
        return configurationProvider.getValue(ConfigConstants.HOME_URL, "");
    }
}
