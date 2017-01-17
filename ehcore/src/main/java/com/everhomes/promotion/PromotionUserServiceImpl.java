package com.everhomes.promotion;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.admin.CommunityUserAddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.promotion.OpPromotionScopeType;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PromotionUserServiceImpl implements PromotionUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionUserServiceImpl.class);

    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private OpPromotionAssignedScopeProvider promotionAssignedScopeProvider;
    
    @Autowired
    private UserService userService;
    
    @Override
    public void listAllUser(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        int pageSize = 100;
        List<User> users = userProvider.findUserByNamespaceId(visitor.getPromotion().getNamespaceId().intValue(), locator, pageSize);
        while((users != null) && (users.size() > 0)) {
            
            for(User user : users) {
                callback.userFound(user, visitor);    
            }
            
            if(locator.getAnchor() == null || users == null || users.size() < pageSize) {
                break;
            }
            
            users = userProvider.findUserByNamespaceId(visitor.getPromotion().getNamespaceId().intValue(), locator, pageSize);
        }
    }
    
    @Override
    public void listUserByCity(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback) {
        //TODO fix value error
        
        int namespaceId = visitor.getPromotion().getNamespaceId().intValue();
        Long id = (Long)visitor.getValue();
        
        ListingLocator locator = new ListingLocator();
        int pageSize = 100;
        
        List<Community> comunities = communityProvider.findCommunitiesByCityId(locator, pageSize, namespaceId, id);
        
        while((comunities != null) && (!comunities.isEmpty())) {
            for(Community c : comunities) {
                OpPromotionUserVisitor child = new OpPromotionUserVisitor();
                child.setParent(visitor);
                child.setValue(c.getId());
                child.setPromotion(visitor.getPromotion());
                
                listUserByCommunity(child, callback);
            }
            
            //Break after first process
            if(locator.getAnchor() == null || comunities == null || comunities.size() < pageSize) {
                break;
            }
            
            comunities = communityProvider.findCommunitiesByCityId(locator, pageSize, namespaceId, id);
        }
        
    }

    @Override
    public void listUserByCommunity(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback) {
        ListCommunityUsersCommand cmd = new ListCommunityUsersCommand();
        Long id = (Long)visitor.getValue();
        cmd.setCommunityId(id);
        cmd.setNamespaceId(visitor.getPromotion().getNamespaceId());
        cmd.setPageSize(100);

        Community community = communityProvider.findCommunityById(id);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("promotion communityType is: {}", community.getCommunityType());

        if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.RESIDENTIAL) {
            CommunityUserAddressResponse resp = communityService.listUserBycommunityId(cmd);

            while((resp != null) && (resp.getDtos() != null) && (resp.getDtos().size() > 0)) {
                List<CommunityUserAddressDTO>  dtos = resp.getDtos();
                for(CommunityUserAddressDTO dto : dtos) {
                    User user = userProvider.findUserById(dto.getUserId());

                    if(user != null) {
                        callback.userFound(user, visitor);
                    }
                }

                //break after first process
                if(resp.getNextPageAnchor() == null || resp.getDtos() == null || resp.getDtos().size() < cmd.getPageSize()) {
                    break;
                }

                cmd.setPageAnchor(resp.getNextPageAnchor());
                resp = communityService.listUserBycommunityId(cmd);
            }
        } else {
            // 之前的代码是如果communityType商业园区的话查询该域空间下的所有user
            // 但是需求是也需要按照园区区分的       add by xq.tian  2017/01/11

            CrossShardListingLocator locator = new CrossShardListingLocator();
            int pageSize = 100;
            do {
                List<OrganizationCommunityRequest> requests = organizationProvider.queryOrganizationCommunityRequestByCommunityId(locator, community.getId(), pageSize, null);

                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("CommunityId {}, queryOrganizationCommunityRequestByCommunityId result: {}", community.getId(), requests.toString());

                for (OrganizationCommunityRequest request : requests) {
                    OpPromotionUserVisitor child = new OpPromotionUserVisitor();
                    child.setParent(visitor);
                    child.setValue(request.getMemberId());
                    child.setPromotion(visitor.getPromotion());

                    this.listUserByCompany(child, callback);
                }
            } while (locator.getAnchor() != null);


            /*// 园区
            ListOrganizationsCommand listOrganizationsCommand = new ListOrganizationsCommand();
            listOrganizationsCommand.setPageSize(100);

            ListOrganizationsCommandResponse resp = organizationService.listOrganizations(listOrganizationsCommand);
            for(OrganizationDTO dto : resp.getDtos()) {
                   OpPromotionUserVisitor child = new OpPromotionUserVisitor();
                   child.setParent(visitor);
                   child.setValue(dto.getId());
                   child.setPromotion(visitor.getPromotion());

                   listUserByCompany(child, callback);
            }*/

        	/*if(visitor.getParent() == null) {
        		OpPromotionUserVisitor child = new OpPromotionUserVisitor();
        		child.setParent(visitor);
        		child.setValue(id);
        		child.setPromotion(visitor.getPromotion());
        		this.listAllUser(visitor, callback);
        	}*/
        }
    }
    
    @Override
    public void listUserByCompany(final OpPromotionUserVisitor visitor, final OpPromotionUserCallback callback) {
        Long orgId = (Long)visitor.getValue();

        List<OrganizationMember> organizationMembers = organizationProvider.listOrganizationMembersByOrgId(orgId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("OrganizationId {}, listOrganizationMembersByOrgId result: {}", orgId, organizationMembers.toString());

        organizationMembers.stream()
                .filter(member -> member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
                .forEach(member -> callback.userFound(userProvider.findUserById(member.getTargetId()), visitor));
    }
    
    @Override
    public void listUserByUserId(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback) {
        Long userId = (Long)visitor.getValue();
        User user = userProvider.findUserById(userId);
        if(user != null) {
            callback.userFound(user, visitor);    
        } else {
            user = userService.findUserByIndentifier(visitor.getPromotion().getNamespaceId(), userId.toString());
            if(user != null) {
                callback.userFound(user, visitor);    
            }
        }
        
        if(user == null) {
        	LOGGER.warn("promotion user not found value=" + visitor.getValue() + " promotionId=" + visitor.getPromotion().getId());
        }
        
    }
    
    @Override
    public Boolean checkOrganizationMember(OpPromotionActivity promotion, OrganizationMember member) {       
        Map<String, Integer> checkExists = new HashMap<String, Integer>();
        
        List<OpPromotionAssignedScope> scopes = promotionAssignedScopeProvider.getOpPromotionScopeByPromotionId(promotion.getId());
        for(OpPromotionAssignedScope scope : scopes) {
            checkExists.put("" + scope.getScopeCode() + ":" + scope.getScopeId(), 1);
        }
        
        String key = "" + OpPromotionScopeType.ORGANIZATION.getCode() + ":" + member.getOrganizationId();
        if(checkExists.containsKey(key)) {
            return true;
        }
        
        key = "" + OpPromotionScopeType.ALL.getCode() + ":0";
        if(checkExists.containsKey(key)) {
            return true;
        }
        
            Long communityId = organizationService.getOrganizationActiveCommunityId(member.getOrganizationId());
            key = "" + OpPromotionScopeType.COMMUNITY.getCode() + ":" + member.getOrganizationId();
            
            if(checkExists.containsKey(key)) {
                return true;
            }
            
            Community community = communityProvider.findCommunityById(communityId);
            if(community != null) {
                key = "" + OpPromotionScopeType.CITY.getCode() + ":" + community.getCityId();
                if(checkExists.containsKey(key)) {
                    return true;
                }      
            }
          
        return false;
    }
    
//    @Override
//    public Community listUserCommunities(Long userId) {
//        
//    }
//    
//    @Override
//    public Boolean checkUserInCompany(Long userId, Long companyId) {
//        
//    }
}
