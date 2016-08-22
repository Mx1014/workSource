#
# This file contains initial data set for a typical developer setup with DB+redis configuration
#
INSERT INTO `eh_servers` (`id`, `address_uri`, `server_type`, `status`, `config_tag`, `description`) 
    VALUES(1, 'jdbc:mysql://ehcore:ehcore@db-master:3306/ehcore?characterEncoding=UTF-8', 0, 1, 'default', 'default DB');
INSERT INTO `eh_servers` (`id`, `address_uri`, `address_port`, `server_type`, `status`, `config_tag`, `description`) 
    VALUES(2, 'redis-server', 6380, 1, 1, 'RedisStorage', 'Redis Storage');
INSERT INTO `eh_servers` (`id`, `address_uri`, `address_port`, `server_type`, `status`, `config_tag`, `description`) 
    VALUES(3, 'redis-cache', 6379, 2, 1, 'RedisCache', 'Redis Cache');
INSERT INTO `eh_content_server` (`name`, `description`, `private_address`, `private_port`, `public_address`, `public_port`) 
    VALUES('content server', 'content server', '10.1.1.90', 5000, '10.1.1.90', 5000);
INSERT INTO `eh_borders` (`description`, `private_address`, `private_port`, `public_address`, `public_port`, `status`) 
    VALUES('border server 1', '10.1.1.91', 9090, 'border1.lab.everhomes.com', 443, 1);	
INSERT INTO `eh_borders` (`description`, `private_address`, `private_port`, `public_address`, `public_port`, `status`) 
    VALUES('border server 2', '10.1.1.92', 9090, 'border2.lab.everhomes.com', 443, 1);	
