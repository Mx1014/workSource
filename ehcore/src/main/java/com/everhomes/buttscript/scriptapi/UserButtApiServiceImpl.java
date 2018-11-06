package com.everhomes.buttscript.scriptapi;

import com.everhomes.flow.*;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.scriptengine.nashorn.NashornModuleApiService;
import com.everhomes.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserButtApiServiceImpl implements NashornModuleApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserButtApiServiceImpl.class);


    @Autowired
    private UserService userService ;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public String name() {
        return "userButtService";
    }


    /**
     * 更新会员等级
     */
    public void updateUserVipLevel(Long userId, Integer vipLevel ,String vipLevelText){
        LOGGER.info("the script call the api updateUserVipLevel . userId:{},vipLevel:{},vipLevelText:{}",userId,vipLevel,vipLevelText);
        userService.updateUserVipLevel( userId,  vipLevel ,vipLevelText);

    }

    /**
     * 获取用户认证企业的企业数
     * @param userId
     * @param namespaceId
     * @return
     */
    public int getUserRelateOrganizationCount(Long userId,Integer namespaceId){
        int count = 0 ;
        //获取用户认证的企业
        OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
        List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
        LOGGER.info("organizationList :{}",organizationList);
        if(CollectionUtils.isEmpty(organizationList)){
            LOGGER.info("count :{}",count);
            return count;
        }
        List<OrganizationDTO> list = new ArrayList<OrganizationDTO>();
        //只考虑正常的企业
        for(OrganizationDTO dto :organizationList){
            LOGGER.info("OrganizationDTO :{}",dto);
            if(OrganizationMemberStatus.ACTIVE.getCode() == dto.getMemberStatus()
                    && CommunityType.COMMERCIAL.getCode() == dto.getCommunityType()){
                count = count + 1 ;
            }

        }
        LOGGER.info("count :{}",count);
        return count ;
    }


    public void testCall() {
        LOGGER.debug("this is test api call");
    }
}