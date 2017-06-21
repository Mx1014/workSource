// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 在某节点进行评论。使用方法：首先通过 searchFlowCase 得到，是否支持评价。支持评价则显示评价按钮。再点按钮，再通过 getEvaluateInfo 拿到评价的信息，比如有多少项评价。
 再通过 postEvaluate 进行多项数据的评价。这个时候，评价会显示在任务跟踪里，如果记录有字段 isEvaluate，则有多项评价，需要显示详情。也调用 getEvaluateInfo 拿到评价的详情。
 * <li> userId: 当前登录用户 ID ，可以不传</li>
 * <li> flowCaseId: 当前 case 的 ID </li>
 * <li> flowNodeId: 当前节点的 ID </li>
 * <li> stepCount: ?? </li>
 * <li> stars: 评价内容列表 {@link com.everhomes.rest.flow.FlowEvaluateItemStar} </li>
 * </ul>
 * @author janson
 *
 */
public class FlowPostEvaluateCommand {

    private Long userId;
    private Long flowCaseId;
    private Long flowNodeId;
    private Long stepCount;

    @ItemType(FlowEvaluateItemStar.class)
    private List<FlowEvaluateItemStar> stars;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public List<FlowEvaluateItemStar> getStars() {
        return stars;
    }

    public void setStars(List<FlowEvaluateItemStar> stars) {
        this.stars = stars;
    }

    public Long getStepCount() {
        return stepCount;
    }

    public void setStepCount(Long stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
