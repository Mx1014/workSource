// @formatter:off
package com.everhomes.qrcode;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.activity.ActivityQRCodeDTO;
import com.everhomes.rest.qrcode.GetQRCodeInfoCommand;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.qrcode.QRCodeStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.sdk.SdkQRCodeService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        com.everhomes.rest.user.qrcode.GetQRCodeInfoCommand command = ConvertHelper.convert(cmd, com.everhomes.rest.user.qrcode.GetQRCodeInfoCommand.class);
        return ConvertHelper.convert(sdkDelegate.getQRCodeInfo(command), QRCodeDTO.class);
    }

    @Override
    public QRCodeDTO getQRCodeInfoById(String qrid, String source) {
        GetQRCodeInfoCommand cmd = new GetQRCodeInfoCommand();
        cmd.setQrid(qrid);
        cmd.setSource(source);
        return this.getQRCodeInfo(cmd);
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
        url += "activity/build/index.html#/checkIn?authFlag=0&forumId=%s&topicId=%s&activityId=%s&namespaceId=%s&wechatSignup=%s&categoryId=%s#sign_suffix";
        url = String.format(url, activityQRCodeDTO.getForumId(), activityQRCodeDTO.getTopicId(), activityQRCodeDTO.getActivityId(),
                UserContext.getCurrentNamespaceId(), activityQRCodeDTO.getWechatSignup(),activityQRCodeDTO.getCategoryId());
        qrcodeDto.setUrl(url);

        return qrcodeDto;
    }
}
