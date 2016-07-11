package com.everhomes.banner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.everhomes.rest.banner.BannerClickDTO;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.CreateBannerClickCommand;
import com.everhomes.rest.banner.GetBannerByIdCommand;
import com.everhomes.rest.banner.GetBannersCommand;
import com.everhomes.rest.banner.admin.CreateBannerAdminCommand;
import com.everhomes.rest.banner.admin.DeleteBannerAdminCommand;
import com.everhomes.rest.banner.admin.ListBannersAdminCommand;
import com.everhomes.rest.banner.admin.ListBannersAdminCommandResponse;
import com.everhomes.rest.banner.admin.UpdateBannerAdminCommand;
import com.everhomes.rest.ui.banner.GetBannersBySceneCommand;


public interface BannerService {

    List<BannerDTO> getBanners(GetBannersCommand cmd, HttpServletRequest request);
    
    List<BannerDTO> getBannersByScene(GetBannersBySceneCommand cmd, HttpServletRequest request);

    void createBanner(CreateBannerAdminCommand cmd);

    void updateBanner(UpdateBannerAdminCommand cmd);

    void deleteBannerById(DeleteBannerAdminCommand cmd);

    String createOrUpdateBannerClick(CreateBannerClickCommand cmd);

    BannerClickDTO findBannerClickByToken(String token);

    BannerDTO getBannerById(GetBannerByIdCommand cmd);

    ListBannersAdminCommandResponse listBanners(ListBannersAdminCommand cmd);
    
}
