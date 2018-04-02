// @formatter:off
package com.everhomes.launchad;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.CreateOrUpdateLaunchAdCommand;
import com.everhomes.rest.launchad.GetLaunchAdCommand;
import com.everhomes.rest.launchad.LaunchAdDTO;
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
    public LaunchAdDTO getLaunchAd(GetLaunchAdCommand cmd) {
        LaunchAd launchAd = launchAdProvider.findByNamespaceId(currNamespaceId());
        if (launchAd != null) {
            return this.toLaunchAdDTO(launchAd);
        }
        return new LaunchAdDTO();
    }

    @Override
    public LaunchAdDTO createOrUpdateLaunchAd(CreateOrUpdateLaunchAdCommand cmd) {
        LaunchAd launchAd = launchAdProvider.findById(cmd.getId());
        if (launchAd != null) {
            launchAd.setContentType(cmd.getContentType());
            launchAd.setContentUri(zipContent(cmd.getContentUriOrigin()));
            launchAd.setDisplayInterval(cmd.getDisplayInterval());
            launchAd.setDurationTime(cmd.getDurationTime());
            launchAd.setSkipFlag(cmd.getSkipFlag());
            launchAd.setStatus(cmd.getStatus());
            launchAd.setTimesPerDay(cmd.getTimesPerDay());

            launchAdProvider.updateLaunchAd(launchAd);
        } else {
            launchAd = ConvertHelper.convert(cmd, LaunchAd.class);
            launchAdProvider.createLaunchAd(launchAd);
        }
        return toLaunchAdDTO(launchAd);
    }

    private String zipContent(String contentUri) {
        String url = contentServerService.parserUri(contentUri);

        return null;
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
