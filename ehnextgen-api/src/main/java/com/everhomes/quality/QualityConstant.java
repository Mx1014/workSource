package com.everhomes.quality;

/**
 * Created by ying.xiong on 2017/6/22.
 */
public interface QualityConstant {
    public static final String QUALITY_CATEGORY_LIST = "quality.category.list";//品质核查 类型管理查看权限
    public static final String QUALITY_CATEGORY_CREATE = "quality.category.create";//品质核查 类型管理新增权限
    public static final String QUALITY_CATEGORY_DELETE = "quality.category.delete";//品质核查 类型管理删除权限
    public static final String QUALITY_CATEGORY_UPDATE = "quality.category.update";//品质核查 类型管理修改权限
    public static final String QUALITY_SPECIFICATION_LIST = "quality.specification.list";//品质核查 规范管理查看权限
    public static final String QUALITY_SPECIFICATION_CREATE = "quality.specification.create";//品质核查 规范管理新增权限
    public static final String QUALITY_SPECIFICATION_DELETE = "quality.specification.delete";//品质核查 规范管理删除权限
    public static final String QUALITY_SPECIFICATION_UPDATE = "quality.specification.update";//品质核查 规范管理修改权限
    public static final String QUALITY_STANDARD_LIST = "quality.standard.list";//品质核查 标准管理查看权限
    public static final String QUALITY_STANDARD_CREATE = "quality.standard.create";//品质核查 标准管理新增权限
    public static final String QUALITY_STANDARD_DELETE = "quality.standard.delete";//品质核查 标准管理删除权限
    public static final String QUALITY_STANDARD_UPDATE = "quality.standard.update";//品质核查 标准管理修改权限
    public static final String QUALITY_STANDARDREVIEW_LIST = "quality.standardreview.list";//品质核查 标准审批查看权限
    public static final String QUALITY_STANDARDREVIEW_REVIEW = "quality.standardreview.review";//品质核查 标准审批审核权限
    public static final String QUALITY_TASK_LIST = "quality.task.list";//品质核查 任务查询权限
    public static final String QUALITY_STAT_SCORE = "quality.stat.score";//品质核查 分数统计查看权限
    public static final String QUALITY_STAT_TASK = "quality.stat.task";//品质核查 任务数统计查看权限
    public static final String QUALITY_UPDATELOG_LIST = "quality.updatelog.list";//品质核查 修改记录查看权限
    public static final String QUALITY_SAMPLE_LIST = "quality.sample.list";//品质核查 绩效考核查看权限
    public static final String QUALITY_SAMPLE_CREATE = "quality.sample.create";//品质核查 绩效考核新增权限
    public static final String QUALITY_SAMPLE_UPDATE = "quality.sample.update";//品质核查 绩效考核修改权限
    public static final String QUALITY_SAMPLE_DELETE = "quality.sample.delete";//品质核查 绩效考核删除权限
    public static final String QUALITY_STAT_SAMPLE = "quality.stat.sample";//品质核查 检查统计查看权限
    public static final String QUALITY_MANAGE = "quality.manage";//品质核查 品质核查管理权限
    public static final String QUALITY_ALL = "quality.all";//品质核查 品质核查全部权限

    Long QUALITY_MODULE= 20600L;
    Long CUSTOMER_MODULE= 21100L;
    Long INVITED_CUSTOMER = 150020L;
}
