-- 增加param by st.zheng
SET @eh_flow_predefined_params_id = (SELECT MAX(id) FROM eh_flow_predefined_params );
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`) VALUES (@eh_flow_predefined_params_id:=@eh_flow_predefined_params_id+1, '0', '0', '', '20100', 'any-module', 'flow_node', '待确认费用', '待确认费用', '{\"nodeType\":\"CONFIRMFEE\"}', '2');
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`) VALUES (@eh_flow_predefined_params_id:=@eh_flow_predefined_params_id+1, '0', '0', '', '20100', 'any-module', 'flow_node', '调整费用', '调整费用', '{\"nodeType\":\"MOTIFYFEE\"}', '2');

-- 增加费用清单表单 by st.zheng
SET @eh_general_forms_id = (SELECT MAX(id) FROM eh_general_forms);
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `create_time`) VALUES (@eh_general_forms_id + 1, '0', '0', '0', 'PMTASK', '费用清单', @eh_general_forms_id + 1, '0', 'DEFAULT_JSON', '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"联系电话\",\"fieldExtra\":\"{\\\"limitLength\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"企业\",\"fieldExtra\":\"{}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_ADDRESS\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋门牌\",\"fieldExtra\":\"{}\",\"fieldName\":\"USER_ADDRESS\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入服务费\",\"fieldDisplayName\":\"服务费\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"服务费\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"物品\",\"fieldExtra\":\"{\\\"formFields\\\":[{\\\"fieldDesc\\\":\\\"请输入物品名称\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"EDITABLE\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"SINGLE_LINE_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"limitWord\\\\\\\":8}\\\",\\\"validatorType\\\":\\\"TEXT_LIMIT\\\",\\\"fieldName\\\":\\\"物品名称\\\",\\\"fieldDisplayName\\\":\\\"物品名称\\\"},{\\\"fieldDesc\\\":\\\"请输入单价（元）\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"EDITABLE\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"NUMBER_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"defaultValue\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"validatorType\\\":\\\"NUM_LIMIT\\\",\\\"fieldName\\\":\\\"单价\\\",\\\"fieldDisplayName\\\":\\\"单价\\\"},{\\\"fieldDesc\\\":\\\"请输入物品数量\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"EDITABLE\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"NUMBER_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"defaultValue\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"validatorType\\\":\\\"NUM_LIMIT\\\",\\\"fieldName\\\":\\\"数量\\\",\\\"fieldDisplayName\\\":\\\"数量\\\"},{\\\"fieldDesc\\\":\\\"\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"READONLY\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"NUMBER_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"defaultValue\\\\\\\":\\\\\\\"${物品.单价}*${物品.数量}\\\\\\\"}\\\",\\\"validatorType\\\":\\\"NUM_LIMIT\\\",\\\"fieldName\\\":\\\"小计\\\",\\\"fieldDisplayName\\\":\\\"小计\\\"}]}\",\"fieldName\":\"物品\",\"fieldType\":\"SUBFORM\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"总计\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"${服务费}+sum(${物品.小计})\\\"}\",\"fieldName\":\"总计\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"READONLY\"}]', '1', NOW());

-- merge from asset-org by xiongying
SET @id := (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'address', '20005', 'zh_CN', '门牌已关联合同，无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'building', '10003', 'zh_CN', '楼栋下有门牌已关联合同，无法删除');

SET @config_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'ebei.url', 'http://183.62.222.87:5902/sf', '一碑url', '0', NULL);

-- merge from forum-2.6 by yanjun start 201711291416
-- 园区生活、招聘与求职去掉tag
UPDATE eh_launch_pad_items SET action_data = '{"forumEntryId":"1"}' WHERE namespace_id = 999983 AND item_label = '园区生活' AND action_type = 62;
UPDATE eh_launch_pad_items SET action_data = '{"forumEntryId":"2"}' WHERE namespace_id = 999983 AND item_label = '招聘与求职' AND action_type = 62;

SET @id = (SELECT MAX(id) FROM eh_hot_tags);
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '闲置', '1', '1', NOW(), '1', NOW(), NULL, '1', '1');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '合租', '1', '2', NOW(), '1', NOW(), NULL, '1', '1');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '拼车', '1', '3', NOW(), '1', NOW(), NULL, '1', '1');

INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '招聘', '1', '1', NOW(), '1', NOW(), NULL, '2', '1');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'topic', '求职', '1', '2', NOW(), '1', NOW(), NULL, '2', '1');

INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'activity', '旅游', '1', '1', NOW(), '1', NOW(), NULL, '2', '2');
INSERT INTO `eh_hot_tags` (`id`, `namespace_id`, `service_type`, `name`, `status`, `default_order`, `create_time`, `create_uid`, `delete_time`, `delete_uid`, `category_id`, `module_type`) VALUES ((@id := @id + 1), '999983', 'activity', '团建', '1', '2', NOW(), '1', NOW(), NULL, '2', '2');

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) VALUES ('1', '0', NULL, NULL, 'topic', '话题', '0', NOW());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) VALUES ('2', '0', NULL, NULL, 'activity', '活动', '0', NOW());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) VALUES ('3', '0', NULL, NULL, 'poll', '投票', '0', NOW());
-- merge from forum-2.6 by yanjun end 201711291416


-- merge from fixPm-1.0  by jiarui   20171130
-- 巡检对象类型

INSERT  INTO  `eh_var_field_groups` VALUES (10000, 'equipment_inspection', 0, CONCAT('/',10000), '设备', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10001, 'equipment_inspection', 0, CONCAT('/',10001), '装修', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10002, 'equipment_inspection', 0, CONCAT('/',10002), '空置房', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10003, 'equipment_inspection', 0, CONCAT('/',10003), '安保', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10004, 'equipment_inspection', 0, CONCAT('/',10004), '日常工作检查', '',0,  NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10005, 'equipment_inspection', 0, CONCAT('/',10005), '公共设施检查', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10006, 'equipment_inspection', 0, CONCAT('/',10006), '周末值班', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10007, 'equipment_inspection', 0, CONCAT('/',10007), '安全检查', '', 0, NULL, 2, 1, NOW(), NULL, NULL);
INSERT  INTO  `eh_var_field_groups` VALUES (10008, 'equipment_inspection', 0, CONCAT('/',10008), '其他', '', 0, NULL, 2, 1, NOW(), NULL, NULL);


-- 其他类型表单项  08

INSERT  INTO  `eh_var_fields` VALUES (10000, 'equipment_inspection', 'name', '设备名称', 'String', 10008, CONCAT('/',10008), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10001, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10008, CONCAT('/',10008), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10002, 'equipment_inspection', 'status', '当前状态', 'Long', 10008, CONCAT('/',10008), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10005, 'equipment_inspection', 'location', '安装位置', 'String', 10008, CONCAT('/',10008), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10006, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10008, CONCAT('/',10008), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');


-- 其他类型下拉菜单选项 08


INSERT INTO `eh_var_field_items` VALUES (10000, 'equipment_inspection', 10002, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10001, 'equipment_inspection', 10002, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10002, 'equipment_inspection', 10002, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10003, 'equipment_inspection', 10002, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10004, 'equipment_inspection', 10002, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10005, 'equipment_inspection', 10002, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10030, 'equipment_inspection', 10006, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10031, 'equipment_inspection', 10006, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10032, 'equipment_inspection', 10006, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10033, 'equipment_inspection', 10006, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10034, 'equipment_inspection', 10006, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10035, 'equipment_inspection', 10006, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10036, 'equipment_inspection', 10006, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10037, 'equipment_inspection', 10006, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10038, 'equipment_inspection', 10006, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10049, 'equipment_inspection', 10006, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10040, 'equipment_inspection', 10006, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10041, 'equipment_inspection', 10006, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10042, 'equipment_inspection', 10006, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10043, 'equipment_inspection', 10006, '其他', 14, 2, 1, NOW(), NULL, NULL,14);


-- 安全检查表单项  07


INSERT  INTO  `eh_var_fields` VALUES (10100, 'equipment_inspection', 'name', '设备名称', 'String', 10007, CONCAT('/',10007), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10101, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10007, CONCAT('/',10007), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10102, 'equipment_inspection', 'status', '当前状态', 'Long', 10007, CONCAT('/',10007), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10105, 'equipment_inspection', 'location', '安装位置', 'String', 10007, CONCAT('/',10007), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10106, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10007, CONCAT('/',10007), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');




INSERT INTO `eh_var_field_items` VALUES (10100, 'equipment_inspection', 10102, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10101, 'equipment_inspection', 10102, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10102, 'equipment_inspection', 10102, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10103, 'equipment_inspection', 10102, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10104, 'equipment_inspection', 10102, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10105, 'equipment_inspection', 10102, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10130, 'equipment_inspection', 10106, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10131, 'equipment_inspection', 10106, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10132, 'equipment_inspection', 10106, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10133, 'equipment_inspection', 10106, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10134, 'equipment_inspection', 10106, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10135, 'equipment_inspection', 10106, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10136, 'equipment_inspection', 10106, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10137, 'equipment_inspection', 10106, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10138, 'equipment_inspection', 10106, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10149, 'equipment_inspection', 10106, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10140, 'equipment_inspection', 10106, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10141, 'equipment_inspection', 10106, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10142, 'equipment_inspection', 10106, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10143, 'equipment_inspection', 10106, '其他', 14, 2, 1, NOW(), NULL, NULL,14);





-- 周末值班表单项  06


INSERT  INTO  `eh_var_fields` VALUES (10200, 'equipment_inspection', 'name', '设备名称', 'String', 10006, CONCAT('/',10006), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10201, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10006, CONCAT('/',10006), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10202, 'equipment_inspection', 'status', '当前状态', 'Long', 10006, CONCAT('/',10006), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10205, 'equipment_inspection', 'location', '安装位置', 'String', 10006, CONCAT('/',10006), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10206, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10006, CONCAT('/',10006), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');

-- 周末值班下拉菜单选项 06


INSERT INTO `eh_var_field_items` VALUES (10200, 'equipment_inspection', 10202, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10201, 'equipment_inspection', 10202, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10202, 'equipment_inspection', 10202, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10203, 'equipment_inspection', 10202, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10204, 'equipment_inspection', 10202, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10205, 'equipment_inspection', 10202, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10230, 'equipment_inspection', 10206, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10231, 'equipment_inspection', 10206, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10232, 'equipment_inspection', 10206, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10233, 'equipment_inspection', 10206, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10234, 'equipment_inspection', 10206, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10235, 'equipment_inspection', 10206, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10236, 'equipment_inspection', 10206, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10237, 'equipment_inspection', 10206, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10238, 'equipment_inspection', 10206, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10249, 'equipment_inspection', 10206, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10240, 'equipment_inspection', 10206, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10241, 'equipment_inspection', 10206, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10242, 'equipment_inspection', 10206, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10243, 'equipment_inspection', 10206, '其他', 14, 2, 1, NOW(), NULL, NULL,14);




-- 公共设施检查表单项  05


INSERT  INTO  `eh_var_fields` VALUES (10300, 'equipment_inspection', 'name', '设备名称', 'String', 10005, CONCAT('/',10005), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10301, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10005, CONCAT('/',10005), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10302, 'equipment_inspection', 'status', '当前状态', 'Long', 10005, CONCAT('/',10005), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10305, 'equipment_inspection', 'location', '安装位置', 'String', 10005, CONCAT('/',10005), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10306, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10005, CONCAT('/',10005), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');

-- 公共设施下拉菜单选项 05


INSERT INTO `eh_var_field_items` VALUES (10300, 'equipment_inspection', 10302, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10301, 'equipment_inspection', 10302, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10302, 'equipment_inspection', 10302, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10303, 'equipment_inspection', 10302, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10304, 'equipment_inspection', 10302, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10305, 'equipment_inspection', 10302, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10330, 'equipment_inspection', 10306, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10331, 'equipment_inspection', 10306, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10332, 'equipment_inspection', 10306, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10333, 'equipment_inspection', 10306, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10334, 'equipment_inspection', 10306, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10335, 'equipment_inspection', 10306, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10336, 'equipment_inspection', 10306, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10337, 'equipment_inspection', 10306, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10338, 'equipment_inspection', 10306, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10349, 'equipment_inspection', 10306, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10340, 'equipment_inspection', 10306, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10341, 'equipment_inspection', 10306, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10342, 'equipment_inspection', 10306, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10343, 'equipment_inspection', 10306, '其他', 14, 2, 1, NOW(), NULL, NULL,14);



-- 日常工作检查表单项  04


INSERT  INTO  `eh_var_fields` VALUES (10400, 'equipment_inspection', 'name', '设备名称', 'String', 10004, CONCAT('/',10004), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10401, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10004, CONCAT('/',10004), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10402, 'equipment_inspection', 'status', '当前状态', 'Long', 10004, CONCAT('/',10004), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10405, 'equipment_inspection', 'location', '安装位置', 'String', 10004, CONCAT('/',10004), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10406, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10004, CONCAT('/',10004), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');

-- 日常工作检查下拉菜单选项  04


INSERT INTO `eh_var_field_items` VALUES (10400, 'equipment_inspection', 10402, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10401, 'equipment_inspection', 10402, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10402, 'equipment_inspection', 10402, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10403, 'equipment_inspection', 10402, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10404, 'equipment_inspection', 10402, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10405, 'equipment_inspection', 10402, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10430, 'equipment_inspection', 10406, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10431, 'equipment_inspection', 10406, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10432, 'equipment_inspection', 10406, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10433, 'equipment_inspection', 10406, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10434, 'equipment_inspection', 10406, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10435, 'equipment_inspection', 10406, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10436, 'equipment_inspection', 10406, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10437, 'equipment_inspection', 10406, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10438, 'equipment_inspection', 10406, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10449, 'equipment_inspection', 10406, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10440, 'equipment_inspection', 10406, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10441, 'equipment_inspection', 10406, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10442, 'equipment_inspection', 10406, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10443, 'equipment_inspection', 10406, '其他', 14, 2, 1, NOW(), NULL, NULL,14);


-- 安保表单项   03


INSERT  INTO  `eh_var_fields` VALUES (10500, 'equipment_inspection', 'name', '设备名称', 'String', 10003, CONCAT('/',10003), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10501, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10003, CONCAT('/',10003), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10502, 'equipment_inspection', 'status', '当前状态', 'Long', 10003, CONCAT('/',10003), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10505, 'equipment_inspection', 'location', '安装位置', 'String', 10003, CONCAT('/',10003), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10506, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10003, CONCAT('/',10003), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');


-- 安保下拉菜单选项  03

INSERT INTO `eh_var_field_items` VALUES (10500, 'equipment_inspection', 10502, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10501, 'equipment_inspection', 10502, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10502, 'equipment_inspection', 10502, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10503, 'equipment_inspection', 10502, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10504, 'equipment_inspection', 10502, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10505, 'equipment_inspection', 10502, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10530, 'equipment_inspection', 10506, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10531, 'equipment_inspection', 10506, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10532, 'equipment_inspection', 10506, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10533, 'equipment_inspection', 10506, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10534, 'equipment_inspection', 10506, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10535, 'equipment_inspection', 10506, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10536, 'equipment_inspection', 10506, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10537, 'equipment_inspection', 10506, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10538, 'equipment_inspection', 10506, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10549, 'equipment_inspection', 10506, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10540, 'equipment_inspection', 10506, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10541, 'equipment_inspection', 10506, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10542, 'equipment_inspection', 10506, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10543, 'equipment_inspection', 10506, '其他', 14, 2, 1, NOW(), NULL, NULL,14);




-- 空置房表单项  02

INSERT  INTO  `eh_var_fields` VALUES (10600, 'equipment_inspection', 'name', '设备名称', 'String', 10002, CONCAT('/',10002), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10601, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10002, CONCAT('/',10002), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10602, 'equipment_inspection', 'status', '当前状态', 'Long', 10002, CONCAT('/',10002), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10605, 'equipment_inspection', 'location', '安装位置', 'String', 10002, CONCAT('/',10002), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10606, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10002, CONCAT('/',10002), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');


-- 空置房下拉菜单选项  02

INSERT INTO `eh_var_field_items` VALUES (10600, 'equipment_inspection', 10602, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10601, 'equipment_inspection', 10602, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10602, 'equipment_inspection', 10602, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10603, 'equipment_inspection', 10602, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10604, 'equipment_inspection', 10602, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10605, 'equipment_inspection', 10602, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10630, 'equipment_inspection', 10606, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10631, 'equipment_inspection', 10606, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10632, 'equipment_inspection', 10606, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10633, 'equipment_inspection', 10606, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10634, 'equipment_inspection', 10606, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10635, 'equipment_inspection', 10606, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10636, 'equipment_inspection', 10606, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10637, 'equipment_inspection', 10606, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10638, 'equipment_inspection', 10606, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10649, 'equipment_inspection', 10606, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10640, 'equipment_inspection', 10606, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10641, 'equipment_inspection', 10606, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10642, 'equipment_inspection', 10606, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10643, 'equipment_inspection', 10606, '其他', 14, 2, 1, NOW(), NULL, NULL,14);



-- 装修表单项  01

INSERT  INTO  `eh_var_fields` VALUES (10700, 'equipment_inspection', 'name', '设备名称', 'String', 10001, CONCAT('/',10001), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10701, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10001, CONCAT('/',10001), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10702, 'equipment_inspection', 'status', '当前状态', 'Long', 10001, CONCAT('/',10001), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10705, 'equipment_inspection', 'location', '安装位置', 'String', 10001, CONCAT('/',10001), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10706, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10001, CONCAT('/',10001), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');



-- 装修下拉菜单选项 01

INSERT INTO `eh_var_field_items` VALUES (10700, 'equipment_inspection', 10702, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10701, 'equipment_inspection', 10702, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10702, 'equipment_inspection', 10702, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10703, 'equipment_inspection', 10702, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10704, 'equipment_inspection', 10702, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10705, 'equipment_inspection', 10702, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10730, 'equipment_inspection', 10706, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10731, 'equipment_inspection', 10706, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10732, 'equipment_inspection', 10706, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10733, 'equipment_inspection', 10706, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10734, 'equipment_inspection', 10706, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10735, 'equipment_inspection', 10706, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10736, 'equipment_inspection', 10706, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10737, 'equipment_inspection', 10706, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10738, 'equipment_inspection', 10706, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10749, 'equipment_inspection', 10706, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10740, 'equipment_inspection', 10706, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10741, 'equipment_inspection', 10706, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10742, 'equipment_inspection', 10706, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10743, 'equipment_inspection', 10706, '其他', 14, 2, 1, NOW(), NULL, NULL,14);



-- 设备表单项  00


INSERT  INTO  `eh_var_fields` VALUES (10800, 'equipment_inspection', 'name', '设备名称', 'String', 10000, CONCAT('/',10000), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10801, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10802, 'equipment_inspection', 'status', '当前状态', 'Long', 10000, CONCAT('/',10000), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10803, 'equipment_inspection', 'brandName', '品牌名称', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10804, 'equipment_inspection', 'equipmentModel', '设备型号', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10805, 'equipment_inspection', 'location', '安装位置', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10806, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10000, CONCAT('/',10000), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10807, 'equipment_inspection', 'installationTime', '安装日期', 'Date', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10808, 'equipment_inspection', 'discardTime', '报废日期', 'Date', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10809, 'equipment_inspection', 'repairTime', '免保日期', 'Date', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10810, 'equipment_inspection', 'manufacturer', '生产厂家', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10811, 'equipment_inspection', 'constructionParty', '施工方', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10812, 'equipment_inspection', 'manager', '联系人', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10813, 'equipment_inspection', 'managerContact', '联系方式', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10814, 'equipment_inspection', 'parameter', '设备参数', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10815, 'equipment_inspection', 'detail', '设备详情', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10816, 'equipment_inspection', 'provenance', '产地', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10817, 'equipment_inspection', 'sequenceNo', '出厂编号', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10818, 'equipment_inspection', 'factoryTime', '出厂日期', 'Date', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10819, 'equipment_inspection', 'price', '购买价格', 'BigDecimal', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10820, 'equipment_inspection', 'buyTime', '购买日期', 'Date', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10821, 'equipment_inspection', 'depreciationYears', '折旧年限', 'Long', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10822, 'equipment_inspection', 'qrCodeFlag', '二维码状态', 'Long', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10823, 'equipment_inspection', 'remarks', '备注', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"multiText\", \"length\": 2048}');
INSERT  INTO  `eh_var_fields` VALUES (10824, 'equipment_inspection', 'attachments', '附件', 'String', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"file\", \"length\": 32}');
-- 添加设备数量  by jiarui 20171201
INSERT  INTO  `eh_var_fields` VALUES (10925, 'equipment_inspection', 'quantity', '数量', 'Long', 10000, CONCAT('/',10000), 0, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"text\", \"length\": 32}');



-- 下拉菜单选项  00



INSERT INTO `eh_var_field_items` VALUES (10800, 'equipment_inspection', 10802, '不完整', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10801, 'equipment_inspection', 10802, '使用中', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10802, 'equipment_inspection', 10802, '维修中', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10803, 'equipment_inspection', 10802, '报废', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10804, 'equipment_inspection', 10802, '停用', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10805, 'equipment_inspection', 10802, '备用', 6, 2, 1, NOW(), NULL, NULL,6);

INSERT INTO `eh_var_field_items` VALUES (10830, 'equipment_inspection', 10806, '消防', 1, 2, 1, NOW(), NULL, NULL,1);
INSERT INTO `eh_var_field_items` VALUES (10831, 'equipment_inspection', 10806, '强电', 2, 2, 1, NOW(), NULL, NULL,2);
INSERT INTO `eh_var_field_items` VALUES (10832, 'equipment_inspection', 10806, '弱电', 3, 2, 1, NOW(), NULL, NULL,3);
INSERT INTO `eh_var_field_items` VALUES (10833, 'equipment_inspection', 10806, '电梯', 4, 2, 1, NOW(), NULL, NULL,4);
INSERT INTO `eh_var_field_items` VALUES (10834, 'equipment_inspection', 10806, '空调', 5, 2, 1, NOW(), NULL, NULL,5);
INSERT INTO `eh_var_field_items` VALUES (10835, 'equipment_inspection', 10806, '给排水', 6, 2, 1, NOW(), NULL, NULL,6);
INSERT INTO `eh_var_field_items` VALUES (10836, 'equipment_inspection', 10806, '空置房', 7, 2, 1, NOW(), NULL, NULL,7);
INSERT INTO `eh_var_field_items` VALUES (10837, 'equipment_inspection', 10806, '装修', 8, 2, 1, NOW(), NULL, NULL,8);
INSERT INTO `eh_var_field_items` VALUES (10838, 'equipment_inspection', 10806, '安保', 9, 2, 1, NOW(), NULL, NULL,9);
INSERT INTO `eh_var_field_items` VALUES (10849, 'equipment_inspection', 10806, '日常工作检查', 10, 2, 1, NOW(), NULL, NULL,10);
INSERT INTO `eh_var_field_items` VALUES (10840, 'equipment_inspection', 10806, '公共设施检查', 11, 2, 1, NOW(), NULL, NULL,11);
INSERT INTO `eh_var_field_items` VALUES (10841, 'equipment_inspection', 10806, '周末值班', 12, 2, 1, NOW(), NULL, NULL,12);
INSERT INTO `eh_var_field_items` VALUES (10842, 'equipment_inspection', 10806, '安全检查', 13, 2, 1, NOW(), NULL, NULL,13);
INSERT INTO `eh_var_field_items` VALUES (10843, 'equipment_inspection', 10806, '其他', 14, 2, 1, NOW(), NULL, NULL,14);

INSERT INTO `eh_var_field_items` VALUES (10860, 'equipment_inspection', 10822, '停用', 1, 2, 1, NOW(), NULL, NULL,0);
INSERT INTO `eh_var_field_items` VALUES (10861, 'equipment_inspection', 10822, '启用', 2, 2, 1, NOW(), NULL, NULL,1);



-- 迁移设备表   equipments

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10000   WHERE  inspection_category_id=1;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10001   WHERE  inspection_category_id=2;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10002   WHERE  inspection_category_id=3;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10003   WHERE  inspection_category_id=4;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10004   WHERE  inspection_category_id=5;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10005   WHERE  inspection_category_id=6;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10006   WHERE  inspection_category_id=7;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10007   WHERE  inspection_category_id=8;

UPDATE `eh_equipment_inspection_equipments`
SET inspection_category_id = 10008   WHERE  inspection_category_id=9;


-- 迁移设备表  设备类型
UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 1   WHERE  category_id=200887;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 2   WHERE  category_id=200888;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 3   WHERE  category_id=200889;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 4   WHERE  category_id=202224;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 5   WHERE  category_id=202222;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 6   WHERE  category_id=202223;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 7   WHERE  category_id=203014;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 8   WHERE  category_id=203015;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 9   WHERE  category_id=203016;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 10   WHERE  category_id=203017;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 11   WHERE  category_id=203018;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 12   WHERE  category_id=203019;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 13   WHERE  category_id=203020;

UPDATE `eh_equipment_inspection_equipments`
SET  category_id = 14   WHERE  category_id=200890;


-- 迁移巡检对象类型 inspection_categories

UPDATE `eh_equipment_inspection_categories`
SET id = 10000   WHERE  id=1;

UPDATE `eh_equipment_inspection_categories`
SET id = 10001   WHERE  id=2;

UPDATE `eh_equipment_inspection_categories`
SET id = 10002   WHERE  id=3;

UPDATE `eh_equipment_inspection_categories`
SET id = 10003   WHERE  id=4;

UPDATE `eh_equipment_inspection_categories`
SET id = 10004   WHERE  id=5;

UPDATE `eh_equipment_inspection_categories`
SET id = 10005   WHERE  id=6;

UPDATE `eh_equipment_inspection_categories`
SET id = 10006   WHERE  id=7;

UPDATE `eh_equipment_inspection_categories`
SET id = 10007   WHERE  id=8;

UPDATE `eh_equipment_inspection_categories`
SET id = 10008   WHERE  id=9;



-- 迁移标准表 inspection_category_id

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10000   WHERE  inspection_category_id=1;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10001   WHERE  inspection_category_id=2;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10002   WHERE  inspection_category_id=3;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10003   WHERE  inspection_category_id=4;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10004   WHERE  inspection_category_id=5;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10005   WHERE  inspection_category_id=6;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10006   WHERE  inspection_category_id=7;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10007   WHERE  inspection_category_id=8;

UPDATE `eh_equipment_inspection_standards`
SET inspection_category_id = 10008   WHERE  inspection_category_id=9;



-- 迁移修改任务日志 inspection_category_id

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10000   WHERE  inspection_category_id=1;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10001   WHERE  inspection_category_id=2;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10002   WHERE  inspection_category_id=3;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10003   WHERE  inspection_category_id=4;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10004   WHERE  inspection_category_id=5;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10005   WHERE  inspection_category_id=6;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10006   WHERE  inspection_category_id=7;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10007   WHERE  inspection_category_id=8;

UPDATE `eh_equipment_inspection_task_logs`
SET inspection_category_id = 10008   WHERE  inspection_category_id=9;



-- 迁移修改任务表 inspection_category_id

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10000   WHERE  inspection_category_id=1;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10001   WHERE  inspection_category_id=2;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10002   WHERE  inspection_category_id=3;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10003   WHERE  inspection_category_id=4;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10004   WHERE  inspection_category_id=5;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10005   WHERE  inspection_category_id=6;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10006   WHERE  inspection_category_id=7;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10007   WHERE  inspection_category_id=8;

UPDATE `eh_equipment_inspection_tasks`
SET inspection_category_id = 10008   WHERE  inspection_category_id=9;


-- 后台管理菜单

INSERT INTO `eh_web_menus`  VALUES ('2080000', '物业巡检', '2000000', '', 'equipment-management', '1', '2', '/2000000/2080000', 'zuolin', '2', '20800', '2', 'system', 'module');
INSERT INTO `eh_web_menus`  VALUES ('2081000', '物业巡检', '2080000', '', 'equipment-management', '1', '2', '/2000000/2080000/2081000', 'zuolin', '2', '20800', '3', 'system', 'page');

-- merge from fixPm-1.0  by jiarui   20171130

-- merge from yuekongjian 1.0  by yanjun 201711301748  start

-- bydengs,20171114 物业报修2.9.3
SET @eh_categories_id = (SELECT MAX(id) FROM `eh_categories`);
SET @namespace_id = 999957;
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES
((@eh_categories_id:=@eh_categories_id+1), '6', '0', '物业报修', '任务/物业报修', '-1', '2', NOW(), NULL, NULL, NULL, @namespace_id);

SET @namespace_id = 999957;
DELETE  FROM  eh_configurations WHERE namespace_id = 0 AND `name`=CONCAT('pmtask.handler-',@namespace_id);
INSERT INTO `eh_configurations` (`name`, `value`,`description`) VALUES (CONCAT('pmtask.handler-',@namespace_id), 'yue_kong_jian','越空间物业报修的handler');


-- added by Janson 20171128
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)VALUES (1009, 0, '禁止门禁访客访问', '禁止门禁访客访问', NULL);

-- "我的"里面的菜单，一部分是默认的，一部分是杭州项目的 add by yanjun 20171129
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('1', '0', '我的收藏', '/activity/build/index.html#/myFavorite', NULL, 'cs://1/image/aW1hZ2UvTVRvelpEa3pObUUyTTJJek4yUXpaalE1TmpaaFpUQmhZV0kzTXpJM1pqWXhOZw', '1', '1', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('2', '0', '我的报名', '/activity/build/index.html#/mySignup', NULL, 'cs://1/image/aW1hZ2UvTVRwbVl6STFNRFZqTlRrd05HUmlZMkV5TXpjeVpHRTJaV1l4WVRZNU5qVmxNZw', '1', '2', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('3', '0', '查看我的团队/企业', '/enter-apply/build/index.html#/applyRecord', NULL, NULL, '2', '3', '2');

INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('11', '999957', '我的收藏', '/activity/build/index.html#/myFavorite', NULL, 'cs://1/image/aW1hZ2UvTVRvelpEa3pObUUyTTJJek4yUXpaalE1TmpaaFpUQmhZV0kzTXpJM1pqWXhOZw', '1', '1', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('12', '999957', '我的报名', '/activity/build/index.html#/mySignup', NULL, 'cs://1/image/aW1hZ2UvTVRwbVl6STFNRFZqTlRrd05HUmlZMkV5TXpjeVpHRTJaV1l4WVRZNU5qVmxNZw', '1', '2', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('13', '999957', '会员中心', '/service-hub/build/index.html#/waiting/101', NULL, 'cs://1/image/aW1hZ2UvTVRvME1HUTJZekF6TTJJMVpqZGhaVFZqTW1ZMVpHWmlZemt4WkRKalptVTNaQQ', '1', '3', '2');
INSERT INTO `eh_me_web_menus` (`id`, `namespace_id`, `name`, `action_path`, `action_data`, `icon_uri`, `position_flag`, `sort_num`, `status`) VALUES ('14', '999957', '意见反馈', '/service-hub/build/index.html#/waiting/102', NULL, 'cs://1/image/aW1hZ2UvTVRveE5ESmxNbVJsWVRFd01ESmpPV0ZqTWpVNU5UVTVPVEJoTURNMU9XWTROdw', '1', '4', '2');

-- 杭州越空间微信公众号 add by yanjun 20171130
SET @configId = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configId := @configId + 1), 'wx.offical.account.appid', 'wxf273770e5f383b2f', '公众号开发者AppId-杭州', '999957', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configId := @configId + 1), 'wx.offical.account.secret', '7b8267c856f293f2eeaeffbf062c3583', '公众号开发者AppId-杭州', '999957', NULL);

-- merge from yuekongjian 1.0  by yanjun 201711301748  end


SET @id := (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'address', '20006', 'zh_CN', '门牌已关联合同，无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'building', '10003', 'zh_CN', '楼栋下有门牌已关联合同，无法删除');
-- by wentian
SET @config_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.15', '/rest/LeaseContractChargeInfo/getLeaseContractBillOnFiProperty', '2.2.4.15接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.10', '/rest/LeaseContractChargeInfo/getLeaseContractReceivableGroupForStatistics', '2.2.4.10接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.9', '/rest/LeaseContractChargeInfo/getLeaseContractReceivableGroup', '2.2.4.9接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.8', '/rest/LeaseContractChargeInfo/getLeaseContractReceivable', '2.2.4.8接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.url', 'http://183.62.222.87:5902/sf', '科兴缴费url', '999983', NULL);

UPDATE `eh_communities` SET namespace_community_type='ebei', namespace_community_token = '6255265c-80bc-11e7-8e8b-020b31d69c8a' WHERE `namespace_id` = 999983 AND `name`= '科兴科学园';

SET @locale_id = (SELECT MAX(`id`) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id:=@locale_id+1,'assetv2','10006','zh_CN','远程请求一碑系统错误');

-- 正中会 配置账单管理和账单统计 by Wentian Wang
SET @eh_menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@eh_menu_scope_id:=@eh_menu_scope_id+1, '20400', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@eh_menu_scope_id:=@eh_menu_scope_id+1, '204011', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@eh_menu_scope_id:=@eh_menu_scope_id+1, '204021', '', 'EhNamespaces', '999983', '2');

-- 正中会url by wentian
SET @config_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'ebei.url', 'http://183.62.222.87:5902/sf', '一碑url', '0', NULL);
-- 张江高科支持新支付 by wentian
-- SET @config_id = (SELECT MAX(`id`) from `eh_configurations`);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'pay.platform', '1', '张江高科物业缴费新支付', '999971', NULL);

SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@field_id:=@field_id+1, 'contract', 'denunciationTime', '退约日期', 'Long', '15', '/13/15', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@field_id:=@field_id+1, 'contract', 'denunciationUid', '退约经办人', 'Long', '15', '/13/15', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@field_id:=@field_id+1, 'contract', 'buildingRename', '房间别名', 'Long', '15', '/13/15', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- 科兴基础数据 by xiongying
UPDATE eh_communities SET namespace_community_type = 'ebei' WHERE namespace_id = 999983;
UPDATE eh_communities SET namespace_community_token = '6255265c-80bc-11e7-8e8b-020b31d69c8a' WHERE namespace_id = 999983;


UPDATE eh_addresses SET namespace_address_type = 'ebei' WHERE namespace_id = 999983;

UPDATE eh_addresses SET namespace_address_token = '1578107' WHERE namespace_id = 999983 AND apartment_name = 'A1-0401A';
UPDATE eh_addresses SET namespace_address_token = '1578108' WHERE namespace_id = 999983 AND apartment_name = 'A1-0401B';
UPDATE eh_addresses SET namespace_address_token = '145048' WHERE namespace_id = 999983 AND apartment_name = 'A1-1001';
UPDATE eh_addresses SET namespace_address_token = '1381429' WHERE namespace_id = 999983 AND apartment_name = 'A1-1001A';
UPDATE eh_addresses SET namespace_address_token = '1381430' WHERE namespace_id = 999983 AND apartment_name = 'A1-1001B';
UPDATE eh_addresses SET namespace_address_token = '145049' WHERE namespace_id = 999983 AND apartment_name = 'A1-1002';
UPDATE eh_addresses SET namespace_address_token = '145050' WHERE namespace_id = 999983 AND apartment_name = 'A1-1003';
UPDATE eh_addresses SET namespace_address_token = '145051' WHERE namespace_id = 999983 AND apartment_name = 'A1-1005';
UPDATE eh_addresses SET namespace_address_token = '145052' WHERE namespace_id = 999983 AND apartment_name = 'A1-1101';
UPDATE eh_addresses SET namespace_address_token = '145053' WHERE namespace_id = 999983 AND apartment_name = 'A1-1102';
UPDATE eh_addresses SET namespace_address_token = '145054' WHERE namespace_id = 999983 AND apartment_name = 'A1-1103';
UPDATE eh_addresses SET namespace_address_token = '145055' WHERE namespace_id = 999983 AND apartment_name = 'A1-1105';
UPDATE eh_addresses SET namespace_address_token = '145056' WHERE namespace_id = 999983 AND apartment_name = 'A1-1201';
UPDATE eh_addresses SET namespace_address_token = '145057' WHERE namespace_id = 999983 AND apartment_name = 'A1-1202';
UPDATE eh_addresses SET namespace_address_token = '145058' WHERE namespace_id = 999983 AND apartment_name = 'A1-1203';
UPDATE eh_addresses SET namespace_address_token = '145059' WHERE namespace_id = 999983 AND apartment_name = 'A1-1205';
UPDATE eh_addresses SET namespace_address_token = '145060' WHERE namespace_id = 999983 AND apartment_name = 'A1-1301';
UPDATE eh_addresses SET namespace_address_token = '145061' WHERE namespace_id = 999983 AND apartment_name = 'A1-1302';
UPDATE eh_addresses SET namespace_address_token = '145062' WHERE namespace_id = 999983 AND apartment_name = 'A1-1303';
UPDATE eh_addresses SET namespace_address_token = '145063' WHERE namespace_id = 999983 AND apartment_name = 'A1-1305';
UPDATE eh_addresses SET namespace_address_token = '145064' WHERE namespace_id = 999983 AND apartment_name = 'A1-1401';
UPDATE eh_addresses SET namespace_address_token = '145065' WHERE namespace_id = 999983 AND apartment_name = 'A1-1402';
UPDATE eh_addresses SET namespace_address_token = '145066' WHERE namespace_id = 999983 AND apartment_name = 'A1-1403';
UPDATE eh_addresses SET namespace_address_token = '145067' WHERE namespace_id = 999983 AND apartment_name = 'A1-1501';
UPDATE eh_addresses SET namespace_address_token = '145070' WHERE namespace_id = 999983 AND apartment_name = 'A1-1505';
UPDATE eh_addresses SET namespace_address_token = '145068' WHERE namespace_id = 999983 AND apartment_name = 'A1-1502';
UPDATE eh_addresses SET namespace_address_token = '145069' WHERE namespace_id = 999983 AND apartment_name = 'A1-1503';
UPDATE eh_addresses SET namespace_address_token = '145071' WHERE namespace_id = 999983 AND apartment_name = 'A1-1601';
UPDATE eh_addresses SET namespace_address_token = '145072' WHERE namespace_id = 999983 AND apartment_name = 'A1-1602';
UPDATE eh_addresses SET namespace_address_token = '145073' WHERE namespace_id = 999983 AND apartment_name = 'A1-1603';
UPDATE eh_addresses SET namespace_address_token = '145074' WHERE namespace_id = 999983 AND apartment_name = 'A1-1605';
UPDATE eh_addresses SET namespace_address_token = '145075' WHERE namespace_id = 999983 AND apartment_name = 'A1-1701';
UPDATE eh_addresses SET namespace_address_token = '145076' WHERE namespace_id = 999983 AND apartment_name = 'A1-1702';
UPDATE eh_addresses SET namespace_address_token = '145077' WHERE namespace_id = 999983 AND apartment_name = 'A1-1801';
UPDATE eh_addresses SET namespace_address_token = '145078' WHERE namespace_id = 999983 AND apartment_name = 'A1-1802';
UPDATE eh_addresses SET namespace_address_token = '145079' WHERE namespace_id = 999983 AND apartment_name = 'A1-1803';
UPDATE eh_addresses SET namespace_address_token = '145080' WHERE namespace_id = 999983 AND apartment_name = 'A1-1901';
UPDATE eh_addresses SET namespace_address_token = '145081' WHERE namespace_id = 999983 AND apartment_name = 'A1-1902';
UPDATE eh_addresses SET namespace_address_token = '145017' WHERE namespace_id = 999983 AND apartment_name = 'A1-0201';
UPDATE eh_addresses SET namespace_address_token = '145018' WHERE namespace_id = 999983 AND apartment_name = 'A1-0202';
UPDATE eh_addresses SET namespace_address_token = '145019' WHERE namespace_id = 999983 AND apartment_name = 'A1-0203';
UPDATE eh_addresses SET namespace_address_token = '145020' WHERE namespace_id = 999983 AND apartment_name = 'A1-0301';
UPDATE eh_addresses SET namespace_address_token = '145021' WHERE namespace_id = 999983 AND apartment_name = 'A1-0302';
UPDATE eh_addresses SET namespace_address_token = '145022' WHERE namespace_id = 999983 AND apartment_name = 'A1-0303';
UPDATE eh_addresses SET namespace_address_token = '145023' WHERE namespace_id = 999983 AND apartment_name = 'A1-0305';
UPDATE eh_addresses SET namespace_address_token = '145024' WHERE namespace_id = 999983 AND apartment_name = 'A1-0401';
UPDATE eh_addresses SET namespace_address_token = '145025' WHERE namespace_id = 999983 AND apartment_name = 'A1-0402';
UPDATE eh_addresses SET namespace_address_token = '145026' WHERE namespace_id = 999983 AND apartment_name = 'A1-0403';
UPDATE eh_addresses SET namespace_address_token = '145027' WHERE namespace_id = 999983 AND apartment_name = 'A1-0405';
UPDATE eh_addresses SET namespace_address_token = '145028' WHERE namespace_id = 999983 AND apartment_name = 'A1-0501';
UPDATE eh_addresses SET namespace_address_token = '145029' WHERE namespace_id = 999983 AND apartment_name = 'A1-0502';
UPDATE eh_addresses SET namespace_address_token = '145030' WHERE namespace_id = 999983 AND apartment_name = 'A1-0503';
UPDATE eh_addresses SET namespace_address_token = '145031' WHERE namespace_id = 999983 AND apartment_name = 'A1-0505';
UPDATE eh_addresses SET namespace_address_token = '145032' WHERE namespace_id = 999983 AND apartment_name = 'A1-0601';
UPDATE eh_addresses SET namespace_address_token = '145033' WHERE namespace_id = 999983 AND apartment_name = 'A1-0602';
UPDATE eh_addresses SET namespace_address_token = '145034' WHERE namespace_id = 999983 AND apartment_name = 'A1-0603';
UPDATE eh_addresses SET namespace_address_token = '145035' WHERE namespace_id = 999983 AND apartment_name = 'A1-0605';
UPDATE eh_addresses SET namespace_address_token = '145036' WHERE namespace_id = 999983 AND apartment_name = 'A1-0701';
UPDATE eh_addresses SET namespace_address_token = '145038' WHERE namespace_id = 999983 AND apartment_name = 'A1-0702';
UPDATE eh_addresses SET namespace_address_token = '145039' WHERE namespace_id = 999983 AND apartment_name = 'A1-0703';
UPDATE eh_addresses SET namespace_address_token = '145037' WHERE namespace_id = 999983 AND apartment_name = 'A1-0705';
UPDATE eh_addresses SET namespace_address_token = '145040' WHERE namespace_id = 999983 AND apartment_name = 'A1-0801';
UPDATE eh_addresses SET namespace_address_token = '145043' WHERE namespace_id = 999983 AND apartment_name = 'A1-0802';
UPDATE eh_addresses SET namespace_address_token = '145041' WHERE namespace_id = 999983 AND apartment_name = 'A1-0803';
UPDATE eh_addresses SET namespace_address_token = '145042' WHERE namespace_id = 999983 AND apartment_name = 'A1-0805';
UPDATE eh_addresses SET namespace_address_token = '145044' WHERE namespace_id = 999983 AND apartment_name = 'A1-0901';
UPDATE eh_addresses SET namespace_address_token = '145045' WHERE namespace_id = 999983 AND apartment_name = 'A1-0902';
UPDATE eh_addresses SET namespace_address_token = '145046' WHERE namespace_id = 999983 AND apartment_name = 'A1-0903';
UPDATE eh_addresses SET namespace_address_token = '145047' WHERE namespace_id = 999983 AND apartment_name = 'A1-0905';
UPDATE eh_addresses SET namespace_address_token = '1567672' WHERE namespace_id = 999983 AND apartment_name = 'A2-0805';
UPDATE eh_addresses SET namespace_address_token = '1567673' WHERE namespace_id = 999983 AND apartment_name = 'A2-0806';
UPDATE eh_addresses SET namespace_address_token = '145119' WHERE namespace_id = 999983 AND apartment_name = 'A2-1001';
UPDATE eh_addresses SET namespace_address_token = '145120' WHERE namespace_id = 999983 AND apartment_name = 'A2-1002';
UPDATE eh_addresses SET namespace_address_token = '145121' WHERE namespace_id = 999983 AND apartment_name = 'A2-1003';
UPDATE eh_addresses SET namespace_address_token = '145122' WHERE namespace_id = 999983 AND apartment_name = 'A2-1005';
UPDATE eh_addresses SET namespace_address_token = '145123' WHERE namespace_id = 999983 AND apartment_name = 'A2-1006';
UPDATE eh_addresses SET namespace_address_token = '145124' WHERE namespace_id = 999983 AND apartment_name = 'A2-1101';
UPDATE eh_addresses SET namespace_address_token = '145125' WHERE namespace_id = 999983 AND apartment_name = 'A2-1102';
UPDATE eh_addresses SET namespace_address_token = '145126' WHERE namespace_id = 999983 AND apartment_name = 'A2-1103';
UPDATE eh_addresses SET namespace_address_token = '145127' WHERE namespace_id = 999983 AND apartment_name = 'A2-1105';
UPDATE eh_addresses SET namespace_address_token = '145128' WHERE namespace_id = 999983 AND apartment_name = 'A2-1106';
UPDATE eh_addresses SET namespace_address_token = '145129' WHERE namespace_id = 999983 AND apartment_name = 'A2-1201';
UPDATE eh_addresses SET namespace_address_token = '145130' WHERE namespace_id = 999983 AND apartment_name = 'A2-1202';
UPDATE eh_addresses SET namespace_address_token = '145131' WHERE namespace_id = 999983 AND apartment_name = 'A2-1203';
UPDATE eh_addresses SET namespace_address_token = '145132' WHERE namespace_id = 999983 AND apartment_name = 'A2-1205';
UPDATE eh_addresses SET namespace_address_token = '145133' WHERE namespace_id = 999983 AND apartment_name = 'A2-1206';
UPDATE eh_addresses SET namespace_address_token = '145134' WHERE namespace_id = 999983 AND apartment_name = 'A2-1301';
UPDATE eh_addresses SET namespace_address_token = '145135' WHERE namespace_id = 999983 AND apartment_name = 'A2-1302';
UPDATE eh_addresses SET namespace_address_token = '145136' WHERE namespace_id = 999983 AND apartment_name = 'A2-1303';
UPDATE eh_addresses SET namespace_address_token = '145137' WHERE namespace_id = 999983 AND apartment_name = 'A2-1305';
UPDATE eh_addresses SET namespace_address_token = '145138' WHERE namespace_id = 999983 AND apartment_name = 'A2-1306';
UPDATE eh_addresses SET namespace_address_token = '145139' WHERE namespace_id = 999983 AND apartment_name = 'A2-1401';
UPDATE eh_addresses SET namespace_address_token = '145140' WHERE namespace_id = 999983 AND apartment_name = 'A2-1402';
UPDATE eh_addresses SET namespace_address_token = '145141' WHERE namespace_id = 999983 AND apartment_name = 'A2-1403';
UPDATE eh_addresses SET namespace_address_token = '145142' WHERE namespace_id = 999983 AND apartment_name = 'A2-1405';
UPDATE eh_addresses SET namespace_address_token = '145143' WHERE namespace_id = 999983 AND apartment_name = 'A2-1406';
UPDATE eh_addresses SET namespace_address_token = '145144' WHERE namespace_id = 999983 AND apartment_name = 'A2-1501';
UPDATE eh_addresses SET namespace_address_token = '145145' WHERE namespace_id = 999983 AND apartment_name = 'A2-1502';
UPDATE eh_addresses SET namespace_address_token = '145146' WHERE namespace_id = 999983 AND apartment_name = 'A2-1503';
UPDATE eh_addresses SET namespace_address_token = '145147' WHERE namespace_id = 999983 AND apartment_name = 'A2-1505';
UPDATE eh_addresses SET namespace_address_token = '145148' WHERE namespace_id = 999983 AND apartment_name = 'A2-1506';
UPDATE eh_addresses SET namespace_address_token = '145149' WHERE namespace_id = 999983 AND apartment_name = 'A2-1601';
UPDATE eh_addresses SET namespace_address_token = '145150' WHERE namespace_id = 999983 AND apartment_name = 'A2-1602';
UPDATE eh_addresses SET namespace_address_token = '145151' WHERE namespace_id = 999983 AND apartment_name = 'A2-1603';
UPDATE eh_addresses SET namespace_address_token = '145152' WHERE namespace_id = 999983 AND apartment_name = 'A2-1605';
UPDATE eh_addresses SET namespace_address_token = '145153' WHERE namespace_id = 999983 AND apartment_name = 'A2-1701';
UPDATE eh_addresses SET namespace_address_token = '145154' WHERE namespace_id = 999983 AND apartment_name = 'A2-1702';
UPDATE eh_addresses SET namespace_address_token = '145155' WHERE namespace_id = 999983 AND apartment_name = 'A2-1801';
UPDATE eh_addresses SET namespace_address_token = '145156' WHERE namespace_id = 999983 AND apartment_name = 'A2-1802';
UPDATE eh_addresses SET namespace_address_token = '145157' WHERE namespace_id = 999983 AND apartment_name = 'A2-1803';
UPDATE eh_addresses SET namespace_address_token = '145158' WHERE namespace_id = 999983 AND apartment_name = 'A2-1805';
UPDATE eh_addresses SET namespace_address_token = '145159' WHERE namespace_id = 999983 AND apartment_name = 'A2-1901';
UPDATE eh_addresses SET namespace_address_token = '145160' WHERE namespace_id = 999983 AND apartment_name = 'A2-1902';
UPDATE eh_addresses SET namespace_address_token = '634198' WHERE namespace_id = 999983 AND apartment_name = 'A2-0201';
UPDATE eh_addresses SET namespace_address_token = '634199' WHERE namespace_id = 999983 AND apartment_name = 'A2-0202';
UPDATE eh_addresses SET namespace_address_token = '634200' WHERE namespace_id = 999983 AND apartment_name = 'A2-0203A';
UPDATE eh_addresses SET namespace_address_token = '634201' WHERE namespace_id = 999983 AND apartment_name = 'A2-0203B';
UPDATE eh_addresses SET namespace_address_token = '634202' WHERE namespace_id = 999983 AND apartment_name = 'A2-0205';
UPDATE eh_addresses SET namespace_address_token = '145085' WHERE namespace_id = 999983 AND apartment_name = 'A2-0301';
UPDATE eh_addresses SET namespace_address_token = '145086' WHERE namespace_id = 999983 AND apartment_name = 'A2-0302';
UPDATE eh_addresses SET namespace_address_token = '145087' WHERE namespace_id = 999983 AND apartment_name = 'A2-0303';
UPDATE eh_addresses SET namespace_address_token = '145088' WHERE namespace_id = 999983 AND apartment_name = 'A2-0305';
UPDATE eh_addresses SET namespace_address_token = '145089' WHERE namespace_id = 999983 AND apartment_name = 'A2-0306';
UPDATE eh_addresses SET namespace_address_token = '145090' WHERE namespace_id = 999983 AND apartment_name = 'A2-0401';
UPDATE eh_addresses SET namespace_address_token = '145091' WHERE namespace_id = 999983 AND apartment_name = 'A2-0402';
UPDATE eh_addresses SET namespace_address_token = '145092' WHERE namespace_id = 999983 AND apartment_name = 'A2-0403';
UPDATE eh_addresses SET namespace_address_token = '145093' WHERE namespace_id = 999983 AND apartment_name = 'A2-0405';
UPDATE eh_addresses SET namespace_address_token = '145094' WHERE namespace_id = 999983 AND apartment_name = 'A2-0406';
UPDATE eh_addresses SET namespace_address_token = '145095' WHERE namespace_id = 999983 AND apartment_name = 'A2-0501';
UPDATE eh_addresses SET namespace_address_token = '145096' WHERE namespace_id = 999983 AND apartment_name = 'A2-0502';
UPDATE eh_addresses SET namespace_address_token = '145097' WHERE namespace_id = 999983 AND apartment_name = 'A2-0601';
UPDATE eh_addresses SET namespace_address_token = '145098' WHERE namespace_id = 999983 AND apartment_name = 'A2-0602';
UPDATE eh_addresses SET namespace_address_token = '145099' WHERE namespace_id = 999983 AND apartment_name = 'A2-0603';
UPDATE eh_addresses SET namespace_address_token = '145100' WHERE namespace_id = 999983 AND apartment_name = 'A2-0605';
UPDATE eh_addresses SET namespace_address_token = '145101' WHERE namespace_id = 999983 AND apartment_name = 'A2-0606';
UPDATE eh_addresses SET namespace_address_token = '145102' WHERE namespace_id = 999983 AND apartment_name = 'A2-0701';
UPDATE eh_addresses SET namespace_address_token = '145103' WHERE namespace_id = 999983 AND apartment_name = 'A2-0702';
UPDATE eh_addresses SET namespace_address_token = '145104' WHERE namespace_id = 999983 AND apartment_name = 'A2-0703';
UPDATE eh_addresses SET namespace_address_token = '145105' WHERE namespace_id = 999983 AND apartment_name = 'A2-0705';
UPDATE eh_addresses SET namespace_address_token = '145106' WHERE namespace_id = 999983 AND apartment_name = 'A2-0706';
UPDATE eh_addresses SET namespace_address_token = '145107' WHERE namespace_id = 999983 AND apartment_name = 'A2-0801';
UPDATE eh_addresses SET namespace_address_token = '145108' WHERE namespace_id = 999983 AND apartment_name = 'A2-0802';
UPDATE eh_addresses SET namespace_address_token = '145109' WHERE namespace_id = 999983 AND apartment_name = 'A2-0803';
UPDATE eh_addresses SET namespace_address_token = '145110' WHERE namespace_id = 999983 AND apartment_name = 'A2-0805A';
UPDATE eh_addresses SET namespace_address_token = '145111' WHERE namespace_id = 999983 AND apartment_name = 'A2-0805B';
UPDATE eh_addresses SET namespace_address_token = '145112' WHERE namespace_id = 999983 AND apartment_name = 'A2-0806A';
UPDATE eh_addresses SET namespace_address_token = '145113' WHERE namespace_id = 999983 AND apartment_name = 'A2-0806B';
UPDATE eh_addresses SET namespace_address_token = '145114' WHERE namespace_id = 999983 AND apartment_name = 'A2-0901';
UPDATE eh_addresses SET namespace_address_token = '145115' WHERE namespace_id = 999983 AND apartment_name = 'A2-0902';
UPDATE eh_addresses SET namespace_address_token = '145116' WHERE namespace_id = 999983 AND apartment_name = 'A2-0903';
UPDATE eh_addresses SET namespace_address_token = '145117' WHERE namespace_id = 999983 AND apartment_name = 'A2-0905';
UPDATE eh_addresses SET namespace_address_token = '145118' WHERE namespace_id = 999983 AND apartment_name = 'A2-0906';
UPDATE eh_addresses SET namespace_address_token = '1456954' WHERE namespace_id = 999983 AND apartment_name = 'A3-0201FF';
UPDATE eh_addresses SET namespace_address_token = '1456955' WHERE namespace_id = 999983 AND apartment_name = 'A3-0601FF';
UPDATE eh_addresses SET namespace_address_token = '145206' WHERE namespace_id = 999983 AND apartment_name = 'A3-1001';
UPDATE eh_addresses SET namespace_address_token = '145207' WHERE namespace_id = 999983 AND apartment_name = 'A3-1002';
UPDATE eh_addresses SET namespace_address_token = '145208' WHERE namespace_id = 999983 AND apartment_name = 'A3-1003';
UPDATE eh_addresses SET namespace_address_token = '145209' WHERE namespace_id = 999983 AND apartment_name = 'A3-1005';
UPDATE eh_addresses SET namespace_address_token = '145210' WHERE namespace_id = 999983 AND apartment_name = 'A3-1006';
UPDATE eh_addresses SET namespace_address_token = '145211' WHERE namespace_id = 999983 AND apartment_name = 'A3-1101';
UPDATE eh_addresses SET namespace_address_token = '145212' WHERE namespace_id = 999983 AND apartment_name = 'A3-1102';
UPDATE eh_addresses SET namespace_address_token = '145213' WHERE namespace_id = 999983 AND apartment_name = 'A3-1103';
UPDATE eh_addresses SET namespace_address_token = '145214' WHERE namespace_id = 999983 AND apartment_name = 'A3-1105';
UPDATE eh_addresses SET namespace_address_token = '145215' WHERE namespace_id = 999983 AND apartment_name = 'A3-1106';
UPDATE eh_addresses SET namespace_address_token = '145216' WHERE namespace_id = 999983 AND apartment_name = 'A3-1201';
UPDATE eh_addresses SET namespace_address_token = '145217' WHERE namespace_id = 999983 AND apartment_name = 'A3-1202';
UPDATE eh_addresses SET namespace_address_token = '145218' WHERE namespace_id = 999983 AND apartment_name = 'A3-1203';
UPDATE eh_addresses SET namespace_address_token = '145219' WHERE namespace_id = 999983 AND apartment_name = 'A3-1205';
UPDATE eh_addresses SET namespace_address_token = '145220' WHERE namespace_id = 999983 AND apartment_name = 'A3-1206';
UPDATE eh_addresses SET namespace_address_token = '145221' WHERE namespace_id = 999983 AND apartment_name = 'A3-1301';
UPDATE eh_addresses SET namespace_address_token = '145222' WHERE namespace_id = 999983 AND apartment_name = 'A3-1302';
UPDATE eh_addresses SET namespace_address_token = '145223' WHERE namespace_id = 999983 AND apartment_name = 'A3-1303';
UPDATE eh_addresses SET namespace_address_token = '145224' WHERE namespace_id = 999983 AND apartment_name = 'A3-1305';
UPDATE eh_addresses SET namespace_address_token = '145225' WHERE namespace_id = 999983 AND apartment_name = 'A3-1306';
UPDATE eh_addresses SET namespace_address_token = '145226' WHERE namespace_id = 999983 AND apartment_name = 'A3-1401';
UPDATE eh_addresses SET namespace_address_token = '145227' WHERE namespace_id = 999983 AND apartment_name = 'A3-1402';
UPDATE eh_addresses SET namespace_address_token = '145228' WHERE namespace_id = 999983 AND apartment_name = 'A3-1403';
UPDATE eh_addresses SET namespace_address_token = '145229' WHERE namespace_id = 999983 AND apartment_name = 'A3-1405';
UPDATE eh_addresses SET namespace_address_token = '145230' WHERE namespace_id = 999983 AND apartment_name = 'A3-1406';
UPDATE eh_addresses SET namespace_address_token = '145231' WHERE namespace_id = 999983 AND apartment_name = 'A3-1501';
UPDATE eh_addresses SET namespace_address_token = '145232' WHERE namespace_id = 999983 AND apartment_name = 'A3-1502';
UPDATE eh_addresses SET namespace_address_token = '145233' WHERE namespace_id = 999983 AND apartment_name = 'A3-1503';
UPDATE eh_addresses SET namespace_address_token = '145234' WHERE namespace_id = 999983 AND apartment_name = 'A3-1505';
UPDATE eh_addresses SET namespace_address_token = '145235' WHERE namespace_id = 999983 AND apartment_name = 'A3-1506';
UPDATE eh_addresses SET namespace_address_token = '145236' WHERE namespace_id = 999983 AND apartment_name = 'A3-1601';
UPDATE eh_addresses SET namespace_address_token = '145237' WHERE namespace_id = 999983 AND apartment_name = 'A3-1602';
UPDATE eh_addresses SET namespace_address_token = '145238' WHERE namespace_id = 999983 AND apartment_name = 'A3-1603';
UPDATE eh_addresses SET namespace_address_token = '145239' WHERE namespace_id = 999983 AND apartment_name = 'A3-1605';
UPDATE eh_addresses SET namespace_address_token = '145240' WHERE namespace_id = 999983 AND apartment_name = 'A3-1606';
UPDATE eh_addresses SET namespace_address_token = '145241' WHERE namespace_id = 999983 AND apartment_name = 'A3-1701';
UPDATE eh_addresses SET namespace_address_token = '145242' WHERE namespace_id = 999983 AND apartment_name = 'A3-1702';
UPDATE eh_addresses SET namespace_address_token = '145243' WHERE namespace_id = 999983 AND apartment_name = 'A3-1703';
UPDATE eh_addresses SET namespace_address_token = '145705' WHERE namespace_id = 999983 AND apartment_name = 'A3-1705';
UPDATE eh_addresses SET namespace_address_token = '145244' WHERE namespace_id = 999983 AND apartment_name = 'A3-1801';
UPDATE eh_addresses SET namespace_address_token = '145245' WHERE namespace_id = 999983 AND apartment_name = 'A3-1802';
UPDATE eh_addresses SET namespace_address_token = '145246' WHERE namespace_id = 999983 AND apartment_name = 'A3-1803';
UPDATE eh_addresses SET namespace_address_token = '145247' WHERE namespace_id = 999983 AND apartment_name = 'A3-1805';
UPDATE eh_addresses SET namespace_address_token = '145248' WHERE namespace_id = 999983 AND apartment_name = 'A3-1806';
UPDATE eh_addresses SET namespace_address_token = '145249' WHERE namespace_id = 999983 AND apartment_name = 'A3-1901';
UPDATE eh_addresses SET namespace_address_token = '145250' WHERE namespace_id = 999983 AND apartment_name = 'A3-1902';
UPDATE eh_addresses SET namespace_address_token = '145161' WHERE namespace_id = 999983 AND apartment_name = 'A3-0201';
UPDATE eh_addresses SET namespace_address_token = '145162' WHERE namespace_id = 999983 AND apartment_name = 'A3-0202';
UPDATE eh_addresses SET namespace_address_token = '145163' WHERE namespace_id = 999983 AND apartment_name = 'A3-0203';
UPDATE eh_addresses SET namespace_address_token = '145164' WHERE namespace_id = 999983 AND apartment_name = 'A3-0205';
UPDATE eh_addresses SET namespace_address_token = '145165' WHERE namespace_id = 999983 AND apartment_name = 'A3-0206';
UPDATE eh_addresses SET namespace_address_token = '145166' WHERE namespace_id = 999983 AND apartment_name = 'A3-0301';
UPDATE eh_addresses SET namespace_address_token = '145167' WHERE namespace_id = 999983 AND apartment_name = 'A3-0302';
UPDATE eh_addresses SET namespace_address_token = '145168' WHERE namespace_id = 999983 AND apartment_name = 'A3-0303';
UPDATE eh_addresses SET namespace_address_token = '145169' WHERE namespace_id = 999983 AND apartment_name = 'A3-0305';
UPDATE eh_addresses SET namespace_address_token = '145170' WHERE namespace_id = 999983 AND apartment_name = 'A3-0306';
UPDATE eh_addresses SET namespace_address_token = '145171' WHERE namespace_id = 999983 AND apartment_name = 'A3-0307';
UPDATE eh_addresses SET namespace_address_token = '145172' WHERE namespace_id = 999983 AND apartment_name = 'A3-0401';
UPDATE eh_addresses SET namespace_address_token = '145173' WHERE namespace_id = 999983 AND apartment_name = 'A3-0402';
UPDATE eh_addresses SET namespace_address_token = '145174' WHERE namespace_id = 999983 AND apartment_name = 'A3-0403';
UPDATE eh_addresses SET namespace_address_token = '145175' WHERE namespace_id = 999983 AND apartment_name = 'A3-0405';
UPDATE eh_addresses SET namespace_address_token = '145176' WHERE namespace_id = 999983 AND apartment_name = 'A3-0406';
UPDATE eh_addresses SET namespace_address_token = '145177' WHERE namespace_id = 999983 AND apartment_name = 'A3-0407';
UPDATE eh_addresses SET namespace_address_token = '145178' WHERE namespace_id = 999983 AND apartment_name = 'A3-0501';
UPDATE eh_addresses SET namespace_address_token = '145179' WHERE namespace_id = 999983 AND apartment_name = 'A3-0502';
UPDATE eh_addresses SET namespace_address_token = '145180' WHERE namespace_id = 999983 AND apartment_name = 'A3-0503';
UPDATE eh_addresses SET namespace_address_token = '145181' WHERE namespace_id = 999983 AND apartment_name = 'A3-0505';
UPDATE eh_addresses SET namespace_address_token = '145182' WHERE namespace_id = 999983 AND apartment_name = 'A3-0506';
UPDATE eh_addresses SET namespace_address_token = '145183' WHERE namespace_id = 999983 AND apartment_name = 'A3-0507';
UPDATE eh_addresses SET namespace_address_token = '145184' WHERE namespace_id = 999983 AND apartment_name = 'A3-0601';
UPDATE eh_addresses SET namespace_address_token = '145185' WHERE namespace_id = 999983 AND apartment_name = 'A3-0602';
UPDATE eh_addresses SET namespace_address_token = '145186' WHERE namespace_id = 999983 AND apartment_name = 'A3-0603';
UPDATE eh_addresses SET namespace_address_token = '145187' WHERE namespace_id = 999983 AND apartment_name = 'A3-0605';
UPDATE eh_addresses SET namespace_address_token = '145188' WHERE namespace_id = 999983 AND apartment_name = 'A3-0606';
UPDATE eh_addresses SET namespace_address_token = '145189' WHERE namespace_id = 999983 AND apartment_name = 'A3-0607';
UPDATE eh_addresses SET namespace_address_token = '145190' WHERE namespace_id = 999983 AND apartment_name = 'A3-0701';
UPDATE eh_addresses SET namespace_address_token = '145191' WHERE namespace_id = 999983 AND apartment_name = 'A3-0702';
UPDATE eh_addresses SET namespace_address_token = '145192' WHERE namespace_id = 999983 AND apartment_name = 'A3-0703';
UPDATE eh_addresses SET namespace_address_token = '145193' WHERE namespace_id = 999983 AND apartment_name = 'A3-0705';
UPDATE eh_addresses SET namespace_address_token = '145194' WHERE namespace_id = 999983 AND apartment_name = 'A3-0706';
UPDATE eh_addresses SET namespace_address_token = '145195' WHERE namespace_id = 999983 AND apartment_name = 'A3-0707';
UPDATE eh_addresses SET namespace_address_token = '145196' WHERE namespace_id = 999983 AND apartment_name = 'A3-0801';
UPDATE eh_addresses SET namespace_address_token = '145197' WHERE namespace_id = 999983 AND apartment_name = 'A3-0802';
UPDATE eh_addresses SET namespace_address_token = '145198' WHERE namespace_id = 999983 AND apartment_name = 'A3-0803';
UPDATE eh_addresses SET namespace_address_token = '145199' WHERE namespace_id = 999983 AND apartment_name = 'A3-0805';
UPDATE eh_addresses SET namespace_address_token = '145200' WHERE namespace_id = 999983 AND apartment_name = 'A3-0806';
UPDATE eh_addresses SET namespace_address_token = '145201' WHERE namespace_id = 999983 AND apartment_name = 'A3-0901';
UPDATE eh_addresses SET namespace_address_token = '145202' WHERE namespace_id = 999983 AND apartment_name = 'A3-0902';
UPDATE eh_addresses SET namespace_address_token = '145203' WHERE namespace_id = 999983 AND apartment_name = 'A3-0903';
UPDATE eh_addresses SET namespace_address_token = '145204' WHERE namespace_id = 999983 AND apartment_name = 'A3-0905';
UPDATE eh_addresses SET namespace_address_token = '145205' WHERE namespace_id = 999983 AND apartment_name = 'A3-0906';
UPDATE eh_addresses SET namespace_address_token = '145704' WHERE namespace_id = 999983 AND apartment_name = 'A3-B101';
UPDATE eh_addresses SET namespace_address_token = '1421935' WHERE namespace_id = 999983 AND apartment_name = 'A4-1202A';
UPDATE eh_addresses SET namespace_address_token = '1421936' WHERE namespace_id = 999983 AND apartment_name = 'A4-1202B';
UPDATE eh_addresses SET namespace_address_token = '1456420' WHERE namespace_id = 999983 AND apartment_name = 'A4-1401A';
UPDATE eh_addresses SET namespace_address_token = '1456421' WHERE namespace_id = 999983 AND apartment_name = 'A4-1401B';
UPDATE eh_addresses SET namespace_address_token = '145298' WHERE namespace_id = 999983 AND apartment_name = 'A4-1001';
UPDATE eh_addresses SET namespace_address_token = '145299' WHERE namespace_id = 999983 AND apartment_name = 'A4-1002';
UPDATE eh_addresses SET namespace_address_token = '145300' WHERE namespace_id = 999983 AND apartment_name = 'A4-1003';
UPDATE eh_addresses SET namespace_address_token = '145301' WHERE namespace_id = 999983 AND apartment_name = 'A4-1005';
UPDATE eh_addresses SET namespace_address_token = '145302' WHERE namespace_id = 999983 AND apartment_name = 'A4-1006';
UPDATE eh_addresses SET namespace_address_token = '145303' WHERE namespace_id = 999983 AND apartment_name = 'A4-1007';
UPDATE eh_addresses SET namespace_address_token = '145304' WHERE namespace_id = 999983 AND apartment_name = 'A4-1008';
UPDATE eh_addresses SET namespace_address_token = '145305' WHERE namespace_id = 999983 AND apartment_name = 'A4-1101';
UPDATE eh_addresses SET namespace_address_token = '145306' WHERE namespace_id = 999983 AND apartment_name = 'A4-1102';
UPDATE eh_addresses SET namespace_address_token = '145307' WHERE namespace_id = 999983 AND apartment_name = 'A4-1103';
UPDATE eh_addresses SET namespace_address_token = '145308' WHERE namespace_id = 999983 AND apartment_name = 'A4-1105';
UPDATE eh_addresses SET namespace_address_token = '145309' WHERE namespace_id = 999983 AND apartment_name = 'A4-1106';
UPDATE eh_addresses SET namespace_address_token = '145310' WHERE namespace_id = 999983 AND apartment_name = 'A4-1107';
UPDATE eh_addresses SET namespace_address_token = '915820' WHERE namespace_id = 999983 AND apartment_name = 'A4-1201A';
UPDATE eh_addresses SET namespace_address_token = '915822' WHERE namespace_id = 999983 AND apartment_name = 'A4-1201B';
UPDATE eh_addresses SET namespace_address_token = '145311' WHERE namespace_id = 999983 AND apartment_name = 'A4-1201';
UPDATE eh_addresses SET namespace_address_token = '145312' WHERE namespace_id = 999983 AND apartment_name = 'A4-1202';
UPDATE eh_addresses SET namespace_address_token = '145313' WHERE namespace_id = 999983 AND apartment_name = 'A4-1203';
UPDATE eh_addresses SET namespace_address_token = '145314' WHERE namespace_id = 999983 AND apartment_name = 'A4-1205';
UPDATE eh_addresses SET namespace_address_token = '145315' WHERE namespace_id = 999983 AND apartment_name = 'A4-1206';
UPDATE eh_addresses SET namespace_address_token = '145316' WHERE namespace_id = 999983 AND apartment_name = 'A4-1207A';
UPDATE eh_addresses SET namespace_address_token = '145317' WHERE namespace_id = 999983 AND apartment_name = 'A4-1207B';
UPDATE eh_addresses SET namespace_address_token = '145318' WHERE namespace_id = 999983 AND apartment_name = 'A4-1301';
UPDATE eh_addresses SET namespace_address_token = '145319' WHERE namespace_id = 999983 AND apartment_name = 'A4-1302';
UPDATE eh_addresses SET namespace_address_token = '145320' WHERE namespace_id = 999983 AND apartment_name = 'A4-1303';
UPDATE eh_addresses SET namespace_address_token = '145321' WHERE namespace_id = 999983 AND apartment_name = 'A4-1305';
UPDATE eh_addresses SET namespace_address_token = '145322' WHERE namespace_id = 999983 AND apartment_name = 'A4-1306';
UPDATE eh_addresses SET namespace_address_token = '145323' WHERE namespace_id = 999983 AND apartment_name = 'A4-1307';
UPDATE eh_addresses SET namespace_address_token = '145324' WHERE namespace_id = 999983 AND apartment_name = 'A4-1401';
UPDATE eh_addresses SET namespace_address_token = '145325' WHERE namespace_id = 999983 AND apartment_name = 'A4-1402';
UPDATE eh_addresses SET namespace_address_token = '145326' WHERE namespace_id = 999983 AND apartment_name = 'A4-1403';
UPDATE eh_addresses SET namespace_address_token = '145327' WHERE namespace_id = 999983 AND apartment_name = 'A4-1405';
UPDATE eh_addresses SET namespace_address_token = '145328' WHERE namespace_id = 999983 AND apartment_name = 'A4-1406';
UPDATE eh_addresses SET namespace_address_token = '145329' WHERE namespace_id = 999983 AND apartment_name = 'A4-1407';
UPDATE eh_addresses SET namespace_address_token = '145334' WHERE namespace_id = 999983 AND apartment_name = 'A4-1506';
UPDATE eh_addresses SET namespace_address_token = '145330' WHERE namespace_id = 999983 AND apartment_name = 'A4-1501';
UPDATE eh_addresses SET namespace_address_token = '145331' WHERE namespace_id = 999983 AND apartment_name = 'A4-1502';
UPDATE eh_addresses SET namespace_address_token = '145332' WHERE namespace_id = 999983 AND apartment_name = 'A4-1503';
UPDATE eh_addresses SET namespace_address_token = '145333' WHERE namespace_id = 999983 AND apartment_name = 'A4-1505';
UPDATE eh_addresses SET namespace_address_token = '145335' WHERE namespace_id = 999983 AND apartment_name = 'A4-1507';
UPDATE eh_addresses SET namespace_address_token = '145336' WHERE namespace_id = 999983 AND apartment_name = 'A4-1601';
UPDATE eh_addresses SET namespace_address_token = '145337' WHERE namespace_id = 999983 AND apartment_name = 'A4-1602';
UPDATE eh_addresses SET namespace_address_token = '145338' WHERE namespace_id = 999983 AND apartment_name = 'A4-1603';
UPDATE eh_addresses SET namespace_address_token = '145339' WHERE namespace_id = 999983 AND apartment_name = 'A4-1701';
UPDATE eh_addresses SET namespace_address_token = '145340' WHERE namespace_id = 999983 AND apartment_name = 'A4-1702';
UPDATE eh_addresses SET namespace_address_token = '145341' WHERE namespace_id = 999983 AND apartment_name = 'A4-1703';
UPDATE eh_addresses SET namespace_address_token = '145342' WHERE namespace_id = 999983 AND apartment_name = 'A4-1801';
UPDATE eh_addresses SET namespace_address_token = '145343' WHERE namespace_id = 999983 AND apartment_name = 'A4-1802';
UPDATE eh_addresses SET namespace_address_token = '145344' WHERE namespace_id = 999983 AND apartment_name = 'A4-1803';
UPDATE eh_addresses SET namespace_address_token = '145345' WHERE namespace_id = 999983 AND apartment_name = 'A4-1901';
UPDATE eh_addresses SET namespace_address_token = '145346' WHERE namespace_id = 999983 AND apartment_name = 'A4-1902';
UPDATE eh_addresses SET namespace_address_token = '145347' WHERE namespace_id = 999983 AND apartment_name = 'A4-1903';
UPDATE eh_addresses SET namespace_address_token = '145251' WHERE namespace_id = 999983 AND apartment_name = 'A4-0201';
UPDATE eh_addresses SET namespace_address_token = '145252' WHERE namespace_id = 999983 AND apartment_name = 'A4-0202';
UPDATE eh_addresses SET namespace_address_token = '145253' WHERE namespace_id = 999983 AND apartment_name = 'A4-0203';
UPDATE eh_addresses SET namespace_address_token = '145254' WHERE namespace_id = 999983 AND apartment_name = 'A4-0301';
UPDATE eh_addresses SET namespace_address_token = '145255' WHERE namespace_id = 999983 AND apartment_name = 'A4-0302';
UPDATE eh_addresses SET namespace_address_token = '145256' WHERE namespace_id = 999983 AND apartment_name = 'A4-0303';
UPDATE eh_addresses SET namespace_address_token = '145257' WHERE namespace_id = 999983 AND apartment_name = 'A4-0305';
UPDATE eh_addresses SET namespace_address_token = '145258' WHERE namespace_id = 999983 AND apartment_name = 'A4-0306';
UPDATE eh_addresses SET namespace_address_token = '145260' WHERE namespace_id = 999983 AND apartment_name = 'A4-0402';
UPDATE eh_addresses SET namespace_address_token = '145259' WHERE namespace_id = 999983 AND apartment_name = 'A4-0401';
UPDATE eh_addresses SET namespace_address_token = '145261' WHERE namespace_id = 999983 AND apartment_name = 'A4-0403';
UPDATE eh_addresses SET namespace_address_token = '145265' WHERE namespace_id = 999983 AND apartment_name = 'A4-0505';
UPDATE eh_addresses SET namespace_address_token = '145262' WHERE namespace_id = 999983 AND apartment_name = 'A4-0501';
UPDATE eh_addresses SET namespace_address_token = '145263' WHERE namespace_id = 999983 AND apartment_name = 'A4-0502';
UPDATE eh_addresses SET namespace_address_token = '145264' WHERE namespace_id = 999983 AND apartment_name = 'A4-0503';
UPDATE eh_addresses SET namespace_address_token = '145266' WHERE namespace_id = 999983 AND apartment_name = 'A4-0506';
UPDATE eh_addresses SET namespace_address_token = '1416577' WHERE namespace_id = 999983 AND apartment_name = 'A4-0506A';
UPDATE eh_addresses SET namespace_address_token = '1416578' WHERE namespace_id = 999983 AND apartment_name = 'A4-0506B';
UPDATE eh_addresses SET namespace_address_token = '145267' WHERE namespace_id = 999983 AND apartment_name = 'A4-0507';
UPDATE eh_addresses SET namespace_address_token = '145268' WHERE namespace_id = 999983 AND apartment_name = 'A4-0601';
UPDATE eh_addresses SET namespace_address_token = '145269' WHERE namespace_id = 999983 AND apartment_name = 'A4-0602';
UPDATE eh_addresses SET namespace_address_token = '145270' WHERE namespace_id = 999983 AND apartment_name = 'A4-0603';
UPDATE eh_addresses SET namespace_address_token = '145271' WHERE namespace_id = 999983 AND apartment_name = 'A4-0605';
UPDATE eh_addresses SET namespace_address_token = '145272' WHERE namespace_id = 999983 AND apartment_name = 'A4-0606';
UPDATE eh_addresses SET namespace_address_token = '145273' WHERE namespace_id = 999983 AND apartment_name = 'A4-0607';
UPDATE eh_addresses SET namespace_address_token = '145274' WHERE namespace_id = 999983 AND apartment_name = 'A4-0701';
UPDATE eh_addresses SET namespace_address_token = '145275' WHERE namespace_id = 999983 AND apartment_name = 'A4-0702';
UPDATE eh_addresses SET namespace_address_token = '145276' WHERE namespace_id = 999983 AND apartment_name = 'A4-0703';
UPDATE eh_addresses SET namespace_address_token = '145277' WHERE namespace_id = 999983 AND apartment_name = 'A4-0705';
UPDATE eh_addresses SET namespace_address_token = '145278' WHERE namespace_id = 999983 AND apartment_name = 'A4-0706';
UPDATE eh_addresses SET namespace_address_token = '145279' WHERE namespace_id = 999983 AND apartment_name = 'A4-0707';
UPDATE eh_addresses SET namespace_address_token = '145280' WHERE namespace_id = 999983 AND apartment_name = 'A4-0708';
UPDATE eh_addresses SET namespace_address_token = '145281' WHERE namespace_id = 999983 AND apartment_name = 'A4-0709';
UPDATE eh_addresses SET namespace_address_token = '145282' WHERE namespace_id = 999983 AND apartment_name = 'A4-0801';
UPDATE eh_addresses SET namespace_address_token = '145283' WHERE namespace_id = 999983 AND apartment_name = 'A4-0802';
UPDATE eh_addresses SET namespace_address_token = '145284' WHERE namespace_id = 999983 AND apartment_name = 'A4-0803';
UPDATE eh_addresses SET namespace_address_token = '145285' WHERE namespace_id = 999983 AND apartment_name = 'A4-0805';
UPDATE eh_addresses SET namespace_address_token = '145286' WHERE namespace_id = 999983 AND apartment_name = 'A4-0806';
UPDATE eh_addresses SET namespace_address_token = '145287' WHERE namespace_id = 999983 AND apartment_name = 'A4-0807';
UPDATE eh_addresses SET namespace_address_token = '145288' WHERE namespace_id = 999983 AND apartment_name = 'A4-0808';
UPDATE eh_addresses SET namespace_address_token = '145289' WHERE namespace_id = 999983 AND apartment_name = 'A4-0809';
UPDATE eh_addresses SET namespace_address_token = '145290' WHERE namespace_id = 999983 AND apartment_name = 'A4-0901';
UPDATE eh_addresses SET namespace_address_token = '145291' WHERE namespace_id = 999983 AND apartment_name = 'A4-0902';
UPDATE eh_addresses SET namespace_address_token = '145292' WHERE namespace_id = 999983 AND apartment_name = 'A4-0903';
UPDATE eh_addresses SET namespace_address_token = '145293' WHERE namespace_id = 999983 AND apartment_name = 'A4-0905';
UPDATE eh_addresses SET namespace_address_token = '145294' WHERE namespace_id = 999983 AND apartment_name = 'A4-0906';
UPDATE eh_addresses SET namespace_address_token = '145295' WHERE namespace_id = 999983 AND apartment_name = 'A4-0907';
UPDATE eh_addresses SET namespace_address_token = '145296' WHERE namespace_id = 999983 AND apartment_name = 'A4-0908';
UPDATE eh_addresses SET namespace_address_token = '145297' WHERE namespace_id = 999983 AND apartment_name = 'A4-0909';
UPDATE eh_addresses SET namespace_address_token = '145374' WHERE namespace_id = 999983 AND apartment_name = 'B1-1001';
UPDATE eh_addresses SET namespace_address_token = '145375' WHERE namespace_id = 999983 AND apartment_name = 'B1-1002';
UPDATE eh_addresses SET namespace_address_token = '145376' WHERE namespace_id = 999983 AND apartment_name = 'B1-1003';
UPDATE eh_addresses SET namespace_address_token = '145377' WHERE namespace_id = 999983 AND apartment_name = 'B1-1101';
UPDATE eh_addresses SET namespace_address_token = '145378' WHERE namespace_id = 999983 AND apartment_name = 'B1-1102';
UPDATE eh_addresses SET namespace_address_token = '145379' WHERE namespace_id = 999983 AND apartment_name = 'B1-1103';
UPDATE eh_addresses SET namespace_address_token = '145380' WHERE namespace_id = 999983 AND apartment_name = 'B1-1201';
UPDATE eh_addresses SET namespace_address_token = '145381' WHERE namespace_id = 999983 AND apartment_name = 'B1-1202';
UPDATE eh_addresses SET namespace_address_token = '145382' WHERE namespace_id = 999983 AND apartment_name = 'B1-1203';
UPDATE eh_addresses SET namespace_address_token = '145383' WHERE namespace_id = 999983 AND apartment_name = 'B1-1301';
UPDATE eh_addresses SET namespace_address_token = '145384' WHERE namespace_id = 999983 AND apartment_name = 'B1-1302';
UPDATE eh_addresses SET namespace_address_token = '145385' WHERE namespace_id = 999983 AND apartment_name = 'B1-1303';
UPDATE eh_addresses SET namespace_address_token = '145386' WHERE namespace_id = 999983 AND apartment_name = 'B1-1401';
UPDATE eh_addresses SET namespace_address_token = '145387' WHERE namespace_id = 999983 AND apartment_name = 'B1-1402';
UPDATE eh_addresses SET namespace_address_token = '145388' WHERE namespace_id = 999983 AND apartment_name = 'B1-1403';
UPDATE eh_addresses SET namespace_address_token = '145389' WHERE namespace_id = 999983 AND apartment_name = 'B1-1501';
UPDATE eh_addresses SET namespace_address_token = '145390' WHERE namespace_id = 999983 AND apartment_name = 'B1-1502';
UPDATE eh_addresses SET namespace_address_token = '145391' WHERE namespace_id = 999983 AND apartment_name = 'B1-1503';
UPDATE eh_addresses SET namespace_address_token = '145392' WHERE namespace_id = 999983 AND apartment_name = 'B1-1601';
UPDATE eh_addresses SET namespace_address_token = '145393' WHERE namespace_id = 999983 AND apartment_name = 'B1-1602';
UPDATE eh_addresses SET namespace_address_token = '145394' WHERE namespace_id = 999983 AND apartment_name = 'B1-1701';
UPDATE eh_addresses SET namespace_address_token = '145395' WHERE namespace_id = 999983 AND apartment_name = 'B1-1702';
UPDATE eh_addresses SET namespace_address_token = '145396' WHERE namespace_id = 999983 AND apartment_name = 'B1-1703';
UPDATE eh_addresses SET namespace_address_token = '145397' WHERE namespace_id = 999983 AND apartment_name = 'B1-1801';
UPDATE eh_addresses SET namespace_address_token = '145398' WHERE namespace_id = 999983 AND apartment_name = 'B1-1802';
UPDATE eh_addresses SET namespace_address_token = '145348' WHERE namespace_id = 999983 AND apartment_name = 'B1-0101';
UPDATE eh_addresses SET namespace_address_token = '145349' WHERE namespace_id = 999983 AND apartment_name = 'B1-0201';
UPDATE eh_addresses SET namespace_address_token = '145350' WHERE namespace_id = 999983 AND apartment_name = 'B1-0202';
UPDATE eh_addresses SET namespace_address_token = '145351' WHERE namespace_id = 999983 AND apartment_name = 'B1-0301';
UPDATE eh_addresses SET namespace_address_token = '145352' WHERE namespace_id = 999983 AND apartment_name = 'B1-0302';
UPDATE eh_addresses SET namespace_address_token = '145353' WHERE namespace_id = 999983 AND apartment_name = 'B1-0303';
UPDATE eh_addresses SET namespace_address_token = '145354' WHERE namespace_id = 999983 AND apartment_name = 'B1-0401';
UPDATE eh_addresses SET namespace_address_token = '145355' WHERE namespace_id = 999983 AND apartment_name = 'B1-0402';
UPDATE eh_addresses SET namespace_address_token = '145356' WHERE namespace_id = 999983 AND apartment_name = 'B1-0403';
UPDATE eh_addresses SET namespace_address_token = '145357' WHERE namespace_id = 999983 AND apartment_name = 'B1-0501';
UPDATE eh_addresses SET namespace_address_token = '145358' WHERE namespace_id = 999983 AND apartment_name = 'B1-0502';
UPDATE eh_addresses SET namespace_address_token = '145359' WHERE namespace_id = 999983 AND apartment_name = 'B1-0503';
UPDATE eh_addresses SET namespace_address_token = '145360' WHERE namespace_id = 999983 AND apartment_name = 'B1-0505';
UPDATE eh_addresses SET namespace_address_token = '145361' WHERE namespace_id = 999983 AND apartment_name = 'B1-0601';
UPDATE eh_addresses SET namespace_address_token = '145362' WHERE namespace_id = 999983 AND apartment_name = 'B1-0602';
UPDATE eh_addresses SET namespace_address_token = '145363' WHERE namespace_id = 999983 AND apartment_name = 'B1-0603';
UPDATE eh_addresses SET namespace_address_token = '145364' WHERE namespace_id = 999983 AND apartment_name = 'B1-0701';
UPDATE eh_addresses SET namespace_address_token = '145365' WHERE namespace_id = 999983 AND apartment_name = 'B1-0702';
UPDATE eh_addresses SET namespace_address_token = '145366' WHERE namespace_id = 999983 AND apartment_name = 'B1-0703';
UPDATE eh_addresses SET namespace_address_token = '145367' WHERE namespace_id = 999983 AND apartment_name = 'B1-0801';
UPDATE eh_addresses SET namespace_address_token = '145369' WHERE namespace_id = 999983 AND apartment_name = 'B1-0802A';
UPDATE eh_addresses SET namespace_address_token = '145370' WHERE namespace_id = 999983 AND apartment_name = 'B1-0802B';
UPDATE eh_addresses SET namespace_address_token = '145368' WHERE namespace_id = 999983 AND apartment_name = 'B1-0803';
UPDATE eh_addresses SET namespace_address_token = '145371' WHERE namespace_id = 999983 AND apartment_name = 'B1-0901';
UPDATE eh_addresses SET namespace_address_token = '735055' WHERE namespace_id = 999983 AND apartment_name = 'B1-0901A';
UPDATE eh_addresses SET namespace_address_token = '145372' WHERE namespace_id = 999983 AND apartment_name = 'B1-0902';
UPDATE eh_addresses SET namespace_address_token = '145373' WHERE namespace_id = 999983 AND apartment_name = 'B1-0903';
UPDATE eh_addresses SET namespace_address_token = '145431' WHERE namespace_id = 999983 AND apartment_name = 'B2-1001';
UPDATE eh_addresses SET namespace_address_token = '145432' WHERE namespace_id = 999983 AND apartment_name = 'B2-1002';
UPDATE eh_addresses SET namespace_address_token = '145433' WHERE namespace_id = 999983 AND apartment_name = 'B2-1003';
UPDATE eh_addresses SET namespace_address_token = '145434' WHERE namespace_id = 999983 AND apartment_name = 'B2-1005';
UPDATE eh_addresses SET namespace_address_token = '145435' WHERE namespace_id = 999983 AND apartment_name = 'B2-1101';
UPDATE eh_addresses SET namespace_address_token = '145436' WHERE namespace_id = 999983 AND apartment_name = 'B2-1102';
UPDATE eh_addresses SET namespace_address_token = '145437' WHERE namespace_id = 999983 AND apartment_name = 'B2-1103';
UPDATE eh_addresses SET namespace_address_token = '145438' WHERE namespace_id = 999983 AND apartment_name = 'B2-1105';
UPDATE eh_addresses SET namespace_address_token = '145439' WHERE namespace_id = 999983 AND apartment_name = 'B2-1201';
UPDATE eh_addresses SET namespace_address_token = '145440' WHERE namespace_id = 999983 AND apartment_name = 'B2-1202';
UPDATE eh_addresses SET namespace_address_token = '145441' WHERE namespace_id = 999983 AND apartment_name = 'B2-1203';
UPDATE eh_addresses SET namespace_address_token = '145442' WHERE namespace_id = 999983 AND apartment_name = 'B2-1205';
UPDATE eh_addresses SET namespace_address_token = '145443' WHERE namespace_id = 999983 AND apartment_name = 'B2-1301';
UPDATE eh_addresses SET namespace_address_token = '145444' WHERE namespace_id = 999983 AND apartment_name = 'B2-1302';
UPDATE eh_addresses SET namespace_address_token = '145445' WHERE namespace_id = 999983 AND apartment_name = 'B2-1303';
UPDATE eh_addresses SET namespace_address_token = '145446' WHERE namespace_id = 999983 AND apartment_name = 'B2-1305';
UPDATE eh_addresses SET namespace_address_token = '145447' WHERE namespace_id = 999983 AND apartment_name = 'B2-1401';
UPDATE eh_addresses SET namespace_address_token = '145448' WHERE namespace_id = 999983 AND apartment_name = 'B2-1402';
UPDATE eh_addresses SET namespace_address_token = '145449' WHERE namespace_id = 999983 AND apartment_name = 'B2-1403';
UPDATE eh_addresses SET namespace_address_token = '145450' WHERE namespace_id = 999983 AND apartment_name = 'B2-1405';
UPDATE eh_addresses SET namespace_address_token = '145451' WHERE namespace_id = 999983 AND apartment_name = 'B2-1501';
UPDATE eh_addresses SET namespace_address_token = '145452' WHERE namespace_id = 999983 AND apartment_name = 'B2-1502';
UPDATE eh_addresses SET namespace_address_token = '145453' WHERE namespace_id = 999983 AND apartment_name = 'B2-1503';
UPDATE eh_addresses SET namespace_address_token = '145454' WHERE namespace_id = 999983 AND apartment_name = 'B2-1505';
UPDATE eh_addresses SET namespace_address_token = '145455' WHERE namespace_id = 999983 AND apartment_name = 'B2-1601';
UPDATE eh_addresses SET namespace_address_token = '145456' WHERE namespace_id = 999983 AND apartment_name = 'B2-1602';
UPDATE eh_addresses SET namespace_address_token = '145457' WHERE namespace_id = 999983 AND apartment_name = 'B2-1603';
UPDATE eh_addresses SET namespace_address_token = '145458' WHERE namespace_id = 999983 AND apartment_name = 'B2-1701';
UPDATE eh_addresses SET namespace_address_token = '145459' WHERE namespace_id = 999983 AND apartment_name = 'B2-1702';
UPDATE eh_addresses SET namespace_address_token = '145460' WHERE namespace_id = 999983 AND apartment_name = 'B2-1703';
UPDATE eh_addresses SET namespace_address_token = '145461' WHERE namespace_id = 999983 AND apartment_name = 'B2-1705';
UPDATE eh_addresses SET namespace_address_token = '145462' WHERE namespace_id = 999983 AND apartment_name = 'B2-1801';
UPDATE eh_addresses SET namespace_address_token = '145463' WHERE namespace_id = 999983 AND apartment_name = 'B2-1802';
UPDATE eh_addresses SET namespace_address_token = '145464' WHERE namespace_id = 999983 AND apartment_name = 'B2-1803';
UPDATE eh_addresses SET namespace_address_token = '145399' WHERE namespace_id = 999983 AND apartment_name = 'B2-0101';
UPDATE eh_addresses SET namespace_address_token = '634203' WHERE namespace_id = 999983 AND apartment_name = 'B2-0102';
UPDATE eh_addresses SET namespace_address_token = '145400' WHERE namespace_id = 999983 AND apartment_name = 'B2-0201';
UPDATE eh_addresses SET namespace_address_token = '145401' WHERE namespace_id = 999983 AND apartment_name = 'B2-0202';
UPDATE eh_addresses SET namespace_address_token = '634204' WHERE namespace_id = 999983 AND apartment_name = 'B2-0203';
UPDATE eh_addresses SET namespace_address_token = '145402' WHERE namespace_id = 999983 AND apartment_name = 'B2-0301';
UPDATE eh_addresses SET namespace_address_token = '145403' WHERE namespace_id = 999983 AND apartment_name = 'B2-0302';
UPDATE eh_addresses SET namespace_address_token = '145404' WHERE namespace_id = 999983 AND apartment_name = 'B2-0303';
UPDATE eh_addresses SET namespace_address_token = '145405' WHERE namespace_id = 999983 AND apartment_name = 'B2-0305';
UPDATE eh_addresses SET namespace_address_token = '145406' WHERE namespace_id = 999983 AND apartment_name = 'B2-0401';
UPDATE eh_addresses SET namespace_address_token = '145407' WHERE namespace_id = 999983 AND apartment_name = 'B2-0402';
UPDATE eh_addresses SET namespace_address_token = '145408' WHERE namespace_id = 999983 AND apartment_name = 'B2-0403';
UPDATE eh_addresses SET namespace_address_token = '145409' WHERE namespace_id = 999983 AND apartment_name = 'B2-0405';
UPDATE eh_addresses SET namespace_address_token = '145410' WHERE namespace_id = 999983 AND apartment_name = 'B2-0501';
UPDATE eh_addresses SET namespace_address_token = '145411' WHERE namespace_id = 999983 AND apartment_name = 'B2-0502';
UPDATE eh_addresses SET namespace_address_token = '145412' WHERE namespace_id = 999983 AND apartment_name = 'B2-0503';
UPDATE eh_addresses SET namespace_address_token = '145413' WHERE namespace_id = 999983 AND apartment_name = 'B2-0505';
UPDATE eh_addresses SET namespace_address_token = '145414' WHERE namespace_id = 999983 AND apartment_name = 'B2-0601';
UPDATE eh_addresses SET namespace_address_token = '145415' WHERE namespace_id = 999983 AND apartment_name = 'B2-0602';
UPDATE eh_addresses SET namespace_address_token = '145416' WHERE namespace_id = 999983 AND apartment_name = 'B2-0603';
UPDATE eh_addresses SET namespace_address_token = '145417' WHERE namespace_id = 999983 AND apartment_name = 'B2-0605';
UPDATE eh_addresses SET namespace_address_token = '145418' WHERE namespace_id = 999983 AND apartment_name = 'B2-0701';
UPDATE eh_addresses SET namespace_address_token = '145419' WHERE namespace_id = 999983 AND apartment_name = 'B2-0702';
UPDATE eh_addresses SET namespace_address_token = '145420' WHERE namespace_id = 999983 AND apartment_name = 'B2-0703';
UPDATE eh_addresses SET namespace_address_token = '145421' WHERE namespace_id = 999983 AND apartment_name = 'B2-0705';
UPDATE eh_addresses SET namespace_address_token = '145422' WHERE namespace_id = 999983 AND apartment_name = 'B2-0801';
UPDATE eh_addresses SET namespace_address_token = '145423' WHERE namespace_id = 999983 AND apartment_name = 'B2-0802';
UPDATE eh_addresses SET namespace_address_token = '145424' WHERE namespace_id = 999983 AND apartment_name = 'B2-0803';
UPDATE eh_addresses SET namespace_address_token = '145425' WHERE namespace_id = 999983 AND apartment_name = 'B2-0805';
UPDATE eh_addresses SET namespace_address_token = '145426' WHERE namespace_id = 999983 AND apartment_name = 'B2-0806';
UPDATE eh_addresses SET namespace_address_token = '145427' WHERE namespace_id = 999983 AND apartment_name = 'B2-0901';
UPDATE eh_addresses SET namespace_address_token = '145428' WHERE namespace_id = 999983 AND apartment_name = 'B2-0902';
UPDATE eh_addresses SET namespace_address_token = '145429' WHERE namespace_id = 999983 AND apartment_name = 'B2-0903';
UPDATE eh_addresses SET namespace_address_token = '145430' WHERE namespace_id = 999983 AND apartment_name = 'B2-0905';
UPDATE eh_addresses SET namespace_address_token = '145495' WHERE namespace_id = 999983 AND apartment_name = 'B3-1001';
UPDATE eh_addresses SET namespace_address_token = '145496' WHERE namespace_id = 999983 AND apartment_name = 'B3-1002';
UPDATE eh_addresses SET namespace_address_token = '145497' WHERE namespace_id = 999983 AND apartment_name = 'B3-1003';
UPDATE eh_addresses SET namespace_address_token = '145498' WHERE namespace_id = 999983 AND apartment_name = 'B3-1005';
UPDATE eh_addresses SET namespace_address_token = '145499' WHERE namespace_id = 999983 AND apartment_name = 'B3-1006';
UPDATE eh_addresses SET namespace_address_token = '145500' WHERE namespace_id = 999983 AND apartment_name = 'B3-1101';
UPDATE eh_addresses SET namespace_address_token = '145501' WHERE namespace_id = 999983 AND apartment_name = 'B3-1102';
UPDATE eh_addresses SET namespace_address_token = '145502' WHERE namespace_id = 999983 AND apartment_name = 'B3-1103';
UPDATE eh_addresses SET namespace_address_token = '145503' WHERE namespace_id = 999983 AND apartment_name = 'B3-1105';
UPDATE eh_addresses SET namespace_address_token = '145504' WHERE namespace_id = 999983 AND apartment_name = 'B3-1106';
UPDATE eh_addresses SET namespace_address_token = '145505' WHERE namespace_id = 999983 AND apartment_name = 'B3-1201';
UPDATE eh_addresses SET namespace_address_token = '145506' WHERE namespace_id = 999983 AND apartment_name = 'B3-1202';
UPDATE eh_addresses SET namespace_address_token = '145507' WHERE namespace_id = 999983 AND apartment_name = 'B3-1203';
UPDATE eh_addresses SET namespace_address_token = '145508' WHERE namespace_id = 999983 AND apartment_name = 'B3-1205';
UPDATE eh_addresses SET namespace_address_token = '145509' WHERE namespace_id = 999983 AND apartment_name = 'B3-1206';
UPDATE eh_addresses SET namespace_address_token = '145510' WHERE namespace_id = 999983 AND apartment_name = 'B3-1301';
UPDATE eh_addresses SET namespace_address_token = '145511' WHERE namespace_id = 999983 AND apartment_name = 'B3-1302';
UPDATE eh_addresses SET namespace_address_token = '145512' WHERE namespace_id = 999983 AND apartment_name = 'B3-1303';
UPDATE eh_addresses SET namespace_address_token = '145513' WHERE namespace_id = 999983 AND apartment_name = 'B3-1305';
UPDATE eh_addresses SET namespace_address_token = '145514' WHERE namespace_id = 999983 AND apartment_name = 'B3-1306';
UPDATE eh_addresses SET namespace_address_token = '145515' WHERE namespace_id = 999983 AND apartment_name = 'B3-1401';
UPDATE eh_addresses SET namespace_address_token = '145516' WHERE namespace_id = 999983 AND apartment_name = 'B3-1402';
UPDATE eh_addresses SET namespace_address_token = '145517' WHERE namespace_id = 999983 AND apartment_name = 'B3-1403';
UPDATE eh_addresses SET namespace_address_token = '145518' WHERE namespace_id = 999983 AND apartment_name = 'B3-1405';
UPDATE eh_addresses SET namespace_address_token = '145519' WHERE namespace_id = 999983 AND apartment_name = 'B3-1406';
UPDATE eh_addresses SET namespace_address_token = '145520' WHERE namespace_id = 999983 AND apartment_name = 'B3-1501';
UPDATE eh_addresses SET namespace_address_token = '145521' WHERE namespace_id = 999983 AND apartment_name = 'B3-1502';
UPDATE eh_addresses SET namespace_address_token = '145522' WHERE namespace_id = 999983 AND apartment_name = 'B3-1503';
UPDATE eh_addresses SET namespace_address_token = '145523' WHERE namespace_id = 999983 AND apartment_name = 'B3-1505';
UPDATE eh_addresses SET namespace_address_token = '145524' WHERE namespace_id = 999983 AND apartment_name = 'B3-1506';
UPDATE eh_addresses SET namespace_address_token = '145525' WHERE namespace_id = 999983 AND apartment_name = 'B3-1601';
UPDATE eh_addresses SET namespace_address_token = '145526' WHERE namespace_id = 999983 AND apartment_name = 'B3-1602';
UPDATE eh_addresses SET namespace_address_token = '145527' WHERE namespace_id = 999983 AND apartment_name = 'B3-1701';
UPDATE eh_addresses SET namespace_address_token = '145528' WHERE namespace_id = 999983 AND apartment_name = 'B3-1702';
UPDATE eh_addresses SET namespace_address_token = '145530' WHERE namespace_id = 999983 AND apartment_name = 'B3-1802';
UPDATE eh_addresses SET namespace_address_token = '145529' WHERE namespace_id = 999983 AND apartment_name = 'B3-1801';
UPDATE eh_addresses SET namespace_address_token = '145465' WHERE namespace_id = 999983 AND apartment_name = 'B3-0301';
UPDATE eh_addresses SET namespace_address_token = '145466' WHERE namespace_id = 999983 AND apartment_name = 'B3-0302';
UPDATE eh_addresses SET namespace_address_token = '145467' WHERE namespace_id = 999983 AND apartment_name = 'B3-0303';
UPDATE eh_addresses SET namespace_address_token = '145468' WHERE namespace_id = 999983 AND apartment_name = 'B3-0305';
UPDATE eh_addresses SET namespace_address_token = '145469' WHERE namespace_id = 999983 AND apartment_name = 'B3-0401';
UPDATE eh_addresses SET namespace_address_token = '145470' WHERE namespace_id = 999983 AND apartment_name = 'B3-0402';
UPDATE eh_addresses SET namespace_address_token = '145471' WHERE namespace_id = 999983 AND apartment_name = 'B3-0403';
UPDATE eh_addresses SET namespace_address_token = '145472' WHERE namespace_id = 999983 AND apartment_name = 'B3-0405';
UPDATE eh_addresses SET namespace_address_token = '145473' WHERE namespace_id = 999983 AND apartment_name = 'B3-0501';
UPDATE eh_addresses SET namespace_address_token = '145474' WHERE namespace_id = 999983 AND apartment_name = 'B3-0502';
UPDATE eh_addresses SET namespace_address_token = '145475' WHERE namespace_id = 999983 AND apartment_name = 'B3-0503';
UPDATE eh_addresses SET namespace_address_token = '145476' WHERE namespace_id = 999983 AND apartment_name = 'B3-0505';
UPDATE eh_addresses SET namespace_address_token = '145477' WHERE namespace_id = 999983 AND apartment_name = 'B3-0601';
UPDATE eh_addresses SET namespace_address_token = '145478' WHERE namespace_id = 999983 AND apartment_name = 'B3-0602';
UPDATE eh_addresses SET namespace_address_token = '145479' WHERE namespace_id = 999983 AND apartment_name = 'B3-0603';
UPDATE eh_addresses SET namespace_address_token = '145480' WHERE namespace_id = 999983 AND apartment_name = 'B3-0605';
UPDATE eh_addresses SET namespace_address_token = '145481' WHERE namespace_id = 999983 AND apartment_name = 'B3-0701';
UPDATE eh_addresses SET namespace_address_token = '145482' WHERE namespace_id = 999983 AND apartment_name = 'B3-0702';
UPDATE eh_addresses SET namespace_address_token = '145483' WHERE namespace_id = 999983 AND apartment_name = 'B3-0703';
UPDATE eh_addresses SET namespace_address_token = '145484' WHERE namespace_id = 999983 AND apartment_name = 'B3-0705';
UPDATE eh_addresses SET namespace_address_token = '145486' WHERE namespace_id = 999983 AND apartment_name = 'B3-0802';
UPDATE eh_addresses SET namespace_address_token = '145485' WHERE namespace_id = 999983 AND apartment_name = 'B3-0801';
UPDATE eh_addresses SET namespace_address_token = '145487' WHERE namespace_id = 999983 AND apartment_name = 'B3-0803';
UPDATE eh_addresses SET namespace_address_token = '145488' WHERE namespace_id = 999983 AND apartment_name = 'B3-0805';
UPDATE eh_addresses SET namespace_address_token = '145489' WHERE namespace_id = 999983 AND apartment_name = 'B3-0806';
UPDATE eh_addresses SET namespace_address_token = '145490' WHERE namespace_id = 999983 AND apartment_name = 'B3-0901';
UPDATE eh_addresses SET namespace_address_token = '145491' WHERE namespace_id = 999983 AND apartment_name = 'B3-0902';
UPDATE eh_addresses SET namespace_address_token = '145492' WHERE namespace_id = 999983 AND apartment_name = 'B3-0903';
UPDATE eh_addresses SET namespace_address_token = '145493' WHERE namespace_id = 999983 AND apartment_name = 'B3-0905';
UPDATE eh_addresses SET namespace_address_token = '145494' WHERE namespace_id = 999983 AND apartment_name = 'B3-0906';
UPDATE eh_addresses SET namespace_address_token = '144971' WHERE namespace_id = 999983 AND apartment_name = 'B4-0106';
UPDATE eh_addresses SET namespace_address_token = '145556' WHERE namespace_id = 999983 AND apartment_name = 'B4-1001';
UPDATE eh_addresses SET namespace_address_token = '145557' WHERE namespace_id = 999983 AND apartment_name = 'B4-1002';
UPDATE eh_addresses SET namespace_address_token = '145558' WHERE namespace_id = 999983 AND apartment_name = 'B4-1003';
UPDATE eh_addresses SET namespace_address_token = '145559' WHERE namespace_id = 999983 AND apartment_name = 'B4-1101';
UPDATE eh_addresses SET namespace_address_token = '145560' WHERE namespace_id = 999983 AND apartment_name = 'B4-1102';
UPDATE eh_addresses SET namespace_address_token = '145561' WHERE namespace_id = 999983 AND apartment_name = 'B4-1103';
UPDATE eh_addresses SET namespace_address_token = '145562' WHERE namespace_id = 999983 AND apartment_name = 'B4-1201';
UPDATE eh_addresses SET namespace_address_token = '145563' WHERE namespace_id = 999983 AND apartment_name = 'B4-1202';
UPDATE eh_addresses SET namespace_address_token = '145564' WHERE namespace_id = 999983 AND apartment_name = 'B4-1203';
UPDATE eh_addresses SET namespace_address_token = '145565' WHERE namespace_id = 999983 AND apartment_name = 'B4-1301';
UPDATE eh_addresses SET namespace_address_token = '145566' WHERE namespace_id = 999983 AND apartment_name = 'B4-1302';
UPDATE eh_addresses SET namespace_address_token = '145567' WHERE namespace_id = 999983 AND apartment_name = 'B4-1303';
UPDATE eh_addresses SET namespace_address_token = '145568' WHERE namespace_id = 999983 AND apartment_name = 'B4-1401';
UPDATE eh_addresses SET namespace_address_token = '145569' WHERE namespace_id = 999983 AND apartment_name = 'B4-1402';
UPDATE eh_addresses SET namespace_address_token = '145570' WHERE namespace_id = 999983 AND apartment_name = 'B4-1403';
UPDATE eh_addresses SET namespace_address_token = '145571' WHERE namespace_id = 999983 AND apartment_name = 'B4-1501';
UPDATE eh_addresses SET namespace_address_token = '145572' WHERE namespace_id = 999983 AND apartment_name = 'B4-1502';
UPDATE eh_addresses SET namespace_address_token = '145573' WHERE namespace_id = 999983 AND apartment_name = 'B4-1503';
UPDATE eh_addresses SET namespace_address_token = '145574' WHERE namespace_id = 999983 AND apartment_name = 'B4-1601';
UPDATE eh_addresses SET namespace_address_token = '145575' WHERE namespace_id = 999983 AND apartment_name = 'B4-1602';
UPDATE eh_addresses SET namespace_address_token = '145576' WHERE namespace_id = 999983 AND apartment_name = 'B4-1603';
UPDATE eh_addresses SET namespace_address_token = '145577' WHERE namespace_id = 999983 AND apartment_name = 'B4-1701';
UPDATE eh_addresses SET namespace_address_token = '145578' WHERE namespace_id = 999983 AND apartment_name = 'B4-1702';
UPDATE eh_addresses SET namespace_address_token = '145579' WHERE namespace_id = 999983 AND apartment_name = 'B4-1703';
UPDATE eh_addresses SET namespace_address_token = '145580' WHERE namespace_id = 999983 AND apartment_name = 'B4-1801';
UPDATE eh_addresses SET namespace_address_token = '145531' WHERE namespace_id = 999983 AND apartment_name = 'B4-0203';
UPDATE eh_addresses SET namespace_address_token = '145532' WHERE namespace_id = 999983 AND apartment_name = 'B4-0205';
UPDATE eh_addresses SET namespace_address_token = '145533' WHERE namespace_id = 999983 AND apartment_name = 'B4-0301';
UPDATE eh_addresses SET namespace_address_token = '145534' WHERE namespace_id = 999983 AND apartment_name = 'B4-0302';
UPDATE eh_addresses SET namespace_address_token = '145535' WHERE namespace_id = 999983 AND apartment_name = 'B4-0303';
UPDATE eh_addresses SET namespace_address_token = '145536' WHERE namespace_id = 999983 AND apartment_name = 'B4-0305';
UPDATE eh_addresses SET namespace_address_token = '145537' WHERE namespace_id = 999983 AND apartment_name = 'B4-0306';
UPDATE eh_addresses SET namespace_address_token = '145538' WHERE namespace_id = 999983 AND apartment_name = 'B4-0307';
UPDATE eh_addresses SET namespace_address_token = '145539' WHERE namespace_id = 999983 AND apartment_name = 'B4-0401';
UPDATE eh_addresses SET namespace_address_token = '145540' WHERE namespace_id = 999983 AND apartment_name = 'B4-0402';
UPDATE eh_addresses SET namespace_address_token = '145541' WHERE namespace_id = 999983 AND apartment_name = 'B4-0403';
UPDATE eh_addresses SET namespace_address_token = '145542' WHERE namespace_id = 999983 AND apartment_name = 'B4-0501';
UPDATE eh_addresses SET namespace_address_token = '145543' WHERE namespace_id = 999983 AND apartment_name = 'B4-0502';
UPDATE eh_addresses SET namespace_address_token = '145544' WHERE namespace_id = 999983 AND apartment_name = 'B4-0503';
UPDATE eh_addresses SET namespace_address_token = '145545' WHERE namespace_id = 999983 AND apartment_name = 'B4-0601';
UPDATE eh_addresses SET namespace_address_token = '145546' WHERE namespace_id = 999983 AND apartment_name = 'B4-0602';
UPDATE eh_addresses SET namespace_address_token = '145547' WHERE namespace_id = 999983 AND apartment_name = 'B4-0603';
UPDATE eh_addresses SET namespace_address_token = '145549' WHERE namespace_id = 999983 AND apartment_name = 'B4-0702';
UPDATE eh_addresses SET namespace_address_token = '145548' WHERE namespace_id = 999983 AND apartment_name = 'B4-0701';
UPDATE eh_addresses SET namespace_address_token = '145550' WHERE namespace_id = 999983 AND apartment_name = 'B4-0801';
UPDATE eh_addresses SET namespace_address_token = '145551' WHERE namespace_id = 999983 AND apartment_name = 'B4-0802';
UPDATE eh_addresses SET namespace_address_token = '145552' WHERE namespace_id = 999983 AND apartment_name = 'B4-0803';
UPDATE eh_addresses SET namespace_address_token = '145553' WHERE namespace_id = 999983 AND apartment_name = 'B4-0901';
UPDATE eh_addresses SET namespace_address_token = '145554' WHERE namespace_id = 999983 AND apartment_name = 'B4-0902';
UPDATE eh_addresses SET namespace_address_token = '145555' WHERE namespace_id = 999983 AND apartment_name = 'B4-0903';
UPDATE eh_addresses SET namespace_address_token = '916518' WHERE namespace_id = 999983 AND apartment_name = 'C2-064';
UPDATE eh_addresses SET namespace_address_token = '145002' WHERE namespace_id = 999983 AND apartment_name = 'C-G27';
UPDATE eh_addresses SET namespace_address_token = '145003' WHERE namespace_id = 999983 AND apartment_name = 'C-G28';
UPDATE eh_addresses SET namespace_address_token = '145697' WHERE namespace_id = 999983 AND apartment_name = 'C-G41';
UPDATE eh_addresses SET namespace_address_token = '915627' WHERE namespace_id = 999983 AND apartment_name = 'C1-02';
UPDATE eh_addresses SET namespace_address_token = '145644' WHERE namespace_id = 999983 AND apartment_name = 'C1-1001';
UPDATE eh_addresses SET namespace_address_token = '145645' WHERE namespace_id = 999983 AND apartment_name = 'C1-1002';
UPDATE eh_addresses SET namespace_address_token = '145646' WHERE namespace_id = 999983 AND apartment_name = 'C1-1101';
UPDATE eh_addresses SET namespace_address_token = '145647' WHERE namespace_id = 999983 AND apartment_name = 'C1-1102';
UPDATE eh_addresses SET namespace_address_token = '145648' WHERE namespace_id = 999983 AND apartment_name = 'C1-1201';
UPDATE eh_addresses SET namespace_address_token = '145649' WHERE namespace_id = 999983 AND apartment_name = 'C1-1202';
UPDATE eh_addresses SET namespace_address_token = '145651' WHERE namespace_id = 999983 AND apartment_name = 'C1-1302';
UPDATE eh_addresses SET namespace_address_token = '145650' WHERE namespace_id = 999983 AND apartment_name = 'C1-1301';
UPDATE eh_addresses SET namespace_address_token = '145652' WHERE namespace_id = 999983 AND apartment_name = 'C1-1401';
UPDATE eh_addresses SET namespace_address_token = '145653' WHERE namespace_id = 999983 AND apartment_name = 'C1-1402';
UPDATE eh_addresses SET namespace_address_token = '145654' WHERE namespace_id = 999983 AND apartment_name = 'C1-1501';
UPDATE eh_addresses SET namespace_address_token = '145655' WHERE namespace_id = 999983 AND apartment_name = 'C1-1502';
UPDATE eh_addresses SET namespace_address_token = '145656' WHERE namespace_id = 999983 AND apartment_name = 'C1-1601';
UPDATE eh_addresses SET namespace_address_token = '145657' WHERE namespace_id = 999983 AND apartment_name = 'C1-1602';
UPDATE eh_addresses SET namespace_address_token = '145658' WHERE namespace_id = 999983 AND apartment_name = 'C1-1701';
UPDATE eh_addresses SET namespace_address_token = '145659' WHERE namespace_id = 999983 AND apartment_name = 'C1-1702';
UPDATE eh_addresses SET namespace_address_token = '145660' WHERE namespace_id = 999983 AND apartment_name = 'C1-1801';
UPDATE eh_addresses SET namespace_address_token = '145661' WHERE namespace_id = 999983 AND apartment_name = 'C1-1802';
UPDATE eh_addresses SET namespace_address_token = '144879' WHERE namespace_id = 999983 AND apartment_name = 'A-107';
UPDATE eh_addresses SET namespace_address_token = '144890' WHERE namespace_id = 999983 AND apartment_name = 'A-118';
UPDATE eh_addresses SET namespace_address_token = '144891' WHERE namespace_id = 999983 AND apartment_name = 'A-119';
UPDATE eh_addresses SET namespace_address_token = '144902' WHERE namespace_id = 999983 AND apartment_name = 'A-130';
UPDATE eh_addresses SET namespace_address_token = '145627' WHERE namespace_id = 999983 AND apartment_name = 'C1-0101';
UPDATE eh_addresses SET namespace_address_token = '145628' WHERE namespace_id = 999983 AND apartment_name = 'C1-0201';
UPDATE eh_addresses SET namespace_address_token = '145629' WHERE namespace_id = 999983 AND apartment_name = 'C1-0202';
UPDATE eh_addresses SET namespace_address_token = '145630' WHERE namespace_id = 999983 AND apartment_name = 'C1-0301';
UPDATE eh_addresses SET namespace_address_token = '145631' WHERE namespace_id = 999983 AND apartment_name = 'C1-0302';
UPDATE eh_addresses SET namespace_address_token = '145632' WHERE namespace_id = 999983 AND apartment_name = 'C1-0401';
UPDATE eh_addresses SET namespace_address_token = '145633' WHERE namespace_id = 999983 AND apartment_name = 'C1-0402';
UPDATE eh_addresses SET namespace_address_token = '145634' WHERE namespace_id = 999983 AND apartment_name = 'C1-0501';
UPDATE eh_addresses SET namespace_address_token = '145635' WHERE namespace_id = 999983 AND apartment_name = 'C1-0502';
UPDATE eh_addresses SET namespace_address_token = '145636' WHERE namespace_id = 999983 AND apartment_name = 'C1-0601';
UPDATE eh_addresses SET namespace_address_token = '145637' WHERE namespace_id = 999983 AND apartment_name = 'C1-0602';
UPDATE eh_addresses SET namespace_address_token = '145638' WHERE namespace_id = 999983 AND apartment_name = 'C1-0701';
UPDATE eh_addresses SET namespace_address_token = '145639' WHERE namespace_id = 999983 AND apartment_name = 'C1-0702';
UPDATE eh_addresses SET namespace_address_token = '145640' WHERE namespace_id = 999983 AND apartment_name = 'C1-0801';
UPDATE eh_addresses SET namespace_address_token = '145641' WHERE namespace_id = 999983 AND apartment_name = 'C1-0802';
UPDATE eh_addresses SET namespace_address_token = '145642' WHERE namespace_id = 999983 AND apartment_name = 'C1-0901';
UPDATE eh_addresses SET namespace_address_token = '145643' WHERE namespace_id = 999983 AND apartment_name = 'C1-0902';
UPDATE eh_addresses SET namespace_address_token = '145679' WHERE namespace_id = 999983 AND apartment_name = 'C2-1001';
UPDATE eh_addresses SET namespace_address_token = '145680' WHERE namespace_id = 999983 AND apartment_name = 'C2-1002';
UPDATE eh_addresses SET namespace_address_token = '145681' WHERE namespace_id = 999983 AND apartment_name = 'C2-1101';
UPDATE eh_addresses SET namespace_address_token = '145682' WHERE namespace_id = 999983 AND apartment_name = 'C2-1102';
UPDATE eh_addresses SET namespace_address_token = '145683' WHERE namespace_id = 999983 AND apartment_name = 'C2-1201';
UPDATE eh_addresses SET namespace_address_token = '145684' WHERE namespace_id = 999983 AND apartment_name = 'C2-1202';
UPDATE eh_addresses SET namespace_address_token = '145685' WHERE namespace_id = 999983 AND apartment_name = 'C2-1301';
UPDATE eh_addresses SET namespace_address_token = '145686' WHERE namespace_id = 999983 AND apartment_name = 'C2-1302';
UPDATE eh_addresses SET namespace_address_token = '145688' WHERE namespace_id = 999983 AND apartment_name = 'C2-1402';
UPDATE eh_addresses SET namespace_address_token = '145687' WHERE namespace_id = 999983 AND apartment_name = 'C2-1401';
UPDATE eh_addresses SET namespace_address_token = '145689' WHERE namespace_id = 999983 AND apartment_name = 'C2-1501';
UPDATE eh_addresses SET namespace_address_token = '145690' WHERE namespace_id = 999983 AND apartment_name = 'C2-1502';
UPDATE eh_addresses SET namespace_address_token = '145691' WHERE namespace_id = 999983 AND apartment_name = 'C2-1601';
UPDATE eh_addresses SET namespace_address_token = '145692' WHERE namespace_id = 999983 AND apartment_name = 'C2-1602';
UPDATE eh_addresses SET namespace_address_token = '145693' WHERE namespace_id = 999983 AND apartment_name = 'C2-1701';
UPDATE eh_addresses SET namespace_address_token = '145694' WHERE namespace_id = 999983 AND apartment_name = 'C2-1702';
UPDATE eh_addresses SET namespace_address_token = '145695' WHERE namespace_id = 999983 AND apartment_name = 'C2-1801';
UPDATE eh_addresses SET namespace_address_token = '145696' WHERE namespace_id = 999983 AND apartment_name = 'C2-1802';
UPDATE eh_addresses SET namespace_address_token = '145662' WHERE namespace_id = 999983 AND apartment_name = 'C2-0101';
UPDATE eh_addresses SET namespace_address_token = '145663' WHERE namespace_id = 999983 AND apartment_name = 'C2-0201';
UPDATE eh_addresses SET namespace_address_token = '145664' WHERE namespace_id = 999983 AND apartment_name = 'C2-0202';
UPDATE eh_addresses SET namespace_address_token = '145665' WHERE namespace_id = 999983 AND apartment_name = 'C2-0301';
UPDATE eh_addresses SET namespace_address_token = '145666' WHERE namespace_id = 999983 AND apartment_name = 'C2-0302';
UPDATE eh_addresses SET namespace_address_token = '145667' WHERE namespace_id = 999983 AND apartment_name = 'C2-0401';
UPDATE eh_addresses SET namespace_address_token = '145668' WHERE namespace_id = 999983 AND apartment_name = 'C2-0402';
UPDATE eh_addresses SET namespace_address_token = '145669' WHERE namespace_id = 999983 AND apartment_name = 'C2-0501';
UPDATE eh_addresses SET namespace_address_token = '145670' WHERE namespace_id = 999983 AND apartment_name = 'C2-0502';
UPDATE eh_addresses SET namespace_address_token = '145671' WHERE namespace_id = 999983 AND apartment_name = 'C2-0601';
UPDATE eh_addresses SET namespace_address_token = '145672' WHERE namespace_id = 999983 AND apartment_name = 'C2-0602';
UPDATE eh_addresses SET namespace_address_token = '145673' WHERE namespace_id = 999983 AND apartment_name = 'C2-0701';
UPDATE eh_addresses SET namespace_address_token = '145674' WHERE namespace_id = 999983 AND apartment_name = 'C2-0702';
UPDATE eh_addresses SET namespace_address_token = '145675' WHERE namespace_id = 999983 AND apartment_name = 'C2-0801';
UPDATE eh_addresses SET namespace_address_token = '145676' WHERE namespace_id = 999983 AND apartment_name = 'C2-0802';
UPDATE eh_addresses SET namespace_address_token = '145677' WHERE namespace_id = 999983 AND apartment_name = 'C2-0901';
UPDATE eh_addresses SET namespace_address_token = '145678' WHERE namespace_id = 999983 AND apartment_name = 'C2-0902';
UPDATE eh_addresses SET namespace_address_token = '145703' WHERE namespace_id = 999983 AND apartment_name = 'B2-B101';
UPDATE eh_addresses SET namespace_address_token = '145606' WHERE namespace_id = 999983 AND apartment_name = 'C3-1001';
UPDATE eh_addresses SET namespace_address_token = '145607' WHERE namespace_id = 999983 AND apartment_name = 'C3-1002';
UPDATE eh_addresses SET namespace_address_token = '145608' WHERE namespace_id = 999983 AND apartment_name = 'C3-1003';
UPDATE eh_addresses SET namespace_address_token = '145609' WHERE namespace_id = 999983 AND apartment_name = 'C3-1101';
UPDATE eh_addresses SET namespace_address_token = '145610' WHERE namespace_id = 999983 AND apartment_name = 'C3-1102';
UPDATE eh_addresses SET namespace_address_token = '145611' WHERE namespace_id = 999983 AND apartment_name = 'C3-1103';
UPDATE eh_addresses SET namespace_address_token = '145612' WHERE namespace_id = 999983 AND apartment_name = 'C3-1201';
UPDATE eh_addresses SET namespace_address_token = '145613' WHERE namespace_id = 999983 AND apartment_name = 'C3-1202';
UPDATE eh_addresses SET namespace_address_token = '145614' WHERE namespace_id = 999983 AND apartment_name = 'C3-1203';
UPDATE eh_addresses SET namespace_address_token = '145615' WHERE namespace_id = 999983 AND apartment_name = 'C3-1301';
UPDATE eh_addresses SET namespace_address_token = '145616' WHERE namespace_id = 999983 AND apartment_name = 'C3-1302';
UPDATE eh_addresses SET namespace_address_token = '145617' WHERE namespace_id = 999983 AND apartment_name = 'C3-1303';
UPDATE eh_addresses SET namespace_address_token = '145618' WHERE namespace_id = 999983 AND apartment_name = 'C3-1401';
UPDATE eh_addresses SET namespace_address_token = '145619' WHERE namespace_id = 999983 AND apartment_name = 'C3-1402';
UPDATE eh_addresses SET namespace_address_token = '145620' WHERE namespace_id = 999983 AND apartment_name = 'C3-1403';
UPDATE eh_addresses SET namespace_address_token = '145621' WHERE namespace_id = 999983 AND apartment_name = 'C3-1501';
UPDATE eh_addresses SET namespace_address_token = '145622' WHERE namespace_id = 999983 AND apartment_name = 'C3-1502';
UPDATE eh_addresses SET namespace_address_token = '145623' WHERE namespace_id = 999983 AND apartment_name = 'C3-1503';
UPDATE eh_addresses SET namespace_address_token = '145624' WHERE namespace_id = 999983 AND apartment_name = 'C3-1601';
UPDATE eh_addresses SET namespace_address_token = '145625' WHERE namespace_id = 999983 AND apartment_name = 'C3-1602';
UPDATE eh_addresses SET namespace_address_token = '145626' WHERE namespace_id = 999983 AND apartment_name = 'C3-1603';
UPDATE eh_addresses SET namespace_address_token = '145605' WHERE namespace_id = 999983 AND apartment_name = 'C3-0101';
UPDATE eh_addresses SET namespace_address_token = '145581' WHERE namespace_id = 999983 AND apartment_name = 'C3-0201';
UPDATE eh_addresses SET namespace_address_token = '145582' WHERE namespace_id = 999983 AND apartment_name = 'C3-0202';
UPDATE eh_addresses SET namespace_address_token = '145583' WHERE namespace_id = 999983 AND apartment_name = 'C3-0203';
UPDATE eh_addresses SET namespace_address_token = '145584' WHERE namespace_id = 999983 AND apartment_name = 'C3-0301';
UPDATE eh_addresses SET namespace_address_token = '145585' WHERE namespace_id = 999983 AND apartment_name = 'C3-0302';
UPDATE eh_addresses SET namespace_address_token = '145586' WHERE namespace_id = 999983 AND apartment_name = 'C3-0303';
UPDATE eh_addresses SET namespace_address_token = '145587' WHERE namespace_id = 999983 AND apartment_name = 'C3-0401';
UPDATE eh_addresses SET namespace_address_token = '145588' WHERE namespace_id = 999983 AND apartment_name = 'C3-0402';
UPDATE eh_addresses SET namespace_address_token = '145589' WHERE namespace_id = 999983 AND apartment_name = 'C3-0403';
UPDATE eh_addresses SET namespace_address_token = '145590' WHERE namespace_id = 999983 AND apartment_name = 'C3-0501';
UPDATE eh_addresses SET namespace_address_token = '145591' WHERE namespace_id = 999983 AND apartment_name = 'C3-0502';
UPDATE eh_addresses SET namespace_address_token = '145592' WHERE namespace_id = 999983 AND apartment_name = 'C3-0503';
UPDATE eh_addresses SET namespace_address_token = '145593' WHERE namespace_id = 999983 AND apartment_name = 'C3-0601';
UPDATE eh_addresses SET namespace_address_token = '145594' WHERE namespace_id = 999983 AND apartment_name = 'C3-0602';
UPDATE eh_addresses SET namespace_address_token = '145595' WHERE namespace_id = 999983 AND apartment_name = 'C3-0603';
UPDATE eh_addresses SET namespace_address_token = '145596' WHERE namespace_id = 999983 AND apartment_name = 'C3-0701';
UPDATE eh_addresses SET namespace_address_token = '145597' WHERE namespace_id = 999983 AND apartment_name = 'C3-0702';
UPDATE eh_addresses SET namespace_address_token = '145598' WHERE namespace_id = 999983 AND apartment_name = 'C3-0703';
UPDATE eh_addresses SET namespace_address_token = '145599' WHERE namespace_id = 999983 AND apartment_name = 'C3-0801';
UPDATE eh_addresses SET namespace_address_token = '145600' WHERE namespace_id = 999983 AND apartment_name = 'C3-0802';
UPDATE eh_addresses SET namespace_address_token = '145601' WHERE namespace_id = 999983 AND apartment_name = 'C3-0803';
UPDATE eh_addresses SET namespace_address_token = '145602' WHERE namespace_id = 999983 AND apartment_name = 'C3-0901';
UPDATE eh_addresses SET namespace_address_token = '145603' WHERE namespace_id = 999983 AND apartment_name = 'C3-0902';
UPDATE eh_addresses SET namespace_address_token = '145604' WHERE namespace_id = 999983 AND apartment_name = 'C3-0903';
UPDATE eh_addresses SET namespace_address_token = '735054' WHERE namespace_id = 999983 AND apartment_name = 'C3-B101';
UPDATE eh_addresses SET namespace_address_token = '145698' WHERE namespace_id = 999983 AND apartment_name = 'C3-B201';
UPDATE eh_addresses SET namespace_address_token = '145699' WHERE namespace_id = 999983 AND apartment_name = 'C3-B202';
UPDATE eh_addresses SET namespace_address_token = '145700' WHERE namespace_id = 999983 AND apartment_name = 'C3-B203';
UPDATE eh_addresses SET namespace_address_token = '145701' WHERE namespace_id = 999983 AND apartment_name = 'C3-B204';
UPDATE eh_addresses SET namespace_address_token = '144873' WHERE namespace_id = 999983 AND apartment_name = 'A-101';
UPDATE eh_addresses SET namespace_address_token = '144874' WHERE namespace_id = 999983 AND apartment_name = 'A-102';
UPDATE eh_addresses SET namespace_address_token = '144877' WHERE namespace_id = 999983 AND apartment_name = 'A-105';
UPDATE eh_addresses SET namespace_address_token = '144878' WHERE namespace_id = 999983 AND apartment_name = 'A-106';
UPDATE eh_addresses SET namespace_address_token = '144883' WHERE namespace_id = 999983 AND apartment_name = 'A-111';
UPDATE eh_addresses SET namespace_address_token = '144886' WHERE namespace_id = 999983 AND apartment_name = 'A-114';
UPDATE eh_addresses SET namespace_address_token = '144888' WHERE namespace_id = 999983 AND apartment_name = 'A-116';
UPDATE eh_addresses SET namespace_address_token = '144892' WHERE namespace_id = 999983 AND apartment_name = 'A-120';
UPDATE eh_addresses SET namespace_address_token = '144894' WHERE namespace_id = 999983 AND apartment_name = 'A-122';
UPDATE eh_addresses SET namespace_address_token = '144895' WHERE namespace_id = 999983 AND apartment_name = 'A-123';
UPDATE eh_addresses SET namespace_address_token = '144896' WHERE namespace_id = 999983 AND apartment_name = 'A-124';
UPDATE eh_addresses SET namespace_address_token = '144898' WHERE namespace_id = 999983 AND apartment_name = 'A-126';
UPDATE eh_addresses SET namespace_address_token = '144899' WHERE namespace_id = 999983 AND apartment_name = 'A-127';
UPDATE eh_addresses SET namespace_address_token = '144900' WHERE namespace_id = 999983 AND apartment_name = 'A-128';
UPDATE eh_addresses SET namespace_address_token = '144901' WHERE namespace_id = 999983 AND apartment_name = 'A-129';
UPDATE eh_addresses SET namespace_address_token = '144875' WHERE namespace_id = 999983 AND apartment_name = 'A-103';
UPDATE eh_addresses SET namespace_address_token = '144876' WHERE namespace_id = 999983 AND apartment_name = 'A-104';
UPDATE eh_addresses SET namespace_address_token = '144880' WHERE namespace_id = 999983 AND apartment_name = 'A-108';
UPDATE eh_addresses SET namespace_address_token = '144881' WHERE namespace_id = 999983 AND apartment_name = 'A-109';
UPDATE eh_addresses SET namespace_address_token = '144882' WHERE namespace_id = 999983 AND apartment_name = 'A-110';
UPDATE eh_addresses SET namespace_address_token = '144884' WHERE namespace_id = 999983 AND apartment_name = 'A-112';
UPDATE eh_addresses SET namespace_address_token = '144885' WHERE namespace_id = 999983 AND apartment_name = 'A-113';
UPDATE eh_addresses SET namespace_address_token = '144887' WHERE namespace_id = 999983 AND apartment_name = 'A-115';
UPDATE eh_addresses SET namespace_address_token = '144889' WHERE namespace_id = 999983 AND apartment_name = 'A-117';
UPDATE eh_addresses SET namespace_address_token = '144893' WHERE namespace_id = 999983 AND apartment_name = 'A-121';
UPDATE eh_addresses SET namespace_address_token = '144897' WHERE namespace_id = 999983 AND apartment_name = 'A-125';
UPDATE eh_addresses SET namespace_address_token = '144840' WHERE namespace_id = 999983 AND apartment_name = 'A-G01';
UPDATE eh_addresses SET namespace_address_token = '144841' WHERE namespace_id = 999983 AND apartment_name = 'A-G02';
UPDATE eh_addresses SET namespace_address_token = '144842' WHERE namespace_id = 999983 AND apartment_name = 'A-G03';
UPDATE eh_addresses SET namespace_address_token = '144843' WHERE namespace_id = 999983 AND apartment_name = 'A-G04';
UPDATE eh_addresses SET namespace_address_token = '144844' WHERE namespace_id = 999983 AND apartment_name = 'A-G05';
UPDATE eh_addresses SET namespace_address_token = '144845' WHERE namespace_id = 999983 AND apartment_name = 'A-G06';
UPDATE eh_addresses SET namespace_address_token = '144846' WHERE namespace_id = 999983 AND apartment_name = 'A-G07';
UPDATE eh_addresses SET namespace_address_token = '144847' WHERE namespace_id = 999983 AND apartment_name = 'A-G08';
UPDATE eh_addresses SET namespace_address_token = '915818' WHERE namespace_id = 999983 AND apartment_name = 'A-G09';
UPDATE eh_addresses SET namespace_address_token = '144849' WHERE namespace_id = 999983 AND apartment_name = 'A-G10';
UPDATE eh_addresses SET namespace_address_token = '144850' WHERE namespace_id = 999983 AND apartment_name = 'A-G11';
UPDATE eh_addresses SET namespace_address_token = '144851' WHERE namespace_id = 999983 AND apartment_name = 'A-G12';
UPDATE eh_addresses SET namespace_address_token = '144852' WHERE namespace_id = 999983 AND apartment_name = 'A-G13';
UPDATE eh_addresses SET namespace_address_token = '144853' WHERE namespace_id = 999983 AND apartment_name = 'A-G14';
UPDATE eh_addresses SET namespace_address_token = '144854' WHERE namespace_id = 999983 AND apartment_name = 'A-G15';
UPDATE eh_addresses SET namespace_address_token = '735042' WHERE namespace_id = 999983 AND apartment_name = 'A-G16A';
UPDATE eh_addresses SET namespace_address_token = '735043' WHERE namespace_id = 999983 AND apartment_name = 'A-G17A';
UPDATE eh_addresses SET namespace_address_token = '735044' WHERE namespace_id = 999983 AND apartment_name = 'A-G17B';
UPDATE eh_addresses SET namespace_address_token = '144857' WHERE namespace_id = 999983 AND apartment_name = 'A-G18';
UPDATE eh_addresses SET namespace_address_token = '144858' WHERE namespace_id = 999983 AND apartment_name = 'A-G19';
UPDATE eh_addresses SET namespace_address_token = '144859' WHERE namespace_id = 999983 AND apartment_name = 'A-G20';
UPDATE eh_addresses SET namespace_address_token = '144860' WHERE namespace_id = 999983 AND apartment_name = 'A-G21';
UPDATE eh_addresses SET namespace_address_token = '144861' WHERE namespace_id = 999983 AND apartment_name = 'A-G22';
UPDATE eh_addresses SET namespace_address_token = '735045' WHERE namespace_id = 999983 AND apartment_name = 'A-G23A';
UPDATE eh_addresses SET namespace_address_token = '735046' WHERE namespace_id = 999983 AND apartment_name = 'A-G23B';
UPDATE eh_addresses SET namespace_address_token = '735047' WHERE namespace_id = 999983 AND apartment_name = 'A-G24A';
UPDATE eh_addresses SET namespace_address_token = '735048' WHERE namespace_id = 999983 AND apartment_name = 'A-G24B';
UPDATE eh_addresses SET namespace_address_token = '144864' WHERE namespace_id = 999983 AND apartment_name = 'A-G25';
UPDATE eh_addresses SET namespace_address_token = '144865' WHERE namespace_id = 999983 AND apartment_name = 'A-G26';
UPDATE eh_addresses SET namespace_address_token = '144866' WHERE namespace_id = 999983 AND apartment_name = 'A-G27';
UPDATE eh_addresses SET namespace_address_token = '144867' WHERE namespace_id = 999983 AND apartment_name = 'A-G28';
UPDATE eh_addresses SET namespace_address_token = '144868' WHERE namespace_id = 999983 AND apartment_name = 'A-G29';
UPDATE eh_addresses SET namespace_address_token = '144869' WHERE namespace_id = 999983 AND apartment_name = 'A-G30';
UPDATE eh_addresses SET namespace_address_token = '144870' WHERE namespace_id = 999983 AND apartment_name = 'A-G31';
UPDATE eh_addresses SET namespace_address_token = '144871' WHERE namespace_id = 999983 AND apartment_name = 'A-G32';
UPDATE eh_addresses SET namespace_address_token = '915819' WHERE namespace_id = 999983 AND apartment_name = 'A-G33/34/35/36/37/38/39/40/41/42/43/44/45/46';
UPDATE eh_addresses SET namespace_address_token = '1557624' WHERE namespace_id = 999983 AND apartment_name = 'A-G40-WB';
UPDATE eh_addresses SET namespace_address_token = '144872' WHERE namespace_id = 999983 AND apartment_name = 'A-G91';
UPDATE eh_addresses SET namespace_address_token = '1578090' WHERE namespace_id = 999983 AND apartment_name = 'B-101';
UPDATE eh_addresses SET namespace_address_token = '144903' WHERE namespace_id = 999983 AND apartment_name = 'B-G01';
UPDATE eh_addresses SET namespace_address_token = '144904' WHERE namespace_id = 999983 AND apartment_name = 'B-G02';
UPDATE eh_addresses SET namespace_address_token = '144905' WHERE namespace_id = 999983 AND apartment_name = 'B-G03';
UPDATE eh_addresses SET namespace_address_token = '144906' WHERE namespace_id = 999983 AND apartment_name = 'B-G04';
UPDATE eh_addresses SET namespace_address_token = '144907' WHERE namespace_id = 999983 AND apartment_name = 'B-G05';
UPDATE eh_addresses SET namespace_address_token = '144908' WHERE namespace_id = 999983 AND apartment_name = 'B-G06';
UPDATE eh_addresses SET namespace_address_token = '144909' WHERE namespace_id = 999983 AND apartment_name = 'B-G07';
UPDATE eh_addresses SET namespace_address_token = '144910' WHERE namespace_id = 999983 AND apartment_name = 'B-G08';
UPDATE eh_addresses SET namespace_address_token = '144911' WHERE namespace_id = 999983 AND apartment_name = 'B-G09';
UPDATE eh_addresses SET namespace_address_token = '144912' WHERE namespace_id = 999983 AND apartment_name = 'B-G10';
UPDATE eh_addresses SET namespace_address_token = '144913' WHERE namespace_id = 999983 AND apartment_name = 'B-G11';
UPDATE eh_addresses SET namespace_address_token = '144914' WHERE namespace_id = 999983 AND apartment_name = 'B-G12';
UPDATE eh_addresses SET namespace_address_token = '144915' WHERE namespace_id = 999983 AND apartment_name = 'B-G13';
UPDATE eh_addresses SET namespace_address_token = '144916' WHERE namespace_id = 999983 AND apartment_name = 'B-G14';
UPDATE eh_addresses SET namespace_address_token = '144917' WHERE namespace_id = 999983 AND apartment_name = 'B-G15';
UPDATE eh_addresses SET namespace_address_token = '144918' WHERE namespace_id = 999983 AND apartment_name = 'B-G16A';
UPDATE eh_addresses SET namespace_address_token = '144919' WHERE namespace_id = 999983 AND apartment_name = 'B-G16B';
UPDATE eh_addresses SET namespace_address_token = '144920' WHERE namespace_id = 999983 AND apartment_name = 'B-G16C';
UPDATE eh_addresses SET namespace_address_token = '144921' WHERE namespace_id = 999983 AND apartment_name = 'B-G16D';
UPDATE eh_addresses SET namespace_address_token = '144922' WHERE namespace_id = 999983 AND apartment_name = 'B-G16E';
UPDATE eh_addresses SET namespace_address_token = '144923' WHERE namespace_id = 999983 AND apartment_name = 'B-G16F';
UPDATE eh_addresses SET namespace_address_token = '144924' WHERE namespace_id = 999983 AND apartment_name = 'B-G16G';
UPDATE eh_addresses SET namespace_address_token = '144925' WHERE namespace_id = 999983 AND apartment_name = 'B-G17A';
UPDATE eh_addresses SET namespace_address_token = '144926' WHERE namespace_id = 999983 AND apartment_name = 'B-G17B';
UPDATE eh_addresses SET namespace_address_token = '144927' WHERE namespace_id = 999983 AND apartment_name = 'B-G17C';
UPDATE eh_addresses SET namespace_address_token = '144928' WHERE namespace_id = 999983 AND apartment_name = 'B-G17D';
UPDATE eh_addresses SET namespace_address_token = '144929' WHERE namespace_id = 999983 AND apartment_name = 'B-G17E';
UPDATE eh_addresses SET namespace_address_token = '144930' WHERE namespace_id = 999983 AND apartment_name = 'B-G17F';
UPDATE eh_addresses SET namespace_address_token = '144931' WHERE namespace_id = 999983 AND apartment_name = 'B-G17G';
UPDATE eh_addresses SET namespace_address_token = '144932' WHERE namespace_id = 999983 AND apartment_name = 'B-G17H';
UPDATE eh_addresses SET namespace_address_token = '144933' WHERE namespace_id = 999983 AND apartment_name = 'B-G17I';
UPDATE eh_addresses SET namespace_address_token = '144934' WHERE namespace_id = 999983 AND apartment_name = 'B-G18';
UPDATE eh_addresses SET namespace_address_token = '144935' WHERE namespace_id = 999983 AND apartment_name = 'B-G19';
UPDATE eh_addresses SET namespace_address_token = '144936' WHERE namespace_id = 999983 AND apartment_name = 'B-G20';
UPDATE eh_addresses SET namespace_address_token = '144937' WHERE namespace_id = 999983 AND apartment_name = 'B-G21';
UPDATE eh_addresses SET namespace_address_token = '144938' WHERE namespace_id = 999983 AND apartment_name = 'B-G22';
UPDATE eh_addresses SET namespace_address_token = '144939' WHERE namespace_id = 999983 AND apartment_name = 'B-G23';
UPDATE eh_addresses SET namespace_address_token = '144940' WHERE namespace_id = 999983 AND apartment_name = 'B-G24';
UPDATE eh_addresses SET namespace_address_token = '144941' WHERE namespace_id = 999983 AND apartment_name = 'B-G25';
UPDATE eh_addresses SET namespace_address_token = '144942' WHERE namespace_id = 999983 AND apartment_name = 'B-G26';
UPDATE eh_addresses SET namespace_address_token = '144943' WHERE namespace_id = 999983 AND apartment_name = 'B-G27';
UPDATE eh_addresses SET namespace_address_token = '144944' WHERE namespace_id = 999983 AND apartment_name = 'B-G28';
UPDATE eh_addresses SET namespace_address_token = '144945' WHERE namespace_id = 999983 AND apartment_name = 'B-G29';
UPDATE eh_addresses SET namespace_address_token = '144946' WHERE namespace_id = 999983 AND apartment_name = 'B-G30';
UPDATE eh_addresses SET namespace_address_token = '144947' WHERE namespace_id = 999983 AND apartment_name = 'B-G31';
UPDATE eh_addresses SET namespace_address_token = '144948' WHERE namespace_id = 999983 AND apartment_name = 'B-G32';
UPDATE eh_addresses SET namespace_address_token = '144949' WHERE namespace_id = 999983 AND apartment_name = 'B-G33';
UPDATE eh_addresses SET namespace_address_token = '144950' WHERE namespace_id = 999983 AND apartment_name = 'B-G34';
UPDATE eh_addresses SET namespace_address_token = '144951' WHERE namespace_id = 999983 AND apartment_name = 'B-G35';
UPDATE eh_addresses SET namespace_address_token = '144952' WHERE namespace_id = 999983 AND apartment_name = 'B-G36';
UPDATE eh_addresses SET namespace_address_token = '144953' WHERE namespace_id = 999983 AND apartment_name = 'B-G37';
UPDATE eh_addresses SET namespace_address_token = '144954' WHERE namespace_id = 999983 AND apartment_name = 'B-G38';
UPDATE eh_addresses SET namespace_address_token = '144955' WHERE namespace_id = 999983 AND apartment_name = 'B-G39';
UPDATE eh_addresses SET namespace_address_token = '144956' WHERE namespace_id = 999983 AND apartment_name = 'B-G40';
UPDATE eh_addresses SET namespace_address_token = '144957' WHERE namespace_id = 999983 AND apartment_name = 'B-G41';
UPDATE eh_addresses SET namespace_address_token = '144958' WHERE namespace_id = 999983 AND apartment_name = 'B-G42';
UPDATE eh_addresses SET namespace_address_token = '144959' WHERE namespace_id = 999983 AND apartment_name = 'B-G43';
UPDATE eh_addresses SET namespace_address_token = '144960' WHERE namespace_id = 999983 AND apartment_name = 'B-G44';
UPDATE eh_addresses SET namespace_address_token = '144961' WHERE namespace_id = 999983 AND apartment_name = 'B-G45';
UPDATE eh_addresses SET namespace_address_token = '144962' WHERE namespace_id = 999983 AND apartment_name = 'B-G45A';
UPDATE eh_addresses SET namespace_address_token = '144963' WHERE namespace_id = 999983 AND apartment_name = 'B-G46';
UPDATE eh_addresses SET namespace_address_token = '144964' WHERE namespace_id = 999983 AND apartment_name = 'B3-0101';
UPDATE eh_addresses SET namespace_address_token = '144965' WHERE namespace_id = 999983 AND apartment_name = 'B3-0102';
UPDATE eh_addresses SET namespace_address_token = '144966' WHERE namespace_id = 999983 AND apartment_name = 'B3-0201';
UPDATE eh_addresses SET namespace_address_token = '144967' WHERE namespace_id = 999983 AND apartment_name = 'B4-0101';
UPDATE eh_addresses SET namespace_address_token = '144968' WHERE namespace_id = 999983 AND apartment_name = 'B4-0102';
UPDATE eh_addresses SET namespace_address_token = '144969' WHERE namespace_id = 999983 AND apartment_name = 'B4-0103';
UPDATE eh_addresses SET namespace_address_token = '144970' WHERE namespace_id = 999983 AND apartment_name = 'B4-0105';
UPDATE eh_addresses SET namespace_address_token = '144972' WHERE namespace_id = 999983 AND apartment_name = 'B4-0201';
UPDATE eh_addresses SET namespace_address_token = '144973' WHERE namespace_id = 999983 AND apartment_name = 'B4-0202';
UPDATE eh_addresses SET namespace_address_token = '144974' WHERE namespace_id = 999983 AND apartment_name = 'B4-0304';
UPDATE eh_addresses SET namespace_address_token = '1578100' WHERE namespace_id = 999983 AND apartment_name = '书啦圈';
UPDATE eh_addresses SET namespace_address_token = '145016' WHERE namespace_id = 999983 AND apartment_name = 'C-101';
UPDATE eh_addresses SET namespace_address_token = '144975' WHERE namespace_id = 999983 AND apartment_name = 'C-G01';
UPDATE eh_addresses SET namespace_address_token = '144976' WHERE namespace_id = 999983 AND apartment_name = 'C-G02';
UPDATE eh_addresses SET namespace_address_token = '144977' WHERE namespace_id = 999983 AND apartment_name = 'C-G03';
UPDATE eh_addresses SET namespace_address_token = '144978' WHERE namespace_id = 999983 AND apartment_name = 'C-G04';
UPDATE eh_addresses SET namespace_address_token = '144979' WHERE namespace_id = 999983 AND apartment_name = 'C-G05';
UPDATE eh_addresses SET namespace_address_token = '144980' WHERE namespace_id = 999983 AND apartment_name = 'C-G06';
UPDATE eh_addresses SET namespace_address_token = '144981' WHERE namespace_id = 999983 AND apartment_name = 'C-G07';
UPDATE eh_addresses SET namespace_address_token = '144982' WHERE namespace_id = 999983 AND apartment_name = 'C-G08';
UPDATE eh_addresses SET namespace_address_token = '144983' WHERE namespace_id = 999983 AND apartment_name = 'C-G09';
UPDATE eh_addresses SET namespace_address_token = '144984' WHERE namespace_id = 999983 AND apartment_name = 'C-G10';
UPDATE eh_addresses SET namespace_address_token = '144985' WHERE namespace_id = 999983 AND apartment_name = 'C-G11';
UPDATE eh_addresses SET namespace_address_token = '144986' WHERE namespace_id = 999983 AND apartment_name = 'C-G12';
UPDATE eh_addresses SET namespace_address_token = '144987' WHERE namespace_id = 999983 AND apartment_name = 'C-G13';
UPDATE eh_addresses SET namespace_address_token = '144988' WHERE namespace_id = 999983 AND apartment_name = 'C-G14';
UPDATE eh_addresses SET namespace_address_token = '144989' WHERE namespace_id = 999983 AND apartment_name = 'C-G15';
UPDATE eh_addresses SET namespace_address_token = '144990' WHERE namespace_id = 999983 AND apartment_name = 'C-G16';
UPDATE eh_addresses SET namespace_address_token = '735049' WHERE namespace_id = 999983 AND apartment_name = 'C-G17A';
UPDATE eh_addresses SET namespace_address_token = '735050' WHERE namespace_id = 999983 AND apartment_name = 'C-G17B';
UPDATE eh_addresses SET namespace_address_token = '735051' WHERE namespace_id = 999983 AND apartment_name = 'C-G18A';
UPDATE eh_addresses SET namespace_address_token = '144993' WHERE namespace_id = 999983 AND apartment_name = 'C-G19';
UPDATE eh_addresses SET namespace_address_token = '144994' WHERE namespace_id = 999983 AND apartment_name = 'C-G20';
UPDATE eh_addresses SET namespace_address_token = '144995' WHERE namespace_id = 999983 AND apartment_name = 'C-G21';
UPDATE eh_addresses SET namespace_address_token = '144996' WHERE namespace_id = 999983 AND apartment_name = 'C-G22';
UPDATE eh_addresses SET namespace_address_token = '144997' WHERE namespace_id = 999983 AND apartment_name = 'C-G23';
UPDATE eh_addresses SET namespace_address_token = '144998' WHERE namespace_id = 999983 AND apartment_name = 'C-G24';
UPDATE eh_addresses SET namespace_address_token = '144999' WHERE namespace_id = 999983 AND apartment_name = 'C-G25';
UPDATE eh_addresses SET namespace_address_token = '145000' WHERE namespace_id = 999983 AND apartment_name = 'C-G25A';
UPDATE eh_addresses SET namespace_address_token = '145001' WHERE namespace_id = 999983 AND apartment_name = 'C-G26';
UPDATE eh_addresses SET namespace_address_token = '145004' WHERE namespace_id = 999983 AND apartment_name = 'C-G29A';
UPDATE eh_addresses SET namespace_address_token = '145005' WHERE namespace_id = 999983 AND apartment_name = 'C-G30';
UPDATE eh_addresses SET namespace_address_token = '145006' WHERE namespace_id = 999983 AND apartment_name = 'C-G31';
UPDATE eh_addresses SET namespace_address_token = '145007' WHERE namespace_id = 999983 AND apartment_name = 'C-G32';
UPDATE eh_addresses SET namespace_address_token = '145008' WHERE namespace_id = 999983 AND apartment_name = 'C-G33';
UPDATE eh_addresses SET namespace_address_token = '145009' WHERE namespace_id = 999983 AND apartment_name = 'C-G34';
UPDATE eh_addresses SET namespace_address_token = '145010' WHERE namespace_id = 999983 AND apartment_name = 'C-G35';
UPDATE eh_addresses SET namespace_address_token = '145011' WHERE namespace_id = 999983 AND apartment_name = 'C-G36';
UPDATE eh_addresses SET namespace_address_token = '145012' WHERE namespace_id = 999983 AND apartment_name = 'C-G37';
UPDATE eh_addresses SET namespace_address_token = '145013' WHERE namespace_id = 999983 AND apartment_name = 'C-G38';
UPDATE eh_addresses SET namespace_address_token = '145014' WHERE namespace_id = 999983 AND apartment_name = 'C-G39';
UPDATE eh_addresses SET namespace_address_token = '145015' WHERE namespace_id = 999983 AND apartment_name = 'C-G40';




-- 二维码加路由   add by xq.tian  2017/11/15
SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 1);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100019', 'zh_CN', '对不起，您无权查看此任务');



-- 添加正中会物业缴费入口 by wentian
-- beta
-- update `eh_launch_pad_items` set action_data = '{"url":"http://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}', action_type = 13 where namespace_id = 999983 and item_label = '物业查费';
-- release
UPDATE `eh_launch_pad_items` SET action_data = '{"url":"http://core.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}', action_type = 13 WHERE namespace_id = 999983 AND item_label = '物业查费';


-- 物业报修多入口数据整理 add by sw 20171204
DELETE FROM eh_categories WHERE STATUS = 0 AND path LIKE '任务%';
DELETE FROM eh_categories WHERE STATUS = 0 AND parent_id = 6;
UPDATE eh_launch_pad_items SET action_data = REPLACE(action_data, 'taskCategoryId=0', 'taskCategoryId=6') WHERE action_data LIKE '%zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=%';

UPDATE eh_web_menus SET data_type = 'react:/repair-management/task-list/6' WHERE id = 20140;
UPDATE eh_web_menus SET data_type = 'task_management_service_entry/6' WHERE id = 20150;
UPDATE eh_web_menus SET data_type = 'service_type_setting/6' WHERE id = 20170;
UPDATE eh_web_menus SET data_type = 'classify_setting/6' WHERE id = 20180;
UPDATE eh_web_menus SET data_type = 'task_statistics/6' WHERE id = 20191;

UPDATE eh_web_menus SET data_type = 'react:/repair-management/task-list/9' WHERE id = 20240;
UPDATE eh_web_menus SET data_type = 'task_management_service_entry/9' WHERE id = 20250;
UPDATE eh_web_menus SET data_type = 'classify_setting/9' WHERE id = 20280;
UPDATE eh_web_menus SET data_type = 'task_statistics/9' WHERE id = 20291;
UPDATE eh_web_menus SET data_type = 'react:/working-flow/flow-list/property-service/20100?moduleType=suggestion' WHERE id = 20258;
UPDATE eh_web_menus SET data_type = 'task_management_service_entry/1' WHERE id = 20225;

UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉与建议"}' WHERE namespace_id = 999961 AND item_label = '投诉与建议';
UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}' WHERE namespace_id = 999961 AND item_label = '物业报修';

UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=投诉保修"}' WHERE namespace_id = 999962 AND item_label = '投诉保修';

UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}' WHERE namespace_id = 999958 AND item_label = '物业报修';

UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉建议"}' WHERE namespace_id = 999967 AND item_label = '投诉建议';
UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}' WHERE namespace_id = 999967 AND item_label = '物业报修';

UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉与需求"}' WHERE namespace_id = 999983 AND item_label = '投诉与需求';

UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=家政服务"}' WHERE namespace_id = 999993 AND item_label = '家政服务';
UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=综合维修"}' WHERE namespace_id = 999993 AND item_label = '综合维修';

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ('9', '0', '0', '投诉建议', '投诉建议', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '0');

UPDATE eh_categories SET `name` = '物业报修', path = '物业报修' WHERE id = 6;

UPDATE eh_categories SET path = REPLACE(path, '任务', '物业报修') WHERE path LIKE '任务%' AND id NOT IN (203665, 203532, 202564);

UPDATE eh_categories SET path = REPLACE(path, '任务', '投诉建议'), parent_id = 9 WHERE id IN (203665, 203532, 202564) AND parent_id = 6;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20230,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20240,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20250,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20255,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20258,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20280,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20290,'', 'EhNamespaces', 999967,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),20291,'', 'EhNamespaces', 999967,2);

UPDATE eh_flows SET module_type = 'suggestion' WHERE module_type = 'any-module' AND id = 94;
UPDATE eh_flows SET module_type = 'suggestion' WHERE module_type = 'any-module' AND flow_main_id = 94;
UPDATE eh_flow_cases SET module_type = 'suggestion' WHERE module_type = 'any-module' AND flow_main_id = 94;
UPDATE eh_flow_evaluates SET module_type = 'suggestion' WHERE module_type = 'any-module' AND flow_main_id = 94;

UPDATE eh_flow_predefined_params SET module_type = 'suggestion' WHERE module_type = 'any-module' AND module_id = 20100;


SET @ns_id = 0;
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);

INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待受理', '待受理', '{"nodeType":"ACCEPTING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待分配', '待分配', '{"nodeType":"ASSIGNING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待处理', '待处理', '{"nodeType":"PROCESSING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '已完成', '已完成', '{"nodeType":"COMPLETED"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待确认费用', '待确认费用', '{"nodeType":"CONFIRMFEE"}', '2', NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '调整费用', '调整费用', '{"nodeType":"MOTIFYFEE"}', '2', NULL, NULL, NULL, NULL);


-- add by janson 201712011625
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'system', NULL, 1, 1009, 2000, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());

-- add by xiongying20171204
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21100', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21110', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21120', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21200', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21210', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('20422', '计价条款设置', '20400', NULL, 'react:/payment-management/bills-price', '0', '2', '/20000/20400/20422', 'park', '491', '20400', '3', NULL, 'module');

-- redmine xiongying20171204
UPDATE eh_var_field_group_scopes SET default_order = 5 WHERE namespace_id = 999974 AND community_id = 240111044332059749 AND group_display_name = '知识产权';
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '德国', '4', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '英国', '5', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '法国', '6', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '澳大利亚', '7', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '加拿大', '8', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '新加坡', '9', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '西班牙', '10', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '韩国', '11', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '新西兰', '12', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '香港', '13', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '台湾', '14', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '149', '其他', '15', '2', '1', NOW(), NULL, NULL, NULL);


-- 各个入口加入发帖类型  add by yanjun 20171204
SET @id = IFNULL((SELECT MAX(id) FROM eh_forum_service_types), 1);
INSERT INTO eh_forum_service_types SELECT (@id := @id + 1), namespace_id, 1, entry_id, 'topic', '话题', 0, NOW() FROM eh_forum_categories;
INSERT INTO eh_forum_service_types SELECT (@id := @id + 1), namespace_id, 1, entry_id, 'activity', '活动', 1, NOW() FROM eh_forum_categories;
INSERT INTO eh_forum_service_types SELECT (@id := @id + 1), namespace_id, 1, entry_id, 'poll', '投票', 2, NOW() FROM eh_forum_categories;

UPDATE eh_var_fields SET display_name = '经办人' WHERE display_name = '签约经办人';

-- 2018的法定假日 added by wh
SET @id =(SELECT MAX(id) FROM eh_punch_holidays);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-01-01','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-02-15','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-02-16','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-02-19','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-02-20','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-02-21','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'0','2018-02-11','1','2017-12-06 14:20:10','2018-02-20');
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'0','2018-02-24','1','2017-12-06 14:20:10','2017-12-21');
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-04-05','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-04-06','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'0','2018-04-08','1','2017-12-06 14:20:10','2018-04-06');
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-04-30','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-05-01','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'0','2018-04-28','1','2017-12-06 14:20:10','2018-04-30');
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-06-18','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-09-24','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-10-01','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-10-02','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-10-03','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-10-04','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'1','2018-10-05','1','2017-12-06 14:20:10',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'0','2018-09-29','1','2017-12-06 14:20:10','2018-10-04');
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES((@id:=@id+1),NULL,NULL,NULL,'0','2018-09-30','1','2017-12-06 14:20:10','2018-10-05');

-- 科技园的客户类型直接为organization表 add by xiongying20171207
update eh_contracts set customer_type = 2 where namespace_id = 1000000;