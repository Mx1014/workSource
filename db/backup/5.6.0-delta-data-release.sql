-- 通用脚本
-- 物品放行 1.1 修改模块名
-- by shiheng.ma
update eh_service_modules set name = '物品放行' where id = 49200;
update eh_service_module_apps set name = '物品放行' where module_id = 49200;
update eh_web_menus set name = '物品放行' where module_id = 49200;
update eh_acl_privileges set name = '物品放行 全部权限',description = '物品放行 全部权限' where id = 4920049200;

-- 物品放行 1.1 修改工作流模板
-- by shiheng.ma
update eh_locale_templates t set t.description = '物品放行' , t.text = '物品放行' where t.scope = 'relocation' and t.code = 1;

update eh_locale_templates t set t.description = '物品放行工作流申请人显示内容' , t.text = '放行物品：${items} 共${totalNum}件
放行时间：${relocationDate}' where t.scope = 'relocation' and t.code = 2;

update eh_locale_templates t set t.description = '物品放行工作流处理人显示内容' , t.text = '申请人：${requestorName}  企业名称：${requestorEnterpriseName}
放行物品：${items} 共${totalNum}件
放行时间：${relocationDate}' where t.scope = 'relocation' and t.code = 3;

-- 能耗升级离线包版本 by  jiarui 20180619
UPDATE eh_version_urls SET download_url = REPLACE(download_url,'1-0-3','1-0-4') WHERE realm_id = (SELECT id FROM eh_version_realm WHERE realm = 'energyManagement' LIMIT 1);
UPDATE eh_version_urls SET  info_url = REPLACE(info_url,'1-0-3','1-0-4') WHERE realm_id = (SELECT id FROM eh_version_realm WHERE realm = 'energyManagement' LIMIT 1);
UPDATE eh_version_urls SET  target_version = '1.0.4' WHERE realm_id = (SELECT id FROM eh_version_realm WHERE realm = 'energyManagement' LIMIT 1);
-- END


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


-- 通用脚本
-- 审批3.0初始数据, 需先执行schema后执行此sql start by ryan.
UPDATE eh_general_form_templates SET id = 101 WHERE form_name = '日报';
UPDATE eh_general_form_templates SET id = 102 WHERE form_name = '周报';
UPDATE eh_general_form_templates SET id = 103 WHERE form_name = '月报';
UPDATE eh_work_report_templates SET form_template_id = 101 WHERE report_name = '日报';
UPDATE eh_work_report_templates SET form_template_id = 102 WHERE report_name = '周报';
UPDATE eh_work_report_templates SET form_template_id = 103 WHERE report_name = '月报';
UPDATE eh_general_forms SET form_template_id = 101 WHERE form_name = '日报' AND form_attribute = 'DEFAULT';
UPDATE eh_general_forms SET form_template_id = 102 WHERE form_name = '周报' AND form_attribute = 'DEFAULT';
UPDATE eh_general_forms SET form_template_id = 103 WHERE form_name = '月报' AND form_attribute = 'DEFAULT';
DELETE FROM eh_general_form_templates WHERE form_name = '请示单';
DELETE FROM eh_general_forms WHERE form_name = '请示单' AND form_attribute = 'DEFAULT';

INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('1', '0', NULL, '0', '出勤休假', '1', 'DEFAULT', NULL, '1', '2018-04-28 09:00:00');
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('2', '0', NULL, '0', '组织人事', '1', 'DEFAULT', NULL, '1', '2018-04-28 09:00:00');
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('3', '0', NULL, '0', '行政办公', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '1', '2018-04-28 09:00:00');
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('4', '0', NULL, '0', '市场商务', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '1', '2018-04-28 09:00:00');
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('5', '0', NULL, '0', '其他', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRvNU1ERmtNelkzT0RVek1XTmhPV0ppWVdRMU5ESXdPRFJsWmpFM1l6UmxNUQ', '1', '2018-04-28 09:00:00');

UPDATE `eh_enterprise_approval_templates` SET `approval_name`='请假申请', `group_id`='1', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU0ySXdOVGhsWlRNMVpUUmhOVFkwTURnM00yWmlOV0k0WWpKaE5HUTNOdw' WHERE (`id`='1') LIMIT 1;
UPDATE `eh_enterprise_approval_templates` SET `approval_name`='加班申请', `group_id`='1', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU9ESXlOekF4WlROak1UUXdNRE5qTlRFMk5qVmtOelppTVRFM016QmtOdw' WHERE (`id`='2') LIMIT 1;
UPDATE `eh_enterprise_approval_templates` SET `approval_name`='出差申请', `group_id`='1', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRveVpHTm1ZekpqWlRRME5EZzRNamc1WkRBeE16RXdNVGcxT0RNeU5qbGhNUQ' WHERE (`id`='3') LIMIT 1;
UPDATE `eh_enterprise_approval_templates` SET `approval_name`='外出申请', `group_id`='1', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvelpHTm1NemxtTm1SbVlURmhNVFl5Tm1VNE1UUTRZbU16Wm1WallqRTFOZw' WHERE (`id`='4') LIMIT 1;
UPDATE `eh_enterprise_approval_templates` SET `approval_name`='异常申请', `group_id`='1', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNE9XSXpNbVEwWTJJMFkyVXdPVGxqTkRCbFpHWTNPRE5pTlRSak0yUmtNZw' WHERE (`id`='5') LIMIT 1;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('6', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '6', '0', '物品领用申请', '用于办公用品的领用申请', '3', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('7', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '7', '0', '费用报销申请', '用于办公相关费用的报销申请', '3', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('8', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '8', '0', '用章申请', '用于公司用章申请', '3', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('9', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '9', '0', '用车申请', '用于使用公司车辆的申请', '3', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('10', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '10', '0', '合同申请', '用于签订合同申请', '4', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('11', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '11', '0', '付款申请', '用于公司付款申请', '4', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('12', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '12', '0', '备用金申请', '用于备用金的申请', '4', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('13', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '13', '0', '转正申请', '这是系统内置的转正申请，员工可以在手机上发起申请，审批通过后将在生效日期自动转正', '2', 'EMPLOY_APPLICATION', '0', '0', 'cs://1/image/aW1hZ2UvTVRvNE5qUmpZakJpT0dJd09EUmxPVFl4TWprNFpqSXdaalJpWVRNMU5UUmpaZw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('14', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '14', '0', '离职申请', '这是系统内置的离职申请，员工可以在手机上发起申请，审批通过后将在生效日期自动离职', '2', 'DISMISS_APPLICATION', '0', '0', 'cs://1/image/aW1hZ2UvTVRwaE16VXlaV0ZsTURNd1pEQmpaak0wWVRJNE5Ua3pNVE5rTmpNeE16UmtZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');


INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('6', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '物品领用申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入领用说明\",\"fieldDisplayName\":\"领用说明\",\"fieldExtra\":\"{}\",\"fieldName\":\"领用说明\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"物品名称\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"物品名称\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"物品数量\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"物品数量\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"READONLY\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":1,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('7', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '费用报销申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"费用类型\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"差旅费\\\",\\\"通讯费\\\",\\\"市内交通费\\\",\\\"活动经费\\\",\\\"其他\\\"]}\",\"fieldName\":\"费用类型\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入申请事由\",\"fieldDisplayName\":\"申请事由\",\"fieldExtra\":\"{}\",\"fieldName\":\"申请事由\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入费用金额\",\"fieldDisplayName\":\"费用金额\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"费用金额\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入备注说明\",\"fieldDisplayName\":\"备注说明\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注说明\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":1,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('8', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '用章申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入用章事由\",\"fieldDisplayName\":\"用章事由\",\"fieldExtra\":\"{}\",\"fieldName\":\"用章事由\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择印章类型\",\"fieldDisplayName\":\"印章类型\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"公章\\\",\\\"合同章\\\",\\\"法人章\\\",\\\"其他\\\"]}\",\"fieldName\":\"印章类型\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入文件名称\",\"fieldDisplayName\":\"文件名称\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件名称\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入文件份数\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入用章日期\",\"fieldDisplayName\":\"用章日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"用章日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":1,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('9', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '用车申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入用车事由\",\"fieldDisplayName\":\"用车事由\",\"fieldExtra\":\"{}\",\"fieldName\":\"用车事由\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择开始时间\",\"fieldDisplayName\":\"开始时间\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"开始时间\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择结束时间\",\"fieldDisplayName\":\"结束时间\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"结束时间\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入用车天数\",\"fieldDisplayName\":\"用车天数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"用车天数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入用车目的地\",\"fieldDisplayName\":\"用车目的地\",\"fieldExtra\":\"{}\",\"fieldName\":\"用车目的地\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择车辆类型\",\"fieldDisplayName\":\"车辆类型\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"5座\\\",\\\"7座\\\",\\\"11座\\\",\\\"其他\\\"]}\",\"fieldName\":\"车辆类型\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入备注说明\",\"fieldDisplayName\":\"备注说明\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注说明\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('10', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '合同申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入合同名称\",\"fieldDisplayName\":\"合同名称\",\"fieldExtra\":\"{}\",\"fieldName\":\"合同名称\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入合同编号\",\"fieldDisplayName\":\"合同编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"合同编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择签约日期\",\"fieldDisplayName\":\"签约日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"签约日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入我方单位名称\",\"fieldDisplayName\":\"我方单位名称\",\"fieldExtra\":\"{}\",\"fieldName\":\"我方单位名称\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入我方负责人姓名\",\"fieldDisplayName\":\"我方负责人\",\"fieldExtra\":\"{}\",\"fieldName\":\"我方负责人\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入对方单位名称\",\"fieldDisplayName\":\"对方单位名称\",\"fieldExtra\":\"{}\",\"fieldName\":\"对方单位名称\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入对方负责人姓名\",\"fieldDisplayName\":\"对方负责人\",\"fieldExtra\":\"{}\",\"fieldName\":\"对方负责人\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":1,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('11', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '付款申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入付款事由\",\"fieldDisplayName\":\"付款事由\",\"fieldExtra\":\"{}\",\"fieldName\":\"付款事由\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入付款金额\",\"fieldDisplayName\":\"付款金额\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"付款金额\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择付款方式\",\"fieldDisplayName\":\"付款方式\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"银行转账\\\",\\\"现金\\\",\\\"支付宝转账\\\",\\\"微信转账\\\",\\\"支票\\\",\\\"汇票\\\",\\\"电汇\\\",\\\"贷记\\\",\\\"其他\\\"]}\",\"fieldName\":\"付款方式\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择付款日期\",\"fieldDisplayName\":\"付款日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"付款日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入收款人全称\",\"fieldDisplayName\":\"收款人全称\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"收款人全称\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入收款人银行账号\",\"fieldDisplayName\":\"银行账号\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"银行账号\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入开户行\",\"fieldDisplayName\":\"开户行\",\"fieldExtra\":\"{}\",\"fieldName\":\"开户行\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入备注说明\",\"fieldDisplayName\":\"备注说明\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注说明\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":1,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('12', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '备用金申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入申请事由\",\"fieldDisplayName\":\"申请事由\",\"fieldExtra\":\"{}\",\"fieldName\":\"申请事由\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入申请金额\",\"fieldDisplayName\":\"申请金额\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"申请金额\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择使用日期\",\"fieldDisplayName\":\"使用日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"使用日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择归还日期\",\"fieldDisplayName\":\"归还日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"归还日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入备注说明\",\"fieldDisplayName\":\"备注说明\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注说明\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":1,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-05-18 18:42:09', '2018-05-18 18:42:09');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('13', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '转正申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDisplayName\":\"转正组件\",\"fieldExtra\":\"{}\",\"fieldName\":\"转正组件\",\"fieldType\":\"EMPLOY_APPLICATION\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\",\"modifyFlag\":0,\"deleteFlag\":0},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":2,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '0', '2018-06-07 18:57:08', '2018-06-07 18:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('14', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '离职申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDisplayName\":\"离职组件\",\"fieldExtra\":\"{}\",\"fieldName\":\"离职组件\",\"fieldType\":\"DISMISS_APPLICATION\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\",\"modifyFlag\":0,\"deleteFlag\":0},{\"dynamicFlag\":0,\"fieldDisplayName\":\"图片\",\"fieldExtra\":\"{\\\"limitCount\\\":9,\\\"limitPerSize\\\":5242880}\",\"fieldName\":\"图片\",\"fieldType\":\"IMAGE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"IMAGE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"附件\",\"fieldExtra\":\"{\\\"limitCount\\\":2,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"附件\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '0', '2018-06-07 18:57:08', '2018-06-07 18:57:08');

UPDATE `eh_general_approvals` SET `integral_tag1`='5', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU1ERmtNelkzT0RVek1XTmhPV0ppWVdRMU5ESXdPRFJsWmpFM1l6UmxNUQ' WHERE `module_id`='52000';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '请假申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU0ySXdOVGhsWlRNMVpUUmhOVFkwTURnM00yWmlOV0k0WWpKaE5HUTNOdw' WHERE `approval_attribute`='ASK_FOR_LEAVE';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '加班申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU9ESXlOekF4WlROak1UUXdNRE5qTlRFMk5qVmtOelppTVRFM016QmtOdw' WHERE `approval_attribute`='OVERTIME';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '出差申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRveVpHTm1ZekpqWlRRME5EZzRNamc1WkRBeE16RXdNVGcxT0RNeU5qbGhNUQ' WHERE `approval_attribute`='BUSINESS_TRIP';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '外出申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvelpHTm1NemxtTm1SbVlURmhNVFl5Tm1VNE1UUTRZbU16Wm1WallqRTFOZw' WHERE `approval_attribute`='GO_OUT';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '异常申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNE9XSXpNbVEwWTJJMFkyVXdPVGxqTkRCbFpHWTNPRE5pTlRSak0yUmtNZw' WHERE `approval_attribute`='ABNORMAL_PUNCH';

SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '10001', 'zh_CN', '未找到表单');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '10002', 'zh_CN', '重复的名称');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '10003', 'zh_CN', '未找到对应的审批模板');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '10004', 'zh_CN', '工作流未启用');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '10005', 'zh_CN', '表单未设置');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '10006', 'zh_CN', '未选择目标对象');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '20001', 'zh_CN', '此审批未启用，请联系管理员。');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '20002', 'zh_CN', '你不在可见范围，请联系管理员。');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10001', 'zh_CN', '申请人');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10002', 'zh_CN', '审批编号');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10003', 'zh_CN', '申请时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10004', 'zh_CN', '所在部门');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10005', 'zh_CN', '所在岗位');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10011', 'zh_CN', '开始时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '10012', 'zh_CN', '结束时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20001', 'zh_CN', '请假类型');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20002', 'zh_CN', '请假时长');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20011', 'zh_CN', '出差时长');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20021', 'zh_CN', '加班时长');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20031', 'zh_CN', '外出时长');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20041', 'zh_CN', '异常日期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20042', 'zh_CN', '异常班次');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20051', 'zh_CN', '入职日期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20052', 'zh_CN', '申请转正日期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20053', 'zh_CN', '申请转正理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20061', 'zh_CN', '申请离职日期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20062', 'zh_CN', '离职原因');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval', '20063', 'zh_CN', '离职原因备注');

SET @template_id = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '101', 'zh_CN', '操作提示试用期', '${month}个月', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '102', 'zh_CN', '操作提示部门变动', '从 ${oldOrgNames} 调动至 ${newOrgNames}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'enterpriseApproval', '1', 'zh_CN', '人事审批提醒标题-计划', '已存在${operationType}计划', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'enterpriseApproval', '2', 'zh_CN', '人事审批提醒标题-审批', '已存在${approvalName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'enterpriseApproval', '11', 'zh_CN', '人事审批提醒正文-计划', '原定于${operationTime}${operationType}，继续发起申请，将作废原计划，确定继续吗？', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'enterpriseApproval', '12', 'zh_CN', '人事审批提醒正文-审批', '继续发起申请，将撤回原${approvalName}，确定继续吗？', '0');

-- 审批3.0 end by ryan.

-- 通用脚本
-- ADD BY 梁燕龙
-- issue-26754 敏感词过滤功能
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
  VALUES ('sensitiveword.url', '', '敏感词文档url', '0');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
  VALUES ('sensitiveword.fileName', '', '敏感词文档名称', '0');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
  VALUES ('sensitiveword.filePath', 'C:/Users/Administrator/Desktop/', '下载敏感词文档路径', '0');

INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(20030000 ,'敏感词日志',20000000,NULL,'sensitive-word',1,2,'/11000000/20000000/20030000','zuolin',40,NULL,2,'system','module',NULL);
-- END BY 梁燕龙

-- 通用脚本
-- ADD BY 梁燕龙
-- issue-30635 活动V4.0 支持发布后修改
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 11, 'zh_CN', '活动发布后，修改标题', '您参加的活动“${postName}”的主题已被发起方改成“${newPostName}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 12, 'zh_CN', '活动发布后，修改时间', '您参加的活动“${postName}”的时间已被发起方改成“${startTime}~${endTime}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 13, 'zh_CN', '活动发布后，修改地址', '您参加的活动“${postName}”的地点已被发起方改成“${address}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 14, 'zh_CN', '活动发布后，修改标题和时间', '您参加的活动“${postName}”被发起方修改，详情如下：主题被改成“${newPostName}”、时间被改成“${startTime}~${endTime}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 15, 'zh_CN', '活动发布后，修改标题和地址', '您参加的活动“${postName}”被发起方修改，详情如下：主题被改成“${newPostName}”、地点被改成“${address}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 16, 'zh_CN', '活动发布后，修改时间和地址', '您参加的活动“${postName}”被发起方修改，详情如下：时间被改成“${startTime}~${endTime}”、地点被改成“${address}。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 17, 'zh_CN', '活动发布后，修改标题、时间和地点', '您参加的活动“${postName}”被发起方修改，详情如下：主题被改成“${newPostName}”、时间被改成“${startTime}~${endTime}”、地点被改成“${address}”。');


INSERT INTO eh_locale_strings ( `scope`, `code`, `locale`, `text`)
	VALUES ('activity', 26, 'zh_CN', '活动被修改！！');
-- END BY 梁燕龙

-- 通用脚本
-- ADD BY 黄良铭
-- issue-30013 初始化短信白名单配置项
-- 初始化配置项表“是否只读”字段为“是”，值为1
UPDATE eh_configurations s SET s.is_readonly = 1 ;

INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(30000000 ,'后台配置项',11000000,NULL,'server-configuration',1,2,'/11000000/30000000','zuolin',50,60100,3,'system','module',NULL);
-- END BY 黄良铭

-- 通用脚本  
-- ADD BY huangmingbo
-- ISSUE-31190 31378 
SET @ns_id = 0;
SET @jump_id = IFNULL((SELECT MAX(id) FROM `eh_service_alliance_jump_module`), 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '停车缴费', 40800, 'zl://parking/query?displayName=停车缴费', NULL, 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '物业报修', 20100, NULL, '{"taskCategoryId":6,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '物品放行', 49200, NULL, '{"prefix":"/goods-move/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '园区入驻', 40100, NULL, '{"skipRoute":"zl://park-service/settle"}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '投诉建议', 20100, NULL, '{"taskCategoryId":9,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);

-- END

-- 通用脚本
-- 企业客户管理ERROR code  by jiarui
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10022', 'zh_CN', '为必填项');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10023', 'zh_CN', '需按照给定类型填写');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10024', 'zh_CN', '格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10025', 'zh_CN', '未知格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10026', 'zh_CN', '格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10027', 'zh_CN', '格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10028', 'zh_CN', '不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10029', 'zh_CN', '数字格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10030', 'zh_CN', '日期格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'customer', '10031', 'zh_CN', '手机号系统中不存在');


-- 跟进信息增加字段  by jiarui

set @fId = (SELECT  max(id) from eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fId:=@fId+1), 'enterprise_customer', 'visitTimeLength', '跟进时长(小时)', 'BigDecimal', '19', '/19/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"BigDecimal\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fId:=@fId+1), 'enterprise_customer', 'visitPersonName', '拜访人', 'String', '19', '/19/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fId:=@fId+1), 'enterprise_customer', 'contactPhone', '联系电话', 'String', '19', '/19/', '0', NULL, '2', '1',now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- organization owner  by jiarui start by jiarui 
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18002', 'zh_CN', '必填项未填写');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18003', 'zh_CN', '楼栋门牌不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18004', 'zh_CN', '时间格式不正确');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18008', 'zh_CN', '时间无效');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18005', 'zh_CN', '是否在户口格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18006', 'zh_CN', '性别内容格式错误');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'pm', '18007', 'zh_CN', '客户类型字段内容系统不存在');
-- end by jiarui

-- 通用脚本  
-- ADD BY 丁建民 
-- #30490 资产管理V2.9（产品功能）
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'address', '20011', 'zh_CN', '门牌状态不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'address', '20012', 'zh_CN', '门牌朝向输入过长');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'address', '20013', 'zh_CN', '门牌状态输入格式不正确');

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10013', 'zh_CN', '项目名称不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10014', 'zh_CN', '项目地址超过指定长度');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10015', 'zh_CN', '省市区不能为空');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10016', 'zh_CN', '项目名称已存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '20006', 'zh_CN', '面积格式错误');
-- END BY 丁建民  


-- 深圳湾适用脚本[999966]
-- ADD by 黄良铭 20180615
-- #issue-31670  深圳湾携程用户对接

SET @c_id = (SELECT MAX(id) FROM eh_configurations);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.AppKey', 'obk_Shenzhenwan','携程方提供的接入账号','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.AppSecurity', 'obk_Shenzhenwan','携程方提供的接入密码','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.Appid', 'Shenzhenwan','携程方提供的公司ID','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.CorpPayType', 'public','public（因公）/private（因私）','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.InitPage', 'Home','登录携程成功后的第一个页面','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.Callback', '','返回到客户页面的URL','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.getTicketURL', 'https://ct.ctrip.com/corpservice/authorize/getticket','获取Ticket的URL','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.signInfoURL', 'https://ct.ctrip.com/m/SingleSignOn/H5SignInfo','单点登录H5SignInfo 的URL','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.OnError', 'ErrorCode','错误处理方式','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.batch.Logon_Appid', 'obk_Shenzhenwan','公司登陆ID(obk账户名)，由携程方提供','999966','',null);


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.batch.production.getTicketURL', 'https://ct.ctrip.com/SwitchAPI/Order/Ticket','获取Ticket生产环境服务地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.batch.test.getTicketURL', 'https://cta.fat.ctripqa.com/SwitchAPI/Order/Ticket','获取Ticket测试环境服务地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.restful.production.batchURL', 'https://ct.ctrip.com/corpservice/CorpCustService/SaveCorpCustInfoList','人事信息批量更新生产环境服务Restful地址','999966','',null);


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.soap.production.batchURL', 'https://ct.ctrip.com/corpservice/CorpCustService.asmx','人事信息批量更新生产环境服务SOAP地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.restful.test.batchURL', 'https://cta.fat.ctripqa.com/corpservice/CorpCustService/SaveCorpCustInfoList','人事信息批量更新测试环境服务Restful地址','999966','',null);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.soap.test.batchURL', 'https://cta.fat.ctripqa.com/corpservice/CorpCustService.asmx','人事信息批量更新测试环境服务SOAP地址','999966','',null);


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ,`is_readonly`) 
VALUES (@c_id:= @c_id +1, 'ct.isOpenedCardURL', 'https://ct.ctrip.com/corpservice/OpenCard/IsOpenedCard?type=json','判断员工是否开卡','999966','',null);
-- END

-- 通用脚本  
-- ADD BY 丁建民 
-- #28874 合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同）
UPDATE `eh_service_modules` SET `multiple_flag` = 1 WHERE `id` = 20400;
UPDATE `eh_service_modules` SET `instance_config` = NULL WHERE `id` = 20400;

-- 增加付款合同菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES
  ('16051300', '付款合同', '16050000', NULL, 'payment-contract', '1', '2', '/16000000/16050000/16051300', 'zuolin', '13', '21200', '3', 'system', 'module', NULL);

-- 添加付款合同应用
UPDATE eh_service_modules SET parent_id='110000',path='/110000/21215',`level`='2',default_order='100',action_type='13',module_control_type='community_control' WHERE id='21215';

-- 合同管理点击加号的出现收款合同付款合同分开：
UPDATE eh_var_field_groups SET module_name='paymentContract' WHERE  path like '%/30%';

-- 设置合同为多入口
UPDATE eh_service_modules SET action_type=13,multiple_flag=1 WHERE NAME='合同管理';

-- App请求地址配置
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'contract.app.url', '${home.url}/property-management/build/index.html?hideNavigationBar=1&categoryId=${categoryId}#/contract#sign_suffix', 'contract app url', '0', NULL);

-- 更新企业管理客户级别其他--》历史客户
UPDATE eh_var_field_items SET display_name='历史客户' WHERE module_name='enterprise_customer' and field_id=5 and display_name='其他';
-- 界面显示历史客户
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_var_field_item_scopes`),0);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES 
(@id:=@id+1, '0', 'enterprise_customer', '5', '7', '历史客户', '7', '2', '1', '2018-06-08 17:37:13', NULL, NULL, NULL, NULL, NULL);


-- 合同基础参数配置 工作流配置 权限
SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220');
SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0;
-- 对接第三方 权限
SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99);
-- 免租期字段删除 权限
SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays';
-- 客户管理 同步客户权限
SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104;

-- 合同基础参数配置 工作流配置 权限
DELETE FROM EH_SERVICE_MODULES WHERE id IN(SELECT id FROM (SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220')) sm);
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0) smp);
-- 对接第三方 权限
DELETE FROM EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99))smef);
-- 免租期字段删除 权限
DELETE FROM EH_VAR_FIELDS WHERE id IN(SELECT id FROM (SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays')vf);
-- --客户管理 同步客户权限
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104)smp);

-- 更新合同列表为收款合同
UPDATE EH_SERVICE_MODULES SET `name`='收款合同' WHERE `level`=3 and parent_id=21200 and path='/110000/21200/21210';

-- 更新合同列表为付款合同 下的相关权限
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '签约、修改' ,default_order=1 WHERE module_id = 21210 AND privilege_id = 21201;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '发起审批' ,default_order=2 WHERE module_id = 21210 AND privilege_id = 21202;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '删除' ,default_order=3 WHERE module_id = 21210 AND privilege_id = 21204;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '作废' ,default_order=4 WHERE module_id = 21210 AND privilege_id = 21205;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '入场' ,default_order=5 WHERE module_id = 21210 AND privilege_id = 21206;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '查看' ,default_order=0 WHERE module_id = 21210 AND privilege_id = 21207;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '续约' ,default_order=6 WHERE module_id = 21210 AND privilege_id = 21208;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '变更' ,default_order=7 WHERE module_id = 21210 AND privilege_id = 21209;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '退约' ,default_order=8 WHERE module_id = 21210 AND privilege_id = 21214;

-- 更新付款合同下的相关权限显示
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '新增' ,default_order=5 WHERE module_id = 21215 AND privilege_id = 21215;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '签约、发起审批',default_order=1 WHERE module_id = 21215 AND privilege_id = 21216;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '修改' ,default_order=2 WHERE module_id = 21215 AND privilege_id = 21217;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '删除' ,default_order=3 WHERE module_id = 21215 AND privilege_id = 21218;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '作废' ,default_order=4 WHERE module_id = 21215 AND privilege_id = 21219;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '查看' ,default_order=0 WHERE module_id = 21215 AND privilege_id = 21220;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '续约' ,default_order=6 WHERE module_id = 21215 AND privilege_id = 21221;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '变更' ,default_order=7 WHERE module_id = 21215 AND privilege_id = 21222;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '退约' ,default_order=8 WHERE module_id = 21215 AND privilege_id = 21223;

-- END BY 丁建民 

-- 通用脚本  
-- ADD BY 丁建民 
-- #28874 合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同） 合同旧数据迁移

-- 查询发布过包含合同管理的域空间
SELECT * from eh_service_module_apps where module_id='21200' group by namespace_id; -- 查询发布过包含合同管理的域空间 

-- 生成categoryid
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1001, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '2', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1002, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '11', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1003, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999944', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1004, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999945', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1005, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999946', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1006, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999947', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1007, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999948', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1008, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999949', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1009, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999950', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1010, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999952', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1011, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999954', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1012, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999956', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1013, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999957', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1014, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999958', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1015, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999961', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1016, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999969', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1017, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999970', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1018, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999971', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1019, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999972', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1020, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999980', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1021, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999983', NULL, NULL, '0');
-- INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1022, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999992', NULL, NULL, '0');


-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1001,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1001' WHERE module_id='21200' and namespace_id='2';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1002,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1002' WHERE module_id='21200' and namespace_id='11';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1003,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1003' WHERE module_id='21200' and namespace_id='999944';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1004,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1004' WHERE module_id='21200' and namespace_id='999945';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1005,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1005' WHERE module_id='21200' and namespace_id='999946';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1006,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1006' WHERE module_id='21200' and namespace_id='999947';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1007,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1007' WHERE module_id='21200' and namespace_id='999948';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1008,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1008' WHERE module_id='21200' and namespace_id='999949';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1009,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1009' WHERE module_id='21200' and namespace_id='999950';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1010,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1010' WHERE module_id='21200' and namespace_id='999952';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1011,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1011' WHERE module_id='21200' and namespace_id='999954';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1012,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1012' WHERE module_id='21200' and namespace_id='999956';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1013,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1013' WHERE module_id='21200' and namespace_id='999957';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1014,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1014' WHERE module_id='21200' and namespace_id='999958';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1015,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1015' WHERE module_id='21200' and namespace_id='999961';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1016,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1016' WHERE module_id='21200' and namespace_id='999969';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1017,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1017' WHERE module_id='21200' and namespace_id='999970';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1018,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1018' WHERE module_id='21200' and namespace_id='999971';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1019,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1019' WHERE module_id='21200' and namespace_id='999972';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1020,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1020' WHERE module_id='21200' and namespace_id='999980';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1021,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1021' WHERE module_id='21200' and namespace_id='999983';
-- UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1022,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1022' WHERE module_id='21200' and namespace_id='999992';

-- 更新旧合同的categoryid
UPDATE `eh_contracts` SET `category_id`='1001' WHERE namespace_id='2';
UPDATE `eh_contracts` SET `category_id`='1002' WHERE namespace_id='11';
UPDATE `eh_contracts` SET `category_id`='1003' WHERE namespace_id='999944';
UPDATE `eh_contracts` SET `category_id`='1004' WHERE namespace_id='999945';
UPDATE `eh_contracts` SET `category_id`='1005' WHERE namespace_id='999946';
UPDATE `eh_contracts` SET `category_id`='1006' WHERE namespace_id='999947';
UPDATE `eh_contracts` SET `category_id`='1007' WHERE namespace_id='999948';
UPDATE `eh_contracts` SET `category_id`='1008' WHERE namespace_id='999949';
UPDATE `eh_contracts` SET `category_id`='1009' WHERE namespace_id='999950';
UPDATE `eh_contracts` SET `category_id`='1010' WHERE namespace_id='999952';
UPDATE `eh_contracts` SET `category_id`='1011' WHERE namespace_id='999954';
UPDATE `eh_contracts` SET `category_id`='1012' WHERE namespace_id='999956';
UPDATE `eh_contracts` SET `category_id`='1013' WHERE namespace_id='999957';
UPDATE `eh_contracts` SET `category_id`='1014' WHERE namespace_id='999958';
UPDATE `eh_contracts` SET `category_id`='1015' WHERE namespace_id='999961';
UPDATE `eh_contracts` SET `category_id`='1016' WHERE namespace_id='999969';
UPDATE `eh_contracts` SET `category_id`='1017' WHERE namespace_id='999970';
UPDATE `eh_contracts` SET `category_id`='1018' WHERE namespace_id='999971';
UPDATE `eh_contracts` SET `category_id`='1019' WHERE namespace_id='999972';
UPDATE `eh_contracts` SET `category_id`='1020' WHERE namespace_id='999980';
UPDATE `eh_contracts` SET `category_id`='1021' WHERE namespace_id='999983';
-- UPDATE `eh_contracts` SET `category_id`='1022' WHERE namespace_id='999992';

-- 更新合同基础参数设置
UPDATE `eh_contract_params` SET `category_id`='1001' WHERE namespace_id='2';
UPDATE `eh_contract_params` SET `category_id`='1002' WHERE namespace_id='11';
UPDATE `eh_contract_params` SET `category_id`='1003' WHERE namespace_id='999944';
UPDATE `eh_contract_params` SET `category_id`='1004' WHERE namespace_id='999945';
UPDATE `eh_contract_params` SET `category_id`='1005' WHERE namespace_id='999946';
UPDATE `eh_contract_params` SET `category_id`='1006' WHERE namespace_id='999947';
UPDATE `eh_contract_params` SET `category_id`='1007' WHERE namespace_id='999948';
UPDATE `eh_contract_params` SET `category_id`='1008' WHERE namespace_id='999949';
UPDATE `eh_contract_params` SET `category_id`='1009' WHERE namespace_id='999950';
UPDATE `eh_contract_params` SET `category_id`='1010' WHERE namespace_id='999952';
UPDATE `eh_contract_params` SET `category_id`='1011' WHERE namespace_id='999954';
UPDATE `eh_contract_params` SET `category_id`='1012' WHERE namespace_id='999956';
UPDATE `eh_contract_params` SET `category_id`='1013' WHERE namespace_id='999957';
UPDATE `eh_contract_params` SET `category_id`='1014' WHERE namespace_id='999958';
UPDATE `eh_contract_params` SET `category_id`='1015' WHERE namespace_id='999961';
UPDATE `eh_contract_params` SET `category_id`='1016' WHERE namespace_id='999969';
UPDATE `eh_contract_params` SET `category_id`='1017' WHERE namespace_id='999970';
UPDATE `eh_contract_params` SET `category_id`='1018' WHERE namespace_id='999971';
UPDATE `eh_contract_params` SET `category_id`='1019' WHERE namespace_id='999972';
UPDATE `eh_contract_params` SET `category_id`='1020' WHERE namespace_id='999980';
UPDATE `eh_contract_params` SET `category_id`='1021' WHERE namespace_id='999983';
-- UPDATE `eh_contract_params` SET `category_id`='1022' WHERE namespace_id='999992';

-- 更新表单 ，categoryid为空会用原来的表单，
UPDATE `eh_var_field_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=2; 
UPDATE `eh_var_field_scopes` SET `category_id`='1002' WHERE module_name='contract' and namespace_id=11; 
UPDATE `eh_var_field_scopes` SET `category_id`='1003' WHERE module_name='contract' and namespace_id=999944; 
UPDATE `eh_var_field_scopes` SET `category_id`='1004' WHERE module_name='contract' and namespace_id=999945; 
UPDATE `eh_var_field_scopes` SET `category_id`='1005' WHERE module_name='contract' and namespace_id=999946; 
UPDATE `eh_var_field_scopes` SET `category_id`='1006' WHERE module_name='contract' and namespace_id=999947; 
UPDATE `eh_var_field_scopes` SET `category_id`='1007' WHERE module_name='contract' and namespace_id=999948; 
UPDATE `eh_var_field_scopes` SET `category_id`='1008' WHERE module_name='contract' and namespace_id=999949; 
UPDATE `eh_var_field_scopes` SET `category_id`='1009' WHERE module_name='contract' and namespace_id=999950; 
UPDATE `eh_var_field_scopes` SET `category_id`='1010' WHERE module_name='contract' and namespace_id=999952; 
UPDATE `eh_var_field_scopes` SET `category_id`='1011' WHERE module_name='contract' and namespace_id=999954; 
UPDATE `eh_var_field_scopes` SET `category_id`='1012' WHERE module_name='contract' and namespace_id=999956; 
UPDATE `eh_var_field_scopes` SET `category_id`='1013' WHERE module_name='contract' and namespace_id=999957; 
UPDATE `eh_var_field_scopes` SET `category_id`='1014' WHERE module_name='contract' and namespace_id=999958; 
UPDATE `eh_var_field_scopes` SET `category_id`='1015' WHERE module_name='contract' and namespace_id=999961; 
UPDATE `eh_var_field_scopes` SET `category_id`='1016' WHERE module_name='contract' and namespace_id=999969; 
UPDATE `eh_var_field_scopes` SET `category_id`='1017' WHERE module_name='contract' and namespace_id=999970; 
UPDATE `eh_var_field_scopes` SET `category_id`='1018' WHERE module_name='contract' and namespace_id=999971; 
UPDATE `eh_var_field_scopes` SET `category_id`='1019' WHERE module_name='contract' and namespace_id=999972; 
UPDATE `eh_var_field_scopes` SET `category_id`='1020' WHERE module_name='contract' and namespace_id=999980; 
UPDATE `eh_var_field_scopes` SET `category_id`='1021' WHERE module_name='contract' and namespace_id=999983; 
-- UPDATE `eh_var_field_scopes` SET `category_id`='1022' WHERE module_name='contract' and namespace_id=999992; 

UPDATE `eh_var_field_item_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=2;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1002' WHERE module_name='contract' and namespace_id=11;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1003' WHERE module_name='contract' and namespace_id=999944;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1004' WHERE module_name='contract' and namespace_id=999945;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1005' WHERE module_name='contract' and namespace_id=999946;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1006' WHERE module_name='contract' and namespace_id=999947;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1007' WHERE module_name='contract' and namespace_id=999948;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1008' WHERE module_name='contract' and namespace_id=999949;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1009' WHERE module_name='contract' and namespace_id=999950;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1010' WHERE module_name='contract' and namespace_id=999952;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1011' WHERE module_name='contract' and namespace_id=999954;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1012' WHERE module_name='contract' and namespace_id=999956;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1013' WHERE module_name='contract' and namespace_id=999957;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1014' WHERE module_name='contract' and namespace_id=999958;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1015' WHERE module_name='contract' and namespace_id=999961;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1016' WHERE module_name='contract' and namespace_id=999969;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1017' WHERE module_name='contract' and namespace_id=999970;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1018' WHERE module_name='contract' and namespace_id=999971;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1019' WHERE module_name='contract' and namespace_id=999972;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1020' WHERE module_name='contract' and namespace_id=999980;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1021' WHERE module_name='contract' and namespace_id=999983;
-- UPDATE `eh_var_field_item_scopes` SET `category_id`='1022' WHERE module_name='contract' and namespace_id=999992;

UPDATE `eh_var_field_group_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=2;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1002' WHERE module_name='contract' and namespace_id=11;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1003' WHERE module_name='contract' and namespace_id=999944;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1004' WHERE module_name='contract' and namespace_id=999945;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1005' WHERE module_name='contract' and namespace_id=999946;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1006' WHERE module_name='contract' and namespace_id=999947;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1007' WHERE module_name='contract' and namespace_id=999948;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1008' WHERE module_name='contract' and namespace_id=999949;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1009' WHERE module_name='contract' and namespace_id=999950;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1010' WHERE module_name='contract' and namespace_id=999952;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1011' WHERE module_name='contract' and namespace_id=999954;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1012' WHERE module_name='contract' and namespace_id=999956;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1013' WHERE module_name='contract' and namespace_id=999957;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1014' WHERE module_name='contract' and namespace_id=999958;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1015' WHERE module_name='contract' and namespace_id=999961;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1016' WHERE module_name='contract' and namespace_id=999969;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1017' WHERE module_name='contract' and namespace_id=999970;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1018' WHERE module_name='contract' and namespace_id=999971;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1019' WHERE module_name='contract' and namespace_id=999972;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1020' WHERE module_name='contract' and namespace_id=999980;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1021' WHERE module_name='contract' and namespace_id=999983;
-- UPDATE `eh_var_field_group_scopes` SET `category_id`='1022' WHERE module_name='contract' and namespace_id=999992;

-- 更新工作流相关的表
UPDATE `eh_flows` SET `owner_id`='1001' WHERE  module_id='21200' AND namespace_id=2;
UPDATE `eh_flows` SET `owner_id`='1002' WHERE  module_id='21200' AND namespace_id=11;
UPDATE `eh_flows` SET `owner_id`='1003' WHERE  module_id='21200' AND namespace_id=999944;
UPDATE `eh_flows` SET `owner_id`='1004' WHERE  module_id='21200' AND namespace_id=999945;
UPDATE `eh_flows` SET `owner_id`='1005' WHERE  module_id='21200' AND namespace_id=999946;
UPDATE `eh_flows` SET `owner_id`='1006' WHERE  module_id='21200' AND namespace_id=999947;
UPDATE `eh_flows` SET `owner_id`='1007' WHERE  module_id='21200' AND namespace_id=999948;
UPDATE `eh_flows` SET `owner_id`='1008' WHERE  module_id='21200' AND namespace_id=999949;
UPDATE `eh_flows` SET `owner_id`='1009' WHERE  module_id='21200' AND namespace_id=999950;
UPDATE `eh_flows` SET `owner_id`='1010' WHERE  module_id='21200' AND namespace_id=999952;
UPDATE `eh_flows` SET `owner_id`='1011' WHERE  module_id='21200' AND namespace_id=999954;
UPDATE `eh_flows` SET `owner_id`='1012' WHERE  module_id='21200' AND namespace_id=999956;
UPDATE `eh_flows` SET `owner_id`='1013' WHERE  module_id='21200' AND namespace_id=999957;
UPDATE `eh_flows` SET `owner_id`='1014' WHERE  module_id='21200' AND namespace_id=999958;
UPDATE `eh_flows` SET `owner_id`='1015' WHERE  module_id='21200' AND namespace_id=999961;
UPDATE `eh_flows` SET `owner_id`='1016' WHERE  module_id='21200' AND namespace_id=999969;
UPDATE `eh_flows` SET `owner_id`='1017' WHERE  module_id='21200' AND namespace_id=999970;
UPDATE `eh_flows` SET `owner_id`='1018' WHERE  module_id='21200' AND namespace_id=999971;
UPDATE `eh_flows` SET `owner_id`='1019' WHERE  module_id='21200' AND namespace_id=999972;
UPDATE `eh_flows` SET `owner_id`='1020' WHERE  module_id='21200' AND namespace_id=999980;
UPDATE `eh_flows` SET `owner_id`='1021' WHERE  module_id='21200' AND namespace_id=999983;
-- UPDATE `eh_flows` SET `owner_id`='1022' WHERE  module_id='21200' AND namespace_id=999992;

UPDATE `eh_flow_cases` SET `owner_id`='1001' WHERE module_id='21200' AND namespace_id=2;
UPDATE `eh_flow_cases` SET `owner_id`='1002' WHERE module_id='21200' AND namespace_id=11;
UPDATE `eh_flow_cases` SET `owner_id`='1003' WHERE module_id='21200' AND namespace_id=999944;
UPDATE `eh_flow_cases` SET `owner_id`='1004' WHERE module_id='21200' AND namespace_id=999945;
UPDATE `eh_flow_cases` SET `owner_id`='1005' WHERE module_id='21200' AND namespace_id=999946;
UPDATE `eh_flow_cases` SET `owner_id`='1006' WHERE module_id='21200' AND namespace_id=999947;
UPDATE `eh_flow_cases` SET `owner_id`='1007' WHERE module_id='21200' AND namespace_id=999948;
UPDATE `eh_flow_cases` SET `owner_id`='1008' WHERE module_id='21200' AND namespace_id=999949;
UPDATE `eh_flow_cases` SET `owner_id`='1009' WHERE module_id='21200' AND namespace_id=999950;
UPDATE `eh_flow_cases` SET `owner_id`='1010' WHERE module_id='21200' AND namespace_id=999952;
UPDATE `eh_flow_cases` SET `owner_id`='1011' WHERE module_id='21200' AND namespace_id=999954;
UPDATE `eh_flow_cases` SET `owner_id`='1012' WHERE module_id='21200' AND namespace_id=999956;
UPDATE `eh_flow_cases` SET `owner_id`='1013' WHERE module_id='21200' AND namespace_id=999957;
UPDATE `eh_flow_cases` SET `owner_id`='1014' WHERE module_id='21200' AND namespace_id=999958;
UPDATE `eh_flow_cases` SET `owner_id`='1015' WHERE module_id='21200' AND namespace_id=999961;
UPDATE `eh_flow_cases` SET `owner_id`='1016' WHERE module_id='21200' AND namespace_id=999969;
UPDATE `eh_flow_cases` SET `owner_id`='1017' WHERE module_id='21200' AND namespace_id=999970;
UPDATE `eh_flow_cases` SET `owner_id`='1018' WHERE module_id='21200' AND namespace_id=999971;
UPDATE `eh_flow_cases` SET `owner_id`='1019' WHERE module_id='21200' AND namespace_id=999972;
UPDATE `eh_flow_cases` SET `owner_id`='1020' WHERE module_id='21200' AND namespace_id=999980;
UPDATE `eh_flow_cases` SET `owner_id`='1021' WHERE module_id='21200' AND namespace_id=999983;
-- UPDATE `eh_flow_cases` SET `owner_id`='1022' WHERE module_id='21200' AND namespace_id=999992;


-- 插入合同与缴费关系。



-- END BY 丁建民 




-- 通用脚本  
-- ADD BY 杨崇鑫
-- #28874 物业缴费（多应用） 产品功能  缴费旧数据迁移

-- 查询发布过包含物业缴费的域空间
SELECT * from eh_service_module_apps where module_id='20400' group by namespace_id;

-- 生成categoryid
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1001, 1001, UTC_TIMESTAMP(), 1, 2, '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1002, 1002, UTC_TIMESTAMP(), 1, 11, '{\"categoryId\":1002}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1003, 1003, UTC_TIMESTAMP(), 1, 999944, '{\"categoryId\":1003}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1004, 1004, UTC_TIMESTAMP(), 1, 999945, '{\"categoryId\":1004}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1005, 1005, UTC_TIMESTAMP(), 1, 999946, '{\"categoryId\":1005}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1006, 1006, UTC_TIMESTAMP(), 1, 999947, '{\"categoryId\":1006}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1007, 1007, UTC_TIMESTAMP(), 1, 999948, '{\"categoryId\":1007}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1008, 1008, UTC_TIMESTAMP(), 1, 999949, '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1008}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1009, 1009, UTC_TIMESTAMP(), 1, 999950, '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1009}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1010, 1010, UTC_TIMESTAMP(), 1, 999951, '{\"categoryId\":1010}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1011, 1011, UTC_TIMESTAMP(), 1, 999952, '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1011}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1012, 1012, UTC_TIMESTAMP(), 1, 999953, '{\"categoryId\":1012}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1013, 1013, UTC_TIMESTAMP(), 1, 999954, '{\"categoryId\":1013}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1014, 1014, UTC_TIMESTAMP(), 1, 999956, '{\"categoryId\":1014}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1015, 1015, UTC_TIMESTAMP(), 1, 999957, '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1015}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1016, 1016, UTC_TIMESTAMP(), 1, 999958, '{\"categoryId\":1016}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1017, 1017, UTC_TIMESTAMP(), 1, 999961, '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1017}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1018, 1018, UTC_TIMESTAMP(), 1, 999962, '{\"categoryId\":1018}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1019, 1019, UTC_TIMESTAMP(), 1, 999967, '{\"categoryId\":1019}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1020, 1020, UTC_TIMESTAMP(), 1, 999969, '{\"categoryId\":1020}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1021, 1021, UTC_TIMESTAMP(), 1, 999970, '{\"url\":\"https://core.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1021}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1022, 1022, UTC_TIMESTAMP(), 1, 999971, '{\"url\":\"https://core.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1022}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1023, 1023, UTC_TIMESTAMP(), 1, 999972, '{\"categoryId\":1023}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1024, 1024, UTC_TIMESTAMP(), 1, 999975, '{\"categoryId\":1024}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1025, 1025, UTC_TIMESTAMP(), 1, 999980, '{\"categoryId\":1025}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1026, 1026, UTC_TIMESTAMP(), 1, 999983, '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1026}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1027, 1027, UTC_TIMESTAMP(), 1, 999992, '{\"categoryId\":1027}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1028, 1028, UTC_TIMESTAMP(), 1, 999993, '{\"categoryId\":1028}');

-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}' 
 WHERE module_id='20400' and namespace_id='2';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1002}' 
 WHERE module_id='20400' and namespace_id='11';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1003}' 
 WHERE module_id='20400' and namespace_id='999944';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1004}' 
 WHERE module_id='20400' and namespace_id='999945';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1005}' 
 WHERE module_id='20400' and namespace_id='999946';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1006}' 
 WHERE module_id='20400' and namespace_id='999947';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1007}' 
 WHERE module_id='20400' and namespace_id='999948';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1008}' 
 WHERE module_id='20400' and namespace_id='999949';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1009}' 
 WHERE module_id='20400' and namespace_id='999950';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1010}' 
 WHERE module_id='20400' and namespace_id='999951';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1011}' 
 WHERE module_id='20400' and namespace_id='999952';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1012}' 
 WHERE module_id='20400' and namespace_id='999953';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1013}' 
 WHERE module_id='20400' and namespace_id='999954';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1014}' 
 WHERE module_id='20400' and namespace_id='999956';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1015}' 
 WHERE module_id='20400' and namespace_id='999957';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1016}' 
 WHERE module_id='20400' and namespace_id='999958';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1017}' 
 WHERE module_id='20400' and namespace_id='999961';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1018}' 
 WHERE module_id='20400' and namespace_id='999962';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1019}' 
 WHERE module_id='20400' and namespace_id='999967';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1020}' 
 WHERE module_id='20400' and namespace_id='999969';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1021}' 
 WHERE module_id='20400' and namespace_id='999970';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1022}' 
 WHERE module_id='20400' and namespace_id='999971';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1023}' 
 WHERE module_id='20400' and namespace_id='999972';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1024}' 
 WHERE module_id='20400' and namespace_id='999975';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1025}' 
 WHERE module_id='20400' and namespace_id='999980';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1026}' 
 WHERE module_id='20400' and namespace_id='999983';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1027}' 
 WHERE module_id='20400' and namespace_id='999992';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1028}' 
 WHERE module_id='20400' and namespace_id='999993';

-- 更新eh_payment_bills表的categoryid
update eh_payment_bills set category_id=1001  where namespace_id=2;
update eh_payment_bills set category_id=1002  where namespace_id=11;
update eh_payment_bills set category_id=1003  where namespace_id=999944;
update eh_payment_bills set category_id=1004  where namespace_id=999945;
update eh_payment_bills set category_id=1005  where namespace_id=999946;
update eh_payment_bills set category_id=1006  where namespace_id=999947;
update eh_payment_bills set category_id=1007  where namespace_id=999948;
update eh_payment_bills set category_id=1008  where namespace_id=999949;
update eh_payment_bills set category_id=1009  where namespace_id=999950;
update eh_payment_bills set category_id=1010  where namespace_id=999951;
update eh_payment_bills set category_id=1011  where namespace_id=999952;
update eh_payment_bills set category_id=1012  where namespace_id=999953;
update eh_payment_bills set category_id=1013  where namespace_id=999954;
update eh_payment_bills set category_id=1014  where namespace_id=999956;
update eh_payment_bills set category_id=1015  where namespace_id=999957;
update eh_payment_bills set category_id=1016  where namespace_id=999958;
update eh_payment_bills set category_id=1017  where namespace_id=999961;
update eh_payment_bills set category_id=1018  where namespace_id=999962;
update eh_payment_bills set category_id=1019  where namespace_id=999967;
update eh_payment_bills set category_id=1020  where namespace_id=999969;
update eh_payment_bills set category_id=1021  where namespace_id=999970;
update eh_payment_bills set category_id=1022  where namespace_id=999971;
update eh_payment_bills set category_id=1023  where namespace_id=999972;
update eh_payment_bills set category_id=1024  where namespace_id=999975;
update eh_payment_bills set category_id=1025  where namespace_id=999980;
update eh_payment_bills set category_id=1026  where namespace_id=999983;
update eh_payment_bills set category_id=1027  where namespace_id=999992;
update eh_payment_bills set category_id=1028  where namespace_id=999993;

-- 更新 eh_payment_notice_config 表的categoryid
update eh_payment_notice_config set category_id=1001  where namespace_id=2;
update eh_payment_notice_config set category_id=1002  where namespace_id=11;
update eh_payment_notice_config set category_id=1003  where namespace_id=999944;
update eh_payment_notice_config set category_id=1004  where namespace_id=999945;
update eh_payment_notice_config set category_id=1005  where namespace_id=999946;
update eh_payment_notice_config set category_id=1006  where namespace_id=999947;
update eh_payment_notice_config set category_id=1007  where namespace_id=999948;
update eh_payment_notice_config set category_id=1008  where namespace_id=999949;
update eh_payment_notice_config set category_id=1009  where namespace_id=999950;
update eh_payment_notice_config set category_id=1010  where namespace_id=999951;
update eh_payment_notice_config set category_id=1011  where namespace_id=999952;
update eh_payment_notice_config set category_id=1012  where namespace_id=999953;
update eh_payment_notice_config set category_id=1013  where namespace_id=999954;
update eh_payment_notice_config set category_id=1014  where namespace_id=999956;
update eh_payment_notice_config set category_id=1015  where namespace_id=999957;
update eh_payment_notice_config set category_id=1016  where namespace_id=999958;
update eh_payment_notice_config set category_id=1017  where namespace_id=999961;
update eh_payment_notice_config set category_id=1018  where namespace_id=999962;
update eh_payment_notice_config set category_id=1019  where namespace_id=999967;
update eh_payment_notice_config set category_id=1020  where namespace_id=999969;
update eh_payment_notice_config set category_id=1021  where namespace_id=999970;
update eh_payment_notice_config set category_id=1022  where namespace_id=999971;
update eh_payment_notice_config set category_id=1023  where namespace_id=999972;
update eh_payment_notice_config set category_id=1024  where namespace_id=999975;
update eh_payment_notice_config set category_id=1025  where namespace_id=999980;
update eh_payment_notice_config set category_id=1026  where namespace_id=999983;
update eh_payment_notice_config set category_id=1027  where namespace_id=999992;
update eh_payment_notice_config set category_id=1028  where namespace_id=999993;

-- 更新 eh_payment_charging_item_scopes 表的categoryid
update eh_payment_charging_item_scopes set category_id=1001  where namespace_id=2;
update eh_payment_charging_item_scopes set category_id=1002  where namespace_id=11;
update eh_payment_charging_item_scopes set category_id=1003  where namespace_id=999944;
update eh_payment_charging_item_scopes set category_id=1004  where namespace_id=999945;
update eh_payment_charging_item_scopes set category_id=1005  where namespace_id=999946;
update eh_payment_charging_item_scopes set category_id=1006  where namespace_id=999947;
update eh_payment_charging_item_scopes set category_id=1007  where namespace_id=999948;
update eh_payment_charging_item_scopes set category_id=1008  where namespace_id=999949;
update eh_payment_charging_item_scopes set category_id=1009  where namespace_id=999950;
update eh_payment_charging_item_scopes set category_id=1010  where namespace_id=999951;
update eh_payment_charging_item_scopes set category_id=1011  where namespace_id=999952;
update eh_payment_charging_item_scopes set category_id=1012  where namespace_id=999953;
update eh_payment_charging_item_scopes set category_id=1013  where namespace_id=999954;
update eh_payment_charging_item_scopes set category_id=1014  where namespace_id=999956;
update eh_payment_charging_item_scopes set category_id=1015  where namespace_id=999957;
update eh_payment_charging_item_scopes set category_id=1016  where namespace_id=999958;
update eh_payment_charging_item_scopes set category_id=1017  where namespace_id=999961;
update eh_payment_charging_item_scopes set category_id=1018  where namespace_id=999962;
update eh_payment_charging_item_scopes set category_id=1019  where namespace_id=999967;
update eh_payment_charging_item_scopes set category_id=1020  where namespace_id=999969;
update eh_payment_charging_item_scopes set category_id=1021  where namespace_id=999970;
update eh_payment_charging_item_scopes set category_id=1022  where namespace_id=999971;
update eh_payment_charging_item_scopes set category_id=1023  where namespace_id=999972;
update eh_payment_charging_item_scopes set category_id=1024  where namespace_id=999975;
update eh_payment_charging_item_scopes set category_id=1025  where namespace_id=999980;
update eh_payment_charging_item_scopes set category_id=1026  where namespace_id=999983;
update eh_payment_charging_item_scopes set category_id=1027  where namespace_id=999992;
update eh_payment_charging_item_scopes set category_id=1028  where namespace_id=999993;

-- 更新 eh_payment_charging_standards_scopes 表的categoryid
update eh_payment_charging_standards_scopes set category_id=1001  where namespace_id=2;
update eh_payment_charging_standards_scopes set category_id=1002  where namespace_id=11;
update eh_payment_charging_standards_scopes set category_id=1003  where namespace_id=999944;
update eh_payment_charging_standards_scopes set category_id=1004  where namespace_id=999945;
update eh_payment_charging_standards_scopes set category_id=1005  where namespace_id=999946;
update eh_payment_charging_standards_scopes set category_id=1006  where namespace_id=999947;
update eh_payment_charging_standards_scopes set category_id=1007  where namespace_id=999948;
update eh_payment_charging_standards_scopes set category_id=1008  where namespace_id=999949;
update eh_payment_charging_standards_scopes set category_id=1009  where namespace_id=999950;
update eh_payment_charging_standards_scopes set category_id=1010  where namespace_id=999951;
update eh_payment_charging_standards_scopes set category_id=1011  where namespace_id=999952;
update eh_payment_charging_standards_scopes set category_id=1012  where namespace_id=999953;
update eh_payment_charging_standards_scopes set category_id=1013  where namespace_id=999954;
update eh_payment_charging_standards_scopes set category_id=1014  where namespace_id=999956;
update eh_payment_charging_standards_scopes set category_id=1015  where namespace_id=999957;
update eh_payment_charging_standards_scopes set category_id=1016  where namespace_id=999958;
update eh_payment_charging_standards_scopes set category_id=1017  where namespace_id=999961;
update eh_payment_charging_standards_scopes set category_id=1018  where namespace_id=999962;
update eh_payment_charging_standards_scopes set category_id=1019  where namespace_id=999967;
update eh_payment_charging_standards_scopes set category_id=1020  where namespace_id=999969;
update eh_payment_charging_standards_scopes set category_id=1021  where namespace_id=999970;
update eh_payment_charging_standards_scopes set category_id=1022  where namespace_id=999971;
update eh_payment_charging_standards_scopes set category_id=1023  where namespace_id=999972;
update eh_payment_charging_standards_scopes set category_id=1024  where namespace_id=999975;
update eh_payment_charging_standards_scopes set category_id=1025  where namespace_id=999980;
update eh_payment_charging_standards_scopes set category_id=1026  where namespace_id=999983;
update eh_payment_charging_standards_scopes set category_id=1027  where namespace_id=999992;
update eh_payment_charging_standards_scopes set category_id=1028  where namespace_id=999993;

-- 更新 eh_payment_bill_groups 表的categoryid
update eh_payment_bill_groups set category_id=1001  where namespace_id=2;
update eh_payment_bill_groups set category_id=1002  where namespace_id=11;
update eh_payment_bill_groups set category_id=1003  where namespace_id=999944;
update eh_payment_bill_groups set category_id=1004  where namespace_id=999945;
update eh_payment_bill_groups set category_id=1005  where namespace_id=999946;
update eh_payment_bill_groups set category_id=1006  where namespace_id=999947;
update eh_payment_bill_groups set category_id=1007  where namespace_id=999948;
update eh_payment_bill_groups set category_id=1008  where namespace_id=999949;
update eh_payment_bill_groups set category_id=1009  where namespace_id=999950;
update eh_payment_bill_groups set category_id=1010  where namespace_id=999951;
update eh_payment_bill_groups set category_id=1011  where namespace_id=999952;
update eh_payment_bill_groups set category_id=1012  where namespace_id=999953;
update eh_payment_bill_groups set category_id=1013  where namespace_id=999954;
update eh_payment_bill_groups set category_id=1014  where namespace_id=999956;
update eh_payment_bill_groups set category_id=1015  where namespace_id=999957;
update eh_payment_bill_groups set category_id=1016  where namespace_id=999958;
update eh_payment_bill_groups set category_id=1017  where namespace_id=999961;
update eh_payment_bill_groups set category_id=1018  where namespace_id=999962;
update eh_payment_bill_groups set category_id=1019  where namespace_id=999967;
update eh_payment_bill_groups set category_id=1020  where namespace_id=999969;
update eh_payment_bill_groups set category_id=1021  where namespace_id=999970;
update eh_payment_bill_groups set category_id=1022  where namespace_id=999971;
update eh_payment_bill_groups set category_id=1023  where namespace_id=999972;
update eh_payment_bill_groups set category_id=1024  where namespace_id=999975;
update eh_payment_bill_groups set category_id=1025  where namespace_id=999980;
update eh_payment_bill_groups set category_id=1026  where namespace_id=999983;
update eh_payment_bill_groups set category_id=1027  where namespace_id=999992;
update eh_payment_bill_groups set category_id=1028  where namespace_id=999993;

-- 更新 eh_payment_bill_items 表的categoryid
update eh_payment_bill_items set category_id=1001  where namespace_id=2;
update eh_payment_bill_items set category_id=1002  where namespace_id=11;
update eh_payment_bill_items set category_id=1003  where namespace_id=999944;
update eh_payment_bill_items set category_id=1004  where namespace_id=999945;
update eh_payment_bill_items set category_id=1005  where namespace_id=999946;
update eh_payment_bill_items set category_id=1006  where namespace_id=999947;
update eh_payment_bill_items set category_id=1007  where namespace_id=999948;
update eh_payment_bill_items set category_id=1008  where namespace_id=999949;
update eh_payment_bill_items set category_id=1009  where namespace_id=999950;
update eh_payment_bill_items set category_id=1010  where namespace_id=999951;
update eh_payment_bill_items set category_id=1011  where namespace_id=999952;
update eh_payment_bill_items set category_id=1012  where namespace_id=999953;
update eh_payment_bill_items set category_id=1013  where namespace_id=999954;
update eh_payment_bill_items set category_id=1014  where namespace_id=999956;
update eh_payment_bill_items set category_id=1015  where namespace_id=999957;
update eh_payment_bill_items set category_id=1016  where namespace_id=999958;
update eh_payment_bill_items set category_id=1017  where namespace_id=999961;
update eh_payment_bill_items set category_id=1018  where namespace_id=999962;
update eh_payment_bill_items set category_id=1019  where namespace_id=999967;
update eh_payment_bill_items set category_id=1020  where namespace_id=999969;
update eh_payment_bill_items set category_id=1021  where namespace_id=999970;
update eh_payment_bill_items set category_id=1022  where namespace_id=999971;
update eh_payment_bill_items set category_id=1023  where namespace_id=999972;
update eh_payment_bill_items set category_id=1024  where namespace_id=999975;
update eh_payment_bill_items set category_id=1025  where namespace_id=999980;
update eh_payment_bill_items set category_id=1026  where namespace_id=999983;
update eh_payment_bill_items set category_id=1027  where namespace_id=999992;
update eh_payment_bill_items set category_id=1028  where namespace_id=999993;

-- 初始化缴费、合同之间的映射关系
set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 2, 1001, 1001, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 11, 1002, 1002, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999944, 1003, 1003, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999945, 1004, 1004, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999946, 1005, 1005, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999947, 1006, 1006, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999948, 1007, 1007, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999949, 1008, 1008, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999950, 1009, 1009, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999952, 1011, 1010, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999954, 1013, 1011, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999956, 1014, 1012, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999957, 1015, 1013, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999958, 1016, 1014, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999961, 1017, 1015, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999969, 1020, 1016, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999970, 1021, 1017, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999971, 1022, 1018, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999972, 1023, 1019, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999980, 1025, 1020, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999983, 1026, 1021, NULL, 1, 2, NOW(), 1, NULL, NULL);
-- END BY 杨崇鑫


-- 深圳湾适用脚本[999966]  
-- ADD BY 杨崇鑫 
-- #28874 物业缴费（多应用） 产品功能  缴费旧数据迁移
-- 生成categoryid
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
 VALUES (1001, 1001, UTC_TIMESTAMP(), 1, 2, '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}' 
 WHERE module_id='20400' and namespace_id='999966';
-- 更新eh_payment_bills表的categoryid
update eh_payment_bills set category_id=1001  where namespace_id=999966;
-- 更新 eh_payment_notice_config 表的categoryid
update eh_payment_notice_config set category_id=1001  where namespace_id=999966;
-- 更新 eh_payment_charging_item_scopes 表的categoryid
update eh_payment_charging_item_scopes set category_id=1001  where namespace_id=999966;
-- 更新 eh_payment_charging_standards_scopes 表的categoryid
update eh_payment_charging_standards_scopes set category_id=1001  where namespace_id=999966;
-- 更新 eh_payment_bill_groups 表的categoryid
update eh_payment_bill_groups set category_id=1001  where namespace_id=999966;
-- 更新 eh_payment_bill_items 表的categoryid
update eh_payment_bill_items set category_id=1001  where namespace_id=999966;
-- 初始化缴费、合同之间的映射关系
set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999966, 1001, 1001, NULL, 1, 2, NOW(), 1, NULL, NULL);
-- END BY 杨崇鑫 


-- 清华信息港适用脚本[999984]  
-- ADD BY 杨崇鑫 
-- #28874 物业缴费（多应用） 产品功能  缴费旧数据迁移
-- 生成categoryid
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
 VALUES (1001, 1001, UTC_TIMESTAMP(), 1, 2, '{\"categoryId\":1001}');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1001}' 
 WHERE module_id='20400' and namespace_id='999984';
-- 更新eh_payment_bills表的categoryid
update eh_payment_bills set category_id=1001  where namespace_id=999984;
-- 更新 eh_payment_notice_config 表的categoryid
update eh_payment_notice_config set category_id=1001  where namespace_id=999984;
-- 更新 eh_payment_charging_item_scopes 表的categoryid
update eh_payment_charging_item_scopes set category_id=1001  where namespace_id=999984;
-- 更新 eh_payment_charging_standards_scopes 表的categoryid
update eh_payment_charging_standards_scopes set category_id=1001  where namespace_id=999984;
-- 更新 eh_payment_bill_groups 表的categoryid
update eh_payment_bill_groups set category_id=1001  where namespace_id=999984;
-- 更新 eh_payment_bill_items 表的categoryid
update eh_payment_bill_items set category_id=1001  where namespace_id=999984;
-- 初始化缴费、合同之间的映射关系
set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999984, 1001, 1001, NULL, 1, 2, NOW(), 1, NULL, NULL);
-- END BY 杨崇鑫 


-- 光大we谷适用脚本[999979]  
-- ADD BY 杨崇鑫 
-- #28874 物业缴费（多应用） 产品功能  缴费旧数据迁移
-- 生成categoryid
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
 VALUES (1001, 1001, UTC_TIMESTAMP(), 1, 2, '{\"categoryId\":1001}');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1001}' 
 WHERE module_id='20400' and namespace_id='999979';
-- 更新eh_payment_bills表的categoryid
update eh_payment_bills set category_id=1001  where namespace_id=999979;
-- 更新 eh_payment_notice_config 表的categoryid
update eh_payment_notice_config set category_id=1001  where namespace_id=999979;
-- 更新 eh_payment_charging_item_scopes 表的categoryid
update eh_payment_charging_item_scopes set category_id=1001  where namespace_id=999979;
-- 更新 eh_payment_charging_standards_scopes 表的categoryid
update eh_payment_charging_standards_scopes set category_id=1001  where namespace_id=999979;
-- 更新 eh_payment_bill_groups 表的categoryid
update eh_payment_bill_groups set category_id=1001  where namespace_id=999979;
-- 更新 eh_payment_bill_items 表的categoryid
update eh_payment_bill_items set category_id=1001  where namespace_id=999979;
-- END BY 杨崇鑫 


-- 安邦物业适用脚本[999949]  
-- ADD BY 杨崇鑫 
-- #28874 物业缴费（多应用） 产品功能  缴费旧数据迁移
-- 生成categoryid
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
 VALUES (1001, 1001, UTC_TIMESTAMP(), 1, 2, '{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}' 
 WHERE module_id='20400' and namespace_id='999949';
-- 更新eh_payment_bills表的categoryid
update eh_payment_bills set category_id=1001  where namespace_id=999949;
-- 更新 eh_payment_notice_config 表的categoryid
update eh_payment_notice_config set category_id=1001  where namespace_id=999949;
-- 更新 eh_payment_charging_item_scopes 表的categoryid
update eh_payment_charging_item_scopes set category_id=1001  where namespace_id=999949;
-- 更新 eh_payment_charging_standards_scopes 表的categoryid
update eh_payment_charging_standards_scopes set category_id=1001  where namespace_id=999949;
-- 更新 eh_payment_bill_groups 表的categoryid
update eh_payment_bill_groups set category_id=1001  where namespace_id=999949;
-- 更新 eh_payment_bill_items 表的categoryid
update eh_payment_bill_items set category_id=1001  where namespace_id=999949;
-- 初始化缴费、合同之间的映射关系
set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999949, 1001, 1001, NULL, 1, 2, NOW(), 1, NULL, NULL);
-- END BY 杨崇鑫 


-- 深圳湾适用脚本[999966]  
-- ADD BY 丁建民 
-- #28874  合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同） 合同旧数据迁移
-- 生成categoryid
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1001, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999966', NULL, NULL, '0');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1001,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1001' WHERE module_id='21200' and namespace_id='999966';
-- 更新旧合同的categoryid
UPDATE `eh_contracts` SET `category_id`='1001' WHERE namespace_id='999966';
-- 更新合同基础参数设置
UPDATE `eh_contract_params` SET `category_id`='1001' WHERE namespace_id='999966';
-- 更新表单 ，categoryid为空会用原来的表单，
UPDATE `eh_var_field_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999966; 
UPDATE `eh_var_field_item_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999966;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999966;
-- 更新工作流相关的表
UPDATE `eh_flows` SET `owner_id`='1001' WHERE  module_id='21200' AND namespace_id=999966;
UPDATE `eh_flow_cases` SET `owner_id`='1001' WHERE module_id='21200' AND namespace_id=999966;
-- END BY 丁建民

-- 清华信息港适用脚本[999984]  
-- ADD BY 丁建民 
-- #28874  合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同） 合同旧数据迁移
-- 生成categoryid
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1001, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999984', NULL, NULL, '0');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1001,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1001' WHERE module_id='21200' and namespace_id='999984';
-- 更新旧合同的categoryid
UPDATE `eh_contracts` SET `category_id`='1001' WHERE namespace_id='999984';
-- 更新合同基础参数设置
UPDATE `eh_contract_params` SET `category_id`='1001' WHERE namespace_id='999984';
-- 更新表单 ，categoryid为空会用原来的表单，
UPDATE `eh_var_field_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999984; 
UPDATE `eh_var_field_item_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999984;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999984;
-- 更新工作流相关的表
UPDATE `eh_flows` SET `owner_id`='1001' WHERE  module_id='21200' AND namespace_id=999984;
UPDATE `eh_flow_cases` SET `owner_id`='1001' WHERE module_id='21200' AND namespace_id=999984;
-- END BY 丁建民

-- 安邦物业适用脚本[999949]  
-- ADD BY 丁建民 
-- #28874  合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同） 合同旧数据迁移
-- 生成categoryid
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1001, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999949', NULL, NULL, '0');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1001,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1001' WHERE module_id='21200' and namespace_id='999949';
-- 更新旧合同的categoryid
UPDATE `eh_contracts` SET `category_id`='1001' WHERE namespace_id='999949';
-- 更新合同基础参数设置
UPDATE `eh_contract_params` SET `category_id`='1001' WHERE namespace_id='999949';
-- 更新表单 ，categoryid为空会用原来的表单，
UPDATE `eh_var_field_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999949; 
UPDATE `eh_var_field_item_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999949;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=999949;
-- 更新工作流相关的表
UPDATE `eh_flows` SET `owner_id`='1001' WHERE  module_id='21200' AND namespace_id=999949;
UPDATE `eh_flow_cases` SET `owner_id`='1001' WHERE module_id='21200' AND namespace_id=999949;
-- END BY 丁建民

-- 通用脚本  
-- ADD BY 丁建民 
-- issue32579 合同管理2.6
SET @id = (SELECT MAX(id) from eh_locale_strings);

INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'contract', '10008', 'zh_CN', '合同查询参数错误');
-- END BY 丁建民 

-- 通用脚本 合同管理（科技园）  注：上现网后期补充数据
-- ADD BY 丁建民 
-- #28874  合同管理（多应用） 合同旧数据迁移
-- 生成categoryid
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1000, '0', '0', '0', '合同管理【科技园对接】', NULL, NULL, '2', '1', NOW(), '1', NULL, '1000000', NULL, NULL, '0');
-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1000,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1000' WHERE module_id='32500' and namespace_id='1000000';
-- 更新旧合同的categoryid
UPDATE `eh_contracts` SET `category_id`='1000' WHERE namespace_id='1000000';

-- END BY 丁建民