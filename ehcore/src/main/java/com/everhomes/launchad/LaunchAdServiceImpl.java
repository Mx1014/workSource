// @formatter:off
package com.everhomes.launchad;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.rest.launchad.SetLaunchAdCommand;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by xq.tian on 2016/12/9.
 */
@Service
public class LaunchAdServiceImpl implements LaunchAdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchAdServiceImpl.class);

    @Autowired
    private LaunchAdProvider launchAdProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Override
    public LaunchAdDTO getLaunchAd() {
        LaunchAd launchAd = launchAdProvider.getLaunchAd(currNamespaceId());
        if (launchAd != null) {
            return this.toLaunchAdDTO(launchAd);
        }
        return new LaunchAdDTO();
    }

    @Override
    public LaunchAdDTO setLaunchAd(SetLaunchAdCommand cmd) {
        LaunchAd launchAd = launchAdProvider.getLaunchAd(cmd.getNamespaceId());
        if (launchAd != null) {
            if (cmd.getActionData() != null) {
                launchAd.setActionData(cmd.getActionData());
            }
            if (cmd.getActionType() != null) {
                launchAd.setActionType(cmd.getActionType());
            }
            if (cmd.getContentType() != null) {
                launchAd.setContentType(cmd.getContentType());
            }
            if (cmd.getContentUri() != null) {
                launchAd.setContentUri(cmd.getContentUri());
            }
            if (cmd.getDisplayInterval() != null) {
                launchAd.setDisplayInterval(cmd.getDisplayInterval());
            }
            if (cmd.getDurationTime() != null) {
                launchAd.setDurationTime(cmd.getDurationTime());
            }
            if (cmd.getSkipFlag() != null) {
                launchAd.setSkipFlag(cmd.getSkipFlag());
            }
            if (cmd.getStatus() != null) {
                launchAd.setStatus(cmd.getStatus());
            }
            if (cmd.getTimesPerDay() != null) {
                launchAd.setTimesPerDay(cmd.getTimesPerDay());
            }
            launchAdProvider.updateLaunchAd(launchAd);
        } else {
            launchAd = ConvertHelper.convert(cmd, LaunchAd.class);
            launchAdProvider.createLaunchAd(launchAd);
        }
        return toLaunchAdDTO(launchAd);
    }

    @Override
    public UploadCsFileResponse uploadLaunchAdFile(MultipartFile[] files) {
        List<UploadCsFileResponse> uploadCsFileResponses = contentServerService.uploadFileToContentServer(files);
        if (uploadCsFileResponses != null && uploadCsFileResponses.size() > 0) {
            return uploadCsFileResponses.get(0);
        }
        return new UploadCsFileResponse();
    }

    private LaunchAdDTO toLaunchAdDTO(LaunchAd launchAd) {
        LaunchAdDTO dto = ConvertHelper.convert(launchAd, LaunchAdDTO.class);
        String contentUrl = this.parseUri(launchAd.getContentUri(), EntityType.NAMESPACE.getCode(), currNamespaceId());
        dto.setContentUrl(contentUrl);
        return dto;
    }

    private String parseUri(String uri, String ownerType, long ownerId) {
        try {
            if (StringUtils.isNotEmpty(uri))
                return contentServerService.parserUri(uri, ownerType, ownerId);
        } catch (Exception e) {
            LOGGER.error("Parse launch ad uri error {}", e);
        }
        return null;
    }

    private Integer currNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }
}
