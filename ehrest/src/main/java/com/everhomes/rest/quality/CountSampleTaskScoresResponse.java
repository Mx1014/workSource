package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>sampleTasks: 参考{@link com.everhomes.rest.quality.SampleTaskScoreDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 * Created by ying.xiong on 2017/6/2.
 */
public class CountSampleTaskScoresResponse {

    @ItemType(SampleTaskScoreDTO.class)
    private List<SampleTaskScoreDTO> sampleTasks;

    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SampleTaskScoreDTO> getSampleTasks() {
        return sampleTasks;
    }

    public void setSampleTasks(List<SampleTaskScoreDTO> sampleTasks) {
        this.sampleTasks = sampleTasks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
