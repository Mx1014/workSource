-- 能耗升级离线包版本 by  jiarui 20180619
UPDATE eh_version_urls SET download_url = REPLACE(download_url,'1-0-3','1-0-4') WHERE realm_id = (SELECT id FROM eh_version_realm WHERE realm = 'energyManagement' LIMIT 1);
UPDATE eh_version_urls SET  info_url = REPLACE(info_url,'1-0-3','1-0-4') WHERE realm_id = (SELECT id FROM eh_version_realm WHERE realm = 'energyManagement' LIMIT 1);
UPDATE eh_version_urls SET  target_version = '1.0.4' WHERE realm_id = (SELECT id FROM eh_version_realm WHERE realm = 'energyManagement' LIMIT 1);

-- 通用脚本
-- by 刘一麟 20180619
-- issue-30717 新增应用:园区巴士
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16030650', '园区班车', '16030000', NULL, 'bus-guard', '1', '2', '/16000000/16030000/16030650', 'zuolin', '23', '41015', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('45015000', '园区班车', '45000000', NULL, 'bus-guard', '1', '2', '/45000000/45015000', 'park', '15', '41015', '2', 'system', 'module', NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41015', '园区班车', '40000', '/10000/41015', '1', '2', '2', '10', NULL, '{\"isSupportQR\":1,\"isSupportSmart\":0}', '77', NULL, '0', '0', '0', '0', 'community_control');
-- END


-- 通用脚本
-- ADD BY 张智伟
-- issue-28363 会议管理V1.0

INSERT INTO eh_locale_strings(scope,CODE,locale,TEXT)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'meetingErrorCode' AS scope,100000 AS CODE,'zh_CN' AS locale,'该会议室已删除' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100001 AS CODE,'zh_CN' AS locale,'该会议室名称已存在' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100002 AS CODE,'zh_CN' AS locale,'该会议已删除' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100003 AS CODE,'zh_CN' AS locale,'该纪要已删除' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100005 AS CODE,'zh_CN' AS locale,'您无权删除该纪要' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100006 AS CODE,'zh_CN' AS locale,'您无权创建或编辑该纪要' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100007 AS CODE,'zh_CN' AS locale,'您无权编辑该会议' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100008 AS CODE,'zh_CN' AS locale,'该会议室不可用' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100009 AS CODE,'zh_CN' AS locale,'该时间段已被预订' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100010 AS CODE,'zh_CN' AS locale,'该会议已取消' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100011 AS CODE,'zh_CN' AS locale,'该会议已结束' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100012 AS CODE,'zh_CN' AS locale,'您无权查看该会议' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100013 AS CODE,'zh_CN' AS locale,'您无权查看该纪要' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100014 AS CODE,'zh_CN' AS locale,'只能预订未来的会议' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100015 AS CODE,'zh_CN' AS locale,'该会议已开始，无法更改' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100016 AS CODE,'zh_CN' AS locale,'该会议纪要已被其他人创建' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100017 AS CODE,'zh_CN' AS locale,'会议结束后才可以写纪要' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100018 AS CODE,'zh_CN' AS locale,'会议还没有开始不能结束' AS TEXT UNION ALL
SELECT 'meetingErrorCode' AS scope,100019 AS CODE,'zh_CN' AS locale,'已经结束的会议不能被取消' AS TEXT UNION ALL

SELECT 'meetingMessage' AS scope,100001 AS CODE,'zh_CN' AS locale,'您的会议即将开始' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,100002 AS CODE,'zh_CN' AS locale,'您的会议已调整' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,100003 AS CODE,'zh_CN' AS locale,'您的会议已取消' AS TEXT
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;


INSERT INTO eh_locale_templates(scope,CODE,locale,description,TEXT,namespace_id)
SELECT r.scope,r.code,r.locale,r.description,r.text,0
FROM(
SELECT 'meetingMessage' AS scope,1000000 AS CODE,'zh_CN' AS locale,'#发起人#邀请您参加会议' AS description,'${meetingSponsorName}邀请您参加会议' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000001 AS CODE,'zh_CN' AS locale,'#发起人#发布了会议纪要' AS description,'${meetingRecorderName}发布了会议纪要' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000002 AS CODE,'zh_CN' AS locale,'主题：#会议主题#\r\n时间：#会议开始时间#\r\n地点：#会议室名称#' AS description,'主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000003 AS CODE,'zh_CN' AS locale,'邮箱通知正文' AS description,'主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}|发起人：${meetingSponsorName}|参会人：${meetingUserList}' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000004 AS CODE,'zh_CN' AS locale,'会议主题：#会议主题#' AS description,'会议主题：${meetingSubject}' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000005 AS CODE,'zh_CN' AS locale,'邮箱通知正文' AS description,'主题：${meetingSubject}|时间：${meetingBeginTime}|地点：${meetingRoomName}|发起人：${meetingSponsorName}|参会人：${meetingUserList}|详细信息请打开：${appName} APP查看' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000006 AS CODE,'zh_CN' AS locale,'管理员#姓名#删除了会议室' AS description,'管理员${adminContactName}删除了会议室' AS TEXT UNION ALL
SELECT 'meetingMessage' AS scope,1000007 AS CODE,'zh_CN' AS locale,'#发起人#修改了会议纪要' AS description,'${meetingRecorderName}修改了会议纪要' AS TEXT
)r LEFT JOIN eh_locale_templates t ON r.scope=t.scope AND r.code=t.code AND r.locale=t.locale
WHERE t.id IS NULL;


INSERT INTO eh_service_modules(id,NAME,parent_id,path,TYPE,LEVEL,STATUS,create_time,creator_uid,operator_uid,action_type,multiple_flag,module_control_type,default_order)
VALUE(53000,'会议室管理',50000,'/50000/53000',1,2,2,NOW(),0,0,75,0,'org_control',0);

-- 新增菜单配置
INSERT INTO eh_web_menus(id,NAME,parent_id,data_type,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category)
SELECT r.id,r.name,r.parent_id,r.data_type,r.leaf_flag,r.status,r.path,r.type,r.sort_num,r.module_id,r.level,'system','module' FROM(
SELECT 16041900 AS id,'会议室管理' AS NAME,16040000 AS parent_id,'meeting-management' AS data_type,1 AS leaf_flag,2 AS STATUS,'/16000000/16040000/16041900' AS path,'zuolin' AS TYPE,19 AS sort_num,53000 AS module_id,3 AS LEVEL UNION ALL
SELECT 48190000 AS id,'会议室管理' AS NAME,48000000 AS parent_id,'meeting-management' AS data_type,1 AS leaf_flag,2 AS STATUS,'/48000000/48190000' AS path,'park' AS TYPE,19 AS sort_num,53000 AS module_id,2 AS LEVEL UNION ALL
SELECT 72190000 AS id,'会议室管理' AS NAME,72000000 AS parent_id,'meeting-management' AS data_type,1 AS leaf_flag,2 AS STATUS,'/72000000/72190000' AS path,'organization' AS TYPE,19 AS sort_num,53000 AS module_id,2 AS LEVEL
)r LEFT JOIN eh_web_menus m ON r.id=m.id
WHERE m.id IS NULL;

-- End by: 张智伟


-- 考勤4.2 
-- 通用脚本
-- 缺勤改成旷工
UPDATE eh_locale_strings SET TEXT ='旷工' WHERE scope ='punch.status' AND CODE = 3;
-- 重新刷考勤月报数据 只执行一次
DELETE FROM eh_punch_month_reports;
SET @id := 1;
INSERT INTO eh_punch_month_reports(id, punch_month,owner_type,owner_id, STATUS,PROCESS,creator_uid,create_time , update_time ,punch_member_number )  
  SELECT   (@id :=@id +1) id, punch_month,owner_type,owner_id, 1 STATUS,100 PROCESS,creator_uid,create_time,create_time ,COUNT(1) punch_member_number FROM eh_punch_statistics
GROUP BY punch_month,owner_type,owner_id ORDER BY punch_month DESC   ;
-- 考勤4.2  end