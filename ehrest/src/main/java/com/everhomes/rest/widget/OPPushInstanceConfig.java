//@formatter:off
package com.everhomes.rest.widget;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

/**
 * Created by xq.tian on 2017/1/10.
 */
public class OPPushInstanceConfig implements Serializable {

    private String itemGroup;
    private Integer entityCount;
    private Integer subjectHeight;
    private Integer descriptionHeight;

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Integer getEntityCount() {
        return entityCount;
    }

    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }

    public Integer getSubjectHeight() {
        return subjectHeight;
    }

    public void setSubjectHeight(Integer subjectHeight) {
        this.subjectHeight = subjectHeight;
    }

    public Integer getDescriptionHeight() {
        return descriptionHeight;
    }

    public void setDescriptionHeight(Integer descriptionHeight) {
        this.descriptionHeight = descriptionHeight;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
