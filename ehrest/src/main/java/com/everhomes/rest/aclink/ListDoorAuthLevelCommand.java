package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>levelId: 门禁授权层级对象的 ID，如果 levelId 为空，则显示门禁下的所有公司 </li>
 * <li>levelType: 门禁授权层级对象的类型 {@link com.everhomes.rest.aclink.DoorAccessOwnerType} </li>
 * <li>doorId: 门禁 ID</li>
 * </ul>
 * @author janson
 *
 */
public class ListDoorAuthLevelCommand {
    @NotNull
    private Long     levelId;
    
    @NotNull
    private Byte     levelType;
    
    @NotNull
    private Long     doorId;
    
    private Long pageAnchor;
    
    private Integer pageSize;

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Byte getLevelType() {
        return levelType;
    }

    public void setLevelType(Byte levelType) {
        this.levelType = levelType;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
