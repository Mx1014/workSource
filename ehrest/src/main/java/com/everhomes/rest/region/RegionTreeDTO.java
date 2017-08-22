// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * <ul>
 * <li>id: 区域ID</li>
 * <li>parentId: 父亲区域的ID</li>
 * <li>name: 区域名称</li>
 * <li>path: 区域路径，含层次关系，如/父亲id/第一层孩子id/第二层孩子id/...</li>
 * <li>scopeCode: 范围，参考{@link RegionScope}</li>
 * <li>isoCode: 国际编号</li>
 * <li>telCode: 区号</li>
 * <li>status: 状态，参考{@link RegionAdminStatus}</li>
 * <li>pinyinName: 名字全拼</li>
 * <li>pinyinPrefix: 名字首字母</li>
 * <li>children: 下一层级</li>
 * </ul>
 */
public class RegionTreeDTO {
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
    private List<RegionTreeDTO> children;

    public RegionTreeDTO() {
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

    public List<RegionTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<RegionTreeDTO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof RegionTreeDTO)) {
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
