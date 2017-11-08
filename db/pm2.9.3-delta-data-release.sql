SET @eh_categories_id = (SELECT MAX(id) FROM `eh_categories`);
SET @namespace_id = 1000000;
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES 
((@eh_categories_id:=@eh_categories_id+1), '6', '0', '物业报修', '任务/物业报修', '0', '2', now(), NULL, NULL, NULL, @namespace_id);
