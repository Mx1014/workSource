package com.everhomes.rest.officecubicle.admin;

/**
 * <ul>
 * <li>id: id</li>
 * <li>refundStrategy  {@link CubicleOrderStrategy}</li>
 * <li>tips: 退款文案</li>
 * </ul>
 */
public class OfficeCubicleRefundTipDTO {

    private Long id;
    private Byte refundStrategy;
    private String tips;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getRefundStrategy() {
        return refundStrategy;
    }

    public void setRefundStrategy(Byte refundStrategy) {
        this.refundStrategy = refundStrategy;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
