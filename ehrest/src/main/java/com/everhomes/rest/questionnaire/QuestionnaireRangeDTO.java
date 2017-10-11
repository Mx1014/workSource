// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区id</li>
 * <li>rangeType: 范围类型，参考 {@link com.everhomes.rest.questionnaire.QuestionnaireRangeType}</li>
 * <li>range: 范围id,当rangeType=building时，为楼栋名称，其他情况为对应id </li>
 * <li>rangeDescription: 范围描述，如（园区1认证用户、林园、园区2楼栋1用户、永佳天成科技发展有限公司用户）</li>
 * </ul>
 */
public class QuestionnaireRangeDTO {
    private Integer namespaceId;
    private Long communityId;
    private String rangeType;
    private String range;
    private String rangeDescription;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRangeDescription() {
        return rangeDescription;
    }

    public void setRangeDescription(String rangeDescription) {
        this.rangeDescription = rangeDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
