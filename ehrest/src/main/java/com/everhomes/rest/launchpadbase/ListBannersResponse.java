package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos 参考{@link com.everhomes.rest.banner.BannerDTO}</li>
 * </ul>
 */
public class ListBannersResponse {

    private List<BannerDTO> dtos;

    public List<BannerDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<BannerDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
