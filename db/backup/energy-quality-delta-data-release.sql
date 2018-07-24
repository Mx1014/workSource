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

