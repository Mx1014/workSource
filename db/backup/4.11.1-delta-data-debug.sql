-- bydengs,20171114 物业报修2.9.3
SET @eh_categories_id = (SELECT MAX(id) FROM `eh_categories`);
SET @namespace_id = 1000000;
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES
((@eh_categories_id:=@eh_categories_id+1), '6', '0', '物业报修', '任务/物业报修', '-1', '2', now(), NULL, NULL, NULL, @namespace_id);

SET @namespace_id = 1000000;
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES (CONCAT('pmtask.handler-',@namespace_id), 'yue_kong_jian', '越空间物业报修的handler');
