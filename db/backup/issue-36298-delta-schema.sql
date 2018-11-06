 
-- AUTHOR: 吴寒
-- REMARK: 会议管理V1.2
ALTER TABLE `eh_meeting_reservations`  CHANGE `content` `content` TEXT COMMENT '会议详细内容';
ALTER TABLE `eh_meeting_reservations`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
ALTER TABLE `eh_meeting_records`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';

-- 增加附件表 会议预定和会议纪要共用
CREATE TABLE `eh_meeting_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner type EhMeetingRecords/EhMeetingReservations',
  `owner_id` BIGINT NOT NULL COMMENT 'key of the owner',
  `content_name` VARCHAR(1024) COMMENT 'attachment object content name like: abc.jpg',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `content_size` INT(11)  COMMENT 'attachment object size',
  `content_icon_uri` VARCHAR(1024) COMMENT 'attachment object link of content icon',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;