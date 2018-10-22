// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.activity.ActivityQRCodeDTO;
import com.everhomes.rest.qrcode.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.user.sdk.SdkQRCodeService;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 已经废弃，请使用 {@link com.everhomes.user.sdk.SdkQRCodeService}
 */
@Deprecated
@Component
public class QRCodeServiceImpl implements QRCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeServiceImpl.class);

    @Autowired
    private SdkQRCodeService sdkDelegate;

    @Autowired
    private QRCodeListenerManager qrCodeListenerManager;

    @Autowired
    private QRCodeProvider qrcodeProvider;

    @Autowired
    private ConfigurationProvider configProvider;
    @Override
    public QRCodeDTO createQRCode(NewQRCodeCommand cmd) {
        com.everhomes.rest.user.qrcode.NewQRCodeCommand command = ConvertHelper.convert(cmd, com.everhomes.rest.user.qrcode.NewQRCodeCommand.class);
        return ConvertHelper.convert(sdkDelegate.createQRCode(command), QRCodeDTO.class);
    }


    @Override
    public QRCodeDTO createQRCodeForActivity(NewQRCodeCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();

        QRCode qrcode = new QRCode();
        qrcode.setDescription(cmd.getDescription());
        qrcode.setRouteUri(cmd.getRouteUri());
        qrcode.setHandler(cmd.getHandler());
        qrcode.setActionType(cmd.getActionType());
        qrcode.setActionData(cmd.getActionData());
        qrcode.setStatus(QRCodeStatus.ACTIVE.getCode());
        qrcode.setViewCount(0L);
        qrcode.setCreatorUid(operatorId);
        qrcode.setExtra(cmd.getExtra());

        Long expireSeconds = cmd.getExpireSeconds();
        if(expireSeconds != null) {
            qrcode.setExpireTime(new Timestamp(DateHelper.currentGMTTime().getTime() + expireSeconds * 1000));
        }

        qrCodeListenerManager.onQRCodeCreating(cmd.getHandler(), qrcode);
        qrcodeProvider.createQRCode(qrcode);
        qrCodeListenerManager.onQRCodeCreated(cmd.getHandler(), qrcode);

        return toQRCodeDTOForActivity(qrcode);
    }

    @Override
    public QRCodeDTO getQRCodeInfo(GetQRCodeInfoCommand cmd) {
        return getQRCodeInfoById(cmd.getQrid(), cmd.getSource());
    }

    @Override
    public QRCodeDTO getQRCodeInfoById(String qrid, String source) {
        GetQRCodeInfoCommand cmd = new GetQRCodeInfoCommand();
        cmd.setQrid(qrid);
        cmd.setSource(source);
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
            if (qrcode.getExpireTime() != null) {
                Timestamp current = new Timestamp(DateHelper.currentGMTTime().getTime());
                if (qrcode.getExpireTime().before(current)) {
                    LOGGER.error("QR code expired, operatorId=" + operatorId + ", qrid=" + qrid
                            + ", current=" + current.getTime() + ", expiredTime=" + qrcode.getExpireTime().getTime());
                    throw RuntimeErrorException.errorWith(QRCodeServiceErrorCode.SCOPE,
                            QRCodeServiceErrorCode.ERROR_QR_CODE_EXPIRED, "QR code expired");
                }
            }

            QRCodeDTO qrCodeDTO = toQRCodeDTO(qrcode);
            qrCodeListenerManager.onGetQRCodeInfo(qrCodeDTO, source);
            return qrCodeDTO;
        } catch (RuntimeErrorException ree) {
            throw ree;
        } catch (Exception e) {
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


    private QRCodeDTO toQRCodeDTOForActivity(QRCode qrcode) {
        QRCodeDTO qrcodeDto = ConvertHelper.convert(qrcode, QRCodeDTO.class);
        String url = configProvider.getValue(ConfigConstants.HOME_URL, "");
        if(!url.endsWith("/")) {
            url += "/";
        }
        ActivityQRCodeDTO activityQRCodeDTO = new ActivityQRCodeDTO();
        if (!StringUtils.isBlank(qrcodeDto.getActionData())) {
            activityQRCodeDTO = (ActivityQRCodeDTO)StringHelper.fromJsonString(qrcodeDto.getActionData(), ActivityQRCodeDTO.class);
        }
        url += "activity/build/index.html#/checkIn?forumId=%s&topicId=%s&activityId=%s&namespaceId=%s&wechatSignup=%s&categoryId=%s";
        url = String.format(url, activityQRCodeDTO.getForumId(), activityQRCodeDTO.getTopicId(), activityQRCodeDTO.getActivityId(),
                UserContext.getCurrentNamespaceId(), activityQRCodeDTO.getWechatSignup(),activityQRCodeDTO.getCategoryId());
        qrcodeDto.setUrl(url);

        return qrcodeDto;
    }
}
