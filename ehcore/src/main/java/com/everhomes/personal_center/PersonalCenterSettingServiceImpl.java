// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.personal_center.CreatePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.CreatePersonalSettingCommand;
import com.everhomes.rest.personal_center.CreateUserEmailCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListActivePersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsCommand;
import com.everhomes.rest.personal_center.ListPersonalCenterSettingsResponse;
import com.everhomes.rest.personal_center.ListUserOrganizationCommand;
import com.everhomes.rest.personal_center.ListUserOrganizationResponse;
import com.everhomes.rest.personal_center.ListVersionListCommand;
import com.everhomes.rest.personal_center.ListVersionListResponse;
import com.everhomes.rest.personal_center.PersonalCenterSettingDTO;
import com.everhomes.rest.personal_center.PersonalCenterSettingStatus;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.personal_center.UpdateUserCompanyCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class PersonalCenterSettingServiceImpl implements PersonalCenterService{

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private PersonalCenterSettingProvider personalCenterSettingProvider;
    @Override
    public void createUserEmail(CreateUserEmailCommand cmd) {
        Long userId = UserContext.currentUserId();
        UserIdentifier userIdentifier = new UserIdentifier();
        userIdentifier.setOwnerUid(userId);
        userIdentifier.setIdentifierToken(cmd.getEmail());
        userIdentifier.setIdentifierType(IdentifierType.EMAIL.getCode());
        userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
        userIdentifier.setNamespaceId(UserContext.getCurrentNamespaceId());
        userIdentifier.setCreateTime(new Timestamp(new Date().getTime()));
        userIdentifier.setNotifyTime(new Timestamp(new Date().getTime()));
        this.userProvider.createIdentifier(userIdentifier);
    }

    @Override
    public UserInfo updateShowCompanyFlag(UpdateShowCompanyCommand cmd) {
        User user = UserContext.current().getUser();
        user.setShowCompanyFlag(cmd.getShowCompanyFlag());
        userProvider.updateUser(user);
        return userService.getUserInfo(user.getId());
    }

    @Override
    public UserInfo updateUserCompany(UpdateUserCompanyCommand cmd) {
        User user = UserContext.current().getUser();
        user.setCompany(cmd.getCompanyName());
        userProvider.updateUser(user);
        return userService.getUserInfo(user.getId());
    }

    @Override
    public ListUserOrganizationResponse listUserOrganization(ListUserOrganizationCommand cmd) {
        ListUserOrganizationResponse response = new ListUserOrganizationResponse();
        List<OrganizationDTO> dtoList = this.organizationService.listUserRelateOrganizations(cmd.getNamespaceId(),UserContext.currentUserId(), OrganizationGroupType.ENTERPRISE);
        response.setDtos(dtoList);
        return response;
    }

    @Override
    public ListActivePersonalCenterSettingsResponse listActivePersonalCenterSettings(ListActivePersonalCenterSettingsCommand cmd) {
        ListActivePersonalCenterSettingsResponse response = new ListActivePersonalCenterSettingsResponse();
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryActivePersonalCenterSettings(cmd.getNamespaceId());
        if (CollectionUtils.isEmpty(list)) {
            list = this.personalCenterSettingProvider.queryDefaultPersonalCenterSettings();
        }
        List<PersonalCenterSettingDTO> dtoList = new ArrayList<>();
        list.stream().forEach((r) -> {
             PersonalCenterSettingDTO dto = ConvertHelper.convert(r,PersonalCenterSettingDTO.class);
             dto.setIconUrl(this.parseUrl(dto.getIconUri(),cmd.getNamespaceId()));
             dtoList.add(dto);
        });
        return response;
    }

    @Override
    public ListPersonalCenterSettingsResponse listPersonalCenterSettingsByNamespaceIdAndVersion(ListPersonalCenterSettingsCommand cmd) {
        ListPersonalCenterSettingsResponse response = new ListPersonalCenterSettingsResponse();
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryPersonalCenterSettingsByNamespaceIdAndVersion(cmd.getNamespaceId(), cmd.getVersion());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<PersonalCenterSettingDTO> dtoList = new ArrayList<>();
        list.stream().forEach((r) -> {
            PersonalCenterSettingDTO dto = ConvertHelper.convert(r,PersonalCenterSettingDTO.class);
            dto.setIconUrl(this.parseUrl(dto.getIconUri(),cmd.getNamespaceId()));
            dtoList.add(dto);
        });
        return response;
    }

    @Override
    public CreatePersonalCenterSettingsResponse createPersonalCenterSettings(CreatePersonalSettingCommand cmd) {
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryActivePersonalCenterSettings(cmd.getNamespaceId());
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(r -> {
                r.setStatus(PersonalCenterSettingStatus.SAVING.getCode());
                r.setUpdateTime(new Timestamp(new Date().getTime()));
                r.setUpdateUid(UserContext.currentUserId());
                this.personalCenterSettingProvider.updatePersonalCenterSetting(r);
            });
        }
        List<PersonalCenterSettingDTO> dtoList = cmd.getSettings();
        List<PersonalCenterSettingDTO> returnDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)) {
            Integer version = getVersion(cmd.getNamespaceId());
            dtoList.stream().forEach(r -> {
                PersonalCenterSetting personalCenterSetting = ConvertHelper.convert(r, PersonalCenterSetting.class);
                personalCenterSetting.setStatus(PersonalCenterSettingStatus.ACTIVE.getCode());
                personalCenterSetting.setVersion(version);
                personalCenterSetting.setCreateTime(new Timestamp(new Date().getTime()));
                personalCenterSetting.setCreateUid(UserContext.currentUserId());
                this.personalCenterSettingProvider.createPersonalCenterSetting(personalCenterSetting);
                PersonalCenterSettingDTO returnDto = ConvertHelper.convert(personalCenterSetting, PersonalCenterSettingDTO.class);
                returnDto.setIconUrl(parseUrl(returnDto.getIconUri(),cmd.getNamespaceId()));
                returnDtoList.add(returnDto);
            });
        }
        CreatePersonalCenterSettingsResponse response = new CreatePersonalCenterSettingsResponse();
        response.setDtos(returnDtoList);
        return response;
    }

    @Override
    public ListVersionListResponse listVersion(ListVersionListCommand cmd) {
        ListVersionListResponse response = new ListVersionListResponse();
        response.setVersionList(this.personalCenterSettingProvider.getVersionList(cmd.getNamespaceId()));
        return response;
    }

    private Date getTime(int hour,int minute,int second){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);

        return calendar.getTime();
    }

    private String parseUrl(String uri, Integer namespaceId) {
       return this.contentServerService.parserUri(uri, EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
    }
    /**
     * 生成版本号
     * 如:2018010101
     * @param namespaceId
     * @return
     */
    private Integer getVersion(Integer namespaceId){
        Calendar calendar = Calendar.getInstance();
        Integer version = calendar.get(Calendar.YEAR) * 100000 + (calendar.get(Calendar.MONTH) + 1) * 1000 + calendar.get(Calendar.DATE) * 10;
        Timestamp dayStart = new Timestamp(getTime(0,0,0).getTime());
        Timestamp dayEnd = new Timestamp(getTime(23,59,59).getTime());
        int count = this.personalCenterSettingProvider.countPersonalCenterSettingVersion(namespaceId, dayStart, dayEnd);
        return version+count+1;
    }
}
