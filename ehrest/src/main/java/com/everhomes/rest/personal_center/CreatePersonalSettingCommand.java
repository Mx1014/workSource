// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>basicDtos: 基础信息区个人中心配置列表，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingDTO}</li>
 *     <li>blockDtos: 方块信息区个人中心配置列表，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingDTO}</li>
 *     <li>listDtos: 列表信息区个人中心配置列表，请参考{@link com.everhomes.rest.personal_center.PersonalCenterSettingDTO}</li> * </ul>
 */
public class CreatePersonalSettingCommand {

    private Integer namespaceId;

    @ItemType(PersonalCenterSettingDTO.class)
    private List<PersonalCenterSettingDTO> basicDtos;

    @ItemType(PersonalCenterSettingDTO.class)
    private List<PersonalCenterSettingDTO> blockDtos;

    @ItemType(PersonalCenterSettingDTO.class)
    private List<PersonalCenterSettingDTO> listDtos;

    public List<PersonalCenterSettingDTO> getBasicDtos() {
        return basicDtos;
    }

    public void setBasicDtos(List<PersonalCenterSettingDTO> basicDtos) {
        this.basicDtos = basicDtos;
    }

    public List<PersonalCenterSettingDTO> getBlockDtos() {
        return blockDtos;
    }

    public void setBlockDtos(List<PersonalCenterSettingDTO> blockDtos) {
        this.blockDtos = blockDtos;
    }

    public List<PersonalCenterSettingDTO> getListDtos() {
        return listDtos;
    }

    public void setListDtos(List<PersonalCenterSettingDTO> listDtos) {
        this.listDtos = listDtos;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
