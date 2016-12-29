package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>limitWord: 字符限制</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormTextDTO {
	private Integer limitWord;

	public Integer getLimitWord() {
		return limitWord;
	}

	public void setLimitWord(Integer limitWord) {
		this.limitWord = limitWord;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
