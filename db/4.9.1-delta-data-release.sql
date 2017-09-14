-- 蒙版提示信息修改 by lei.lv
UPDATE `eh_namespace_masks` SET `id`='1', `namespace_id`='999971', `item_name`='人才公寓', `image_type`='3', `tips`='快速切换至人才公寓首页', `scene_type`='park_tourist' WHERE (`id`='1');
UPDATE `eh_namespace_masks` SET `id`='2', `namespace_id`='999971', `item_name`='园区服务', `image_type`='3', `tips`='快速切换至园区首页', `scene_type`='default' WHERE (`id`='2');


-- 把有管理员权限的用户，在eh_organization_members表里面标识成 manager add by sfyan 20170911
update `eh_organization_members` eom set `member_group` = '';
update `eh_organization_members` eom set `member_group` = ifnull((select 'manager' from eh_acls where `owner_type` = 'EhOrganizations' and `owner_id` = eom.organization_id and role_type = 'EhUsers' and role_id = eom.target_id and privilege_id in (10,15)  limit 1), '') where group_type = 'ENTERPRISE' and target_type = 'USER' and status = 3;


-- 去掉老版的缴费 by wentian
-- select * from eh_web_menus where path like '/20000/20700%';
-- select * from eh_web_menu_scopes where owner_id=999971 and menu_id in (20700,20701,20702,6100002);
delete from eh_web_menu_scopes where id=115444; -- 缴费管理(老版的缴费)

-- 去掉合同管理里面的“合同基础参数配置” by wentian
-- select * from eh_web_menus where path like '/20000/21200%';
-- select * from eh_web_menu_scopes where owner_id=999971 and menu_id in (21200,21210,20702,21220);
delete from eh_web_menu_scopes where id=115424; -- 合同基础参数配置

-- 去掉物业缴费/设置（张江高科不能设置） by wentian
-- select * from eh_web_menus where path like '/20000/20400%';
-- select * from eh_web_menu_scopes where owner_id=999971 and menu_id in (20420);
delete from eh_web_menu_scopes where id=115109; -- 物业缴费/设置

-- select * from eh_web_menus where name like '%物业缴费%'; by wentian
-- select * from eh_web_menu_scopes where owner_id=999971 and menu_id in (20410); -- 缴费记录
delete from eh_web_menu_scopes where id=115108;

-- 增加联系人职位 add by xiongying 20170914
ALTER TABLE eh_enterprise_customers ADD COLUMN contact_position VARCHAR(64);
