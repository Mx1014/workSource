package com.everhomes.contentserver;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.user.LoginToken;
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
    private AclProvider aclProvider;

    @Override
    public void upload(MessageHandleRequest request) throws Exception {
        LOGGER.info("upload file and send response.request={}", request);
        LoginToken login = LoginToken.fromTokenString(request.getToken());
        if (null == login) {
            LOGGER.error("cannot find login information");
            throw new RuntimeErrorException("token message is invalid");
        }
        ContentServerResource result = contentServerProvider.findByUidAndMD5(login.getUserId(), request.getMd5());

        ContentServer server = contentServerService.selectContentServer(login.getUserId());
        if (server == null) {
            LOGGER.error("cannot find server.userId={}", login.getUserId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "cannot find server");
        }
        if (result == null) {
            result = createResource(login.getUserId(), request);
            contentServerProvider.addResource(result);
        }
        request.setObjectId(result.getResourceId());
    }

    private ContentServerResource createResource(Long uid, MessageHandleRequest request) {
        ContentServerResource contentServer = new ContentServerResource();
        contentServer.setMetadata(StringHelper.toJsonString(request.getMeta()));
        contentServer.setOwnerId(uid);
        contentServer.setResourceId(UUID.randomUUID().toString());
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
        if (null == login) {
            LOGGER.error("can not check the user information");
            throw RuntimeErrorException.errorWith("", 1, "invalid user");
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
            throw RuntimeErrorException.errorWith("", 1, "");
        }
        request.setMd5(md5);
    }

    private String lookupInvoke(LoginToken login, String objectId) {
        LOGGER.debug("handle lookup message uid={},uniqueId={}", login.getUserId(), objectId);
        ContentServerResource resource = contentServerProvider.findByResourceId(objectId);
        if (resource == null) {
            LOGGER.error("cannot find resource information");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "cannot find file");
        }
        if (!aclProvider.checkAccess("", login.getUserId(), "", resource.getOwnerId(), 0L, new ArrayList<Long>())) {
            LOGGER.error("invalid privillage");
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_PRIVILLAGE, "invalid privillage");
        }
        return resource.getResourceMd5();

    }

    private String deleteInvoke(LoginToken login, String uniqueId) {
        ContentServerResource resource = contentServerProvider.findByResourceId(uniqueId);
        if (resource == null) {
            LOGGER.error("can not find file information");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find the file");
        }
        if (login.getUserId() != resource.getOwnerId()) {
            LOGGER.error("cannot delete file.current user is not own.uid={},own={}", login.getUserId(),
                    resource.getOwnerId());
            throw RuntimeErrorException.errorWith("", 1, "");
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
