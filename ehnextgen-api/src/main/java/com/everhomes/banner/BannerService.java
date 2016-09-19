package com.everhomes.banner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.everhomes.rest.banner.BannerClickDTO;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.rest.banner.CreateBannerByOwnerCommand;
import com.everhomes.rest.banner.CreateBannerClickCommand;
import com.everhomes.rest.banner.DeleteBannerByOwnerCommand;
import com.everhomes.rest.banner.GetBannerByIdCommand;
import com.everhomes.rest.banner.GetBannersCommand;
import com.everhomes.rest.banner.ListBannersByOwnerCommand;
import com.everhomes.rest.banner.ListBannersByOwnerCommandResponse;
import com.everhomes.rest.banner.ReorderBannerByOwnerCommand;
import com.everhomes.rest.banner.UpdateBannerByOwnerCommand;
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
    
    ListBannersByOwnerCommandResponse listBannersByOwner(ListBannersByOwnerCommand cmd);

	void updateBannerByOwner(UpdateBannerByOwnerCommand cmd);

	void deleteBannerByOwner(DeleteBannerByOwnerCommand cmd);

	void createBannerByOwner(CreateBannerByOwnerCommand cmd);

	void reorderBannerByOwner(ReorderBannerByOwnerCommand cmd);
    
}
