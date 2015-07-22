package com.everhomes.contentserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.user.LoginToken;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.WebTokenGenerator;

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
        LoginToken login;
        try {
             login = WebTokenGenerator.getInstance().fromWebToken(request.getToken(), LoginToken.class);
            if (null != login) {
                LOGGER.error("cannot find login information");
                ids[0]=login.getUserId();
            }
        } catch (Exception e) {
            // skip validate
        }

        ContentServer server = contentServerService.selectContentServer();
        if (server == null) {
            LOGGER.error("cannot find server.userId={}", ids[0]);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "cannot find server");
        }
        ContentServerResource result = contentServerProvider.findByUidAndMD5(ids[0], request.getMd5());
        if (result != null) {
            request.setObjectId(Generator.createKey(server.getId(), result.getResourceId(), request.getObjectType()
                    .name()));
            request.setUrl(createUrl(server, result.getResourceId(), request.getObjectType().name(), request.getToken()));
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
        request.setUrl(createUrl(server, result.getResourceId(), request.getObjectType().name(), request.getToken()));
    }

    private String createUrl(ContentServer content, String resourceId, String type, String token) {
        return String.format("http://%s:%d/%s/%s?token=%s", content.getPublicAddress(), content.getPublicPort(), type,
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
            LOGGER.error("may be some internal error.token can convert or is empty.tokenString={}", request.getToken());
            return;
        }
        contentServerProvider.deleteByUidAndResourceId(token.getUserId(), request.getObjectId());
        LOGGER.warn("delete file record from database.file md5={},objectId={}", request.getMd5(), request.getObjectId());
    }

    @Override
    public void auth(MessageHandleRequest request) {
        LoginToken login = null;
        try {
            login = WebTokenGenerator.getInstance().fromWebToken(request.getToken(), LoginToken.class);
        } catch (Exception e) {
            // skip validate
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
            break;
        case DELETE:
            md5 = deleteInvoke(login, request.getObjectId());
            break;
        case UPLOAD:
            md5 = uploadInvoke(login, request.getObjectId());
            break;
        default:
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_ACTION, "invalid action.cannot known");
        }
        request.setMd5(md5);
    }

    private String lookupInvoke(LoginToken login, String resourceId) {
        resourceId = Generator.decodeUrl(resourceId);
        ContentServerResource resource = contentServerProvider.findByResourceId(resourceId);
        if (resource == null) {
            LOGGER.error("cannot find resource information");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "cannot find file");
        }
        return resource.getResourceMd5();

    }

    private String deleteInvoke(LoginToken login, String uniqueId) {
        uniqueId = Generator.decodeUrl(uniqueId);
        ContentServerResource resource = contentServerProvider.findByResourceId(uniqueId);
        if (resource == null) {
            LOGGER.error("can not find file information");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find the file");
        }
        if (login != null && login.getUserId() != resource.getOwnerId()) {
            LOGGER.error("cannot delete file.current user is not own.uid={},own={}", login.getUserId(),
                    resource.getOwnerId());
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_USER, "invalid owner or user");
        }
        LOGGER.error("the usr cannot delete this file.objectId={}", uniqueId);
        return resource.getResourceMd5();
    }

    private String uploadInvoke(LoginToken login, String uniqueId) {
        if (login == null || login.getUserId() == 0) {
            LOGGER.info("can not access.userId is empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "user is invalid.userId=" + login.getUserId());
        }
        return null;
    }

}
