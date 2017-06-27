-- 把装修状态改成其它，add by tt, 20170602
UPDATE eh_organization_address_mappings SET living_status = 0 WHERE living_status = 4;

-- 错误提示，add by tt, 20170602
SELECT MAX(id) INTO @id FROM `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20001', 'zh_CN', '楼栋名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20002', 'zh_CN', '地址不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20003', 'zh_CN', '联系人不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20004', 'zh_CN', '联系电话不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'community', '20005', 'zh_CN', '经纬度格式错误，请使用逗号分隔');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '80001', 'zh_CN', '企业名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '80002', 'zh_CN', '楼栋不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '80003', 'zh_CN', '门牌不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '80004', 'zh_CN', '楼栋不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '80005', 'zh_CN', '门牌不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'organization', '80006', 'zh_CN', '该门牌已经入住其它公司');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20001', 'zh_CN', '楼栋名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20002', 'zh_CN', '门牌不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20003', 'zh_CN', '面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20004', 'zh_CN', '门牌号已存在');

-- 视频会议短信模板，add by wh, 20170615
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-左邻','53378','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-左邻','53385','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-科技园','53386','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-科技园','53387','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-海岸','53389','999993');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-海岸','53390','999993');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-深业','53391','999992');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-深业','53393','999992');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-威新','53394','999991');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-威新','53395','999991');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-储能','53396','999990');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-储能','53397','999990');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-ibase','53398','999989');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-ibase','53399','999989');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-爱特家','53400','999988');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-爱特家','53403','999988');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-深圳湾','','999987');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-深圳湾','','999987');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-创源','53777','999986');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-创源','53778','999986');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-华润','53779','999985');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-华润','53781','999985');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-T空间','53783','999982');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-T空间','53784','999982');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-正中会','53785','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-正中会','53786','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-星商汇','53787','999981');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-星商汇','53788','999981');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-全至100','53801','999980');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-全至100','53802','999980');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-光大','53803','999979');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-光大','53804','999979');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-Volgo','53805','1');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-Volgo','53806','1');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-名网邦','53809','999976');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-名网邦','53811','999976');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-荣超','53814','999975');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-荣超','53815','999975');


SET @id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'video.help','https://videomeeting.kf5.com/hc/','video help doc','0',NULL);

-- 园区入驻 add by sw 20170620
DELETE from eh_general_forms where owner_type in ('EhLeasePromotions', 'EhBuildings');
SET @eh_general_forms := (SELECT MAX(id) FROM `eh_general_forms`) + 1;
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `update_time`, `create_time`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`)
	VALUES (@eh_general_forms, '0', '0', '0', 'EhLeasePromotions', NULL, NULL, '招租管理默认表单', @eh_general_forms, '0', 'DEFAULT_JSON', '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"用户姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"手机号码\",\"fieldExtra\":\"{\\\"limitWord\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"INTEGER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"公司名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_BUILDING\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROMOTION_BUILDING\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_APARTMENT\",\"dynamicFlag\":1,\"fieldDisplayName\":\"门牌号码\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROMOTION_APARTMENT\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_DESCRIPTION\",\"dynamicFlag\":0,\"fieldDisplayName\":\"备注说明\",\"fieldName\":\"LEASE_PROMOTION_DESCRIPTION\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"CUSTOM_DATA\",\"dynamicFlag\":0,\"fieldName\":\"CUSTOM_DATA\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"visibleType\":\"HIDDEN\"}]', '2', '2017-06-10 18:46:36', '2017-06-10 18:45:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `update_time`, `create_time`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`)
	VALUES (@eh_general_forms:=@eh_general_forms +1, '0', '0', '0', 'EhBuildings', NULL, NULL, '招租管理默认表单', @eh_general_forms, '0', 'DEFAULT_JSON', '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"用户姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"手机号码\",\"fieldExtra\":\"{\\\"limitWord\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"INTEGER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"公司名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_BUILDING\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROMOTION_BUILDING\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_DESCRIPTION\",\"dynamicFlag\":0,\"fieldDisplayName\":\"备注说明\",\"fieldName\":\"LEASE_PROMOTION_DESCRIPTION\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"CUSTOM_DATA\",\"dynamicFlag\":0,\"fieldName\":\"CUSTOM_DATA\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"visibleType\":\"HIDDEN\"}]', '2', '2017-06-10 18:46:36', '2017-06-10 18:45:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

UPDATE eh_lease_configs set display_name_str = '园区介绍, 虚位以待', display_order_str = '1,2';

UPDATE `eh_locale_templates` SET `text`='预约楼栋: ${buildingName}\r\n申请来源: ${sourceType}' WHERE `scope`='expansion' and `code`='1';
UPDATE `eh_locale_templates` SET `text`='[{\"key\":\"预约楼栋\",\"value\":\"${applyBuilding}\",\"entityType\":\"list\"},{\"key\":\"姓名\",\"value\":\"${applyUserName}\",\"entityType\":\"list\"},{\"key\":\"手机号\",\"value\":\"${contactPhone}\",\"entityType\":\"list\"},{\"key\":\"公司名称\",\"value\":\"${enterpriseName}\",\"entityType\":\"list\"},{\"key\":\"申请来源\",\"value\":\"${sourceType}\",\"entityType\":\"list\"},{\"key\":\"备注\",\"value\":\"${description}\",\"entityType\":\"multi_line\"}]' WHERE `scope`='expansion' and `code`='2';

SELECT MAX(id) INTO @id FROM `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id+1, 50900, '', 'EhNamespaces', 1000000, 2);

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
	VALUES ('expansion', '6', 'zh_CN', '已下线不能修改为已招租！');

UPDATE eh_buildings set default_order = id;
UPDATE eh_lease_promotions set default_order = unix_timestamp(create_time) * 1000;
UPDATE eh_enterprise_op_requests set source_type = 'renew' where apply_type = 3;

--  use phone_visit for techpark added by janson
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.qr_driver_ext', 'phone_visit', 'the driver extend of this namespace.(zuolin/phone_visit)');


-- item 配置声明 add sfyan 20170626 alpha环境已经执行过了
update `eh_launch_pad_items` set `action_type` = 14 where `action_data` like '%i.eqxiu.com/%' and `action_type` = 13;
update `eh_launch_pad_items` set `action_type` = 13 where `action_data` like '%zuolin.%' and `action_type` = 14;
update `eh_launch_pad_items` set `action_type` = 13 where `action_data` like '%lab.everhomes.com%' and `action_type` = 14;
update `eh_launch_pad_items` set `action_type` = 13 where `action_data` like '%beta.zuolin.com%' and `action_type` = 14;
update `eh_launch_pad_items` set `action_type` = 13 where `target_type` = 'biz' and `action_type` = 14;
update `eh_launch_pad_items` set `action_data` = REPLACE(`action_data`,'}', ',"declareFlag":"1"}') where `action_type` = 14 and `action_data` not like '%dudubashi%';

-- 更新园区入驻配置参数 add by sw 20170626
UPDATE eh_lease_configs set issuing_lease_flag = 0,issuer_manage_flag = 0 where namespace_id != 999985;
UPDATE eh_lease_configs set display_name_str = '项目介绍,待租物业', display_order_str = '1,2' WHERE namespace_id = 999975;


-- 增加报错类型  add by yanjun 20170626
SET @id := (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id:=@id+1,'activity','10027','zh_CN','此活动为收费类活动，必须升级APP才能报名哟！');