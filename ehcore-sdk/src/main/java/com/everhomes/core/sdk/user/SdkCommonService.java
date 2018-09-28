package com.everhomes.core.sdk.user;

import com.everhomes.core.sdk.NsDispatcher;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppDTO;
import com.everhomes.rest.app.FindAppRestResponse;
import com.everhomes.rest.app.GetAppCommand;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.category.GetCategoryCommand;
import com.everhomes.rest.category.GetCategoryRestResponse;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contentserver.ParseURICommand;
import com.everhomes.rest.contentserver.UploadFileByUrlRestResponse;
import com.everhomes.rest.contentserver.UploadFileCommand;
import com.everhomes.rest.enterprise.ProcessUserForMemberCommand;
import com.everhomes.rest.openapi.GetCommunityByIdRestResponse;
import com.everhomes.rest.region.GetRegionCommand;
import com.everhomes.rest.region.GetRegionRestResponse;
import com.everhomes.rest.region.RegionDTO;
import com.everhomes.rest.user.*;
import org.springframework.stereotype.Service;

@Service
public class SdkCommonService extends NsDispatcher {

    public void setDefaultCommunity(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        dispatcher(namecpaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/setUserDefaultCommunity", cmd, RestResponse.class);
        });
    }

    public ListBorderAndContentResponse listAllBorderAccessPoints(Integer namespaceId) {
        return dispatcher(namespaceId, sdkClient -> {
            ListBorderAndContentRestResponse response =
                    sdkClient.restCall("post", "/evh/user/listBorderAndContent", null, ListBorderAndContentRestResponse.class);
            return response.getResponse();
        });
    }

    public void setDefaultCommunityForWx(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);

        dispatcher(namecpaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/setUserDefaultCommunityForWx", cmd, RestResponse.class);
        });
    }

    public void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namecpaceId) {
        SetUserCurrentCommunityCommand cmd = new SetUserCurrentCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        cmd.setCommunityId(communityId);

        dispatcher(namecpaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/updateUserCurrentCommunityToProfile", cmd, RestResponse.class);
        });
    }

    public void sendVerificationCodeSms(Integer namecpaceId, String phoneNumber, String code) {
        SendVerificationCodeCommand cmd = new SendVerificationCodeCommand();
        cmd.setNamespaceId(namecpaceId);
        cmd.setPhoneNumber(phoneNumber);
        cmd.setCode(code);

        dispatcher(namecpaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/sendVerificationCodeSms", cmd, RestResponse.class);
        });
    }

    public void processUserForMember(Integer namecpaceId, String identifierToken, Long ownerId) {
        ProcessUserForMemberCommand cmd = new ProcessUserForMemberCommand();
        cmd.setNamespaceId(namecpaceId);
        cmd.setIdentifierToken(identifierToken);
        cmd.setOwnerId(ownerId);

        dispatcher(namecpaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/org/processUserForMember", cmd, RestResponse.class);
        });
    }

    public CommunityDTO getCommunityById(Integer namecpaceId, Long id) {
        GetCommunityByIdCommand cmd = new GetCommunityByIdCommand();
        cmd.setId(id);
        return dispatcher(namecpaceId, sdkClient -> {
            GetCommunityByIdRestResponse responseBase =
                    sdkClient.restCall("post", "/evh/community/get", cmd, GetCommunityByIdRestResponse.class);
            return responseBase.getResponse();
        });
    }

    public CategoryDTO getCategoryById(Integer namespaceId, Long id) {
        GetCategoryCommand cmd = new GetCategoryCommand();
        cmd.setId(id);

        return dispatcher(namespaceId, sdkClient -> {
            GetCategoryRestResponse response =
                    sdkClient.restCall("post", "/evh/category/getCategory", cmd, GetCategoryRestResponse.class);
            return response.getResponse();
        });
    }

    public RegionDTO getRegionById(Integer namespaceId, Long id) {
        GetRegionCommand cmd = new GetRegionCommand();
        cmd.setId(id);

        return dispatcher(namespaceId, sdkClient -> {
            GetRegionRestResponse response = sdkClient.restCall("post", "/evh/region/getRegion", cmd, GetRegionRestResponse.class);
            return (RegionDTO) response.getResponse();
        });
    }

    public CsFileLocationDTO getUploadFile(Integer namespaceId, String fileName, String url) {
        UploadFileCommand cmd = new UploadFileCommand();
        cmd.setFileName(fileName);
        cmd.setUrl(url);

        return dispatcher(namespaceId, sdkClient -> {
            UploadFileByUrlRestResponse response =
                    sdkClient.restCall("post", "/evh/contentServer/uploadFileByUrl", cmd, UploadFileByUrlRestResponse.class);
            return response.getResponse();
        });
    }

    public String getFileUrl(Integer namespaceId, String uri) {
        ParseURICommand cmd = new ParseURICommand();
        cmd.setUri(uri);
        return dispatcher(namespaceId, sdkClient -> {
            RestResponse response = sdkClient.restCall("post", "/evh/contentServer/parseSharedUri", cmd, RestResponse.class);
            return (String) response.getResponseObject();
        });
    }

    public AppDTO getApp(Integer namespaceId, String appKey) {
        GetAppCommand cmd = new GetAppCommand();
        cmd.setRealAppKey(appKey);
        return dispatcher(namespaceId, sdkClient -> {
            FindAppRestResponse response = sdkClient.restCall("post", "/evh/appkey/findApp", cmd, FindAppRestResponse.class);
            return response.getResponse();
        });
    }
}