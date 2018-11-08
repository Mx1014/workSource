package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>multiSelectValue: 选项list </li>
 * <li>multiSelectItemPingyin: 选项的拼音,该值后台自动处理后保存</li>
 * </ul>
 * @author liangming.huang 表单多选项
 *
 */
public class GeneralFormMultiSelectDTO {
	@ItemType(String.class)
	private List<String> selectValue;

	@ItemType(String.class)
	private List<String> pinYin;



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<String> getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(List<String> selectValue) {
		this.selectValue = selectValue;
	}

	public List<String> getPinYin() {
		return pinYin;
	}

	public void setPingYin(List<String> pinYin) {
		this.pinYin = pinYin;
	}
}
