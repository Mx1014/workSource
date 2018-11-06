package com.everhomes.rest.appurl;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>namespaceId: 域空间</li>
 *  <li>id: 主键</li>
 *  <li>osType: 操作系统类型 参考{@link com.everhomes.rest.user.OSType}</li>
 *  <li>name: name</li>
 *  <li>downloadUrl: URL路径</li>
 *  <li>logoUrl: logo路径</li>
 *  <li>description: 相关描述</li>
 *  <li>themeColor: 主题色</li>
 *  <li>包名</li>
 * </ul>
 *
 */
public class UpdateAppInfoCommand {
	
	@NotNull
	private Integer namespaceId;
	private Long id;
	private Byte osType;	
	private String name;
	private String downloadUrl;
	private String logoUrl;
	private String description;
	private String themeColor;
	private String packageName;

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public UpdateAppInfoCommand() {
		super();
	}

	public UpdateAppInfoCommand(Integer namespaceId, Byte osType) {
		super();
		this.namespaceId = namespaceId;
		this.osType = osType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getOsType() {
		return osType;
	}

	public void setOsType(Byte osType) {
		this.osType = osType;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
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
