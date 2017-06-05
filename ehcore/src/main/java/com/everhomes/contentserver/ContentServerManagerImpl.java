package com.everhomes.contentserver;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.rest.contentserver.ContentServerErrorCode;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContentServerManagerImpl implements ContentServerMananger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServerManagerImpl.class);

    @Autowired
    private ConnectionProvider connectionProvider;

    @Autowired
    private ContentServerProvider contentServerProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private UserService userService;

    @Override
    public void upload(MessageHandleRequest request) throws Exception {
        Long[] ids=new Long[1];
        LoginToken login = null;
        try {
             login = WebTokenGenerator.getInstance().fromWebToken(request.getToken(), LoginToken.class);
            if (null != login) {
                ids[0]=login.getUserId();
            } else {
                LOGGER.error("Login token is null, reqToken=", request.getToken() 
                    + ", md5=" + request.getMd5() + ", objectId=" + request.getObjectId());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse login token, reqToken=" + request.getToken(), e);
        }

        // 由于返回给客户端的URL需要与客户端所使用的scheme保持一致，故需要从请求中分析出使用的是http还是https，
        // 再来拼接URL by lqs 20170119
        String schemeInRequest = getScheme(request);
        ContentServer server = contentServerService.selectContentServer();
        if (server == null) {
            LOGGER.error("Content server not found, userId=" + ids[0] + ", reqToken=" + request.getToken() + ", loginToken=" + login);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Content server not found");
        }
        ContentServerResource result = contentServerProvider.findByUidAndMD5(ids[0], request.getMd5());
        if (result != null) {
            request.setObjectId(Generator.createKey(server.getId(), result.getResourceId(), request.getObjectType()
                    .name()));
            request.setUrl(createUrl(server, result.getResourceId(), request.getObjectType().name(), request.getToken(), schemeInRequest));
            return;
        }
        // add transaction command
        Tuple<ContentServerResource, Boolean> resource = coordinationProvider.getNamedLock(
                CoordinationLocks.CREATE_RESOURCE.getCode()).enter(() -> {
            ContentServerResource r = contentServerProvider.findByUidAndMD5(ids[0], request.getMd5());
            if (r == null) {
                r = createResource(server.getId(), ids[0], request);
                contentServerProvider.addResource(r);
            }
            return r;
        });
        result = resource.first();
        request.setObjectId(Generator.createKey(server.getId(), result.getResourceId(), request.getObjectType().name()));
        request.setUrl(createUrl(server, result.getResourceId(), request.getObjectType().name(), request.getToken(), schemeInRequest));
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Upload resource file successfully, userId=" + ids[0] + ", reqToken=" + request.getToken() 
                + ", loginToken=" + login + ", objectId=" + request.getObjectId() + ", url=" + request.getUrl());
        }
    }

    private String createUrl(ContentServer content, String resourceId, String type, String token, String schemeInRequest) {
        int port = content.getPublicPort();
        if("https".equalsIgnoreCase(schemeInRequest)) {
            port = 443;
        }
        return String.format("%s://%s:%d/%s/%s?token=%s", schemeInRequest, content.getPublicAddress(), port, type,
                Generator.encodeUrl(resourceId), token);
    }

    private ContentServerResource createResource(Long serverId, Long uid, MessageHandleRequest request) {
        ContentServerResource contentServer = new ContentServerResource();
        contentServer.setMetadata(StringHelper.toJsonString(request.getMeta()));
        contentServer.setOwnerId(uid);
        String uri = request.getObjectType().name() + "/" + request.getMd5();
        contentServer.setResourceId(uri);
        contentServer.setResourceMd5(request.getMd5());
        contentServer.setResourceName(request.getFilename());
        contentServer.setResourceSize(request.getTotalSize());
        contentServer.setResourceType(request.getObjectType().getCode());
        return contentServer;
    }

    @Override
    public void delete(MessageHandleRequest request) {
        LoginToken token = WebTokenGenerator.getInstance().fromWebToken(request.getToken(), LoginToken.class);
        if (null == token) {
            LOGGER.error("Login token is null, reqToken=", request.getToken() 
                + ", md5=" + request.getMd5() + ", objectId=" + request.getObjectId());
            return;
        }
        contentServerProvider.deleteByUidAndResourceId(token.getUserId(), request.getObjectId());
        LOGGER.warn("Delete resource file, reqToken=", request.getToken() 
                + ", md5=" + request.getMd5() + ", objectId=" + request.getObjectId());
    }

    @Override
    public void auth(MessageHandleRequest request) {
        LoginToken login = null;
        try {
            login = WebTokenGenerator.getInstance().fromWebToken(request.getToken(), LoginToken.class);
        } catch (Exception e) {
            LOGGER.error("Failed to parse login token, reqToken=" + request.getToken(), e);
        }

        // if (!userService.isValidLoginToken(login)) {
        // LOGGER.error("invalid login token.auth failed");
        // throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
        // UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
        // "INVALID LOGON TOKEN");
        // }
        String md5 = "";
        switch (request.getAccessType()) {
        case LOOKUP:
            md5 = lookupInvoke(login, request.getObjectId());
            buildMetaData(request);
            break;
        case DELETE:
            md5 = deleteInvoke(login, request.getObjectId());
            break;
        case UPLOAD:
            md5 = uploadInvoke(login, request.getObjectId());
            break;
        default:
            LOGGER.error("Unsupported access type, accessType=" + request.getAccessType() + ", loginToken=" + login);
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_ACTION, "Unsupported access type");
        }

        request.setMd5(md5);
    }

    private void buildMetaData(MessageHandleRequest request) {
        String resourceId = Generator.decodeUrl(request.getObjectId());
        ContentServerResource resource = contentServerProvider.findByResourceId(resourceId);
        if (resource != null) {
            request.setFilename(resource.getResourceName());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("build object meta data {}", resource.getResourceName());
            }
        }
    }

    private String lookupInvoke(LoginToken login, String resourceId) {
        String orginResourceId = resourceId;
        resourceId = Generator.decodeUrl(resourceId);

        // user的头像使用固定的链接，但是返回给contentserver的md5又不是固定的     add by xq.tian  2017/04/19
        /*if (resourceId.startsWith("avatar/")) {
            String uid = resourceId.substring(resourceId.indexOf("/") + 1, resourceId.length());
            UserInfo userInfo = userService.getUserSnapshotInfo(Long.parseLong(uid));
            String avatarUri = userInfo.getAvatarUri();
            avatarUri = avatarUri.substring(avatarUri.lastIndexOf("/") + 1, avatarUri.length());
            avatarUri = Generator.decodeUrl(avatarUri);
            avatarUri = avatarUri.substring(avatarUri.lastIndexOf("/") + 1, avatarUri.length());
            return avatarUri;
        }*/

        ContentServerResource resource = contentServerProvider.findByResourceId(resourceId); 
        if (resource == null) {
            LOGGER.error("Resource not found, orginResourceId=" + orginResourceId +
                    ", decodeResourceId=" + resourceId + ", loginToken=" + login);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Resource file not found");
        }
        return resource.getResourceMd5();
    }

    private String deleteInvoke(LoginToken login, String resourceId) {
        String originResourceId = resourceId;
        resourceId = Generator.decodeUrl(resourceId);
        ContentServerResource resource = contentServerProvider.findByResourceId(resourceId);
        if (resource == null) {
            LOGGER.error("Resource file not found, originResourceId=" + originResourceId 
                + ", decodeResourceId=" + resourceId + ", loginToken=" + login);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Resource file not found");
        }
        if (login != null && login.getUserId() != resource.getOwnerId()) {
            LOGGER.error("Can not delete the resource file who is not the owner, operatorUid=" 
                + login.getUserId() + ", ownerId=" + resource.getOwnerId() + ", resourceId=" + resourceId);
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_USER, "invalid owner or user");
        }
        
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("The user can delete the resource, resourceId=" + resourceId + ", md5=" + resource.getResourceMd5()  
                + ", ownerId=" + resource.getOwnerId() + ", loginToken=" + login);
        }
        
        return resource.getResourceMd5();
    }

    private String uploadInvoke(LoginToken login, String resourceId) {
        if (login == null || login.getUserId() == 0) {
            LOGGER.info("Invalid login token, loginToken=" + login + ", resourceId=" + resourceId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Invalid login token");
        }
        return null;
    }

    /**
     * 从content server中分析出头，以便跟随客户端来决定返回的URL是http还是https
     * @param request
     * @return
     */
    private String getScheme(MessageHandleRequest request) {
        Map<String, String> meta = request.getMeta();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Try to strip the scheme from request, meta={}", meta);
        }
        
        String scheme = "http";
        if(meta != null) {
            try {
                String tmpScheme = meta.get("X-Forwarded-Scheme");
                if(tmpScheme != null && tmpScheme.trim().length() > 0) {
                    scheme = tmpScheme.trim();
                }
            } catch (Exception e) {
                LOGGER.error("Failed to strip scheme, meta={}", meta, e);
            }
        }
        
        return scheme;
    }
}
