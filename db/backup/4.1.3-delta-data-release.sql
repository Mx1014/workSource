-- merge from  activity-2.0.0-delta-data-release.sql by lqs 20170119
-- 初始化数据，把官方活动的category_id全部配成1, add by tt, 20170116
update eh_activities set category_id = 1 where official_flag = 1;
update eh_forum_posts set category_id = 1 where official_flag = 1;

-- 配置华润社群layout，add by tt, 20170120
select max(id) into @id from `eh_launch_pad_layouts`;
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES (@id:=@id+1, 999985, 'AssociationLayout', '{"versionCode":"2017012001","versionName":"4.1.2","layoutName":"AssociationLayout","displayName":"OE活动","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', 2017012001, 0, 2, now(), 'park_tourist', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES (@id:=@id+1, 999985, 'AssociationLayout', '{"versionCode":"2017012001","versionName":"4.1.2","layoutName":"AssociationLayout","displayName":"OE活动","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', 2017012001, 0, 2, now(), 'pm_admin', 0, 0, 0);

-- 配置社群item，add by tt, 20170120
select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '白领活动', '白领活动', '', '', 1, 1, 61, '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":4}', 1, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '企业定制', '企业定制', '', '', 1, 1, 33, '{"type":200878,"parentId":200878,"displayType": "icontab"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', 'OE社团', 'OE社团', '', '', 1, 1, 36, '{"privateFlag": 0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '白领活动', '白领活动', '', '', 1, 1, 61, '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":4}', 1, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '企业定制', '企业定制', '', '', 1, 1, 33, '{"type":200878,"parentId":200878,"displayType": "icontab"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', 'OE社团', 'OE社团', '', '', 1, 1, 36, '{"privateFlag": 0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL);

-- 配置华润活动主题分类，add by tt, 20170120
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1001, '', 0, 1, '精英汇', '/1/1001', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvMU56bGpNMk5oTldSaU5tTXlOR0UwTkdaaE56TmlNMlk0WmpVeE9EUmxNdw', 'cs://1/image/aW1hZ2UvTVRvek1qWmpPV0V6TURjMU9EQmxZakk0TXpZd09HUmpaV0pqTlRZNFlXVmpPUQ', NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1002, '', 0, 1, '品质汇', '/1/1002', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvNE9USXpOek0wWWprNU9ERXdOak0zTVdKak9HUTVPREUwWkRjeU9XUmhNQQ', 'cs://1/image/aW1hZ2UvTVRvNU9EZzRPR1l6TlRJellUVmtPR0kyTmpVME5XUTFaVEptWkRBNVpUbGtOZw', NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1003, '', 0, 1, '艺术汇', '/1/1003', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvd09UZGlZMlEzWVRkbVlUQmlPVEUyWmpreU1qazRZVEJpWmpka1ptTmhNdw', 'cs://1/image/aW1hZ2UvTVRwbU5tVmhOR0l3TUdabE1EQTROMk5qT1RjeFpqUmxPRFk1WWpBd1lUUTBZZw', NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1004, '', 0, 1, 'OE大讲堂', '/1/1004', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, 0);

-- 华润初始化数据，add by tt, 20170120
update eh_activities set content_category_id = 1004 where subject like '%金融讲座第二波来袭，带你读懂SDR%' and namespace_id=999985;
update eh_activities set content_category_id = 1004 where subject like '%来这里，教你有效资产配置，抑制钱包缩水%' and namespace_id=999985;
update eh_activities set content_category_id = 1004 where subject like '%华润前海大厦携手吴伯凡，为前海金融创新再发声%' and namespace_id=999985;
update eh_activities set content_category_id = 1004 where subject like '%Officeasy昨晚盛大发布 《最后14堂星期二的课》深圳首演%' and namespace_id=999985;
update eh_activities set content_category_id = 1004 where subject like '%Prodigy Network与您探讨地产众筹新模式%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%全球第一支OL芭蕾舞团，成员竟然是你的女上司%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%初级班二期，10月19日正式开课%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%今年中秋，你想和谁一起过%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%初级班二期课程安排新鲜出炉%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%如果拥有第二人生，你会选择成为音乐人吗？%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%2个月，10个人，如何从零基础到参与芭蕾舞表演？%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%芭蕾男老师，帅出新高度！第一期芭蕾学员招募中%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%优雅的气质，也是一种实力%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%吉他民谣，音乐家招募中！是谁来拨动琴弦？%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%Officeasy午间音乐会%' and namespace_id=999985;
update eh_activities set content_category_id = 1003 where subject like '%青春不散场%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%井柏然、阮经天最爱的MINI%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%不给糖就捣乱！万圣大片演员试镜招募%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%让500人荧光趴帮你驱散节后阴霾%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%“最美”半马“春茧”开跑，华润置地与你一起感受美好生活%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%500人荧光大趴体，点亮深圳湾7公里海岸线%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%组团刷电影副本%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%办公室里动起来，hold住你的最佳状态%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%报名挑战你的极限%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%现在，我想和你一起做件%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%开年垂直马拉松即将开跑%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%Officeasy圈层社交活动拉开序幕%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%全城热跑——给父母99km的爱%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%与TA心有灵"夕" 一起赢大奖%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%Officeasy魔方速拧大赛报名开始啦！%' and namespace_id=999985;
update eh_activities set content_category_id = 1001 where subject like '%Officeasy深圳华润大厦租户摄影大赛%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%东阿阿胶73折，留住父母的年轻状态%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%十一大餐轰炸之后，来点清新的%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%不去巴黎时装周，也能看秋冬时装秀%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%有些事现在不做，这辈子都不会做了%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%说不出口的情话，不如写下来，然后@TA%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%剁手预警！ 一大波5折MK包包，比海淘更划算%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%没吃过香港半岛酒店月饼，还好意思说自己是资深吃货？%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%@您一封家书：爸，别担心，我在深圳过得很好%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%每天专车接送上下班是一种怎样的体验？%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%5.28陪孩子去看海%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%有多久，没对她说“我爱你”？%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%今天中午OE君告诉你午餐吃什么%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%在这万物生长的春日里，把咖啡搬进你的办公室%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%最in的Office Lady彩妆沙龙来了，不要错过咯~%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%你的家乡是什么味道？%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%新钞&纪念币兑换%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%限时福利！免费加班餐现在派送，戳我领取！%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%Officeasy中秋油画体验%' and namespace_id=999985;
update eh_activities set content_category_id = 1002 where subject like '%快乐工作，感谢有你们。Officeasy团队照拍摄开始啦！%' and namespace_id=999985;

-- 更新帖子中的活动主题分类，add by tt, 20170120
update eh_forum_posts t1 set activity_content_category_id = (select content_category_id from eh_activities t2 where t1.embedded_id=t2.id and t2.namespace_id=999985)
where t1.embedded_app_id=3 and exists (
	select 1
	from eh_activities t3
	where t1.embedded_id=t3.id and t3.namespace_id=999985
);

-- 修改华润后台活动菜单，add by tt, 20170120
delete from eh_web_menu_scopes where owner_type = 'EhNamespaces' and owner_id = '999985' and menu_id in (10610, 10620, 10700);
update eh_web_menu_scopes set menu_name = '白领活动', apply_policy = 1 where owner_type = 'EhNamespaces' and owner_id = '999985' and menu_id = 10600;

-- 设置华润活动列表的默认样式，add by tt, 20170120
select max(id) into @id from eh_configurations;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id+1, 'activity.default.list.style', '2', 'officeasy default activity list style', 999985, NULL);




-- merge from equipment2.1-delta-data-release.sql by lqs 20170119
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('1', '999992', 'PM', '1000750', '0', '设备', '/设备', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('2', '999992', 'PM', '1000750', '0', '装修', '/装修', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('3', '999992', 'PM', '1000750', '0', '空置房', '/空置房', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('4', '999992', 'PM', '1000750', '0', '安保', '/安保', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('5', '999992', 'PM', '1000750', '0', '日常工作检查', '/日常工作检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('6', '999992', 'PM', '1000750', '0', '公共设施检查', '/公共设施检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('7', '999992', 'PM', '1000750', '0', '周末值班', '/周末值班', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('8', '999992', 'PM', '1000750', '0', '安全检查', '/安全检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('9', '999992', 'PM', '1000750', '0', '其他', '/其他', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);


update eh_equipment_inspection_equipments set inspection_category_id = 1;
update eh_equipment_inspection_standards set inspection_category_id = 1;
update eh_equipment_inspection_tasks set inspection_category_id = 1;






-- merge from authorization-delta-data-release.sql by lqs 20170120
--
-- 客户资料管理细分权限 add by xq.tian  2017/01/16
--
SELECT max(id) FROM `eh_service_module_privileges` INTO @service_module_pri_id;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10127, '0', '添加客户', '客户资料细分权限 添加客户', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10127, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10128, '0', '批量导入及下载模版', '客户资料细分权限 批量导入及下载模版', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10128, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10129, '0', '导出EXCEL', '客户资料细分权限 导出EXCEL', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10129, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10130, '0', '查看统计信息', '客户资料细分权限 查看统计信息', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10130, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10131, '0', '修改客户资料', '客户资料细分权限 修改客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10131, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10132, '0', '查看客户资料', '客户资料细分权限 查看客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10132, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10133, '0', '管理客户资料', '客户资料细分权限 管理客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10133, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10134, '0', '删除客户资料', '客户资料细分权限 删除客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10134, NULL, '0', NOW());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10127, 37000, '添加客户', 1, 1, '添加客户 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10128, 37000, '批量导入及下载模版', 1, 1, '批量导入及下载模版 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10129, 37000, '导出EXCEL', 1, 1, '导出EXCEL 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10130, 37000, '查看统计信息', 1, 1, '查看统计信息 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10131, 37000, '修改客户资料', 1, 1, '修改客户资料 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10132, 37000, '查看客户资料', 1, 1, '查看客户资料 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10133, 37000, '管理客户资料', 1, 1, '管理客户资料 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10134, 37000, '删除客户资料', 1, 1, '删除客户资料 权限', 202);


-- 开放项目管理业务模块 by sfyan 20170119
UPDATE `eh_service_modules` SET `type` = 0 WHERE `id` in (30000, 30500, 31000, 32000, 33000, 34000, 35000, 37000, 38000);


SELECT max(id) FROM `eh_service_module_scopes` INTO @eh_service_module_scopes;
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
select (@eh_service_module_scopes := @eh_service_module_scopes + 1), owner_id, menu_id, '', owner_type, owner_id, NULL, '2' from eh_web_menu_scopes where
menu_id in (select id from eh_service_modules where id in (30000, 30500, 31000, 32000, 33000, 34000, 35000, 37000, 38000));


-- 修改华润OE首页运营配置   add by xq.tian  2017/01/20
UPDATE `eh_launch_pad_items` SET `action_type`='61', `action_data`='{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":4,"title": "白领活动"}' WHERE `namespace_id`='999985' AND `item_group`='OPPushActivity';

UPDATE `eh_launch_pad_items` SET `item_label`='OE优选',`action_type`=13, `action_data`='{\"url\":\"https://www.zuolin.com/mobile/static/coming_soon/index.html\"}' WHERE `namespace_id`='999985' AND `item_group`='OPPushBiz';

DELETE FROM `eh_business_promotions`;
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ('1', '999985', '释迦 4个/盒', '释迦 4个/盒', 'cs://1/image/aW1hZ2UvTVRwalpHVmlObU5oTWpCbFl6STVZakl6T1dabFlXUTFaak5pTjJSaU1EVTBZZw', '98', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fgood%2Fdetails%2F14780789803129873945%2F3632%2F1%3F_k%3Dzlbiz#sign_suffix', '6', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ('2', '999985', '蓝莓 4盒/份', '蓝莓 4盒/份', 'cs://1/image/aW1hZ2UvTVRveVptVmxNR1V6TWpVd00yRXhNV0psTVRjMFlXTmhZbU0wTldZM01qQmhOZw', '60', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fgood%2Fdetails%2F14780789803129873945%2F3631%2F1%3F_k%3Dzlbiz#sign_suffix', '5', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ('3', '999985', '金艳黄心猕猴桃 25-27个/箱', '金艳黄心猕猴桃 25-27个/箱', 'cs://1/image/aW1hZ2UvTVRwaE9HUTNaamhtTmpVMlpETTVNV1V6TUdNMlkyUmxOek5pWTJFeVl6aG1aQQ', '55', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fgood%2Fdetails%2F14780789803129873945%2F3630%2F1%3F_k%3Dzlbiz#sign_suffix', '4', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ('4', '999985', '牛油果 8个/盒', '牛油果 8个/盒', 'cs://1/image/aW1hZ2UvTVRvek9URmhNak01TTJKaE1XWXdNVEUxT1dNeVlUVTBOek5tTVROak9EZGpOUQ', '68', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fgood%2Fdetails%2F14780789803129873945%2F3608%2F1%3F_k%3Dzlbiz#sign_suffix', '3', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ('5', '999985', '减脂训练营', '减脂训练营', 'cs://1/image/aW1hZ2UvTVRwa09HUTVNVEF5TmpWbE5tUXlPVEEyTmpjd05qTXhaVFJoWW1JNVlUQXdZZw', '2899', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fgood%2Fdetails%2F14797145180329304737%2F4188%2F1%3F_k%3Dzlbiz#sign_suffix', '2', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ('6', '999985', 'Boxing训练营', 'Boxing训练营', 'cs://1/image/aW1hZ2UvTVRvNE9ESmpOV1JqTkRCaU5UZ3dOak5tTVRabFptTTFZVFV6T0RjNE16a3lOUQ', '899', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fgood%2Fdetails%2F14797145180329304737%2F4187%2F1%3F_k%3Dzlbiz#sign_suffix', '1', '2017-01-10 16:20:23', '1', NULL, NULL);


-- 按华润要求更新服务市场 by lqs 20170120
-- 去掉“白领社团”、“OE大讲堂”、“OE微商场”这一整栏，公告栏下分隔条改为一条线
DELETE FROM `eh_launch_pad_layouts` WHERE `id` IN (417, 418) AND `namespace_id`=999985;
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type)
	VALUES (417, 999985, 'ServiceMarketLayout', '{"versionCode":"2017012003","versionName":"4.1.2","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":2},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","entityCount": 3,"subjectHeight": 1,"descriptionHeight": 0},"style": "ListView","defaultOrder": 6,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 1},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushBiz","entityCount": 6,"subjectHeight": 1,"descriptionHeight": 0},"style": "HorizontalScrollView","defaultOrder": 7,"separatorFlag": 1,"separatorHeight": 0,"columnCount": 0}]}', '2017012003', '0', '2', '2017-1-20 14:02:30', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type)
	VALUES (418, 999985, 'ServiceMarketLayout', '{"versionCode":"2017012003","versionName":"4.1.2","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":2},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","entityCount": 3,"subjectHeight": 1,"descriptionHeight": 0},"style": "ListView","defaultOrder": 6,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 1},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushBiz","entityCount": 6,"subjectHeight": 1,"descriptionHeight": 0},"style": "HorizontalScrollView","defaultOrder": 7,"separatorFlag": 1,"separatorHeight": 0,"columnCount": 0}]}', '2017012003', '0', '2', '2017-1-20 14:02:30', 'park_tourist');
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112573, 112584) AND `namespace_id`=999985; -- 白领社团
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112574, 112585) AND `namespace_id`=999985; -- OE大讲堂
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112575, 112586) AND `namespace_id`=999985; -- OE微商城
-- 重新调整服务item顺序、更换icon、先暂时去掉'项目介绍'和'服务热线'
-- 园区管理员场景
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112576, 112577, 112578, 112579, 112580, 112581, 112582, 112701) AND `namespace_id`=999985;
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	VALUES (112578, 999985, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '考勤打卡', 'cs://1/image/aW1hZ2UvTVRvME56UmpOamRsWVdKaFl6TmlNV00zWkdSak5UbGtaREEzWkRFd1lUVXpNdw', '1', '1', '23', '', 10, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112579, 999985, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', 'cs://1/image/aW1hZ2UvTVRvM01HVTVaamsyTjJRMk16azNaVE5pWlROa016aGhNMk5pWW1ZM1pEZzVOZw', '1', '1', '46', '', 20, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112576, 999985, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwaU5UVmpOamt6TnpFNVlUY3dZek0zTlRNM1lUbG1OVFkxTjJaaE1XUTVOZw', '1', '1', '27', '', 30, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112577, 999985, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRwaVlqZzJOV1F3Wm1KbU1EVXpZMlF6TldKalpUWm1PV00zWXpSbU5qVTRaZw', 1, 1, 49,'{\"resourceTypeId\":10096,\"pageType\":0}', 40, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'pm_admin');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
-- 	VALUES (112580, 999985, '0', '0', '0', '/home', 'Bizs', '项目介绍', '项目介绍', 'cs://1/image/aW1hZ2UvTVRvd056SmtNakE1WWpZNVltVmhPRGN6TlRGaFpEQTBOVEF3WTJZM09UVXdaUQ', '1', '1', '28', '', 50, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
-- 	VALUES (112581, 999985, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvelpXRTFOR1U0T1Raa016UTVOV0l3TkRWak5USTBPVGMzTldNelltUmtZZw', '1', '1', '45', '', 60, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112582, 999985, 0, 0, 0, '/home', 'Bizs', '企办汇', '企办汇', 'cs://1/image/aW1hZ2UvTVRveFlqQTNPVGt4WlRVeVpqa3dZamN5WWpJMlpETTJNVGt4TURWbU1XRXlOUQ', 1, 1, 33, '{"type":150,"parentId":150,"displayType": "grid"}', 70, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112701, 999985, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvellqQTFaakkyWm1Rd09XTTJZbVJrTURSa1pEZzRZekJpWXpZek5tWm1Ndw', 1, 1, 1, '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', 999, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');
-- 园区游客场景
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112587, 112588, 112589, 112590, 112591, 112592, 112593, 112702) AND `namespace_id`=999985;
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	VALUES (112589, 999985, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '考勤打卡', 'cs://1/image/aW1hZ2UvTVRvME56UmpOamRsWVdKaFl6TmlNV00zWkdSak5UbGtaREEzWkRFd1lUVXpNdw', '1', '1', '23', '', 10, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112590, 999985, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', 'cs://1/image/aW1hZ2UvTVRvM01HVTVaamsyTjJRMk16azNaVE5pWlROa016aGhNMk5pWW1ZM1pEZzVOZw', '1', '1', '46', '', 20, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112587, 999985, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwaU5UVmpOamt6TnpFNVlUY3dZek0zTlRNM1lUbG1OVFkxTjJaaE1XUTVOZw', '1', '1', '27', '', 30, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112588, 999985, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRwaVlqZzJOV1F3Wm1KbU1EVXpZMlF6TldKalpUWm1PV00zWXpSbU5qVTRaZw', 1, 1, 49,'{\"resourceTypeId\":10096,\"pageType\":0}', 40, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'park_tourist');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
-- 	VALUES (112591, 999985, '0', '0', '0', '/home', 'Bizs', '项目介绍', '项目介绍', 'cs://1/image/aW1hZ2UvTVRvd056SmtNakE1WWpZNVltVmhPRGN6TlRGaFpEQTBOVEF3WTJZM09UVXdaUQ', '1', '1', '28', '', 50, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
-- 	VALUES (112592, 999985, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvelpXRTFOR1U0T1Raa016UTVOV0l3TkRWak5USTBPVGMzTldNelltUmtZZw', '1', '1', '45', '', 60, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112593, 999985, 0, 0, 0, '/home', 'Bizs', '企办汇', '企办汇', 'cs://1/image/aW1hZ2UvTVRveFlqQTNPVGt4WlRVeVpqa3dZamN5WWpJMlpETTJNVGt4TURWbU1XRXlOUQ', 1, 1, 33, '{"type":150,"parentId":150,"displayType": "grid"}', 70, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112702, 999985, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvellqQTFaakkyWm1Rd09XTTJZbVJrTURSa1pEZzRZekJpWXpZek5tWm1Ndw', 1, 1, 1, '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', 999, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');


-- 设备巡检名称修改 by sfyan 20170120
UPDATE `eh_web_menus` SET `name` = '物业巡检' WHERE `id` = 20800;
UPDATE `eh_web_menus` SET `data_type` = 'equipment--equipment_inspection_standard_list' WHERE `id` = 20811;
UPDATE `eh_web_menus` SET `name` = '巡检关联审批', `data_type` = 'equipment--equipment_inspection_check_attachment'  WHERE `id` = 20812;
UPDATE `eh_web_menus` SET `data_type` = 'equipment--equipment_inspection_sparepart_list' WHERE `id` = 20822;
UPDATE `eh_web_menus` SET `name` = '巡检台账' WHERE `id` = 20820;
UPDATE `eh_web_menus` SET `name` = '巡检对象', `data_type` = 'equipment--equipment_inspection_equipment_list' WHERE `id` = 20821;
UPDATE `eh_web_menus` SET `data_type` = 'equipment--equipment_inspection_task_list' WHERE `id` = 20831;
UPDATE `eh_web_menus` SET `data_type` = 'equipment--equipment_inspection_inspection_item_list' WHERE `id` = 20841;

-- 应用统计的菜单配置给华润OE by sfyan 20170120
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41300, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40750, '', 'EhNamespaces', 999985, 2);


-- 设备巡检配置统计菜单 by xiongying20170122
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20850', '统计', '20800', NULL, 'react:/equipment-inspection/statistics', '1', '2', '/20000/20800/20850', 'park', '300', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10011,20850,'设备巡检',1,1,'设备巡检 管理员权限',710);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20850, '', 'EhNamespaces', 999992, 2);


-- 设备巡检设备数据迁移 由关联部门改为关联小区 by xiongying20170124
update eh_equipment_inspection_equipments set target_type = 'community';
update eh_equipment_inspection_equipments set target_id = 240111044331051304 where target_id = 1003859;
update eh_equipment_inspection_equipments set target_id = 240111044331051302 where target_id = 1008875;

-- 更新菜单 add by sw 20170207
update eh_web_menus set data_type = 'access_manage/inside' where data_type = 'access_manage_inside' and name = '门禁管理';
update eh_web_menus set data_type = 'version_manage/inside' where data_type = 'version_manage_inside' and name = '版本管理';
update eh_web_menus set data_type = 'access_group/inside' where data_type = 'access_group_inside' and name = '门禁分组';
update eh_web_menus set data_type = 'user_auth/inside' where data_type = 'user_auth_inside' and name = '用户授权';
update eh_web_menus set data_type = 'visitor_auth/inside' where data_type = 'visitor_auth_inside' and name = '访客授权';
update eh_web_menus set data_type = 'access_log/inside' where data_type = 'access_log_inside' and name = '门禁日志';

update eh_web_menus set data_type = 'forum_activity/road' where data_type = 'road_show' and name = '路演表演';
update eh_web_menus set data_type = 'forum_activity/whiteCollar' where data_type = 'white_collar_activity' and name = '白领活动';
update eh_web_menus set data_type = 'forum_activity/OE' where data_type = 'OE_auditorium' and name = 'OE大讲堂';
update eh_web_menus set data_type = 'news_management/industry' where data_type = 'industry_dynamics' and name = '行业动态';

update eh_web_menus set data_type = 'task/all' where data_type = 'all_task' and name = '全部任务';
update eh_web_menus set data_type = 'task/my' where data_type = 'my_task' and name = '我的任务';


-- 设备巡检对象类型增加 by xiongying20170213
SET @categories_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '空置房', '设备类型/空置房', '0', '2', NOW(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '装修', '设备类型/装修', '0', '2', NOW(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '安保', '设备类型/安保', '0', '2', NOW(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '日常工作检查', '设备类型/日常工作检查', '0', '2', NOW(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '公共设施检查', '设备类型/公共设施检查', '0', '2', NOW(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '周末值班', '设备类型/周末值班', '0', '2', NOW(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@categories_id := @categories_id + 1), '7', '0', '安全检查', '设备类型/安全检查', '0', '2', NOW(), NULL, NULL, NULL, '0');

-- 添加健身房 add by sw 20170217
SET @eh_launch_pad_items = (SELECT max(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999986', '0', '0', '0', '/home', 'Bizs', '健身房', '健身房', 'cs://1/image/aW1hZ2UvTVRvek5UUTBObU0wT0dReU1tWmhNbVZpTTJNMU1UVTVNRGN4WVRobE5qbGtPUQ', '1', '1', '14', '{"url":"http://core.zuolin.com/mobile/static/banner/jsf.html"}', '40', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999986', '0', '0', '0', '/home', 'Bizs', '健身房', '健身房', 'cs://1/image/aW1hZ2UvTVRvek5UUTBObU0wT0dReU1tWmhNbVZpTTJNMU1UVTVNRGN4WVRobE5qbGtPUQ', '1', '1', '14', '{"url":"http://core.zuolin.com/mobile/static/banner/jsf.html"}', '40', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL);

-- 更新现网时间周期timeRange为负bug by xiongying20170217
update eh_repeat_settings set time_ranges = '{"ranges":[{"startTime":"06:00:00","endTime":"05:45:00","duration":"1425m"}]}' where id = 11;
update eh_repeat_settings set time_ranges = '{"ranges":[{"startTime":"07:00:00","endTime":"11:00:00","duration":"240m"},{"startTime":"11:05:00","endTime":"15:00:00","duration":"235m"},{"startTime":"15:05:00","endTime":"19:00:00","duration":"235m"},{"startTime":"19:05:00","endTime":"23:00:00","duration":"235m"},{"startTime":"23:05:00","endTime":"03:00:00","duration":"235m"},{"startTime":"03:05:00","endTime":"06:58:00","duration":"233m"}]}' where id = 194;

-- 添加服务广场健身房 add by sw 20170217
SET @eh_launch_pad_items = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999986', '0', '0', '0', '/home', 'Bizs', '健身房', '健身房', 'cs://1/image/aW1hZ2UvTVRvek5UUTBObU0wT0dReU1tWmhNbVZpTTJNMU1UVTVNRGN4WVRobE5qbGtPUQ', '1', '1', '14', '{"url":"http://core.zuolin.com/mobile/static/banner/jsf.html"}', '40', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999986', '0', '0', '0', '/home', 'Bizs', '健身房', '健身房', 'cs://1/image/aW1hZ2UvTVRvek5UUTBObU0wT0dReU1tWmhNbVZpTTJNMU1UVTVNRGN4WVRobE5qbGtPUQ', '1', '1', '14', '{"url":"http://core.zuolin.com/mobile/static/banner/jsf.html"}', '40', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL);

-- 滚动广告增加行数高度配置	by sfyan 20170120
UPDATE `eh_launch_pad_layouts` SET `layout_json` = REPLACE(`layout_json`,'"widget":"Bulletins","instanceConfig":{"itemGroup":""}', '"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}');
UPDATE `eh_launch_pad_layouts` SET `layout_json` = REPLACE(`layout_json`,'"widget":"Bulletins","instanceConfig":{"itemGroup":"Default"}', '"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}');
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2016100901"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2017012003"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2017011302"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2016081701"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2015082914"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2016100901"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2016110101"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2016121201"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';
UPDATE `eh_launch_pad_layouts` SET `version_code` = 2017022001, `layout_json` = replace(`layout_json`,'"versionCode":"2016121601"','"versionCode":"2017022001"') WHERE `layout_json` LIKE '%"widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2}%';

-- 更新线网深业短信模版 add by sw 20170221
update eh_locale_templates set text = 29478 where code = 10 and namespace_id = 999992 and scope = 'sms.default.yzx';
update eh_locale_templates set text = 29479 where code = 11 and namespace_id = 999992 and scope = 'sms.default.yzx';

-- 园区配套 item配置修改 add by sfyan 20170221
SET @eh_service_alliance_skip_rule = (SELECT max(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999986', '60');
UPDATE eh_launch_pad_items SET action_data = REPLACE(action_data,'"displayType": "grid"}','"displayType": "list"}') WHERE namespace_id = 999986 AND item_label = '园区配套';

-- 华润OE 图书馆 item配置修改 add by sfyan 20170222
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) values((@item_id := @item_id + 1),'999985','0','0','0','/association','TabGroup','OE图书馆','OE图书馆','','1','1','2','{"itemLocation":"/association/ad","layoutName":"libraryLayout"}','4','0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','1',NULL,'');

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) values((@item_id := @item_id + 1),'999985','0','0','0','/association','TabGroup','OE图书馆','OE图书馆','','1','1','2','{"itemLocation":"/association/ad","layoutName":"libraryLayout"}','4','0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','1',NULL,'');

SET @layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);

insert into `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) values((@layout_id := @layout_id + 1),'999985','libraryLayout','{\"versionCode\":\"2017022201\",\"versionName\":\"4.1.3\",\"layoutName\":\"libraryLayout\",\"displayName\":\"图书馆\",\"groups\":[{\"groupName\":\"图书馆Banner\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"libraryBanner\"},\"style\":\"Gallery\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0,\"columnCount\": 1,\"editFlag\":0}]}','2017022201','0','2',now(),'pm_admin','0','0','0');
insert into `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) values((@layout_id := @layout_id + 1),'999985','libraryLayout','{\"versionCode\":\"2017022201\",\"versionName\":\"4.1.3\",\"layoutName\":\"libraryLayout\",\"displayName\":\"图书馆\",\"groups\":[{\"groupName\":\"图书馆Banner\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"libraryBanner\"},\"style\":\"Gallery\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0,\"columnCount\": 1,\"editFlag\":0}]}','2017022201','0','2',now(),'park_tourist','0','0','0');

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) values((@item_id := @item_id + 1),'999985','0','0','0','/association/ad','libraryBanner','','','cs://1/image/aW1hZ2UvTVRvellUVmpOVGcxWkdabU1UUTFaREU0WmpaaFlqbGxaVGMxT0RBMVpqQXpNUQ','1','1','0',NULL,'1','0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','0',NULL,NULL);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) values((@item_id := @item_id + 1),'999985','0','0','0','/association/ad','libraryBanner','','','cs://1/image/aW1hZ2UvTVRvellUVmpOVGcxWkdabU1UUTFaREU0WmpaaFlqbGxaVGMxT0RBMVpqQXpNUQ','1','1','0',NULL,'1','0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','0',NULL,NULL);

-- 科兴 服务广场的item不准删除和添加 add by sfyan 20170222
UPDATE `eh_launch_pad_items` SET `delete_flag` = 0 WHERE `item_group` = 'Bizs' AND `item_location` = '/home' AND `namespace_id` = 999983;

-- 科兴 服务广场item的排序 add by sfyan 20170222
update eh_launch_pad_items set `default_order` = 1 where item_label = "物业查费" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 2 where item_label = "报修" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 3 where item_label = "停车" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 4 where item_label = "通知" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 5 where item_label = "服务热线" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 7 where item_label = "办事指南" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 6, `display_flag` = 1 where item_label = "投诉建议" and namespace_id = 999983;
update eh_launch_pad_items set `default_order` = 10000 where item_label = "更多" and namespace_id = 999983;

-- 科兴 服务广场增加item 任务管理 add by sfyan 20170222
SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES ((@item_id := @item_id + 1), '999983', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwaU1tVTJNbUV4Wm1Jd05HRTBZV1F4T0Roa09HUXhNMkUwTldReFpHVXpOUQ', '1', '1', '56', '', '8', '0', '1', '0', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);

-- added by janson 20170322
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'aclink.user_key_timeout', '3600', 'timeout in second for qr');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.user_key_timeout', '1800', 'timeout in second for qr');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999982, 'aclink.user_key_timeout', '1800', 'timeout in second for qr qinghuatianan');


-- 中洲智邦和UFine的缴费layout配置 add by sfyan 20170223
SET @layout_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);
insert into `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) values((@layout_id := @layout_id + 1),'999990','PaymentLayout','{\"versionCode\":\"2017022301\",\"versionName\":\"3.0.0\",\"layoutName\":\"PaymentLayout\",\"displayName\":\"缴费首页\",\"groups\":[{\"groupName\":\"pay\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"PayActions\"},\"style\":\"Light\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0,\"columnCount\":3}]}','2017022301','0','2',UTC_TIMESTAMP(),'pm_admin','0','0','0');

insert into `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) values((@layout_id := @layout_id + 1),'999999','PaymentLayout','{\"versionCode\":\"2017022301\",\"versionName\":\"3.0.0\",\"layoutName\":\"PaymentLayout\",\"displayName\":\"缴费首页\",\"groups\":[{\"groupName\":\"pay\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"PayActions\"},\"style\":\"Light\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0,\"columnCount\":3}]}','2017022301','0','2',UTC_TIMESTAMP(),'pm_admin','0','0','0');

insert into `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) values((@layout_id := @layout_id + 1),'999990','PaymentLayout','{\"versionCode\":\"2017022301\",\"versionName\":\"3.0.0\",\"layoutName\":\"PaymentLayout\",\"displayName\":\"缴费首页\",\"groups\":[{\"groupName\":\"pay\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"PayActions\"},\"style\":\"Light\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0,\"columnCount\":3}]}','2017022301','0','2',UTC_TIMESTAMP(),'park_tourist','0','0','0');

insert into `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) values((@layout_id := @layout_id + 1),'999999','PaymentLayout','{\"versionCode\":\"2017022301\",\"versionName\":\"3.0.0\",\"layoutName\":\"PaymentLayout\",\"displayName\":\"缴费首页\",\"groups\":[{\"groupName\":\"pay\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"PayActions\"},\"style\":\"Light\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0,\"columnCount\":3}]}','2017022301','0','2',UTC_TIMESTAMP(),'park_tourist','0','0','0');

SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '物业费', '物业费', 'cs://1/image/aW1hZ2UvTVRwa05EQTJOVGxqTkRJMk56UmtaVEpqTm1ZeFlqazRPR1pqTkRJM01UTmpNQQ', '1', '1', '22', '', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '水费', '水费', 'cs://1/image/aW1hZ2UvTVRvd01XTXhPRGd5TnpZek9HUTFObVE1Wldaa01qQTNORFF6WlRjeU9XTmhZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '电费', '电费', 'cs://1/image/aW1hZ2UvTVRvd09UazFOMlkzWkRObFl6RTJZakEzT1RobU5EY3lZamN6TXpRM01tUTVOUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '燃气费', '燃气费', 'cs://1/image/aW1hZ2UvTVRvNFpqVXhNREl6TmpneE1XVmxZVGhrTnpJd09XVXlaakZqWlRJeE1XUTFNQQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '固话宽带', '固话宽带', 'cs://1/image/aW1hZ2UvTVRwa05UWmxNVGt4TURObE9EbG1Oamd3TVdFMllUSTVOV1kwTlRNMk9HWmtNUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '有线电视', '有线电视', 'cs://1/image/aW1hZ2UvTVRwaU9EWTFObVJpTmpCallUSTFOams1T0RJNFpHWTJNV1E1T1dOaE1UWmtOdw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '物业费', '物业费', 'cs://1/image/aW1hZ2UvTVRwa05EQTJOVGxqTkRJMk56UmtaVEpqTm1ZeFlqazRPR1pqTkRJM01UTmpNQQ', '1', '1', '22', '', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '水费', '水费', 'cs://1/image/aW1hZ2UvTVRvd01XTXhPRGd5TnpZek9HUTFObVE1Wldaa01qQTNORFF6WlRjeU9XTmhZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '电费', '电费', 'cs://1/image/aW1hZ2UvTVRvd09UazFOMlkzWkRObFl6RTJZakEzT1RobU5EY3lZamN6TXpRM01tUTVOUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '燃气费', '燃气费', 'cs://1/image/aW1hZ2UvTVRvNFpqVXhNREl6TmpneE1XVmxZVGhrTnpJd09XVXlaakZqWlRJeE1XUTFNQQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '固话宽带', '固话宽带', 'cs://1/image/aW1hZ2UvTVRwa05UWmxNVGt4TURObE9EbG1Oamd3TVdFMllUSTVOV1kwTlRNMk9HWmtNUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999990, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '有线电视', '有线电视', 'cs://1/image/aW1hZ2UvTVRwaU9EWTFObVJpTmpCallUSTFOams1T0RJNFpHWTJNV1E1T1dOaE1UWmtOdw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
	
	
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '物业费', '物业费', 'cs://1/image/aW1hZ2UvTVRwa05EQTJOVGxqTkRJMk56UmtaVEpqTm1ZeFlqazRPR1pqTkRJM01UTmpNQQ', '1', '1', '22', '', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '水费', '水费', 'cs://1/image/aW1hZ2UvTVRvd01XTXhPRGd5TnpZek9HUTFObVE1Wldaa01qQTNORFF6WlRjeU9XTmhZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '电费', '电费', 'cs://1/image/aW1hZ2UvTVRvd09UazFOMlkzWkRObFl6RTJZakEzT1RobU5EY3lZamN6TXpRM01tUTVOUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '燃气费', '燃气费', 'cs://1/image/aW1hZ2UvTVRvNFpqVXhNREl6TmpneE1XVmxZVGhrTnpJd09XVXlaakZqWlRJeE1XUTFNQQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '固话宽带', '固话宽带', 'cs://1/image/aW1hZ2UvTVRwa05UWmxNVGt4TURObE9EbG1Oamd3TVdFMllUSTVOV1kwTlRNMk9HWmtNUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '有线电视', '有线电视', 'cs://1/image/aW1hZ2UvTVRwaU9EWTFObVJpTmpCallUSTFOams1T0RJNFpHWTJNV1E1T1dOaE1UWmtOdw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '物业费', '物业费', 'cs://1/image/aW1hZ2UvTVRwa05EQTJOVGxqTkRJMk56UmtaVEpqTm1ZeFlqazRPR1pqTkRJM01UTmpNQQ', '1', '1', '22', '', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '水费', '水费', 'cs://1/image/aW1hZ2UvTVRvd01XTXhPRGd5TnpZek9HUTFObVE1Wldaa01qQTNORFF6WlRjeU9XTmhZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '电费', '电费', 'cs://1/image/aW1hZ2UvTVRvd09UazFOMlkzWkRObFl6RTJZakEzT1RobU5EY3lZamN6TXpRM01tUTVOUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '燃气费', '燃气费', 'cs://1/image/aW1hZ2UvTVRvNFpqVXhNREl6TmpneE1XVmxZVGhrTnpJd09XVXlaakZqWlRJeE1XUTFNQQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '固话宽带', '固话宽带', 'cs://1/image/aW1hZ2UvTVRwa05UWmxNVGt4TURObE9EbG1Oamd3TVdFMllUSTVOV1kwTlRNMk9HWmtNUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)  
	VALUES ((@item_id := @item_id + 1), 999999, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '有线电视', '有线电视', 'cs://1/image/aW1hZ2UvTVRwaU9EWTFObVJpTmpCallUSTFOams1T0RJNFpHWTJNV1E1T1dOaE1UWmtOdw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL);
	
-- 华润OE 后台菜单增加物业菜单配置 add sfyan 20170224
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20400,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20410,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20420,'', 'EhNamespaces', 999985,2);

SET @module_scope_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
	VALUES ((@module_scope_id := @module_scope_id + 1), '999985', '20000', '', NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
	VALUES ((@module_scope_id := @module_scope_id + 1), '999985', '20400', '', NULL, NULL, NULL, '2');
	
-- 华润OE 广场配置服务热线 add sfyan 20170224
SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) values((@item_id := @item_id + 1),'999985','0','0','0','/home','Bizs','','服务热线','cs://1/image/aW1hZ2UvTVRwalpXTm1PVEJoWWpSbE0yRTJaVFUwTlRVNFpqVmxORGswT1RSaE1XSm1OUQ','1','1','45','{}','0','0','1','0','1','0',NULL,NULL,NULL,'1','pm_admin','0',NULL,NULL);

-- 科兴 广场配置url修改 add sfyan 20170224
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://m.mafengwo.cn/"}' WHERE namespace_id = 999983 AND item_label = '旅游';
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://m.ziroom.com/"}' WHERE namespace_id = 999983 AND item_label = '租房';
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://m.maizuo.com/"}' WHERE namespace_id = 999983 AND item_label = '电影';
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://m.plateno.com/"}' WHERE namespace_id = 999983 AND item_label = '酒店';
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://m.fitnes.cn/"}' WHERE namespace_id = 999983 AND item_label = '健身';
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://www.dadabus.com/mobile/index.html"}' WHERE namespace_id = 999983 AND item_label = '交通';
UPDATE eh_launch_pad_items SET display_flag = 0 WHERE namespace_id = 999983 AND item_label IN ('理财','医疗','厂房出租','公寓出租');

