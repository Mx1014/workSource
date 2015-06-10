// @formatter:off
package com.everhomes.banner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

import freemarker.core.ReturnInstruction.Return;



@Component
public class BannerServiceImpl implements BannerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerServiceImpl.class);
    
    @Autowired
    private BannerProvider bannerProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Override
    public List<BannerDTO> listBannerByCommuniyId(ListBannerByCommunityIdCommand cmd){
        if(cmd.getCommunityId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
        }
        long communityId = cmd.getCommunityId();
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid communityId paramter.");
        }
        long startTime = System.currentTimeMillis();
        long cityId = community.getCityId();
        User user = UserContext.current().getUser();
        long userId = user.getId();
        //query user relate banners
        List<Banner> countryBanners = bannerProvider.listBannersByScopeTypeAndScopeId(BannerScopeType.COUNTRY.getCode(), 0L);
        List<Banner> cityBanners = bannerProvider.listBannersByScopeTypeAndScopeId(BannerScopeType.CITY.getCode(), cityId);
        List<Banner> communityBanners = bannerProvider.listBannersByScopeTypeAndScopeId(BannerScopeType.COMMUNITY.getCode(), communityId);
        List<Banner> allBanners = new ArrayList<Banner>();
        if(countryBanners != null)
            allBanners.addAll(countryBanners);
        if(cityBanners != null)
            allBanners.addAll(cityBanners);
        if(communityBanners != null)
            allBanners.addAll(communityBanners);
        List<BannerDTO> result = allBanners.stream().map((Banner r) ->{
           BannerDTO dto = ConvertHelper.convert(r, BannerDTO.class); 
           dto.setPosterPath(parserUri(dto.getPosterPath(),EntityType.USER.getCode(),userId));
           return dto;
        }).collect(Collectors.toList());
        
        long endTime = System.currentTimeMillis();
        int size = result == null ? 0 : result.size();
        LOGGER.info("Query banner by communityId complete,communityId=" + communityId + ",size=" + size + ",esplse=" + (endTime - startTime));
        return result;
    }
    private String parserUri(String uri,String ownerType, long ownerId){
        try {
            if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
                return contentServerService.parserUri(uri,ownerType,ownerId);
            
        } catch (Exception e) {
            LOGGER.error("Parser uri is error." + e.getMessage());
        }
        return null;

    }
    
    @Override
    public void createBanner(CreateBannerCommand cmd){
        if(cmd.getScopes() == null || cmd.getScopes().isEmpty()){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid scopes paramter.");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        List<BannerScope> scopes = cmd.getScopes();
        scopes.forEach(scope ->{
            Banner banner = new Banner();
            banner.setActionName(cmd.getActionName());
            banner.setActionUri(cmd.getActionUri());
            banner.setAppid(cmd.getAppid());
            banner.setCreatorUid(userId);
            banner.setName(cmd.getName());
            banner.setNamespaceId(cmd.getNamespaceId());
            banner.setStartTime(cmd.getStartTime());
            banner.setEndTime(cmd.getEndTime());
            banner.setStatus(cmd.getStatus());
            banner.setPosterPath(cmd.getPosterPath());
            banner.setScopeType(scope.getScopeType());
            banner.setScopeId(scope.getScopeId());
            banner.setOrder(scope.getOrder());
            banner.setVendorTag(cmd.getVendorTag());
            bannerProvider.createBanner(banner);
        });
    }
    
    @Override
    public void updateBanner(UpdateBannerCommand cmd){
        if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null){
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        
        if(cmd.getActionUri() != null)
            banner.setActionUri(cmd.getActionUri());
        if(cmd.getEndTime() != null)
            banner.setEndTime(cmd.getEndTime());
        if(cmd.getStartTime() != null)
            banner.setEndTime(cmd.getStartTime());
        if(cmd.getOrder() != null)
            banner.setOrder(cmd.getOrder());
        if(cmd.getPosterPath() != null)
            banner.setPosterPath(cmd.getPosterPath());
        if(cmd.getScopeType() != null)
            banner.setScopeType(cmd.getScopeType());
        if(cmd.getScopeId() != null)
            banner.setScopeId(cmd.getScopeId());
        if(cmd.getStatus() != null)
            banner.setStatus(cmd.getStatus());
        
        bannerProvider.updateBanner(banner);
    }
    @Override
    public void deleteBannerById(DeleteBannerCommand cmd){
        if(cmd.getId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid id paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getId());
        if(banner == null){
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        
        bannerProvider.deleteBanner(banner);
    }
    
    @Override
    public void createOrUpdateBannerClick(CreateBannerClickCommand cmd){
        if(cmd.getBannerId() == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid bannerId paramter.");
        }
        Banner banner = bannerProvider.findBannerById(cmd.getBannerId());
        if(banner == null){
            throw RuntimeErrorException.errorWith(BannerServiceErrorCode.SCOPE,
                    BannerServiceErrorCode.ERROR_BANNER_NOT_EXISTS, "Banner is not exists.");
        }
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        BannerClick bannerClick = bannerProvider.findBannerClickByBannerIdAndUserId(cmd.getBannerId(),userId);
        if(bannerClick == null){
            bannerClick = new BannerClick();
            bannerClick.setBannerId(cmd.getBannerId());
            if(cmd.getFamilyId() != null)
                bannerClick.setFamilyId(cmd.getFamilyId());
            bannerClick.setClickCount(1L);
            bannerClick.setUid(userId);
            bannerClick.setUuid(UUID.randomUUID().toString());
            bannerClick.setLastClickTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            bannerClick.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            bannerProvider.createBannerClick(bannerClick);
            
        }else {
            bannerClick.setClickCount(bannerClick.getClickCount().longValue() + 1);
            bannerClick.setLastClickTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            bannerProvider.updateBannerClick(bannerClick);
        }
        
    }
}
