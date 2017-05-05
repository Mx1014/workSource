// @formatter:off
package com.everhomes.qrcode;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeServiceErrorCode;
import com.everhomes.rest.qrcode.QRCodeStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IdToken;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;

@Component
public class QRCodeServiceImpl implements QRCodeService {   
    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeServiceImpl.class);
    
    @Autowired
    private QRCodeProvider qrcodeProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
     
    @Override
    public QRCodeDTO createQRCode(NewQRCodeCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        QRCode qrcode = new QRCode();
        qrcode.setDescription(cmd.getDescription());
        Long expireSeconds = cmd.getExpireSeconds();
        if(expireSeconds != null) {
            qrcode.setExpireTime(new Timestamp(DateHelper.currentGMTTime().getTime() + expireSeconds * 1000));
        }
        ActionType actionType = ActionType.fromCode(cmd.getActionType());
        if(actionType == null) {
            LOGGER.error("Invalid build type, operatorId=" + operatorId + ", actionType=" + cmd.getActionType());
            throw RuntimeErrorException.errorWith(QRCodeServiceErrorCode.SCOPE, 
                QRCodeServiceErrorCode.ERROR_ACTION_TYPE_INVALID, "Action type invalid");
        }
        qrcode.setActionType(cmd.getActionType());
        qrcode.setActionData(cmd.getActionData());
        qrcode.setStatus(QRCodeStatus.ACTIVE.getCode());
        qrcode.setViewCount(0L);
        qrcode.setCreatorUid(operatorId);
        
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
            if(qrcode.getExpireTime() != null) {
                Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
                if(qrcode.getExpireTime().before(current)) {
                    LOGGER.error("QR code expired, operatorId=" + operatorId + ", qrid=" + qrid 
                        + ", current=" + current.getTime() + ", expiredTime=" + qrcode.getExpireTime().getTime());
                    throw RuntimeErrorException.errorWith(QRCodeServiceErrorCode.SCOPE, 
                        QRCodeServiceErrorCode.ERROR_QR_CODE_EXPIRED, "QR code expired");
                }
            }
            
            return toQRCodeDTO(qrcode);
        } catch(Exception e) {
            LOGGER.error("QR code id is invalid format, operatorId=" + operatorId + ", qrid=" + qrid);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid QR code id");
        }
    }
    
    private QRCodeDTO toQRCodeDTO(QRCode qrcode) {
        QRCodeDTO qrcodeDto = ConvertHelper.convert(qrcode, QRCodeDTO.class);
        
        IdToken idToken = new IdToken(qrcode.getId());
        String qrid = WebTokenGenerator.getInstance().toWebToken(idToken);
        qrcodeDto.setQrid(qrid);
        IdToken token = WebTokenGenerator.getInstance().fromWebToken(qrid, IdToken.class);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("id=" + qrcode.getId() + ", qrid=" + qrid + ", decodeId=" + token.getId());
        }
        
        String url = configProvider.getValue(ConfigConstants.HOME_URL, "");
        if(!url.endsWith("/")) {
            url += "/";
        }
        url += "qr?qrid=" + qrid;
        qrcodeDto.setUrl(url);
        
        return qrcodeDto;
    }
}
