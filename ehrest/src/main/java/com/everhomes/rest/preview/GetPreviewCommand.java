package com.everhomes.rest.preview;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 预览内容id</li>
 * </ul>
 */
public class GetPreviewCommand {
	@NotNull
    private Long id;
   
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
