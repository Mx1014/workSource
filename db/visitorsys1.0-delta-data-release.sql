-- 访客管理1.0事由列表 by dengs,20180507
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('1', '0', 'community', '0', '临时来访', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('2', '0', 'community', '0', '入职', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('3', '0', 'community', '0', '面试', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('4', '0', 'community', '0', '送货', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('5', '0', 'community', '0', '出差	', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('6', '0', 'community', '0', '开会', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('7', '0', 'community', '0', '施工', '2', '0', now(), '0', now());
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES ('8', '0', 'community', '0', '其它原因', '2', '0', now(), '0', now());
-- 访客管理1.0短信模板 by dengs,20180507
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('sms.default', 67, 'zh_CN', '验证码-访客管理', '${modlueName}你的验证码是${verificationCode}。验证码15分钟内有效。', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('sms.default', 68, 'zh_CN', '预约访客邀请-访客管理', '${appName}你收到了一条来自${visitEnterpriseName}的访客邀请，请点击查看：${invitationLink}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('sms.default', 69, 'zh_CN', '临时访客邀请-访客管理', '欢迎光临${name}，请点击查看你的在线通行证：${invitationLink}', '0');

-- 访客管理1.0 连接配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('visitorsys.invitation.link', '%s/visitor-appointment/build/invitation.html?visitorToken=%s', '访客管理邀请函连接地址', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('visitorsys.selfregister.link', '%s/vsregister/dist/i.html?t=%s', '访客管理自助登记连接地址', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('visitorsys.inviter.route', '%s/visitor-appointment/build/index.html?id=%s&ns=%s#/appointment-detail#sign_suffix', '访客管理消息详情连接', '0', NULL);

-- 访客管理1.0 模块配置
SET @homeurl = (select `value` from eh_configurations WHERE `name`='home.url' AND namespace_id = 0 limit 1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('52100', '企业访客', '50000', '/50000/52100', '1', '2', '2', '210', now(), CONCAT('{"url":"',@homeurl,'/visitor-appointment/build/index.html?ns=%s#/home#sign_suffix "}'), '13', now(), '0', '0', '0', '0', 'org_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41800', '园区访客', '40000', '/40000/41800', '1', '2', '2', '180', now(), '{"url":"https://www.zuolin.com/mobile/static/coming_soon/index.html"}', '13', now(), '0', '0', '0', '0', 'community_control');

-- 访客管理1.0 菜单配置
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16033000', '园区访客', '16030000', NULL, 'visitor-park', '1', '2', '/16000000/16030000/16033000', 'zuolin', '22', '41800', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('45141800', '园区访客', '45000000', NULL, 'visitor-park', '1', '2', '/45000000/45141800', 'park', '22', '41800', '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('48052100', '企业访客', '48000000', NULL, 'visitor-enterprise', '1', '2', '/48000000/48052100', 'park', '20', '52100', '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('72152100', '企业访客', '72000000', NULL, 'visitor-enterprise', '1', '2', '/72000000/72152100', 'organization', '18', '52100', '2', 'system', 'module', NULL);

-- 访客管理1.0 表单配置
SET @id = (select max(id) from eh_general_forms);
SET @id = IFNULL(@id,0)+1;
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `update_time`, `create_time`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `form_template_id`, `form_template_version`, `form_attribute`, `modify_flag`, `delete_flag`) VALUES (@id, '0', '0', '0', 'visitorsys', '41800', 'visitorsys', 'visitorsys', @id, '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"输入有效期\",\"fieldDisplayName\":\"有效期\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"24小时\\\",\\\"48小时\\\",\\\"72小时\\\",\\\"不限\\\"]}\",\"fieldName\":\"invalidTime\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dynamicFlag\":0,\"fieldDesc\":\"输入车牌号码\",\"fieldDisplayName\":\"车牌号码\",\"fieldExtra\":\"{\\\"limitWord\\\":512}\",\"fieldName\":\"plateNo\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dynamicFlag\":0,\"fieldDesc\":\"输入证件号码\",\"fieldDisplayName\":\"证件号码\",\"fieldExtra\":\"{\\\"limitWord\\\":512}\",\"fieldName\":\"idNumber\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dynamicFlag\":0,\"fieldDesc\":\"输入到访楼层\",\"fieldDisplayName\":\"到访楼层\",\"fieldExtra\":\"{\\\"limitWord\\\":512}\",\"fieldName\":\"visitFloor\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dynamicFlag\":0,\"fieldDesc\":\"输入到访门牌\",\"fieldDisplayName\":\"到访门牌\",\"fieldExtra\":\"{\\\"limitWord\\\":512}\",\"fieldName\":\"visitAddresses\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"}]', '1', NULL, now(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CUSTOMIZE', '1', '1');

-- 访客管理1.0 园区访客权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41810', '预约管理', '41800', '/40000/41800/41810', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41820', '访客管理', '41800', '/40000/41800/41820', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41830', '统计信息', '41800', '/40000/41800/41830', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41840', '设备管理', '41800', '/40000/41800/41840', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41800', '0', '4180041800', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041810, '0', '园区访客 预约管理权限', '园区访客 预约管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41810', '0', 4180041810, '预约管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041820, '0', '园区访客 访客管理权限', '园区访客 访客管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41820', '0', 4180041820, '访客管理权限', '0', now());

-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041830, '0', '园区访客 统计信息权限', '园区访客 统计信息权限', NULL);
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41830', '0', 4180041830, '统计信息权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041840, '0', '园区访客 设备管理权限', '园区访客 设备管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41840', '0', 4180041840, '设备管理权限', '0', now());

--访客管理1.0 错误码描述等
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1001','zh_CN','必填项为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','408','zh_CN','配对码验证超时');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1401','zh_CN','设备没找到');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1402','zh_CN','预约未找到');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1405','zh_CN','验证码错误，请重新输入');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1406','zh_CN','重复的黑名单电话号码');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1407','zh_CN','此手机号码已进入黑名单');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1408','zh_CN','此手机号码已进入黑名单');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1409','zh_CN','此设备已被注册过');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1410','zh_CN','计划到访时间不能早于当前时间');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1411','zh_CN','配对码错误，请重新输入');