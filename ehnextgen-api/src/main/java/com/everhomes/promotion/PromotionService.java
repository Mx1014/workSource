package com.everhomes.promotion;

import com.everhomes.db.DaoAction;
import com.everhomes.rest.promotion.CreateOpPromotionCommand;
import com.everhomes.rest.promotion.GetOpPromotionActivityByPromotionId;
import com.everhomes.rest.promotion.ListOpPromotionActivityResponse;
import com.everhomes.rest.promotion.ListPromotionCommand;
import com.everhomes.rest.promotion.OpPromotionActivityDTO;
import com.everhomes.rest.promotion.OpPromotionOrderRangeCommand;
import com.everhomes.rest.promotion.OpPromotionRegionPushingCommand;
import com.everhomes.rest.promotion.OpPromotionSearchCommand;
import com.everhomes.rest.promotion.UpdateOpPromotionCommand;

public interface PromotionService {

    void createPromotion(CreateOpPromotionCommand cmd);

    void broadcastEvent(DaoAction action, Class<?> pojoClz, Long id);

    ListOpPromotionActivityResponse listPromotion(ListPromotionCommand cmd);

    OpPromotionActivityDTO getPromotionById(GetOpPromotionActivityByPromotionId cmd);

    void newOrderPriceEvent(OpPromotionOrderRangeCommand cmd);

    void onNewOrderPriceJob(Long userId, Long price);

    void updateOpPromotionActivity(OpPromotionActivity obj);

    void closeOpPromotion(GetOpPromotionActivityByPromotionId cmd);

    OpPromotionActivity addPushCountByPromotionId(Long id, int count);

    void createRegionPushing(OpPromotionRegionPushingCommand cmd);

    ListOpPromotionActivityResponse searchPromotion(OpPromotionSearchCommand cmd);

    void udpateOpPromotion(UpdateOpPromotionCommand cmd);

    void bizFetchCoupon(Long userId, Long couposId);

    void closeOpPromotion(OpPromotionActivity promotion);

    void finishOpPromotion(OpPromotionActivity promotion);

}
