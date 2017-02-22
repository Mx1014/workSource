package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 模板字段列表 参考{@link com.everhomes.rest.asset.AssetBillTemplateFieldDTO}</li>
 * </ul>
 */
public class UpdateAssetBillTemplateCommand {

    @ItemType(AssetBillTemplateFieldDTO.class)
    private List<AssetBillTemplateFieldDTO> dtos;

    public List<AssetBillTemplateFieldDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<AssetBillTemplateFieldDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
