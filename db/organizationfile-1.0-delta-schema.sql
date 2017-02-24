
-- 对象存储(object storage)  add by xq.tian  2017/02/24
-- 对象表
CREATE TABLE `eh_os_objects` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `service_type` VARCHAR(32) COMMENT '需要存储对象的业务类型',
  `service_id` BIGINT COMMENT '业务类型对应的业务id',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id, eh_os_objects id',
  `object_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: dir, 1: file',
  `md5` VARCHAR(64) COMMENT 'md5',
  `path` VARCHAR(2048) COMMENT 'e.g: a/b/c/d/e',
  `name` VARCHAR(256) NOT NULL COMMENT 'object name',
  `size` INTEGER NOT NULL DEFAULT 0 COMMENT 'The unit is byte',
  `content_type` VARCHAR(32) COMMENT 'file object content type',
  `content_uri` VARCHAR(1024) COMMENT 'file object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `delete_uid` BIGINT,
  `delete_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';

-- 对象下载记录
CREATE TABLE `eh_os_object_download_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `service_type` VARCHAR(32) COMMENT '需要存储对象的业务类型',
  `service_id` BIGINT COMMENT '业务类型对应的业务id',
  `object_id` BIGINT NOT NULL COMMENT 'eh_os_objects id',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';