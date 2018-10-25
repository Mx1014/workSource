package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>appType: 应用类型，参考{@link com.everhomes.rest.module.ServiceModuleAppType}</li>
 *     <li>keyWord: 搜索关键字</li>
 * </ul>
 */
public class ListServiceModulesByAppTypeCommand {

	private Byte appType;

	private String keyWord;

	public Byte getAppType() {
		return appType;
	}

	public void setAppType(Byte appType) {
		this.appType = appType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
