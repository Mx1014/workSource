package com.everhomes.contentserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.user.LoginToken;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

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
    private UserService userService;

    @Override
    public void upload(MessageHandleRequest request) throws Exception {
        LOGGER.info("upload file and send response.request={}", request);
        LoginToken login = LoginToken.fromTokenString(request.getToken());
        if (null == login) {
            LOGGER.error("cannot find login information");
            throw new RuntimeErrorException("token message is invalid");
        }
        ContentServerResource result = contentServerProvider.findByUidAndMD5(login.getUserId(), request.getMd5());

        ContentServer server = contentServerService.selectContentServer();
        if (server == null) {
            LOGGER.error("cannot find server.userId={}", login.getUserId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "cannot find server");
        }
        if (result == null) {
            result = createResource(server.getId(), login.getUserId(), request);
            contentServerProvider.addResource(result);
        }
        request.setObjectId(result.getResourceId());
    }

    private ContentServerResource createResource(Long serverId, Long uid, MessageHandleRequest request) {
        ContentServerResource contentServer = new ContentServerResource();
        contentServer.setMetadata(StringHelper.toJsonString(request.getMeta()));
        contentServer.setOwnerId(uid);
        String uri = request.getObjectType().name() + "/" + request.getMd5();
        if (request.getParamsMap() != null) {
            StringBuilder sb = new StringBuilder();
            request.getParamsMap().forEach((key, val) -> {
                sb.append(key + "=" + val).append("&");
            });
            uri = "?" + sb.toString().substring(0, sb.toString().length() - 1);
        }
        contentServer.setResourceId(Generator.createKey(serverId, uri));
        contentServer.setResourceMd5(request.getMd5());
        contentServer.setResourceName(request.getFilename());
        contentServer.setResourceSize(request.getTotalSize());
        contentServer.setResourceType(request.getObjectType().getCode());
        return contentServer;
    }

    @Override
    public void delete(MessageHandleRequest request) {
        LoginToken token = LoginToken.fromTokenString(request.getToken());
        if (null == token) {
            LOGGER.error("may be some internal error.token can convert or is empty.tokenString={}", request.getToken());
            return;
        }
        contentServerProvider.deleteByUidAndResourceId(token.getUserId(), request.getObjectId());
        LOGGER.warn("delete file record from database.file md5={},objectId={}", request.getMd5(), request.getObjectId());
    }

    @Override
    public void auth(MessageHandleRequest request) {
        LOGGER.info("handle auth method.request={}", request);

        LoginToken login = LoginToken.fromTokenString(request.getToken());
        if (!userService.isValidLoginToken(login)) {
            LOGGER.error("invalid login token.auth failed");
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_SESSION, "invlida login token");
        }
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

    private String lookupInvoke(LoginToken login, String md5) {
        LOGGER.info("handle lookup message uid={},uniqueId={}", login.getUserId(), md5);
        ContentServerResource resource = contentServerProvider.findByMD5(md5);
        if (resource == null) {
            LOGGER.error("cannot find resource information");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "cannot find file");
        }
        return resource.getResourceMd5();

    }

    private String deleteInvoke(LoginToken login, String uniqueId) {
        ContentServerResource resource = contentServerProvider.findByMD5(uniqueId);
        if (resource == null) {
            LOGGER.error("can not find file information");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find the file");
        }
        if (login.getUserId() != resource.getOwnerId()) {
            LOGGER.error("cannot delete file.current user is not own.uid={},own={}", login.getUserId(),
                    resource.getOwnerId());
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_USER, "invalid owner or user");
        }
        LOGGER.error("the usr cannot delete this file.objectId={}", uniqueId);
        return resource.getResourceMd5();
    }

    private String uploadInvoke(LoginToken login, String uniqueId) {
        if (login.getUserId() == 0) {
            LOGGER.info("can not access.userId is empty");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "user is invalid.userId=" + login.getUserId());
        }
        return null;
    }

}
