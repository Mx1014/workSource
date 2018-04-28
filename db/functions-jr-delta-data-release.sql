-- 对接 北科建远程抄表  by jiarui 20180416
SET  @id = (SELECT MAX(id) FROM  eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id:=@id+1), 'energy.meter.thirdparty.server', 'http://122.225.71.66:211/test', 'energy.meter.thirdparty.server', '0', NULL);

-- 增加公摊水电费，水电费改为自用水电费 by wentian
set @id = IFNULL((select MAX(`id`) from eh_payment_charging_items),0);
INSERT INTO `ehcore`.`eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
  (@id:=@id+1, '公摊水费', '0', NOW(), NULL, NULL, '1');
INSERT INTO `ehcore`.`eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
  (@id:=@id+1, '公摊电费', '0', NOW(), NULL, NULL, '1');

UPDATE `eh_payment_charging_items` SET `name` = '自用水费' where `name` = '水费';
UPDATE `eh_payment_charging_items` SET `name` = '自用电费' where `name` = '电费';

-- 增加错误码 by jiarui
set @id = IFNULL((SELECT MAX(id) from eh_locale_strings),0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10032', 'zh_CN', '分摊比例大于1');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10033', 'zh_CN', '比例系数不是数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10034', 'zh_CN', '比例系数未添加');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy', '10035', 'zh_CN', '比例系数在一个楼栋门牌的时候不等于1');

-- 增加表计类型及文案修改 by jiarui
set @id=(select MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy.meter.type', '4', 'zh_CN', '公摊水表');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'energy.meter.type', '5', 'zh_CN', '公摊电表');


UPDATE `eh_locale_strings`
SET `text`='自用水表' WHERE scope = 'energy.meter.type' and `code` = 1;

UPDATE `eh_locale_strings`
SET `text`='自用电表' WHERE scope='energy.meter.type' and `code` = 2 ;

-- 增加比例系数 by jiarui
set @id = ifnull((SELECT MAX(`id`) FROM eh_payment_variables), 0);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@id:=@id+1, NULL, NULL , '比例系数', '0', now(), NULL, now(), 'blxs');

-- 没有生成账单以及账单任务记录时的提示语更改 by wentian 2018/4/28
UPDATE `eh_locale_strings` SET `text` = '无数据' WHERE `scope` = 'assetv2' AND `code` = '10003';

UPDATE eh_enterprise_customers SET tracking_uid = NULL WHERE tracking_uid = '-1';

-- 客户增加的权限  by jiarui
UPDATE `eh_service_module_privileges` SET  default_order = 1, `remark`='新增客户' WHERE `module_id`='21110' and  `privilege_id`='21101';
UPDATE `eh_service_module_privileges` SET  default_order = 2,`remark`='修改客户' WHERE `module_id`='21110' and  `privilege_id`='21102';
UPDATE `eh_service_module_privileges` SET  default_order = 4,`remark`='导入客户' WHERE `module_id`='21110' and  `privilege_id`='21103';
UPDATE `eh_service_module_privileges` SET  default_order = 6,`remark`='同步客户' WHERE `module_id`='21110' and  `privilege_id`='21104';
UPDATE `eh_service_module_privileges` SET  default_order = 3,`remark`='删除客户' WHERE `module_id`='21110' and  `privilege_id`='21105';
UPDATE `eh_service_module_privileges` SET  default_order = 0 , `remark`='查看客户' WHERE `module_id`='21110' and  `privilege_id`='21106';
UPDATE `eh_service_module_privileges` SET  default_order = 7,`remark`='查看管理信息'WHERE `module_id`='21110' and  `privilege_id`='21107';
UPDATE `eh_service_module_privileges` SET  default_order = 8,`remark`='新增管理信息'WHERE `module_id`='21110' and  `privilege_id`='21108';
UPDATE `eh_service_module_privileges` SET  default_order = 9,`remark`='修改管理信息'WHERE `module_id`='21110' and  `privilege_id`='21109';
UPDATE `eh_service_module_privileges` SET  default_order = 10,`remark`='删除管理信息'WHERE `module_id`='21110' and  `privilege_id`='21110';
UPDATE `eh_service_module_privileges` SET  default_order = 11,`remark`='导入管理信息'WHERE `module_id`='21110' and  `privilege_id`='21111';
UPDATE `eh_service_module_privileges` SET  default_order = 12,`remark`='导出管理信息'WHERE `module_id`='21110' and  `privilege_id`='21112';
UPDATE `eh_service_module_privileges` SET  default_order = 0,`remark`='查看' WHERE `module_id`='21120' and  `privilege_id`='21113';


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21114', '0', '客户管理导出权限', '客户管理 业务模块权限', NULL);

set @id =(select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@id:=@id+1) ,'21110', '0', '21114', '导出客户', '5', NOW());

