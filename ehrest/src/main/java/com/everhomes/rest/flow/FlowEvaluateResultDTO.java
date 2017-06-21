// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>evaluateItemId: itemId</li>
 * <li>name: 节点名字</li>
 * <li>star: 评分，如果暂时没有评分则为空</li>
 * <li>inputFlag: 是否需要输入评价内容 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>content: 评价内容</li>
 * </ul>
 * @author janson
 *
 */
public class FlowEvaluateResultDTO {

    private Long evaluateItemId;
    private String name;
    private Byte star;
    private Byte inputFlag;
    private String content;

    public Long getEvaluateItemId() {
        return evaluateItemId;
    }

    public void setEvaluateItemId(Long evaluateItemId) {
        this.evaluateItemId = evaluateItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStar() {
        return star;
    }

    public void setStar(Byte star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getInputFlag() {
        return inputFlag;
    }

    public void setInputFlag(Byte inputFlag) {
        this.inputFlag = inputFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
