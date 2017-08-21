-- 电商一维码查询uri add by yanjun 20170816
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'biz.zuolin.checkBarcode','/zl-ec/rest/openapi/commodity/barcodeByCommodityUrl','电商一维码查询uri','0',NULL);
