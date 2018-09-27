package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>lossType: 操作类型 0挂失 1解挂</li>
 * <li>cardId: 我方系统内卡号</li>
 * </ul>
 */
public class FreezeCardCommand {

    private Byte lossType;
    private Long cardId;


    public Byte getLossType() {
        return lossType;
    }

    public void setLossType(Byte lossType) {
        this.lossType = lossType;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
