package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>sampleQualityInspectionDTOList: 例行检查列表 参考{@link SampleQualityInspectionDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 * Created by ying.xiong on 2017/6/1.
 */
public class ListSampleQualityInspectionResponse {

    private Long nextPageAnchor;

    @ItemType(SampleQualityInspectionDTO.class)
    private List<SampleQualityInspectionDTO> sampleQualityInspectionDTOList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SampleQualityInspectionDTO> getSampleQualityInspectionDTOList() {
        return sampleQualityInspectionDTOList;
    }

    public void setSampleQualityInspectionDTOList(List<SampleQualityInspectionDTO> sampleQualityInspectionDTOList) {
        this.sampleQualityInspectionDTOList = sampleQualityInspectionDTOList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
