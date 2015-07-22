// @formatter:off
package com.everhomes.qrcode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.Acl;
import com.everhomes.acl.AclAccessor;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.PrivilegeConstants;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.app.AppConstants;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumServiceErrorCode;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessageMetaConstant;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.MetaObjectType;
import com.everhomes.messaging.QuestionMetaObject;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.RegionDescriptor;
import com.everhomes.search.GroupQueryResult;
import com.everhomes.search.GroupSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IdToken;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.visibility.VisibilityScope;
import com.google.gson.Gson;

@Component
public class QRCodeServiceImpl implements QRCodeService {   
    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeServiceImpl.class);
    
    @Autowired
    private QRCodeProvider qrcodeProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    private static final String HOME_URL = "home.url";
     
    @Override
    public QRCodeDTO createQRCode(NewQRCodeCommand cmd) {
        User user = UserContext.current().getUser();
        
        QRCode qrcode = new QRCode();
        qrcode.setDescription(cmd.getDescription());
        qrcode.setExpireTime(cmd.getExpireTime());
        qrcode.setActionType(cmd.getActionType());
        qrcode.setActionData(cmd.getActionData());
        qrcode.setStatus(QRCodeStatus.ACTIVE.getCode());
        qrcode.setViewCount(0L);
        qrcode.setCreatorUid(user.getId());
        
        qrcodeProvider.createQRCode(qrcode);
        
        return toQRCodeDTO(qrcode);
    }
    
    @Override
    public QRCodeDTO getQRCodeInfo(GetQRCodeInfoCommand cmd) {
        return getQRCodeInfoById(cmd.getQrid());
    }
    
    @Override
    public QRCodeDTO getQRCodeInfoById(String qrid) {
        User operator = UserContext.current().getUser();
        Long operatorId = -1L;
        if(operator != null) {
            operatorId = operator.getId();
        }
        
        if(qrid == null) {
            LOGGER.error("QR code id is null, operatorId=" + operatorId + ", qrid=" + qrid);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid QR code id");
        }
        
        try {
            IdToken token = WebTokenGenerator.getInstance().fromWebToken(qrid, IdToken.class);
            long qrcodeId = token.getId();
            QRCode qrcode = qrcodeProvider.findQRCodeById(qrcodeId);
            
            return toQRCodeDTO(qrcode);
        } catch(Exception e) {
            LOGGER.error("QR code id is invalid format, operatorId=" + operatorId + ", qrid=" + qrid);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid QR code id");
        }
    }
    
    private QRCodeDTO toQRCodeDTO(QRCode qrcode) {
        QRCodeDTO qrcodeDto = ConvertHelper.convert(qrcode, QRCodeDTO.class);
        
        // TODO: set url
        String qrid = WebTokenGenerator.getInstance().toWebToken(qrcode.getId());
        qrcodeDto.setQrid(qrid);
        
        String url = configProvider.getValue(HOME_URL, "");
        if(!url.endsWith("/")) {
            url += "/";
        }
        url += "qr?qrid=" + qrid;
        qrcodeDto.setUrl(url);
        
        return qrcodeDto;
    }
}
