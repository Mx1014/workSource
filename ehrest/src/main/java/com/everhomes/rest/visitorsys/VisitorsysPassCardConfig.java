// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>brandImage : (必填)品牌形象，{@link com.everhomes.rest.visitorsys.VisitorsysBrandImageType}</li>
  *<li>leftImage : (必填)左侧图像，{@link com.everhomes.rest.visitorsys.VisitorsysLeftImageType}</li>
  *<li>selfDefineOne : (必填)自定义字段1，{@link com.everhomes.rest.visitorsys.VisitorsysFieldType}</li>
  *<li>selfDefineTwo : (必填)自定义字段2，{@link com.everhomes.rest.visitorsys.VisitorsysFieldType}</li>
  *<li>remark : (选填)备注</li>
  *</ul>
  */

public class VisitorsysPassCardConfig {
    private String brandImage;
    private String leftImage;
    private String selfDefineOne;
    private String selfDefineTwo;
    private String remark;

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(String leftImage) {
        this.leftImage = leftImage;
    }

    public String getSelfDefineOne() {
        return selfDefineOne;
    }

    public void setSelfDefineOne(String selfDefineOne) {
        this.selfDefineOne = selfDefineOne;
    }

    public String getSelfDefineTwo() {
        return selfDefineTwo;
    }

    public void setSelfDefineTwo(String selfDefineTwo) {
        this.selfDefineTwo = selfDefineTwo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
