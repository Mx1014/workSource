-- 在业主类型表中添加数据      2016/09/02 by xq.tian
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('1', '0', 'owner', '业主', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('2', '0', 'tenement', '租客', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('3', '0', 'relative', '亲属', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('4', '0', 'friend', '朋友', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('5', '0', 'nurse', '保姆', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('6', '0', 'agency', '地产中介', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('7', '0', 'readyauth', '待审核', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('8', '0', 'other', '其他', '1');

-- 插入模板       2016/09/02 by xq.tian
SET @eh_locale_strings_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerBehavior', 'immigration', 'zh_CN', '迁入');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerBehavior', 'emigration', 'zh_CN', '迁出');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerAddressAuthType', '2', 'zh_CN', '无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerAddressAuthType', '0', 'zh_CN', '未认证');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerAddressAuthType', '1', 'zh_CN', '认证');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerAddressLivingStatus', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'orgOwnerAddressLivingStatus', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'ownerCarPrimaryFlag', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'ownerCarPrimaryFlag', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14001', 'zh_CN', '业主已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14002', 'zh_CN', '业主不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '14003', 'zh_CN', '客户手机号码重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '15001', 'zh_CN', '导入失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16001', 'zh_CN', '车辆已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16002', 'zh_CN', '车辆不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16003', 'zh_CN', '车牌号码重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '16004', 'zh_CN', '用户已经在车辆的使用者列表中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'pm', '17001', 'zh_CN', '用户已经在楼栋门牌中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'parkingType', '2', 'zh_CN', '月卡');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'parkingType', '1', 'zh_CN', '临时');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'user', '1', 'zh_CN', '男');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'user', '2', 'zh_CN', '女');

-- 修改业主管理菜单为客户资料管理   by xq.tian 20160920
UPDATE `eh_web_menus` SET `name`='客户资料管理' WHERE (`id`='36000');
UPDATE `eh_acl_privileges` SET `name`='客户资料管理',`description`='客户资料管理' WHERE (`id`='411');
UPDATE `eh_web_menu_privileges` SET `name`='客户资料管理', `discription`='客户资料管理 全部权限' WHERE (`id`='142');


-- 新增车辆管理菜单   by xq.tian  20160920
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (37000,'车辆管理',30000,null,null,0,2,'/30000/37000','park',370);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (420,0,'车辆管理','车辆管理',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),420,37000,'车辆管理',1,1,'车辆管理  全部权限',370);

set @eh_web_menu_scopes_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@eh_web_menu_scopes_id:=@eh_web_menu_scopes_id + 1), '37000', NULL, 'EhNamespaces', '999992', '2');


-- 添加菜单与权限的关联关系
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
    SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now()
    FROM `eh_web_menu_privileges`
    WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%37000%');


