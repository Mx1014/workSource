package com.everhomes.rest.community;

/**
 * <ul>
 * <li><p>id: 小区ID</p></li>
 * <li><p>cityId: 城市Id</p></li>
 * <li><p>name: 账单名称</p></li>
 * <li><p>namespaceId: 域空间</p></li>
 * </ul>
 * @author janson
 *
 */
public class CommunityDoc {
    private Long id;
    private Long cityId;
    private String name;
    private String cityName;
    private Long regionId;
    private Integer namespaceId;
    private Byte communityType;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCityId() {
        return cityId;
    }
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public Long getRegionId() {
        return regionId;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Byte getCommunityType() {
		return communityType;
	}
	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
	}

}
