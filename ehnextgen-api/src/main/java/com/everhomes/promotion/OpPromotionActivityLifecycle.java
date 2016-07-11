package com.everhomes.promotion;

/**
 * TODO lifecycle for the job
 * @author janson
 *
 */
public interface OpPromotionActivityLifecycle {
    void onPromotionCreating(OpPromotionActivityContext ctx);
    void onPromotionCreated(OpPromotionActivityContext ctx);
    
    void onPromotionJobStart(OpPromotionActivityContext ctx);
    void onPromotionJobStop(OpPromotionActivityContext ctx);
    
    void onPromotionDoJob(OpPromotionActivityContext ctx);
    
    void onPromotionDie(OpPromotionActivityContext ctx);
}
