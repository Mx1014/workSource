package com.everhomes.banner;

import com.everhomes.rest.banner.*;
import com.everhomes.rest.banner.admin.CreateBannerCommand;
import com.everhomes.rest.banner.admin.DeleteBannerCommand;
import com.everhomes.rest.banner.admin.*;
import com.everhomes.rest.banner.admin.UpdateBannerCommand;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BannerService {

    List<BannerDTO> getBanners(GetBannersCommand cmd, HttpServletRequest request);
    
    List<BannerDTO> getBannersByScene(GetBannersBySceneCommand cmd, HttpServletRequest request);

    void createBanner(CreateBannerCommand cmd);

    BannerDTO updateBanner(UpdateBannerCommand cmd);

    void deleteBannerById(DeleteBannerCommand cmd);

    String createOrUpdateBannerClick(CreateBannerClickCommand cmd);

    BannerClickDTO findBannerClickByToken(String token);

    BannerDTO getBannerById(GetBannerByIdCommand cmd);

    ListBannersResponse listBanners(ListBannersCommand cmd);
    
    ListBannersByOwnerCommandResponse listBannersByOwner(ListBannersByOwnerCommand cmd);

	void updateBannerByOwner(UpdateBannerByOwnerCommand cmd);

	void deleteBannerByOwner(DeleteBannerByOwnerCommand cmd);

	void createBannerByOwner(CreateBannerByOwnerCommand cmd);

	void reorderBannerByOwner(ReorderBannerByOwnerCommand cmd);

    List<BannerDTO> getBannersBySceneNew(GetBannersBySceneCommand cmd);

    List<BannerDTO> listBannerByCommunityId(Long communityId);

    CountEnabledBannersByScopeResponse countEnabledBannersByScope(CountEnabledBannersByScopeCommand cmd);

    void reorderBanners(ReorderBannersCommand cmd);

    BannerDTO updateBannerStatus(UpdateBannerStatusCommand cmd);
}
