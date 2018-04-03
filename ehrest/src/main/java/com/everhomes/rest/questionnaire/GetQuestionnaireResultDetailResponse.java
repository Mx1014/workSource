// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>collectionCount: 收集量</li>
 * <li>targetUserNum: 目标用户数量</li>
 * <li>percentComplete: 完成收集量的百分比</li>
 * <li>questionnaireResultTargets: 问卷调查结果目标列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireResultTargetDTO}</li>
 * </ul>
 */
public class GetQuestionnaireResultDetailResponse {

	private Long nextPageAnchor;
	private Integer collectionCount;
	private Integer targetUserNum;
	private String percentComplete;

	@ItemType(QuestionnaireResultTargetDTO.class)
	private List<QuestionnaireResultTargetDTO> questionnaireResultTargets;

	public GetQuestionnaireResultDetailResponse() {

	}

	public GetQuestionnaireResultDetailResponse(Long nextPageAnchor, List<QuestionnaireResultTargetDTO> questionnaireResultTargets) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.questionnaireResultTargets = questionnaireResultTargets;
	}

	public GetQuestionnaireResultDetailResponse(Long nextPageAnchor, Integer collectionCount, Integer targetUserNum, String percentComplete, List<QuestionnaireResultTargetDTO> questionnaireResultTargets) {
		this.nextPageAnchor = nextPageAnchor;
		this.collectionCount = collectionCount;
		this.targetUserNum = targetUserNum;
		this.percentComplete = percentComplete;
		this.questionnaireResultTargets = questionnaireResultTargets;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public Integer getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(Integer collectionCount) {
		this.collectionCount = collectionCount;
	}

	public Integer getTargetUserNum() {
		return targetUserNum;
	}

	public void setTargetUserNum(Integer targetUserNum) {
		this.targetUserNum = targetUserNum;
	}

	public String getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(String percentComplete) {
		this.percentComplete = percentComplete;
	}

	public List<QuestionnaireResultTargetDTO> getQuestionnaireResultTargets() {
		return questionnaireResultTargets;
	}

	public void setQuestionnaireResultTargets(List<QuestionnaireResultTargetDTO> questionnaireResultTargets) {
		this.questionnaireResultTargets = questionnaireResultTargets;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
