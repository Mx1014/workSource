package com.everhomes.core.sdk.user;

import com.everhomes.core.sdk.CoreSdkSettings;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.app.AppDTO;
import com.everhomes.rest.app.GetAppCommand;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.category.GetCategoryCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.contentserver.ParseURICommand;
import com.everhomes.rest.contentserver.UploadFileCommand;
import com.everhomes.rest.enterprise.ProcessUserForMemberCommand;
import com.everhomes.rest.region.GetRegionCommand;
import com.everhomes.rest.region.RegionDTO;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.sdk.LogonInfoCommand;
import com.everhomes.rest.user.sdk.UserLogonInfo;
import com.everhomes.tachikoma.commons.sdk.SdkRestClient;
import org.springframework.stereotype.Service;

@Service
public class SdkCommonService {

    private final SdkRestClient sdkRestClient = SdkRestClient.getInstance(CoreSdkSettings.getInstance());

    public UserLogonInfo logonInfo() {
        LogonInfoCommand cmd = new LogonInfoCommand();
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/logonInfo", cmd, RestResponse.class);
        return new UserLogonInfo();
    }

    public void setDefaultCommunity(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/setUserDefaultCommunity", cmd, RestResponse.class);
    }

    public ListBorderAndContentResponse listAllBorderAccessPoints() {
        ListBorderAndContentRestResponse response = sdkRestClient.restCall("post", "/evh/user/listBorderAndContent", null, ListBorderAndContentRestResponse.class);
        ListBorderAndContentResponse res = response.getResponse();
        return res;
    }

    public void setDefaultCommunityForWx(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/setUserDefaultCommunityForWx", cmd, RestResponse.class);
    }

    public void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namecpaceId) {
        SetUserCurrentCommunityCommand cmd = new SetUserCurrentCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        cmd.setCommunityId(communityId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/updateUserCurrentCommunityToProfile", cmd, RestResponse.class);
    }

    public void sendVerificationCodeSms(Integer namecpaceId, String phoneNumber, String code) {
        SendVerificationCodeCommand cmd = new SendVerificationCodeCommand();
        cmd.setNamespaceId(namecpaceId);
        cmd.setPhoneNumber(phoneNumber);
        cmd.setCode(code);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/sendVerificationCodeSms", cmd, RestResponse.class);
    }

    public void processUserForMember(Integer namecpaceId, String identifierToken, Long ownerId) {
        ProcessUserForMemberCommand cmd = new ProcessUserForMemberCommand();
        cmd.setNamespaceId(namecpaceId);
        cmd.setIdentifierToken(identifierToken);
        cmd.setOwnerId(ownerId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/org/processUserForMember", cmd, RestResponse.class);
    }
    public RestResponse getCommunityById(Long id){
        GetCommunityByIdCommand cmd = new GetCommunityByIdCommand();
        cmd.setId(id);
        RestResponse response = sdkRestClient.restCall("post", "/evh/community/get", cmd, RestResponse.class);
        return response;
    }

    public CategoryDTO getCategoryById(Long id){
        GetCategoryCommand cmd = new GetCategoryCommand();
        cmd.setId(id);
        RestResponse response = sdkRestClient.restCall("post", "/evh/category/getCategory", cmd, RestResponse.class);
        return (CategoryDTO) response.getResponseObject();
    }

    public RegionDTO getRegionById(Long id){
        GetRegionCommand cmd = new GetRegionCommand();
        cmd.setId(id);
        RestResponse response = sdkRestClient.restCall("post", "/evh/region/getRegion", cmd, RestResponse.class);
        return (RegionDTO) response.getResponseObject();
    }

    public CsFileLocationDTO getUploadFile(String fileName, String url){
        UploadFileCommand cmd = new UploadFileCommand();
        cmd.setFileName(fileName);
        cmd.setUrl(url);
        RestResponse response = sdkRestClient.restCall("post", "/evh/contentServer/uploadFileByUrl", cmd, RestResponse.class);
        return (CsFileLocationDTO) response.getResponseObject();
    }

    public String getFileUrl(String uri){
        ParseURICommand cmd = new ParseURICommand();
        cmd.setUri(uri);
        RestResponse response = sdkRestClient.restCall("post", "/evh/contentServer/parseSharedUri", cmd, RestResponse.class);
        return (String) response.getResponseObject();
    }

    public AppDTO getApp(String appKey) {
        GetAppCommand cmd = new GetAppCommand();
        cmd.setAppKey(appKey);
        RestResponse response = sdkRestClient.restCall("post", "/evh/appkey/findApp", cmd, RestResponse.class);
        return (AppDTO) response.getResponseObject();
    }
}