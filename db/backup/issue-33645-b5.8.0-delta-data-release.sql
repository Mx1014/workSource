-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 将旧版本无用的数据置为删除状态
UPDATE eh_approval_categories SET status=0 WHERE namespace_id<>0;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 新增请假单位等字段
UPDATE eh_approval_categories SET owner_type='organization',time_unit='HOUR',time_step=0.5,status=3,remainder_flag=0,default_order=1 WHERE approval_type=1 AND category_name='事假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='HOUR',time_step=0.5,status=3,remainder_flag=0,default_order=2 WHERE approval_type=1 AND category_name='病假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,hander_type='ANNUAL_LEAVE',remainder_flag=2,default_order=3 WHERE approval_type=1 AND category_name='年假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,hander_type='WORKING_DAY_OFF',remainder_flag=2,default_order=4 WHERE approval_type=1 AND category_name='调休' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=5 WHERE approval_type=1 AND category_name='婚假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=6 WHERE approval_type=1 AND category_name='产假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=7 WHERE approval_type=1 AND category_name='陪产假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=8 WHERE approval_type=1 AND category_name='丧假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=9 WHERE approval_type=1 AND category_name='工伤假' and namespace_id=0;
UPDATE eh_approval_categories SET owner_type='organization', time_unit='DAY',time_step=0.5,remainder_flag=0,default_order=10 WHERE approval_type=1 AND category_name='路途假' and namespace_id=0;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 新增四种请假类型
SET @lastest_id = IFNULL((SELECT MAX(id) FROM `eh_approval_categories`), 1);
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'哺乳假','DAY',0.5,0,1,11,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'探亲假','DAY',0.5,0,1,12,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'看护假','DAY',0.5,0,1,13,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'产检假','DAY',0.5,0,1,14,0,NOW());

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 请假类型相关操作提示文案信息
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'approval' AS scope,10030 AS code,'zh_CN' AS locale,'该假期类型不存在' AS text UNION ALL
SELECT 'approval' AS scope,10031 AS code,'zh_CN' AS locale,'该假期类型不可禁用' AS text UNION ALL
SELECT 'approval' AS scope,10032 AS code,'zh_CN' AS locale,'假期类型不支持删除操作' AS text UNION ALL
SELECT 'approval' AS scope,10033 AS code,'zh_CN' AS locale,'请假表单数据不完整' AS text UNION ALL
SELECT 'approval' AS scope,10034 AS code,'zh_CN' AS locale,'该请假类型未开启，请联系管理员' AS text UNION ALL
SELECT 'approval' AS scope,10035 AS code,'zh_CN' AS locale,'余额不足' AS text UNION ALL
SELECT 'approval.tip.info' AS scope,10003 AS code,'zh_CN' AS locale,'请假申请已取消' AS text UNION ALL
SELECT 'approval.tip.info' AS scope,10004 AS code,'zh_CN' AS locale,'请假申请被驳回' AS text UNION ALL
SELECT 'approval.tip.info' AS scope,10005 AS code,'zh_CN' AS locale,'员工发起请假申请' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 请假类型余额提示文案信息
SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10000, 'zh_CN', '$假期类型名称$：最小请假0.5$请假单位$','${categoryName}：最小请假${timeStep}${timeUnit}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10001, 'zh_CN', '$假期类型名称$：最小请假0.5$请假单位$，剩余 $余额$','${categoryName}：最小请假${timeStep}${timeUnit}，剩余${remainCountDisplay}。', 0);

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 初始化假期余额列表已请年假总计和已请调休总计
UPDATE eh_punch_vacation_balances b INNER JOIN
(
SELECT d.namespace_id,r.enterprise_id,r.user_id,d.id AS detail_id,
    SUM((CASE ac.category_name
        WHEN '年假' THEN r.duration
        ELSE 0
    END)) AS annual_leave_history_count,
    SUM((CASE ac.category_name
        WHEN '调休' THEN r.duration
        ELSE 0
    END)) AS overtime_compensation_history_count
FROM eh_punch_exception_requests r
INNER JOIN eh_approval_categories ac ON r.category_id = ac.id
INNER JOIN eh_organization_member_details d ON r.user_id = d.target_id AND r.enterprise_id = d.organization_id
WHERE r.status = 1 AND ac.namespace_id = 0 AND ac.owner_id = 0 AND ac.category_name IN ('年假' , '调休')
GROUP BY r.enterprise_id , r.user_id
)t ON t.namespace_id=b.namespace_id AND t.enterprise_id=b.owner_id AND t.user_id=b.user_id
SET b.annual_leave_history_count=t.annual_leave_history_count,b.overtime_compensation_history_count=t.overtime_compensation_history_count;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假规则 初始化假期余额列表已请年假总计和已请调休总计
SET @lastest_id = IFNULL((SELECT MAX(id) FROM `eh_punch_vacation_balances`), 1);
INSERT INTO eh_punch_vacation_balances(id,namespace_id,owner_id,owner_type,user_id,detail_id,annual_leave_balance,overtime_compensation_balance,annual_leave_history_count,overtime_compensation_history_count,creator_uid,create_time,operator_uid,update_time)
SELECT t.id,t.namespace_id,t.enterprise_id,'organization' as owner_type,t.user_id,t.detail_id,0 AS annual_leave_balance,0 AS overtime_compensation_balance,t.annual_leave_history_count,t.overtime_compensation_history_count,0 AS creator_uid,NOW() AS create_time,0 AS operator_uid,NOW() update_time FROM
(
SELECT (@lastest_id := @lastest_id + 1) AS id,d.namespace_id,r.enterprise_id,r.user_id,d.id AS detail_id,
    SUM((CASE ac.category_name
        WHEN '年假' THEN r.duration
        ELSE 0
    END)) AS annual_leave_history_count,
    SUM((CASE ac.category_name
        WHEN '调休' THEN r.duration
        ELSE 0
    END)) AS overtime_compensation_history_count
FROM eh_punch_exception_requests r
INNER JOIN eh_approval_categories ac ON r.category_id = ac.id
INNER JOIN eh_organization_member_details d ON r.user_id = d.target_id AND r.enterprise_id = d.organization_id
WHERE r.status = 1 AND ac.namespace_id = 0 AND ac.owner_id = 0 AND ac.category_name IN ('年假' , '调休')
GROUP BY r.enterprise_id , r.user_id
)t LEFT JOIN eh_punch_vacation_balances b ON t.namespace_id=b.namespace_id AND t.enterprise_id=b.owner_id AND t.user_id=b.user_id
WHERE b.id IS NULL;


-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 报表相关 初始化旷工天数
UPDATE eh_punch_statistics SET absence_count=(LENGTH(status_list) - LENGTH( REPLACE(status_list,'旷工','')))/6
WHERE status_list IS NOT NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 加班设置 考勤加班规则不同加班类型的提示文案初始化
SET @max_locale_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', -1, 'zh_CN', '未设置打卡规则');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 0, 'zh_CN', '未开启');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 1, 'zh_CN', '需要申请和打卡，时长按打卡时间计算，但不能超过申请的时间');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 2, 'zh_CN', '需要申请，时长按申请单时间计算');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 3, 'zh_CN', '不需要申请，时长按打卡时间计算');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 4, 'zh_CN', '工作日加班：');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'overtime.rule.tip.info', 5, 'zh_CN', '休息日/节假日加班：');

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 加班设置 初始化2018剩余节假日
UPDATE eh_punch_holidays SET legal_flag = 1 WHERE rule_date IN ('2018-01-01','2018-02-16','2018-02-17','2018-02-18','2018-04-05','2018-05-01','2018-06-18','2018-09-24','2018-10-01','2018-10-02','2018-10-03');

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 申请统计标题和详情的template
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.exception.statistic',1,'zh_CN','申请统计项列表-请假-内容','${beginYear}年${beginMonth}月${beginDate}日 ${beginTime} 至 ${endYear}年${endMonth}月${endDate}日 ${endTime}','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.exception.statistic',2,'zh_CN','申请统计项列表-外出、出差、加班-标题','${day}天${hour}小时 ','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.exception.statistic',3,'zh_CN','申请统计项列表-外出、出差、加班-内容','${beginYear}年${beginMonth}月${beginDate}日 ${beginTime} 至 ${endYear}年${endMonth}月${endDate}日 ${endTime}','0');

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 审批类型的的一些locale_string
SET @max_locale_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'ASK_FOR_LEAVE', 'zh_CN', '请假');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'BUSINESS_TRIP', 'zh_CN', '出差');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'OVERTIME', 'zh_CN', '加班');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'general.approval.attribute', 'GO_OUT', 'zh_CN', '外出');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'time.unit', 'date', 'zh_CN', '日');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'punch.punchType', '0', 'zh_CN', '上班');
INSERT INTO `eh_locale_strings` (`id`,scope,CODE,locale,TEXT) VALUE (@max_locale_id:=@max_locale_id+1, 'punch.punchType', '1', 'zh_CN', '下班');

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 考勤状态统计项名称定义
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'punch.status' AS scope,18 AS code,'zh_CN' AS locale,'迟到且缺卡' AS text UNION ALL
SELECT 'punch.status' AS scope,19 AS code,'zh_CN' AS locale,'缺卡' AS text UNION ALL
SELECT 'punch.status' AS scope,-1 AS code,'zh_CN' AS locale,'未设置规则' AS text UNION ALL
SELECT 'punch.status' AS scope,-2 AS code,'zh_CN' AS locale,'未安排班次' AS text UNION ALL
SELECT 'punch.status' AS scope,-3 AS code,'zh_CN' AS locale,'未打卡' AS text UNION ALL
SELECT 'punch' AS scope,10300 AS code,'zh_CN' AS locale,'月报数据还没有生成' AS text UNION ALL

SELECT 'PunchStatusStatisticsItemName' AS scope,1 AS code,'zh_CN' AS locale,'未到' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,2 AS code,'zh_CN' AS locale,'迟到' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,3 AS code,'zh_CN' AS locale,'早退' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,4 AS code,'zh_CN' AS locale,'正常' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,5 AS code,'zh_CN' AS locale,'休息' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,6 AS code,'zh_CN' AS locale,'旷工' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,7 AS code,'zh_CN' AS locale,'缺卡' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,8 AS code,'zh_CN' AS locale,'核算中' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,9 AS code,'zh_CN' AS locale,'应到' AS text UNION ALL
SELECT 'PunchStatusStatisticsItemName' AS scope,10 AS code,'zh_CN' AS locale,'已到' AS text UNION ALL

SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,1 AS code,'zh_CN' AS locale,'请假' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,2 AS code,'zh_CN' AS locale,'外出' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,3 AS code,'zh_CN' AS locale,'出差' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,4 AS code,'zh_CN' AS locale,'加班' AS text UNION ALL
SELECT 'PunchExceptionRequestStatisticsItemName' AS scope,5 AS code,'zh_CN' AS locale,'打卡异常' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 对新增的detail_id进行旧数据初始化
UPDATE eh_punch_statistics l INNER JOIN eh_organization_member_details d ON d.organization_id=l.owner_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

UPDATE eh_punch_day_logs l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

UPDATE eh_punch_day_log_files l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 从每日统计eh_punch_day_logs表中追溯当日的考勤规则，更新到eh_punch_logs表中
UPDATE eh_punch_logs l INNER JOIN eh_punch_day_logs pdl ON l.user_id=pdl.user_id AND l.enterprise_id=pdl.enterprise_id AND l.punch_date=pdl.punch_date
SET l.punch_organization_id=pdl.punch_organization_id
WHERE l.punch_organization_id IS NULL;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 将非工作日变更为休息
update eh_locale_strings set text='休息' where scope='punch.status' and code=17;
UPDATE eh_punch_statistics SET status_list=REPLACE(status_list,'非工作日','休息')  WHERE status_list LIKE '%非工作日%';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 统计相关 初始化考勤月报的异常打卡申请次数
UPDATE eh_punch_statistics s INNER JOIN
(
SELECT enterprise_id,user_id,REPLACE(LEFT(punch_date,7),'-','') AS punch_month,COUNT(1) AS exception_request_counts
FROM eh_punch_exception_requests WHERE approval_attribute='ABNORMAL_PUNCH' AND status IN(0,1) GROUP BY enterprise_id,user_id,LEFT(punch_date,7)
) e
ON s.owner_id=e.enterprise_id AND s.user_id=e.user_id AND s.punch_month=e.punch_month
SET s.exception_request_counts=e.exception_request_counts,s.punch_exception_request_count=e.exception_request_counts
WHERE s.user_id>0;

-- AUTHOR: 吴寒 20180816
-- REMARK: ISSUE-33645: 考勤7.0 - 给日/月报表旧数据初始化dept_id
-- REMARK: 先找有没有部门,如果有就取第一个部门
UPDATE eh_punch_day_logs a SET  a.dept_id =
  (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`enterprise_id`, "%")
    AND b.group_type = 'DEPARTMENT'
    AND b.`status` = 3 LIMIT 1)
WHERE a.`dept_id` IS NULL ;

-- REMARK: 没部门再找公司
UPDATE eh_punch_day_logs a SET  a.dept_id =
   (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`enterprise_id`, "%")
    AND b.group_type = 'ENTERPRISE'
    AND b.`status` = 3  LIMIT 1)
WHERE a.`dept_id` IS NULL ;

-- REMARK: 先找有没有部门,如果有就取第一个部门
UPDATE eh_punch_statistics a SET  a.dept_id =
  (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`owner_id`, "%")
    AND b.group_type = 'DEPARTMENT'
    AND b.`status` = 3 LIMIT 1)
WHERE a.`dept_id` IS NULL ;

-- REMARK: 没部门再找公司
UPDATE eh_punch_statistics a SET  a.dept_id =
  (SELECT
    b.`organization_id`
  FROM
    eh_organization_members b
  WHERE a.`detail_id` = b.`detail_id`
    AND b.group_path LIKE CONCAT("/", a.`owner_id`, "%")
    AND b.group_type = 'ENTERPRISE'
    AND b.`status` = 3  LIMIT 1)
WHERE a.`dept_id` IS NULL ;