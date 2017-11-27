-- bydengs,20171114 物业报修2.9.3
SET @eh_categories_id = (SELECT MAX(id) FROM `eh_categories`);
SET @namespace_id = 999957;
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES
((@eh_categories_id:=@eh_categories_id+1), '6', '0', '物业报修', '任务/物业报修', '-1', '2', now(), NULL, NULL, NULL, @namespace_id);

SET @namespace_id = 999957;
DELETE  FROM  eh_configurations WHERE namespace_id = @namespace_id AND `name`=CONCAT('pmtask.handler-',@namespace_id);
INSERT INTO `eh_configurations` (`name`, `value`, `namespace_id`,`description`) VALUES (CONCAT('pmtask.handler-',@namespace_id), 'yue_kong_jian',@namespace_id,'越空间物业报修的handler');
