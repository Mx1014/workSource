// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>newMessageCount: 新消息数量 0或者空就不展示</li>
 * <li>moments: 动态列表 参考 {@link com.everhomes.rest.enterprisemoment.MomentDTO}</li>
 * </ul>
 */
public class ListMomentsResponse {
    private Long nextPageAnchor;
    private Integer newMessageCount;
    private List<MomentDTO> moments;

    public ListMomentsResponse() {

    }

    public ListMomentsResponse(Long nextPageAnchor, Integer newMessageCount, List<MomentDTO> moments) {
        this.nextPageAnchor = nextPageAnchor;
        this.newMessageCount = newMessageCount;
        this.moments = moments;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Integer getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(Integer newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    public List<MomentDTO> getMoments() {
        return moments;
    }

    public void setMoments(List<MomentDTO> moments) {
        this.moments = moments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
