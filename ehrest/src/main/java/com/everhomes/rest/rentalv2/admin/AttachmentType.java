package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>TEXT_REMARK:0 文本备注</li>
 * <li>LICENSE_NUMBER:1 车牌</li>
 * <li>SHOW_CONTENT:2 显示内容</li>
 * <li>ATTACHMENT:3 附件</li>
 * <li>GOOD_ITEM:4 物资</li>
 * <li>RECOMMEND_USER:5 推荐员</li>
 * <li>ORDER_GOOD_ITEM:6 订单物资 </li>
 * <li>ORDER_RECOMMEND_USER:7 订单推荐员</li>
 * </ul>
 */
public enum AttachmentType {
	TEXT_REMARK((byte) 0), LICENSE_NUMBER((byte) 1), SHOW_CONTENT((byte) 2), ATTACHMENT((byte) 3)
	, GOOD_ITEM((byte) 4), RECOMMEND_USER((byte) 5), ORDER_GOOD_ITEM((byte) 6), ORDER_RECOMMEND_USER((byte) 7);
	private Byte code;

	private AttachmentType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static AttachmentType fromCode(Byte code) {
		if (code != null) {
			for (AttachmentType a : AttachmentType.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
