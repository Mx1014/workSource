package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>routineQualityInspectionDTOList: 例行检查列表 参考{@link com.everhomes.rest.quality.RoutineQualityInspectionDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 * Created by ying.xiong on 2017/6/1.
 */
public class ListRoutineQualityInspectionResponse {

    private Long nextPageAnchor;

    @ItemType(RoutineQualityInspectionDTO.class)
    private List<RoutineQualityInspectionDTO> routineQualityInspectionDTOList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<RoutineQualityInspectionDTO> getRoutineQualityInspectionDTOList() {
        return routineQualityInspectionDTOList;
    }

    public void setRoutineQualityInspectionDTOList(List<RoutineQualityInspectionDTO> routineQualityInspectionDTOList) {
        this.routineQualityInspectionDTOList = routineQualityInspectionDTOList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
