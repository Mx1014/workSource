CREATE TABLE `eh_forum_categories` (
`id`  bigint(20) NOT NULL ,
`uuid`  varchar(128) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`forum_id`  bigint(20) NOT NULL COMMENT 'forum id' ,
`entry_id`  bigint(20) NOT NULL COMMENT 'entry id' ,
`name`  varchar(255) NULL ,
`activity_entry_id`  bigint(20) NULL DEFAULT 0 COMMENT 'activity entry id' ,
`comment_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'support comment, 0-no, 1-yes',
`create_time`  datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP ,
`update_time`  datetime NULL DEFAULT '' ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
);

ALTER TABLE `eh_forum_posts` ADD COLUMN `forum_entry_id`  bigint(20) NULL DEFAULT 0 COMMENT 'forum_category  entry_id' ;