set @eh_configurations_id = (select Max(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id:=@eh_configurations_id+1),
'debug.flag', 'true', 'authorzaiton2.0 debug flag', 0, NULL);