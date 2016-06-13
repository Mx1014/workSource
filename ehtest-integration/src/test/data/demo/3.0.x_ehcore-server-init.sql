SET foreign_key_checks = 0;

DELETE FROM `eh_servers`;
DELETE FROM `eh_content_server`;
DELETE FROM `eh_borders`;
INSERT INTO `eh_servers` (`id`, `address_uri`, `server_type`, `status`, `config_tag`, `description`) VALUES(1, 'jdbc:mysql://ehcore_test:ehcore_test@db-master:3306/ehcore_test?characterEncoding=UTF-8&encrypted=0', 0, 1, 'default', 'default DB');
INSERT INTO `eh_servers` (`id`, `address_uri`, `address_port`, `server_type`, `status`, `config_tag`, `description`) VALUES(2, 'redis-server', 6380, 1, 1, 'RedisStorage', 'Redis Storage');
INSERT INTO `eh_servers` (`id`, `address_uri`, `address_port`, `server_type`, `status`, `config_tag`, `description`) VALUES(3, 'redis-cache', 6379, 2, 1, 'RedisCache', 'Redis Cache');
INSERT INTO `eh_content_server` (`id`, `name`, `description`, `private_address`, `private_port`, `public_address`, `public_port`) VALUES(1, 'content server', 'content server', '10.1.1.87', 5000, '10.1.1.87', 5000);
INSERT INTO `eh_borders` (`id`, `description`, `private_address`, `private_port`, `public_address`, `public_port`, `status`) VALUES(1, 'border server 1', '10.1.1.87', 9090, '10.1.1.87', 9090, 1);
-- INSERT INTO `eh_borders` (`id`, `description`, `private_address`, `private_port`, `public_address`, `public_port`, `status`) VALUES(2, 'border server 2', '10.1.1.87', 9091, '10.1.1.87', 9091, 1);

SET foreign_key_checks = 1;
