package com.everhomes.rest.appurl;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>namespaceId: 域空间</li>
 *  <li>name: name</li>
 *  <li>dtos:  参考{@link com.everhomes.rest.appurl.AppUrlDeviceDTO}</li>
 *  <li>logoUrl: logo路径</li>
 *  <li>description: 相关描述</li>
 *  <li>themeColor: 主题色</li>
 * </ul>
 *
 */
public class CreateAppInfoCommand {
	
	@NotNull
	private Integer namespaceId;	
	private String name;
	private String logoUrl;
	private String description;
	private List<AppUrlDeviceDTO> dtos ;
	private String themeColor;

	public String getThemeColor() {
		return themeColor;
	}

	public void setThemeColor(String themeColor) {
		this.themeColor = themeColor;
	}

	public CreateAppInfoCommand() {
		super();
	}

	
	public List<AppUrlDeviceDTO> getDtos() {
		return dtos;
	}


	public void setDtos(List<AppUrlDeviceDTO> dtos) {
		this.dtos = dtos;
	}


	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
