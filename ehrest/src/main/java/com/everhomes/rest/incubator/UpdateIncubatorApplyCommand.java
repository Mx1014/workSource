package com.everhomes.rest.incubator;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>checkFlag: 查看状态0-未查看、1-已查看 参考{@link TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdateIncubatorApplyCommand {

	private Long id;
	private Byte checkFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Byte checkFlag) {
		this.checkFlag = checkFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
