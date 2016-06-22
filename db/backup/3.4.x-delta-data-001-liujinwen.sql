-- user/getBizSignature
INSERT INTO `eh_apps` (`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`,`update_uid`,`update_time`) VALUES ('1', '44952417-b120-4f41-885f-0c1110c6aece', 'gPVzYlvWb1z7zPZkQTFX1W92xqnKgBL3yy5aL/foPRdOuGmXeKvQ5lH1zEV8XqeN5zEACaOCeoWh6ezPv31kaw==', 'sign app', 'getBizsignature interface use it', '1', '2016-03-23 00:00:00', null, null);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES ('sign.appKey','44952417-b120-4f41-885f-0c1110c6aece','sign.appKey',0);

INSERT INTO `eh_apps` (`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`,`update_uid`,`update_time`) VALUES ('1', 'f9392ce2-341b-40c1-9c2c-99c702215535', 'COTFiIlZ8mZ3b8w3AS84c6b4XE3yx+raanCIJHClxjRTBqpFoyqYrP9VukWclmqI7Qk3WH36IWfN3Xdps3I4rQ==', 'zuolin app', 'signLogon or oauth2Logon interface use it', '1', '2016-03-23 00:00:00', null, null);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES ('zuolin.appKey', 'f9392ce2-341b-40c1-9c2c-99c702215535', 'zuolin.appKey', '0');

INSERT INTO `eh_apps` (`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES ('1', 'd80e06ca-3766-11e5-b18f-b083fe4e159f', 'g1JOZUM3BYzWpZD5Q7p3z+i/z0nj2TcokTFx2ic53FCMRIKbMhSUCi7fSu9ZklFCZ9tlj68unxur9qmOji4tNg==', 'biz app', '', '1', now());
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES ('biz.appKey', 'd80e06ca-3766-11e5-b18f-b083fe4e159f', 'biz.appKey', '0');