// @formatter:off
package com.everhomes.rest.region;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 区域ID</li>
 * <li>parentId: 父亲区域的ID</li>
 * <li>name: 区域名称</li>
 * <li>path: 区域路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>scopeCode: 范围，参考{@link com.everhomes.rest.region.RegionScope}</li>
 * <li>isoCode: 国际编号</li>
 * <li>telCode: 区号</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.region.RegionAdminStatus}</li>
 * <li>pinyinName: 名字全拼</li>
 * <li>pinyinPrefix: 名字首字母</li>
 * </ul>
 */
public class RegionDTO {
    private Long id;
    private Integer parentId;
    private String  name;
    private String  path;
    private Byte    scopeCode;
    private String  isoCode;
    private String  telCode;
    private Byte    status;
    private String  pinyinName;
    private String  pinyinPrefix;

    public RegionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Byte getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(Byte scopeCode) {
        this.scopeCode = scopeCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getTelCode() {
        return telCode;
    }

    public void setTelCode(String telCode) {
        this.telCode = telCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    
    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getPinyinPrefix() {
        return pinyinPrefix;
    }

    public void setPinyinPrefix(String pinyinPrefix) {
        this.pinyinPrefix = pinyinPrefix;
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof RegionDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
