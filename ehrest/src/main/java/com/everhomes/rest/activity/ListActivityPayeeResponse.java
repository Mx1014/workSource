// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 收款方账户列表</li>
 * </ul>
 */
public class ListActivityPayeeResponse {

    @ItemType(ActivityPayeeDTO.class)
    private List<ActivityPayeeDTO> dtos;

    public List<ActivityPayeeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ActivityPayeeDTO> dtos) {
        this.dtos = dtos;
    }
}
