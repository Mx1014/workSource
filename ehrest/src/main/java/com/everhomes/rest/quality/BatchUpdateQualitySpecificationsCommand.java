package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 * <li>parentId: specifications 的parentId</li>
 * <li>specifications：修改的specifications列表</li>
 * </ul>
 */
public class BatchUpdateQualitySpecificationsCommand {

    private Long parentId;

    @ItemType(CreateQualitySpecificationCommand.class)
    private List<CreateQualitySpecificationCommand> specifications;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<CreateQualitySpecificationCommand> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<CreateQualitySpecificationCommand> specifications) {
        this.specifications = specifications;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
