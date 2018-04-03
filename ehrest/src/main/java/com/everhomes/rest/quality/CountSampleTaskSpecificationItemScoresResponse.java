package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>itemScores: 单项扣分情况 参考{@link com.everhomes.rest.quality.SpecificationItemScores}</li>
 * </ul>
 * Created by ying.xiong on 2017/6/3.
 */
public class CountSampleTaskSpecificationItemScoresResponse {
    @ItemType(SpecificationItemScores.class)
    private List<SpecificationItemScores> itemScores;

    public List<SpecificationItemScores> getItemScores() {
        return itemScores;
    }

    public void setItemScores(List<SpecificationItemScores> itemScores) {
        this.itemScores = itemScores;
    }
}
