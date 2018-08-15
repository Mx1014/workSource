-- Designer: zhang zhiwei
-- Description: ISSUE#29760: 考勤5.0 - 请假规则

-- 将旧版本无用的数据置为删除状态
UPDATE eh_approval_categories SET status=0 WHERE namespace_id<>0;
-- 补充新增字段值
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

-- 新增四种请假类型
SET @lastest_id = IFNULL((SELECT MAX(id) FROM `eh_approval_categories`), 1);
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'哺乳假','DAY',0.5,0,1,11,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'探亲假','DAY',0.5,0,1,12,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'看护假','DAY',0.5,0,1,13,0,NOW());
INSERT INTO eh_approval_categories(id,namespace_id,owner_type,owner_id,approval_type,category_name,time_unit,time_step,remainder_flag,status,default_order,creator_uid,create_time)
VALUE ((@lastest_id := @lastest_id + 1),0,'organization',0,1,'产检假','DAY',0.5,0,1,14,0,NOW());


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

-- 不同请假类型的提示信息模板初始化
SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10000, 'zh_CN', '$假期类型名称$：最小请假0.5$请假单位$','${categoryName}：最小请假${timeStep}${timeUnit}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10001, 'zh_CN', '$假期类型名称$：最小请假0.5$请假单位$，剩余 $余额$','${categoryName}：最小请假${timeStep}${timeUnit}，剩余${remainCountDisplay}。', 0);



-- 初始化假期余额列表已请年假总计和已请调休总计
-- 更新已有的记录
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

-- 插入新的记录
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

-- 初始化旷工天数
UPDATE eh_punch_statistics SET absence_count=(LENGTH(status_list) - LENGTH( REPLACE(status_list,'旷工','')))/6
WHERE status_list IS NOT NULL;