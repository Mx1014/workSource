package com.everhomes.rest.category;

import java.util.Arrays;
import java.util.List;

/**
 * <ul>
 * <li>CATEGORY_ID_TOPIC(1): 帖子类型（根类型）</li>
 * <li>CATEGORY_ID_INTEREST(2): 兴趣（根类型）</li>
 * <li>CATEGORY_ID_SERVICE(3): 商家与服务（根类型）</li>
 * <li>CATEGORY_ID_ACTIVITY(4): 活动（根类型）</li>
 * <li>CATEGORY_ID_TECH_ACTIVITY(5): 科技园活动（根类型）</li>
 * <li>CATEGORY_ID_NOTICE(1003): 帖子/公告（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_REPAIRS(1004): 帖子/报修（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_CONSULT_APPEAL(1005): 帖子/咨询与求助（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_COMPLAINT_ADVICE(1006): 帖子/投诉与建议（物业、业委、居委、公安等）</li>
 * <li>CATEGORY_ID_USED_AND_RENTAL(1007): 帖子/二手和租售</li>
 * <li>CATEGORY_ID_FREE_STUFF(1008): 帖子/免费物品</li>
 * <li>CATEGORY_ID_LOST_AND_FOUND(1009): 帖子/失物招领</li>
 * <li>CATEGORY_ID_TOPIC_ACTIVITY(1010): 帖子/活动</li>
 * <li>CATEGORY_ID_TOPIC_POLLING(1011): 帖子/投票</li>
 * <li>CATEGORY_ID_CONF_CAPACITY(1011):视频会议会议容量（25方or100方）</li>
 * <li>CATEGORY_ID_CONF_TYPE(1011):视频会议会议类型（仅视频; 支持电话）</li>
 * <li>CATEGORY_ID_ACCOUNT_TYPE(1011): 视频会议账号模式（单账号or多账号）</li>
 * <li>CATEGORY_ID_QUALITY_INSPECTION(6): 品质核查根类型</li>
 * <li>CATEGORY_ID_BUSINESS_AROUND(3001): 周边商铺</li>
 * <li>CATEGORY_ID_BUSINESS_NEXTDOOR(3002): 邻家小店</li>
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
    
    /** 科技园活动 */
    public static final long CATEGORY_ID_TECH_ACTIVITY = 5L;
    
    /** 品质核查类型 */
    public static final long CATEGORY_ID_QUALITY_INSPECTION = 6L;

    /** 帖子/普通 */
    public static final long CATEGORY_ID_TOPIC_COMMON = 1001L;
    
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
    
    /** 帖子/紧急求助 */
    public static final long CATEGORY_ID_EMERGENCY_HELP = 1012L;
    
    public static final long CATEGORY_ID_CONF = 200001L;
    
    public static final long CATEGORY_ID_CONF_CAPACITY = 200101L;
    
    public static final long CATEGORY_ID_CONF_TYPE = 200201L;
    
    public static final long CATEGORY_ID_ACCOUNT_TYPE = 200301L;
    
    
    public static final long CATEGORY_ID_CLEANING = 102021L;
    public static final long CATEGORY_ID_HOUSE_KEEPING = 102022L;
    public static final long CATEGORY_ID_MAINTENANCE = 102023L;
    
    /**周边商铺 **/
    public static final long CATEGORY_ID_BUSINESS_AROUND = 3001L;
    /**邻家小店 **/
    public static final long CATEGORY_ID_BUSINESS_NEXTDOOR = 3002L;
    
    
    /** 政府机构相关的类型 */
    public static final List<Long> GA_RELATED_CATEGORIES = Arrays.asList(
        CATEGORY_ID_NOTICE, 
        CATEGORY_ID_REPAIRS, 
        CATEGORY_ID_CONSULT_APPEAL, 
        CATEGORY_ID_COMPLAINT_ADVICE,
        CATEGORY_ID_CLEANING,
        CATEGORY_ID_HOUSE_KEEPING,
        CATEGORY_ID_MAINTENANCE,
        CATEGORY_ID_EMERGENCY_HELP
    );
    
    /** 政府机构相关的有隐私的类型，使用这些类型的帖子对政府机关来说需要有权控制是否公开 */
    public static final List<Long> GA_PRIVACY_CATEGORIES = Arrays.asList(
        CATEGORY_ID_REPAIRS, 
        CATEGORY_ID_CONSULT_APPEAL, 
        CATEGORY_ID_COMPLAINT_ADVICE,
        CATEGORY_ID_CLEANING,
        CATEGORY_ID_HOUSE_KEEPING,
        CATEGORY_ID_MAINTENANCE
    );
}
