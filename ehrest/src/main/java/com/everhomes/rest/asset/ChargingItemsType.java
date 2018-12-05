package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * 
 * 1 租金 2 物业费 3 押金 4 自用水费 5 自用电费 6 滞纳金 7 公摊水费 8 公摊电费 9 增值服务费 10 税金 11 电梯保养费 12
 * 基本费 13 服务费 14 保洁费 15 安保费 16 污水费 17 管理费 18 空调费 19 网络费 20 广告费 
 * Created by djm on 2018/11/19
 */
public enum ChargingItemsType {
	RENT((long) 1, "租金"), PROPERTYFEE((long) 2, "物业费");

	private long code;

	private String description;

	ChargingItemsType(long code, String description) {
		this.code = code;
		this.description = description;
	}

	public long getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static ChargingItemsType fromStatus(Long code) {
		if (code != null) {
			for (ChargingItemsType v : ChargingItemsType.values()) {
				if (v.getCode() == code)
					return v;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
