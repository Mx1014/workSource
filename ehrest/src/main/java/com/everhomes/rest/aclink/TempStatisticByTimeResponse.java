package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
/**
 * <ul>
 *     <li>dtos:数据统计列表 {@link com.everhomes.rest.aclink.TempStatisticByTimeDTO}</li>
 *     <li>若用户无权限，抛出异常"errorCode":100055,"errorDescription":"校验应用权限失败"</li>
 * </ul>
 */
public class TempStatisticByTimeResponse {
    @ItemType(TempStatisticByTimeDTO.class)
    private List<TempStatisticByTimeDTO> dtos;

    public List<TempStatisticByTimeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<TempStatisticByTimeDTO> dtos) {
        this.dtos = dtos;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
