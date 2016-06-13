package com.everhomes.rest.business;


import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 商家ID</li>
 * <li>targetType: 商家来源类型,参考{@link com.everhomes.rest.business.BusinessTargetType}</li>
 * <li>targetId: 商家原始id</li>
 * <li>name: 商家名字</li>
 * <li>displayName: 商家显示名</li>
 * <li>logoUri: 商家logo/li>
 * <li>url: 访问商家信息的url</li>
 * <li>contact: 商家拥有者名字</li>
 * <li>phone: 商家所有在联系方式</li>
 * <li>longitude: 商家地址所在经度</li>
 * <li>latitude: 商家地址所在纬度</li>
 * <li>address: 商家地址详情</li>
 * <li>description: 商家描述</li>
 * <li>scopes: 商家可见范围，参考{@link com.everhomes.rest.business.BusinessScope}</li>
 * <li>categroies: 商家归属的分类列表</li>
 * </ul>
 */

public class UpdateBusinessCommand{
    @NotNull
    private Long id;
    private Byte     targetType;
    private String     targetId;
    @NotNull
    private String   name;
    @NotNull
    private String   displayName;
    private String   logoUri;
    private String   url;
    private String   contact;
    private String phone;
    private Double   longitude;
    private Double   latitude;
    private String  address;
    private String  description;
    @ItemType(BusinessScope.class)
    private List<BusinessScope> scopes;
    @ItemType(Long.class)
    private List<Long> categroies;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getTargetType() {
        return targetType;
    }

    public void setTargetType(Byte targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<BusinessScope> getScopes() {
        return scopes;
    }

    public void setScopes(List<BusinessScope> scopes) {
        this.scopes = scopes;
    }

    public List<Long> getCategroies() {
        return categroies;
    }

    public void setCategroies(List<Long> categroies) {
        this.categroies = categroies;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
