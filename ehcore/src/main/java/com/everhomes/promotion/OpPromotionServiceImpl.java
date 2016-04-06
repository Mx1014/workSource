// @formatter:off
package com.everhomes.promotion;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.promotion.OpPromotionDTO;
import com.everhomes.rest.promotion.OpPromotionScope;
import com.everhomes.rest.promotion.OpPromotionStatus;
import com.everhomes.rest.ui.user.GetUserOpPromotionCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.ListUserOpPromotionsRespose;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;

@Component
public class OpPromotionServiceImpl implements OpPromotionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpPromotionServiceImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private OpPromotionProvider opPromotionProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Override
    public ListUserOpPromotionsRespose getUserOpPromotionsByScene(GetUserOpPromotionCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        
        Integer namespaceId = user.getNamespaceId();
        String sceneType = sceneToken.getScene();
        
        Condition scopeCondition = buildOpPromotionScopeCondition(userId, sceneToken);
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<OpPromotion> promotionList = opPromotionProvider.listOpPromotions(locator, pageSize, (loc, query) -> {
            query.addConditions(Tables.EH_OP_PROMOTIONS.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(Tables.EH_OP_PROMOTIONS.SCENE_TYPE.eq(sceneType));
            query.addConditions(Tables.EH_OP_PROMOTIONS.STATUS.eq(OpPromotionStatus.ACTIVE.getCode()));
            query.addConditions(scopeCondition);
            
            return query;
        });
        
        List<OpPromotionDTO> dtoList = promotionList.stream().map((r) -> {
            return ConvertHelper.convert(r, OpPromotionDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isDebugEnabled()) {
            long endTime = System.currentTimeMillis();
            int resultSize = (promotionList == null) ? 0 : promotionList.size();
            LOGGER.debug("List user operation promotions, sceneToken={}, resultSize={}, elapse={}", 
                sceneToken, resultSize, (endTime - startTime));
        }
        
        return new ListUserOpPromotionsRespose(dtoList, locator.getAnchor());
    }
    
    /**
     * 构建活动可见范围条件：个人、所在小区、所在公司
     * @param userId 用户ID
     * @param sceneToken 用户场景信息
     * @return
     */
    private Condition buildOpPromotionScopeCondition(Long userId, SceneTokenDTO sceneToken) {
        Condition userScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.USER.getCode());
        userScopeCond = userScopeCond.and(Tables.EH_OP_PROMOTIONS.SCOPE_ID.eq(userId));
        
        Condition allScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.ALL.getCode());
        Condition cmntyScopeCond = null;
        Condition cityScopeCond = null;
        Condition orgScopeCond = null;
        
        Community community = null;
        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
        switch(entityType) {
        case COMMUNITY_RESIDENTIAL:
        case COMMUNITY_COMMERCIAL:
        case COMMUNITY:
            cmntyScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.COMMUNITY.getCode());
            cmntyScopeCond = cmntyScopeCond.and(Tables.EH_OP_PROMOTIONS.SCOPE_ID.eq(sceneToken.getEntityId()));
            
            community = communityProvider.findCommunityById(sceneToken.getEntityId());
            if(community != null) {
                cityScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.CITY.getCode());
                cityScopeCond = cityScopeCond.and(Tables.EH_OP_PROMOTIONS.SCOPE_ID.eq(community.getCityId()));
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Community not found, sceneToken={}, communityId={}", sceneToken, sceneToken.getEntityId());
                }
            }
            break;
        case FAMILY:
            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
            if(family != null) {
                cmntyScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.COMMUNITY.getCode());
                cmntyScopeCond = cmntyScopeCond.and(Tables.EH_OP_PROMOTIONS.SCOPE_ID.eq(family.getCommunityId()));
                
                community = communityProvider.findCommunityById(family.getCommunityId());
                if(community != null) {
                    cityScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.CITY.getCode());
                    cityScopeCond = cityScopeCond.and(Tables.EH_OP_PROMOTIONS.SCOPE_ID.eq(community.getCityId()));
                } else {
                    if(LOGGER.isWarnEnabled()) {
                        LOGGER.warn("Community not found, sceneToken={}, communityId={}", sceneToken, sceneToken.getEntityId());
                    }
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken={}", sceneToken);
                }
            }
            break;
        case ORGANIZATION:
        case ENTERPRISE:
            orgScopeCond = Tables.EH_OP_PROMOTIONS.SCOPE_CODE.eq(OpPromotionScope.ORGANIZATION.getCode());
            orgScopeCond = orgScopeCond.and(Tables.EH_OP_PROMOTIONS.SCOPE_ID.eq(sceneToken.getEntityId()));
            break;
        default:
            LOGGER.error("Unsupported user entity type, sceneToken={}", sceneToken);
        }
        
        Condition c = userScopeCond.or(allScopeCond);
        if(cmntyScopeCond != null) {
            c = c.or(cmntyScopeCond);
        }
        
        if(orgScopeCond != null) {
            c = c.or(orgScopeCond);
        }
        
        return c;
    }
}
