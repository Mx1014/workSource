// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 个人中心配置列表，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingDTO}</li>
 * </ul>
 */
public class ListPersonalCenterSettingsResponse {

    @ItemType(PersonalCenterSettingDTO.class)
    private List<PersonalCenterSettingDTO> dtos;

    public List<PersonalCenterSettingDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<PersonalCenterSettingDTO> dtos) {
        this.dtos = dtos;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
