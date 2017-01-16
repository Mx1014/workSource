package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>limitCount: 文件数限制</li>
 * <li>limitPerSize: 每个文件大小的限制</li>
 * <li>fileExtension: 文件拓展名</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormFileDTO {
	private Integer limitCount;
	private Integer limitPerSize;
	private String fileExtension;

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public Integer getLimitPerSize() {
		return limitPerSize;
	}

	public void setLimitPerSize(Integer limitPerSize) {
		this.limitPerSize = limitPerSize;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
