set @id= IFNULL((select max(id) from eh_apps),0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (@id:=@id+1, '1', '95908e84-fe3c-4bb3-a2a4-db6a078abfe3', 'fz8QGHnJ0796c1LIyyMQI2z1rAVY0DRcynEh23CdpPatapDmHkv0sqGWDBVLWHLBVmOu3StHw4JrD4TB8iX1EQ==', '发票系统', NULL, '1', NOW(), NULL, NULL);
