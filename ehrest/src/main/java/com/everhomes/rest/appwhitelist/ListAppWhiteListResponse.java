// @formatter:off
package com.everhomes.rest.appwhitelist;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 第三方应用白名单列表，请参考{@link com.everhomes.rest.appwhitelist.AppWhiteListDTO}</li>
 * </ul>
 */
public class ListAppWhiteListResponse {

    @ItemType(value = AppWhiteListDTO.class)
    private List<AppWhiteListDTO> dtos;

    public List<AppWhiteListDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<AppWhiteListDTO> dtos) {
        this.dtos = dtos;
    }
}
