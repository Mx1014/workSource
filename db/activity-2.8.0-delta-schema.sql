-- 储存预览内容  2017-04-06 add by yanjun
CREATE TABLE `eh_previews` (
   `id` bigint(20) NOT NULL COMMENT 'id of the record',
   `content` text,
   `content_type` varchar(128) DEFAULT NULL COMMENT 'content type',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
-- 如果是富文本的话rich_content存储原始的content  2017-04-06 add by yanjun
ALTER TABLE `eh_links` ADD COLUMN `rich_content` longtext NULL COMMENT 'rich_content';