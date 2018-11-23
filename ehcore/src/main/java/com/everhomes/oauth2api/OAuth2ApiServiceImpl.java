package com.everhomes.oauth2api;

import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.organization.OrganizationServiceImpl;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.user.ZhenZhiHuiUserDetailInfo;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiUserType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.zhenzhihui.ZhenzhihuiEnterpriseInfo;
import com.everhomes.zhenzhihui.ZhenzhihuiEnterpriseInfoProvider;
import com.everhomes.zhenzhihui.ZhenzhihuiUserInfo;
import com.everhomes.zhenzhihui.ZhenzhihuiUserInfoProvider;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.oauthapi.OAuth2ApiService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserInfoDTO;
import com.everhomes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2018/4/13.
 */
@Service
public class OAuth2ApiServiceImpl implements OAuth2ApiService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OAuth2ApiServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationServiceImpl organizationServiceImpl;

    @Autowired
    private ZhenzhihuiUserInfoProvider zhenzhihuiUserInfoProvider;

    @Autowired
    private ZhenzhihuiEnterpriseInfoProvider zhenzhihuiEnterpriseInfoProvider;

    @Override
    public UserInfo getUserInfoForInternal(Long grantorUid) {
        return userService.getUserSnapshotInfoWithPhone(grantorUid);
    }

    @Override
    public UserInfoDTO getUserInfoForThird(Long grantorUid) {
        UserInfo info = userService.getUserSnapshotInfoWithPhone(grantorUid);
        return sensitiveClean(info);
    }

    @Override
    public ZhenZhiHuiUserDetailInfo getUserInfoForZhenZhiHui(Long grantorUid) {
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(grantorUid);
        List<OrganizationSimpleDTO> organizationSimpleDTOS = this.organizationServiceImpl.listUserRelateOrganizations(grantorUid);
        if (!CollectionUtils.isEmpty(organizationSimpleDTOS)) {
            userInfo.setOrganizationList(organizationSimpleDTOS);
        }
        ZhenZhiHuiUserDetailInfo zhenZhiHuiUserDetailInfo = ConvertHelper.convert(userInfo, ZhenZhiHuiUserDetailInfo.class);
        LOGGER.info("userId = {}", userInfo.getId());
        List<ZhenzhihuiUserInfo> zhenzhihuiUserInfos = this.zhenzhihuiUserInfoProvider.listZhenzhihuiUserInfosByUserId(userInfo.getId());
        LOGGER.info("userInfo size = {}",zhenzhihuiUserInfos.size());
        if (!CollectionUtils.isEmpty(zhenzhihuiUserInfos)) {
            ZhenzhihuiUserInfo zhenzhihuiUserInfo = zhenzhihuiUserInfos.get(0);
            zhenZhiHuiUserDetailInfo.setIdentifyType(zhenzhihuiUserInfo.getIdentifyType());
            zhenZhiHuiUserDetailInfo.setIdentifyToken(zhenzhihuiUserInfo.getIdentifyToken());
            zhenZhiHuiUserDetailInfo.setName(zhenzhihuiUserInfo.getName());
            zhenZhiHuiUserDetailInfo.setEmail(zhenzhihuiUserInfo.getEmail());
        }
        List<ZhenzhihuiEnterpriseInfo> zhenzhihuiEnterpriseInfos = this.zhenzhihuiEnterpriseInfoProvider.listZhenzhihuiEnterpriseInfoByUserId(userInfo.getId());
        LOGGER.info("enterprise size = {}",zhenzhihuiEnterpriseInfos.size());
        if (!CollectionUtils.isEmpty(zhenzhihuiEnterpriseInfos)) {
            ZhenzhihuiEnterpriseInfo zhenzhihuiEnterpriseInfo = zhenzhihuiEnterpriseInfos.get(0);
            zhenZhiHuiUserDetailInfo.setEnterpriseName(zhenzhihuiEnterpriseInfo.getEnterpriseName());
            zhenZhiHuiUserDetailInfo.setEnterpriseToken(zhenzhihuiEnterpriseInfo.getEnterpriseToken());
            zhenZhiHuiUserDetailInfo.setEnterpriseType(zhenzhihuiEnterpriseInfo.getEnterpriseType());
            zhenZhiHuiUserDetailInfo.setCorporationName(zhenzhihuiEnterpriseInfo.getCorporationName());
            zhenZhiHuiUserDetailInfo.setCorporationToken(zhenzhihuiEnterpriseInfo.getIdentifyToken());
            zhenZhiHuiUserDetailInfo.setCorporationType(zhenzhihuiEnterpriseInfo.getIdentifyType());
            zhenZhiHuiUserDetailInfo.setUserType(ZhenZhiHuiUserType.BOTH.getCode());
        }else {
            zhenZhiHuiUserDetailInfo.setUserType(ZhenZhiHuiUserType.PERSON.getCode());
        }
        return zhenZhiHuiUserDetailInfo;
    }

    @Override
    public List<OrganizationMemberDTO> getAuthenticationInfo(Long grantorUid) {
        List<OrganizationMember> orgMembers = this.organizationProvider.listOrganizationMembersByUId(grantorUid);

        return orgMembers.stream().filter(r ->
                OrganizationGroupType.ENTERPRISE.getCode().equals(r.getGroupType())
                        && OrganizationMemberStatus.fromCode(r.getStatus()) == OrganizationMemberStatus.ACTIVE
                        && r.getGroupPath() != null && r.getGroupPath().split("/").length <= 2
        ).map(this::toOrganizationMemberDTO).collect(Collectors.toList());
    }

    private OrganizationMemberDTO toOrganizationMemberDTO(OrganizationMember member) {
        OrganizationMemberDTO dto = new OrganizationMemberDTO();
        dto.setContactName(member.getContactName());
        dto.setOrganizationName(member.getOrganizationName());
        dto.setContactToken(member.getContactToken());
        dto.setOrganizationId(member.getOrganizationId());
        // dto.setGroupPath(member.getGroupPath());

        Organization organization = organizationProvider.findOrganizationById(member.getOrganizationId());
        if (organization != null) {
            dto.setOrganizationName(organization.getName());
        }

        /*OrganizationMember departmentMember = organizationProvider.findDepartmentMemberByTargetIdAndOrgId(
                member.getTargetId(), member.getOrganizationId());
        if (departmentMember != null) {
            Organization department = organizationProvider.findOrganizationById(departmentMember.getOrganizationId());
            if (department != null) {
                dto.setDepartments(Collections.singletonList(toOrganizationDTO(department)));
            }
        }*/
        return dto;
    }

    private OrganizationDTO toOrganizationDTO(Organization department) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setName(department.getName());
        dto.setId(department.getId());
        dto.setPath(department.getPath());
        return dto;
    }

    private UserInfoDTO sensitiveClean(UserInfo info) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setAvatarUrl(info.getAvatarUrl());
        dto.setNickName(info.getNickName());
        dto.setAccountName(info.getAccountName());
        dto.setPhone((info.getPhones() != null && info.getPhones().size() > 0) ? info.getPhones().iterator().next() : null);
        dto.setRegionCode((info.getRegionCodes() != null && info.getRegionCodes().size() > 0) ? info.getRegionCodes().iterator().next() : null);
        dto.setBirthday(info.getBirthday());
        dto.setGender(info.getGender());
        dto.setId(info.getId());
        return dto;
    }
}
