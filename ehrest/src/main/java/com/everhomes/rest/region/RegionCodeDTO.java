// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <ul>
 * <li>id: 区域ID</li>
 * <li>name: 区域名称</li>
 * <li>code: 区域码</li>
 * <li>pinyin: 拼音</li>
 * <li>firstLetter: 首字母</li>
 * <li>status: 状态</li>
 * <li>hotFlag: 是否热点</li>
 * </ul>
 */
public class RegionCodeDTO {
    private Long id;
    private String name;
    private Integer code;
    private String regionCode;
    private String pinyin;
    private String firstLetter;
    private Byte hotFlag;

    public RegionCodeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public Byte getHotFlag() {
        return hotFlag;
    }

    public void setHotFlag(Byte hotFlag) {
        this.hotFlag = hotFlag;
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof RegionCodeDTO)) {
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
