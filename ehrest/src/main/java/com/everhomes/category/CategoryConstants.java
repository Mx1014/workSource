package com.everhomes.category;

/**
 * <ul>
 * <li>CATEGORY_ID_TOPIC(1): 帖子类型（根类型）</li>
 * <li>CATEGORY_ID_INTEREST(2): 兴趣（根类型）</li>
 * <li>CATEGORY_ID_SERVICE(3): 商家与服务（根类型）</li>
 * <li>CATEGORY_ID_ACTIVITY(4): 活动（根类型）</li>
 * <li>CATEGORY_ID_NOTICE(1003): 帖子/公告（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_REPAIRS(1004): 帖子/报修（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_CONSULT_APPEAL(1005): 帖子/咨询与求助（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_COMPLAINT_ADVICE(1006): 帖子/投诉与建议（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_USED_AND_RENTAL(1007): 帖子/二手和租售</li>
 * <li>CATEGORY_ID_FREE_STUFF(1008): 帖子/免费物品</li>
 * <li>CATEGORY_ID_LOST_AND_FOUND(1009): 帖子/失物招领</li>
 * <li>CATEGORY_ID_TOPIC_ACTIVITY(1010): 帖子/活动</li>
 * <li>CATEGORY_ID_TOPIC_POLLING(1011): 帖子/投票</li>
 * </ul>
 */
public interface CategoryConstants {
    /** 帖子 */
    public static final long CATEGORY_ID_TOPIC = 1L;
    /** 兴趣 */
    public static final long CATEGORY_ID_INTEREST = 2L;
    /** 商家与服务 */
    public static final long CATEGORY_ID_SERVICE = 3L;
    /** 活动 */
    public static final long CATEGORY_ID_ACTIVITY = 4L;
    
    /** 帖子/紧急通知 */
    public static final long CATEGORY_ID_URGENT_NOTICE = 1002L;
    
    /** 帖子/公告（物业、业委、居委、公安等） */
    public static final long CATEGORY_ID_NOTICE = 1003L;
    /** 帖子/报修（物业、业委、居委、公安等）*/
    public static final long CATEGORY_ID_REPAIRS = 1004L;
    /** 帖子/咨询与求助（物业、业委、居委、公安等） */
    public static final long CATEGORY_ID_CONSULT_APPEAL = 1005L;
    /** 帖子/投诉与建议（物业、业委、居委、公安等） */
    public static final long CATEGORY_ID_COMPLAINT_ADVICE = 1006L;
    /** 帖子/二手和租售 */
    public static final long CATEGORY_ID_USED_AND_RENTAL = 1007L;
    /** 帖子/免费物品 */
    public static final long CATEGORY_ID_FREE_STUFF = 1008L;
    /** 帖子/失物招领 */
    public static final long CATEGORY_ID_LOST_AND_FOUND = 1009L;
    /** 帖子/活动 */
    public static final long CATEGORY_ID_TOPIC_ACTIVITY = 1010L;
    /** 帖子/投票 */
    public static final long CATEGORY_ID_TOPIC_POLLING = 1011L;
}
