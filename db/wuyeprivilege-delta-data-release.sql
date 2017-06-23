-- delete from eh_service_modules where path like '%/20100/%';
-- delete from eh_service_modules where path like '%/20400/%';
delete from eh_service_modules where path like '%/20600/%';
delete from eh_service_modules where path like '%/20800/%';
-- delete from eh_service_modules where path like '%/20900/%';
delete from eh_service_modules where path like '%/21000/%';
-- delete from eh_service_modules where path like '%/49100/%';



insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20610,'类型管理',20600,'/20000/20600/20610','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20620,'规范管理',20600,'/20000/20600/20620','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20630,'标准管理',20600,'/20000/20600/20630','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20640,'标准审批',20600,'/20000/20600/20640','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20650,'任务查询',20600,'/20000/20600/20650','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20655,'绩效考核',20600,'/20000/20600/20655','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20660,'统计',20600,'/20000/20600/20660','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20661,'分数统计',20600,'/20000/20600/20660/20661','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20662,'任务数统计',20600,'/20000/20600/20660/20662','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20663,'检查统计',20600,'/20000/20600/20660/20663','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20670,'修改记录',20600,'/20000/20600/20670','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块

insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20810,'参考标准',20800,'/20000/20800/20810','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20811,'标准列表',20800,'/20000/20800/20810/20811','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20812,'巡检关联审批',20800,'/20000/20800/20810/20812','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20820,'巡检台账',20800,'/20000/20800/20820','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20821,'巡检对象',20800,'/20000/20800/20820/20821','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20822,'备品备件',20800,'/20000/20800/20820/20822','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20830,'任务列表',20800,'/20000/20800/20830','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20831,'任务列表',20800,'/20000/20800/20830/20831','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20840,'巡检项资料库管理',20800,'/20000/20800/20840','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20841,'巡检项设置',20800,'/20000/20800/20840/20841','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20850,'统计',20800,'/20000/20800/20850','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20851,'总览',20800,'/20000/20800/20850/20851','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(20852,'查看所有任务',20800,'/20000/20800/20850/20852','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块

insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21010,'仓库维护',21000,'/20000/21000/21010','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21020,'物品维护',21000,'/20000/21000/21020','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21022,'物品信息',21000,'/20000/21000/21020/21022','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21024,'物品分类',21000,'/20000/21000/21020/21024','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21030,'库存维护',21000,'/20000/21000/21030','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21032,'库存查询',21000,'/20000/21000/21030/21032','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21034,'库存日志',21000,'/20000/21000/21030/21034','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21040,'领用管理',21000,'/20000/21000/21040','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21042,'领用管理',21000,'/20000/21000/21040/21042','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21044,'我的领用',21000,'/20000/21000/21040/21044','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21050,'参数配置',21000,'/20000/21000/21050','1','3','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21052,'工作流设置',21000,'/20000/21000/21050/21052','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块
insert into `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) values(21054,'参数配置',21000,'/20000/21000/21050/21054','1','4','2','0',NOW()); -- 定义模块下的权限分类，parentId代表分类的所属模块



SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.manage', 10010, '', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.all', 10010, '', '0', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 类型管理修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.category.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20610','0',@privilege_id,'品质核查 类型管理修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 规范管理修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.specification.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20620','0',@privilege_id,'品质核查 规范管理修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准管理修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standard.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20630','0',@privilege_id,'品质核查 标准管理修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准审批查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standardreview.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20640','0',@privilege_id,'品质核查 标准审批查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 标准审批审核权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.standardreview.review', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20640','0',@privilege_id,'品质核查 标准审批审核权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 任务查询权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.task.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20650','0',@privilege_id,'品质核查 任务查询权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 分数统计查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.stat.score', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20661','0',@privilege_id,'品质核查 分数统计查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 任务数统计查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.stat.task', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20662','0',@privilege_id,'品质核查 任务数统计查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 修改记录查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.updatelog.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20670','0',@privilege_id,'品质核查 修改记录查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核新增权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核修改权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 绩效考核删除权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.sample.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20655','0',@privilege_id,'品质核查 绩效考核删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '品质核查 检查统计查看权限', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'quality.stat.sample', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20663','0',@privilege_id,'品质核查 检查统计查看权限','0',NOW());

  
  
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.manage', 10011, '', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.all', 10011, '', '0', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 标准新增修改权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.standard.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20811','0',@privilege_id,'设备巡检 标准新增修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 标准查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.standard.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20811','0',@privilege_id,'设备巡检 标准查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 标准删除权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.standard.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20811','0',@privilege_id,'设备巡检 标准删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检关联审批查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.relation.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20812','0',@privilege_id,'设备巡检 巡检关联审批查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检关联审批审核权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.relation.review', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20812','0',@privilege_id,'设备巡检 巡检关联审批审核权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检关联审批删除失效关联权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.relation.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20812','0',@privilege_id,'设备巡检 巡检关联审批删除失效关联权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检对象查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20821','0',@privilege_id,'设备巡检 巡检对象查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检对象新增修改权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20821','0',@privilege_id,'设备巡检 巡检对象新增修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检对象删除权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20821','0',@privilege_id,'设备巡检 巡检对象删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 任务查询权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.task.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20831','0',@privilege_id,'设备巡检 任务查询权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项查看权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.list', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20841','0',@privilege_id,'设备巡检 巡检项查看权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项新增权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.create', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20841','0',@privilege_id,'设备巡检 巡检项新增权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项删除权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.delete', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20841','0',@privilege_id,'设备巡检 巡检项删除权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 巡检项修改权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.item.update', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20841','0',@privilege_id,'设备巡检 巡检项修改权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 统计总览权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.stat.pandect', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20851','0',@privilege_id,'设备巡检 统计总览权限','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ((@privilege_id := @privilege_id + 1), '0', '设备巡检 统计查看所有任务权限', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'equipment.stat.alltask', @privilege_id, '', '0', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20852','0',@privilege_id,'设备巡检 统计查看所有任务权限','0',NOW());

insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20600','1','10010','品质核查管理权限','0',NOW());   -- 定义模块的管理权限， 其中privilege_type 代表权限类型，1管理权限，直接管理模块id
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20600','2','10010','品质核查全部权限','0',NOW());   -- 定义模块的全部权限， 其中privilege_type 代表权限类型，2模块全部权限，直接管理模块id

insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20800','1','10011','设备巡检管理权限','0',NOW());   -- 定义模块的管理权限， 其中privilege_type 代表权限类型，1管理权限，直接管理模块id
insert into `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) values((@module_privilege_id := @module_privilege_id + 1),'20800','2','10011','设备巡检全部权限','0',NOW());   -- 定义模块的全部权限， 其中privilege_type 代表权限类型，2模块全部权限，直接管理模块id










