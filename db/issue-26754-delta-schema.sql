-- 通用脚本
-- ADD BY 梁燕龙
-- issue-26754 敏感词日志记录
CREATE TABLE `eh_sensitive_filter_record` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `sensitive_words` VARCHAR(128) COMMENT '敏感词',
  `module_id` BIGINT COMMENT '模块ID',
  `community_id` BIGINT COMMENT '项目ID',
  `creator_uid` BIGINT COMMENT '记录发布人userId' ,
  `creator_name` VARCHAR(32) COMMENT '发布人姓名',
  `phone` VARCHAR(128) COMMENT '发布人手机号',
  `publish_time` DATETIME COMMENT '记录发布时间' ,
  `text` TEXT COMMENT '文本内容',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '敏感词过滤日志表';
-- END