-- 对接 北科建远程抄表  by jiarui 20180416
SET  @id = (SELECT MAX(id) FROM  eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id:=@id+1), 'energy.meter.thirdparty.server', 'http://122.225.71.66:211/test', 'energy.meter.thirdparty.server', '0', NULL);
