set @id = ifnull((select max(`id`) from `eh_service_module_app_mappings`), 0);
INSERT INTO `eh_service_module_app_mappings` (`id`, `app_origin_id_male`, `app_module_id_male`, `app_origin_id_female`, `app_module_id_female`, `create_time`, `create_uid`, `update_time`, `update_uid`) VALUES (@id:=@id+1, '0', '20400', '0', '21200', now(), '1', now(), NULL);
