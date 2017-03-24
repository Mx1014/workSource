-- add more variables by janson
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1004', '0', '0', '', '0', '', 'currAllProcessorsName', '本节点处理人姓名', 'text', 'bean_id', 'flow-variable-curr-all-processors-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1005', '0', '0', '', '0', '', 'currAllProcessorsPhone', '本节点处理人手机号码', 'text', 'bean_id', 'flow-variable-curr-all-processors-phone', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1006', '0', '0', '', '0', '', 'prefixAllProcessorsName', '上一步处理人姓名', 'text', 'bean_id', 'flow-variable-prefix-all-processors-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1007', '0', '0', '', '0', '', 'prefixAllProcessorsPhone', '上一步处理人手机号码', 'text', 'bean_id', 'flow-variable-prefix-all-processors-phone', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1008', '0', '0', '', '0', '', 'transferTargetName', '被转交人姓名', 'text', 'bean_id', 'flow-variable-transfer-target-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1009', '0', '0', '', '0', '', 'transferTargetPhone', '被转交人手机号', 'text', 'bean_id', 'flow-variable-transfer-target-phone', '1');

UPDATE `eh_flow_variables` SET `label`='本节点操作执行人姓名' WHERE `id`='1002';
UPDATE `eh_flow_variables` SET `label`='本节点操作执行人手机号' WHERE `id`='1003';

UPDATE `eh_flow_variables` SET `label`='上一个节点执行人' WHERE `id`='2001';
UPDATE `eh_flow_variables` SET `label`='本节点执行人' WHERE `id`='2002';

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2007', '0', '0', '', '0', '', 'prefixProcessors', '上个节点处理人', 'node_user', 'bean_id', 'flow-variable-prefix-node-processors', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2008', '0', '0', '', '0', '', 'currProcessors', '本节点处理人', 'node_user', 'bean_id', 'flow-variable-curr-node-processors', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2009', '0', '0', '', '0', '', 'targetTransfer', '被转交人', 'node_user', 'bean_id', 'flow-variable-target-node-transfer', '1');

-- 对接工作流短信模版 add by sw 20170324
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 23, 'zh_CN', '新建月卡申请', '34956');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 24, 'zh_CN', '月卡申请驳回', '34957');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 25, 'zh_CN', '月卡申请审核通过', '34958');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 26, 'zh_CN', '月卡申请办理成功', '34959');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 35, 'zh_CN', '新建园区入驻', '39496');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 36, 'zh_CN', '园区入驻已受理', '39497');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 37, 'zh_CN', '园区入驻审核未通过', '39498');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 38, 'zh_CN', '园区入驻催办', '39499');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 39, 'zh_CN', '园区入驻办理成功', '39500');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 40, 'zh_CN', '新建资源预约', '39615');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 41, 'zh_CN', '资源预约督办', '39616');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 42, 'zh_CN', '资源预约已受理', '39617');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 43, 'zh_CN', '资源预约驳回', '39618');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 44, 'zh_CN', '资源预约催办', '39619');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 45, 'zh_CN', '资源预约办理成功', '39620');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 46, 'zh_CN', '物业报修督办受理', '39721');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 47, 'zh_CN', '物业报修待分配', '39722');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 48, 'zh_CN', '物业报修督办分配', '39723');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 49, 'zh_CN', '物业报修已完成', '39724');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40800', 23, 'zh_CN', '新建月卡申请', '34956');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40800', 24, 'zh_CN', '月卡申请驳回', '34957');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40800', 25, 'zh_CN', '月卡申请审核通过', '34958');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40800', 26, 'zh_CN', '月卡申请办理成功', '34959');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40100', 35, 'zh_CN', '新建园区入驻', '39496');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40100', 36, 'zh_CN', '园区入驻已受理', '39497');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40100', 37, 'zh_CN', '园区入驻审核未通过', '39498');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40100', 38, 'zh_CN', '园区入驻催办', '39499');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40100', 39, 'zh_CN', '园区入驻办理成功', '39500');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40400', 40, 'zh_CN', '新建资源预约', '39615');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40400', 41, 'zh_CN', '资源预约督办', '39616');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40400', 42, 'zh_CN', '资源预约已受理', '39617');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40400', 43, 'zh_CN', '资源预约驳回', '39618');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40400', 44, 'zh_CN', '资源预约催办', '39619');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:40400', 45, 'zh_CN', '资源预约办理成功', '39620');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 46, 'zh_CN', '物业报修督办受理', '39721');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 47, 'zh_CN', '物业报修待分配', '39722');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 48, 'zh_CN', '物业报修督办分配', '39723');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 49, 'zh_CN', '物业报修已完成', '39724');


-- oauth2client 1.0   add by xq.tian 2017/03/09
--
-- 门禁icon
--
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999986, 0, 0, 0, '/home', 'Bizs', 'ACLINK', '门禁', 'cs://1/image/aW1hZ2UvTVRwaVl6ZGtPVFE0TURZd1pUZzRZekppTTJNMVl6QmlPVFprTWpWbFpHRXlNUQ', 1, 1, 13, '{"url":"https://core.zuolin.com/evh/oauth2cli/redirect/huanteng?serviceUrl=%2fzlapp%2fdist%2f%3fhideNavigationBar%3d1%23%2faccess-control%2flock-list-cy&hideNavigationBar=1#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'default', 0, NULL, NULL);
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999986, 0, 0, 0, '/home', 'Bizs', 'ACLINK', '门禁', 'cs://1/image/aW1hZ2UvTVRwaVl6ZGtPVFE0TURZd1pUZzRZekppTTJNMVl6QmlPVFprTWpWbFpHRXlNUQ', 1, 1, 13, '{"url":"https://core.zuolin.com/evh/oauth2cli/redirect/huanteng?serviceUrl=%2fzlapp%2fdist%2f%3fhideNavigationBar%3d1%23%2faccess-control%2flock-list-cy&hideNavigationBar=1#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL);


-- 帖子评论时给创建者或父评论者发送消息模板，add by tt, 20170316
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'forum.notification', 2, 'zh_CN', '帖子评论给创建者发消息', '有人评论了你的帖子\t${userName} 评论了你的帖子 ${postName}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'forum.notification', 3, 'zh_CN', '帖子评论给父评论者发消息', '有人回复了你的评论\t${userName} 回复了你在帖子 ${postName} 的评论。', 0);

-- 更改帖子删除提示，add by tt, 20170316
UPDATE `eh_locale_strings` SET `text`='该帖子已被主人删除' WHERE  `id`=44;