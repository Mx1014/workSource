package com.everhomes.core.sdk.user;

import com.everhomes.core.sdk.CoreSdkSettings;
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
import com.everhomes.tachikoma.commons.sdk.SdkClient;
import org.springframework.stereotype.Service;

@Service
public class SdkCommonService {

    private final SdkClient sdkClient = new SdkClient(new CoreSdkSettings());

    public void setDefaultCommunity(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        RestResponse response = sdkClient.restCall("post", "/evh/user/setUserDefaultCommunity", cmd, RestResponse.class);
    }

    public ListBorderAndContentResponse listAllBorderAccessPoints() {
        ListBorderAndContentRestResponse response = sdkClient.restCall("post", "/evh/user/listBorderAndContent", null, ListBorderAndContentRestResponse.class);
        ListBorderAndContentResponse res = response.getResponse();
        return res;
    }

    public void setDefaultCommunityForWx(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        RestResponse response = sdkClient.restCall("post", "/evh/user/setUserDefaultCommunityForWx", cmd, RestResponse.class);
    }

    public void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namecpaceId) {
        SetUserCurrentCommunityCommand cmd = new SetUserCurrentCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        cmd.setCommunityId(communityId);
        RestResponse response = sdkClient.restCall("post", "/evh/user/updateUserCurrentCommunityToProfile", cmd, RestResponse.class);
    }

    public void sendVerificationCodeSms(Integer namecpaceId, String phoneNumber, String code) {
        SendVerificationCodeCommand cmd = new SendVerificationCodeCommand();
        cmd.setNamespaceId(namecpaceId);
        cmd.setPhoneNumber(phoneNumber);
        cmd.setCode(code);
        RestResponse response = sdkClient.restCall("post", "/evh/user/sendVerificationCodeSms", cmd, RestResponse.class);
    }

    public void processUserForMember(Integer namecpaceId, String identifierToken, Long ownerId) {
        ProcessUserForMemberCommand cmd = new ProcessUserForMemberCommand();
        cmd.setNamespaceId(namecpaceId);
        cmd.setIdentifierToken(identifierToken);
        cmd.setOwnerId(ownerId);
        RestResponse response = sdkClient.restCall("post", "/evh/org/processUserForMember", cmd, RestResponse.class);
    }

    public CommunityDTO getCommunityById(Long id) {
        GetCommunityByIdCommand cmd = new GetCommunityByIdCommand();
        cmd.setId(id);
        GetCommunityByIdRestResponse response = sdkClient.restCall("post", "/evh/community/get", cmd, GetCommunityByIdRestResponse.class);
        return response.getResponse();
    }

    public CategoryDTO getCategoryById(Long id) {
        GetCategoryCommand cmd = new GetCategoryCommand();
        cmd.setId(id);
        GetCategoryRestResponse response = sdkClient.restCall("post", "/evh/category/getCategory", cmd, GetCategoryRestResponse.class);
        return (CategoryDTO) response.getResponse();
    }

    public RegionDTO getRegionById(Long id) {
        GetRegionCommand cmd = new GetRegionCommand();
        cmd.setId(id);
        GetRegionRestResponse response = sdkClient.restCall("post", "/evh/region/getRegion", cmd, GetRegionRestResponse.class);
        return (RegionDTO) response.getResponse();
    }

    public CsFileLocationDTO getUploadFile(String fileName, String url) {
        UploadFileCommand cmd = new UploadFileCommand();
        cmd.setFileName(fileName);
        cmd.setUrl(url);
        UploadFileByUrlRestResponse response = sdkClient.restCall("post", "/evh/contentServer/uploadFileByUrl", cmd, UploadFileByUrlRestResponse.class);
        return (CsFileLocationDTO) response.getResponse();
    }

    public String getFileUrl(String uri) {
        ParseURICommand cmd = new ParseURICommand();
        cmd.setUri(uri);
        RestResponse response = sdkClient.restCall("post", "/evh/contentServer/parseSharedUri", cmd, RestResponse.class);
        return (String) response.getResponseObject();
    }

    public AppDTO getApp(String appKey) {
        GetAppCommand cmd = new GetAppCommand();
        cmd.setRealAppKey(appKey);
        FindAppRestResponse response = sdkClient.restCall("post", "/evh/appkey/findApp", cmd, FindAppRestResponse.class);
        return (AppDTO) response.getResponse();
    }
}