-- 紫荆车辆放行测试参数 by dengs,20180118
UPDATE `eh_configurations` SET `value`='http://tgd.poapi.parkingjet.cn:8082/CommonOpenAPi/default.ashx' WHERE `name` = 'parking.zijing.url';
UPDATE `eh_configurations` SET `value`='201706221000' WHERE `name` = 'parking.zijing.appid';
UPDATE `eh_configurations` SET `value`='qyruirxn20145601739' WHERE `name` = 'parking.zijing.appkey';
UPDATE `eh_configurations` SET `value`='20170104000000000002' WHERE `name` = 'parking.zijing.parkingid';

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.zijing.marketid', '076900012201712120001', '紫荆车辆放行markid', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.zijing.userid', '0769000120171212000000000001', '紫荆车辆放行userid', 0, NULL);

-- 正中会申请月卡车测试参数 by dengs,20180118
UPDATE `eh_configurations` SET `value`='http://220.160.111.118:9095/' WHERE `name` = 'parking.kexing.url';
UPDATE `eh_configurations` SET `value`='ktapi' WHERE `name` = 'parking.kexing.user';
UPDATE `eh_configurations` SET `value`='0306A9' WHERE `name` = 'parking.kexing.pwd';
UPDATE `eh_configurations` SET `value`='F7A0B971B199FD2A1017CEC5' WHERE `name` = 'parking.kexing.key';

-- 深圳湾申请月卡车测试参数 by dengs,20180118
UPDATE `eh_configurations` SET `value`='http://220.160.111.114:9099' WHERE `name` = 'parking.mybay.url';
UPDATE `eh_configurations` SET `value`='ktapi' WHERE `name` = 'parking.mybay.user';
UPDATE `eh_configurations` SET `value`='0306A9' WHERE `name` = 'parking.mybay.pwd';
UPDATE `eh_configurations` SET `value`='F7A0B971B199FD2A1017CEC5' WHERE `name` = 'parking.mybay.key';
UPDATE `eh_configurations` SET `value`='http://szdas.iok.la:17508' WHERE `name` = 'parking.mybay.searchCar.url';
