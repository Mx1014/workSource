// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * <p>launchPad信息
 * <ul>
 * <li>launchPadItems: 服务市场信息，参考{@link LaunchPadItemDTO}</li>
 * <li>categryId: 类别Id</li>
 * <li>categryName: item的类别名称</li>
 * <li>categryIconUrl: 类别icon</li>
 * <li>categryAlign: 类别标题位置 @link{com.everhomes.rest.launchpad.ItemServiceCategryAlign}</li>
 * </ul>
 */
public class CategryItemDTO {

    private Long categryId;

    private String categryName;

    private String categryIconUrl;

    private Byte categryAlign;

    @ItemType(LaunchPadItemDTO.class)
    private List<LaunchPadItemDTO> launchPadItems;

    public CategryItemDTO() {
    }
    public List<LaunchPadItemDTO> getLaunchPadItems() {
        return launchPadItems;
    }

    public void setLaunchPadItems(List<LaunchPadItemDTO> launchPadItems) {
        this.launchPadItems = launchPadItems;
    }

    public Long getCategryId() {
        return categryId;
    }

    public void setCategryId(Long categryId) {
        this.categryId = categryId;
    }

    public String getCategryName() {
        return categryName;
    }

    public void setCategryName(String categryName) {
        this.categryName = categryName;
    }

    public String getCategryIconUrl() {
        return categryIconUrl;
    }

    public void setCategryIconUrl(String categryIconUrl) {
        this.categryIconUrl = categryIconUrl;
    }

    public Byte getCategryAlign() {
        return categryAlign;
    }

    public void setCategryAlign(Byte categryAlign) {
        this.categryAlign = categryAlign;
    }

    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof CategryItemDTO)) {
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
