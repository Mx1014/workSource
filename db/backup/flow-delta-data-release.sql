-- step button names
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.start_step', '开始', 'start-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.approve_step', '下一步', 'approve-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reject_step', '驳回', 'reject-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.transfer_step', '转交', 'transfer-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.comment_step', '附言', 'comment-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.absort_step', '终止', 'absort-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reminder_step', '催办', 'reminder-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.evaluate_step', '评价', 'evaluate-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.end_step', '结束', 'end-step');

-- step message templates
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10001, 'zh_CN', '${nodeName} 已完成', '${nodeName} 已完成');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10002, 'zh_CN', '${nodeName} 被驳回', '${nodeName} 驳回');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10003, 'zh_CN', '${applierName} 已取消任务', '${applierName} 已取消任务');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10004, 'zh_CN', '${nodeName} 已转交', '${nodeName} 已转交');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10005, 'zh_CN', '${nodeName} 上传了 ${imageCount}张图片', '${nodeName} 上传了 ${imageCount}张图片');

-- text variables
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1000', '0', '0', '', '0', '', 'applierName', '发起人姓名', 'text', 'bean_id', 'flow-variable-applier-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1001', '0', '0', '', '0', '', 'applierPhone', '发起人手机号码', 'text', 'bean_id', 'flow-variable-applier-phone', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1002', '0', '0', '', '0', '', 'currProcessorName', '本节点处理人姓名', 'text', 'bean_id', 'flow-variable-curr-processor-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1003', '0', '0', '', '0', '', 'currProcessorPhone', '本节点处理人手机号码', 'text', 'bean_id', 'flow-variable-curr-processor-phone', '1');


-- user variables
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2000', '0', '0', '', '0', '', 'applier', '发起人', 'node_user', 'bean_id', 'flow-variable-applier', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2001', '0', '0', '', '0', '', 'prefixProcessor', '上一节点处理人', 'node_user', 'bean_id', 'flow-variable-prefix-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2002', '0', '0', '', '0', '', 'currProcessor', '本节点处理人', 'node_user', 'bean_id', 'flow-variable-current-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2003', '0', '0', '', '0', '', 'nextProcessor', '下个节点处理人', 'node_user', 'bean_id', 'flow-variable-next-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2004', '0', '0', '', '0', '', 'numberProcessor', 'N节点处理人', 'node_user', 'bean_id', 'flow-variable-n-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2005', '0', '0', '', '0', '', 'supervisor', '督办', 'node_user', 'bean_id', 'flow-variable-supervisor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2006', '0', '0', '', '0', '', 'targetProcessor', '目标节点处理人', 'node_user', 'bean_id', 'flow-variable-target-node-processor', '1');



SET @id := (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES((@id := @id+1),'1000000','0','0','0','/home','Bizs','FLOW_TASKS','任务管理','cs://1/image/aW1hZ2UvTVRvME1UWXpZak01WkdSa05USmxNekppT1RWaVlUa3lZemt3WkRabFlUSXhZZw','1','1','56','','0','0','1','0','','0',NULL,NULL,NULL,'1','pm_admin','0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES((@id := @id+1),'1000000','0','0','0','/home','Bizs','FLOW_TASKS','任务管理','cs://1/image/aW1hZ2UvTVRvME1UWXpZak01WkdSa05USmxNekppT1RWaVlUa3lZemt3WkRabFlUSXhZZw','1','1','56','','0','0','1','0','','0',NULL,NULL,NULL,'1','park_tourist','0',NULL);
