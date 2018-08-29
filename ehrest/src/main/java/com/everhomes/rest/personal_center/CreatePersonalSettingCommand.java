// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>settings: 个人中心配置项，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingDTO}</li>
 * </ul>
 */
public class CreatePersonalSettingCommand {

    private Integer namespaceId;

    @ItemType(PersonalCenterSettingDTO.class)
    private List<PersonalCenterSettingDTO> settings;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<PersonalCenterSettingDTO> getSettings() {
        return settings;
    }

    public void setSettings(List<PersonalCenterSettingDTO> settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
