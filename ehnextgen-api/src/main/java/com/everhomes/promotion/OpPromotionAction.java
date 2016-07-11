package com.everhomes.promotion;

/**
 * 执行推送动作
 * @author janson
 *
 */
public interface OpPromotionAction {
    void fire(OpPromotionContext ctx);
}
