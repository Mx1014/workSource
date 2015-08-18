#
# This file contains initial data set for a typical developer setup with DB+redis configuration
#
INSERT INTO `eh_servers` (`id`, `address_uri`, `server_type`, `status`, `config_tag`, `description`) 
    VALUES(1, 'jdbc:mysql://ehcore:ehcore@db-master:3306/ehcore?characterEncoding=UTF-8', 0, 1, 'default', 'default DB');
INSERT INTO `eh_servers` (`id`, `address_uri`, `address_port`, `server_type`, `status`, `config_tag`, `description`) 
    VALUES(2, 'redis-server', 6379, 1, 1, 'RedisStorage', 'Redis Storage');
INSERT INTO `eh_servers` (`id`, `address_uri`, `address_port`, `server_type`, `status`, `config_tag`, `description`) 
    VALUES(3, 'redis-cache', 6379, 2, 1, 'RedisCache', 'Redis Cache');
INSERT INTO `eh_content_server` (`name`, `description`, `private_address`, `private_port`, `public_address`, `public_port`) 
    VALUES('content server', 'content server', '10.1.1.90', 5000, '10.1.1.90', 5000,);

