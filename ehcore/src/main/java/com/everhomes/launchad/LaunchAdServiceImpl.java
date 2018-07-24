// @formatter:off
package com.everhomes.launchad;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerProvider;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contentserver.Generator;
import com.everhomes.entity.EntityType;
import com.everhomes.rest.banner.BannerServiceErrorCode;
import com.everhomes.rest.banner.BannerTargetType;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.launchad.CreateOrUpdateLaunchAdCommand;
import com.everhomes.rest.launchad.GetLaunchAdCommand;
import com.everhomes.rest.launchad.LaunchAdDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Autowired
    private ContentServerProvider contentServerProvider;

    @Override
    public LaunchAdDTO getLaunchAd(GetLaunchAdCommand cmd) {
        LaunchAd launchAd = launchAdProvider.findByNamespaceId(cmd.getNamespaceId());
        if (launchAd != null) {
            return this.toLaunchAdDTO(launchAd);
        }
        return new LaunchAdDTO();
    }

    /*private LaunchAdDTO toClientLaunchAdDTO(LaunchAd launchAd) {
        LaunchAdDTO launchAdDTO = this.toLaunchAdDTO(launchAd);
        // 分钟化秒
        launchAdDTO.setDisplayInterval(launchAd.getDisplayInterval() != null ? launchAd.getDisplayInterval() * 60 : 0);
        return launchAdDTO;
    }*/

    @Override
    public LaunchAdDTO createOrUpdateLaunchAd(CreateOrUpdateLaunchAdCommand cmd) {
        LaunchAd launchAd = launchAdProvider.findById(cmd.getId());
        if (launchAd != null) {
            updateLaunchAd(cmd, launchAd);
        } else {
            launchAd = createLaunchAd(cmd);
        }
        return toLaunchAdDTO(launchAd);
    }

    private void updateLaunchAd(CreateOrUpdateLaunchAdCommand cmd, LaunchAd launchAd) {
        launchAd.setContentType(cmd.getContentType());
        launchAd.setDisplayInterval(cmd.getDisplayInterval());
        launchAd.setDurationTime(cmd.getDurationTime());
        launchAd.setSkipFlag(cmd.getSkipFlag());
        launchAd.setStatus(cmd.getStatus());
        launchAd.setTimesPerDay(cmd.getTimesPerDay());
        launchAd.setTargetType(cmd.getTargetType());
        launchAd.setTargetData(cmd.getTargetData());
        launchAd.setResourceName(cmd.getResourceName());

        // 原来的和现在的不同才去修改
        if (!Objects.equals(launchAd.getContentUriOrigin(), cmd.getContentUriOrigin())) {
            launchAd.setContentUri(zipContent(cmd.getContentUriOrigin()));
            launchAd.setContentUriOrigin(cmd.getContentUriOrigin());
        }

        BannerTargetHandleResult result = getTargetHandleResult(cmd.getTargetType(), cmd.getTargetData());
        launchAd.setActionType(result.getActionType());
        launchAd.setActionData(result.getActionData());

        launchAdProvider.updateLaunchAd(launchAd);
    }

    private LaunchAd createLaunchAd(CreateOrUpdateLaunchAdCommand cmd) {
        LaunchAd launchAd = ConvertHelper.convert(cmd, LaunchAd.class);

        launchAd.setContentUri(zipContent(cmd.getContentUriOrigin()));

        BannerTargetHandleResult result = getTargetHandleResult(cmd.getTargetType(), cmd.getTargetData());
        launchAd.setActionType(result.getActionType());
        launchAd.setActionData(result.getActionData());

        launchAdProvider.createLaunchAd(launchAd);
        return launchAd;
    }

    private BannerTargetHandleResult getTargetHandleResult(String targetTypeStr, String targetDataStr) {
        BannerTargetType targetType = BannerTargetType.fromCode(targetTypeStr);
        if (targetType == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter targetType: %s.", targetDataStr);
        }

        BannerTargetHandler handler = PlatformContext.getComponent(
                BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + targetType.getCode());

        BannerTargetHandleResult result = null;
        try {
            result = handler.evaluate(targetDataStr);
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(e,
                    BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_TARGET_HANDLE_ERROR,
                    "Parse targetData error, targetData: %s.", targetDataStr);
        }
        return result;
    }

    private String zipContent(String contentUri) {
        try {
            String url = contentServerService.parserUri(contentUri);
            RestTemplate tpl = new RestTemplate();
            ByteArrayInputStream fileStream = tpl.execute(new URI(url), HttpMethod.GET, null, response -> {
                BufferedInputStream boi = new BufferedInputStream(response.getBody());

                String resId = contentUri.substring(contentUri.lastIndexOf("/") + 1, contentUri.length());
                ContentServerResource resource = contentServerProvider.findByResourceId(Generator.decodeUrl(resId));
                if (resource == null) {
                    throw new RuntimeException("Resource is null for resId = " + resId);
                }
                ByteArrayOutputStream byteData = new ByteArrayOutputStream();
                ZipOutputStream zipData = new ZipOutputStream(byteData);
                zipData.putNextEntry(new ZipEntry(resource.getResourceName()));

                int len;
                byte[] buf = new byte[4096];
                while ((len = boi.read(buf)) != -1) {
                    zipData.write(buf, 0, len);
                }
                zipData.closeEntry();
                zipData.finish();
                zipData.close();
                boi.close();
                byteData.close();
                return new ByteArrayInputStream(byteData.toByteArray());
            });

            String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
            String fileName = "LaunchAd_" + System.currentTimeMillis() + ".zip";
            UploadCsFileResponse csFileResponse = contentServerService.uploadFileToContentServer(fileStream, fileName, token);
            return csFileResponse.getResponse().getUri();
        } catch (Exception e) {
            LOGGER.error("Zip content error", e);
        }
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
        String contentUrl = this.parseUri(launchAd.getContentUri(), EntityType.NAMESPACE.getCode(), launchAd.getNamespaceId());
        String contentUrlOrigin = this.parseUri(launchAd.getContentUriOrigin(), EntityType.NAMESPACE.getCode(), launchAd.getNamespaceId());

        dto.setContentUrl(contentUrl);
        dto.setContentUrlOrigin(contentUrlOrigin);
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
}
