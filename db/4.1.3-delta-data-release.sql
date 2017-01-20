-- merge from  activity-2.0.0-delta-data-release.sql by lqs 20170119
-- ��ʼ�����ݣ��ѹٷ����category_idȫ�����1, add by tt, 20170116
update eh_activities set category_id = 1 where official_flag = 1;
update eh_forum_posts set category_id = 1 where official_flag = 1;

-- ���û�����Ⱥlayout��add by tt, 20170120
select max(id) into @id from `eh_launch_pad_layouts`;
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES (@id:=@id+1, 999985, 'AssociationLayout', '{"versionCode":"2017012001","versionName":"4.1.2","layoutName":"AssociationLayout","displayName":"OE�","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', 2017012001, 0, 2, now(), 'park_tourist', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES (@id:=@id+1, 999985, 'AssociationLayout', '{"versionCode":"2017012001","versionName":"4.1.2","layoutName":"AssociationLayout","displayName":"OE�","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', 2017012001, 0, 2, now(), 'pm_admin', 0, 0, 0);

-- ������Ⱥitem��add by tt, 20170120
select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '����', '����', '', '', 1, 1, 61, '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":3}', 1, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '��ҵ����', '��ҵ����', '', '', 1, 1, 33, '{"type":200878,"parentId":200878,"displayType": "icontab"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', 'OE����', 'OE����', '', '', 1, 1, 36, '{"privateFlag": 0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '����', '����', '', '', 1, 1, 61, '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":3}', 1, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', '��ҵ����', '��ҵ����', '', '', 1, 1, 33, '{"type":200878,"parentId":200878,"displayType": "icontab"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `selected_icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/association', 'TabGroup', 'OE����', 'OE����', '', '', 1, 1, 36, '{"privateFlag": 0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL);

-- ���û���������࣬add by tt, 20170120
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1001, '', 0, 1, '��Ӣ��', '/1/1001', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvMU56bGpNMk5oTldSaU5tTXlOR0UwTkdaaE56TmlNMlk0WmpVeE9EUmxNdw', 'cs://1/image/aW1hZ2UvTVRvek1qWmpPV0V6TURjMU9EQmxZakk0TXpZd09HUmpaV0pqTlRZNFlXVmpPUQ', NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1002, '', 0, 1, 'Ʒ�ʻ�', '/1/1002', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvNE9USXpOek0wWWprNU9ERXdOak0zTVdKak9HUTVPREUwWkRjeU9XUmhNQQ', 'cs://1/image/aW1hZ2UvTVRvNU9EZzRPR1l6TlRJellUVmtPR0kyTmpVME5XUTFaVEptWkRBNVpUbGtOZw', NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1003, '', 0, 1, '������', '/1/1003', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvd09UZGlZMlEzWVRkbVlUQmlPVEUyWmpreU1qazRZVEJpWmpka1ptTmhNdw', 'cs://1/image/aW1hZ2UvTVRwbU5tVmhOR0l3TUdabE1EQTROMk5qT1RjeFpqUmxPRFk1WWpBd1lUUTBZZw', NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) VALUES (1004, '', 0, 1, 'OE����', '/1/1004', 0, 2, 1, now(), 0, NULL, 999985, 0, 1, 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, 0);

-- 华润初始化数据，add by tt, 20170120
update eh_activities set content_category_id = 1004 where subject like '%金融讲座第二波，带你读懂SDR%';
update eh_activities set content_category_id = 1004 where subject like '%来这里，教你有效资产配置，抑制钱包缩水%';
update eh_activities set content_category_id = 1004 where subject like '%华润前海大厦携手吴伯凡，为前海金融创新再发声%';
update eh_activities set content_category_id = 1004 where subject like '%Officeasy昨晚盛大发布 《最后14堂星期二的课》深圳首演%';
update eh_activities set content_category_id = 1004 where subject like '%Prodigy Network与您探讨地产众筹新模式%';
update eh_activities set content_category_id = 1003 where subject like '%全球第一支OL芭蕾舞团，成员竟然是你的女上司%';
update eh_activities set content_category_id = 1003 where subject like '%初级班二期，10月19日正式开课%';
update eh_activities set content_category_id = 1003 where subject like '%今年中秋，你想和谁一起过？朗朗、李云迪%';
update eh_activities set content_category_id = 1003 where subject like '%初级班二期课程安排新鲜出炉%';
update eh_activities set content_category_id = 1003 where subject like '%如果拥有第二人生，你会选择成为音乐人吗？%';
update eh_activities set content_category_id = 1003 where subject like '%2个月，10个人，如何从零基础到参与芭蕾舞表演？%';
update eh_activities set content_category_id = 1003 where subject like '%芭蕾男老师，帅出新高度！第一期芭蕾学员招募中%';
update eh_activities set content_category_id = 1003 where subject like '%优雅的气质，也是一种实力%';
update eh_activities set content_category_id = 1003 where subject like '% 吉他民谣，音乐家招募中！是谁来拨动琴弦？%';
update eh_activities set content_category_id = 1003 where subject like '%Officeasy午间音乐会%';
update eh_activities set content_category_id = 1003 where subject like '%白领精音会，青春不散场%';
update eh_activities set content_category_id = 1001 where subject like '%OE车友会—井柏然、阮经天最爱的MINI，免费试驾+拍摄大片%';
update eh_activities set content_category_id = 1001 where subject like '%不给糖就捣乱！万圣大片演员试镜招募%';
update eh_activities set content_category_id = 1001 where subject like '%Easy Run活动流程 让500人荧光趴帮你驱散节后阴霾%';
update eh_activities set content_category_id = 1001 where subject like '%“最美”半马“春茧”开跑，华润置地与你一起感受美好生活%';
update eh_activities set content_category_id = 1001 where subject like '%500人荧光大趴体，点亮深圳湾7公里海岸线%';
update eh_activities set content_category_id = 1001 where subject like '%为了部落 or 联盟，组团刷电影副本！%';
update eh_activities set content_category_id = 1001 where subject like '% 办公室里动起来，hold住你的最佳状态%';
update eh_activities set content_category_id = 1001 where subject like '%第二季平板撑挑战赛来袭，报名挑战你的极限%';
update eh_activities set content_category_id = 1001 where subject like '%现在，我想和你一起做件%';
update eh_activities set content_category_id = 1001 where subject like '% 开年垂直马拉松即将开跑%';
update eh_activities set content_category_id = 1001 where subject like '% Officeasy圈层社交活动拉开序幕%';
update eh_activities set content_category_id = 1001 where subject like '%全城热跑——给父母99km的爱%';
update eh_activities set content_category_id = 1001 where subject like '%与TA心有灵"夕" 一起赢大奖%';
update eh_activities set content_category_id = 1001 where subject like '%Officeasy魔方速拧大赛报名开始啦！%';
update eh_activities set content_category_id = 1001 where subject like '%Officeasy深圳华润大厦租户摄影大赛%';
update eh_activities set content_category_id = 1002 where subject like '%东阿阿胶73折，留住父母的年轻状态%';
update eh_activities set content_category_id = 1002 where subject like '%十一大餐轰炸之后，来点清新的%';
update eh_activities set content_category_id = 1002 where subject like '%不去巴黎时装周，也能看秋冬时装秀%';
update eh_activities set content_category_id = 1002 where subject like '%有些事现在不做，这辈子都不会做了%';
update eh_activities set content_category_id = 1002 where subject like '%说不出口的情话，不如写下来，然后@TA%';
update eh_activities set content_category_id = 1002 where subject like '%剁手预警！ 一大波5折MK包包，比海淘更划算%';
update eh_activities set content_category_id = 1002 where subject like '%没吃过香港半岛酒店月饼，还好意思说自己是资深吃货？%';
update eh_activities set content_category_id = 1002 where subject like '%@您一封家书：爸，别担心，我在深圳过得很好%';
update eh_activities set content_category_id = 1002 where subject like '%每天专车接送上下班是一种怎样的体验？%';
update eh_activities set content_category_id = 1002 where subject like '%5.28陪孩子去看海%';
update eh_activities set content_category_id = 1002 where subject like '%有多久，没对她说“我爱你”？%';
update eh_activities set content_category_id = 1002 where subject like '%今天中午OE君告诉你午餐吃什么，速点%';
update eh_activities set content_category_id = 1002 where subject like '% 在这万物生长的春日里，把咖啡搬进你的办公室%';
update eh_activities set content_category_id = 1002 where subject like '%最in的Office Lady彩妆沙龙来了，不要错过咯~%';
update eh_activities set content_category_id = 1002 where subject like '%你的家乡是什么味道？%';
update eh_activities set content_category_id = 1002 where subject like '% 新钞&纪念币兑换%';
update eh_activities set content_category_id = 1002 where subject like '% 限时福利！免费加班餐现在派送，戳我领取！%';
update eh_activities set content_category_id = 1002 where subject like '%Officeasy中秋油画体验%';
update eh_activities set content_category_id = 1002 where subject like '%快乐工作，感谢有你们。Officeasy团队照拍摄开始啦！%';





-- merge from equipment2.1-delta-data-release.sql by lqs 20170119
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('1', '999992', 'PM', '1000750', '0', '�豸', '/�豸', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('2', '999992', 'PM', '1000750', '0', 'װ��', '/װ��', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('3', '999992', 'PM', '1000750', '0', '���÷�', '/���÷�', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('4', '999992', 'PM', '1000750', '0', '����', '/����', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('5', '999992', 'PM', '1000750', '0', '�ճ��������', '/�ճ��������', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('6', '999992', 'PM', '1000750', '0', '������ʩ���', '/������ʩ���', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('7', '999992', 'PM', '1000750', '0', '��ĩֵ��', '/��ĩֵ��', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('8', '999992', 'PM', '1000750', '0', '��ȫ���', '/��ȫ���', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('9', '999992', 'PM', '1000750', '0', '����', '/����', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);


update eh_equipment_inspection_equipments set inspection_category_id = 1;
update eh_equipment_inspection_standards set inspection_category_id = 1;
update eh_equipment_inspection_tasks set inspection_category_id = 1;






-- merge from authorization-delta-data-release.sql by lqs 20170120
-- 
-- �ͻ����Ϲ���ϸ��Ȩ�� add by xq.tian  2017/01/16
--
SELECT max(id) FROM `eh_service_module_privileges` INTO @service_module_pri_id;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10127, '0', '��ӿͻ�', '�ͻ�����ϸ��Ȩ�� ��ӿͻ�', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10127, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10128, '0', '�������뼰����ģ��', '�ͻ�����ϸ��Ȩ�� �������뼰����ģ��', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10128, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10129, '0', '����EXCEL', '�ͻ�����ϸ��Ȩ�� ����EXCEL', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10129, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10130, '0', '�鿴ͳ����Ϣ', '�ͻ�����ϸ��Ȩ�� �鿴ͳ����Ϣ', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10130, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10131, '0', '�޸Ŀͻ�����', '�ͻ�����ϸ��Ȩ�� �޸Ŀͻ�����', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10131, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10132, '0', '�鿴�ͻ�����', '�ͻ�����ϸ��Ȩ�� �鿴�ͻ�����', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10132, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10133, '0', '����ͻ�����', '�ͻ�����ϸ��Ȩ�� ����ͻ�����', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10133, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10134, '0', 'ɾ���ͻ�����', '�ͻ�����ϸ��Ȩ�� ɾ���ͻ�����', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10134, NULL, '0', NOW());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10127, 37000, '��ӿͻ�', 1, 1, '��ӿͻ� Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10128, 37000, '�������뼰����ģ��', 1, 1, '�������뼰����ģ�� Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10129, 37000, '����EXCEL', 1, 1, '����EXCEL Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10130, 37000, '�鿴ͳ����Ϣ', 1, 1, '�鿴ͳ����Ϣ Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10131, 37000, '�޸Ŀͻ�����', 1, 1, '�޸Ŀͻ����� Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10132, 37000, '�鿴�ͻ�����', 1, 1, '�鿴�ͻ����� Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10133, 37000, '����ͻ�����', 1, 1, '����ͻ����� Ȩ��', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10134, 37000, 'ɾ���ͻ�����', 1, 1, 'ɾ���ͻ����� Ȩ��', 202);


-- ������Ŀ����ҵ��ģ�� by sfyan 20170119
UPDATE `eh_service_modules` SET `type` = 0 WHERE `id` in (30000, 30500, 31000, 32000, 33000, 34000, 35000, 37000, 38000);


SELECT max(id) FROM `eh_service_module_scopes` INTO @eh_service_module_scopes;
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
select (@eh_service_module_scopes := @eh_service_module_scopes + 1), owner_id, menu_id, '', owner_type, owner_id, NULL, '2' from eh_web_menu_scopes where 
menu_id in (select id from eh_service_modules where id in (30000, 30500, 31000, 32000, 33000, 34000, 35000, 37000, 38000));
