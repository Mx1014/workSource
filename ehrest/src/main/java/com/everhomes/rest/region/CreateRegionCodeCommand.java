// @formatter:off
package com.everhomes.rest.region;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>name: 区域名称</li>
 * <li>code: 区域码</li>
 * <li>hotFlag: 是否热点</li>
 * </ul>
 */
public class CreateRegionCodeCommand {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer code;

    private Byte hotFlag;

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

    public Byte getHotFlag() {
        return hotFlag;
    }

    public void setHotFlag(Byte hotFlag) {
        this.hotFlag = hotFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
