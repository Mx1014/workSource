-- 紫荆车辆放行测试参数 by dengs,20180118
UPDATE `eh_configurations` SET `value`='http://tgd.poapi.parkingjet.cn:8082/CommonOpenAPi/default.ashx' WHERE `name` = 'parking.zijing.url';
UPDATE `eh_configurations` SET `value`='201706221000' WHERE `name` = 'parking.zijing.appid';
UPDATE `eh_configurations` SET `value`='qyruirxn20145601739' WHERE `name` = 'parking.zijing.appkey';
UPDATE `eh_configurations` SET `value`='20170104000000000002' WHERE `name` = 'parking.zijing.parkingid';

UPDATE `eh_configurations` SET `value`='076900012201712120001' WHERE `name` = 'parking.zijing.marketid';
UPDATE `eh_configurations` SET `value`='0769000120171212000000000001' WHERE `name` = 'parking.zijing.userid';


-- 正中会申请月卡车测试参数 by dengs,20180118
UPDATE `eh_configurations` SET `value`='http://220.160.111.118:9095/' WHERE `name` = 'parking.kexing.url';
UPDATE `eh_configurations` SET `value`='ktapi' WHERE `name` = 'parking.kexing.user';
UPDATE `eh_configurations` SET `value`='0306A9' WHERE `name` = 'parking.kexing.pwd';
UPDATE `eh_configurations` SET `value`='F7A0B971B199FD2A1017CEC5' WHERE `name` = 'parking.kexing.key';
