CREATE TABLE `eh_previews` (
   `id` bigint(20) NOT NULL COMMENT 'id of the record',
   `content` text,
   `content_type` varchar(128) DEFAULT NULL COMMENT 'content type',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;