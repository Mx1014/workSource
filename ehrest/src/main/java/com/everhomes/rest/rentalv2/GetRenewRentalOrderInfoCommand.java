package com.everhomes.rest.rentalv2;


/**
 * <ul>
 * <li>rentalBillId: 订单id</li>
 * <li>rentalType: rentalType</li>
 * <li>timeStep: timeStep</li>
 * <li>cellCount: 单元格数量</li>
 * </ul>
 */
public class GetRenewRentalOrderInfoCommand {
    private Long rentalBillId;

    private Byte rentalType;
    private Double timeStep;

    private Double cellCount;

    public Double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(Double timeStep) {
        this.timeStep = timeStep;
    }

    public Long getRentalBillId() {
        return rentalBillId;
    }

    public void setRentalBillId(Long rentalBillId) {
        this.rentalBillId = rentalBillId;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public Double getCellCount() {
        return cellCount;
    }

    public void setCellCount(Double cellCount) {
        this.cellCount = cellCount;
    }
}
