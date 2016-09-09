-- 接口配置
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.ware.info.api','zl-ec/rest/openapi/model/listByCondition','获取商品信息',0);
