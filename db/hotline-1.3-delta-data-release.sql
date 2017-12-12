set @eh_service_configurations_id = (select max(id) from eh_service_configurations);
INSERT INTO `eh_service_configurations` (`id`, `owner_type`, `owner_id`, `name`, `value`, `namespace_id`, `display_name`) VALUES (@eh_service_configurations_id+1, 'community', '240111044331050370', 'hotline-notshow', '0', '999968', '专属客服');
