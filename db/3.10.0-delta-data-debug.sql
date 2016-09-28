INSERT INTO `eh_search_types` (`id`, `namespace_id`, `name`, `content_type`, `status`, `create_time`) VALUES (1, 0, '快讯', 'news', '1', UTC_TIMESTAMP());

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('news.url', '/park-news/index.html/#/news/share/', 'news page url', 0, NULL);
-- added by wh  2016-9-21 配置园区快讯广场图标测试用 增加categoryId =1
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112138, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 48, '{"categoryId":1,"timeWidgetStyle":"date"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112147, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 48, '{"categoryId":1,"timeWidgetStyle":"date"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

-- added by wh  2016-9-21 给ibase园区快讯 增加categoryId=0,展示方式为日期时间
UPDATE  `eh_launch_pad_layouts` SET layout_json = '{"versionCode":"2016081701","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21}]}' WHERE namespace_id = 999989 AND NAME = 'ServiceMarketLayout';

-- added by wh  2016-9-22 配置园区快讯web广场图标测试用 增加categoryId =1
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112137, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯web', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 14, '{"url":"http://beta.zuolin.com/park-news/index.html?hideNavigationBar=1#sign_suffix"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112146, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯web', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 14, '{"url":"http://beta.zuolin.com/park-news/index.html?hideNavigationBar=1#sign_suffix"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

    
-- 在业主类型表中添加数据      2016/09/02 by xq.tian
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('1', '0', 'owner', '业主', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('2', '0', 'tenement', '租户', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('3', '0', 'relative', '亲属', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('4', '0', 'friend', '朋友', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('5', '0', 'nurse', '保姆', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('6', '0', 'agency', '地产中介', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('7', '0', 'other', '其它', '1');

-- 插入模板       2016/09/02 by xq.tian
SET @eh_locale_strings_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.behavior', 'immigration', 'zh_CN', '迁入');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.behavior', 'emigration', 'zh_CN', '迁出');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.behavior', 'delete', 'zh_CN', '删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.authType', '2', 'zh_CN', '无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.authType', '0', 'zh_CN', '未认证');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.authType', '1', 'zh_CN', '认证');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.livingStatus', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.address.livingStatus', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.primaryFlag', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.primaryFlag', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.parkingType', '2', 'zh_CN', '月卡');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm.car.parkingType', '1', 'zh_CN', '临时');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14001', 'zh_CN', '业主已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14002', 'zh_CN', '业主不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14003', 'zh_CN', '客户手机号码重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '15001', 'zh_CN', '导入失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '15002', 'zh_CN', '导入失败, 没有解析到数据');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '15003', 'zh_CN', '导入失败, 楼栋门牌信息错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '15004', 'zh_CN', '导入失败, 生日信息错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16001', 'zh_CN', '车辆已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16002', 'zh_CN', '车辆不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16003', 'zh_CN', '车牌号码重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16004', 'zh_CN', '用户已经在车辆的使用者列表中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '17001', 'zh_CN', '用户已经在楼栋门牌中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '17002', 'zh_CN', '该楼栋门牌已经处于此状态');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'user', '1', 'zh_CN', '男');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'user', '2', 'zh_CN', '女');

-- 修改业主管理菜单为客户资料管理   by xq.tian 20160920
UPDATE `eh_web_menus` SET `name`='客户资料管理' WHERE (`id`='36000');
UPDATE `eh_acl_privileges` SET `name`='客户资料管理',`description`='客户资料管理' WHERE (`id`='411');

-- 新增车辆管理菜单   by xq.tian  20160920
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (37000,'车辆管理',30000,null,null,0,2,'/30000/37000','park',370);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (420,0,'车辆管理','车辆管理',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),420,37000,'车辆管理',1,1,'车辆管理  全部权限',370);

-- 添加菜单与权限的关联关系
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
    SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now()
    FROM `eh_web_menu_privileges`
    WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%37000%');

-- 隐藏业主管理菜单
UPDATE `eh_web_menu_scopes` SET `apply_policy` = '0' WHERE (`menu_id` = '36000');


-- 插入审批类型对应的汉字文本，add by tt, 20160901
SET @max_id = (SELECT max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '1', 'zh_CN', '请假');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '2', 'zh_CN', '异常');

-- 审批错误提示，add by tt, 20160923
set @id = (select max(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10000', 'zh_CN', '请选择请假类型');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10001', 'zh_CN', '请输入请假理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10002', 'zh_CN', '请选择请假时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10003', 'zh_CN', '请假开始时间必须小于请假结束时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10004', 'zh_CN', '所选时间包含已请假时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10005', 'zh_CN', '请假时间是非工作时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10006', 'zh_CN', '请假时长为0');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10007', 'zh_CN', '请输入申请理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10008', 'zh_CN', '类型名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10009', 'zh_CN', '类型名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10010', 'zh_CN', '该审批人设置关联了审批规则，请先删除对应的审批规则，再删除该审批人设置');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10011', 'zh_CN', '审批名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10012', 'zh_CN', '审批名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10013', 'zh_CN', '审批名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10014', 'zh_CN', '审批规则名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10015', 'zh_CN', '审批规则名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10016', 'zh_CN', '审批规则名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10017', 'zh_CN', '请假审批不能选择无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10018', 'zh_CN', '忘打卡审批不能为无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10019', 'zh_CN', '请输入驳回理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10020', 'zh_CN', '请选择需要审批的申请单');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10021', 'zh_CN', '类型名称不能超过8个字');

-- 审批发送消息模板，add by tt, 20160923
set @id := (select max(id) from `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 11, 'zh_CN', '用户提交请假申请', '${creatorName}提交了请假申请，请假时间：${time}，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 12, 'zh_CN', '请假申请到下一级别', '${creatorName}提交了请假申请，请假时间：${time}，${approver}已同意，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 13, 'zh_CN', '请假申请通过', '您提交的请假申请已通过审批，请假时间：${time}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 14, 'zh_CN', '请假申请驳回', '您提交的请假申请被${approver}驳回，理由是：${reason}，请假时间：${time}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 21, 'zh_CN', '用户提交异常申请', '${creatorName}提交了异常申请，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 22, 'zh_CN', '异常申请到下一级别', '${creatorName}提交了异常申请，${approver}已同意，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 23, 'zh_CN', '异常申请通过', '您对${punchDate}提交的异常申请已通过审批。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 24, 'zh_CN', '异常申请驳回', '您对${punchDate}提交的异常申请被${approver}驳回，理由是：${reason}。', 0);


-- 服务联盟配三种形式的入口供测试 add by xiongying 20160923
update eh_launch_pad_items set action_data = '{"type":11,"parentId":11,"displayType": "grid"}' where id in(803, 10304) and namespace_id = 1000000;
update eh_launch_pad_items set item_label = '服务联盟grid' where id in(803, 10304) and namespace_id = 1000000;

set @items_id = (select max(id) from `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟tab', 'cs://1/image/aW1hZ2UvTVRvM016RmpNelJtTURGbU9ETTJZVEJrTWprNE1qbGpZbU0xTTJZMFlqWTFNZw', '1', '1', '33', '{\"type\":11,\"parentId\":11,\"displayType\": \"tab\"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟tab', 'cs://1/image/aW1hZ2UvTVRvek9EazRObVUyWXpJNU9UUTJOVEppTTJSa1l6VXpOVGxsWmpFMk1UYzJOdw', '1', '1', '33', '{\"type\":11,\"parentId\":11,\"displayType\": \"tab\"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟list', 'cs://1/image/aW1hZ2UvTVRvM016RmpNelJtTURGbU9ETTJZVEJrTWprNE1qbGpZbU0xTTJZMFlqWTFNZw', '1', '1', '33', '{\"type\":11,\"parentId\":11,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟list', 'cs://1/image/aW1hZ2UvTVRvek9EazRObVUyWXpJNU9UUTJOVEppTTJSa1l6VXpOVGxsWmpFMk1UYzJOdw', '1', '1', '33', '{\"type\":11,\"parentId\":11,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'pm_admin', '0');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟展现形式1wx', 'cs://1/image/aW1hZ2UvTVRvM016RmpNelJtTURGbU9ETTJZVEJrTWprNE1qbGpZbU0xTTJZMFlqWTFNZw', '1', '1', '14', '{"url":"http://alpha.lab.everhomes.com/service-alliance/index.html?parentId=11&type=11&hideNavigationBar=1#sign_suffix"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟展现形式1wx', 'cs://1/image/aW1hZ2UvTVRvek9EazRObVUyWXpJNU9UUTJOVEppTTJSa1l6VXpOVGxsWmpFMk1UYzJOdw', '1', '1', '14', '{"url":"http://alpha.lab.everhomes.com/service-alliance/index.html?parentId=11&type=11&hideNavigationBar=1#sign_suffix"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟展现形式2wx', 'cs://1/image/aW1hZ2UvTVRvM016RmpNelJtTURGbU9ETTJZVEJrTWprNE1qbGpZbU0xTTJZMFlqWTFNZw', '1', '1', '14', '{"url":"http://alpha.lab.everhomes.com/service-alliance/index.html?parentId=11&type=11&hideNavigationBar=1#/service_typeList#sign_suffix"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟展现形式2wx', 'cs://1/image/aW1hZ2UvTVRvek9EazRObVUyWXpJNU9UUTJOVEppTTJSa1l6VXpOVGxsWmpFMk1UYzJOdw', '1', '1', '14', '{"url":"http://alpha.lab.everhomes.com/service-alliance/index.html?parentId=11&type=11&hideNavigationBar=1#/service_typeList#sign_suffix"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟展现形式3wx', 'cs://1/image/aW1hZ2UvTVRvM016RmpNelJtTURGbU9ETTJZVEJrTWprNE1qbGpZbU0xTTJZMFlqWTFNZw', '1', '1', '14', '{"url":"http://alpha.lab.everhomes.com/service-alliance/index.html?parentId=11&type=11&hideNavigationBar=1#/service_list#sign_suffix"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) VALUES (@items_id:=@items_id+1, '1000000', '0', '0', '0', '/home', 'GovAgencies', '服务联盟', '服务联盟展现形式3wx', 'cs://1/image/aW1hZ2UvTVRvek9EazRObVUyWXpJNU9UUTJOVEppTTJSa1l6VXpOVGxsWmpFMk1UYzJOdw', '1', '1', '14', '{"url":"http://alpha.lab.everhomes.com/service-alliance/index.html?parentId=11&type=11&hideNavigationBar=1#/service_list#sign_suffix"}', '0', '0', '1', '1', '', '0', '', '', NULL, '0', 'pm_admin', '0');


-- 深圳湾管理员账号
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233082, UUID(), '19067786551', '杨江', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228015, 233082,  '0',  '13247768050',  '221616',  3, UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2111453, 1005034, 'USER', 233082, 'manager', '杨江', 0, '13247768050', 3);	 
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12568, 'EhOrganizations', 1005034, 'EhUsers', 233082, 1001, 1, UTC_TIMESTAMP());
    
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233083, UUID(), '19067786552', '许娟', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228016, 233083,  '0',  '18589001345',  '221616',  3, UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2111454, 1005034, 'USER', 233083, 'manager', '许娟', 0, '18589001345', 3);	 
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12569, 'EhOrganizations', 1005034, 'EhUsers', 233083, 1001, 1, UTC_TIMESTAMP());    

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233084, UUID(), '19067786553', '颜婷', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228017, 233084,  '0',  '15083842638',  '221616',  3, UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2111455, 1005034, 'USER', 233084, 'manager', '颜婷', 0, '15083842638', 3);	 
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12570, 'EhOrganizations', 1005034, 'EhUsers', 233084, 1001, 1, UTC_TIMESTAMP());    
    
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233085, UUID(), '19067786554', '张大伟', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228018, 233085,  '0',  '18682473820',  '221616',  3, UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2111456, 1005034, 'USER', 233085, 'manager', '张大伟', 0, '18682473820', 3);	 
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12571, 'EhOrganizations', 1005034, 'EhUsers', 233085, 1001, 1, UTC_TIMESTAMP());  
    
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233086, UUID(), '19067786555', '陈慕葶', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228019, 233086,  '0',  '13043452532',  '221616',  3, UTC_TIMESTAMP(), 999987);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2111457, 1005034, 'USER', 233086, 'manager', '陈慕葶', 0, '13043452532', 3);	 
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12572, 'EhOrganizations', 1005034, 'EhUsers', 233086, 1001, 1, UTC_TIMESTAMP());  

-- configuration表下方配活动
set @configuration_id = (select max(id) from `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (@configuration_id:=@configuration_id+1, 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdefault%3F_k%3Dzlbiz#sign_suffix', 'business url', '999987', NULL);

-- 更新ibase、威新LINK+、科技园物业报修2.0
delete from eh_launch_pad_layouts where name = 'PmLayout' and namespace_id in (999989, 999991, 1000000); 
delete from eh_launch_pad_items where item_location = '/home/Pm' and namespace_id in (999989, 999991, 1000000);
update eh_launch_pad_items set action_type = 14 , action_data='{"url":"http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}' where id in (814, 10628, 109996);
update eh_launch_pad_items set action_type = 51 , action_data='' where id in (10310, 10610, 109986);
   
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
--	VALUES ('197', 'sms.default.yzx', '11', 'zh_CN', '任务操作模版', '29479', '999992');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
--	VALUES ('198', 'sms.default.yzx', '10', 'zh_CN', '任务操作模版', '29478', '999992');
delete from eh_web_menu_scopes where owner_id = 999989 and menu_id in (21000, 22000, 23000);
delete from eh_web_menu_scopes where owner_id = 999991 and menu_id in (21000, 22000, 23000);
delete from eh_web_menu_scopes where owner_id = 1000000 and menu_id in (21000, 22000, 23000);
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 999989,2);
	
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 999991,2);
	
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 1000000,2);

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('8', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '999989');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('9', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '999991');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('10', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '1000000');
