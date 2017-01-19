// @formatter:off
package com.everhomes.rest.category;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;

/**
 * <ul>
 * <li>id: 类型ID</li>
 * <li>parentId: 父类型ID</li>
 * <li>name: 类型名称</li>
 * <li>path: 类型路径</li>
 * <li>defaultOrder: 默认顺序索引</li>
 * <li>status: 状态，{@link com.everhomes.rest.category.CategoryAdminStatus}</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteTime: 删除时间</li>
 * <li>iconUri: 图标uri</li>
 * <li>iconUrl: 图标url</li>
 * <li>description: 描述</li>
 * </ul>
 */
public class CategoryDTO {
    private Long     id;
    private Long     parentId;
    private String   name;
    private String   path;
    private Integer  defaultOrder;
    private Byte     status;
    private String iconUri;
    private String iconUrl;
    private String description;
    
    private Timestamp createTime;
    private Timestamp deleteTime;

    @ItemType(CategoryDTO.class)
	private List<CategoryDTO> childrens;
    
    private Byte isSupportDelete;
    
    public CategoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }   
    
    public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<CategoryDTO> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<CategoryDTO> childrens) {
		this.childrens = childrens;
	}

	@Override
    public boolean equals(Object obj){
        if (! (obj instanceof FamilyDTO)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

	public Byte getIsSupportDelete() {
		return isSupportDelete;
	}

	public void setIsSupportDelete(Byte isSupportDelete) {
		this.isSupportDelete = isSupportDelete;
	}
}
