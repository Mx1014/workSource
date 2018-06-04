-- 审批3.0初始数据, 需先执行schema后执行此sql start.
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
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('6', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '物品领用申请', '3', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('7', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '费用报销申请', '3', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('8', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '用章申请', '3', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('9', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '用车申请', '3', '1', '1', 'cs://1/image/aW1hZ2UvTVRveVl6Z3dNVEl5TVdZMU1HUmpNRGxsWWpKaFlUWTFNelJtTTJJek0ySm1Odw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('10', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '合同申请', '4', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('11', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '付款申请', '4', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`, `approval_attribute`) VALUES ('12', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '备用金申请', '4', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM01qVmhORE5rWkRaalpUUTRNMkV3WkRJek5HWTROV0V6WTJVeE1tVmxZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43', 'CUSTOMIZE');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('13', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '转正申请', '2', 'EMPLOY_APPLICATION', '0', '0', 'cs://1/image/aW1hZ2UvTVRvNE5qUmpZakJpT0dJd09EUmxPVFl4TWprNFpqSXdaalJpWVRNMU5UUmpaZw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('14', '0', '0', 'EhOrganizations', '0', '52000', 'any-module', '0', NULL, '0', '0', '离职申请', '2', 'DISMISS_APPLICATION', '0', '0', 'cs://1/image/aW1hZ2UvTVRwaE16VXlaV0ZsTURNd1pEQmpaak0wWVRJNE5Ua3pNVE5rTmpNeE16UmtZdw', '2018-04-17 21:11:45', '2018-04-17 21:11:43');


UPDATE `eh_general_approvals` SET `integral_tag1`='5', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU1ERmtNelkzT0RVek1XTmhPV0ppWVdRMU5ESXdPRFJsWmpFM1l6UmxNUQ' WHERE `module_id`='52000';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '请假申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU0ySXdOVGhsWlRNMVpUUmhOVFkwTURnM00yWmlOV0k0WWpKaE5HUTNOdw' WHERE `approval_attribute`='ASK_FOR_LEAVE';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '加班申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNU9ESXlOekF4WlROak1UUXdNRE5qTlRFMk5qVmtOelppTVRFM016QmtOdw' WHERE `approval_attribute`='OVERTIME';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '出差申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRveVpHTm1ZekpqWlRRME5EZzRNamc1WkRBeE16RXdNVGcxT0RNeU5qbGhNUQ' WHERE `approval_attribute`='BUSINESS_TRIP';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '外出申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvelpHTm1NemxtTm1SbVlURmhNVFl5Tm1VNE1UUTRZbU16Wm1WallqRTFOZw' WHERE `approval_attribute`='GO_OUT';
UPDATE `eh_general_approvals` SET `integral_tag1`='1', `approval_name` = '异常申请', `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvNE9XSXpNbVEwWTJJMFkyVXdPVGxqTkRCbFpHWTNPRE5pTlRSak0yUmtNZw' WHERE `approval_attribute`='ABNORMAL_PUNCH';

SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
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

-- 审批3.0 end.

-- 人事2.7 数据同步 start

-- 执行 /archives/syncArchivesConfigAndLogs 接口

-- 人事2.7 数据同步 end

