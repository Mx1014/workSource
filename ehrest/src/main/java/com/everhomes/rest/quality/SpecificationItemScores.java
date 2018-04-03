package com.everhomes.rest.quality;

/**
 * <ul>
 *  <li>specificationId: 类型/规范 id</li>
 *  <li>specificationName: 类型/规范 名</li>
 *  <li>specificationDeducted: 类型/规范 扣分</li>
 *  <li>specificationDeductedProportion: 类型/规范 扣分占比</li>
 * </ul>
 * Created by ying.xiong on 2017/6/3.
 */
public class SpecificationItemScores {

    private Long specificationId;

    private String specificationName;

    private Double specificationDeducted;

    private Double specificationDeductedProportion;

    public Double getSpecificationDeducted() {
        return specificationDeducted;
    }

    public void setSpecificationDeducted(Double specificationDeducted) {
        this.specificationDeducted = specificationDeducted;
    }

    public Double getSpecificationDeductedProportion() {
        return specificationDeductedProportion;
    }

    public void setSpecificationDeductedProportion(Double specificationDeductedProportion) {
        this.specificationDeductedProportion = specificationDeductedProportion;
    }

    public Long getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Long specificationId) {
        this.specificationId = specificationId;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }
}
