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

-- 人事2.7 数据同步 start by ryan.

-- 执行 /archives/syncArchivesConfigAndLogs 接口

-- 人事2.7 数据同步 end by ryan.



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