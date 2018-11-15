// @formatter:off
package com.everhomes.personal_center;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationService;
import com.everhomes.point.PointService;
import com.everhomes.rest.common.TrueOrFalseFlag;
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
import com.everhomes.rest.personal_center.PersonalCenterSettingRegionType;
import com.everhomes.rest.personal_center.PersonalCenterSettingStatus;
import com.everhomes.rest.personal_center.PersonalCenterSettingType;
import com.everhomes.rest.personal_center.ShopMallId;
import com.everhomes.rest.personal_center.ShowPrivateSettingCommand;
import com.everhomes.rest.personal_center.ShowPrivateSettingResponse;
import com.everhomes.rest.personal_center.UpdateShowCompanyCommand;
import com.everhomes.rest.personal_center.UpdateUserCompanyCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserTreasureDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProfile;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class PersonalCenterSettingServiceImpl implements PersonalCenterService{
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalCenterSettingServiceImpl.class);

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
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;
    @Autowired
    private PointService pointService;
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
        user.setCompanyId(cmd.getCompanyId());
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
        List<PersonalCenterSettingDTO> basicDtos = new ArrayList<>();
        List<PersonalCenterSettingDTO> blockDtos = new ArrayList<>();
        List<PersonalCenterSettingDTO> listDtos = new ArrayList<>();
        list.stream().forEach((r) -> {
             PersonalCenterSettingDTO dto = ConvertHelper.convert(r,PersonalCenterSettingDTO.class);
             dto.setIconUrl(this.parseUrl(dto.getIconUri(),cmd.getNamespaceId()));
             if (PersonalCenterSettingType.WALLET.getCode().equals(r.getType())) {
                 String homeUrl = this.configurationProvider.getValue(0,"personal.wallet.home.url","https://payv2.zuolin.com");
                 dto.setLinkUrl(homeUrl+dto.getLinkUrl());
             }
            if (PersonalCenterSettingType.ORDER.getCode().equals(r.getType())) {
                String homeUrl = this.configurationProvider.getValue(0,"personal.order.home.url","https://biz.zuolin.com");
                dto.setLinkUrl(homeUrl+dto.getLinkUrl());
            }
            //为了不影响个人中心，将获取积分的数据从此处移除，放到获取用户财富中。
//            if (PersonalCenterSettingType.POINT.getCode().equals(r.getType())) {
//                UserTreasureDTO point = null;
//                 try {
//                     point = pointService.getPointTreasure();
//                 }catch (Exception e) {
//                     LOGGER.error("get point exception");
//                     e.printStackTrace();
//                 }
//                if (point != null && TrueOrFalseFlag.TRUE.getCode().equals(point.getStatus()) && TrueOrFalseFlag.TRUE.getCode().equals(point.getUrlStatus())) {
//                    String homeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.HOME_URL, "");
//                    dto.setLinkUrl(homeUrl + String.format(dto.getLinkUrl(), 1));
//                }
//            }
            if (PersonalCenterSettingType.MY_SHOP.getCode().equals(r.getType())) {
//                UserProfile applied = userActivityProvider.findUserProfileBySpecialKey(user.getId(),
//                        UserProfileContstant.IS_APPLIED_SHOP);
//                dto.setLinkUrl(getApplyShopUrl());
//                if (applied != null) {
//                    if (NumberUtils.toInt(applied.getItemValue(), 0) != 0)
//                }
                dto.setLinkUrl(getManageShopUrl());

            }
             switch (dto.getRegion()) {
                 case 0 :
                     basicDtos.add(dto);
                     break;
                 case 1 :
                     blockDtos.add(dto);
                     break;
                 case 2 :
                     listDtos.add(dto);
                     break;
                 default:
                     break;
             }
        });
        response.setBasicDtos(basicDtos);
        response.setBlockDtos(blockDtos);
        response.setListDtos(listDtos);
        return response;
    }

    private String getApplyShopUrl() {
        String homeurl = configurationProvider.getValue(ConfigConstants.PREFIX_URL, "");
        String applyShopPath = configurationProvider.getValue(ConfigConstants.APPLY_SHOP_URL, "");
        if(homeurl.length() == 0 || applyShopPath.length() == 0) {
            LOGGER.error("Invalid home url or apply path, homeUrl=" + homeurl + ", applyShopPath=" + applyShopPath);
            return null;
        } else {
            ShopMallId mallId = ShopMallId.fromNamespaceId(UserContext.getCurrentNamespaceId());
            if (mallId != null) {
                applyShopPath = applyShopPath.replace("?","&");
                return homeurl +"?mallId=" +mallId.getCode() + applyShopPath;
            }else {
                return homeurl + applyShopPath;
            }
        }
    }

    private String getManageShopUrl() {
        String homeurl = configurationProvider.getValue(ConfigConstants.PREFIX_URL, "");
        String manageShopPath = configurationProvider.getValue(ConfigConstants.MANAGE_SHOP_URL, "");

        if(homeurl.length() == 0 || manageShopPath.length() == 0) {
            LOGGER.error("Invalid home url or manage path, homeUrl=" + homeurl + ", manageShopPath=" + manageShopPath);
            return null;
        } else {
            //不需要拼接mallId  update by yanlong.liang 20181017
//            ShopMallId mallId = ShopMallId.fromNamespaceId(UserContext.getCurrentNamespaceId());
//            if (mallId != null) {
//                manageShopPath = manageShopPath.replace("?","&");
//                if (homeurl.contains("?")) {
//                    return homeurl +"&mallId=" +mallId.getCode() + manageShopPath;
//                }else {
//                    return homeurl +"?mallId=" +mallId.getCode() + manageShopPath;
//                }
//            }else {
//            }
            return homeurl + manageShopPath;
        }
    }

    @Override
    public ListPersonalCenterSettingsResponse listPersonalCenterSettingsByNamespaceIdAndVersion(ListPersonalCenterSettingsCommand cmd) {
        ListPersonalCenterSettingsResponse response = new ListPersonalCenterSettingsResponse();
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryPersonalCenterSettingsByNamespaceIdAndVersion(cmd.getNamespaceId(), cmd.getVersion());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<PersonalCenterSettingDTO> basicDtos = new ArrayList<>();
        List<PersonalCenterSettingDTO> blockDtos = new ArrayList<>();
        List<PersonalCenterSettingDTO> listDtos = new ArrayList<>();
        list.stream().forEach((r) -> {
            PersonalCenterSettingDTO dto = ConvertHelper.convert(r,PersonalCenterSettingDTO.class);
            dto.setIconUrl(this.parseUrl(dto.getIconUri(),cmd.getNamespaceId()));
            switch (dto.getRegion()) {
                case 0 :
                    basicDtos.add(dto);
                    break;
                case 1 :
                    blockDtos.add(dto);
                    break;
                case 2 :
                    listDtos.add(dto);
                    break;
                default:
                    break;
            }
        });
        response.setBasicDtos(basicDtos);
        response.setBlockDtos(blockDtos);
        response.setListDtos(listDtos);
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
        List<PersonalCenterSettingDTO> dtoList = new ArrayList<>();
        dtoList.addAll(cmd.getBasicDtos());
        dtoList.addAll(cmd.getBlockDtos());
        dtoList.addAll(cmd.getListDtos());
        List<PersonalCenterSettingDTO> basicDtos = new ArrayList<>();
        List<PersonalCenterSettingDTO> blockDtos = new ArrayList<>();
        List<PersonalCenterSettingDTO> listDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)) {
            Integer version = getVersion(cmd.getNamespaceId());
            dtoList.stream().forEach(r -> {
                PersonalCenterSetting personalCenterSetting = ConvertHelper.convert(r, PersonalCenterSetting.class);
                personalCenterSetting.setStatus(PersonalCenterSettingStatus.ACTIVE.getCode());
                personalCenterSetting.setVersion(version);
                personalCenterSetting.setCreateTime(new Timestamp(new Date().getTime()));
                personalCenterSetting.setCreateUid(UserContext.currentUserId());
                personalCenterSetting.setNamespaceId(cmd.getNamespaceId());
                personalCenterSetting.setLinkUrl(getLinkUrl(personalCenterSetting.getType()));
                this.personalCenterSettingProvider.createPersonalCenterSetting(personalCenterSetting);
                PersonalCenterSettingDTO returnDto = ConvertHelper.convert(personalCenterSetting, PersonalCenterSettingDTO.class);
                returnDto.setIconUrl(parseUrl(returnDto.getIconUri(),cmd.getNamespaceId()));
                switch (returnDto.getRegion()) {
                    case 0 :
                        basicDtos.add(returnDto);
                        break;
                    case 1 :
                        blockDtos.add(returnDto);
                        break;
                    case 2 :
                        listDtos.add(returnDto);
                        break;
                    default:
                        break;
                }
            });
        }
        CreatePersonalCenterSettingsResponse response = new CreatePersonalCenterSettingsResponse();
        response.setBasicDtos(basicDtos);
        response.setBlockDtos(blockDtos);
        response.setListDtos(listDtos);
        return response;
    }

    @Override
    public ListVersionListResponse listVersion(ListVersionListCommand cmd) {
        ListVersionListResponse response = new ListVersionListResponse();
        response.setVersionList(this.personalCenterSettingProvider.getVersionList(cmd.getNamespaceId()));
        return response;
    }

    @Override
    public ShowPrivateSettingResponse showPrivateSetting(ShowPrivateSettingCommand cmd) {
        ShowPrivateSettingResponse response = new ShowPrivateSettingResponse();
        response.setShowPrivateSetting(TrueOrFalseFlag.FALSE.getCode());
        String showFlag = configurationProvider.getValue(cmd.getNamespaceId(),ConfigConstants.SHOW_PRIVATE_FLAG, "");
        if ("true".equals(showFlag)) {
            response.setShowPrivateSetting(TrueOrFalseFlag.TRUE.getCode());
        }
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

    private String getLinkUrl(Integer type){
        List<PersonalCenterSetting> list = this.personalCenterSettingProvider.queryDefaultPersonalCenterSettings();
        String linkUrl = "";
        for (PersonalCenterSetting personalCenterSetting : list) {
            if (personalCenterSetting.getType().equals(type)) {
                linkUrl =  personalCenterSetting.getLinkUrl();
                break;
            }
        }
        return linkUrl;
    }
}
