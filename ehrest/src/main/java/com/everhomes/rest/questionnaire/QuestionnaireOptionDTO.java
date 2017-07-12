// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>选项
 * <li>id: id</li>
 * <li>optionName: 选项名称（若为统计时的填空题，则为企业名称）</li>
 * <li>optionUrl: 图片链接url</li>
 * <li>optionUri: 图片链接uri</li>
 * <li>checkedCount: 被选择次数</li>
 * <li>optionContent: 选项内容，仅针对填空题统计时此字段有值</li>
 * <li>checked: 是否选中，0否1是，仅当企业查询时有效</li>
 * </ul>
 */
public class QuestionnaireOptionDTO {
	private Long id;
	private String optionName;
	private String optionUrl;
	private String optionUri;
	private Integer checkedCount;
	private String optionContent;
	private Byte checked;

	public String getOptionUri() {
		return optionUri;
	}

	public void setOptionUri(String optionUri) {
		this.optionUri = optionUri;
	}

	public Byte getChecked() {
		return checked;
	}

	public void setChecked(Byte checked) {
		this.checked = checked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionUrl() {
		return optionUrl;
	}

	public void setOptionUrl(String optionUrl) {
		this.optionUrl = optionUrl;
	}

	public Integer getCheckedCount() {
		return checkedCount;
	}

	public void setCheckedCount(Integer checkedCount) {
		this.checkedCount = checkedCount;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
