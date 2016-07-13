package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>NO_DISCOUNT: 0不打折</li>
 * <li>FULL_MOENY_CUT_MONEY:  1满钱减钱优惠</li>
 * <li>FULL_DAY_CUT_MONEY: 2满天减钱</li>
 * <li>RATIO:  3比例折扣</li>
 * </ul>
 */
public enum DiscountType {
	NO_DISCOUNT((byte) 0), FULL_MOENY_CUT_MONEY((byte) 1), FULL_DAY_CUT_MONEY((byte) 2), RATIO((byte) 3);
	private Byte code;

	private DiscountType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static DiscountType fromCode(Byte code) {
		if (code != null) {
			for (DiscountType a : DiscountType.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
