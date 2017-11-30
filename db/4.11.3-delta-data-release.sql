-- 增加param by st.zheng
set @eh_flow_predefined_params_id = (select max(id) from eh_flow_predefined_params );
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`) VALUES (@eh_flow_predefined_params_id:=@eh_flow_predefined_params_id+1, '0', '0', '', '20100', 'any-module', 'flow_node', '待确认费用', '待确认费用', '{\"nodeType\":\"CONFIRMFEE\"}', '2');
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`) VALUES (@eh_flow_predefined_params_id:=@eh_flow_predefined_params_id+1, '0', '0', '', '20100', 'any-module', 'flow_node', '调整费用', '调整费用', '{\"nodeType\":\"MOTIFYFEE\"}', '2');

-- 增加费用清单表单 by st.zheng
set @eh_general_forms_id = (select max(id) from eh_general_forms);
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `create_time`) VALUES (@eh_general_forms_id + 1, '0', '0', '0', 'PMTASK', '费用清单', @eh_general_forms_id + 1, '0', 'DEFAULT_JSON', '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"联系电话\",\"fieldExtra\":\"{\\\"limitLength\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"企业\",\"fieldExtra\":\"{}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dataSourceType\":\"USER_ADDRESS\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋门牌\",\"fieldExtra\":\"{}\",\"fieldName\":\"USER_ADDRESS\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"HIDDEN\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入服务费\",\"fieldDisplayName\":\"服务费\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"服务费\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"物品\",\"fieldExtra\":\"{\\\"formFields\\\":[{\\\"fieldDesc\\\":\\\"请输入物品名称\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"EDITABLE\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"SINGLE_LINE_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"limitWord\\\\\\\":8}\\\",\\\"validatorType\\\":\\\"TEXT_LIMIT\\\",\\\"fieldName\\\":\\\"物品名称\\\",\\\"fieldDisplayName\\\":\\\"物品名称\\\"},{\\\"fieldDesc\\\":\\\"请输入单价（元）\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"EDITABLE\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"NUMBER_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"defaultValue\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"validatorType\\\":\\\"NUM_LIMIT\\\",\\\"fieldName\\\":\\\"单价\\\",\\\"fieldDisplayName\\\":\\\"单价\\\"},{\\\"fieldDesc\\\":\\\"请输入物品数量\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"EDITABLE\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"NUMBER_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"defaultValue\\\\\\\":\\\\\\\"\\\\\\\"}\\\",\\\"validatorType\\\":\\\"NUM_LIMIT\\\",\\\"fieldName\\\":\\\"数量\\\",\\\"fieldDisplayName\\\":\\\"数量\\\"},{\\\"fieldDesc\\\":\\\"\\\",\\\"requiredFlag\\\":0,\\\"dynamicFlag\\\":0,\\\"visibleType\\\":\\\"READONLY\\\",\\\"renderType\\\":\\\"DEFAULT\\\",\\\"fieldType\\\":\\\"NUMBER_TEXT\\\",\\\"fieldExtra\\\":\\\"{\\\\\\\"defaultValue\\\\\\\":\\\\\\\"${物品.单价}*${物品.数量}\\\\\\\"}\\\",\\\"validatorType\\\":\\\"NUM_LIMIT\\\",\\\"fieldName\\\":\\\"小计\\\",\\\"fieldDisplayName\\\":\\\"小计\\\"}]}\",\"fieldName\":\"物品\",\"fieldType\":\"SUBFORM\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"总计\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"${服务费}+sum(${物品.小计})\\\"}\",\"fieldName\":\"总计\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"READONLY\"}]', '1', now());

-- merge from asset-org by xiongying
SET @id := (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'address', '20005', 'zh_CN', '门牌已关联合同，无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'building', '10003', 'zh_CN', '楼栋下有门牌已关联合同，无法删除');

SET @config_id = (SELECT MAX(id) from `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'ebei.url', 'http://183.62.222.87:5902/sf', '一碑url', '0', NULL);

-- merge from forum-2.6 by yanjun start 201711291416
-- 园区生活、招聘与求职去掉tag
UPDATE eh_launch_pad_items set action_data = '{"forumEntryId":"1"}' where namespace_id = 999983 and item_label = '园区生活' and action_type = 62;
UPDATE eh_launch_pad_items set action_data = '{"forumEntryId":"2"}' where namespace_id = 999983 and item_label = '招聘与求职' and action_type = 62;

SET @id = (SELECT MAX(id) from eh_hot_tags);
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

INSERT  INTO  `eh_var_field_groups` VALUES (10000, 'equipment_inspection', 0, CONCAT('/',10000), '设备', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10001, 'equipment_inspection', 0, CONCAT('/',10001), '装修', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10002, 'equipment_inspection', 0, CONCAT('/',10002), '空置房', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10003, 'equipment_inspection', 0, CONCAT('/',10003), '安保', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10004, 'equipment_inspection', 0, CONCAT('/',10004), '日常工作检查', '',0,  null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10005, 'equipment_inspection', 0, CONCAT('/',10005), '公共设施检查', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10006, 'equipment_inspection', 0, CONCAT('/',10006), '周末值班', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10007, 'equipment_inspection', 0, CONCAT('/',10007), '安全检查', '', 0, null, 2, 1, now(), null, null);
INSERT  INTO  `eh_var_field_groups` VALUES (10008, 'equipment_inspection', 0, CONCAT('/',10008), '其他', '', 0, null, 2, 1, now(), null, null);


-- 其他类型表单项  08

INSERT  INTO  `eh_var_fields` VALUES (10000, 'equipment_inspection', 'name', '设备名称', 'String', 10008, CONCAT('/',10008), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10001, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10008, CONCAT('/',10008), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10002, 'equipment_inspection', 'status', '当前状态', 'Long', 10008, CONCAT('/',10008), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10005, 'equipment_inspection', 'location', '安装位置', 'String', 10008, CONCAT('/',10008), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10006, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10008, CONCAT('/',10008), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');


-- 其他类型下拉菜单选项 08


INSERT INTO `eh_var_field_items` VALUES (10000, 'equipment_inspection', 10002, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10001, 'equipment_inspection', 10002, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10002, 'equipment_inspection', 10002, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10003, 'equipment_inspection', 10002, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10004, 'equipment_inspection', 10002, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10005, 'equipment_inspection', 10002, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10030, 'equipment_inspection', 10006, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10031, 'equipment_inspection', 10006, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10032, 'equipment_inspection', 10006, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10033, 'equipment_inspection', 10006, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10034, 'equipment_inspection', 10006, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10035, 'equipment_inspection', 10006, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10036, 'equipment_inspection', 10006, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10037, 'equipment_inspection', 10006, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10038, 'equipment_inspection', 10006, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10049, 'equipment_inspection', 10006, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10040, 'equipment_inspection', 10006, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10041, 'equipment_inspection', 10006, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10042, 'equipment_inspection', 10006, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10043, 'equipment_inspection', 10006, '其他', 14, 2, 1, now(), null, null,14);


-- 安全检查表单项  07


INSERT  INTO  `eh_var_fields` VALUES (10100, 'equipment_inspection', 'name', '设备名称', 'String', 10007, CONCAT('/',10007), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10101, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10007, CONCAT('/',10007), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10102, 'equipment_inspection', 'status', '当前状态', 'Long', 10007, CONCAT('/',10007), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10105, 'equipment_inspection', 'location', '安装位置', 'String', 10007, CONCAT('/',10007), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10106, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10007, CONCAT('/',10007), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');




INSERT INTO `eh_var_field_items` VALUES (10100, 'equipment_inspection', 10102, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10101, 'equipment_inspection', 10102, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10102, 'equipment_inspection', 10102, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10103, 'equipment_inspection', 10102, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10104, 'equipment_inspection', 10102, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10105, 'equipment_inspection', 10102, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10130, 'equipment_inspection', 10106, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10131, 'equipment_inspection', 10106, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10132, 'equipment_inspection', 10106, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10133, 'equipment_inspection', 10106, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10134, 'equipment_inspection', 10106, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10135, 'equipment_inspection', 10106, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10136, 'equipment_inspection', 10106, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10137, 'equipment_inspection', 10106, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10138, 'equipment_inspection', 10106, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10149, 'equipment_inspection', 10106, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10140, 'equipment_inspection', 10106, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10141, 'equipment_inspection', 10106, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10142, 'equipment_inspection', 10106, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10143, 'equipment_inspection', 10106, '其他', 14, 2, 1, now(), null, null,14);





-- 周末值班表单项  06


INSERT  INTO  `eh_var_fields` VALUES (10200, 'equipment_inspection', 'name', '设备名称', 'String', 10006, CONCAT('/',10006), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10201, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10006, CONCAT('/',10006), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10202, 'equipment_inspection', 'status', '当前状态', 'Long', 10006, CONCAT('/',10006), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10205, 'equipment_inspection', 'location', '安装位置', 'String', 10006, CONCAT('/',10006), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10206, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10006, CONCAT('/',10006), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');

-- 周末值班下拉菜单选项 06


INSERT INTO `eh_var_field_items` VALUES (10200, 'equipment_inspection', 10202, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10201, 'equipment_inspection', 10202, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10202, 'equipment_inspection', 10202, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10203, 'equipment_inspection', 10202, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10204, 'equipment_inspection', 10202, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10205, 'equipment_inspection', 10202, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10230, 'equipment_inspection', 10206, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10231, 'equipment_inspection', 10206, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10232, 'equipment_inspection', 10206, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10233, 'equipment_inspection', 10206, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10234, 'equipment_inspection', 10206, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10235, 'equipment_inspection', 10206, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10236, 'equipment_inspection', 10206, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10237, 'equipment_inspection', 10206, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10238, 'equipment_inspection', 10206, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10249, 'equipment_inspection', 10206, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10240, 'equipment_inspection', 10206, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10241, 'equipment_inspection', 10206, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10242, 'equipment_inspection', 10206, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10243, 'equipment_inspection', 10206, '其他', 14, 2, 1, now(), null, null,14);




-- 公共设施检查表单项  05


INSERT  INTO  `eh_var_fields` VALUES (10300, 'equipment_inspection', 'name', '设备名称', 'String', 10005, CONCAT('/',10005), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10301, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10005, CONCAT('/',10005), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10302, 'equipment_inspection', 'status', '当前状态', 'Long', 10005, CONCAT('/',10005), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10305, 'equipment_inspection', 'location', '安装位置', 'String', 10005, CONCAT('/',10005), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10306, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10005, CONCAT('/',10005), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');

-- 公共设施下拉菜单选项 05


INSERT INTO `eh_var_field_items` VALUES (10300, 'equipment_inspection', 10302, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10301, 'equipment_inspection', 10302, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10302, 'equipment_inspection', 10302, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10303, 'equipment_inspection', 10302, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10304, 'equipment_inspection', 10302, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10305, 'equipment_inspection', 10302, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10330, 'equipment_inspection', 10306, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10331, 'equipment_inspection', 10306, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10332, 'equipment_inspection', 10306, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10333, 'equipment_inspection', 10306, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10334, 'equipment_inspection', 10306, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10335, 'equipment_inspection', 10306, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10336, 'equipment_inspection', 10306, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10337, 'equipment_inspection', 10306, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10338, 'equipment_inspection', 10306, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10349, 'equipment_inspection', 10306, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10340, 'equipment_inspection', 10306, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10341, 'equipment_inspection', 10306, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10342, 'equipment_inspection', 10306, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10343, 'equipment_inspection', 10306, '其他', 14, 2, 1, now(), null, null,14);



-- 日常工作检查表单项  04


INSERT  INTO  `eh_var_fields` VALUES (10400, 'equipment_inspection', 'name', '设备名称', 'String', 10004, CONCAT('/',10004), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10401, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10004, CONCAT('/',10004), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10402, 'equipment_inspection', 'status', '当前状态', 'Long', 10004, CONCAT('/',10004), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10405, 'equipment_inspection', 'location', '安装位置', 'String', 10004, CONCAT('/',10004), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10406, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10004, CONCAT('/',10004), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');

-- 日常工作检查下拉菜单选项  04


INSERT INTO `eh_var_field_items` VALUES (10400, 'equipment_inspection', 10402, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10401, 'equipment_inspection', 10402, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10402, 'equipment_inspection', 10402, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10403, 'equipment_inspection', 10402, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10404, 'equipment_inspection', 10402, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10405, 'equipment_inspection', 10402, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10430, 'equipment_inspection', 10406, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10431, 'equipment_inspection', 10406, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10432, 'equipment_inspection', 10406, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10433, 'equipment_inspection', 10406, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10434, 'equipment_inspection', 10406, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10435, 'equipment_inspection', 10406, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10436, 'equipment_inspection', 10406, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10437, 'equipment_inspection', 10406, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10438, 'equipment_inspection', 10406, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10449, 'equipment_inspection', 10406, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10440, 'equipment_inspection', 10406, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10441, 'equipment_inspection', 10406, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10442, 'equipment_inspection', 10406, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10443, 'equipment_inspection', 10406, '其他', 14, 2, 1, now(), null, null,14);


-- 安保表单项   03


INSERT  INTO  `eh_var_fields` VALUES (10500, 'equipment_inspection', 'name', '设备名称', 'String', 10003, CONCAT('/',10003), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10501, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10003, CONCAT('/',10003), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10502, 'equipment_inspection', 'status', '当前状态', 'Long', 10003, CONCAT('/',10003), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10505, 'equipment_inspection', 'location', '安装位置', 'String', 10003, CONCAT('/',10003), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10506, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10003, CONCAT('/',10003), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');


-- 安保下拉菜单选项  03

INSERT INTO `eh_var_field_items` VALUES (10500, 'equipment_inspection', 10502, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10501, 'equipment_inspection', 10502, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10502, 'equipment_inspection', 10502, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10503, 'equipment_inspection', 10502, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10504, 'equipment_inspection', 10502, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10505, 'equipment_inspection', 10502, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10530, 'equipment_inspection', 10506, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10531, 'equipment_inspection', 10506, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10532, 'equipment_inspection', 10506, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10533, 'equipment_inspection', 10506, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10534, 'equipment_inspection', 10506, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10535, 'equipment_inspection', 10506, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10536, 'equipment_inspection', 10506, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10537, 'equipment_inspection', 10506, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10538, 'equipment_inspection', 10506, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10549, 'equipment_inspection', 10506, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10540, 'equipment_inspection', 10506, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10541, 'equipment_inspection', 10506, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10542, 'equipment_inspection', 10506, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10543, 'equipment_inspection', 10506, '其他', 14, 2, 1, now(), null, null,14);




-- 空置房表单项  02

INSERT  INTO  `eh_var_fields` VALUES (10600, 'equipment_inspection', 'name', '设备名称', 'String', 10002, CONCAT('/',10002), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10601, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10002, CONCAT('/',10002), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10602, 'equipment_inspection', 'status', '当前状态', 'Long', 10002, CONCAT('/',10002), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10605, 'equipment_inspection', 'location', '安装位置', 'String', 10002, CONCAT('/',10002), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10606, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10002, CONCAT('/',10002), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');


-- 空置房下拉菜单选项  02

INSERT INTO `eh_var_field_items` VALUES (10600, 'equipment_inspection', 10602, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10601, 'equipment_inspection', 10602, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10602, 'equipment_inspection', 10602, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10603, 'equipment_inspection', 10602, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10604, 'equipment_inspection', 10602, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10605, 'equipment_inspection', 10602, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10630, 'equipment_inspection', 10606, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10631, 'equipment_inspection', 10606, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10632, 'equipment_inspection', 10606, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10633, 'equipment_inspection', 10606, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10634, 'equipment_inspection', 10606, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10635, 'equipment_inspection', 10606, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10636, 'equipment_inspection', 10606, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10637, 'equipment_inspection', 10606, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10638, 'equipment_inspection', 10606, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10649, 'equipment_inspection', 10606, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10640, 'equipment_inspection', 10606, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10641, 'equipment_inspection', 10606, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10642, 'equipment_inspection', 10606, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10643, 'equipment_inspection', 10606, '其他', 14, 2, 1, now(), null, null,14);



-- 装修表单项  01

INSERT  INTO  `eh_var_fields` VALUES (10700, 'equipment_inspection', 'name', '设备名称', 'String', 10001, CONCAT('/',10001), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10701, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10001, CONCAT('/',10001), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10702, 'equipment_inspection', 'status', '当前状态', 'Long', 10001, CONCAT('/',10001), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10705, 'equipment_inspection', 'location', '安装位置', 'String', 10001, CONCAT('/',10001), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10706, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10001, CONCAT('/',10001), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');



-- 装修下拉菜单选项 01

INSERT INTO `eh_var_field_items` VALUES (10700, 'equipment_inspection', 10702, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10701, 'equipment_inspection', 10702, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10702, 'equipment_inspection', 10702, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10703, 'equipment_inspection', 10702, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10704, 'equipment_inspection', 10702, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10705, 'equipment_inspection', 10702, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10730, 'equipment_inspection', 10706, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10731, 'equipment_inspection', 10706, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10732, 'equipment_inspection', 10706, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10733, 'equipment_inspection', 10706, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10734, 'equipment_inspection', 10706, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10735, 'equipment_inspection', 10706, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10736, 'equipment_inspection', 10706, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10737, 'equipment_inspection', 10706, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10738, 'equipment_inspection', 10706, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10749, 'equipment_inspection', 10706, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10740, 'equipment_inspection', 10706, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10741, 'equipment_inspection', 10706, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10742, 'equipment_inspection', 10706, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10743, 'equipment_inspection', 10706, '其他', 14, 2, 1, now(), null, null,14);



-- 设备表单项  00


INSERT  INTO  `eh_var_fields` VALUES (10800, 'equipment_inspection', 'name', '设备名称', 'String', 10000, CONCAT('/',10000), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10801, 'equipment_inspection', 'customNumber', '设备编号', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10802, 'equipment_inspection', 'status', '当前状态', 'Long', 10000, CONCAT('/',10000), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10803, 'equipment_inspection', 'brandName', '品牌名称', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10804, 'equipment_inspection', 'equipmentModel', '设备型号', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10805, 'equipment_inspection', 'location', '安装位置', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10806, 'equipment_inspection', 'categoryId', '设备类型', 'Long', 10000, CONCAT('/',10000), 1, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10807, 'equipment_inspection', 'installationTime', '安装日期', 'Date', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10808, 'equipment_inspection', 'discardTime', '报废日期', 'Date', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10809, 'equipment_inspection', 'repairTime', '免保日期', 'Date', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10810, 'equipment_inspection', 'manufacturer', '生产厂家', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10811, 'equipment_inspection', 'constructionParty', '施工方', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10812, 'equipment_inspection', 'manager', '联系人', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10813, 'equipment_inspection', 'managerContact', '联系方式', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10814, 'equipment_inspection', 'parameter', '设备参数', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10815, 'equipment_inspection', 'detail', '设备详情', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10816, 'equipment_inspection', 'provenance', '产地', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10817, 'equipment_inspection', 'sequenceNo', '出厂编号', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10818, 'equipment_inspection', 'factoryTime', '出厂日期', 'Date', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10819, 'equipment_inspection', 'price', '购买价格', 'BigDecimal', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10820, 'equipment_inspection', 'buyTime', '购买日期', 'Date', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10821, 'equipment_inspection', 'depreciationYears', '折旧年限', 'Long', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10822, 'equipment_inspection', 'qrCodeFlag', '二维码状态', 'Long', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT  INTO  `eh_var_fields` VALUES (10823, 'equipment_inspection', 'remarks', '备注', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"multiText\", \"length\": 2048}');
INSERT  INTO  `eh_var_fields` VALUES (10824, 'equipment_inspection', 'attachments', '附件', 'String', 10000, CONCAT('/',10000), 0, null, 2, 1, now(),null ,null,'{\"fieldParamType\": \"file\", \"length\": 32}');



-- 下拉菜单选项  00



INSERT INTO `eh_var_field_items` VALUES (10800, 'equipment_inspection', 10802, '不完整', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10801, 'equipment_inspection', 10802, '使用中', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10802, 'equipment_inspection', 10802, '维修中', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10803, 'equipment_inspection', 10802, '报废', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10804, 'equipment_inspection', 10802, '停用', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10805, 'equipment_inspection', 10802, '备用', 6, 2, 1, now(), null, null,6);

INSERT INTO `eh_var_field_items` VALUES (10830, 'equipment_inspection', 10806, '消防', 1, 2, 1, now(), null, null,1);
INSERT INTO `eh_var_field_items` VALUES (10831, 'equipment_inspection', 10806, '强电', 2, 2, 1, now(), null, null,2);
INSERT INTO `eh_var_field_items` VALUES (10832, 'equipment_inspection', 10806, '弱电', 3, 2, 1, now(), null, null,3);
INSERT INTO `eh_var_field_items` VALUES (10833, 'equipment_inspection', 10806, '电梯', 4, 2, 1, now(), null, null,4);
INSERT INTO `eh_var_field_items` VALUES (10834, 'equipment_inspection', 10806, '空调', 5, 2, 1, now(), null, null,5);
INSERT INTO `eh_var_field_items` VALUES (10835, 'equipment_inspection', 10806, '给排水', 6, 2, 1, now(), null, null,6);
INSERT INTO `eh_var_field_items` VALUES (10836, 'equipment_inspection', 10806, '空置房', 7, 2, 1, now(), null, null,7);
INSERT INTO `eh_var_field_items` VALUES (10837, 'equipment_inspection', 10806, '装修', 8, 2, 1, now(), null, null,8);
INSERT INTO `eh_var_field_items` VALUES (10838, 'equipment_inspection', 10806, '安保', 9, 2, 1, now(), null, null,9);
INSERT INTO `eh_var_field_items` VALUES (10849, 'equipment_inspection', 10806, '日常工作检查', 10, 2, 1, now(), null, null,10);
INSERT INTO `eh_var_field_items` VALUES (10840, 'equipment_inspection', 10806, '公共设施检查', 11, 2, 1, now(), null, null,11);
INSERT INTO `eh_var_field_items` VALUES (10841, 'equipment_inspection', 10806, '周末值班', 12, 2, 1, now(), null, null,12);
INSERT INTO `eh_var_field_items` VALUES (10842, 'equipment_inspection', 10806, '安全检查', 13, 2, 1, now(), null, null,13);
INSERT INTO `eh_var_field_items` VALUES (10843, 'equipment_inspection', 10806, '其他', 14, 2, 1, now(), null, null,14);

INSERT INTO `eh_var_field_items` VALUES (10860, 'equipment_inspection', 10822, '停用', 1, 2, 1, now(), null, null,0);
INSERT INTO `eh_var_field_items` VALUES (10861, 'equipment_inspection', 10822, '启用', 2, 2, 1, now(), null, null,1);



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
((@eh_categories_id:=@eh_categories_id+1), '6', '0', '物业报修', '任务/物业报修', '-1', '2', now(), NULL, NULL, NULL, @namespace_id);

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
SET @configId = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configId := @configId + 1), 'wx.offical.account.appid', 'wxf273770e5f383b2f', '公众号开发者AppId-杭州', '999957', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configId := @configId + 1), 'wx.offical.account.secret', '7b8267c856f293f2eeaeffbf062c3583', '公众号开发者AppId-杭州', '999957', NULL);

-- merge from yuekongjian 1.0  by yanjun 201711301748  end