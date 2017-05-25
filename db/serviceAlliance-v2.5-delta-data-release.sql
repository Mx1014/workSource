-- 服务联盟初始化排序的序号为id  by dengs, 20170523
UPDATE `eh_service_alliances` SET `default_order` = `id`;

-- 将服务联盟下的菜单机构管理改为服务管理 by dengs,20170524
update `eh_web_menus` SET `name`='服务管理' WHERE `id` = 40520;