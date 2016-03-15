package com.everhomes.rest.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>path: content server使用的路径</li>
 * </ul>
 */
public class DecodeContentPathCommand {
	private String path;
	
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
