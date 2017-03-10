// @formatter:off
package com.everhomes.rest.questionnaire;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>题目
 * <li>id: id</li>
 * <li>questionType: 题目类型，1. 单选，2. 多选， 3. 填空， 4. 图片单选， 5. 图片多选</li>
 * <li>questionName: 题目名称</li>
 * <li>nextPageAnchor: 下一页锚点，仅当填空题时有效</li>
 * <li>options: 选项列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireOptionDTO}</li>
 * </ul>
 */
public class QuestionnaireQuestionDTO {
	private Long id;
	private Byte questionType;
	private String questionName;
	@ItemType(QuestionnaireOptionDTO.class)
	private List<QuestionnaireOptionDTO> options;
	private Long nextPageAnchor;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<QuestionnaireOptionDTO> getOptions() {
		return options;
	}

	public void setOptions(List<QuestionnaireOptionDTO> options) {
		this.options = options;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Byte questionType) {
		this.questionType = questionType;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
