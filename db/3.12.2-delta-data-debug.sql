-- merge from flow-delta-data-debug.sql by lqs 20161214
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.start_step', '开始', 'start-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.approve_step', '下一步', 'approve-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reject_step', '驳回', 'reject-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.transfer_step', '转交', 'transfer-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.comment_step', '附言', 'comment-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.absort_step', '终止', 'absort-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reminder_step', '催办', 'reminder-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.evaluate_step', '评价', 'evaluate-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.end_step', '结束', 'end-step');

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

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1000', '0', '0', '', '0', '', 'applierName', '发起人姓名', 'text', 'bean_id', 'flow-variable-applier-name', '1');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1001', '0', '0', '', '0', '', 'applierPhone', '发起人手机号码', 'text', 'bean_id', 'flow-variable-applier-phone', '1');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1002', '0', '0', '', '0', '', 'currProcessorName', '本节点处理人姓名', 'text', 'bean_id', 'flow-variable-curr-processor-name', '1');

INSERT INTO `ehcore_aclink`.`eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1003', '0', '0', '', '0', '', 'currProcessorPhone', '本节点处理人手机号码', 'text', 'bean_id', 'flow-variable-curr-processor-phone', '1');



-- merge from parking-clearance-1.0-delta-data-debug.sql by lqs 20161215
--
-- 车辆放行模块   add by xq.tian  2016/12/05
--
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('20900', '车辆放行', '20000', '/20000/20900', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10056', '0', '车辆放行 申请放行', '车辆放行 申请放行权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10057', '0', '车辆放行 处理放行任务', '车辆放行 处理放行任务权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10056, 1001, 0, 'EhAclRoles', 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`,  `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10057, 1001, 0, 'EhAclRoles', 1, NOW());

SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10056', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10057', NULL, '0', UTC_TIMESTAMP());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20900, '车辆放行', 20000, NULL, 'parking_clearance', 1, 2, '/20000/20900', 'park', 300);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20910, '权限设置', 20900, NULL, 'vehicle_setting', 0, 2, '/20000/20900/20910', 'park', 301);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20920, '放行记录', 20900, NULL, 'release_record', 0, 2, '/20000/20900/20920', 'park', 302);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20930, '工作流设置', 20900, NULL, 'react:/working-flow/flow-list/vehicle-release/20900', 0, 2, '/20000/20900/20930', 'park', 303);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20900, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20910, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20920, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20930, '', 'EhNamespaces', 999984, 2);

--
-- 模板
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '1', 'zh_CN', '停车放行申请人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"multi_line"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '2', 'zh_CN', '停车放行处理人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"申请人","value":"${applicant}","entityType":"list"},{"key":"手机号","value":"${identifierToken}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"multi_line"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '3', 'zh_CN', '工作流摘要内容', '车牌号码：${plateNumber}\n来访时间：${clearanceTime}', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '1', 'zh_CN', '无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '2', 'zh_CN', '对不起,您没有权限申请放行');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '3', 'zh_CN', '对不起,您没有权限处理放行任务');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10001', 'zh_CN', '删除用户失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10002', 'zh_CN', '没有启用的工作流');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '1', 'zh_CN', '处理中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '2', 'zh_CN', '已完成');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '3', 'zh_CN', '已取消');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '4', 'zh_CN', '待处理');


INSERT INTO `eh_launch_advertisements` (`id`, `namespace_id`, `content_type`, `content_uri`, `times_per_day`, `display_interval`, `duration_time`, `skip_flag`, `action_type`, `action_data`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
VALUES ('1', '0', 'VIDEO', 'cs://1/file/ZmlsZS9Nem96TmprMU1EaGxORE0xWkdSbVptTXdZemcwWmpaaE16Y3pOMk00T0RSak53', '1', '0', '3', '1', '14', '{\"url\":\"http://www.baidu.com\"}', '2', NULL, NULL, NULL, NULL);


SELECT MAX(id) FROM `eh_launch_pad_items` INTO @launch_pad_item_id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE', '车辆放行', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '57', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE_TASK', '放行任务', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '58', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);

--
-- 新增的模板  add by xq.tian  2016/12/16
--
SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10003', 'zh_CN', '用户已添加');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parameters.error', '10001', 'zh_CN', '参数长度超过限制');


-- 添加消息标题 by lqs 20161217
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'message.title', '左邻App', '消息标题：左邻APP');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (999993, 'message.title', '海岸馨服务', '消息标题：海岸馨服务');


-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES (@id:=@id+1, 'rental.flow', 1, 'zh_CN', '工作流列表内容', '资源名称：${resourceName}\n使用时间：${useDetail}', 0);

-- 资源预订工作流中文，added by wh ,2016-12-19
SET @id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'user', 'zh_CN', '发起人');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'contact', 'zh_CN', '联系电话');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'organization', 'zh_CN', '企业');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'resourceName', 'zh_CN', '资源名称');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'useDetail', 'zh_CN', '使用时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'count', 'zh_CN', '预约数量');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'price', 'zh_CN', '订单金额');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'item', 'zh_CN', '购买商品');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'content', 'zh_CN', '显示内容');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'license', 'zh_CN', '车牌');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'remark', 'zh_CN', '备注');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'attachment', 'zh_CN', '附件');

--
-- 清华信息港停车场测试数据   add by xq.tian  2016/12/19
--
SELECT max(id) FROM `eh_parking_lots` INTO @max_id;
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`, `rate_flag`, `recharge_month_count`, `recharge_type`, `is_support_recharge`, `namespace_id`)
VALUES ((@max_id := @max_id + 1), 'community', '240111044331055835', '清华信息港停车场1', 'BOSIGAO', '', '1', '2', '1025', '2016-03-31 17:07:20', '1', '0', '0', '1', '1', '1', '999984');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`, `rate_flag`, `recharge_month_count`, `recharge_type`, `is_support_recharge`, `namespace_id`)
VALUES ((@max_id := @max_id + 1), 'community', '240111044331055835', '清华信息港停车场2', 'BOSIGAO', '', '1', '2', '1025', '2016-03-31 17:07:20', '1', '0', '0', '1', '1', '1', '999984');


--
-- 园区入驻 2.3  add by xq.tian  2016/12/20
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'expansion', '1', 'zh_CN', '园区入驻工作流摘要内容', '申请类型: ${applyType}\n面积需求: ${areaSize} 平米', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'expansion', '2', 'zh_CN', '园区入驻工作流详情内容', '[{"key":"发起人","value":"${applyUserName}","entityType":"list"},{"key":"联系电话","value":"${contactPhone}","entityType":"list"},{"key":"企业","value":"${enterpriseName}","entityType":"list"},{"key":"申请类型","value":"${applyType}","entityType":"list"},{"key":"面积需求","value":"${areaSize} 平米","entityType":"list"},{"key":"申请来源","value":"${sourceType}","entityType":"list"},{"key":"备注","value":"${description}","entityType":"multi_line"}]', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '1', 'zh_CN', '入驻申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '2', 'zh_CN', '扩租申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '3', 'zh_CN', '续租申请');

--
-- 工作流设置菜单
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40130, '工作流设置', 40100, NULL, 'react:/working-flow/flow-list/rent-manage/40100', 0, 2, '/40000/40100/40130', 'park', 419);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40130, '', 'EhNamespaces', 1000000, 2);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10020, 40130, '招租管理', 1, 1, '招租管理 工作流设置 全部权限', 419);

-- scripts support, by Janson 20161221
SET @id := (SELECT MAX(id) FROM eh_flow_scripts);
INSERT INTO `eh_flow_scripts`(`id`,`namespace_id`,`owner_id`,`owner_type`,`module_id`,`module_type`,`name`,`script_type`,`script_cls`,`flow_step_type`,`step_type`)
VALUES ((@id := @id+1), '0', '11', 'ENTERPRISE', '111', 'any-module', 'test-dummpy', 'prototype', 'com.everhomes.flow.FlowScriptFireDummy', 'approve_step', 'step_enter'
);

--
-- 修改没有权限时的提示语  add by xq.tian  2016/12/21
--
UPDATE `eh_locale_strings` SET `text`='对不起,您没有权限执行此操作' WHERE (`scope`='general' AND `code`='505');

--- 短信测试 Janson 20161223
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow:40800', 10001, 'zh_CN', '您的验证码是${vcode}', '验证码短信测试');

-- Janson 20161223
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10001, 'zh_CN', '您的验证码是${vcode} 1', '验证码短信测试1');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10002, 'zh_CN', '您的验证码是${vcode} 2', '验证码短信测试2');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10003, 'zh_CN', '您的验证码是${vcode} 3', '验证码短信测试3');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10004, 'zh_CN', '您的验证码是${vcode} 4', '验证码短信测试4');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10005, 'zh_CN', '您的验证码是${vcode} 5', '验证码短信测试5');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10006, 'zh_CN', '您的验证码是${vcode} 6', '验证码短信测试6');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10007, 'zh_CN', '您的验证码是${vcode} 7', '验证码短信测试7');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10008, 'zh_CN', '您的验证码是${vcode} 8', '验证码短信测试8');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10009, 'zh_CN', '您的验证码是${vcode} 9', '验证码短信测试9');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10010, 'zh_CN', '您的验证码是${vcode} 10', '验证码短信测10');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10011, 'zh_CN', '您的验证码是${vcode} 11', '验证码短信测试11');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10012, 'zh_CN', '您的验证码是${vcode} 12', '验证码短信测试12');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10013, 'zh_CN', '您的验证码是${vcode} 13', '验证码短信测试13');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10014, 'zh_CN', '您的验证码是${vcode} 14', '验证码短信测试14');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10015, 'zh_CN', '您的验证码是${vcode} 15', '验证码短信测试15');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10016, 'zh_CN', '您的验证码是${vcode} 16', '验证码短信测试16');

SET @id := (SELECT MAX(id) FROM eh_flow_scripts);
INSERT INTO `eh_flow_scripts`(`id`,`namespace_id`,`owner_id`,`owner_type`,`module_id`,`module_type`,`name`,`script_type`,`script_cls`,`flow_step_type`,`step_type`)
VALUES ((@id := @id+1), '1000000', '1000001', 'PARKING', '40800', 'any-module', 'test-dummpy', 'prototype', 'com.everhomes.flow.FlowScriptFireDummy', 'approve_step', 'step_enter'
);

-- sms tests
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10002, 'zh_CN', '您的验证码是${vcode} 2', '验证码短信测试2');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10003, 'zh_CN', '您的验证码是${vcode} 3', '验证码短信测试3');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10004, 'zh_CN', '您的验证码是${vcode} 4', '验证码短信测试4');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10005, 'zh_CN', '您的验证码是${vcode} 5', '验证码短信测试5');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10006, 'zh_CN', '您的验证码是${vcode} 6', '验证码短信测试6');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10007, 'zh_CN', '您的验证码是${vcode} 7', '验证码短信测试7');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10008, 'zh_CN', '您的验证码是${vcode} 8', '验证码短信测试8');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10009, 'zh_CN', '您的验证码是${vcode} 9', '验证码短信测试9');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10010, 'zh_CN', '您的验证码是${vcode} 10', '验证码短信测10');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10011, 'zh_CN', '您的验证码是${vcode} 11', '验证码短信测试11');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10012, 'zh_CN', '您的验证码是${vcode} 12', '验证码短信测试12');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10013, 'zh_CN', '您的验证码是${vcode} 13', '验证码短信测试13');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10014, 'zh_CN', '您的验证码是${vcode} 14', '验证码短信测试14');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10015, 'zh_CN', '您的验证码是${vcode} 15', '验证码短信测试15');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES
(1000000, 'flow:40800', 10016, 'zh_CN', '您的验证码是${vcode} 16', '验证码短信测试16');
