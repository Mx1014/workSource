-- 通用脚本
-- ADD BY 张智伟
-- issue-28363 会议管理V1.0

INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'meetingErrorCode' AS scope,100000 AS code,'zh_CN' AS locale,'该会议室已删除' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100001 AS code,'zh_CN' AS locale,'该会议室名称已存在' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100002 AS code,'zh_CN' AS locale,'该会议已删除' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100003 AS code,'zh_CN' AS locale,'该纪要已删除' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100005 AS code,'zh_CN' AS locale,'您无权删除该纪要' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100006 AS code,'zh_CN' AS locale,'您无权创建或编辑该纪要' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100007 AS code,'zh_CN' AS locale,'您无权编辑该会议' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100008 AS code,'zh_CN' AS locale,'该会议室不可用' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100009 AS code,'zh_CN' AS locale,'该时间段已被预订' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100010 AS code,'zh_CN' AS locale,'该会议已取消' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100011 AS code,'zh_CN' AS locale,'该会议已结束' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100012 AS code,'zh_CN' AS locale,'您无权查看该会议' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100013 AS code,'zh_CN' AS locale,'您无权查看该纪要' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100014 AS code,'zh_CN' AS locale,'只能预订未来的会议' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100015 AS code,'zh_CN' AS locale,'该会议已开始，无法更改' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100016 AS code,'zh_CN' AS locale,'该会议纪要已被其他人创建' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100017 AS code,'zh_CN' AS locale,'会议结束后才可以写纪要' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100018 AS code,'zh_CN' AS locale,'会议还没有开始不能结束' AS text UNION ALL
SELECT 'meetingErrorCode' AS scope,100019 AS code,'zh_CN' AS locale,'已经结束的会议不能被取消' AS text UNION ALL

SELECT 'meetingMessage' AS scope,100001 AS code,'zh_CN' AS locale,'您的会议即将开始' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100002 AS code,'zh_CN' AS locale,'您的会议已调整' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100003 AS code,'zh_CN' AS locale,'您的会议已取消' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;


INSERT INTO eh_locale_templates(scope,code,locale,description,text,namespace_id)
SELECT r.scope,r.code,r.locale,r.description,r.text,0
FROM(
SELECT 'meetingMessage' AS scope,1000000 AS code,'zh_CN' AS locale,'#发起人#邀请您参加会议' AS description,'${meetingSponsorName}邀请您参加会议' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000001 AS code,'zh_CN' AS locale,'#发起人#发布了会议纪要' AS description,'${meetingRecorderName}发布了会议纪要' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000002 AS code,'zh_CN' AS locale,'主题：#会议主题#\r\n时间：#会议开始时间#\r\n地点：#会议室名称#' AS description,'主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000003 AS code,'zh_CN' AS locale,'邮箱通知正文' AS description,'主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}|发起人：${meetingSponsorName}|参会人：${meetingUserList}' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000004 AS code,'zh_CN' AS locale,'会议主题：#会议主题#' AS description,'会议主题：${meetingSubject}' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000005 AS code,'zh_CN' AS locale,'邮箱通知正文' AS description,'主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}|发起人：${meetingSponsorName}|参会人：${meetingUserList}|详细信息请打开：${appName} APP查看' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000006 AS code,'zh_CN' AS locale,'管理员#姓名#删除了会议室' AS description,'管理员${adminContactName}删除了会议室' AS text UNION ALL
SELECT 'meetingMessage' AS scope,1000007 AS code,'zh_CN' AS locale,'#发起人#修改了会议纪要' AS description,'${meetingRecorderName}修改了会议纪要' AS text
)r LEFT JOIN eh_locale_templates t ON r.scope=t.scope AND r.code=t.code AND r.locale=t.locale
WHERE t.id IS NULL;


INSERT INTO eh_service_modules(id,name,parent_id,path,type,level,STATUS,create_time,creator_uid,operator_uid,action_type,multiple_flag,module_control_type,default_order)
VALUE(53000,'会议室管理',50000,'/50000/53000',1,2,2,NOW(),0,0,75,0,'org_control',0);

-- 新增菜单配置
INSERT INTO eh_web_menus(id,name,parent_id,data_type,leaf_flag,status,path,type,sort_num,module_id,level,condition_type,category)
SELECT r.id,r.name,r.parent_id,r.data_type,r.leaf_flag,r.status,r.path,r.type,r.sort_num,r.module_id,r.level,'system','module' FROM(
SELECT 16041900 AS id,'会议室管理' AS name,16040000 AS parent_id,'meeting-management' AS data_type,1 AS leaf_flag,2 AS status,'/16000000/16040000/16041900' AS path,'zuolin' AS type,19 AS sort_num,53000 AS module_id,3 AS level UNION ALL
SELECT 48190000 AS id,'会议室管理' AS name,48000000 AS parent_id,'meeting-management' AS data_type,1 AS leaf_flag,2 AS status,'/48000000/48190000' AS path,'park' AS type,19 AS sort_num,53000 AS module_id,2 AS level UNION ALL
SELECT 72190000 AS id,'会议室管理' AS name,72000000 AS parent_id,'meeting-management' AS data_type,1 AS leaf_flag,2 AS status,'/72000000/72190000' AS path,'organization' AS type,19 AS sort_num,53000 AS module_id,2 AS level
)r LEFT JOIN eh_web_menus m ON r.id=m.id
WHERE m.id IS NULL;

-- End by: 张智伟