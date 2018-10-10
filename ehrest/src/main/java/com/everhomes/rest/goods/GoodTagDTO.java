package com.everhomes.rest.goods;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>goodTagKey : 商品标识，print,copy</li>
 * <li>goodTagValue : 商品名称 月卡/打印</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月9日
 */
public class GoodTagDTO {
	private String goodTagKey;
	private String goodTagValue;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getGoodTagKey() {
		return goodTagKey;
	}

	public void setGoodTagKey(String goodTagKey) {
		this.goodTagKey = goodTagKey;
	}

	public String getGoodTagValue() {
		return goodTagValue;
	}

	public void setGoodTagValue(String goodTagValue) {
		this.goodTagValue = goodTagValue;
	}

}
